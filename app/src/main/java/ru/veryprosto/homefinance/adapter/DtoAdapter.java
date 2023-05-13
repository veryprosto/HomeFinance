package ru.veryprosto.homefinance.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.veryprosto.homefinance.model.DTO;
import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.model.Account;
import ru.veryprosto.homefinance.model.Operation;
import ru.veryprosto.homefinance.model.OperationType;

public class DtoAdapter extends RecyclerView.Adapter<DtoAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<DTO> dtos;

    public DtoAdapter(LayoutInflater inflater, List<DTO> dtos) {
        this.inflater = inflater;
        this.dtos = dtos;
    }

    @NonNull
    @Override
    public DtoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull DtoAdapter.ViewHolder holder, int position) {
        DTO dto = dtos.get(position);

        ImageView iconView = holder.iconView;
        TextView leftDownView = holder.leftDownView;
        TextView leftUpView = holder.leftUpView;
        TextView rightDownView = holder.rightDownView;
        TextView rightUpView = holder.rightUpView;

        if (dto.getOperationType() != null) {
            int color = 0;
            if (dto.getOperationType() == OperationType.OUTPUT) {
                color = Color.RED;
            } else if (dto.getOperationType() == OperationType.INPUT) {
                color = Color.GREEN;
            } else if (dto.getOperationType() == OperationType.TRANSFER) {
                color = Color.BLUE;
            }
            iconView.setColorFilter(color);
            leftDownView.setTextColor(color);
            leftUpView.setTextColor(color);
            rightUpView.setTextColor(color);
            rightDownView.setTextColor(color);
        }

        iconView.setImageResource(dto.getIconResource());
        leftDownView.setText(dto.getLeftDown());
        leftUpView.setText(dto.getLeftUp());
        rightDownView.setText(dto.getRightDown());
        rightUpView.setText(dto.getRightUp());

        holder.itemView.setOnLongClickListener(view -> {
            DTO dtoItem = dtos.get(position);
            Bundle args = new Bundle();
            String parentClassName = dtoItem.getParentClassName();

            if (parentClassName.equals(Operation.class.getSimpleName())){
                args.putInt("operationId", dtoItem.getId());
                Navigation.findNavController(view).navigate(R.id.nav_operation_to_nav_edit_operation, args);
            } else if (parentClassName.equals(Account.class.getSimpleName())){
                args.putInt("accountId", dtoItem.getId());
                Navigation.findNavController(view).navigate(R.id.nav_account_to_nav_edit_account, args);
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return dtos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView iconView;
        final TextView leftUpView, rightUpView, leftDownView, rightDownView;

        ViewHolder(View itemView) {
            super(itemView);
            iconView = itemView.findViewById(R.id.iv_icon);
            leftUpView = itemView.findViewById(R.id.tv_left_up);
            rightUpView = itemView.findViewById(R.id.tv_right_up);
            leftDownView = itemView.findViewById(R.id.tv_left_down);
            rightDownView = itemView.findViewById(R.id.tv_right_down);
        }
    }

    public List<DTO> getDtos() {
        return dtos;
    }

    public DTO getDTO(int id){
        return dtos.get(id);
    }
}