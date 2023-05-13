package ru.veryprosto.homefinance.controller;


import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import ru.veryprosto.homefinance.db.HelperFactory;
import ru.veryprosto.homefinance.db.dao.CategoryDAO;
import ru.veryprosto.homefinance.db.dao.OperationDAO;
import ru.veryprosto.homefinance.model.Category;
import ru.veryprosto.homefinance.model.Operation;
import ru.veryprosto.homefinance.model.OperationType;
import ru.veryprosto.homefinance.util.DateRange;

public class OperationController {
    private static OperationController instance;

    private final OperationDAO operationDAO = HelperFactory.getHelper().getOperationDAO();
    private final CategoryDAO categoryDAO = HelperFactory.getHelper().getCategoryDAO();

    private OperationController() {
    }

    public static OperationController getInstance() {
        return instance == null ? new OperationController() : instance;
    }

    public void createOperation(Operation operation) {
        try {
            operationDAO.create(operation);
        } catch (SQLException e) {
            throw new RuntimeException(e); // TODO: Обработать исключение
        }
    }

    public void removeOperation(Operation operation) {
        try {
            operationDAO.delete(operation);
        } catch (SQLException e) {
            throw new RuntimeException(e);
            //TODO: Обработать исключение
        }
    }


    public List<Operation> getOperationByDatesAndTypes(DateRange dateRange, List<OperationType> types) {
        QueryBuilder<Operation, Integer> operationQB = operationDAO.queryBuilder();
        QueryBuilder<Category, Integer> categoryQB = categoryDAO.queryBuilder();

        try {
            categoryQB.where().in("type", types);
            operationQB.where().between("date", dateRange.getStartIncludeStartDate(), dateRange.getEnd());
            return operationQB.join(categoryQB).query();
        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO: Обработать исключение
        }

        /*return operations.stream()
                .filter(operation -> types.contains(operation.getCategory().getType()))
                .filter(operation -> dateRange.isInDiapason(operation.getDate())).collect(Collectors.toList());*/
    }

    public Operation getOperationById(Integer id) {
        Operation operation;
        try {
            operation = operationDAO.queryBuilder().where().eq("id", id).queryForFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO: Обработать исключение
        }
        return operation == null ? new Operation() : operation;
    }

    public void updateOperation(Operation operation) {
        try {
            operationDAO.update(operation);
        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO: Обработать исключение
        }
    }
}
