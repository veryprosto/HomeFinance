package ru.veryprosto.homefinance;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import ru.veryprosto.homefinance.db.HelperFactory;
import ru.veryprosto.homefinance.db.dao.CategoryDAO;
import ru.veryprosto.homefinance.db.dao.OperationDAO;
import ru.veryprosto.homefinance.db.dao.AccountDAO;
import ru.veryprosto.homefinance.db.model.AccountType;
import ru.veryprosto.homefinance.db.model.Category;
import ru.veryprosto.homefinance.db.model.Operation;
import ru.veryprosto.homefinance.db.model.Account;
import ru.veryprosto.homefinance.db.model.OperationType;
import ru.veryprosto.homefinance.util.DateRange;

public class MainController {

    private static MainController instance;

    private final AccountDAO accountDAO = HelperFactory.getHelper().getAccountDAO();
    private final OperationDAO operationDAO = HelperFactory.getHelper().getOperationDAO();
    private final CategoryDAO categoryDAO = HelperFactory.getHelper().getCategoryDAO();

    private MainController() {
    }

    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    //------------ Create operations start ------------
    public void createAccount(Account account) {
        try {
            accountDAO.create(account);
        } catch (SQLException e) {
            throw new RuntimeException(e);
            // TODO: Обработать исключение, в т.ч. если кошелек с таким именем существует
        }
    }

    public void createOperation(Operation operation) {
        try {
            OperationType type = operation.getCategory().getType();
            if (type == OperationType.OUTPUT) {
                operation.setSumm(operation.getSumm().multiply(new BigDecimal(-1)));
            }
            operationDAO.create(operation);
        } catch (SQLException e) {
            throw new RuntimeException(e);
            // TODO: Обработать исключение
        }
    }

    public void createTransferOperation(Account from, Account to, BigDecimal summ, Date date) {
        try {
            Category transferCategory = categoryDAO.queryBuilder().where().eq("name", "transfer").queryForFirst();
            if (transferCategory == null) {
                categoryDAO.create(new Category("transfer", OperationType.TRANSFER));
                transferCategory = categoryDAO.queryBuilder().where().eq("name", "transfer").queryForFirst();
            }


            Operation operationFrom = new Operation("Перевод на " + to.getName(), from, transferCategory, date, summ.multiply(new BigDecimal(-1)));
            Operation operationTo = new Operation("Перевод с " + from.getName(), to, transferCategory, date, summ);

            Collection<Operation> transferOperations = Arrays.asList(operationFrom, operationTo);


            operationDAO.create(transferOperations);//todo проверить транзакция ли это?
        } catch (SQLException e) {
            throw new RuntimeException(e);
            // TODO: Обработать исключение
        }
    }

    public void createCategory(Category category) {
        try {
            categoryDAO.create(category);
        } catch (SQLException e) {
            throw new RuntimeException(e);
            // TODO: Обработать исключение, в т.ч. если категория существует
        }
    }
    //------------ Create operations end -----------

    //------------ Get operations start ------------
    public List<Account> getAccountsByTypes(AccountType... types) {
        try {
            return accountDAO.queryBuilder().where().in("type", types).query();
        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO: Обработать исключение
        }
    }

    public List<Category> getCategoriesByTypes(OperationType... types) {
        try {
            return categoryDAO.queryBuilder().where().in("type", types).query();
        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO: Обработать исключение
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Operation> getOperationByDatesAndTypes(DateRange dateRange, List<OperationType> types) {
        QueryBuilder<Operation, Integer> operationQB = operationDAO.queryBuilder();
        QueryBuilder<Category, Integer> categoryQB = categoryDAO.queryBuilder();

        try {
            categoryQB.where().in("type", types);

            //return operationQB.join(categoryQB).where().between("date", dateRange.getStartIncudeStartDate(), dateRange.getEnd()).query();
            return operationQB.join(categoryQB).query();

        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO: Обработать исключение
        }
    }
    //------------ Get operations end ------------

    //------------ Remove operations start ------------
    public void removeAccount(Account account) {
        try {
            accountDAO.delete(account);
        } catch (SQLException e) {
            throw new RuntimeException(e);//TODO: Обработать исключение
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
