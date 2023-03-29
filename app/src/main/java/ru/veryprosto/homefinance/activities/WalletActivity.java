package ru.veryprosto.homefinance.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import ru.veryprosto.homefinance.MainController;
import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.db.model.Wallet;
import ru.veryprosto.homefinance.util.WalletAdapter;

public class WalletActivity extends AppCompatActivity {

    private MainController mainController;
    private Button addWalletButton;
    private ListView walletListView;
    private WalletAdapter adapter;

    private void init(){
        mainController = MainController.getInstance();
        addWalletButton = findViewById(R.id.addWalletBtn);
        walletListView = findViewById(R.id.walletList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_layout);

        init();
        fillWalletListView();

        addWalletButton.setOnClickListener(v -> {
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

                                Wallet wallet = new Wallet(userText);
                                mainController.createWallet(wallet);

                                fillWalletListView();
                            })
                    .setNegativeButton("Отмена",
                            (dialog, id) -> dialog.cancel())
                    .create().show();
            adapter.notifyDataSetChanged();
        });
    }

    public void fillWalletListView(){
        adapter = new WalletAdapter(this, R.layout.list_item, mainController.getWallets());
        walletListView.setAdapter(adapter);
    }

}