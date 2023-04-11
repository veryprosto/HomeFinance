package ru.veryprosto.homefinance.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import ru.veryprosto.homefinance.MainController;
import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.db.model.Account;
import ru.veryprosto.homefinance.db.model.AccountType;
import ru.veryprosto.homefinance.db.model.OperationType;
import ru.veryprosto.homefinance.util.AccountAdapter;

public class AccountActivity extends AppCompatActivity {

    private MainController mainController;
    private Button addAccountButton;
    private ListView accountListView;
    private AccountAdapter adapter;

    private void init(){
        mainController = MainController.getInstance();
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
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.prompt, null);

            AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);

            mDialogBuilder.setView(promptsView);

            final EditText userInput = promptsView.findViewById(R.id.input_text);

            mDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            (dialog, id) -> {
                                String userText = userInput.getText().toString();

                                Account account = new Account(userText, AccountType.DEBITCARD);
                                mainController.createAccount(account);

                                fillAccountListView();
                            })
                    .setNegativeButton("Отмена",
                            (dialog, id) -> dialog.cancel())
                    .create().show();
            adapter.notifyDataSetChanged();
        });
    }

    public void fillAccountListView(){
        adapter = new AccountAdapter(this, R.layout.list_item, mainController.getAccountsByTypes(AccountType.DEBITCARD));
        accountListView.setAdapter(adapter);
    }

}