package org.example.db_app.operations;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.db_app.database.DatabaseConnection;
import java.sql.*;

public class insertOperation implements DatabaseOperation {
    private TextField artistIdField;
    private TextField albumIdField;
    private TextField albumNameField;

    @Override
    public VBox getOperationUI() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(15));

        artistIdField = new TextField();
        albumIdField = new TextField();
        albumNameField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Artist ID:"), 0, 0);
        grid.add(artistIdField, 1, 0);
        grid.add(new Label("Album ID:"), 0, 1);
        grid.add(albumIdField, 1, 1);
        grid.add(new Label("Album Name:"), 0, 2);
        grid.add(albumNameField, 1, 2);

        Button insertButton = new Button("Insert");
        insertButton.setOnAction(e -> execute());

        container.getChildren().addAll(grid, insertButton);
        return container;
    }

    @Override
    public void execute() {
        if (!validateInputs()) {
            showError("Validation Error", "All fields must be filled");
            return;
        }

        Connection conn = null; // common practice to initialize connection outside try block so that it can be closed in finally block and also to avoid null pointer exception
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String query = "INSERT INTO albums (artist_id, album_id, album_name) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, artistIdField.getText().trim());
                pstmt.setString(2, albumIdField.getText().trim());
                pstmt.setString(3, albumNameField.getText().trim());

                int result = pstmt.executeUpdate();
                if (result > 0) {
                    conn.commit();
                    clearFields();
                    showSuccess("Record inserted successfully");
                }
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            showError("Database Error", e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean validateInputs() {
        return !artistIdField.getText().trim().isEmpty() &&
                !albumIdField.getText().trim().isEmpty() &&
                !albumNameField.getText().trim().isEmpty();
    }

    private void clearFields() {
        artistIdField.clear();
        albumIdField.clear();
        albumNameField.clear();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}