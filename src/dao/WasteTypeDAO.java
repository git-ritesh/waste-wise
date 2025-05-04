package dao;

import models.WasteType;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Waste Type-related database operations
 */
public class WasteTypeDAO {
    
    /**
     * Add a new waste type to the database
     * @param wasteType WasteType object to add
     * @return true if successful, false otherwise
     */
    public boolean addWasteType(WasteType wasteType) {
        String query = "INSERT INTO waste_types (type_name, description) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, wasteType.getTypeName());
            stmt.setString(2, wasteType.getDescription());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding waste type: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update an existing waste type in the database
     * @param wasteType WasteType object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateWasteType(WasteType wasteType) {
        String query = "UPDATE waste_types SET type_name = ?, description = ? WHERE type_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, wasteType.getTypeName());
            stmt.setString(2, wasteType.getDescription());
            stmt.setInt(3, wasteType.getTypeId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating waste type: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a waste type from the database
     * @param typeId ID of the waste type to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteWasteType(int typeId) {
        String query = "DELETE FROM waste_types WHERE type_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, typeId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting waste type: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get a waste type by its ID
     * @param typeId Waste type ID to search for
     * @return WasteType object if found, null otherwise
     */
    public WasteType getWasteTypeById(int typeId) {
        String query = "SELECT * FROM waste_types WHERE type_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, typeId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractWasteTypeFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting waste type by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get all waste types
     * @return List of all waste types
     */
    public List<WasteType> getAllWasteTypes() {
        List<WasteType> wasteTypes = new ArrayList<>();
        String query = "SELECT * FROM waste_types ORDER BY type_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                wasteTypes.add(extractWasteTypeFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all waste types: " + e.getMessage());
        }
        
        return wasteTypes;
    }
    
    /**
     * Extract a WasteType object from a ResultSet
     * @param rs ResultSet containing waste type data
     * @return WasteType object
     * @throws SQLException if an error occurs
     */
    private WasteType extractWasteTypeFromResultSet(ResultSet rs) throws SQLException {
        WasteType wasteType = new WasteType();
        wasteType.setTypeId(rs.getInt("type_id"));
        wasteType.setTypeName(rs.getString("type_name"));
        wasteType.setDescription(rs.getString("description"));
        return wasteType;
    }
}