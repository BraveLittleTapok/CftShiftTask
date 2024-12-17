package org.utils;

public class Info {

    public static void displayInfo() {
        String format = "%1$-12s %2$-1s%n";

        System.out.println("Поддерживаемые команды: ");
        System.out.printf(String.format(format, "-p [String]",
                "Указать префикс для имен файлов результата"));
        System.out.printf(String.format(format, "-o [String]",
                "Указать путь для файлов результата в виде строки"));
        System.out.printf(String.format(format, "-a", "добавляет результаты в уже имеющиеся файлы, " +
                "без указания содержание файлов будет перезаписано"));
        System.out.printf(String.format(format, "-s",
                "Получение краткой информации о записанных данных"));
        System.out.printf(String.format(format, "-f",
                "Получение полной информации о записанных данных"));
        System.out.println("Пожалуйста попробуйте еще раз ");

    }
}
