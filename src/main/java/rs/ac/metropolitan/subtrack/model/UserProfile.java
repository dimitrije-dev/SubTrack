package rs.ac.metropolitan.subtrack.model;

public class UserProfile {

    private Long id;
    private String fullName;
    private String email;
    private String preferredCurrency;
    private double monthlyBudget;

    public UserProfile() {
    }

    public UserProfile(Long id, String fullName, String email, String preferredCurrency, double monthlyBudget) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.preferredCurrency = preferredCurrency;
        this.monthlyBudget = monthlyBudget;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPreferredCurrency() {
        return preferredCurrency;
    }

    public void setPreferredCurrency(String preferredCurrency) {
        this.preferredCurrency = preferredCurrency;
    }

    public double getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }
}
