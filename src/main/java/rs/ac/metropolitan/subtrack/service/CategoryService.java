package rs.ac.metropolitan.subtrack.service;

import org.springframework.stereotype.Service;
import rs.ac.metropolitan.subtrack.model.Category;
import rs.ac.metropolitan.subtrack.storage.ApplicationStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final ApplicationStorage storage;

    public CategoryService(ApplicationStorage storage) {
        this.storage = storage;
    }

    public List<Category> getAllCategories() {
        return storage.getCategories();
    }

    public Optional<Category> getCategoryById(Long id) {
        return storage.getCategories().stream()
                .filter(category -> category.getId().equals(id))
                .findFirst();
    }

    public Category createCategory(Category category) {
        long nextId = storage.getCategories().stream()
                .map(Category::getId)
                .filter(existingId -> existingId != null)
                .max(Comparator.naturalOrder())
                .orElse(0L) + 1;

        category.setId(nextId);
        storage.getCategories().add(category);
        return category;
    }

    public boolean updateCategory(Long id, Category updated) {
        Optional<Category> existingOptional = getCategoryById(id);
        if (existingOptional.isEmpty()) {
            return false;
        }

        Category existing = existingOptional.get();
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPercentage(updated.getPercentage());
        existing.setColorClass(updated.getColorClass());
        return true;
    }

    public boolean deleteCategory(Long id) {
        return storage.getCategories().removeIf(category -> category.getId().equals(id));
    }
}
