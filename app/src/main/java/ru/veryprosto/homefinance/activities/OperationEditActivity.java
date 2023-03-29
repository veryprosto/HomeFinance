package ru.veryprosto.homefinance.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import ru.veryprosto.homefinance.MainController;
import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.db.model.Category;
import ru.veryprosto.homefinance.db.model.Operation;
import ru.veryprosto.homefinance.db.model.Wallet;
import ru.veryprosto.homefinance.util.Util;

public class OperationEditActivity extends AppCompatActivity {

    private MainController mainController;
    private Button saveOperationButton;
    private Button addCategoryButton;
    private List<Wallet> wallets;
    private List<Category> categories;
    private Spinner walletSpinner;
    private Spinner categorySpinner;
    private EditText nameInput;
    private EditText dateInput;
    private EditText summInput;
    private ArrayAdapter<Category> categoryArrayAdapter;

    private void init() {
        mainController = MainController.getInstance();
        saveOperationButton = findViewById(R.id.saveOperationBtn);
        addCategoryButton = findViewById(R.id.addCategoryBtn);


        walletSpinner = findViewById(R.id.walletSpinner);
        categorySpinner = findViewById(R.id.categorySpinner);
        nameInput = findViewById(R.id.input_name);
        dateInput = findViewById(R.id.input_date);
        summInput = findViewById(R.id.input_summ);
        fillWalletSpinner();
        fillCategorySpinner();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operation_edit_layout);

        init();

        saveOperationButton.setOnClickListener(v -> {
            String operationName = nameInput.getText().toString();

            Wallet wallet = (Wallet) walletSpinner.getItemAtPosition(walletSpinner.getSelectedItemPosition());
            Category category = (Category) categorySpinner.getItemAtPosition(categorySpinner.getSelectedItemPosition());// todo добавить диалоговое окно для добавления категории
            Date operationDate = Util.StringToDate(dateInput.getText().toString(), "dd.MM.yyyy"); //todo выделить формат в глобальную переменную, добавить валидацию ввода
            BigDecimal operationSumm = new BigDecimal(summInput.getText().toString()); // todo добавить валидацию ввода
            boolean isOutput = true;

            Operation operation = new Operation(operationName, wallet, category, operationDate, operationSumm, isOutput);
            mainController.createOperation(operation);
        });

        addCategoryButton.setOnClickListener(v -> {
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
                                Category newCategory = new Category(userText, true);
                                mainController.createCategory(newCategory);
                                fillCategorySpinner();
                            })
                    .setNegativeButton("Отмена",
                            (dialog, id) -> dialog.cancel())
                    .create().show();
            categoryArrayAdapter.notifyDataSetChanged();
        });
    }

    private void fillCategorySpinner() {
        categories = mainController.getCategories();
        categoryArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryArrayAdapter);
    }

    private void fillWalletSpinner() {
        wallets = mainController.getWallets();
        ArrayAdapter<Wallet> walletArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, wallets);
        walletArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        walletSpinner.setAdapter(walletArrayAdapter);
    }

}