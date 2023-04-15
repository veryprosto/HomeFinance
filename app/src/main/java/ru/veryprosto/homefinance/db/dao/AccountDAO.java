package ru.veryprosto.homefinance.db.dao;

import java.sql.SQLException;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import ru.veryprosto.homefinance.model.Account;

public class AccountDAO extends BaseDaoImpl<Account, Integer> {

    public AccountDAO(ConnectionSource connectionSource, Class<Account> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}
