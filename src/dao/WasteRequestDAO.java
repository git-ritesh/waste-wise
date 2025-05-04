package dao;

import models.WasteRequest;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Waste Request-related database operations
 */
public class WasteRequestDAO {
    
    /**
     * Add a new waste request to the database
     * @param request WasteRequest object to add
     * @return true if successful, false otherwise
     */
    public boolean addWasteRequest(WasteRequest request) {
        String query = "INSERT INTO waste_requests (user_id, waste_type, quantity, address, status, requested_date) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, request.getUserId());
            stmt.setInt(2, request.getWasteType());
            stmt.setDouble(3, request.getQuantity());
            stmt.setString(4, request.getAddress());
            stmt.setString(5, request.getStatus());
            stmt.setDate(6, request.getRequestedDate());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding waste request: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update an existing waste request in the database
     * @param request WasteRequest object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateWasteRequest(WasteRequest request) {
        String query = "UPDATE waste_requests SET waste_type = ?, quantity = ?, address = ?, " +
                       "status = ?, requested_date = ?, pickup_date = ? WHERE request_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, request.getWasteType());
            stmt.setDouble(2, request.getQuantity());
            stmt.setString(3, request.getAddress());
            stmt.setString(4, request.getStatus());
            stmt.setDate(5, request.getRequestedDate());
            stmt.setDate(6, request.getPickupDate());
            stmt.setInt(7, request.getRequestId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating waste request: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update the status of a waste request
     * @param requestId Request ID
     * @param status New status
     * @return true if successful, false otherwise
     */
    public boolean updateRequestStatus(int requestId, String status) {
        String query = "UPDATE waste_requests SET status = ? WHERE request_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, requestId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating request status: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a waste request from the database
     * @param requestId ID of the waste request to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteWasteRequest(int requestId) {
        String query = "DELETE FROM waste_requests WHERE request_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, requestId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting waste request: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get a waste request by its ID
     * @param requestId Waste request ID to search for
     * @return WasteRequest object if found, null otherwise
     */
    public WasteRequest getWasteRequestById(int requestId) {
        String query = "SELECT wr.*, u.full_name as user_name, wt.type_name as waste_type_name " +
                       "FROM waste_requests wr " +
                       "JOIN users u ON wr.user_id = u.user_id " +
                       "JOIN waste_types wt ON wr.waste_type = wt.type_id " +
                       "WHERE wr.request_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, requestId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractWasteRequestFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting waste request by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get all waste requests
     * @return List of all waste requests
     */
    public List<WasteRequest> getAllWasteRequests() {
        List<WasteRequest> requests = new ArrayList<>();
        String query = "SELECT wr.*, u.full_name as user_name, wt.type_name as waste_type_name " +
                       "FROM waste_requests wr " +
                       "JOIN users u ON wr.user_id = u.user_id " +
                       "JOIN waste_types wt ON wr.waste_type = wt.type_id " +
                       "ORDER BY wr.created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                requests.add(extractWasteRequestFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all waste requests: " + e.getMessage());
        }
        
        return requests;
    }
    
    /**
     * Get waste requests by user ID
     * @param userId User ID to filter by
     * @return List of waste requests for the specified user
     */
    public List<WasteRequest> getWasteRequestsByUserId(int userId) {
        List<WasteRequest> requests = new ArrayList<>();
        String query = "SELECT wr.*, u.full_name as user_name, wt.type_name as waste_type_name " +
                       "FROM waste_requests wr " +
                       "JOIN users u ON wr.user_id = u.user_id " +
                       "JOIN waste_types wt ON wr.waste_type = wt.type_id " +
                       "WHERE wr.user_id = ? " +
                       "ORDER BY wr.created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(extractWasteRequestFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting waste requests by user ID: " + e.getMessage());
        }
        
        return requests;
    }
    
    /**
     * Get waste requests by status
     * @param status Status to filter by
     * @return List of waste requests with the specified status
     */
    public List<WasteRequest> getWasteRequestsByStatus(String status) {
        List<WasteRequest> requests = new ArrayList<>();
        String query = "SELECT wr.*, u.full_name as user_name, wt.type_name as waste_type_name " +
                       "FROM waste_requests wr " +
                       "JOIN users u ON wr.user_id = u.user_id " +
                       "JOIN waste_types wt ON wr.waste_type = wt.type_id " +
                       "WHERE wr.status = ? " +
                       "ORDER BY wr.created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, status);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(extractWasteRequestFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting waste requests by status: " + e.getMessage());
        }
        
        return requests;
    }
    
    /**
     * Extract a WasteRequest object from a ResultSet
     * @param rs ResultSet containing waste request data
     * @return WasteRequest object
     * @throws SQLException if an error occurs
     */
    private WasteRequest extractWasteRequestFromResultSet(ResultSet rs) throws SQLException {
        WasteRequest request = new WasteRequest();
        request.setRequestId(rs.getInt("request_id"));
        request.setUserId(rs.getInt("user_id"));
        request.setWasteType(rs.getInt("waste_type"));
        request.setQuantity(rs.getDouble("quantity"));
        request.setAddress(rs.getString("address"));
        request.setStatus(rs.getString("status"));
        request.setRequestedDate(rs.getDate("requested_date"));
        request.setPickupDate(rs.getDate("pickup_date"));
        request.setCreatedAt(rs.getTimestamp("created_at"));
        
        // Set additional display fields
        request.setUserName(rs.getString("user_name"));
        request.setWasteTypeName(rs.getString("waste_type_name"));
        
        return request;
    }
}