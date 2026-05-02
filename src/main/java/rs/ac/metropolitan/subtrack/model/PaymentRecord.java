package rs.ac.metropolitan.subtrack.model;

import java.time.LocalDate;

public class PaymentRecord {

    private Long id;
    private Long subscriptionId;
    private LocalDate paymentDate;
    private double amount;
    private String note;

    public PaymentRecord() {
    }

    public PaymentRecord(Long id, Long subscriptionId, LocalDate paymentDate, double amount, String note) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
