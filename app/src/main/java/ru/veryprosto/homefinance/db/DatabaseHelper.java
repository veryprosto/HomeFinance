package ru.veryprosto.homefinance.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import ru.veryprosto.homefinance.db.dao.CategoryDAO;
import ru.veryprosto.homefinance.db.dao.OperationDAO;
import ru.veryprosto.homefinance.db.dao.WalletDAO;
import ru.veryprosto.homefinance.db.model.Category;
import ru.veryprosto.homefinance.db.model.Operation;
import ru.veryprosto.homefinance.db.model.Wallet;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    //имя файла базы данных который будет храниться в /data/data/APPNAME/DATABASE_NAME.db
    private static final String DATABASE_NAME = "homefinance.db";

    //с каждым увеличением версии, при нахождении в устройстве БД с предыдущей версией будет выполнен метод onUpgrade();
    private static final int DATABASE_VERSION = 1;

    //ссылки на DAO соответсвующие сущностям, хранимым в БД
    private WalletDAO walletDAO = null;
    private OperationDAO operationDAO = null;
    private CategoryDAO categoryDAO = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Выполняется, когда файл с БД не найден на устройстве
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Wallet.class);
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
            TableUtils.dropTable(connectionSource, Wallet.class, true);
            TableUtils.dropTable(connectionSource, Operation.class, true);
            TableUtils.dropTable(connectionSource, Category.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "error upgrading db " + DATABASE_NAME + "from ver " + oldVer);
            throw new RuntimeException(e);
        }
    }

    //синглтон для WalletDAO
    public WalletDAO getWalletDAO() {
        if (walletDAO == null) {
            try {
                walletDAO = new WalletDAO(getConnectionSource(), Wallet.class);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return walletDAO;
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

    //синглтон для OperationCategoryDAO
    public CategoryDAO getOperationCategoryDAO() {
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
        walletDAO = null;
        operationDAO = null;
        categoryDAO = null;
    }
}