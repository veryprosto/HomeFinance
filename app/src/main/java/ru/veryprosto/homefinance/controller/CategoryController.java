package ru.veryprosto.homefinance.controller;

import java.sql.SQLException;
import java.util.List;

import ru.veryprosto.homefinance.db.HelperFactory;
import ru.veryprosto.homefinance.db.dao.CategoryDAO;
import ru.veryprosto.homefinance.model.Category;
import ru.veryprosto.homefinance.model.OperationType;

public class CategoryController {

    private static CategoryController instance;

    private final CategoryDAO categoryDAO = HelperFactory.getHelper().getCategoryDAO();

    private CategoryController() {
    }

    public static CategoryController getInstance() {
        return instance == null ? new CategoryController() : instance;
    }

    public void createCategory(Category category) {
        try {
            categoryDAO.create(category);
        } catch (SQLException e) {
            throw new RuntimeException(e);
            // TODO: Обработать исключение, в т.ч. если категория существует
        }
    }

    public void removeCategory(Category category) {
        try {
            categoryDAO.delete(category);
        } catch (SQLException e) {
            throw new RuntimeException(e);
            //TODO: Обработать исключение
        }
    }

    public List<Category> getCategoriesByTypes(OperationType... types) {
        try {
            return categoryDAO.queryBuilder().where().in("type", types).query();
        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO: Обработать исключение
        }
    }
}
