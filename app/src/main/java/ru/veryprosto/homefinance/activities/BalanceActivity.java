package ru.veryprosto.homefinance.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ru.veryprosto.homefinance.MainController;
import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.db.model.OperationType;
import ru.veryprosto.homefinance.util.DateRange;
import ru.veryprosto.homefinance.util.OperationAdapter;

public class BalanceActivity extends AppCompatActivity {
    private MainController mainController;
    private Button addOperationButton;
    private ListView operationListView;
    private OperationAdapter operationAdapter;

    private void init() {
        mainController = MainController.getInstance();
        addOperationButton = findViewById(R.id.addOperationBtn);
        operationListView = findViewById(R.id.operationList);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_layout);

        init();

        List<OperationType> operationTypes = new ArrayList<>();
        operationTypes.add(OperationType.OUTPUT);
        operationTypes.add(OperationType.INPUT);

        operationAdapter = new OperationAdapter(this, R.layout.list_item, mainController.getOperationByDatesAndTypes(new DateRange(null, null), operationTypes));
        operationListView.setAdapter(operationAdapter);

        addOperationButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(BalanceActivity.this, OperationEditActivity.class);
            startActivity(intent);
        });
    }
}
