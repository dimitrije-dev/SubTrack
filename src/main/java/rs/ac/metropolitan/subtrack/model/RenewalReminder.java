package rs.ac.metropolitan.subtrack.model;

import java.time.LocalDate;

public class RenewalReminder {

    private Long id;
    private Long subscriptionId;
    private LocalDate reminderDate;
    private String message;
    private boolean completed;

    public RenewalReminder() {
    }

    public RenewalReminder(Long id, Long subscriptionId, LocalDate reminderDate, String message, boolean completed) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.reminderDate = reminderDate;
        this.message = message;
        this.completed = completed;
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
}
