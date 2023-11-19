package org.example;

public   class Completed implements ParserWorker.OnCompleted{
    @Override
    public void OnCompleted(Object sender) {
        System.out.println("Загрузка закончена");
    }
}
