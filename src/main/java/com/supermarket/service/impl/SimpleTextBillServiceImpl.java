package com.supermarket.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.supermarket.dto.BillDTO;
import com.supermarket.dto.BillDTO.BillItemDTO;
import com.supermarket.service.SimpleTextBillService;

@Service
public class SimpleTextBillServiceImpl implements SimpleTextBillService {

    @Override
    public byte[] generateBill(BillDTO billDTO) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);

            document.open();

            // Add title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("SUPERMARKET BILL", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Add bill info
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 10);

            document.add(new Paragraph("Bill Number: " + billDTO.getBillNumber(), normalFont));
            document.add(new Paragraph("Customer Name: " + billDTO.getCustomerName(), normalFont));
            document.add(new Paragraph("Customer Phone: " + billDTO.getCustomerPhone(), normalFont));
            document.add(new Paragraph("Date: "
                    + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.sql.Timestamp.valueOf(billDTO.getCreatedAt())),
                    normalFont));
            document.add(new Paragraph(" "));

            // Add table for items
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);

            // Add table headers
            String[] headers = {"Product", "Code", "Qty", "Price", "Tax%", "Total"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, normalFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            // Add items
            for (BillItemDTO item : billDTO.getItems()) {
                table.addCell(new PdfPCell(new Phrase(item.getProductName(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(item.getProductCode(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(item.getQuantity().toString(), normalFont)));
                table.addCell(new PdfPCell(new Phrase("₹" + item.getUnitPrice(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(item.getTaxPercentage() + "%", normalFont)));
                table.addCell(new PdfPCell(new Phrase("₹" + item.getTotalPrice(), normalFont)));
            }

            document.add(table);
            document.add(new Paragraph(" "));

            // Add totals
            document.add(new Paragraph("Subtotal: ₹" + billDTO.getTotalAmount(), normalFont));
            document.add(new Paragraph("Tax: ₹" + billDTO.getTotalTax(), normalFont));
            document.add(new Paragraph("Discount: ₹" + billDTO.getDiscount(), normalFont));

            Font totalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            document.add(new Paragraph("Total Amount: ₹" + billDTO.getFinalAmount(), totalFont));
            document.add(new Paragraph("Payment Method: " + billDTO.getPaymentMethod(), normalFont));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Thank you for shopping with us!", normalFont));

            document.close();
            return baos.toByteArray();
        } catch (DocumentException e) {
            throw new IOException("Error generating PDF", e);
        }
    }
}
