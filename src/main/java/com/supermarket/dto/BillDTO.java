package com.supermarket.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BillDTO {

    private Long id;
    private String billNumber;
    private String customerName;
    private String customerPhone;
    private BigDecimal totalAmount;
    private BigDecimal totalTax;
    private BigDecimal discount;
    private BigDecimal finalAmount;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private List<BillItemDTO> items = new ArrayList<>();

    // Constructors
    public BillDTO() {
    }

    public BillDTO(Long id, String billNumber, String customerName, String customerPhone,
            BigDecimal totalAmount, BigDecimal totalTax, BigDecimal discount,
            BigDecimal finalAmount, String paymentMethod, LocalDateTime createdAt) {
        this.id = id;
        this.billNumber = billNumber;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.totalAmount = totalAmount;
        this.totalTax = totalTax;
        this.discount = discount;
        this.finalAmount = finalAmount;
        this.paymentMethod = paymentMethod;
        this.createdAt = createdAt;
    }

    // Getters and Setters for BillDTO
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<BillItemDTO> getItems() {
        return items;
    }

    public void setItems(List<BillItemDTO> items) {
        this.items = items;
    }

    // Inner class for Create Bill Request
    public static class CreateBillRequest {

        private String customerName;
        private String customerPhone;
        private String paymentMethod;
        private BigDecimal discount;
        private List<CreateBillItemRequest> items = new ArrayList<>();

        // Getters and Setters
        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerPhone() {
            return customerPhone;
        }

        public void setCustomerPhone(String customerPhone) {
            this.customerPhone = customerPhone;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public BigDecimal getDiscount() {
            return discount;
        }

        public void setDiscount(BigDecimal discount) {
            this.discount = discount;
        }

        public List<CreateBillItemRequest> getItems() {
            return items;
        }

        public void setItems(List<CreateBillItemRequest> items) {
            this.items = items;
        }
    }

    // Inner class for Create Bill Item Request
    public static class CreateBillItemRequest {

        private Long productId;
        private Integer quantity;

        // Getters and Setters
        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

    // Inner class for Bill Item DTO
    public static class BillItemDTO {

        private Long productId;
        private String productName;
        private String productCode;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal taxPercentage;
        private BigDecimal totalPrice;
        private BigDecimal totalTax;

        // Getters and Setters
        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }

        public BigDecimal getTaxPercentage() {
            return taxPercentage;
        }

        public void setTaxPercentage(BigDecimal taxPercentage) {
            this.taxPercentage = taxPercentage;
        }

        public BigDecimal getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
        }

        public BigDecimal getTotalTax() {
            return totalTax;
        }

        public void setTotalTax(BigDecimal totalTax) {
            this.totalTax = totalTax;
        }
    }
}
