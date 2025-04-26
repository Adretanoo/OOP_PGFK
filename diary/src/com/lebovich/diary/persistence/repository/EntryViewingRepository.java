package com.lebovich.diary.persistence.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lebovich.diary.persistence.entitys.Record;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторій для перегляду записів у щоденнику.
 */
public class EntryViewingRepository {
    private final Gson gson;

    public EntryViewingRepository() {
        this.gson = new Gson();
    }

    /**
     * Завантажує список записів для конкретного користувача.
     *
     * @param filePath шлях до файлу записів користувача
     * @return Список записів або порожній список у разі помилки.
     */
    public List<Record> getAllEntries(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>(); // Якщо файлу немає, повертаємо порожній список
        }

        try (FileReader reader = new FileReader(filePath)) {
            Type entryListType = new TypeToken<List<Record>>() {}.getType();
            List<Record> entries = gson.fromJson(reader, entryListType);
            return entries != null ? entries : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("❌ Помилка при зчитуванні файлу: " + filePath);
            return new ArrayList<>();
        }
    }
}
