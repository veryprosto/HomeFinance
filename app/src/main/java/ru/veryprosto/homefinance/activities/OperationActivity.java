package ru.veryprosto.homefinance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import ru.veryprosto.homefinance.MainController;
import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.util.OperationAdapter;

public class OperationActivity extends AppCompatActivity {
    private MainController mainController;
    private Button addOperationButton;
    private ListView operationListView;
    private OperationAdapter operationAdapter;

    private void init() {
        mainController = MainController.getInstance();
        addOperationButton = findViewById(R.id.addOperationBtn);
        operationListView = findViewById(R.id.operationList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operation_layout);

        init();
        operationAdapter = new OperationAdapter(this, R.layout.list_item, mainController.getOperationBetweenDates(null, null)); //todo добавить возможность фильтра по датам
        operationListView.setAdapter(operationAdapter);

        addOperationButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(OperationActivity.this, OperationEditActivity.class);
            startActivity(intent);
        });
    }
}
