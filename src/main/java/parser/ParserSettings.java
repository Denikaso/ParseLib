package parser;

import lombok.Getter;

public abstract class ParserSettings {
    public static String BASE_URL;
    public static String PREFIX;
    @Getter
    protected int startPoint;
    @Getter
    protected int endPoint;
}
