package rs.ac.metropolitan.subtrack.service;

import org.springframework.stereotype.Service;
import rs.ac.metropolitan.subtrack.model.ActivityLog;
import rs.ac.metropolitan.subtrack.model.Budget;
import rs.ac.metropolitan.subtrack.model.Category;
import rs.ac.metropolitan.subtrack.model.RenewalReminder;
import rs.ac.metropolitan.subtrack.model.Subscription;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class DashboardService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d, yyyy");

    private final SubscriptionService subscriptionService;

    public DashboardService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    public BigDecimal getTotalMonthlySpend() {
        return BigDecimal.valueOf(256.58).setScale(2, RoundingMode.HALF_UP);
    }

    public int getActiveSubscriptionsCount() {
        return 18;
    }

    public int getUpcomingRenewalsCount() {
        return 5;
    }

    public BigDecimal getAnnualProjection() {
        return getTotalMonthlySpend().multiply(BigDecimal.valueOf(12)).setScale(2, RoundingMode.HALF_UP);
    }

    public List<MonthlySpendBar> getMonthlySpendBars() {
        List<MonthlySpendBar> bars = new ArrayList<>();
        bars.add(new MonthlySpendBar("Dec 2024", BigDecimal.valueOf(198.35), 58));
        bars.add(new MonthlySpendBar("Jan 2025", BigDecimal.valueOf(212.20), 62));
        bars.add(new MonthlySpendBar("Feb 2025", BigDecimal.valueOf(224.40), 67));
        bars.add(new MonthlySpendBar("Mar 2025", BigDecimal.valueOf(236.85), 72));
        bars.add(new MonthlySpendBar("Apr 2025", BigDecimal.valueOf(248.67), 78));
        bars.add(new MonthlySpendBar("May 2025", getTotalMonthlySpend(), 86));
        return bars;
    }

    public List<Subscription> getSubscriptionsForTable() {
        List<Subscription> subscriptions = new ArrayList<>();
        subscriptions.add(new Subscription(1L, "Netflix", "Entertainment", BigDecimal.valueOf(15.49), "MONTHLY", LocalDate.of(2025, 5, 30), "ACTIVE", "icon-netflix"));
        subscriptions.add(new Subscription(2L, "Spotify", "Music", BigDecimal.valueOf(10.99), "MONTHLY", LocalDate.of(2025, 6, 1), "ACTIVE", "icon-spotify"));
        subscriptions.add(new Subscription(3L, "Adobe Creative Cloud", "Productivity", BigDecimal.valueOf(54.99), "MONTHLY", LocalDate.of(2025, 5, 28), "ACTIVE", "icon-adobe"));
        subscriptions.add(new Subscription(4L, "Dropbox", "Cloud Storage", BigDecimal.valueOf(11.99), "MONTHLY", LocalDate.of(2025, 6, 10), "ACTIVE", "icon-dropbox"));
        subscriptions.add(new Subscription(5L, "Notion", "Productivity", BigDecimal.valueOf(8.00), "MONTHLY", LocalDate.of(2025, 6, 15), "ACTIVE", "icon-notion"));
        subscriptions.add(new Subscription(6L, "Google One", "Cloud Storage", BigDecimal.valueOf(1.99), "MONTHLY", LocalDate.of(2025, 6, 3), "ACTIVE", "icon-google"));
        return subscriptions;
    }

    public List<RenewalReminder> getUpcomingRenewals() {
        List<RenewalReminder> reminders = new ArrayList<>();
        reminders.add(new RenewalReminder(1L, "Adobe Creative Cloud", LocalDate.of(2025, 5, 28), BigDecimal.valueOf(54.99), 3));
        reminders.add(new RenewalReminder(2L, "Netflix", LocalDate.of(2025, 5, 30), BigDecimal.valueOf(15.49), 5));
        reminders.add(new RenewalReminder(3L, "Google One", LocalDate.of(2025, 6, 3), BigDecimal.valueOf(1.99), 9));
        reminders.add(new RenewalReminder(4L, "Dropbox", LocalDate.of(2025, 6, 10), BigDecimal.valueOf(11.99), 16));
        reminders.add(new RenewalReminder(5L, "Notion", LocalDate.of(2025, 6, 15), BigDecimal.valueOf(8.00), 21));
        return reminders;
    }

    public List<Category> getSpendingCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1L, "Productivity", 38, "cat-productivity"));
        categories.add(new Category(2L, "Entertainment", 27, "cat-entertainment"));
        categories.add(new Category(3L, "Cloud Storage", 20, "cat-cloud"));
        categories.add(new Category(4L, "Music", 15, "cat-music"));
        return categories;
    }

    public List<ActivityLog> getRecentActivities() {
        List<ActivityLog> activities = new ArrayList<>();
        activities.add(new ActivityLog(1L, "Adobe Creative Cloud", "Subscription renewed", "2h ago"));
        activities.add(new ActivityLog(2L, "Netflix", "Payment processed", "1d ago"));
        activities.add(new ActivityLog(3L, "Spotify", "Reminder set for Jun 1", "2d ago"));
        activities.add(new ActivityLog(4L, "Notion", "Plan updated to Personal Pro", "3d ago"));
        return activities;
    }

    public Budget getBudgetStatus() {
        BigDecimal monthlyBudget = BigDecimal.valueOf(300.00).setScale(2, RoundingMode.HALF_UP);
        BigDecimal spent = getTotalMonthlySpend();
        BigDecimal remaining = monthlyBudget.subtract(spent).setScale(2, RoundingMode.HALF_UP);
        int percentage = spent.divide(monthlyBudget, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .intValue();

        return new Budget(monthlyBudget, spent, remaining, percentage);
    }

    public String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    public List<Subscription> getLiveUpcomingRenewals() {
        return subscriptionService.getUpcomingRenewals(5).stream()
                .sorted(Comparator.comparing(Subscription::getNextRenewalDate))
                .toList();
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
}
