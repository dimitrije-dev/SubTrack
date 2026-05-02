package rs.ac.metropolitan.subtrack.model;

import java.time.LocalDateTime;

public class AuditLog {

    private Long id;
    private String action;
    private String className;
    private String methodName;
    private LocalDateTime timestamp;

    public AuditLog() {
    }

    public AuditLog(Long id, String action, String className, String methodName, LocalDateTime timestamp) {
        this.id = id;
        this.action = action;
        this.className = className;
        this.methodName = methodName;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
