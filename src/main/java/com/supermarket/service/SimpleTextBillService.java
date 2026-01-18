package com.supermarket.service;

import com.supermarket.dto.BillDTO;
import java.io.IOException;

public interface SimpleTextBillService {

    byte[] generateBill(BillDTO billDTO) throws IOException;
}
