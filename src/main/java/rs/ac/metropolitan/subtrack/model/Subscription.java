package rs.ac.metropolitan.subtrack.model;

import java.time.LocalDate;

public class Subscription {

    private Long id;
    private String name;
    private String description;
    private double price;
    private BillingCycle billingCycle;
    private LocalDate startDate;
    private LocalDate nextRenewalDate;
    private SubscriptionStatus status;
    private Long providerId;
    private Long categoryId;

    public Subscription() {
    }

    public Subscription(Long id,
                        String name,
                        String description,
                        double price,
                        BillingCycle billingCycle,
                        LocalDate startDate,
                        LocalDate nextRenewalDate,
                        SubscriptionStatus status,
                        Long providerId,
                        Long categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.billingCycle = billingCycle;
        this.startDate = startDate;
        this.nextRenewalDate = nextRenewalDate;
        this.status = status;
        this.providerId = providerId;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public BillingCycle getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(BillingCycle billingCycle) {
        this.billingCycle = billingCycle;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getNextRenewalDate() {
        return nextRenewalDate;
    }

    public void setNextRenewalDate(LocalDate nextRenewalDate) {
        this.nextRenewalDate = nextRenewalDate;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public double getMonthlyCost() {
        if (billingCycle == BillingCycle.YEARLY) {
            return price / 12;
        }
        return price;
    }

    public double getYearlyCost() {
        if (billingCycle == BillingCycle.MONTHLY) {
            return price * 12;
        }
        return price;
    }

    public boolean isActive() {
        return status == SubscriptionStatus.ACTIVE;
    }

    public boolean isRenewingSoon() {
        if (nextRenewalDate == null) {
            return false;
        }

        LocalDate today = LocalDate.now();
        LocalDate sevenDaysFromNow = today.plusDays(7);

        return !nextRenewalDate.isBefore(today) && !nextRenewalDate.isAfter(sevenDaysFromNow);
    }
}
