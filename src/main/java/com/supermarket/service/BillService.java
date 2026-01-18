package com.supermarket.service;

import com.supermarket.dto.BillDTO;
import com.supermarket.dto.BillDTO.CreateBillRequest;
import java.io.IOException;
import java.util.List;

public interface BillService {

    BillDTO createBill(CreateBillRequest request);

    BillDTO getBillById(Long id);

    BillDTO getBillByNumber(String billNumber);

    List<BillDTO> getAllBills();

    List<BillDTO> getBillsByCustomerName(String customerName);

    List<BillDTO> getBillsByDateRange(String startDate, String endDate);

    void deleteBill(Long id);

    BillDTO generateBillDetails(Long id);

    byte[] generateBillPdf(Long billId) throws IOException;
}
