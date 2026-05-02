package rs.ac.metropolitan.subtrack.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RenewalReminder {

    private Long id;
    private Long subscriptionId;
    private LocalDate reminderDate;
    private String message;
    private boolean completed;
    private String serviceName;
    private LocalDate renewalDate;
    private BigDecimal price;
    private int daysLeft;

    public RenewalReminder() {
    }

    public RenewalReminder(Long id, Long subscriptionId, LocalDate reminderDate, String message, boolean completed) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.reminderDate = reminderDate;
        this.message = message;
        this.completed = completed;
    }

    public RenewalReminder(Long id, String serviceName, LocalDate renewalDate, BigDecimal price, int daysLeft) {
        this.id = id;
        this.serviceName = serviceName;
        this.renewalDate = renewalDate;
        this.price = price;
        this.daysLeft = daysLeft;
        this.reminderDate = renewalDate;
        this.message = serviceName + " renewal reminder";
        this.completed = false;
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

    public LocalDate getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void markAsCompleted() {
        this.completed = true;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public LocalDate getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(LocalDate renewalDate) {
        this.renewalDate = renewalDate;
        this.reminderDate = renewalDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
    }
}
