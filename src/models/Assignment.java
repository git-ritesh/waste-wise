package models;

import java.sql.Timestamp;

/**
 * Model class for Assignment
 */
public class Assignment {
    private int assignmentId;
    private int requestId;
    private int collectorId;
    private Timestamp assignedDate;
    private String status;
    
    // Additional fields for display purposes
    private String collectorName;
    private String requestDetails;
    
    // Default constructor
    public Assignment() {}
    
    // Constructor for creating a new assignment (without ID)
    public Assignment(int requestId, int collectorId) {
        this.requestId = requestId;
        this.collectorId = collectorId;
        this.status = "Assigned";
    }
    
    // Constructor with all fields
    public Assignment(int assignmentId, int requestId, int collectorId, Timestamp assignedDate, String status) {
        this.assignmentId = assignmentId;
        this.requestId = requestId;
        this.collectorId = collectorId;
        this.assignedDate = assignedDate;
        this.status = status;
    }
    
    // Getters and Setters
    public int getAssignmentId() {
        return assignmentId;
    }
    
    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }
    
    public int getRequestId() {
        return requestId;
    }
    
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
    
    public int getCollectorId() {
        return collectorId;
    }
    
    public void setCollectorId(int collectorId) {
        this.collectorId = collectorId;
    }
    
    public Timestamp getAssignedDate() {
        return assignedDate;
    }
    
    public void setAssignedDate(Timestamp assignedDate) {
        this.assignedDate = assignedDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCollectorName() {
        return collectorName;
    }
    
    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
    }
    
    public String getRequestDetails() {
        return requestDetails;
    }
    
    public void setRequestDetails(String requestDetails) {
        this.requestDetails = requestDetails;
    }
}