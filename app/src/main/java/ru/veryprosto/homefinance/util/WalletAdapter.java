package ru.veryprosto.homefinance.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import ru.veryprosto.homefinance.MainController;
import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.db.model.Wallet;

public class WalletAdapter extends ArrayAdapter<Wallet> {
    private LayoutInflater inflater;
    private int layout;
    private List<Wallet> walletList;
    private MainController mainController;

    public WalletAdapter(Context context, int resource, List<Wallet> walletList) {
        super(context, resource, walletList);
        this.walletList = walletList;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

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

        final Wallet wallet = walletList.get(position);

        viewHolder.nameView.setText(wallet.getName());
        viewHolder.summView.setText("10000");

        viewHolder.removeButton.setOnClickListener(v -> {
            mainController.removeWallet(wallet);
            walletList.remove(wallet);
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
