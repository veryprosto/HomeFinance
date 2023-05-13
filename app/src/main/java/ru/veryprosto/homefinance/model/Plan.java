package ru.veryprosto.homefinance.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;
import java.util.Date;

@DatabaseTable
public class Plan {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField
    private Date date; // используем только месяц

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true, columnName = "category_id")
    private Category category;

    @DatabaseField
    private BigDecimal summ;

    public Plan() {
    }

    public Plan(Date date, Category category, BigDecimal summ) {
        this.date = date;
        this.category = category;
        this.summ = summ;
    }

    public Integer getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getSumm() {
        return summ;
    }

    public void setSumm(BigDecimal summ) {
        this.summ = summ;
    }
}
