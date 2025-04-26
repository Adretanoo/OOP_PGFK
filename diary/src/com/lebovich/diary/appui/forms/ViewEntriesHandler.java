package com.lebovich.diary.appui.forms;

import com.lebovich.diary.persistence.entitys.Record;
import com.lebovich.diary.persistence.repository.EntryViewingRepository;

import java.io.File;
import java.util.List;

/**
 * Клас для перегляду записів у щоденнику.
 * Цей клас відповідає за відображення всіх записів конкретного користувача, що зберігаються у файлі.
 */
public class ViewEntriesHandler {
    private final EntryViewingRepository entryRepository;
    private final String userEmail;

    /**
     * Конструктор класу ViewEntriesHandler.
     * Ініціалізує обробник з репозиторієм записів та електронною поштою користувача.
     *
     * @param entryRepository Репозиторій для доступу до записів користувача.
     * @param userEmail      Електронна пошта користувача, для якого потрібно відображати записи.
     */
    public ViewEntriesHandler(EntryViewingRepository entryRepository, String userEmail) {
        this.entryRepository = entryRepository;
        this.userEmail = userEmail.replaceAll("[^a-zA-Z0-9]", "_"); // Форматування email
    }

    /**
     * Відображає список записів користувача.
     * Завантажує записи з файлу, який відповідає електронній пошті користувача, і виводить їх у консоль.
     * Якщо записи відсутні, виводиться повідомлення про відсутність записів.
     */
    public void execute() {
        // Формуємо шлях до файлу записів користувача
        String userEntriesFile = "data/entries_" + userEmail + ".json"; // Файл конкретного користувача
        List<Record> recordList = entryRepository.getAllEntries(userEntriesFile);

        // Перевірка наявності записів у списку
        if (recordList != null && !recordList.isEmpty()) {
            System.out.println("\n=== Ваші записи ===");
            // Виводимо інформацію про кожен запис
            for (Record record : recordList) {
                System.out.println("ID: " + record.getId());
                System.out.println("Заголовок: " + record.getTitle());
                System.out.println("Опис: " + record.getDescription());
                System.out.println("Дата: " + record.getDate());

                // Перевірка наявності вкладення та його існування
                if (record.getAttachmentPath() != null && !record.getAttachmentPath().isEmpty()) {
                    File file = new File(record.getAttachmentPath());
                    if (file.exists()) {
                        System.out.println("[Файл вкладення: " + file.getAbsolutePath() + "]");
                    } else {
                        System.out.println("[Файл не знайдено: " + record.getAttachmentPath() + "]");
                    }
                }
                System.out.println("-------------------------");
            }
        } else {
            System.out.println("❌ У вас ще немає записів.");
        }
    }
}
