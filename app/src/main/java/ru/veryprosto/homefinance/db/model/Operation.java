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
    @DatabaseField(canBeNull = true)
    private String name;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true, columnName = "wallet_id")
    private Wallet wallet;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true, columnName = "category_id")
    private Category category;

    @DatabaseField()
    private Date date;

    @DatabaseField
    private BigDecimal summ;

    @DatabaseField(canBeNull = false)
    private boolean isOutput;

    public Operation() {
    }

    public Operation(String name, Wallet wallet, Category category, Date date, BigDecimal summ, boolean isOutput) {
        this.name = name;
        this.wallet = wallet;
        this.category = category;
        this.date = date;
        this.summ = summ;
        this.isOutput = isOutput;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
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

    public boolean isOutput() {
        return isOutput;
    }

    public void setOutput(boolean output) {
        isOutput = output;
    }

    @Override
    public String toString() {
        return wallet +
                ", " + category +
                ", " + Util.dateToString(date) +
                ", " + summ +
                ", " + isOutput;
    }
}
