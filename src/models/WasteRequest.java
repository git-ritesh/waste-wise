package models;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model class for Waste Request
 */
public class WasteRequest {
    private int requestId;
    private int userId;
    private int wasteType;
    private double quantity;
    private String address;
    private String status;
    private Date requestedDate;
    private Date pickupDate;
    private Timestamp createdAt;
    
    // Additional fields for display purposes
    private String userName;
    private String wasteTypeName;
    
    // Default constructor
    public WasteRequest() {}
    
    // Constructor for creating a new request (without ID and timestamp)
    public WasteRequest(int userId, int wasteType, double quantity, String address, Date requestedDate) {
        this.userId = userId;
        this.wasteType = wasteType;
        this.quantity = quantity;
        this.address = address;
        this.status = "Pending";
        this.requestedDate = requestedDate;
    }
    
    // Constructor with all fields
    public WasteRequest(int requestId, int userId, int wasteType, double quantity, String address, 
                        String status, Date requestedDate, Date pickupDate, Timestamp createdAt) {
        this.requestId = requestId;
        this.userId = userId;
        this.wasteType = wasteType;
        this.quantity = quantity;
        this.address = address;
        this.status = status;
        this.requestedDate = requestedDate;
        this.pickupDate = pickupDate;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getRequestId() {
        return requestId;
    }
    
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getWasteType() {
        return wasteType;
    }
    
    public void setWasteType(int wasteType) {
        this.wasteType = wasteType;
    }
    
    public double getQuantity() {
        return quantity;
    }
    
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Date getRequestedDate() {
        return requestedDate;
    }
    
    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }
    
    public Date getPickupDate() {
        return pickupDate;
    }
    
    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getWasteTypeName() {
        return wasteTypeName;
    }
    
    public void setWasteTypeName(String wasteTypeName) {
        this.wasteTypeName = wasteTypeName;
    }
}