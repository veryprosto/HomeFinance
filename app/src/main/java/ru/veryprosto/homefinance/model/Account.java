package ru.veryprosto.homefinance.model;


import androidx.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

@DatabaseTable
public class Account {
    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(unique = true, canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private AccountType type;

    @DatabaseField(defaultValue = "true")
    private boolean active = true;

    @ForeignCollectionField(eager = true, foreignFieldName = "firstAccount")
    private Collection<Operation> operationsAccountFirst;

    @ForeignCollectionField(eager = true, foreignFieldName = "secondAccount")
    private Collection<Operation> operationsAccountSecond;

    public Account() {
    }

    public Account(String name, AccountType type) {
        this.name = name;
        this.type = type;
        this.active = true;
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

    public Collection<Operation> getOperationsAccountFirst() {
        return operationsAccountFirst;
    }

    public Collection<Operation> getOperationsAccountSecond() {
        return operationsAccountSecond;
    }

    public BigDecimal getTotal() {
        BigDecimal inputOperationsSumm = getOperationsSummByType(OperationType.INPUT);
        BigDecimal outputOperationsSumm = getOperationsSummByType(OperationType.OUTPUT);
        BigDecimal outputTransferOperationsSumm = getOperationsSummByType(OperationType.TRANSFER);
        BigDecimal inputTransferOperationsSumm = operationsAccountSecond.stream().filter(
                        o -> o.getSecondAccount() != null)
                .map(Operation::getSumm).reduce(BigDecimal.ZERO, BigDecimal::add);

        return inputOperationsSumm.add(inputTransferOperationsSumm).subtract(outputOperationsSumm).subtract(outputTransferOperationsSumm);
    }

    public BigDecimal getOperationsSummByType(OperationType operationType) {
        return operationsAccountFirst.stream().filter(o -> o.getCategory().getType() == operationType).map(Operation::getSumm).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id.equals(account.id) && name.equals(account.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}