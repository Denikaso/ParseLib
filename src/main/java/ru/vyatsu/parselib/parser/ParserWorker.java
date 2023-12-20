package ru.vyatsu.parselib.parser;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import ru.vyatsu.parselib.exception.ParsingRuntimeException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Data
public class ParserWorker<T> {

    private static final Logger logger = LogManager.getLogger(ParserWorker.class);

    Parser<T> parser;
    ParserSetting parserSetting;
    HtmlLoader loader;
    boolean isActive;
    protected List<OnNewDataHandler<T>> onNewDataList = new ArrayList<>();
    protected List<OnCompleted> onCompletedList = new ArrayList<>();
    public ParserWorker(Parser<T> parser) {
        this.parser = parser;
    }
    private final ImageProcessor imageProcessor = new ImageProcessor();
    private void worker() {
        imageProcessor.createImageDirectory();

        IntStream.range(parserSetting.getStartPoint(), parserSetting.getEndPoint() + 1)
                .filter(i -> isActive)
                .forEachOrdered(i -> {
                    Document document;
                    try {
                        document = loader.getSourceByPageId(i);
                    } catch (IOException exception) {
                        logger.error("Ошибка при загрузке данных с сайта", exception);
                        throw new ParsingRuntimeException("Ошибка при загрузке данных с сайта", exception);
                    }
                    T result = parser.parse(document, imageProcessor);
                    onNewDataList.get(0).onNewData(this, result);
                });

        onCompletedList.get(0).onCompleted(this);
        isActive = false;
    }

    public void start() {
        isActive = true;
        worker();
    }

    public void abort() {
        isActive = false;
    }
    public void setParserSetting(final ParserSetting parserSetting) {
        this.parserSetting = parserSetting;
        loader = new HtmlLoader(parserSetting);
    }

    public interface OnNewDataHandler<T> {
        void onNewData(Object sender, final T args);
    }

    public interface OnCompleted {
        void onCompleted(final Object sender);
    }
}
