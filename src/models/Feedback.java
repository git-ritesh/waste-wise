package models;

import java.sql.Timestamp;

/**
 * Model class for Feedback
 */
public class Feedback {
    private int feedbackId;
    private int userId;
    private int requestId;
    private int rating;
    private String comments;
    private Timestamp submittedOn;
    
    // Additional fields for display purposes
    private String userName;
    private String requestDetails;
    
    // Default constructor
    public Feedback() {}
    
    // Constructor for creating new feedback (without ID and timestamp)
    public Feedback(int userId, int requestId, int rating, String comments) {
        this.userId = userId;
        this.requestId = requestId;
        this.rating = rating;
        this.comments = comments;
    }
    
    // Constructor with all fields
    public Feedback(int feedbackId, int userId, int requestId, int rating, String comments, Timestamp submittedOn) {
        this.feedbackId = feedbackId;
        this.userId = userId;
        this.requestId = requestId;
        this.rating = rating;
        this.comments = comments;
        this.submittedOn = submittedOn;
    }
    
    // Getters and Setters
    public int getFeedbackId() {
        return feedbackId;
    }
    
    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getRequestId() {
        return requestId;
    }
    
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
    
    public int getRating() {
        return rating;
    }
    
    public void setRating(int rating) {
        this.rating = rating;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public Timestamp getSubmittedOn() {
        return submittedOn;
    }
    
    public void setSubmittedOn(Timestamp submittedOn) {
        this.submittedOn = submittedOn;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getRequestDetails() {
        return requestDetails;
    }
    
    public void setRequestDetails(String requestDetails) {
        this.requestDetails = requestDetails;
    }
}