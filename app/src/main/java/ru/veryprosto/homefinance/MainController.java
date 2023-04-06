package ru.veryprosto.homefinance;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
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
    public void createWallet(Wallet wallet) {
        try {
            walletDAO.create(wallet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
            // TODO: Обработать исключение, в т.ч. если кошелек с таким именем существует
        }
    }

    public void createOperation(Operation operation) {
        try {
            operationDAO.create(operation);
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
    public List<Wallet> getWallets() {
        List<Wallet> walletList;
        try {
            walletList = walletDAO.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e); // TODO: Обработать исключение
        }
        return walletList;
    }

    public List<Category> getCategoriesByType(Boolean isOutput) {
        List<Category> categories;
        try {
            categories = categoryDAO.queryBuilder().where().eq("isOutput", isOutput).query();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Operation> getOperationByDatesAndTypes(Date start, Date end, boolean isOutput, boolean isInput) { //todo рефактор - создать утильный класс DateRange
        if (!isOutput && !isInput) {
            return new ArrayList<>();
        }

        if (start == null) start = new GregorianCalendar(1900, 0, 1).getTime();
        if (end == null) end = new GregorianCalendar(5000, 0, 1).getTime();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.DATE, -1);
        start = calendar.getTime();

        try {
            Where<Operation, Integer> where = operationDAO.queryBuilder().where();

            return where.and(where.between("date", start, end), where.or(where.eq("isOutput", isOutput), where.eq("isOutput", !isInput))).query();

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
