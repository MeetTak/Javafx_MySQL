package org.example.db_app.operations;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.db_app.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class deleteOperation implements DatabaseOperation {
    private ComboBox<String> fieldSelector;
    private TextField deleteInput;

    @Override
    public VBox getOperationUI() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(15));

        fieldSelector = new ComboBox<>(FXCollections.observableArrayList(
                "artist_id", "album_id", "album_name"
        ));
        fieldSelector.setValue("album_id");

        deleteInput = new TextField();
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> execute());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Delete by:"), 0, 0);
        grid.add(fieldSelector, 1, 0);
        grid.add(new Label("Value:"), 0, 1);
        grid.add(deleteInput, 1, 1);
        grid.add(deleteButton, 1, 2);

        container.getChildren().add(grid);
        return container;
    }

    @Override
    public void execute() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String field = fieldSelector.getValue();
            String value = deleteInput.getText();
            String query = "DELETE FROM albums WHERE " + field + " = ?";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, value);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                showSuccess("Record deleted successfully");
            }

        } catch (SQLException e) {
            showError("Database Error", e.getMessage());
        }

        System.out.println("Deleting where " +
                fieldSelector.getValue() + " = " + deleteInput.getText());
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