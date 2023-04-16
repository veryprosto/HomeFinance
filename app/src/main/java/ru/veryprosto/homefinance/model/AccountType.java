package ru.veryprosto.homefinance.model;

import ru.veryprosto.homefinance.R;

public enum AccountType {
    DEBITCARD("дебетовая карта", R.drawable.debitcard),
    CREDITCARD("кредитная карта", R.drawable.creditcard),
    CASH("наличные", R.drawable.cash),
    DEPOSIT("депозит", R.drawable.deposit),
    PERSON("человек", R.drawable.person);

    private String title;

    private int icon;

    AccountType(String title) {
        this.title = title;
    }

    AccountType(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }
}
