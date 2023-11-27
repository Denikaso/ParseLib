package org.example;

import habr.HabrParser;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;

public class ParserWorker<T> {
    Parser<T> parser;
    ParserSettings parserSettings;
    HtmlLoader loader;
    boolean isActive;
    ArrayList<OnNewDataHandler> onNewDataList = new ArrayList<>();
    ArrayList<OnCompleted> onCompletedList = new ArrayList<>();
    public ParserWorker(Parser<T> parser) {
        this.parser = parser;
    }

    private void Worker() throws IOException {
        for (int i = parserSettings.getStartPoint(); i <= parserSettings.getEndPoint(); i++) {
            if (!isActive) {
                onCompletedList.get(0).OnCompleted(this);
                return;
            }
            Document document = loader.GetSourceByPageId(i);
            T result = parser.Parse(document);
            onNewDataList.get(0).OnNewData(this,result);
        }
        onCompletedList.get(0).OnCompleted(this);
        isActive = false;
    }

    public void Start() throws IOException {
        isActive = true;
        Worker();
    }
    public void Abort() {
        isActive = false;
    }

    public void setParser(Parser<T> parser) {
        this.parser = parser;
    }

    public void setParserSettings(ParserSettings parserSettings) {
        this.parserSettings = parserSettings;
        loader = new HtmlLoader(parserSettings);
    }

    public ParserSettings getParserSettings() {
        return parserSettings;
    }

    public Parser<T> getParser() {
        return parser;
    }
    public interface OnNewDataHandler<T> {
        void OnNewData(Object sender, T e);
    }
    public interface OnCompleted {
        void OnCompleted(Object sender);
    }

}