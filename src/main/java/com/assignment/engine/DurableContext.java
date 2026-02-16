package com.assignment.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.*;
import java.util.concurrent.Callable;

public class DurableContext {
    private final Connection connection;
    private final String workflowId;
    private final ObjectMapper mapper = new ObjectMapper();
    private int sequenceId = 0; // The Logical Clock for loop support

    public DurableContext(String workflowId) throws SQLException {
        this.workflowId = workflowId;
        // Connects to a local file named workflow_state.db
        this.connection = DriverManager.getConnection("jdbc:sqlite:workflow_state.db");
        setupDatabase();
    }

    private void setupDatabase() throws SQLException {
        Statement stmt = connection.createStatement();
        // Creates the RDBMS table required by the assignment
        stmt.execute("CREATE TABLE IF NOT EXISTS workflow_steps (" +
                "workflow_id TEXT, step_key TEXT, status TEXT, output TEXT, " +
                "PRIMARY KEY (workflow_id, step_key))");
    }

    // The Generic Step Primitive (Type Safe)
    public <T> T step(String id, Callable<T> fn) {
        String stepKey = id + "-" + (sequenceId++); 
        
        try {
            // Memoization: Check if step results already exist in DB
            PreparedStatement pstmt = connection.prepareStatement(
                "SELECT output FROM workflow_steps WHERE workflow_id = ? AND step_key = ?");
            pstmt.setString(1, workflowId);
            pstmt.setString(2, stepKey);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println(">>> [RESUMING] Skipping: " + id);
                return (T) mapper.readValue(rs.getString("output"), Object.class);
            }

            // Execute the actual code if not found in DB
            T result = fn.call();

            // Persistence: Save to SQLite as JSON
            PreparedStatement insert = connection.prepareStatement(
                "INSERT INTO workflow_steps VALUES (?, ?, 'COMPLETED', ?)");
            insert.setString(1, workflowId);
            insert.setString(2, stepKey);
            insert.setString(3, mapper.writeValueAsString(result));
            insert.executeUpdate();

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Workflow halted at: " + id, e);
        }
    }
}