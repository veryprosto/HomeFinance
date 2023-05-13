package ru.veryprosto.homefinance.ui.operation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.controller.AccountController;
import ru.veryprosto.homefinance.controller.CategoryController;
import ru.veryprosto.homefinance.controller.OperationController;
import ru.veryprosto.homefinance.databinding.FragmentOperationEditBinding;
import ru.veryprosto.homefinance.model.Account;
import ru.veryprosto.homefinance.model.AccountType;
import ru.veryprosto.homefinance.model.Category;
import ru.veryprosto.homefinance.model.Operation;
import ru.veryprosto.homefinance.model.OperationType;
import ru.veryprosto.homefinance.util.DateRange;

public class OperationEditFragment extends Fragment {

    private FragmentOperationEditBinding binding;
    private TextView title;
    private TextView accountTitle;
    private TextView accountToTitle;
    private TextView categoryTitle;
    private View.OnClickListener radioButtonClickListener;
    private CategoryController categoryController;
    private OperationController operationController;
    private AccountController accountController;
    private Spinner accountSpinner;
    private Spinner accountToSpinner;
    private Spinner categorySpinner;
    private EditText descriptionInput;
    private EditText summInput;
    private ArrayAdapter<Category> categoryArrayAdapter;
    private OperationType operationType;
    private TextView dateTextView;
    private Operation operation;
    List<Category> categories;


    public OperationEditFragment() {

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOperationEditBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        init();
        return root;
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        Bundle bundle = getArguments();
        assert bundle != null;

        categoryController = CategoryController.getInstance();
        operationController = OperationController.getInstance();
        accountController = AccountController.getInstance();

        operation = operationController.getOperationById(bundle.getInt("operationId"));

        accountToTitle = binding.tvAccountToTitle;
        accountTitle = binding.tvAccountTitle;
        categoryTitle = binding.tvCategoryTitle;

        descriptionInput = binding.etDescription;
        descriptionInput.setText(operation.getDescription());

        summInput = binding.etSumm;
        summInput.setText(operation.getSumm().toString());

        operationType = operation.getId() == null ? OperationType.OUTPUT : operation.getCategory().getType();

        initTitle();
        initRadioButtons();
        initAccountSpinner();
        initAccountToSpinner();
        initCategorySpinner();
        initDateButtonAndTextView();
        initSaveButton();
        initCancelButton();
    }

    private void initTitle() {
        title = binding.tvOperationEditTitle;
        title.setText(operationType.getTitle());
    }

    private void initRadioButtons() {
        RadioButton outputRadioButton = binding.radioOutput;
        RadioButton inputRadioButton = binding.radioInput;
        RadioButton transferRadioButton = binding.radioTransfer;

        radioButtonClickListener = v -> {
            RadioButton rb = (RadioButton) v;

            switch (rb.getId()) {
                case R.id.radio_output:
                    title.setText(R.string.output_operation);
                    operationType = OperationType.OUTPUT;
                    accountToSpinner.setVisibility(View.INVISIBLE);
                    accountTitle.setText(R.string.output_from_account);
                    accountToTitle.setVisibility(View.INVISIBLE);
                    categoryTitle.setVisibility(View.VISIBLE);
                    break;
                case R.id.radio_input:
                    title.setText(R.string.input_operation);
                    operationType = OperationType.INPUT;
                    accountToSpinner.setVisibility(View.INVISIBLE);
                    accountTitle.setText(R.string.input_to_account);
                    accountToTitle.setVisibility(View.INVISIBLE);
                    categoryTitle.setVisibility(View.VISIBLE);
                    break;
                case R.id.radio_transfer:
                    title.setText(R.string.transfer_operation);
                    operationType = OperationType.TRANSFER;
                    accountToSpinner.setVisibility(View.VISIBLE);
                    accountTitle.setText(R.string.output_from_account);
                    accountToTitle.setVisibility(View.VISIBLE);
                    categoryTitle.setVisibility(View.INVISIBLE);
                    break;
            }
            fillCategorySpinner();
        };

        outputRadioButton.setOnClickListener(radioButtonClickListener);
        inputRadioButton.setOnClickListener(radioButtonClickListener);
        transferRadioButton.setOnClickListener(radioButtonClickListener);

        if (operationType == OperationType.OUTPUT) {
            outputRadioButton.setChecked(true);
            inputRadioButton.setChecked(false);
            transferRadioButton.setChecked(false);
        } else if (operationType == OperationType.INPUT) {
            outputRadioButton.setChecked(false);
            inputRadioButton.setChecked(true);
            transferRadioButton.setChecked(false);
        } else if (operationType == OperationType.TRANSFER) {
            outputRadioButton.setChecked(false);
            inputRadioButton.setChecked(false);
            transferRadioButton.setChecked(true);
        }
    }

