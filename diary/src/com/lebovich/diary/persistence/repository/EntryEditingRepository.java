package com.lebovich.diary.persistence.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lebovich.diary.persistence.entitys.Record;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Репозиторій для редагування записів у файлі.
 */
public class EntryEditingRepository {

    private static final String FILE_PATH_FORMAT = "data/entries_%s.json"; // Формат шляху для кожного користувача
    private Gson gson;

    public EntryEditingRepository() {
        gson = new Gson();
    }

    /**
     * Завантажує всі записи користувача.
     *
     * @param userEmail Електронна пошта користувача.
     * @return Список записів або null у випадку помилки.
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
     * Оновлює запис у списку та зберігає його у файл.
     *
     * @param userEmail Електронна пошта користувача.
     * @param updatedEntries Список оновлених записів.
     */
    public void updateEntriesInFile(String userEmail, List<Record> updatedEntries) {
        String filePath = String.format(FILE_PATH_FORMAT, userEmail.replaceAll("[^a-zA-Z0-9]", "_"));
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(updatedEntries, writer); // Перетворюємо список записів на JSON і записуємо у файл
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Помилка при збереженні оновленого файлу.");
        }
    }

    /**
     * Оновлює конкретний запис у списку.
     *
     * @param userEmail Електронна пошта користувача.
     * @param entryId ID запису для оновлення.
     * @param newRecord Нові дані запису.
     * @return true, якщо запис оновлено, false, якщо не знайдено.
     */
    public boolean updateEntry(String userEmail, int entryId, Record newRecord) {
        List<Record> entries = getAllEntries(userEmail);
        if (entries != null) {
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).getId() == entryId) {
                    entries.set(i, newRecord);  // Оновлюємо запис
                    updateEntriesInFile(userEmail, entries); // Зберігаємо оновлений список записів у файл
                    return true;
                }
            }
        }
        return false; // Якщо запис не знайдений
    }
}
