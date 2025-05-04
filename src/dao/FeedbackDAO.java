package dao;

import models.Feedback;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Feedback-related database operations
 */
public class FeedbackDAO {
    
    /**
     * Add new feedback to the database
     * @param feedback Feedback object to add
     * @return true if successful, false otherwise
     */
    public boolean addFeedback(Feedback feedback) {
        String query = "INSERT INTO feedback (user_id, request_id, rating, comments) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, feedback.getUserId());
            stmt.setInt(2, feedback.getRequestId());
            stmt.setInt(3, feedback.getRating());
            stmt.setString(4, feedback.getComments());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding feedback: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update existing feedback in the database
     * @param feedback Feedback object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateFeedback(Feedback feedback) {
        String query = "UPDATE feedback SET rating = ?, comments = ? WHERE feedback_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, feedback.getRating());
            stmt.setString(2, feedback.getComments());
            stmt.setInt(3, feedback.getFeedbackId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating feedback: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete feedback from the database
     * @param feedbackId ID of the feedback to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteFeedback(int feedbackId) {
        String query = "DELETE FROM feedback WHERE feedback_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, feedbackId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting feedback: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get feedback by its ID
     * @param feedbackId Feedback ID to search for
     * @return Feedback object if found, null otherwise
     */
    public Feedback getFeedbackById(int feedbackId) {
        String query = "SELECT f.*, u.full_name as user_name, " +
                       "CONCAT(wr.address, ' (', wt.type_name, ')') as request_details " +
                       "FROM feedback f " +
                       "JOIN users u ON f.user_id = u.user_id " +
                       "JOIN waste_requests wr ON f.request_id = wr.request_id " +
                       "JOIN waste_types wt ON wr.waste_type = wt.type_id " +
                       "WHERE f.feedback_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, feedbackId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractFeedbackFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting feedback by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get feedback by request ID
     * @param requestId Request ID to search for
     * @return Feedback object if found, null otherwise
     */
    public Feedback getFeedbackByRequestId(int requestId) {
        String query = "SELECT f.*, u.full_name as user_name, " +
                       "CONCAT(wr.address, ' (', wt.type_name, ')') as request_details " +
                       "FROM feedback f " +
                       "JOIN users u ON f.user_id = u.user_id " +
                       "JOIN waste_requests wr ON f.request_id = wr.request_id " +
                       "JOIN waste_types wt ON wr.waste_type = wt.type_id " +
                       "WHERE f.request_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, requestId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractFeedbackFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting feedback by request ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get all feedback
     * @return List of all feedback
     */
    public List<Feedback> getAllFeedback() {
        List<Feedback> feedbackList = new ArrayList<>();
        String query = "SELECT f.*, u.full_name as user_name, " +
                       "CONCAT(wr.address, ' (', wt.type_name, ')') as request_details " +
                       "FROM feedback f " +
                       "JOIN users u ON f.user_id = u.user_id " +
                       "JOIN waste_requests wr ON f.request_id = wr.request_id " +
                       "JOIN waste_types wt ON wr.waste_type = wt.type_id " +
                       "ORDER BY f.submitted_on DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                feedbackList.add(extractFeedbackFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all feedback: " + e.getMessage());
        }
        
        return feedbackList;
    }
    
    /**
     * Get feedback by user ID
     * @param userId User ID to filter by
     * @return List of feedback from the specified user
     */
    public List<Feedback> getFeedbackByUserId(int userId) {
        List<Feedback> feedbackList = new ArrayList<>();
        String query = "SELECT f.*, u.full_name as user_name, " +
                       "CONCAT(wr.address, ' (', wt.type_name, ')') as request_details " +
                       "FROM feedback f " +
                       "JOIN users u ON f.user_id = u.user_id " +
                       "JOIN waste_requests wr ON f.request_id = wr.request_id " +
                       "JOIN waste_types wt ON wr.waste_type = wt.type_id " +
                       "WHERE f.user_id = ? " +
                       "ORDER BY f.submitted_on DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    feedbackList.add(extractFeedbackFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting feedback by user ID: " + e.getMessage());
        }
        
        return feedbackList;
    }
    
    /**
     * Extract a Feedback object from a ResultSet
     * @param rs ResultSet containing feedback data
     * @return Feedback object
     * @throws SQLException if an error occurs
     */
    private Feedback extractFeedbackFromResultSet(ResultSet rs) throws SQLException {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(rs.getInt("feedback_id"));
        feedback.setUserId(rs.getInt("user_id"));
        feedback.setRequestId(rs.getInt("request_id"));
        feedback.setRating(rs.getInt("rating"));
        feedback.setComments(rs.getString("comments"));
        feedback.setSubmittedOn(rs.getTimestamp("submitted_on"));
        
        // Set additional display fields
        feedback.setUserName(rs.getString("user_name"));
        feedback.setRequestDetails(rs.getString("request_details"));
        
        return feedback;
    }
}