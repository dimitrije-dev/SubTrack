package rs.ac.metropolitan.subtrack.service;

import org.springframework.stereotype.Service;
import rs.ac.metropolitan.subtrack.model.ActivityLog;
import rs.ac.metropolitan.subtrack.model.Budget;
import rs.ac.metropolitan.subtrack.model.Category;
import rs.ac.metropolitan.subtrack.model.PaymentRecord;
import rs.ac.metropolitan.subtrack.model.RenewalReminder;
import rs.ac.metropolitan.subtrack.model.Subscription;
import rs.ac.metropolitan.subtrack.model.SubscriptionStatus;
import rs.ac.metropolitan.subtrack.model.UserProfile;
import rs.ac.metropolitan.subtrack.storage.ApplicationStorage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH);
    private static final List<String> CATEGORY_COLOR_CLASSES = List.of(
            "cat-productivity",
            "cat-entertainment",
            "cat-cloud",
            "cat-music"
    );

    private final ApplicationStorage storage;
    private final SubscriptionService subscriptionService;
    private final PaymentService paymentService;
    private final ReminderService reminderService;

    public DashboardService(ApplicationStorage storage,
                            SubscriptionService subscriptionService,
                            PaymentService paymentService,
                            ReminderService reminderService) {
        this.storage = storage;
        this.subscriptionService = subscriptionService;
        this.paymentService = paymentService;
        this.reminderService = reminderService;
    }

    public BigDecimal getTotalMonthlySpend() {
        double monthly = subscriptionService.getDashboardSubscriptions().stream()
                .filter(subscription -> subscription.getStatus() == SubscriptionStatus.ACTIVE)
                .mapToDouble(Subscription::getMonthlyCost)
                .sum();

        return BigDecimal.valueOf(monthly).setScale(2, RoundingMode.HALF_UP);
    }

    public int getActiveSubscriptionsCount() {
        return (int) subscriptionService.getDashboardSubscriptions().stream()
                .filter(subscription -> subscription.getStatus() == SubscriptionStatus.ACTIVE)
                .count();
    }

    public int getUpcomingRenewalsCount() {
        LocalDate today = LocalDate.now();
        LocalDate threshold = today.plusDays(30);

        return (int) subscriptionService.getDashboardSubscriptions().stream()
                .filter(subscription -> subscription.getStatus() == SubscriptionStatus.ACTIVE)
                .filter(subscription -> subscription.getNextRenewalDate() != null)
                .filter(subscription -> !subscription.getNextRenewalDate().isBefore(today))
                .filter(subscription -> !subscription.getNextRenewalDate().isAfter(threshold))
                .count();
    }

    public BigDecimal getAnnualProjection() {
        return getTotalMonthlySpend()
                .multiply(BigDecimal.valueOf(12))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public List<MonthlySpendBar> getMonthlySpendBars() {
        List<PaymentRecord> payments = paymentService.getAllPayments();
        Map<YearMonth, BigDecimal> monthlyAmountMap = new LinkedHashMap<>();

        YearMonth current = YearMonth.now();
        YearMonth start = current.minusMonths(5);
        for (int i = 0; i < 6; i++) {
            YearMonth bucket = start.plusMonths(i);
            monthlyAmountMap.put(bucket, BigDecimal.ZERO);
        }

        for (PaymentRecord payment : payments) {
            if (payment.getPaymentDate() == null) {
                continue;
            }
            YearMonth paymentMonth = YearMonth.from(payment.getPaymentDate());
            if (monthlyAmountMap.containsKey(paymentMonth)) {
                BigDecimal amount = monthlyAmountMap.get(paymentMonth)
                        .add(BigDecimal.valueOf(payment.getAmount()));
                monthlyAmountMap.put(paymentMonth, amount);
            }
        }

        YearMonth currentMonth = YearMonth.now();
        if (monthlyAmountMap.getOrDefault(currentMonth, BigDecimal.ZERO).compareTo(BigDecimal.ZERO) == 0) {
            monthlyAmountMap.put(currentMonth, getTotalMonthlySpend());
        }

        BigDecimal max = monthlyAmountMap.values().stream()
                .max(Comparator.naturalOrder())
                .orElse(BigDecimal.ONE);

        if (max.compareTo(BigDecimal.ZERO) == 0) {
            max = BigDecimal.ONE;
        }

        List<MonthlySpendBar> bars = new ArrayList<>();
        for (Map.Entry<YearMonth, BigDecimal> entry : monthlyAmountMap.entrySet()) {
            BigDecimal amount = entry.getValue().setScale(2, RoundingMode.HALF_UP);
            int height = amount
                    .divide(max, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(0, RoundingMode.HALF_UP)
                    .intValue();

            height = Math.max(height, 12);
            bars.add(new MonthlySpendBar(entry.getKey().format(MONTH_FORMATTER), amount, height));
        }

        return bars;
    }

    public List<Subscription> getSubscriptionsForTable() {
        return subscriptionService.getDashboardSubscriptions().stream()
                .filter(subscription -> subscription.getStatus() == SubscriptionStatus.ACTIVE)
                .sorted(Comparator.comparing(Subscription::getNextRenewalDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    public List<RenewalReminder> getUpcomingRenewals() {
        List<RenewalReminder> reminders = reminderService.getUpcomingReminders(10);
        List<RenewalReminder> enriched = reminders.stream()
                .map(this::enrichReminder)
                .sorted(Comparator.comparing(RenewalReminder::getRenewalDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .limit(5)
                .collect(Collectors.toList());

        if (!enriched.isEmpty()) {
            return enriched;
        }

        return subscriptionService.getUpcomingRenewals(5).stream()
                .map(this::mapSubscriptionToReminder)
                .collect(Collectors.toList());
    }

    public List<Category> getSpendingCategories() {
        List<Subscription> active = subscriptionService.getDashboardSubscriptions().stream()
                .filter(subscription -> subscription.getStatus() == SubscriptionStatus.ACTIVE)
                .collect(Collectors.toList());

        Map<String, BigDecimal> totals = new LinkedHashMap<>();
        for (Subscription subscription : active) {
            String categoryName = subscription.getCategory() != null && !subscription.getCategory().isBlank()
                    ? subscription.getCategory()
                    : "General";

            BigDecimal existing = totals.getOrDefault(categoryName, BigDecimal.ZERO);
            totals.put(categoryName, existing.add(BigDecimal.valueOf(subscription.getMonthlyCost())));
        }

        BigDecimal monthlyTotal = totals.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (monthlyTotal.compareTo(BigDecimal.ZERO) == 0) {
            return defaultCategoryPlaceholders();
        }

        List<Map.Entry<String, BigDecimal>> sorted = totals.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(4)
                .collect(Collectors.toList());

        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < sorted.size(); i++) {
            Map.Entry<String, BigDecimal> entry = sorted.get(i);
            int percentage = entry.getValue()
                    .divide(monthlyTotal, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(0, RoundingMode.HALF_UP)
                    .intValue();

            categories.add(new Category(
                    (long) (i + 1),
                    entry.getKey(),
                    percentage,
                    CATEGORY_COLOR_CLASSES.get(i % CATEGORY_COLOR_CLASSES.size())
            ));
        }

        return categories;
    }

    public List<ActivityLog> getRecentActivities() {
        List<ActivityLogEntry> entries = new ArrayList<>();

        for (PaymentRecord payment : paymentService.getAllPayments()) {
            if (payment.getPaymentDate() == null) {
                continue;
            }

            String service = resolveSubscriptionName(payment.getSubscriptionId());
            String description = "Payment processed";
            String time = toPastTimeLabel(payment.getPaymentDate());
            entries.add(new ActivityLogEntry(payment.getPaymentDate(), new ActivityLog(payment.getId(), service, description, time)));
        }

        for (RenewalReminder reminder : reminderService.getAllReminders()) {
            LocalDate date = effectiveReminderDate(reminder);
            if (date == null) {
                continue;
            }

            String service = reminder.getServiceName();
            if (service == null || service.isBlank()) {
                service = resolveSubscriptionName(reminder.getSubscriptionId());
            }

            String description = reminder.getMessage() != null && !reminder.getMessage().isBlank()
                    ? reminder.getMessage()
                    : "Reminder scheduled";
            String time = toRelativeTimeLabel(date);
            entries.add(new ActivityLogEntry(date, new ActivityLog(reminder.getId(), service, description, time)));
        }

        return entries.stream()
                .sorted((a, b) -> b.date.compareTo(a.date))
                .map(entry -> entry.log)
                .limit(4)
                .collect(Collectors.toList());
    }

    public Budget getBudgetStatus() {
        UserProfile profile = storage.getUserProfile();
        BigDecimal monthlyBudget = BigDecimal.valueOf(profile != null ? profile.getMonthlyBudget() : 300.00)
                .setScale(2, RoundingMode.HALF_UP);

        if (monthlyBudget.compareTo(BigDecimal.ZERO) <= 0) {
            monthlyBudget = BigDecimal.valueOf(300.00).setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal spent = getTotalMonthlySpend();
        BigDecimal remaining = monthlyBudget.subtract(spent).setScale(2, RoundingMode.HALF_UP);

        int percentage = spent.divide(monthlyBudget, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .intValue();

        percentage = Math.max(0, Math.min(percentage, 100));

        return new Budget(monthlyBudget, spent, remaining, percentage);
    }

    public String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    public List<Subscription> getLiveUpcomingRenewals() {
        return subscriptionService.getUpcomingRenewals(5).stream()
                .sorted(Comparator.comparing(Subscription::getNextRenewalDate))
                .collect(Collectors.toList());
    }

    private RenewalReminder enrichReminder(RenewalReminder original) {
        RenewalReminder copy = new RenewalReminder();
        copy.setId(original.getId());
        copy.setSubscriptionId(original.getSubscriptionId());
        copy.setMessage(original.getMessage());
        copy.setCompleted(original.isCompleted());

        LocalDate date = effectiveReminderDate(original);
        Optional<Subscription> linkedSubscription = resolveSubscription(original.getSubscriptionId());
        if (date == null && linkedSubscription.isPresent()) {
            date = linkedSubscription.get().getNextRenewalDate();
        }

        copy.setRenewalDate(date);
        copy.setReminderDate(date);

        String serviceName = original.getServiceName();
        if ((serviceName == null || serviceName.isBlank()) && linkedSubscription.isPresent()) {
            serviceName = linkedSubscription.get().getServiceName();
        }
        copy.setServiceName(serviceName != null ? serviceName : "Unknown Service");

        if (original.getPrice() != null) {
            copy.setPrice(original.getPrice().setScale(2, RoundingMode.HALF_UP));
        } else if (linkedSubscription.isPresent()) {
            copy.setPrice(BigDecimal.valueOf(linkedSubscription.get().getPrice()).setScale(2, RoundingMode.HALF_UP));
        } else {
            copy.setPrice(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        }

        if (date != null) {
            copy.setDaysLeft((int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), date));
        }

        return copy;
    }

    private RenewalReminder mapSubscriptionToReminder(Subscription subscription) {
        LocalDate renewalDate = subscription.getNextRenewalDate();
        int daysLeft = renewalDate != null
                ? (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), renewalDate)
                : 0;

        return new RenewalReminder(
                subscription.getId(),
                subscription.getServiceName(),
                renewalDate,
                BigDecimal.valueOf(subscription.getPrice()).setScale(2, RoundingMode.HALF_UP),
                daysLeft
        );
    }

    private Optional<Subscription> resolveSubscription(Long subscriptionId) {
        if (subscriptionId == null) {
            return Optional.empty();
        }
        return subscriptionService.getDashboardSubscriptions().stream()
                .filter(subscription -> subscription.getId().equals(subscriptionId))
                .findFirst();
    }

    private String resolveSubscriptionName(Long subscriptionId) {
        return resolveSubscription(subscriptionId)
                .map(Subscription::getServiceName)
                .orElse("Unknown Service");
    }

    private LocalDate effectiveReminderDate(RenewalReminder reminder) {
        return reminder.getRenewalDate() != null ? reminder.getRenewalDate() : reminder.getReminderDate();
    }

    private String toPastTimeLabel(LocalDate date) {
        long days = java.time.temporal.ChronoUnit.DAYS.between(date, LocalDate.now());
        if (days <= 0) {
            return "today";
        }
        if (days == 1) {
            return "1d ago";
        }
        return days + "d ago";
    }

    private String toRelativeTimeLabel(LocalDate date) {
        long days = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), date);
        if (days < 0) {
            return Math.abs(days) + "d ago";
        }
        if (days == 0) {
            return "today";
        }
        return "in " + days + "d";
    }

    private List<Category> defaultCategoryPlaceholders() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1L, "Productivity", 0, "cat-productivity"));
        categories.add(new Category(2L, "Entertainment", 0, "cat-entertainment"));
        categories.add(new Category(3L, "Cloud Storage", 0, "cat-cloud"));
        categories.add(new Category(4L, "Music", 0, "cat-music"));
        return categories;
    }

    public static class MonthlySpendBar {
        private final String month;
        private final BigDecimal amount;
        private final int heightPercent;

        public MonthlySpendBar(String month, BigDecimal amount, int heightPercent) {
            this.month = month;
            this.amount = amount;
            this.heightPercent = heightPercent;
        }

        public String getMonth() {
            return month;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public int getHeightPercent() {
            return heightPercent;
        }
    }

    private static class ActivityLogEntry {
        private final LocalDate date;
        private final ActivityLog log;

        private ActivityLogEntry(LocalDate date, ActivityLog log) {
            this.date = date;
            this.log = log;
        }
    }
}
