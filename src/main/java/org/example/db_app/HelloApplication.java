package org.example.db_app;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.db_app.operations.*;

public class HelloApplication extends Application {

    // user defined error class
//    static class IncorrectFieldName extends Exception {
//        public IncorrectFieldName(String message) {
//            super(message);
//        }
//    }

    private ComboBox<String> operation;
//    private VBox searchBox;
    private VBox operationContainer;
    private DatabaseOperation currentOperation; // DatabaseOperation is an interface

    @Override
    public void start(Stage primaryStage) {
        Label optionField = new Label("Select the option");
        ObservableList<String> operationTypes = FXCollections.observableArrayList(
                "Insert", "Delete", "Search", "Update"
        );

        operation = new ComboBox<>(operationTypes);
        operation.setValue("Search");

        //Operation container
        operationContainer = new VBox();

        Button applyButton = new Button("APPLY");
        Button cancelButton = new Button("CANCEL");

        // Add button handlers
        applyButton.setOnAction(e -> handleApply());
        cancelButton.setOnAction(e -> handleCancel());

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(optionField, 0, 0);
        gridPane.add(operation, 1, 0);
        gridPane.add(applyButton, 0, 3);
        gridPane.add(cancelButton, 1, 3);

        vbox.getChildren().addAll(gridPane, operationContainer);

        applyButton.setOnAction(event -> {
            operationContainer.getChildren().clear();

            switch (operation.getValue()) {
                case "Search":
                    currentOperation = new searchOperation();
                    break;
                case "Insert":
                    currentOperation = new insertOperation();
                    break;
                case "Delete":
                    currentOperation = new deleteOperation();
                    break;
                case "Update":
                    currentOperation = new updateOperation();
                    break;
            }
                if (currentOperation != null) {
                    operationContainer.getChildren().add(currentOperation.getOperationUI());
                }
        });

        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setTitle("Music Database");
        primaryStage.setScene(scene);
        primaryStage.show(); // Add this line
    }

    private void handleApply() {
        System.out.println("Apply clicked: " + operation.getValue());
    }

    private void handleCancel() {
        operationContainer.getChildren().clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

