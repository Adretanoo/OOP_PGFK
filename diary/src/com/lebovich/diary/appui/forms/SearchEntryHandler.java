package com.lebovich.diary.appui.forms;

import com.lebovich.diary.persistence.entitys.Record;
import com.lebovich.diary.persistence.repository.EntrySearchRepository;
import java.util.List;
import java.util.Scanner;
import java.io.File;

/**
 * Клас, який обробляє пошук записів у щоденнику користувача.
 * Використовується для пошуку за заголовком або описом.
 */
public class SearchEntryHandler {

    private final EntrySearchRepository entryRepository;

    /**
     * Конструктор класу SearchEntryHandler.
     *
     * @param entryRepository Репозиторій для доступу до записів.
     */
    public SearchEntryHandler(EntrySearchRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    /**
     * Виконує пошук записів за заданими критеріями (заголовок чи опис).
     * Виводить знайдені записи або повідомлення про відсутність результатів.
     */
    public void execute(String userEmail) {
        // Перевірка наявності файлу для користувача
        String filePath = String.format("data/entries_%s.json", userEmail.replaceAll("[^a-zA-Z0-9]", "_"));
        File userFile = new File(filePath);

        if (!userFile.exists()) {
            System.out.println("❌ Для цього користувача не існує записів.");
            return; // Вихід, якщо файл не існує
        }

        List<Record> recordList = entryRepository.getAllEntries(userEmail);

        if (recordList == null || recordList.isEmpty()) {
            System.out.println("❌ Немає доступних записів для пошуку.");
            return;
        }

        System.out.println("=== Пошук запису ===");
        System.out.println("1. Пошук за заголовком");
        System.out.println("2. Пошук за описом");
        System.out.print("Виберіть критерій пошуку: ");
        int choice = new Scanner(System.in).nextInt();
        Scanner scanner = new Scanner(System.in); // Для зчитування рядків

        switch (choice) {
            case 1:
                System.out.print("Введіть заголовок запису для пошуку: ");
                String title = scanner.nextLine();
                searchByTitle(recordList, title);
                break;
            case 2:
                System.out.print("Введіть опис запису для пошуку: ");
                String description = scanner.nextLine();
                searchByDescription(recordList, description);
                break;
            default:
                System.out.println("Некоректний вибір.");
        }
    }

    /**
     * Шукає записи за заголовком і виводить інформацію про знайдені записи.
     * Якщо запис не знайдений, виводить повідомлення про це.
     *
     * @param recordList Список всіх записів для пошуку.
     * @param title Заголовок для пошуку.
     */
    private void searchByTitle(List<Record> recordList, String title) {
        boolean found = false;
        for (Record record : recordList) {
            if (record.getTitle().equalsIgnoreCase(title)) {
                printEntryInfo(record);
                found = true;
            }
        }
        if (!found) {
            System.out.println("❌ Запис з заголовком '" + title + "' не знайдено.");
        }
    }

    /**
     * Шукає записи за описом і виводить інформацію про знайдені записи.
     * Якщо запис не знайдений, виводить повідомлення про це.
     *
     * @param recordList Список всіх записів для пошуку.
     * @param description Опис для пошуку.
     */
    private void searchByDescription(List<Record> recordList, String description) {
        boolean found = false;
        for (Record record : recordList) {
            if (record.getDescription().equalsIgnoreCase(description)) {
                printEntryInfo(record);
                found = true;
            }
        }
        if (!found) {
            System.out.println("❌ Запис з описом '" + description + "' не знайдено.");
        }
    }

    /**
     * Виводить інформацію про знайдений запис.
     *
     * @param record Запис, інформацію про який потрібно вивести.
     */
    private void printEntryInfo(Record record) {
        System.out.println("\n=== Знайдений запис ===");
        System.out.println("Заголовок: " + record.getTitle());
        System.out.println("Опис: " + record.getDescription());
        System.out.println("Дата: " + record.getDate());
        if (record.getAttachmentPath() != null && !record.getAttachmentPath().isEmpty()) {
            System.out.println("Файл вкладення: " + record.getAttachmentPath());
        }
        System.out.println("-------------------------");
    }
}
