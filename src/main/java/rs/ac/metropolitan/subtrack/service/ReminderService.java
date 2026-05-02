package rs.ac.metropolitan.subtrack.service;

import org.springframework.stereotype.Service;
import rs.ac.metropolitan.subtrack.model.RenewalReminder;
import rs.ac.metropolitan.subtrack.storage.ApplicationStorage;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReminderService {

    private final ApplicationStorage storage;

    public ReminderService(ApplicationStorage storage) {
        this.storage = storage;
    }

    public List<RenewalReminder> getAllReminders() {
        return storage.getReminders().stream()
                .peek(this::syncComputedFields)
                .sorted(Comparator.comparing(this::effectiveDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    public Optional<RenewalReminder> getReminderById(Long id) {
        return storage.getReminders().stream()
                .filter(reminder -> reminder.getId().equals(id))
                .findFirst();
    }

    public List<RenewalReminder> getRemindersBySubscriptionId(Long subscriptionId) {
        return storage.getReminders().stream()
                .filter(reminder -> reminder.getSubscriptionId() != null && reminder.getSubscriptionId().equals(subscriptionId))
                .peek(this::syncComputedFields)
                .sorted(Comparator.comparing(this::effectiveDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    public List<RenewalReminder> getUpcomingReminders(int limit) {
        LocalDate today = LocalDate.now();
        return storage.getReminders().stream()
                .peek(this::syncComputedFields)
                .filter(reminder -> !reminder.isCompleted())
                .filter(reminder -> {
                    LocalDate date = effectiveDate(reminder);
                    return date != null && !date.isBefore(today);
                })
                .sorted(Comparator.comparing(this::effectiveDate))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public RenewalReminder createReminder(RenewalReminder reminder) {
        long nextId = storage.getReminders().stream()
                .map(RenewalReminder::getId)
                .filter(existingId -> existingId != null)
                .max(Comparator.naturalOrder())
                .orElse(0L) + 1;

        reminder.setId(nextId);
        if (reminder.getReminderDate() == null && reminder.getRenewalDate() == null) {
            reminder.setReminderDate(LocalDate.now().plusDays(7));
        }
        syncComputedFields(reminder);

        storage.getReminders().add(reminder);
        return reminder;
    }

    public boolean updateReminder(Long id, RenewalReminder updated) {
        Optional<RenewalReminder> existingOptional = getReminderById(id);
        if (existingOptional.isEmpty()) {
            return false;
        }

        RenewalReminder existing = existingOptional.get();
        existing.setSubscriptionId(updated.getSubscriptionId());
        existing.setReminderDate(updated.getReminderDate());
        existing.setRenewalDate(updated.getRenewalDate());
        existing.setMessage(updated.getMessage());
        existing.setCompleted(updated.isCompleted());
        existing.setServiceName(updated.getServiceName());
        existing.setPrice(updated.getPrice());
        syncComputedFields(existing);
        return true;
    }

    public boolean markAsCompleted(Long id) {
        Optional<RenewalReminder> existingOptional = getReminderById(id);
        if (existingOptional.isEmpty()) {
            return false;
        }

        existingOptional.get().markAsCompleted();
        return true;
    }

    public boolean deleteReminder(Long id) {
        return storage.getReminders().removeIf(reminder -> reminder.getId().equals(id));
    }

    private void syncComputedFields(RenewalReminder reminder) {
        LocalDate date = effectiveDate(reminder);
        if (date != null) {
            reminder.setDaysLeft((int) ChronoUnit.DAYS.between(LocalDate.now(), date));
        }
    }

    private LocalDate effectiveDate(RenewalReminder reminder) {
        return reminder.getRenewalDate() != null ? reminder.getRenewalDate() : reminder.getReminderDate();
    }
}
