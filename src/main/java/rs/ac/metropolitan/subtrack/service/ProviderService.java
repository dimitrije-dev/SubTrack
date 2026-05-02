package rs.ac.metropolitan.subtrack.service;

import org.springframework.stereotype.Service;
import rs.ac.metropolitan.subtrack.model.Provider;
import rs.ac.metropolitan.subtrack.storage.ApplicationStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ProviderService {

    private final ApplicationStorage storage;

    public ProviderService(ApplicationStorage storage) {
        this.storage = storage;
    }

    public List<Provider> getAllProviders() {
        return storage.getProviders();
    }

    public Optional<Provider> getProviderById(Long id) {
        return storage.getProviders().stream()
                .filter(provider -> provider.getId().equals(id))
                .findFirst();
    }

    public Provider createProvider(Provider provider) {
        long nextId = storage.getProviders().stream()
                .map(Provider::getId)
                .filter(existingId -> existingId != null)
                .max(Comparator.naturalOrder())
                .orElse(0L) + 1;

        provider.setId(nextId);
        storage.getProviders().add(provider);
        return provider;
    }

    public boolean updateProvider(Long id, Provider updated) {
        Optional<Provider> existingOptional = getProviderById(id);
        if (existingOptional.isEmpty()) {
            return false;
        }

        Provider existing = existingOptional.get();
        existing.setName(updated.getName());
        existing.setWebsite(updated.getWebsite());
        existing.setSupportEmail(updated.getSupportEmail());
        return true;
    }

    public boolean deleteProvider(Long id) {
        return storage.getProviders().removeIf(provider -> provider.getId().equals(id));
    }
}
