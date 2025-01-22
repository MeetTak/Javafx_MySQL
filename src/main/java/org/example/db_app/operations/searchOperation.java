package org.example.db_app.operations;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.db_app.database.DatabaseConnection;

import java.sql.*;

public class searchOperation implements DatabaseOperation {
    private ComboBox<String> fieldSelector;
    private TextField searchInput;
    private TableView<ObservableList<String>> resultTable;

    @Override
    public VBox getOperationUI() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(15));

        fieldSelector = new ComboBox<>(FXCollections.observableArrayList(
                "album_id", "album_name", "artist_id"
        ));
        fieldSelector.setValue("album_id");

        HBox searchRow = new HBox(10);
        searchInput = new TextField();
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> execute());
        searchRow.getChildren().addAll(fieldSelector, searchInput, searchButton);

        resultTable = new TableView<>();

        container.getChildren().addAll(searchRow, resultTable);
        return container;
    }

//    @Override
//    public void execute() {
//        System.out.println("Searching for " + searchInput.getText() + " in " + fieldSelector.getValue()); // for terminal
//
//        try (Connection conn = DatabaseConnection.getConnection()) {
//            String query = String.format("SELECT * FROM albums WHERE %s = ?",
//                    fieldSelector.getValue());
//
//            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
//                pstmt.setString(1, searchInput.getText());
//                ResultSet rs = pstmt.executeQuery();
//
//                ObservableList<String> data = FXCollections.observableArrayList();
//                var meta = rs.getMetaData();
//                for (int i = 1; i <= meta.getColumnCount(); i++) {
//                    data.add(meta.getColumnName(i));
//                }
//
//                while (rs.next()) {
//                    for (int i = 1; i <= meta.getColumnCount(); i++) {
//                        data.add(rs.getString(i));
//                    }
//                }
//
//                Platform.runLater(() -> {
//                    resultTable.getItems().clear();
//                    resultTable.getColumns().clear();
//
//                    TableColumn<String, String> column = new TableColumn<>(fieldSelector.getValue());
//                    column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()));
//                    resultTable.getColumns().add(column);
//
//                    resultTable.setItems(data);
//                });
//            }
//        } catch (SQLException e) {
//            showError("Database Error", e.getMessage());
//        }
//    }

    @Override
    public void execute() {
        System.out.println("Searching for " + searchInput.getText() + " in " + fieldSelector.getValue()); // for terminal

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = String.format("SELECT * FROM albums WHERE %s = ?", fieldSelector.getValue());

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, searchInput.getText());
                ResultSet rs = pstmt.executeQuery();

                ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
                var meta = rs.getMetaData();

                // Clear existing columns
                Platform.runLater(() -> {
                    resultTable.getColumns().clear();
                });

                // Create table columns dynamically based on ResultSet metadata
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    final int colIndex = i - 1; // Zero-based index for ObservableList
                    TableColumn<ObservableList<String>, String> column = new TableColumn<>(meta.getColumnName(i));
                    column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(colIndex)));

                    Platform.runLater(() -> resultTable.getColumns().add(column));
                }

                // Populate data
                while (rs.next()) {
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= meta.getColumnCount(); i++) {
                        row.add(rs.getString(i));
                    }
                    data.add(row);
                }

                // Update the TableView on the JavaFX application thread
                Platform.runLater(() -> {
                    resultTable.setItems(data);
                });
            }
        } catch (SQLException e) {
            showError("Database Error", e.getMessage());
        }
    }


    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
