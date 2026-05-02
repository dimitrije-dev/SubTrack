package rs.ac.metropolitan.subtrack.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private String serviceName;
    private String category;
    private String iconClass;

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
        this.serviceName = name;
    }

    public Subscription(Long id,
                        String serviceName,
                        String category,
                        BigDecimal price,
                        String billingCycle,
                        LocalDate nextRenewalDate,
                        String status,
                        String iconClass) {
        this.id = id;
        this.name = serviceName;
        this.serviceName = serviceName;
        this.category = category;
        this.price = price != null ? price.doubleValue() : 0.0;
        this.billingCycle = "YEARLY".equalsIgnoreCase(billingCycle) ? BillingCycle.YEARLY : BillingCycle.MONTHLY;
        this.startDate = LocalDate.now().minusMonths(1);
        this.nextRenewalDate = nextRenewalDate;
        this.status = parseStatus(status);
        this.iconClass = iconClass;
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
        this.serviceName = name;
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

    public BigDecimal getPriceAmount() {
        return BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
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

    public String getServiceName() {
        return serviceName != null ? serviceName : name;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
        this.name = serviceName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
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

    private SubscriptionStatus parseStatus(String statusValue) {
        if (statusValue == null) {
            return SubscriptionStatus.ACTIVE;
        }
        try {
            return SubscriptionStatus.valueOf(statusValue.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return SubscriptionStatus.ACTIVE;
        }
    }
}
