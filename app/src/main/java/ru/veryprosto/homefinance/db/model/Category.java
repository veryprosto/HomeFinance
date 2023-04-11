package ru.veryprosto.homefinance.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Category {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(unique = true, canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private OperationType type;

    public Category() {
    }

    public Category(String name, OperationType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }
}
