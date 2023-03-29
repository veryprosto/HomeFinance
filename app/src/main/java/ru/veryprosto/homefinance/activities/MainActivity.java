package ru.veryprosto.homefinance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ru.veryprosto.homefinance.MainController;
import ru.veryprosto.homefinance.R;


public class MainActivity extends AppCompatActivity {

    MainController mainController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainController = MainController.getInstance();

        Button balanceButton = findViewById(R.id.balanceBtn);
        Button operationButton = findViewById(R.id.operationBtn);
        Button walletButton = findViewById(R.id.walletBtn);

        balanceButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, BalanceActivity.class);
            startActivity(intent);
        });

        operationButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, OperationActivity.class);
            startActivity(intent);
        });

        walletButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, WalletActivity.class);
            startActivity(intent);
        });
    }
}