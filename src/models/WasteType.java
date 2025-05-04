package models;

/**
 * Model class for Waste Type
 */
public class WasteType {
    private int typeId;
    private String typeName;
    private String description;
    
    // Default constructor
    public WasteType() {}
    
    // Constructor with all fields
    public WasteType(int typeId, String typeName, String description) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.description = description;
    }
    
    // Getters and Setters
    public int getTypeId() {
        return typeId;
    }
    
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
    
    public String getTypeName() {
        return typeName;
    }
    
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return typeName;
    }
}