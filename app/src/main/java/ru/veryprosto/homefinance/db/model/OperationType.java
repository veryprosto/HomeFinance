package ru.veryprosto.homefinance.db.model;

public enum OperationType {
    INPUT("Доходная операция"),
    OUTPUT("Расходная операция"),
    TRANSFER("Перевод");

    private String title;

    OperationType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
