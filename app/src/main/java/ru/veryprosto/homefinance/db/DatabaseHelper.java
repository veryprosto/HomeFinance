package ru.veryprosto.homefinance.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ru.veryprosto.homefinance.db.dao.CategoryDAO;
import ru.veryprosto.homefinance.db.dao.OperationDAO;
import ru.veryprosto.homefinance.db.dao.AccountDAO;
import ru.veryprosto.homefinance.model.AccountType;
import ru.veryprosto.homefinance.model.Category;
import ru.veryprosto.homefinance.model.Operation;
import ru.veryprosto.homefinance.model.Account;
import ru.veryprosto.homefinance.model.OperationType;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    //имя файла базы данных который будет храниться в /data/data/APPNAME/DATABASE_NAME.db
    private static final String DATABASE_NAME = "homefinance.db";

    //с каждым увеличением версии, при нахождении в устройстве БД с предыдущей версией будет выполнен метод onUpgrade();
    private static final int DATABASE_VERSION = 15;

    //ссылки на DAO соответствующие сущностям, хранимым в БД
    private AccountDAO accountDAO = null;
    private OperationDAO operationDAO = null;
    private CategoryDAO categoryDAO = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Выполняется, когда файл с БД не найден на устройстве
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Account.class);
            TableUtils.createTable(connectionSource, Operation.class);
            TableUtils.createTable(connectionSource, Category.class);
        } catch (SQLException e) {
            Log.e(TAG, "error creating DB " + DATABASE_NAME);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer,
                          int newVer) {
        try {
            //Так делают ленивые, гораздо предпочтительнее не удаляя БД аккуратно вносить изменения
            TableUtils.dropTable(connectionSource, Account.class, true);
            TableUtils.dropTable(connectionSource, Operation.class, true);
            TableUtils.dropTable(connectionSource, Category.class, true);
            onCreate(db, connectionSource);
            accountDAO = getAccountDAO();
            categoryDAO = getCategoryDAO();
            operationDAO = getOperationDAO();

            accountDAO.create(new Account("Tinkoff", AccountType.DEBITCARD));
            accountDAO.create(new Account("Yandex", AccountType.DEBITCARD));
            accountDAO.create(new Account("Наличка", AccountType.CASH));

            categoryDAO.create(new Category("Зарплата", OperationType.INPUT));
            categoryDAO.create(new Category("Шабашка", OperationType.INPUT));
            categoryDAO.create(new Category("Продукты", OperationType.OUTPUT));
            categoryDAO.create(new Category("Комуналка", OperationType.OUTPUT));
            categoryDAO.create(new Category("Транспорт", OperationType.OUTPUT));
            categoryDAO.create(new Category("Бухло", OperationType.OUTPUT));
            categoryDAO.create(new Category("Отдых", OperationType.OUTPUT));
            categoryDAO.create(new Category("Перевод", OperationType.TRANSFER));

            List<Account> accounts = accountDAO.queryForAll();
            List<Category> categories = categoryDAO.queryForAll();

            operationDAO.create(new Operation("description1", accounts.get(0), null, categories.get(0), new GregorianCalendar(2023, 3, 10).getTime(), new BigDecimal(60000)));
            operationDAO.create(new Operation("description2", accounts.get(1), null, categories.get(1), new GregorianCalendar(2023, 3, 12).getTime(), new BigDecimal(10000)));
            operationDAO.create(new Operation("description3", accounts.get(2), null, categories.get(2), new Date(), new BigDecimal(9854)));
            operationDAO.create(new Operation("description4", accounts.get(1), null, categories.get(3), new Date(), new BigDecimal(11500)));
            operationDAO.create(new Operation("description5", accounts.get(2), null, categories.get(4), new Date(), new BigDecimal(150)));
            operationDAO.create(new Operation("description6", accounts.get(0), null, categories.get(5), new Date(), new BigDecimal(4564)));
            operationDAO.create(new Operation("description7", accounts.get(1), null, categories.get(6), new Date(), new BigDecimal(4542)));
            operationDAO.create(new Operation("description8", accounts.get(1), null, categories.get(2), new Date(), new BigDecimal(5000)));

        } catch (SQLException e) {
            Log.e(TAG, "error upgrading db " + DATABASE_NAME + "from ver " + oldVer);
            throw new RuntimeException(e);
        }
    }

    //синглтон для AccountDAO
    public AccountDAO getAccountDAO() {
        if (accountDAO == null) {
            try {
                accountDAO = new AccountDAO(getConnectionSource(), Account.class);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return accountDAO;
    }

    //синглтон для OperationDAO
    public OperationDAO getOperationDAO() {
        if (operationDAO == null) {
            try {
                operationDAO = new OperationDAO(getConnectionSource(), Operation.class);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return operationDAO;
    }

    //синглтон для CategoryDAO
    public CategoryDAO getCategoryDAO() {
        if (categoryDAO == null) {
            try {
                categoryDAO = new CategoryDAO(getConnectionSource(), Category.class);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return categoryDAO;
    }

    //выполняется при закрытии приложения
    @Override
    public void close() {
        super.close();
        accountDAO = null;
        operationDAO = null;
        categoryDAO = null;
    }
}