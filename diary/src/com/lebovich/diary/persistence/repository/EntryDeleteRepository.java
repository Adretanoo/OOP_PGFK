package com.lebovich.diary.persistence.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lebovich.diary.persistence.entitys.Record;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Репозиторій для роботи з видаленням записів у щоденнику.
 * Забезпечує можливість отримання всіх записів для користувача та видалення конкретного запису з файлу.
 */
public class EntryDeleteRepository {

    private static final String FILE_PATH_FORMAT = "data/entries_%s.json"; // Формат шляху для кожного користувача

    private Gson gson;

    /**
     * Конструктор класу EntryDeleteRepository, ініціалізує об'єкт Gson для роботи з JSON.
     */
    public EntryDeleteRepository() {
        gson = new Gson();
    }

    /**
     * Завантажує всі записи користувача з відповідного JSON файлу.
     *
     * @param userEmail Електронна пошта користувача, яка використовується для визначення шляху до файлу.
     * @return Список всіх записів користувача або null у разі помилки.
     */
    public List<Record> getAllEntries(String userEmail) {
        String filePath = String.format(FILE_PATH_FORMAT, userEmail.replaceAll("[^a-zA-Z0-9]", "_"));
        try (FileReader reader = new FileReader(filePath)) {
            Type entryListType = new TypeToken<List<Record>>() {}.getType();
            return gson.fromJson(reader, entryListType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Видаляє конкретний запис із файлу.
     * Якщо запис має вкладення (фото), то також видаляється відповідний файл.
     * Після видалення запису, список записів зберігається назад у файл.
     *
     * @param record Запис, який необхідно видалити.
     * @param userEmail Електронна пошта користувача, для якого зберігається запис.
     */
    public void deleteEntry(Record record, String userEmail) {
        List<Record> recordList = getAllEntries(userEmail);
        if (recordList != null) {
            // Видалення запису з колекції за заголовком
            recordList.removeIf(e -> e.getTitle().equalsIgnoreCase(record.getTitle()));

            // Якщо запис містить вкладення (фото), спробуємо видалити файл
            if (record.getAttachmentPath() != null && !record.getAttachmentPath().isEmpty()) {
                File attachmentFile = new File(record.getAttachmentPath());
                if (attachmentFile.exists()) {
                    boolean deleted = attachmentFile.delete();
                    if (deleted) {
                        System.out.println("✅ Файл вкладення '" + attachmentFile.getName() + "' успішно видалено.");
                    } else {
                        System.out.println("❌ Не вдалося видалити файл вкладення '" + attachmentFile.getName() + "'.");
                    }
                }
            }

            // Запис оновленого списку в файл
            writeToFile(recordList, userEmail);
        }
    }

    /**
     * Записує список записів у файл після оновлення.
     *
     * @param recordList Список записів, який необхідно записати в файл.
     * @param userEmail Електронна пошта користувача для визначення шляху до файлу.
     */
    private void writeToFile(List<Record> recordList, String userEmail) {
        String filePath = String.format(FILE_PATH_FORMAT, userEmail.replaceAll("[^a-zA-Z0-9]", "_"));
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(recordList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
