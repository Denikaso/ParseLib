package parser;

import lombok.Builder;
import lombok.Data;
import lombok.val;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public class ParserWorker<T> {
    Parser<T> parser;
    ParserSettings parserSettings;
    HtmlLoader loader;
    boolean isActive;
    public List<OnNewDataHandler<T>> onNewDataList = new ArrayList<>();
    public List<OnCompleted> onCompletedList = new ArrayList<>();
    public ParserWorker(Parser<T> parser) {
        this.parser = parser;
    }

    private void worker() throws IOException {
        for (int i = parserSettings.getStartPoint(); i <= parserSettings.getEndPoint(); i++) {
            if (!isActive) {
                onCompletedList.get(0).onCompleted(this);
                return;
            }
            val document = loader.getSourceByPageId(i);
            T result = parser.parse(document);
            onNewDataList.get(0).onNewData(this,result);
        }
        onCompletedList.get(0).onCompleted(this);
        isActive = false;
    }

    public void start() throws IOException {
        isActive = true;
        worker();
    }

    public void abort() {
        isActive = false;
    }
    public void setParserSettings(ParserSettings parserSettings) {
        this.parserSettings = parserSettings;
        loader = new HtmlLoader(parserSettings);
    }

    public interface OnNewDataHandler<T> {
        void onNewData(Object sender, T e);
    }

    public interface OnCompleted {
        void onCompleted(Object sender);
    }
}
