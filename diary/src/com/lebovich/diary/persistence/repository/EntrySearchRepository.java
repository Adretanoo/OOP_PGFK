package com.lebovich.diary.persistence.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lebovich.diary.persistence.entitys.Record;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class EntrySearchRepository {
    private static final String FILE_PATH_FORMAT = "data/entries_%s.json"; // Формат шляху для кожного користувача

    private Gson gson;

    public EntrySearchRepository() {
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
}
