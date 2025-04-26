import com.jdbc.util.ConnectionManager;
import java.sql.Connection;
import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\nМеню:");
            System.out.println("1. Додати користувача");
            System.out.println("2. Показати всіх користувачів");
            System.out.println("3. Вийти");
            System.out.print("Вибір: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addUser(scanner);
                    break;
                case 2:
                    showUsers();
                    break;
                case 3:
                    System.out.println("Вихід...");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Невірний вибір, спробуйте ще раз.");
            }
        }

        scanner.close();
    }

    private static void addUser(Scanner scanner) {
        System.out.print("Введіть логін: ");
        String login = scanner.nextLine();

        System.out.print("Введіть email: ");
        String email = scanner.nextLine();

        System.out.print("Введіть пароль: ");
        String password = scanner.nextLine();

        System.out.print("Введіть адресу: ");
        String address = scanner.nextLine();

        System.out.print("Введіть телефон: ");
        String phone = scanner.nextLine();

        String query = "INSERT INTO users_validation (login, email, password, address, phone) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionManager.open();
            PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, login);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, address);
            statement.setString(5, phone);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Користувача успішно додано!");
            } else {
                System.out.println("Не вдалося додати користувача.");
            }
        } catch (SQLException e) {
            System.out.println("Помилка під час додавання користувача: " + e.getMessage());
        }
    }

    private static void showUsers() {
        String query = "SELECT * FROM users_validation";

        try (Connection connection = ConnectionManager.open();
            PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String login = resultSet.getString("login");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");

                System.out.println("ID: " + id + ", Login: " + login + ", Email: " + email + ", Address: " + address + ", Phone: " + phone);
            }
        } catch (SQLException e) {
            System.out.println("Помилка при отриманні користувачів: " + e.getMessage());
        }
    }
}