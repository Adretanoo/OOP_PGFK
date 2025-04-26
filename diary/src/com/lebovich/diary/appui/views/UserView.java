package com.lebovich.diary.appui.views;

import com.lebovich.diary.appui.forms.AddEntryHandler;
import com.lebovich.diary.appui.forms.DeleteEntryHandler;
import com.lebovich.diary.appui.forms.EditEntryHandler;
import com.lebovich.diary.appui.forms.SearchEntryHandler;
import com.lebovich.diary.appui.forms.ViewEntriesHandler;
import com.lebovich.diary.domain.validators.InputUserMenuValidator;
import com.lebovich.diary.persistence.repository.EntryAddRepository;
import com.lebovich.diary.persistence.repository.EntryDeleteRepository;
import com.lebovich.diary.persistence.repository.EntryEditingRepository;
import com.lebovich.diary.persistence.repository.EntrySearchRepository;
import com.lebovich.diary.persistence.repository.EntryViewingRepository;

/**
 * Клас, що відповідає за відображення та обробку меню користувача.
 * Цей клас дозволяє користувачу виконувати різні операції з записами у щоденнику,
 * такі як додавання, видалення, редагування, перегляд та пошук записів.
 */
public class UserView {

    private final String userEmail;

    /**
     * Конструктор класу UserView.
     * Ініціалізує об'єкт з електронною поштою користувача.
     *
     * @param userEmail Електронна пошта користувача.
     */
    public UserView(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * Виводить меню та обробляє вибір користувача.
     * Цей метод виконується в циклі, поки користувач не вибере опцію для виходу.
     * Меню дозволяє додавати, видаляти, редагувати, переглядати та шукати записи.
     */
    public void display() {

        while (true) {
            System.out.println("\n┌──────────────────────────┐");
            System.out.println("│      Мій щоденник        │");
            System.out.println("├──────────────────────────┤");
            System.out.println("│ 1. Додати новий запис    │");
            System.out.println("│ 2. Видалити запис        │");
            System.out.println("│ 3. Перегляд записів      │");
            System.out.println("│ 4. Пошук запису          │");
            System.out.println("│ 5. Редагувати запис      │");
            System.out.println("│ 6. Вихід в головне меню  │");
            System.out.println("└──────────────────────────┘");

            // Отримуємо вибір користувача з меню
            int choice = InputUserMenuValidator.getValidatedOption();

            // Обробляємо вибір користувача
            switch (choice) {
                case 1:
                    addRecord();
                    break;
                case 2:
                    deleteRecord();
                    break;
                case 3:
                    viewRecords();
                    break;
                case 4:
                    searchRecord();
                    break;
                case 5:
                    editRecord();
                    break;
                case 6:
                    System.out.println("Вихід з меню адміністратора....");
                    new MainMenuView().display();
                    return;
            }
        }
    }

    /**
     * Додає новий запис у щоденник.
     * Для цього використовується обробник AddEntryHandler.
     */
    private void addRecord() {
        EntryAddRepository entryRepository = new EntryAddRepository();
        AddEntryHandler handler = new AddEntryHandler(entryRepository, userEmail);
        handler.execute();
    }

    /**
     * Видаляє запис з щоденника.
     * Для цього використовується обробник DeleteEntryHandler.
     */
    private void deleteRecord() {
        EntryDeleteRepository entryRepository = new EntryDeleteRepository();
        DeleteEntryHandler handler = new DeleteEntryHandler(entryRepository);
        handler.execute(userEmail);
    }

    /**
     * Переглядає всі записи в щоденнику.
     * Для цього використовується обробник ViewEntriesHandler.
     */
    private void viewRecords() {
        EntryViewingRepository entryRepository = new EntryViewingRepository();
        ViewEntriesHandler handler = new ViewEntriesHandler(entryRepository, userEmail);
        handler.execute();
    }

    /**
     * Шукає записи за певними критеріями.
     * Для цього використовується обробник SearchEntryHandler.
     */
    private void searchRecord() {
        EntrySearchRepository entryRepository = new EntrySearchRepository();
        SearchEntryHandler handler = new SearchEntryHandler(entryRepository);
        handler.execute(userEmail);
    }

    /**
     * Редагує запис у щоденнику.
     * Для цього використовується обробник EditEntryHandler.
     */
    private void editRecord() {
        EntryEditingRepository entryRepository = new EntryEditingRepository();
        EditEntryHandler handler = new EditEntryHandler(entryRepository);
        handler.execute(userEmail);
    }
}