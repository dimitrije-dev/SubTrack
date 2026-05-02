package rs.ac.metropolitan.subtrack.model;

public class ActivityLog {

    private Long id;
    private String serviceName;
    private String description;
    private String timeAgo;

    public ActivityLog() {
    }

    public ActivityLog(Long id, String serviceName, String description, String timeAgo) {
        this.id = id;
        this.serviceName = serviceName;
        this.description = description;
        this.timeAgo = timeAgo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }
}
