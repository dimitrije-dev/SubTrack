package rs.ac.metropolitan.subtrack.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import rs.ac.metropolitan.subtrack.model.BillingCycle;
import rs.ac.metropolitan.subtrack.model.Category;
import rs.ac.metropolitan.subtrack.model.PaymentRecord;
import rs.ac.metropolitan.subtrack.model.Provider;
import rs.ac.metropolitan.subtrack.model.RenewalReminder;
import rs.ac.metropolitan.subtrack.model.Subscription;
import rs.ac.metropolitan.subtrack.model.SubscriptionStatus;
import rs.ac.metropolitan.subtrack.model.UserProfile;
import rs.ac.metropolitan.subtrack.storage.ApplicationStorage;

import java.time.LocalDate;

@Component
public class DataInitializer {

    private final ApplicationStorage storage;

    public DataInitializer(ApplicationStorage storage) {
        this.storage = storage;
    }

    @PostConstruct
    public void init() {
        if (!storage.getSubscriptions().isEmpty()
                || !storage.getCategories().isEmpty()
                || !storage.getProviders().isEmpty()) {
            return;
        }

        storage.setUserProfile(new UserProfile(
                1L,
                "Demo korisnik",
                "demo@example.com",
                "EUR",
                50.00
        ));

        storage.getCategories().add(new Category(
                1L,
                "Entertainment",
                "Streaming servisi za filmove, serije i video sadržaj"
        ));

        storage.getCategories().add(new Category(
                2L,
                "Music",
                "Muzičke platforme i audio streaming servisi"
        ));

        storage.getCategories().add(new Category(
                3L,
                "Productivity",
                "Alati za produktivnost, dizajn i organizaciju rada"
        ));

        storage.getCategories().add(new Category(
                4L,
                "Development Tools",
                "Alati za programiranje i razvoj softvera"
        ));

        storage.getCategories().add(new Category(
                5L,
                "Cloud Storage",
                "Cloud skladištenje i online backup servisi"
        ));

        storage.getProviders().add(new Provider(
                1L,
                "Netflix",
                "https://www.netflix.com",
                "support@netflix.com"
        ));

        storage.getProviders().add(new Provider(
                2L,
                "Spotify",
                "https://www.spotify.com",
                "support@spotify.com"
        ));

        storage.getProviders().add(new Provider(
                3L,
                "Adobe",
                "https://www.adobe.com",
                "support@adobe.com"
        ));

        storage.getProviders().add(new Provider(
                4L,
                "GitHub",
                "https://github.com",
                "support@github.com"
        ));

        storage.getProviders().add(new Provider(
                5L,
                "Google",
                "https://www.google.com",
                "support@google.com"
        ));

        storage.getSubscriptions().add(new Subscription(
                1L,
                "Netflix",
                "Streaming servis za filmove i serije",
                9.99,
                BillingCycle.MONTHLY,
                LocalDate.now().minusMonths(6),
                LocalDate.now().plusDays(5),
                SubscriptionStatus.ACTIVE,
                1L,
                1L
        ));

        storage.getSubscriptions().add(new Subscription(
                2L,
                "Spotify",
                "Muzički streaming servis",
                6.99,
                BillingCycle.MONTHLY,
                LocalDate.now().minusMonths(8),
                LocalDate.now().plusDays(12),
                SubscriptionStatus.ACTIVE,
                2L,
                2L
        ));

        storage.getSubscriptions().add(new Subscription(
                3L,
                "Adobe Creative Cloud",
                "Paket alata za dizajn, fotografiju i video produkciju",
                239.88,
                BillingCycle.YEARLY,
                LocalDate.now().minusYears(1),
                LocalDate.now().plusDays(3),
                SubscriptionStatus.ACTIVE,
                3L,
                3L
        ));

        storage.getSubscriptions().add(new Subscription(
                4L,
                "GitHub Copilot",
                "AI alat za asistenciju pri programiranju",
                10.00,
                BillingCycle.MONTHLY,
                LocalDate.now().minusMonths(3),
                LocalDate.now().plusDays(20),
                SubscriptionStatus.ACTIVE,
                4L,
                4L
        ));

        storage.getSubscriptions().add(new Subscription(
                5L,
                "Google Drive",
                "Cloud skladištenje za fajlove i dokumente",
                19.99,
                BillingCycle.YEARLY,
                LocalDate.now().minusMonths(10),
                LocalDate.now().plusDays(30),
                SubscriptionStatus.ACTIVE,
                5L,
                5L
        ));

        storage.getSubscriptions().add(new Subscription(
                6L,
                "Old Video App",
                "Primer otkazane pretplate koja se više ne računa u troškove",
                7.99,
                BillingCycle.MONTHLY,
                LocalDate.now().minusYears(1),
                LocalDate.now().plusDays(2),
                SubscriptionStatus.CANCELLED,
                1L,
                1L
        ));

        storage.getPayments().add(new PaymentRecord(
                1L,
                1L,
                LocalDate.now().minusMonths(1),
                9.99,
                "Netflix mesečna uplata"
        ));

        storage.getPayments().add(new PaymentRecord(
                2L,
                2L,
                LocalDate.now().minusMonths(1),
                6.99,
                "Spotify mesečna uplata"
        ));

        storage.getPayments().add(new PaymentRecord(
                3L,
                3L,
                LocalDate.now().minusMonths(2),
                239.88,
                "Adobe godišnja uplata"
        ));

        storage.getPayments().add(new PaymentRecord(
                4L,
                4L,
                LocalDate.now().minusDays(20),
                10.00,
                "GitHub Copilot mesečna uplata"
        ));

        storage.getReminders().add(new RenewalReminder(
                1L,
                1L,
                LocalDate.now().plusDays(3),
                "Netflix se uskoro obnavlja.",
                false
        ));

        storage.getReminders().add(new RenewalReminder(
                2L,
                3L,
                LocalDate.now().plusDays(1),
                "Proveri da li je Adobe Creative Cloud i dalje potreban.",
                false
        ));

        storage.getReminders().add(new RenewalReminder(
                3L,
                5L,
                LocalDate.now().plusDays(25),
                "Google Drive godišnja pretplata se približava obnovi.",
                false
        ));
    }
}
