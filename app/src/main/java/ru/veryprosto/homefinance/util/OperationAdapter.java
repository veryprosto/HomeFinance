package ru.veryprosto.homefinance.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import ru.veryprosto.homefinance.controller.CategoryController;
import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.controller.OperationController;
import ru.veryprosto.homefinance.model.Operation;

public class OperationAdapter extends ArrayAdapter<Operation> {
    private LayoutInflater inflater;
    private int layout;
    private List<Operation> operationList;
    private OperationController operationController;

    public OperationAdapter(Context context, int resource, List<Operation> operationList) {
        super(context, resource, operationList);
        this.operationList = operationList;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, View convertView, ViewGroup parent) {
        operationController = OperationController.getInstance();

        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Operation operation = operationList.get(position);

        viewHolder.nameView.setText(operation.getCategory().getName());
        viewHolder.summView.setText(operation.getSumm().toString());

        viewHolder.removeButton.setOnClickListener(v -> {
            operationController.removeOperation(operation);
            operationList.remove(operation);
            notifyDataSetChanged();
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
