package ru.veryprosto.homefinance.db.model;

public enum AccountType {
    DEBITCARD("дебетовая карта"),
    CREDITCARD("кредитная карта"),
    CASH("наличные"),
    DEPOSIT("депозит"),
    PERSON("человек");

    private String title;

    AccountType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
