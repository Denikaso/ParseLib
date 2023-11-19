package org.example;

import java.util.ArrayList;

public class NewData implements ParserWorker.OnNewDataHandler<ArrayList<String>> {
    @Override
    public void OnNewData(Object sender, ArrayList<String> args) {
        for (String s : args) {
            System.out.println(s);
        }
    }
}
