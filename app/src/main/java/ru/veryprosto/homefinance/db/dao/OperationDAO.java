package ru.veryprosto.homefinance.db.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import ru.veryprosto.homefinance.db.model.Operation;

public class OperationDAO extends BaseDaoImpl<Operation, Integer> {

    public OperationDAO(ConnectionSource connectionSource, Class<Operation> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}