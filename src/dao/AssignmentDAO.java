package dao;

import models.Assignment;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Assignment-related database operations
 */
public class AssignmentDAO {
    
    /**
     * Add a new assignment to the database
     * @param assignment Assignment object to add
     * @return true if successful, false otherwise
     */
    public boolean addAssignment(Assignment assignment) {
        String query = "INSERT INTO assignments (request_id, collector_id, status) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, assignment.getRequestId());
            stmt.setInt(2, assignment.getCollectorId());
            stmt.setString(3, assignment.getStatus());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Update the waste request status to "Assigned"
                WasteRequestDAO requestDAO = new WasteRequestDAO();
                requestDAO.updateRequestStatus(assignment.getRequestId(), "Assigned");
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error adding assignment: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update an existing assignment in the database
     * @param assignment Assignment object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateAssignment(Assignment assignment) {
        String query = "UPDATE assignments SET collector_id = ?, status = ? WHERE assignment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, assignment.getCollectorId());
            stmt.setString(2, assignment.getStatus());
            stmt.setInt(3, assignment.getAssignmentId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating assignment: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update the status of an assignment
     * @param assignmentId Assignment ID
     * @param status New status
     * @return true if successful, false otherwise
     */
    public boolean updateAssignmentStatus(int assignmentId, String status) {
        String query = "UPDATE assignments SET status = ? WHERE assignment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, assignmentId);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // If the assignment is completed, update the waste request status to "Collected"
                if (status.equals("Completed")) {
                    Assignment assignment = getAssignmentById(assignmentId);
                    if (assignment != null) {
                        WasteRequestDAO requestDAO = new WasteRequestDAO();
                        requestDAO.updateRequestStatus(assignment.getRequestId(), "Collected");
                    }
                }
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error updating assignment status: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete an assignment from the database
     * @param assignmentId ID of the assignment to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteAssignment(int assignmentId) {
        String query = "DELETE FROM assignments WHERE assignment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, assignmentId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting assignment: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get an assignment by its ID
     * @param assignmentId Assignment ID to search for
     * @return Assignment object if found, null otherwise
     */
    public Assignment getAssignmentById(int assignmentId) {
        String query = "SELECT a.*, u.full_name as collector_name, " +
                       "CONCAT(wr.address, ' (', wt.type_name, ', ', wr.quantity, ' kg)') as request_details " +
                       "FROM assignments a " +
                       "JOIN users u ON a.collector_id = u.user_id " +
                       "JOIN waste_requests wr ON a.request_id = wr.request_id " +
                       "JOIN waste_types wt ON wr.waste_type = wt.type_id " +
                       "WHERE a.assignment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, assignmentId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractAssignmentFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting assignment by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get an assignment by request ID
     * @param requestId Request ID to search for
     * @return Assignment object if found, null otherwise
     */
    public Assignment getAssignmentByRequestId(int requestId) {
        String query = "SELECT a.*, u.full_name as collector_name, " +
                       "CONCAT(wr.address, ' (', wt.type_name, ', ', wr.quantity, ' kg)') as request_details " +
                       "FROM assignments a " +
                       "JOIN users u ON a.collector_id = u.user_id " +
                       "JOIN waste_requests wr ON a.request_id = wr.request_id " +
                       "JOIN waste_types wt ON wr.waste_type = wt.type_id " +
                       "WHERE a.request_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, requestId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractAssignmentFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting assignment by request ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get all assignments
     * @return List of all assignments
     */
    public List<Assignment> getAllAssignments() {
        List<Assignment> assignments = new ArrayList<>();
        String query = "SELECT a.*, u.full_name as collector_name, " +
                       "CONCAT(wr.address, ' (', wt.type_name, ', ', wr.quantity, ' kg)') as request_details " +
                       "FROM assignments a " +
                       "JOIN users u ON a.collector_id = u.user_id " +
                       "JOIN waste_requests wr ON a.request_id = wr.request_id " +
                       "JOIN waste_types wt ON wr.waste_type = wt.type_id " +
                       "ORDER BY a.assigned_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                assignments.add(extractAssignmentFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all assignments: " + e.getMessage());
        }
        
        return assignments;
    }
    
    /**
     * Get assignments by collector ID
     * @param collectorId Collector ID to filter by
     * @return List of assignments for the specified collector
     */
    public List<Assignment> getAssignmentsByCollectorId(int collectorId) {
        List<Assignment> assignments = new ArrayList<>();
        String query = "SELECT a.*, u.full_name as collector_name, " +
                       "CONCAT(wr.address, ' (', wt.type_name, ', ', wr.quantity, ' kg)') as request_details " +
                       "FROM assignments a " +
                       "JOIN users u ON a.collector_id = u.user_id " +
                       "JOIN waste_requests wr ON a.request_id = wr.request_id " +
                       "JOIN waste_types wt ON wr.waste_type = wt.type_id " +
                       "WHERE a.collector_id = ? " +
                       "ORDER BY a.assigned_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, collectorId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    assignments.add(extractAssignmentFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting assignments by collector ID: " + e.getMessage());
        }
        
        return assignments;
    }
    
    /**
     * Get assignments by status
     * @param status Status to filter by
     * @return List of assignments with the specified status
     */
    public List<Assignment> getAssignmentsByStatus(String status) {
        List<Assignment> assignments = new ArrayList<>();
        String query = "SELECT a.*, u.full_name as collector_name, " +
                       "CONCAT(wr.address, ' (', wt.type_name, ', ', wr.quantity, ' kg)') as request_details " +
                       "FROM assignments a " +
                       "JOIN users u ON a.collector_id = u.user_id " +
                       "JOIN waste_requests wr ON a.request_id = wr.request_id " +
                       "JOIN waste_types wt ON wr.waste_type = wt.type_id " +
                       "WHERE a.status = ? " +
                       "ORDER BY a.assigned_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, status);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    assignments.add(extractAssignmentFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting assignments by status: " + e.getMessage());
        }
        
        return assignments;
    }
    
    /**
     * Extract an Assignment object from a ResultSet
     * @param rs ResultSet containing assignment data
     * @return Assignment object
     * @throws SQLException if an error occurs
     */
    private Assignment extractAssignmentFromResultSet(ResultSet rs) throws SQLException {
        Assignment assignment = new Assignment();
        assignment.setAssignmentId(rs.getInt("assignment_id"));
        assignment.setRequestId(rs.getInt("request_id"));
        assignment.setCollectorId(rs.getInt("collector_id"));
        assignment.setAssignedDate(rs.getTimestamp("assigned_date"));
        assignment.setStatus(rs.getString("status"));
        
        // Set additional display fields
        assignment.setCollectorName(rs.getString("collector_name"));
        assignment.setRequestDetails(rs.getString("request_details"));
        
        return assignment;
    }
}