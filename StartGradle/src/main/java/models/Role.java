package models;

public enum Role {
    ADMIN,       // Administrator with full system access
    REFEREE,     // Official responsible for a specific event
    PARTICIPANT, // Competitor in the triathlon
    ORGANIZER,   // Event management staff
    VIEWER       // Read-only access to results
}