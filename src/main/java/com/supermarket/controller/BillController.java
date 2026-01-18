package com.supermarket.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.supermarket.dto.BillDTO;
import com.supermarket.dto.BillDTO.CreateBillRequest;

import com.supermarket.service.BillService;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin(origins = "http://localhost:3000")
public class BillController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @Autowired
    private BillService billService;

    @PostMapping
    public ResponseEntity<BillDTO> createBill(
            @Valid @RequestBody CreateBillRequest request) {

        BillDTO createdBill = billService.createBill(request);
        return new ResponseEntity<>(createdBill, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillDTO> getBillById(@PathVariable Long id) {
        return ResponseEntity.ok(billService.getBillById(id));
    }

    @GetMapping("/number/{billNumber}")
    public ResponseEntity<BillDTO> getBillByNumber(@PathVariable String billNumber) {
        return ResponseEntity.ok(billService.getBillByNumber(billNumber));
    }

    @GetMapping
    public ResponseEntity<List<BillDTO>> getAllBills() {
        return ResponseEntity.ok(billService.getAllBills());
    }

    @GetMapping("/search/customer")
    public ResponseEntity<List<BillDTO>> getBillsByCustomerName(
            @RequestParam String customerName) {
        return ResponseEntity.ok(
                billService.getBillsByCustomerName(customerName)
        );
    }

    @GetMapping("/search/date-range")
    public ResponseEntity<List<BillDTO>> getBillsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return ResponseEntity.ok(
                billService.getBillsByDateRange(startDate, endDate)
        );
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadBillPdf(@PathVariable Long id)
            throws IOException {

        byte[] pdfBytes = billService.generateBillPdf(id);

        BillDTO bill = billService.getBillById(id);
        String filename = "Bill_" + bill.getBillNumber() + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", filename);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", true);
        return ResponseEntity.ok(response);
    }
}
