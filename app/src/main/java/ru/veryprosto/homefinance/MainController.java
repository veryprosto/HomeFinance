package ru.veryprosto.homefinance;

import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ru.veryprosto.homefinance.db.HelperFactory;
import ru.veryprosto.homefinance.db.dao.CategoryDAO;
import ru.veryprosto.homefinance.db.dao.OperationDAO;
import ru.veryprosto.homefinance.db.dao.WalletDAO;
import ru.veryprosto.homefinance.db.model.Category;
import ru.veryprosto.homefinance.db.model.Operation;
import ru.veryprosto.homefinance.db.model.Wallet;

public class MainController {

    private static MainController instance;

    private final WalletDAO walletDAO = HelperFactory.getHelper().getWalletDAO();
    private final OperationDAO operationDAO = HelperFactory.getHelper().getOperationDAO();
    private final CategoryDAO categoryDAO = HelperFactory.getHelper().getOperationCategoryDAO();

    private MainController() {
    }

    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    //------------ Create operations start ------------
    public Wallet createWallet(Wallet wallet) {
        try {
            walletDAO.create(wallet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
            // TODO: Обработать исключение, в т.ч. если кошелек с таким именем существует
        }
        return wallet;
    }

    public Operation createOperation(Operation operation) {
        try {
            operationDAO.create(operation);
        } catch (SQLException e) {
            throw new RuntimeException(e);
            // TODO: Обработать исключение
        }
        return operation;
    }

    public Category createCategory(Category category) {
        try {
            categoryDAO.create(category);
        } catch (SQLException e) {
            throw new RuntimeException(e);
            // TODO: Обработать исключение, в т.ч. если категория существует
        }
        return category;
    }
    //------------ Create operations end -----------

    //------------ Get operations start ------------
    public List<Wallet> getWallets() {
        List<Wallet> walletList;
        try {
            walletList = walletDAO.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e); // TODO: Обработать исключение
        }
        return walletList;
    }

    public List<Category> getCategories() {
        List<Category> categories;
        try {
            categories = categoryDAO.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    public Category getCategoryByName(String name) {
        try {
            return categoryDAO.queryBuilder().where().eq("name", name).queryForFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO: Обработать исключение
        }
    }

    public List<Operation> getOperationBetweenDates(Date start, Date end) { //todo доделать возможность фильтра выборки по категориям и кошелькам
        if (start == null) start = new GregorianCalendar(1900, 0, 1).getTime();
        if (end == null) end = new GregorianCalendar(5000, 0, 1).getTime();

        try {
            return operationDAO.queryBuilder().where().between("date", start, end).query();
        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO: Обработать исключение
        }
    }
    //------------ Get operations end ------------

    //------------ Remove operations start ------------
    public void removeWallet(Wallet wallet) {
        try {
            walletDAO.delete(wallet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
            //TODO: Обработать исключение
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

    public void removeOperation(Operation operation) {
        try {
            operationDAO.delete(operation);
        } catch (SQLException e) {
            throw new RuntimeException(e);
            //TODO: Обработать исключение
        }
    }
    //------------ Remove operations end ------------


}
