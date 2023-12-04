package org.example.parser;

import lombok.Data;

@Data
public abstract class ParserSetting {
    protected String baseUrl;
    protected String prefix;
    protected int startPoint;
    protected int endPoint;
}