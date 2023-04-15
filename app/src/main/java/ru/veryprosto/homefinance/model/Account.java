package ru.veryprosto.homefinance.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;
import java.util.Collection;

@DatabaseTable
public class Account {
    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(unique = true, canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private AccountType type;

    @ForeignCollectionField(eager = true)
    private Collection<Operation> operations;

    public Account() {
    }

    public Account(String name, AccountType type) {
        this.name = name;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public Collection<Operation> getOperations() {
        return operations;
    }

    public void setOperations(Collection<Operation> operations) {
        this.operations = operations;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public BigDecimal getTotal() {
        return operations.stream().map(Operation::getSumm).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public String toString() {
        return name;
    }
}
