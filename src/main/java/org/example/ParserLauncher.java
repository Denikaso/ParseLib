package org.example;

import org.example.exceptions.ParsingRuntimeException;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.parser.Completed;
import org.example.parser.NewData;
import org.example.parser.ParserWorker;
import java.io.IOException;

public class ParserLauncher {
    private static final int SLEEP_TIME = 5000;
    private final Logger logger = LogManager.getLogger(ParserLauncher.class);
    private final String siteUrl;
    private int startPage;
    private int endPage;

    public ParserLauncher(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public final void launchSite() {
        try {
            val site = Site.fromUrl(siteUrl);

            if (site == null) {
                logger.error("Неизвестный сайт: {} ", siteUrl);
                return;
            }

            ParserWorker<?> parser = createParser(site);

            parser.getOnCompletedList().add(new Completed());
            parser.getOnNewDataList().add(new NewData<>());
            parser.start();

            sleepForMilliseconds();

            parser.abort();
            logger.info("Парсинг сайта {} завершен", siteUrl);
        } catch (IOException exception) {
            handleIOException(exception);
        } catch (ParsingRuntimeException exception) {
            handleParsingRuntimeException(exception);
        } catch (Exception exception) {
            handleGeneralException(exception);
        }
    }

    public final void launchSiteWithoutPagination() {
        startPage = 1;
        endPage = 1;
        launchSite();
    }

    public final void launchSiteWithPagination (int startPage, int endPage){
        this.startPage = startPage;
        this.endPage = endPage;
        launchSite();
    }
    private ParserWorker<?> createParser(Site site) {
        try {
            ParserWorker<?> parser = new ParserWorker<>(site.getParserClass()
                    .getDeclaredConstructor()
                    .newInstance());
            parser.setParserSetting(site.getSettingClass()
                    .getDeclaredConstructor(int.class, int.class)
                    .newInstance(startPage, endPage));
            return parser;
        } catch (Exception exception) {
            logger.error("Ошибка при создании парсера", exception);
            throw new ParsingRuntimeException("Ошибка при создании парсера", exception);
        }
    }
    private void sleepForMilliseconds() {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException exception) {
            logger.error("Прерывание потока сна", exception);
            Thread.currentThread().interrupt();
        }
    }
    private void handleIOException(IOException exception) {
        logger.error("Ошибка при запуске сайта", exception);
        throw new ParsingRuntimeException("Ошибка при запуске сайта", exception);
    }

    private void handleParsingRuntimeException(ParsingRuntimeException exception) {
        logger.error("Ошибка при запуске сайта: {} ", exception.getMessage());
        throw exception;
    }

    private void handleGeneralException(Exception exception) {
        logger.error("Необработанная ошибка при запуске сайта", exception);
    }

}