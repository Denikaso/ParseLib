package ru.vyatsu.parselib;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import java.util.HashMap;
import java.util.Map;
import static java.lang.System.*;

@NoArgsConstructor
@Slf4j
public class InputHandler {
    public void handleArguments(String[] args) {
        if (args.length == 0) {
            err.println("Ошибка: Не указаны аргументы. Используйте: java -jar файл.jar ссылка_на_сайт [начальная_страница конечная_страница]");
            log.error("Не указаны аргументы при запуске программы.");
            return;
        }

        val siteUrl = args[0];
        val parserLauncher = new ParserLauncher(siteUrl);

        Map<Integer, Runnable> actions = new HashMap<>();
        actions.put(1, () -> {
            log.info("Запущен парсинг сайта {} без пагинации.", siteUrl);
            parserLauncher.launchSiteWithoutPagination();
        });
        actions.put(3, () -> {
            try {
                val startPage = Integer.parseInt(args[1]);
                val endPage = Integer.parseInt(args[2]);
                log.info("Запущен парсинг сайта {} с пагинацией от страницы {} до {}.",siteUrl, startPage, endPage);
                parserLauncher.launchSiteWithPagination(startPage, endPage);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException exception) {
                log.error("Ошибка при разборе аргументов для парсинга сайта с пагинацией", exception);
            }
        });

        actions.getOrDefault(args.length, () -> log.error("Неверное количество аргументов")).run();
    }
}
