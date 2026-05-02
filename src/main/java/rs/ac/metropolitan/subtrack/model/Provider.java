package rs.ac.metropolitan.subtrack.model;

public class Provider {

    private Long id;
    private String name;
    private String website;
    private String supportEmail;

    public Provider() {
    }

    public Provider(Long id, String name, String website, String supportEmail) {
        this.id = id;
        this.name = name;
        this.website = website;
        this.supportEmail = supportEmail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSupportEmail() {
        return supportEmail;
    }

    public void setSupportEmail(String supportEmail) {
        this.supportEmail = supportEmail;
    }
}
