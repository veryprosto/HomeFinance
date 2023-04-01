package ru.veryprosto.homefinance.db.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;
import java.util.Collection;

@DatabaseTable(tableName = "wallet")
public class Wallet {
    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(unique = true, canBeNull = false)
    private String name;

    @ForeignCollectionField(eager = true)
    private Collection<Operation> operations;

    public Wallet() {
    }

    public Wallet(String name) {
        this.name = name;
    }

    public Collection<Operation> getOperations() {
        return operations;
    }

    public void setOperations(Collection<Operation> operations) {
        this.operations = operations;
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    public BigDecimal getTotal() {
        return operations.stream().map(Operation::getSumm).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public String toString() {
        return name + " " + getTotal();
    }
}
