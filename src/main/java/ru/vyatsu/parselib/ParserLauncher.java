package ru.vyatsu.parselib;

import ru.vyatsu.parselib.exception.ParsingRuntimeException;
import lombok.val;
import ru.vyatsu.parselib.parser.Completed;
import ru.vyatsu.parselib.parser.NewData;
import ru.vyatsu.parselib.parser.ParserWorker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParserLauncher {
    private static final int SLEEP_TIME = 5000;
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
                log.error("Неизвестный сайт: {} ", siteUrl);
                return;
            }

            ParserWorker<?> parser = createParser(site);

            parser.getOnCompletedList().add(new Completed());
            parser.getOnNewDataList().add(new NewData<>());
            parser.start();

            sleepForMilliseconds();

            parser.abort();
            log.info("Парсинг сайта {} завершен", siteUrl);
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

    public final void launchSiteWithPagination (final int startPage, final int endPage){
        this.startPage = startPage;
        this.endPage = endPage;
        launchSite();
    }

    private ParserWorker<?> createParser(final Site site) {
        try {
            ParserWorker<?> parser = new ParserWorker<>(site.getParserClass()
                    .getDeclaredConstructor()
                    .newInstance());
            parser.setParserSetting(site.getSettingClass()
                    .getDeclaredConstructor(int.class, int.class)
                    .newInstance(startPage, endPage));
            return parser;
        } catch (Exception exception) {
            log.error("Ошибка при создании парсера", exception);
            throw new ParsingRuntimeException("Ошибка при создании парсера", exception);
        }
    }

    private void sleepForMilliseconds() {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException exception) {
            log.error("Прерывание потока сна", exception);
            Thread.currentThread().interrupt();
        }
    }

    private void handleParsingRuntimeException(final ParsingRuntimeException exception) {
        log.error("Ошибка при запуске сайта: {} ", exception.getMessage());
        throw exception;
    }

    private void handleGeneralException(final Exception exception) {
        log.error("Необработанная ошибка при запуске сайта", exception);
    }

}