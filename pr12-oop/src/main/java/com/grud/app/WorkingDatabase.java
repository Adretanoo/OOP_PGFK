package com.grud.app;


import com.grud.dao.UserDao;
import com.grud.entity.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

public class WorkingDatabase {

    public static void main(String[] args) {
//        Шукає в асинхронному режимі
//        searchData();

        // Загружає фото для таблиці user
        uploadingPicture(5,"Images/photo1.jpg");
        uploadingPicture(6,"Images/photo2.png");
        uploadingPicture(7,"Images/photo3.png");
    }



    public static void uploadingPicture(Integer userId, String imagePath){

        try (InputStream inputStream = WorkingDatabase.class.getClassLoader()
            .getResourceAsStream(imagePath)) {

            if (inputStream != null) {
                byte[] imageBytes = inputStream.readAllBytes();
                UserDao userDao = UserDao.getInstance();
                boolean isUpdated = userDao.updateProfileImage(userId, imageBytes);

                if (isUpdated) {
                    System.out.println("Картинка успішно оновлена!");
                } else {
                    System.out.println("Не вдалося оновити картинку.");
                }
            } else {
                System.out.println("Файл не знайдено!");
            }
            Thread.sleep(2000);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void searchData(){
        loadUserData(50);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static byte[] loadImageFromFile(String path) throws IOException {
        Path filePath = Paths.get(path);
        return Files.readAllBytes(filePath);
    }

    public static void uploadImageToDatabase(int userId, String imagePath) {
        System.out.println("Завантаження картинки...");

        CompletableFuture.supplyAsync(() -> {
            try {
                byte[] imageBytes = loadImageFromFile(imagePath);
                UserDao userDao = UserDao.getInstance();
                return userDao.updateProfileImage(userId, imageBytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).thenAccept(success -> {
            if (success) {
                System.out.println("Картинка успішно збережена!");
            } else {
                System.out.println("Не вдалося зберегти картинку.");
            }
        }).exceptionally(ex -> {
            System.out.println("Сталася помилка: " + ex.getMessage());
            return null;
        });
    }

    public static void loadUserData(int userId) {
        System.out.println("Завантаження даних...");

        CompletableFuture.supplyAsync(() -> {
            UserDao userDao = UserDao.getInstance();
            return userDao.selectById(userId);
        }).thenAccept(userOptional -> {
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                System.out.println("Дані завантажено: " + user.getName());
            } else {
                System.out.println("Користувач не знайдений.");
            }
        }).exceptionally(ex -> {
            System.out.println("Сталася помилка: " + ex.getMessage());
            return null;
        });
    }
}
