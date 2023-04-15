package ru.veryprosto.homefinance.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.controller.AccountController;
import ru.veryprosto.homefinance.model.Account;
import ru.veryprosto.homefinance.model.AccountType;


public class AccountEditActivity extends AppCompatActivity {

    private AccountController accountController;
    private Spinner accountTypeSpinner;
    private EditText accountNameInput;
    private Account account;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_edit_layout);

        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {
        accountController = AccountController.getInstance();

        accountNameInput = findViewById(R.id.accountName);

        TextView accountEditTextView = findViewById(R.id.accountEditTitleTextView);

        Bundle arguments = getIntent().getExtras();

        Integer accountId = (Integer) arguments.get("oldAccountId");

        account = accountController.getAccountById(accountId);


        String title = accountId == 0 ? "Новый аккаунт" : "Редактирование аккаунта";
        accountEditTextView.setText(title);

        initAccountTypeSpinner();

        if (account.getId() != null) {
            accountNameInput.setText(account.getName());
        }

        initSaveButton();
        initCancelButton();
    }

    private void initSaveButton() {
        ImageButton saveOperationButton = findViewById(R.id.saveAccountBtn);

        saveOperationButton.setOnClickListener(v -> {
            String accountName = accountNameInput.getText().toString();
            AccountType accountType = (AccountType) accountTypeSpinner.getItemAtPosition(accountTypeSpinner.getSelectedItemPosition());

            account.setName(accountName);
            account.setType(accountType);

            if (account.getId()==null){
                accountController.createAccount(account);
            } else {
                accountController.updateAccount(account);
            }




            returnAndRefreshPreviousActivity();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initAccountTypeSpinner() {
        accountTypeSpinner = findViewById(R.id.accountTypeSpinner);

        List<AccountType> accountTypes = Arrays.asList(AccountType.values());
        ArrayAdapter<AccountType> accountTypeArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, accountTypes);
        accountTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountTypeSpinner.setAdapter(accountTypeArrayAdapter);

        if (account.getId() != null) {
            accountTypeSpinner.setSelection(accountTypes.indexOf(account.getType()));
        }
    }

    private void returnAndRefreshPreviousActivity() {
        Intent intent = new Intent();
        intent.setClass(AccountEditActivity.this, AccountActivity.class);
        startActivity(intent);
    }

    private void initCancelButton() {
        ImageButton cancelButton = findViewById(R.id.cancelBtn);
        cancelButton.setOnClickListener(v -> returnAndRefreshPreviousActivity());
    }

}
