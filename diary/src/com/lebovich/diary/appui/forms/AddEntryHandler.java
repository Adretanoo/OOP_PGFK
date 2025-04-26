package com.lebovich.diary.appui.forms;

import com.lebovich.diary.persistence.entitys.Record;
import com.lebovich.diary.persistence.repository.EntryAddRepository;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Клас для обробки додавання нового запису в особистий щоденник.
 */
public class AddEntryHandler {

    private final EntryAddRepository entryRepository;
    private final String userEmail;

    /**
     * Конструктор класу AddEntryHandler.
     *
     * @param entryRepository Репозиторій для збереження записів.
     * @param userEmail       Email користувача для організації медіа-файлів.
     */
    public AddEntryHandler(EntryAddRepository entryRepository, String userEmail) {
        this.entryRepository = entryRepository;
        this.userEmail = userEmail.replaceAll("[^a-zA-Z0-9]", "_"); // Видаляємо спецсимволи
    }

    /**
     * Виконує процес додавання нового запису до щоденника.
     */
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("=== Додавання нового запису ===");
        System.out.println("Для виходу введіть 'меню' у будь-який момент.");
        // Створюємо шлях до файлу з записами користувача
        String userEntriesFile = "data/entries_" + userEmail.replaceAll("[^a-zA-Z0-9]", "_") + ".json";

        // Завантаження всіх записів, щоб визначити останній ID
        List<Record> entries = entryRepository.loadEntries(userEntriesFile);
        int newId = entries.stream()
            .mapToInt(Record::getId)
            .max()
            .orElse(0) + 1; // Якщо записів немає, id починається з 1

        // Введення заголовка запису
        String title;
        do {
            System.out.println("Введіть заголовок запису:");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("меню")) return;
            title = input.trim();
        } while (title.isEmpty());

        // Введення тексту запису
        String description;
        do {
            System.out.println("Введіть опис запису:");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("меню")) return;
            description = input.trim();
        } while (description.isEmpty());

        // Отримання поточної дати й часу
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);

        // Вибір типу вкладення (фото, відео, аудіо або нічого)
        String attachmentPath = "";
        int choice;
        do {
            System.out.println("Додати вкладення? (1 - Фото, 2 - Відео, 3 - Аудіо, 4 - Без вкладення)");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("меню")) return;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                choice = -1;
            }
        } while (choice < 1 || choice > 4);

        if (choice != 4) {
            System.out.println("Вкажіть шлях до файлу:");
            String originalPath = scanner.nextLine().trim();
            attachmentPath = saveMediaFile(originalPath);
        }

        // Створення нового запису та його збереження
        Record newRecord = new Record(newId, title, description, formattedDate, attachmentPath);
        entries.add(newRecord);

        entryRepository.saveEntries(entries, userEntriesFile);
        System.out.println("Запис успішно додано:\n" + newRecord);
    }

    /**
     * Копіює медіафайл у директорію `data/media_{email}/`
     * @param filePath шлях до оригінального файлу
     * @return шлях до скопійованого файлу
     */
    private String saveMediaFile(String filePath) {
        try {
            File originalFile = new File(filePath);
            if (!originalFile.exists()) {
                System.out.println("❌ Файл не знайдено.");
                return "";
            }

            // Створюємо папку `data/media_{email}/`, якщо її немає
            File mediaDir = new File("data/media_" + userEmail);
            if (!mediaDir.exists()) {
                mediaDir.mkdirs();
            }

            // Формуємо шлях до нового файлу в папці користувача
            File newFile = new File(mediaDir, originalFile.getName());
            Files.copy(originalFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            System.out.println("✅ Файл збережено у: " + newFile.getAbsolutePath());
            return newFile.getAbsolutePath();
        } catch (Exception e) {
            System.out.println("❌ Помилка при збереженні файлу: " + e.getMessage());
            return "";
        }
    }
}
