package ru.veryprosto.homefinance.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;
import java.util.Date;

import ru.veryprosto.homefinance.util.Util;

@DatabaseTable(tableName = "operation")
public class Operation {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField()
    private String description;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true, columnName = "account_id")
    private Account account;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true, columnName = "category_id")
    private Category category;

    @DatabaseField
    private Date date;

    @DatabaseField
    private BigDecimal summ;

    public Operation() {
    }

    public Operation(String description, Account account, Category category, Date date, BigDecimal summ) {
        this.description = description;
        this.account = account;
        this.category = category;
        this.date = date;
        this.summ = summ;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getSumm() {
        return summ;
    }

    public void setSumm(BigDecimal summ) {
        this.summ = summ;
    }
}
