package parser;

public   class Completed implements ParserWorker.OnCompleted{
    @Override
    public void onCompleted(Object sender) {
        System.out.println("Загрузка закончена");
    }
}
