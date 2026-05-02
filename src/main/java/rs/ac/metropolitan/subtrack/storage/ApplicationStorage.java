package rs.ac.metropolitan.subtrack.storage;

import rs.ac.metropolitan.subtrack.model.AuditLog;
import rs.ac.metropolitan.subtrack.model.Category;
import rs.ac.metropolitan.subtrack.model.PaymentRecord;
import rs.ac.metropolitan.subtrack.model.Provider;
import rs.ac.metropolitan.subtrack.model.RenewalReminder;
import rs.ac.metropolitan.subtrack.model.Subscription;
import rs.ac.metropolitan.subtrack.model.UserProfile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.ArrayList;
import java.util.List;

@Component
@ApplicationScope
public class ApplicationStorage {

    private final List<Subscription> subscriptions = new ArrayList<>();
    private final List<Category> categories = new ArrayList<>();
    private final List<Provider> providers = new ArrayList<>();
    private final List<PaymentRecord> payments = new ArrayList<>();
    private final List<RenewalReminder> reminders = new ArrayList<>();
    private final List<AuditLog> auditLogs = new ArrayList<>();

    private UserProfile userProfile = new UserProfile(
            1L,
            "Demo korisnik",
            "demo@example.com",
            "EUR",
            50.00
    );

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Provider> getProviders() {
        return providers;
    }

    public List<PaymentRecord> getPayments() {
        return payments;
    }

    public List<RenewalReminder> getReminders() {
        return reminders;
    }

    public List<AuditLog> getAuditLogs() {
        return auditLogs;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
