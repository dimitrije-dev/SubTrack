package rs.ac.metropolitan.subtrack.service;

import org.springframework.stereotype.Service;
import rs.ac.metropolitan.subtrack.model.PaymentRecord;
import rs.ac.metropolitan.subtrack.storage.ApplicationStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final ApplicationStorage storage;

    public PaymentService(ApplicationStorage storage) {
        this.storage = storage;
    }

    public List<PaymentRecord> getAllPayments() {
        return storage.getPayments().stream()
                .sorted(Comparator.comparing(PaymentRecord::getPaymentDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    public Optional<PaymentRecord> getPaymentById(Long id) {
        return storage.getPayments().stream()
                .filter(payment -> payment.getId().equals(id))
                .findFirst();
    }

    public List<PaymentRecord> getPaymentsBySubscriptionId(Long subscriptionId) {
        return storage.getPayments().stream()
                .filter(payment -> payment.getSubscriptionId() != null && payment.getSubscriptionId().equals(subscriptionId))
                .sorted(Comparator.comparing(PaymentRecord::getPaymentDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    public PaymentRecord createPayment(PaymentRecord payment) {
        long nextId = storage.getPayments().stream()
                .map(PaymentRecord::getId)
                .filter(existingId -> existingId != null)
                .max(Comparator.naturalOrder())
                .orElse(0L) + 1;

        payment.setId(nextId);
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDate.now());
        }

        storage.getPayments().add(payment);
        return payment;
    }

    public boolean updatePayment(Long id, PaymentRecord updated) {
        Optional<PaymentRecord> existingOptional = getPaymentById(id);
        if (existingOptional.isEmpty()) {
            return false;
        }

        PaymentRecord existing = existingOptional.get();
        existing.setSubscriptionId(updated.getSubscriptionId());
        existing.setPaymentDate(updated.getPaymentDate());
        existing.setAmount(updated.getAmount());
        existing.setNote(updated.getNote());
        return true;
    }

    public boolean deletePayment(Long id) {
        return storage.getPayments().removeIf(payment -> payment.getId().equals(id));
    }
}
