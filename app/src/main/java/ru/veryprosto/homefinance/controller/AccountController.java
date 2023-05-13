package ru.veryprosto.homefinance.controller;

import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import ru.veryprosto.homefinance.db.HelperFactory;
import ru.veryprosto.homefinance.db.dao.AccountDAO;
import ru.veryprosto.homefinance.model.Account;
import ru.veryprosto.homefinance.model.AccountType;

public class AccountController {
    private static AccountController instance;

    private final AccountDAO accountDAO = HelperFactory.getHelper().getAccountDAO();

    private AccountController() {
    }

    public static AccountController getInstance() {
        return instance == null ? new AccountController() : instance;
    }

    public List<Account> getAccounts() {
        try {
            return accountDAO.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createAccount(Account account) {
        try {
            accountDAO.create(account);
        } catch (SQLException e) {
            throw new RuntimeException(e);
            // TODO: Обработать исключение, в т.ч. если кошелек с таким именем существует
        }
    }

    public void removeAccount(Account account) {
        try {
            accountDAO.delete(account);
        } catch (SQLException e) {
            throw new RuntimeException(e);//TODO: Обработать исключение
        }
    }

    public List<Account> getAccountsByTypesAndActivity(Boolean active, AccountType... types) {
        return getAccountsByTypesAndActivity(active, Arrays.asList(types));
    }

    public List<Account> getAccountsByTypesAndActivity(Boolean onlyActive, List<AccountType> types) {
        try {
            Where<Account, Integer> whereTypes = accountDAO.queryBuilder().where().in("type", types);
            return onlyActive ? whereTypes.query() : whereTypes.and().eq("active", true).query();
        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO: Обработать исключение
        }
    }

    public void updateAccount(Account account) {
        try {
            accountDAO.update(account);
        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO: Обработать исключение
        }
    }

    public Account getAccountById(Integer id) {
        Account account;
        try {
            account = accountDAO.queryBuilder().where().eq("id", id).queryForFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e); //TODO: Обработать исключение
        }
        return account == null ? new Account() : account;
    }
}
