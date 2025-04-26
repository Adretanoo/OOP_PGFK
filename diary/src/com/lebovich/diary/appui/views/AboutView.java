package com.lebovich.diary.appui.views;

/**
 * Клас, що відповідає за відображення інформації про програму.
 * Містить опис програми та її можливості для користувачів.
 */
public class AboutView {

    /**
     * Виводить інформацію про програму в консоль.
     * Описує можливості програми та її версію.
     */
    public void display() {
        System.out.println("┌──────────────────────────┐");
        System.out.println("│      Про програму        │");
        System.out.println("└──────────────────────────┘");
        System.out.println("Особистий щоденник – це ваш цифровий простір для збереження думок,");
        System.out.println("емоцій, натхнення та улюбленого контенту.                          ");
        System.out.println("                                                                   ");
        System.out.println("За допомогою цієї програми ви можете записувати свої нотатки,     ");
        System.out.println("додавати музику та відео, а також організовувати свій щоденник так,");
        System.out.println("як вам зручно.                                                    ");
        System.out.println("                                                                   ");
        System.out.println("Функції програми:                                                 ");
        System.out.println("- Створення та редагування записів                                 ");
        System.out.println("- Збереження важливих моментів у вигляді тексту, аудіо чи відео   ");
        System.out.println("- Пошук по записах                                                 ");
        System.out.println("- Захист особистих даних                                           ");
        System.out.println("                                                                   ");
        System.out.println("Версія 1.0                                                         ");
        System.out.println();
    }

}
