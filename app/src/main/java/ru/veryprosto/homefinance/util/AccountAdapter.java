package ru.veryprosto.homefinance.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.List;

import ru.veryprosto.homefinance.activities.AccountActivity;
import ru.veryprosto.homefinance.activities.AccountEditActivity;
import ru.veryprosto.homefinance.controller.AccountController;
import ru.veryprosto.homefinance.controller.CategoryController;
import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.model.Account;

public class AccountAdapter extends ArrayAdapter<Account> {
    private LayoutInflater inflater;
    private int layout;
    private List<Account> accountList;
    private AccountController accountController;

    public AccountAdapter(Context context, int resource, List<Account> accountList) {
        super(context, resource, accountList);
        this.accountList = accountList;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    public View getView(int position, View convertView, ViewGroup parent) {
        accountController = AccountController.getInstance();

        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Account account = accountList.get(position);

        viewHolder.nameView.setText(account.getName());
        viewHolder.summView.setText(account.getTotal().toString());

        viewHolder.removeButton.setOnClickListener(v -> {
            accountController.removeAccount(account);
            accountList.remove(account);
            notifyDataSetChanged(); //обновляет отображение.
        });

        viewHolder.editButton.setOnClickListener(v -> {
            Context context = this.getContext();
            Intent intent = new Intent(this.getContext(), AccountEditActivity.class);
            intent.putExtra("oldAccountId", account.getId());
            context.startActivity(intent);
        });

        return convertView;
    }

    private class ViewHolder {
        final Button editButton, removeButton;
        final TextView nameView, summView;

        ViewHolder(View view) {
            editButton = view.findViewById(R.id.editButton);
            removeButton = view.findViewById(R.id.removeButton);
            nameView = view.findViewById(R.id.nameView);
            summView = view.findViewById(R.id.summView);
        }
    }
}
