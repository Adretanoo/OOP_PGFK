package com.grud.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        Button button = new Button("Click me");

        root.getChildren().add(button);

        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX App with CSS");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
