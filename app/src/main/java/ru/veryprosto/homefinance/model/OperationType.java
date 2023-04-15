package ru.veryprosto.homefinance.model;

import ru.veryprosto.homefinance.R;

public enum OperationType {
    INPUT("Доходная операция", R.drawable.input),
    OUTPUT("Расходная операция", R.drawable.output),
    TRANSFER("Перевод", R.drawable.transfer);

    private String title;

    private int icon;

    OperationType(String title, int icon) {
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
