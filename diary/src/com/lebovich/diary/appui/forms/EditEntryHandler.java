package com.lebovich.diary.appui.forms;

import com.lebovich.diary.persistence.entitys.Record;
import com.lebovich.diary.persistence.repository.EntryEditingRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Клас для обробки редагування запису в особистому щоденнику.
 */
public class EditEntryHandler {

    private final EntryEditingRepository entryEditingRepository;

    public EditEntryHandler(EntryEditingRepository entryEditingRepository) {
        this.entryEditingRepository = entryEditingRepository;
    }

    /**
     * Виконує редагування запису користувача за id.
     */
    public void execute(String userEmail) {
        List<Record> entries = entryEditingRepository.getAllEntries(userEmail);

        if (entries == null || entries.isEmpty()) {
            System.out.println("❌ Немає доступних записів для редагування.");
            return;
        }

        System.out.println("=== Редагування запису ===");
        System.out.print("Введіть id запису для редагування: ");
        Scanner scanner = new Scanner(System.in);
        int entryId = scanner.nextInt();
        scanner.nextLine(); // Скидаємо буфер

        // Шукаємо запис для редагування
        Record recordToEdit = null;
        for (Record record : entries) {
            if (record.getId() == entryId) {
                recordToEdit = record;
                break;
            }
        }

        if (recordToEdit == null) {
            System.out.println("❌ Запис з таким id не знайдено.");
            return;
        }

        // Виведення поточного стану запису
        System.out.println("Поточний заголовок: " + recordToEdit.getTitle());
        System.out.println("Поточний опис: " + recordToEdit.getDescription());
        System.out.println("Поточна дата: " + recordToEdit.getDate());
        System.out.println("Поточний файл вкладення: " + recordToEdit.getAttachmentPath());

        // Запитуємо користувача, які поля він хоче редагувати
        System.out.print("Оберіть поля для редагування (через кому): 1. Заголовок, 2. Опис, 3. Вкладення, 4. Всі поля: ");
        String fieldsToEdit = scanner.nextLine();

        // Розділяємо введені поля на окремі значення
        String[] fields = fieldsToEdit.split(",");
        String newAttachmentPath = null;

        for (String field : fields) {
            switch (field.trim()) {
                case "1":
                    System.out.print("Введіть новий заголовок (залиште порожнім, якщо не хочете змінювати): ");
                    String newTitle = scanner.nextLine();
                    if (!newTitle.isEmpty()) {
                        recordToEdit.setTitle(newTitle);
                    }
                    break;
                case "2":
                    System.out.print("Введіть новий опис (залиште порожнім, якщо не хочете змінювати): ");
                    String newDescription = scanner.nextLine();
                    if (!newDescription.isEmpty()) {
                        recordToEdit.setDescription(newDescription);
                    }
                    break;
                case "3":
                    System.out.print("Введіть новий шлях до вкладення (залиште порожнім, якщо не хочете змінювати): ");
                    newAttachmentPath = scanner.nextLine();
                    if (!newAttachmentPath.isEmpty()) {
                        recordToEdit.setAttachmentPath(newAttachmentPath);
                    }
                    break;
                case "4":
                    // Оновлюємо всі поля
                    System.out.print("Введіть новий заголовок (залиште порожнім, якщо не хочете змінювати): ");
                    String newTitleAll = scanner.nextLine();
                    if (!newTitleAll.isEmpty()) {
                        recordToEdit.setTitle(newTitleAll);
                    }

                    System.out.print("Введіть новий опис (залиште порожнім, якщо не хочете змінювати): ");
                    String newDescriptionAll = scanner.nextLine();
                    if (!newDescriptionAll.isEmpty()) {
                        recordToEdit.setDescription(newDescriptionAll);
                    }

                    System.out.print("Введіть новий шлях до вкладення (залиште порожнім, якщо не хочете змінювати): ");
                    newAttachmentPath = scanner.nextLine();
                    if (!newAttachmentPath.isEmpty()) {
                        recordToEdit.setAttachmentPath(newAttachmentPath);
                    }
                    break;
                default:
                    System.out.println("❌ Невірний вибір: " + field);
                    return;
            }
        }

        // Якщо вкладення змінилося, потрібно видалити старий файл і зберегти новий
        if (newAttachmentPath != null && !newAttachmentPath.equals(recordToEdit.getAttachmentPath())) {
            handleAttachmentChange(recordToEdit.getAttachmentPath(), newAttachmentPath);
        }

        // Оновлюємо дату на поточну
        recordToEdit.setDate(getCurrentDateTime());

        boolean success = entryEditingRepository.updateEntry(userEmail, entryId, recordToEdit);

        if (success) {
            System.out.println("✅ Запис успішно оновлено!");
        } else {
            System.out.println("❌ Помилка при оновленні запису.");
        }
    }

    /**
     * Обробляє зміни вкладеного файлу:
     * 1. Видаляє старий файл.
     * 2. Копіює новий файл за новим шляхом.
     *
     * @param oldAttachmentPath Шлях до старого вкладення.
     * @param newAttachmentPath Шлях до нового вкладення.
     */
    private void handleAttachmentChange(String oldAttachmentPath, String newAttachmentPath) {
        // Видалення старого файлу, якщо він існує
        if (oldAttachmentPath != null && !oldAttachmentPath.isEmpty()) {
            File oldFile = new File(oldAttachmentPath);
            if (oldFile.exists()) {
                boolean deleted = oldFile.delete();
                if (deleted) {
                    System.out.println("✅ Старий файл вкладення успішно видалено.");
                } else {
                    System.out.println("❌ Не вдалося видалити старий файл вкладення.");
                }
            }
        }

        // Копіюємо новий файл в папку для збереження
        try {
            Path sourcePath = Paths.get(newAttachmentPath);
            Path destinationPath = Paths.get("data/" + new File(newAttachmentPath).getName()); // Вказуємо шлях збереження в папці data

            if (Files.exists(sourcePath)) {
                Files.copy(sourcePath, destinationPath);
                System.out.println("✅ Новий файл вкладення успішно додано.");
            } else {
                System.out.println("❌ Джерело файлу не знайдено: " + newAttachmentPath);
            }
        } catch (IOException e) {
            System.out.println("❌ Помилка при додаванні нового файлу вкладення: " + e.getMessage());
        }
    }

    /**
     * Отримує поточну дату та час у форматі String.
     *
     * @return Поточну дату та час у вигляді рядка.
     */
    private String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}
