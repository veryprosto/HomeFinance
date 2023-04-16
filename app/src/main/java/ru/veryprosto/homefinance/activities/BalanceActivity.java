package ru.veryprosto.homefinance.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import ru.veryprosto.homefinance.DTO.DTO;
import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.controller.AccountController;
import ru.veryprosto.homefinance.model.Account;
import ru.veryprosto.homefinance.model.AccountType;
import ru.veryprosto.homefinance.util.DTOAdapter;

public class BalanceActivity extends AppCompatActivity {

    private AccountController accountController;
    private List<DTO> dtos;
    private RecyclerView accountRecyclerView;

    private List<Account> accounts;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_layout);
        init();
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {
        accountController = AccountController.getInstance();

        initAddAccountButton();
        initReturnMenuButton();
        fillOperationRecyclerView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void fillOperationRecyclerView() {
        dtos = new ArrayList<>();

        accounts = accountController.getAccountsByTypes(
                AccountType.DEBITCARD, AccountType.CREDITCARD, AccountType.CASH,
                AccountType.DEPOSIT, AccountType.PERSON);

        Map<AccountType, List<Account>> accountMapByType = accounts.stream().collect(Collectors.groupingBy(Account::getType));

        Comparator<AccountType> comparator = Comparator.naturalOrder();
        SortedMap<AccountType, List<Account>> sortedMap = new TreeMap<>(comparator);
        sortedMap.putAll(accountMapByType);

        for (Map.Entry<AccountType, List<Account>> entry : sortedMap.entrySet()) {
            AccountType accountType = entry.getKey();
            String parentString = accountType.getTitle();
            DTO parent = new DTO(parentString);
            parent.setParentId(parentString);
            parent.setParent(true);
            parent.setIconResource(accountType.getIcon());

            BigDecimal eventSumm = BigDecimal.ZERO;

            dtos.add(parent);

            List<Account> value = entry.getValue();

            for (Account account : value) {
                String name = account.getName();
                BigDecimal summ = account.getTotal();

                DTO child = new DTO(name);
                child.setRightUp(summ.toString());
                child.setParentId(parentString);

                eventSumm = eventSumm.add(summ);
                parent.setRightUp(eventSumm.toString());
                dtos.add(child);
            }
        }

        accountRecyclerView = findViewById(R.id.accountList2);
        DTOAdapter adapter = new DTOAdapter(this, dtos);
        accountRecyclerView.setAdapter(adapter);
    }

    private void initAddAccountButton() {
        ImageButton addAccountButton = findViewById(R.id.addAccountBtn2);

        addAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AccountEditActivity.class);
            intent.putExtra("oldAccountId", 0);
            startActivity(intent);
        });
    }

    private void initReturnMenuButton() {
        ImageButton returnMenuBtn = findViewById(R.id.returnMenuBtn);
        returnMenuBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}
