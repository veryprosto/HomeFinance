package ru.veryprosto.homefinance.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "category")
public class Category {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(unique = true, canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private boolean isOutput;

    public Category() {
    }

    public Category(String name, boolean isOutput) {
        this.name = name;
        this.isOutput = isOutput;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isOutput() {
        return isOutput;
    }

    public void setOutput(boolean output) {
        isOutput = output;
    }

    @Override
    public String toString() {
        return name;
    }
}
