package rs.ac.metropolitan.subtrack.service;

import org.springframework.stereotype.Service;
import rs.ac.metropolitan.subtrack.model.Category;
import rs.ac.metropolitan.subtrack.model.Subscription;
import rs.ac.metropolitan.subtrack.model.SubscriptionStatus;
import rs.ac.metropolitan.subtrack.storage.ApplicationStorage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    private final ApplicationStorage storage;

    public SubscriptionService(ApplicationStorage storage) {
        this.storage = storage;
    }

    public List<Subscription> getAllSubscriptions() {
        return storage.getSubscriptions();
    }

    public List<Subscription> getDashboardSubscriptions() {
        Map<Long, String> categoryNames = new HashMap<>();
        for (Category category : storage.getCategories()) {
            categoryNames.put(category.getId(), category.getName());
        }

        return storage.getSubscriptions().stream()
                .map(subscription -> {
                    if (subscription.getServiceName() == null || subscription.getServiceName().isBlank()) {
                        subscription.setServiceName(subscription.getName());
                    }
                    if (subscription.getCategory() == null && subscription.getCategoryId() != null) {
                        subscription.setCategory(categoryNames.getOrDefault(subscription.getCategoryId(), "General"));
                    }
                    if (subscription.getIconClass() == null || subscription.getIconClass().isBlank()) {
                        subscription.setIconClass(resolveIconClass(subscription.getServiceName()));
                    }
                    return subscription;
                })
                .collect(Collectors.toList());
    }

    public List<Subscription> getUpcomingRenewals(int limit) {
        LocalDate today = LocalDate.now();
        return getDashboardSubscriptions().stream()
                .filter(subscription -> subscription.getStatus() == SubscriptionStatus.ACTIVE)
                .filter(subscription -> subscription.getNextRenewalDate() != null)
                .filter(subscription -> !subscription.getNextRenewalDate().isBefore(today))
                .sorted(Comparator.comparing(Subscription::getNextRenewalDate))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Optional<Subscription> getSubscriptionById(Long id) {
        return storage.getSubscriptions().stream()
                .filter(subscription -> subscription.getId().equals(id))
                .findFirst();
    }

    public Subscription createSubscription(Subscription subscription) {
        long nextId = storage.getSubscriptions().stream()
                .mapToLong(existing -> existing.getId() != null ? existing.getId() : 0L)
                .max()
                .orElse(0L) + 1;

        subscription.setId(nextId);
        if (subscription.getServiceName() == null || subscription.getServiceName().isBlank()) {
            subscription.setServiceName(subscription.getName());
        }
        if (subscription.getName() == null || subscription.getName().isBlank()) {
            subscription.setName(subscription.getServiceName());
        }
        if (subscription.getStatus() == null) {
            subscription.setStatus(SubscriptionStatus.ACTIVE);
        }
        if (subscription.getBillingCycle() == null) {
            subscription.setBillingCycle(rs.ac.metropolitan.subtrack.model.BillingCycle.MONTHLY);
        }
        if (subscription.getNextRenewalDate() == null) {
            subscription.setNextRenewalDate(LocalDate.now().plusMonths(1));
        }
        if (subscription.getStartDate() == null) {
            subscription.setStartDate(LocalDate.now());
        }

        storage.getSubscriptions().add(subscription);
        return subscription;
    }

    public boolean updateSubscription(Long id, Subscription updated) {
        Optional<Subscription> existingOptional = getSubscriptionById(id);
        if (existingOptional.isEmpty()) {
            return false;
        }

        Subscription existing = existingOptional.get();
        existing.setServiceName(updated.getServiceName());
        existing.setName(updated.getServiceName());
        existing.setDescription(updated.getDescription());
        existing.setCategory(updated.getCategory());
        existing.setPrice(updated.getPrice());
        existing.setBillingCycle(updated.getBillingCycle());
        existing.setNextRenewalDate(updated.getNextRenewalDate());
        existing.setStatus(updated.getStatus());
        existing.setIconClass(updated.getIconClass());

        return true;
    }

    public boolean deleteSubscription(Long id) {
        return storage.getSubscriptions().removeIf(subscription -> subscription.getId().equals(id));
    }

    public BigDecimal getMonthlySpend() {
        double total = getDashboardSubscriptions().stream()
                .filter(subscription -> subscription.getStatus() == SubscriptionStatus.ACTIVE)
                .mapToDouble(Subscription::getMonthlyCost)
                .sum();
        return BigDecimal.valueOf(total).setScale(2, java.math.RoundingMode.HALF_UP);
    }

    public int getDaysUntilRenewal(Subscription subscription) {
        if (subscription.getNextRenewalDate() == null) {
            return Integer.MAX_VALUE;
        }
        long days = ChronoUnit.DAYS.between(LocalDate.now(), subscription.getNextRenewalDate());
        return (int) days;
    }

    private String resolveIconClass(String serviceName) {
        if (serviceName == null) {
            return "icon-service";
        }
        String value = serviceName.toLowerCase();
        if (value.contains("netflix")) {
            return "icon-netflix";
        }
        if (value.contains("spotify")) {
            return "icon-spotify";
        }
        if (value.contains("adobe")) {
            return "icon-adobe";
        }
        if (value.contains("github")) {
            return "icon-github";
        }
        if (value.contains("google")) {
            return "icon-google";
        }
        if (value.contains("dropbox")) {
            return "icon-dropbox";
        }
        if (value.contains("notion")) {
            return "icon-notion";
        }
        return "icon-service";
    }
}
