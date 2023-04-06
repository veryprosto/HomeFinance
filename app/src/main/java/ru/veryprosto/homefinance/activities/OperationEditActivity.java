package ru.veryprosto.homefinance.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import ru.veryprosto.homefinance.MainController;
import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.db.model.Category;
import ru.veryprosto.homefinance.db.model.Operation;
import ru.veryprosto.homefinance.db.model.Wallet;
import ru.veryprosto.homefinance.util.Util;


public class OperationEditActivity extends AppCompatActivity {

    private MainController mainController;
    private Button dateButton;
    private Spinner walletSpinner;
    private Spinner categorySpinner;
    private EditText nameInput;
    private EditText summInput;
    private ArrayAdapter<Category> categoryArrayAdapter;
    private Boolean isOutputOperation;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operation_edit_layout);

        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {
        mainController = MainController.getInstance();

        nameInput = findViewById(R.id.input_name);
        summInput = findViewById(R.id.input_summ);
        TextView operationEditTextView = findViewById(R.id.operationEditTextView);

        Bundle arguments = getIntent().getExtras();
        isOutputOperation = (Boolean) arguments.get("isOutputOperation");
        String title = isOutputOperation ? "Расходная операция" : "Доходная операция";
        operationEditTextView.setText(title);

        initWalletSpinner();
        initCategorySpinner();
        initDateButton();
        initAddCategoryButton();
        initSaveOperationButton();
    }

    private void initSaveOperationButton() {
        Button saveOperationButton = findViewById(R.id.saveOperationBtn);

        saveOperationButton.setOnClickListener(v -> {
            String operationName = nameInput.getText().toString();
            Wallet wallet = (Wallet) walletSpinner.getItemAtPosition(walletSpinner.getSelectedItemPosition());
            Category category = (Category) categorySpinner.getItemAtPosition(categorySpinner.getSelectedItemPosition());
            BigDecimal outInKoef = isOutputOperation ? new BigDecimal(-1) : new BigDecimal(1);
            BigDecimal operationSumm = new BigDecimal(summInput.getText().toString()).multiply(outInKoef); // todo добавить валидацию ввода
            Date operationDate = Util.stringToDate((String) dateButton.getText());

            Operation operation = new Operation(operationName, wallet, category, operationDate, operationSumm, isOutputOperation);
            mainController.createOperation(operation);

            returnAndRefreshPreviousActivity();
        });
    }

    private void initCategorySpinner() {
        categorySpinner = findViewById(R.id.categorySpinner);

        List<Category> categories = mainController.getCategoriesByType(isOutputOperation);
        categoryArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryArrayAdapter);
    }

    private void initWalletSpinner() {
        walletSpinner = findViewById(R.id.walletSpinner);

        List<Wallet> wallets = mainController.getWallets();
        ArrayAdapter<Wallet> walletArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, wallets);
        walletArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        walletSpinner.setAdapter(walletArrayAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initDateButton() {
        dateButton = findViewById(R.id.pick_date_button);

        AtomicReference<Date> operationDate = new AtomicReference<>(new Date());
        dateButton.setText(Util.dateToString(operationDate.get()));

        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE" + "\u221E\t");

        final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();

        dateButton.setOnClickListener(
                v -> materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER"));

        materialDatePicker.addOnPositiveButtonClickListener(
                (MaterialPickerOnPositiveButtonClickListener<? super Long>) selection -> {

                    operationDate.set(new Date((Long) selection));
                    dateButton.setText((CharSequence) Util.dateToString(operationDate.get()));
                });
    }

    private void initAddCategoryButton() {
        Button addCategoryButton = findViewById(R.id.addCategoryBtn);

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
                                Category newCategory = new Category(userText, isOutputOperation);
                                mainController.createCategory(newCategory);
                                initCategorySpinner();
                            })
                    .setNegativeButton("Отмена",
                            (dialog, id) -> dialog.cancel())
                    .create().show();
            categoryArrayAdapter.notifyDataSetChanged();
        });
    }

    private void returnAndRefreshPreviousActivity(){
        Intent intent = new Intent();
        intent.setClass(OperationEditActivity.this, OperationActivity.class);
        startActivity(intent);
    }

}
