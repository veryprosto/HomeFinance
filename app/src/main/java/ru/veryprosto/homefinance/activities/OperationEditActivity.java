package ru.veryprosto.homefinance.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.stream.Collectors;

import ru.veryprosto.homefinance.MainController;
import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.db.model.AccountType;
import ru.veryprosto.homefinance.db.model.Category;
import ru.veryprosto.homefinance.db.model.Operation;
import ru.veryprosto.homefinance.db.model.Account;
import ru.veryprosto.homefinance.db.model.OperationType;
import ru.veryprosto.homefinance.util.Util;


public class OperationEditActivity extends AppCompatActivity {

    private MainController mainController;
    private Spinner accountSpinner;
    private Spinner accountToSpinner;
    private Spinner categorySpinner;
    private EditText descriptionInput;
    private EditText summInput;
    private ArrayAdapter<Category> categoryArrayAdapter;
    private OperationType operationType;
    private TextView dateTextView;

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

        descriptionInput = findViewById(R.id.input_description);
        summInput = findViewById(R.id.input_summ);
        TextView operationEditTextView = findViewById(R.id.operationEditTextView);

        Bundle arguments = getIntent().getExtras();
        operationType = (OperationType) arguments.get("operationType");

        String title = operationType.getTitle();
        operationEditTextView.setText(title);

        initAccountSpinner();
        initAccountToSpinner();
        initCategorySpinner(operationType);
        initDateButtonAndTextView();
        initAddCategoryButton();
        initSaveButton();
        initCancelButton();

        if (operationType == OperationType.TRANSFER) {
            categorySpinner.setVisibility(View.INVISIBLE);
            accountToSpinner.setVisibility(View.VISIBLE);
        } else {
            categorySpinner.setVisibility(View.VISIBLE);
            accountToSpinner.setVisibility(View.INVISIBLE);
        }
    }

    private void initSaveButton() {
        ImageButton saveOperationButton = findViewById(R.id.saveOperationBtn);

        saveOperationButton.setOnClickListener(v -> {
            String description = descriptionInput.getText().toString();
            Account account = (Account) accountSpinner.getItemAtPosition(accountSpinner.getSelectedItemPosition());
            Account accountTo = (Account) accountToSpinner.getItemAtPosition(accountToSpinner.getSelectedItemPosition());
            Category category = (Category) categorySpinner.getItemAtPosition(categorySpinner.getSelectedItemPosition());
            BigDecimal summ = new BigDecimal(summInput.getText().toString()); //todo добавить валидацию ввода
            Date operationDate = Util.stringToDate((String) dateTextView.getText());


            if (operationType==OperationType.TRANSFER){
                mainController.createTransferOperation(account, accountTo, summ, operationDate);
            } else {
                Operation operation = new Operation(description, account, category, operationDate, summ);
                mainController.createOperation(operation);
            }


            returnAndRefreshPreviousActivity();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initCategorySpinner(OperationType type) {
        categorySpinner = findViewById(R.id.categorySpinner);

        List<Category> categories = mainController.getCategoriesByTypes(type);

        categoryArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryArrayAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initAccountSpinner() {
        accountSpinner = findViewById(R.id.accountSpinner);

        List<Account> accounts = mainController.getAccountsByTypes(AccountType.DEBITCARD);
        ArrayAdapter<Account> accountArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, accounts);
        accountArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountSpinner.setAdapter(accountArrayAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initAccountToSpinner() {
        accountToSpinner = findViewById(R.id.accountToSpinner);

        List<Account> accounts = mainController.getAccountsByTypes(AccountType.DEBITCARD);
        ArrayAdapter<Account> accountArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, accounts);
        accountArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountToSpinner.setAdapter(accountArrayAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initDateButtonAndTextView() {
        ImageButton dateButton = findViewById(R.id.pick_date_button);
        dateTextView = findViewById(R.id.dateTextView);

        AtomicReference<Date> operationDate = new AtomicReference<>(new Date());
        dateTextView.setText(Util.dateToString(operationDate.get()));

        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE" + "\u221E\t");

        final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();

        dateButton.setOnClickListener(
                v -> materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER"));

        materialDatePicker.addOnPositiveButtonClickListener(
                (MaterialPickerOnPositiveButtonClickListener<? super Long>) selection -> {

                    operationDate.set(new Date((Long) selection));
                    dateTextView.setText((CharSequence) Util.dateToString(operationDate.get()));
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initAddCategoryButton() {
        ImageButton addCategoryButton = findViewById(R.id.addCategoryBtn);

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
                                Category newCategory = new Category(userText, operationType);
                                mainController.createCategory(newCategory);
                                initCategorySpinner(operationType);
                            })
                    .setNegativeButton("Отмена",
                            (dialog, id) -> dialog.cancel())
                    .create().show();
            categoryArrayAdapter.notifyDataSetChanged();
        });
    }

    private void returnAndRefreshPreviousActivity() {
        Intent intent = new Intent();
        intent.setClass(OperationEditActivity.this, OperationActivity.class);
        startActivity(intent);
    }

    private void initCancelButton() {
        ImageButton cancelButton = findViewById(R.id.cancelBtn);
        cancelButton.setOnClickListener(v -> returnAndRefreshPreviousActivity());
    }

}
