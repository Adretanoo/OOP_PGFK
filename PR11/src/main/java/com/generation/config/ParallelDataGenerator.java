package com.generation.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.github.javafaker.Faker;
import com.generation.config.util.ConnectionManager;

public class ParallelDataGenerator {
    private static final int TOTAL_RECORDS = 1_000_000;
    private static final int THREAD_COUNT = Math.max(4, Runtime.getRuntime().availableProcessors());  // Максимальна кількість потоків
    private static final Faker faker = new Faker();

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        System.out.println("Старт генерації даних...");

        // Використовуємо пул потоків для паралельної обробки
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        // Кількість записів, яку оброблятиме кожен потік
        int recordsPerThread = TOTAL_RECORDS / THREAD_COUNT;

        // Останній потік може отримати залишок
        int remainingRecords = TOTAL_RECORDS % THREAD_COUNT;

        // Створення потоків
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int start = i * recordsPerThread;
            final int count = (i == THREAD_COUNT - 1) ? recordsPerThread + remainingRecords : recordsPerThread;
            executorService.submit(() -> insertBatch(start, count));
        }

        // Завершення роботи ExecutorService
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.MINUTES)) {
                System.out.println("❌ Тайм-аут: не всі потоки завершились!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("✅ Завершено за " + (endTime - startTime) / 1000 + " секунд");

        ConnectionManager.closePool();
    }

    private static void insertBatch(int start, int count) {
        try (Connection connection = ConnectionManager.get()) {
            String sql = "INSERT INTO workers (name, age, salary, experience, education, gender, worked_hours) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            for (int i = 0; i < count; i++) {
                statement.setString(1, faker.name().fullName());
                statement.setInt(2, faker.number().numberBetween(18, 65));
                statement.setDouble(3, faker.number().randomDouble(2, 500, 5000));
                statement.setInt(4, faker.number().numberBetween(0, 40));
                statement.setString(5, faker.educator().course());
                statement.setString(6, faker.options().option("Male", "Female"));
                statement.setInt(7, faker.number().numberBetween(20, 200));

                statement.addBatch();

                // Виконуємо пакет вставок кожні 10 000 записів
                if (i % 10_000 == 0) {
                    statement.executeBatch();
                    statement.clearBatch();
                    System.out.println(Thread.currentThread().getName() + " -> " + i + " записів вставлено...");
                }
            }

            // Виконуємо залишкові вставки
            statement.executeBatch();
            statement.close();

            System.out.println("✔ " + count + " записів вставлено потоком: " + Thread.currentThread().getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
