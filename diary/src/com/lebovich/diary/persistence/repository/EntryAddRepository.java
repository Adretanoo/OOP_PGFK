package com.lebovich.diary.persistence.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lebovich.diary.persistence.entitys.Record;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторій для управління записами в особистому щоденнику.
 * Цей клас забезпечує функціональність збереження та завантаження записів у/з JSON файлу.
 */
public class EntryAddRepository {

    private static final String FILE_PATH = "data/entries.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Зберігає список записів у JSON-файл.
     *
     * Цей метод використовує бібліотеку Gson для серіалізації списку записів у формат JSON
     * і записує його в зазначений файл. Якщо під час збереження виникає помилка,
     * вона буде зафіксована в консолі.
     *
     * @param entries Список записів, які необхідно зберегти.
     * @param filePath Шлях до файлу, в який буде записано список записів.
     */
    public void saveEntries(List<Record> entries, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            new Gson().toJson(entries, writer);
        } catch (IOException e) {
            System.out.println("Помилка при збереженні записів.");
        }
    }

    /**
     * Завантажує список записів з JSON-файлу.
     *
     * Цей метод відкриває вказаний файл, десеріалізує JSON в об'єкти типу {@link Record} і
     * повертає їх у вигляді списку. Якщо файл не існує або виникає помилка при зчитуванні,
     * метод повертає порожній список.
     *
     * @param filePath Шлях до файлу, з якого потрібно завантажити записи.
     * @return Список записів або порожній список у разі помилки.
     */
    public List<Record> loadEntries(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return new ArrayList<>();

        try (FileReader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<ArrayList<Record>>() {}.getType();
            List<Record> entries = new Gson().fromJson(reader, listType);
            return entries != null ? entries : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
