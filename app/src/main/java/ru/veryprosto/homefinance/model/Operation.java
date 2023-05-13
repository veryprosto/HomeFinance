package ru.veryprosto.homefinance.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;
import java.util.Date;

@DatabaseTable(tableName = "operation")
public class Operation {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField()
    private String description;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true, columnName = "first_account_id")
    private Account firstAccount;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "second_account_id")
    private Account secondAccount;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true, columnName = "category_id")
    private Category category;

    @DatabaseField
    private Date date;

    @DatabaseField
    private BigDecimal summ;

    public Operation() {
    }

    public Operation(String description, Account firstAccount, Account secondAccount, Category category, Date date, BigDecimal summ) {
        this.description = description;
        this.firstAccount = firstAccount;
        this.secondAccount = secondAccount;
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
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Account getFirstAccount() {
        return firstAccount;
    }

    public void setFirstAccount(Account firstAccount) {
        this.firstAccount = firstAccount;
    }

    public Account getSecondAccount() {
        return secondAccount;
    }

    public void setSecondAccount(Account secondAccount) {
        this.secondAccount = secondAccount;
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
        return summ == null ? BigDecimal.ZERO : summ;
    }

    public void setSumm(BigDecimal summ) {
        this.summ = summ;
    }
}
