package ru.veryprosto.homefinance.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.List;

import ru.veryprosto.homefinance.MainController;
import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.db.model.Account;

public class AccountAdapter extends ArrayAdapter<Account> {
    private LayoutInflater inflater;
    private int layout;
    private List<Account> accountList;
    private MainController mainController;

    public AccountAdapter(Context context, int resource, List<Account> accountList) {
        super(context, resource, accountList);
        this.accountList = accountList;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    public View getView(int position, View convertView, ViewGroup parent) {
        mainController = MainController.getInstance();

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
            mainController.removeAccount(account);
            accountList.remove(account);
            notifyDataSetChanged(); //обновляет отображение.
        });

        viewHolder.editButton.setOnClickListener(v -> {

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
