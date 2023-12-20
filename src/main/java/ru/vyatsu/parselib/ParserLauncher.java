package ru.vyatsu.parselib;

import ru.vyatsu.parselib.exception.ParsingRuntimeException;
import lombok.val;
import ru.vyatsu.parselib.parser.Completed;
import ru.vyatsu.parselib.parser.NewData;
import ru.vyatsu.parselib.parser.ParserWorker;
import lombok.extern.slf4j.Slf4j;

/**
 * Класс для запуска парсера сайтов.
 */
@Slf4j
public class ParserLauncher {
    private static final int SLEEP_TIME = 5000;
    private final String siteUrl;
    private int startPage;
    private int endPage;

    /**
     * Конструктор класса.
     *
     * @param siteUrl URL сайта для парсинга.
     */
    public ParserLauncher(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    /**
     * Запускает парсинг сайта без пагинации.
     */
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

    /**
     * Запускает парсинг сайта без пагинации с использованием значений по умолчанию.
     */
    public final void launchSiteWithoutPagination() {
        startPage = 1;
        endPage = 1;
        launchSite();
    }

    /**
     * Запускает парсинг сайта с пагинацией.
     *
     * @param startPage Начальная страница для парсинга.
     * @param endPage   Конечная страница для парсинга.
     */
    public final void launchSiteWithPagination (final int startPage, final int endPage){
        this.startPage = startPage;
        this.endPage = endPage;
        launchSite();
    }

    /**
     * Создает экземпляр парсера для указанного сайта.
     *
     * @param site Сайт, для которого необходимо создать парсер.
     * @return Экземпляр парсера.
     * @throws ParsingRuntimeException Если произошла ошибка при создании парсера.
     */
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

    /**
     * Приостанавливает выполнение текущего потока на указанное количество миллисекунд.
     * Если поток прерывается во время сна, устанавливается флаг прерывания.
     */
    private void sleepForMilliseconds() {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException exception) {
            log.error("Прерывание потока сна", exception);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Обработка исключения {@link ParsingRuntimeException}.
     *
     * @param exception Исключение {@link ParsingRuntimeException}.
     */
    private void handleParsingRuntimeException(final ParsingRuntimeException exception) {
        log.error("Ошибка при запуске сайта: {} ", exception.getMessage());
        throw exception;
    }

    /**
     * Обработка общего исключения.
     *
     * @param exception Общее исключение.
     */
    private void handleGeneralException(final Exception exception) {
        log.error("Необработанная ошибка при запуске сайта", exception);
    }
}