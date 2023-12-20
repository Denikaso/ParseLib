package ru.vyatsu.parselib.parser;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import ru.vyatsu.parselib.exception.ParsingRuntimeException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Класс, представляющий собой рабочего исполнителя для парсера. Осуществляет парсинг страниц с использованием заданного парсера и настроек.
 *
 * @param <T> Тип данных, возвращаемых парсером.
 */
@Data
@Slf4j
public class ParserWorker<T> {
    private final Parser<T> parser;
    private ParserSetting parserSetting;
    private HtmlLoader loader;
    private boolean isActive;
    protected List<OnNewDataHandler<T>> onNewDataList = new ArrayList<>();
    protected List<OnCompleted> onCompletedList = new ArrayList<>();
    private final ImageProcessor imageProcessor = new ImageProcessor();

    /**
     * Конструктор класса.
     *
     * @param parser Экземпляр парсера, используемого для обработки данных.
     */
    public ParserWorker(Parser<T> parser) {
        this.parser = parser;
    }

    /**
     * Метод, запускающий рабочего исполнителя. Осуществляет парсинг страниц с использованием заданного парсера и настроек.
     */
    private void worker() {
        imageProcessor.createImageDirectory();

        IntStream.range(parserSetting.getStartPoint(), parserSetting.getEndPoint() + 1)
                .filter(i -> isActive)
                .forEachOrdered(i -> {
                    Document document;
                    try {
                        document = loader.getSourceByPageId(i);
                    } catch (IOException exception) {
                        log.error("Ошибка при загрузке данных с сайта", exception);
                        throw new ParsingRuntimeException("Ошибка при загрузке данных с сайта", exception);
                    }
                    T result = parser.parse(document, imageProcessor);
                    onNewDataList.get(0).onNewData(this, result);
                });

        onCompletedList.get(0).onCompleted(this);
        isActive = false;
    }

    /**
     * Запускает рабочего исполнителя.
     */
    public void start() {
        isActive = true;
        worker();
    }

    /**
     * Прерывает выполнение рабочего исполнителя.
     */
    public void abort() {
        isActive = false;
    }

    /**
     * Устанавливает настройки парсера.
     *
     * @param parserSetting Настройки парсера.
     */
    public void setParserSetting(final ParserSetting parserSetting) {
        this.parserSetting = parserSetting;
        loader = new HtmlLoader(parserSetting);
    }

    /**
     * Интерфейс обработчика события новых данных.
     *
     * @param <T> Тип данных, передаваемых в обработчик.
     */
    public interface OnNewDataHandler<T> {
        void onNewData(Object sender, final T args);
    }

    /**
     * Интерфейс обработчика события завершения работы.
     */
    public interface OnCompleted {
        void onCompleted(final Object sender);
    }
}