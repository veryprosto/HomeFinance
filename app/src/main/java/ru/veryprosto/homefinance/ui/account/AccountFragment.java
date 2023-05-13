package ru.veryprosto.homefinance.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.adapter.DtoAdapter;
import ru.veryprosto.homefinance.controller.AccountController;
import ru.veryprosto.homefinance.databinding.FragmentAccountBinding;
import ru.veryprosto.homefinance.model.Account;
import ru.veryprosto.homefinance.model.AccountType;
import ru.veryprosto.homefinance.model.DTO;


public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private AccountController accountController;
    private List<DTO> dtos;
    private List<Account> accounts;
    private RecyclerView accountRecyclerView;
    private DtoAdapter adapter;
    private CheckBox showInactiveAccountsCheckBox;
    private Boolean showInactiveAccounts;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();
        return root;
    }

    private void init() {
        accountController = AccountController.getInstance();

        initAddAccountButton();
        initShowInactiveCheckBox();
        fillAccountRecyclerView();
    }

    private void initAddAccountButton() {
        ImageButton addOperationButton = binding.btnAddAccount;
        addOperationButton.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.nav_edit_account);
        });
    }

    private void initShowInactiveCheckBox() {
        showInactiveAccountsCheckBox = binding.chbxShowInactiveAccounts;
        showInactiveAccounts = false;
        showInactiveAccountsCheckBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            showInactiveAccounts = isChecked;
            fillAccountRecyclerView();
        });

    }

    private void fillAccountRecyclerView() {
        dtos = new ArrayList<>();
        accounts = accountController.getAccounts();

        List<AccountType> accountTypes = Arrays.asList(AccountType.DEBITCARD, AccountType.CASH, AccountType.DEPOSIT, AccountType.PERSON, AccountType.CREDITCARD, AccountType.CREDIT);

        accounts = accountController.getAccountsByTypesAndActivity(showInactiveAccounts, accountTypes);

        Map<AccountType, List<Account>> accountMapByType = accounts.stream().collect(Collectors.groupingBy(Account::getType));

        for (AccountType accountType : accountTypes) {
            List<Account> accountsOneType = accountMapByType.get(accountType);

            if (accountsOneType == null || accountsOneType.isEmpty()) {
                continue;
            }

            String parentType = accountType.getTitle();

            DTO empty = new DTO("");
            DTO parent = new DTO("    " + parentType);
            parent.setIconResource(accountType.getIcon());

            BigDecimal eventSumm = BigDecimal.ZERO;

            if (!dtos.isEmpty()) {
                dtos.add(empty);
            }

            dtos.add(parent);

            for (Account account : accountsOneType) {
                String accountName = account.getName();
                BigDecimal sum = account.getTotal();

                eventSumm = eventSumm.add(sum);
                parent.setRightUp(eventSumm.toString());

                DTO child = new DTO(accountName, sum.toString(), "", "", 0, null);
                child.setParentClassName(account.getClass().getSimpleName());
                child.setId(account.getId());
                dtos.add(child);
            }
        }

        accountRecyclerView = binding.accountList;

        adapter = new DtoAdapter(this.requireActivity().getLayoutInflater(), dtos);

        accountRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}