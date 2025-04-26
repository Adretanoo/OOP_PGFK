package com.lebovich.diary.appui.forms;

import com.lebovich.diary.persistence.repository.EntryDeleteRepository;
import com.lebovich.diary.persistence.entitys.Record;
import java.util.Scanner;
import java.io.File;

/**
 * Клас для обробки видалення запису в особистому щоденнику.
 * Використовується для пошуку та видалення запису з файлу.
 */
public class DeleteEntryHandler {

    private final EntryDeleteRepository entryRepository;

    /**
     * Конструктор класу DeleteEntryHandler.
     *
     * @param entryRepository Репозиторій для видалення записів.
     */
    public DeleteEntryHandler(EntryDeleteRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    /**
     * Виконує видалення запису з особистого щоденника. Користувач може ввести заголовок запису для видалення.
     * Якщо введено неправильний заголовок або команду "меню", операція буде скасована.
     */
    public void execute(String userEmail) {
        Scanner scanner = new Scanner(System.in);
        String input;

        // Перевірка наявності файлу для користувача
        String filePath = String.format("data/entries_%s.json", userEmail.replaceAll("[^a-zA-Z0-9]", "_"));
        File userFile = new File(filePath);

        if (!userFile.exists()) {
            System.out.println("❌ Для цього користувача не існує записів.");
            return; // Вихід, якщо файл не існує
        }

        System.out.println("=== Видалення запису ===");
        System.out.println("Для повернення в меню введіть 'меню' у будь-який момент.");

        String title;
        do {
            System.out.println("Введіть заголовок запису для видалення:");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("меню")) return; // Повернення в меню
            title = input;
        } while (title.isEmpty()); // Перевірка, що заголовок не порожній

        Record recordToDelete = null;
        // Пошук запису за заголовком у списку всіх записів
        for (Record record : entryRepository.getAllEntries(userEmail)) {
            if (record.getTitle().equalsIgnoreCase(title)) {
                recordToDelete = record;
                break;
            }
        }

        // Видалення запису, якщо знайдено
        if (recordToDelete != null) {
            entryRepository.deleteEntry(recordToDelete, userEmail);
            System.out.println("✅ Запис з заголовком '" + title + "' успішно видалено.");
        } else {
            System.out.println("❌ Запис з заголовком '" + title + "' не знайдено.");
        }
    }
}
