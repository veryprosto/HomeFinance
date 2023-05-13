package ru.veryprosto.homefinance.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.Arrays;
import java.util.List;

import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.controller.AccountController;
import ru.veryprosto.homefinance.databinding.FragmentAccountEditBinding;
import ru.veryprosto.homefinance.model.Account;
import ru.veryprosto.homefinance.model.AccountType;

public class AccountEditFragment extends Fragment {

    private FragmentAccountEditBinding binding;
    private Integer accountId;
    private Spinner accountTypeSpinner;
    private AccountController accountController;
    private Account account;
    private EditText accountNameInput;
    private CheckBox isActiveCheckBox;

    public AccountEditFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountEditBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        init();
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            accountId = getArguments().getInt("accountId");
        }
    }

    private void init() {
        accountController = AccountController.getInstance();
        account = accountController.getAccountById(accountId);

        accountNameInput = binding.etAccountName;
        accountNameInput.setText(account.getName());

        initAccountTypeSpinner();
        initIsActiveCheckBox();
        initSaveButton();
        initCancelButton();
    }

    private void initAccountTypeSpinner() {
        accountTypeSpinner = binding.spinnerAccountType;
        List<AccountType> accountTypes = Arrays.asList(AccountType.values());
        ArrayAdapter<AccountType> accountTypeArrayAdapter = new ArrayAdapter<>(
                binding.getRoot().getContext(),
                R.layout.spinner_item, accountTypes);
        accountTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountTypeSpinner.setAdapter(accountTypeArrayAdapter);

        if (account != null && account.getType() != null) {
            accountTypeSpinner.setSelection(accountTypes.indexOf(account.getType()));
        } else {
            accountTypeSpinner.setSelection(0);
        }
    }

    private void initIsActiveCheckBox(){
        isActiveCheckBox = binding.chbxIsActiveAccount;
        isActiveCheckBox.setChecked(account.isActive());
    }

    private void initSaveButton() {
        ImageButton saveAccountButton = binding.btnSaveAccount;

        saveAccountButton.setOnClickListener(v -> {
            String accountName = accountNameInput.getText().toString();
            AccountType accountType = (AccountType) accountTypeSpinner.getItemAtPosition(accountTypeSpinner.getSelectedItemPosition());

            account.setName(accountName);
            account.setType(accountType);
            account.setActive(isActiveCheckBox.isChecked());


            if (account.getId() == null) {
                accountController.createAccount(account);
            } else {
                accountController.updateAccount(account);
            }
            returnAndRefreshPreviousActivity();
        });
    }

    private void initCancelButton() {
        ImageButton cancelButton = binding.btnCancel;
        cancelButton.setOnClickListener(v -> returnAndRefreshPreviousActivity());
    }

    private void returnAndRefreshPreviousActivity() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.nav_account);
    }
}
