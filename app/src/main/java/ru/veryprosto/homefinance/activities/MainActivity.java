package ru.veryprosto.homefinance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ru.veryprosto.homefinance.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button balanceButton = findViewById(R.id.balanceBtn);
        Button operationButton = findViewById(R.id.operationBtn);
        Button accountButton = findViewById(R.id.accountBtn);

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

        accountButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, AccountActivity.class);
            startActivity(intent);
        });
    }
}