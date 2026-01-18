package com.supermarket.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String billNumber;
    private String customerName;
    private String customerPhone;
    private BigDecimal totalAmount;
    private BigDecimal totalTax;
    private BigDecimal discount;
    private BigDecimal finalAmount;
    private String paymentMethod;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillItem> items = new ArrayList<>();

    @PrePersist
    private void generateBillNumber() {
        if (this.billNumber == null) {
            this.billNumber = "BILL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }

    // Getters and Setters
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

    public List<BillItem> getItems() {
        return items;
    }

    public void setItems(List<BillItem> items) {
        this.items = items;
    }

    public void addItem(BillItem item) {
        items.add(item);
        item.setBill(this);
    }

    public void calculateTotals() {
        this.totalAmount = BigDecimal.ZERO;
        this.totalTax = BigDecimal.ZERO;

        for (BillItem item : items) {
            if (item.getTotalPrice() != null) {
                this.totalAmount = this.totalAmount.add(item.getTotalPrice());
            }
            if (item.getTotalTax() != null) {
                this.totalTax = this.totalTax.add(item.getTotalTax());
            }
        }

        this.finalAmount = this.totalAmount.add(this.totalTax);
        if (this.discount != null) {
            this.finalAmount = this.finalAmount.subtract(this.discount);
        }

        if (this.finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            this.finalAmount = BigDecimal.ZERO;
        }
    }
}
