package com.lebovich.diary.persistence.entitys;

/**
 * Клас, що представляє запис у щоденнику.
 * Цей клас містить інформацію про заголовок, опис, дату та шлях до вкладеного файлу
 * для кожного запису в особистому щоденнику.
 */
public class Record {

    private int id;  // Унікальний ідентифікатор запису
    private String title;  // Заголовок запису
    private String description;  // Опис запису
    private String date;  // Дата запису
    private String attachmentPath;  // Шлях до вкладеного файлу (наприклад, фото або документ)

    /**
     * Конструктор класу Record.
     *
     * @param id Унікальний ідентифікатор запису.
     * @param title Заголовок запису.
     * @param description Опис запису.
     * @param date Дата запису.
     * @param attachmentPath Шлях до вкладеного файлу.
     */
    public Record(int id, String title, String description, String date, String attachmentPath) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.attachmentPath = attachmentPath;
    }

    // Геттери та сеттери

    /**
     * Отримує ідентифікатор запису.
     *
     * @return Ідентифікатор запису.
     */
    public int getId() {
        return id;
    }

    /**
     * Встановлює ідентифікатор запису.
     *
     * @param id Ідентифікатор запису.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Отримує заголовок запису.
     *
     * @return Заголовок запису.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Встановлює заголовок запису.
     *
     * @param title Заголовок запису.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Отримує опис запису.
     *
     * @return Опис запису.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Встановлює опис запису.
     *
     * @param description Опис запису.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Отримує дату запису.
     *
     * @return Дата запису.
     */
    public String getDate() {
        return date;
    }

    /**
     * Встановлює дату запису.
     *
     * @param date Дата запису.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Отримує шлях до вкладеного файлу.
     *
     * @return Шлях до вкладеного файлу.
     */
    public String getAttachmentPath() {
        return attachmentPath;
    }

    /**
     * Встановлює шлях до вкладеного файлу.
     *
     * @param attachmentPath Шлях до вкладеного файлу.
     */
    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    /**
     * Переопреділяє метод toString для представлення інформації про запис у зручному форматі.
     * Виводить ID, заголовок, дату, опис та шлях до вкладення (якщо воно є).
     *
     * @return Строкове представлення запису.
     */
    @Override
    public String toString() {
        return "ID: " + id + "\nЗаголовок: " + title + "\nДата: " + date +
            "\nОпис: " + description + (attachmentPath.isEmpty() ? "" : "\nВкладення: " + attachmentPath);
    }
}
