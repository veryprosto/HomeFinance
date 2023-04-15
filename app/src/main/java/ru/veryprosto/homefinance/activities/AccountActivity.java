package ru.veryprosto.homefinance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.controller.AccountController;
import ru.veryprosto.homefinance.model.AccountType;
import ru.veryprosto.homefinance.util.AccountAdapter;

public class AccountActivity extends AppCompatActivity {

    private AccountController accountController;
    private Button addAccountButton;
    private ListView accountListView;

    private void init() {
        accountController = AccountController.getInstance();
        addAccountButton = findViewById(R.id.addAccountBtn);
        accountListView = findViewById(R.id.accountList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_layout);

        init();
        fillAccountListView();

        addAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AccountEditActivity.class);
            intent.putExtra("oldAccountId", 0);
            startActivity(intent);
        });
    }

    public void fillAccountListView() {
        AccountAdapter adapter = new AccountAdapter(this, R.layout.list_item2, accountController.getAccountsByTypes(
                AccountType.DEBITCARD, AccountType.CASH, AccountType.DEPOSIT, AccountType.PERSON, AccountType.CREDITCARD));
        accountListView.setAdapter(adapter);
    }
}