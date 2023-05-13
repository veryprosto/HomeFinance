package ru.veryprosto.homefinance.model;

import ru.veryprosto.homefinance.R;

public enum AccountType {
    DEBITCARD("дебетовая карта", R.drawable.debit_card),
    CREDITCARD("кредитная карта", R.drawable.credit_card),
    CASH("наличные", R.drawable.cash),
    CREDIT("кредит", R.drawable.percent),
    DEPOSIT("депозит", R.drawable.deposit),
    PERSON("человек", R.drawable.person);

    private String title;
    private int icon;

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

    @Override
    public String toString() {
        return title;
    }
}
