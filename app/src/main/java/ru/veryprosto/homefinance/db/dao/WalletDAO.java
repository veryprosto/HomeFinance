package ru.veryprosto.homefinance.db.dao;

import java.sql.SQLException;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import ru.veryprosto.homefinance.db.model.Wallet;

public class WalletDAO extends BaseDaoImpl<Wallet, Integer> {

    public WalletDAO(ConnectionSource connectionSource, Class<Wallet> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}
