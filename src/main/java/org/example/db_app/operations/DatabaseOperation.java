package org.example.db_app.operations;

import javafx.scene.layout.VBox;

public interface DatabaseOperation {
    VBox getOperationUI();
    void execute();
}