    private void initCategorySpinner() {
        categorySpinner = binding.spinnerCategory;

        categoryArrayAdapter = new ArrayAdapter<>(binding.getRoot().getContext(), android.R.layout.simple_spinner_item);
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(categoryArrayAdapter);

        fillCategorySpinner();

        if (operation != null && operation.getCategory() != null) {
            categorySpinner.setSelection(categories.indexOf(operation.getCategory()));
        } else {
            categorySpinner.setSelection(0);
        }
    }

    private void fillCategorySpinner() {
        categories = categoryController.getCategoriesByTypes(operationType);
        categoryArrayAdapter.clear();
        categoryArrayAdapter.addAll(categories);
    }

    private void initAccountSpinner() {
        accountSpinner = binding.spinnerAccount;

        List<Account> accounts = accountController.getAccountsByTypesAndActivity(true, AccountType.DEBITCARD, AccountType.CASH, AccountType.DEPOSIT);
        if (accounts == null || accounts.isEmpty()) {
            return;
        }
        ArrayAdapter<Account> accountArrayAdapter = new ArrayAdapter<>(binding.getRoot().getContext(), android.R.layout.simple_spinner_item, accounts);
        accountArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountSpinner.setAdapter(accountArrayAdapter);

        if (operation != null && operation.getFirstAccount() != null) {
            accountSpinner.setSelection(accounts.indexOf(operation.getFirstAccount()));
        } else {
            accountSpinner.setSelection(0);
        }
    }

    private void initAccountToSpinner() {
        accountToSpinner = binding.spinnerAccountTo;

        List<Account> accounts = accountController.getAccountsByTypesAndActivity(true, AccountType.DEBITCARD, AccountType.CASH, AccountType.DEPOSIT);
        ArrayAdapter<Account> accountArrayAdapter = new ArrayAdapter<>(binding.getRoot().getContext(), android.R.layout.simple_spinner_item, accounts);
        accountArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountToSpinner.setAdapter(accountArrayAdapter);

        if (operation != null && operation.getSecondAccount() != null) {
            accountToSpinner.setSelection(accounts.indexOf(operation.getFirstAccount()));
        } else {
            accountToSpinner.setSelection(1);//todo
        }
    }

    private void initDateButtonAndTextView() {
        dateTextView = binding.tvDate;

        AtomicReference<Date> operationDate = new AtomicReference<>();

        if (operation != null && operation.getDate() != null) {
            dateTextView.setText(DateRange.dateToString(operation.getDate()));
        } else {
            operationDate.set(new Date());
            dateTextView.setText(DateRange.dateToString(operationDate.get()));
        }

        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE" + "\u221E\t");

        final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();

        dateTextView.setOnClickListener(v -> materialDatePicker
                .show(getParentFragmentManager(), "MATERIAL_DATE_PICKER"));

        materialDatePicker.addOnPositiveButtonClickListener(
                (MaterialPickerOnPositiveButtonClickListener<? super Long>) selection -> {
                    operationDate.set(new Date(selection));
                    dateTextView.setText(DateRange.dateToString(operationDate.get()));
                });
    }

    private void initSaveButton() {
        ImageButton saveOperationButton = binding.btnSaveOperation;

        saveOperationButton.setOnClickListener(v -> {
            String description = descriptionInput.getText().toString();
            Account firstAccount = (Account) accountSpinner.getItemAtPosition(accountSpinner.getSelectedItemPosition());
            Account secondAccount = operationType == OperationType.TRANSFER ?
                    (Account) accountToSpinner.getItemAtPosition(accountToSpinner.getSelectedItemPosition()) :
                    null;
            Category category = (Category) categorySpinner.getItemAtPosition(categorySpinner.getSelectedItemPosition());
            BigDecimal summ = new BigDecimal(summInput.getText().toString()); //todo добавить валидацию ввода
            Date operationDate = DateRange.stringToDate((String) dateTextView.getText());

            operation.setDescription(description);
            operation.setFirstAccount(firstAccount);
            operation.setSecondAccount(secondAccount);
            operation.setCategory(category);
            operation.setDate(operationDate);
            operation.setSumm(summ);

            if (operation.getId() == null) {
                operationController.createOperation(operation);
            } else {
                operationController.updateOperation(operation);
            }

            returnAndRefreshPreviousActivity();
        });
    }

    private void initCancelButton() {
        ImageButton cancelButton = binding.btnCancel;
        cancelButton.setOnClickListener(v -> returnAndRefreshPreviousActivity());
    }

    private void returnAndRefreshPreviousActivity() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.nav_operation);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}