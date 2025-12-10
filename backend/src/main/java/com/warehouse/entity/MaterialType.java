package com.warehouse.entity;

public enum MaterialType {
    RM("Raw Materials"),
    PM("Packing Materials"),
    FM("Finished Materials");
    
    private final String displayName;
    
    MaterialType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
