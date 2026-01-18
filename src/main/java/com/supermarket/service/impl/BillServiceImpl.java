package com.supermarket.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermarket.dto.BillDTO;
import com.supermarket.dto.BillDTO.BillItemDTO;
import com.supermarket.dto.BillDTO.CreateBillRequest;
import com.supermarket.exception.ResourceNotFoundException;
import com.supermarket.model.Bill;
import com.supermarket.model.BillItem;
import com.supermarket.model.Product;
import com.supermarket.repository.BillRepository;
import com.supermarket.service.BillService;
import com.supermarket.service.ProductService;
import com.supermarket.service.SimpleTextBillService;

@Service
@Transactional
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private SimpleTextBillService simpleTextBillService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public BillDTO createBill(CreateBillRequest request) {
        Bill bill = new Bill();
        bill.setCustomerName(request.getCustomerName());
        bill.setCustomerPhone(request.getCustomerPhone());
        bill.setPaymentMethod(request.getPaymentMethod());
        bill.setDiscount(request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO);

        request.getItems().forEach(itemRequest -> {
            Product product = productService.getProductById(itemRequest.getProductId());

            if (product.getQuantity() < itemRequest.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
            }

            BillItem billItem = new BillItem(product, itemRequest.getQuantity());
            bill.addItem(billItem);

            productService.updateProductQuantity(product.getId(), -itemRequest.getQuantity());
        });

        bill.calculateTotals();
        Bill savedBill = billRepository.save(bill);
        return convertToDTO(savedBill);
    }

    @Override
    public BillDTO getBillById(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", "id", id));
        return convertToDTO(bill);
    }

    @Override
    public BillDTO getBillByNumber(String billNumber) {
        Bill bill = billRepository.findByBillNumber(billNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", "billNumber", billNumber));
        return convertToDTO(bill);
    }

    @Override
    public List<BillDTO> getAllBills() {
        return billRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillDTO> getBillsByCustomerName(String customerName) {
        return billRepository.findByCustomerNameContainingIgnoreCase(customerName)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BillDTO> getBillsByDateRange(String startDate, String endDate) {
        LocalDateTime start = LocalDate.parse(startDate, DATE_FORMATTER).atStartOfDay();
        LocalDateTime end = LocalDate.parse(endDate, DATE_FORMATTER).atTime(23, 59, 59);

        return billRepository.findByCreatedAtBetween(start, end)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBill(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", "id", id));

        restoreProductQuantities(bill);
        billRepository.delete(bill);
    }

    @Override
    public BillDTO generateBillDetails(Long id) {
        return getBillById(id);
    }

    @Override
    public byte[] generateBillPdf(Long billId) throws IOException {
        BillDTO bill = getBillById(billId);
        return simpleTextBillService.generateBill(bill);
    }

    private void restoreProductQuantities(Bill bill) {
        for (BillItem item : bill.getItems()) {
            if (item.getProduct() != null && item.getQuantity() > 0) {
                productService.updateProductQuantity(item.getProduct().getId(), item.getQuantity());
            }
        }
    }

    private BillDTO convertToDTO(Bill bill) {
        BillDTO dto = new BillDTO();
        dto.setId(bill.getId());
        dto.setBillNumber(bill.getBillNumber());
        dto.setCustomerName(bill.getCustomerName());
        dto.setCustomerPhone(bill.getCustomerPhone());
        dto.setTotalAmount(bill.getTotalAmount());
        dto.setTotalTax(bill.getTotalTax());
        dto.setDiscount(bill.getDiscount());
        dto.setFinalAmount(bill.getFinalAmount());
        dto.setPaymentMethod(bill.getPaymentMethod());
        dto.setCreatedAt(bill.getCreatedAt());

        dto.setItems(bill.getItems().stream()
                .map(this::convertToItemDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private BillItemDTO convertToItemDTO(BillItem item) {
        BillItemDTO dto = new BillItemDTO();
        if (item.getProduct() != null) {
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
            dto.setProductCode(item.getProduct().getCode());
        }
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setTaxPercentage(item.getTaxPercentage());
        dto.setTotalPrice(item.getTotalPrice());
        dto.setTotalTax(item.getTotalTax());
        return dto;
    }
}
