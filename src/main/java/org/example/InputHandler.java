package org.example;

import lombok.NoArgsConstructor;
import lombok.val;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static java.lang.System.*;

@NoArgsConstructor
public class InputHandler {
    private static final Logger logger = LogManager.getLogger(InputHandler.class);
    public void handleArguments(String[] args) {
        if (args.length == 0) {
            err.println("Ошибка: Не указаны аргументы. Используйте: java -jar файл.jar ссылка_на_сайт [начальная_страница конечная_страница]");
            logger.error("Не указаны аргументы при запуске программы.");
            return;
        }

        val siteUrl = args[0];
        val parserLauncher = new ParserLauncher(siteUrl);

        Map<Integer, Runnable> actions = new HashMap<>();
        actions.put(1, () -> {
            logger.info("Запущен парсинг сайта {} без пагинации.", siteUrl);
            parserLauncher.launchSiteWithoutPagination();
        });
        actions.put(3, () -> {
            try {
                val startPage = Integer.parseInt(args[1]);
                val endPage = Integer.parseInt(args[2]);
                logger.info("Запущен парсинг сайта {} с пагинацией от страницы {} до {}.",siteUrl, startPage, endPage);
                parserLauncher.launchSiteWithPagination(startPage, endPage);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException exception) {
                logger.error("Ошибка при разборе аргументов для парсинга сайта с пагинацией", exception);
            }
        });

        actions.getOrDefault(args.length, () -> logger.error("Неверное количество аргументов")).run();
    }
}
