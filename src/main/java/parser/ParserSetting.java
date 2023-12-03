package parser;

import lombok.Data;

@Data
public abstract class ParserSetting {
    public static String BASE_URL;
    public static String PREFIX;
    protected int startPoint;
    protected int endPoint;
}
