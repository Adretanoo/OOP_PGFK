package com.grud.app;

import com.grud.dao.UserDao;
import com.grud.entity.Role;
import com.grud.entity.User;
import com.grud.util.ConnectionManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.sql.Connection;
import java.util.List;

public class Main extends Application {

    private UserDao userDao;

    @Override
    public void start(Stage primaryStage) {
        Connection connection = ConnectionManager.open();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(connection, true));
        userDao = new UserDao(jdbcTemplate);

        TextField idField = new TextField();
        idField.setPromptText("ID (для пошуку/оновлення/видалення)");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField roleIdField = new TextField();
        roleIdField.setPromptText("Role ID");

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);

        // Кнопки
        Button findByIdButton = new Button("Знайти по ID");
        findByIdButton.setOnAction(e -> {
            try {
                Integer id = Integer.parseInt(idField.getText());
                User user = userDao.findById(id);
                outputArea.setText(user.toString());
            } catch (Exception ex) {
                outputArea.setText("Помилка: " + ex.getMessage());
            }
        });

        Button findAllButton = new Button("Показати всіх");
        findAllButton.setOnAction(e -> {
            try {
                List<User> users = userDao.findAll();
                StringBuilder sb = new StringBuilder();
                for (User user : users) {
                    sb.append(user.toString()).append("\n");
                }
                outputArea.setText(sb.toString());
            } catch (Exception ex) {
                outputArea.setText("Помилка: " + ex.getMessage());
            }
        });

        Button saveButton = new Button("Додати користувача");
        saveButton.setOnAction(e -> {
            try {
                User user = new User();
                user.setUsername(usernameField.getText());
                user.setEmail(emailField.getText());
                Role role = new Role();
                role.setId(Integer.parseInt(roleIdField.getText()));
                user.setRole(role);

                userDao.save(user);
                outputArea.setText("Користувача додано успішно!");
            } catch (Exception ex) {
                outputArea.setText("Помилка: " + ex.getMessage());
            }
        });

        Button updateButton = new Button("Оновити користувача");
        updateButton.setOnAction(e -> {
            try {
                User user = new User();
                user.setId(Integer.parseInt(idField.getText()));
                user.setUsername(usernameField.getText());
                user.setEmail(emailField.getText());
                Role role = new Role();
                role.setId(Integer.parseInt(roleIdField.getText()));
                user.setRole(role);

                userDao.update(user);
                outputArea.setText("Користувача оновлено успішно!");
            } catch (Exception ex) {
                outputArea.setText("Помилка: " + ex.getMessage());
            }
        });

        Button deleteButton = new Button("Видалити користувача");
        deleteButton.setOnAction(e -> {
            try {
                Integer id = Integer.parseInt(idField.getText());
                userDao.delete(id);
                outputArea.setText("Користувача видалено успішно!");
            } catch (Exception ex) {
                outputArea.setText("Помилка: " + ex.getMessage());
            }
        });

        // Layout
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.setPadding(new Insets(10));
        inputGrid.add(new Label("ID:"), 0, 0);
        inputGrid.add(idField, 1, 0);
        inputGrid.add(new Label("Username:"), 0, 1);
        inputGrid.add(usernameField, 1, 1);
        inputGrid.add(new Label("Email:"), 0, 2);
        inputGrid.add(emailField, 1, 2);
        inputGrid.add(new Label("Role ID:"), 0, 3);
        inputGrid.add(roleIdField, 1, 3);

        VBox root = new VBox(10, inputGrid,
            findByIdButton, findAllButton, saveButton, updateButton, deleteButton, outputArea);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("User CRUD Application");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        try {
            ConnectionManager.open().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
