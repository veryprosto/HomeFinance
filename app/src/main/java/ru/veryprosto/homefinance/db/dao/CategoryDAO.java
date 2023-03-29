package ru.veryprosto.homefinance.db.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import ru.veryprosto.homefinance.db.model.Category;

public class CategoryDAO extends BaseDaoImpl<Category, Integer> {

    public CategoryDAO(ConnectionSource connectionSource, Class<Category> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}
