package ru.veryprosto.homefinance.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Optional;

import ru.veryprosto.homefinance.DTO.DTO;
import ru.veryprosto.homefinance.R;

public class DTOAdapter extends RecyclerView.Adapter<DTOAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<DTO> dtos;

    public DTOAdapter(Context context, List<DTO> dtos) {
        this.dtos = dtos;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public DTOAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(DTOAdapter.ViewHolder holder, int position) {
        DTO dto = dtos.get(position);

        holder.iconView.setImageResource(dto.getIconResource());
        holder.leftUpView.setText(dto.getLeftUp());
        holder.rightUpView.setText(dto.getRightUp());
        holder.leftDownView.setText(dto.getLeftDown());
        holder.rightDownView.setText(dto.getRightDown());

        if (dto.isParent()) {
            setVisibility(holder.itemView, true);
        } else {
            String parentId = dto.getParentId();
            DTO parent = dtos.stream().filter(s -> s.isParent() && s.getParentId().equals(parentId)).findFirst().get();
            setVisibility(holder.itemView, parent.isChildVisibility());
        }

        holder.itemView.setOnClickListener(view -> {
            DTO dtoItem = dtos.get(position);
            if (dtoItem.isParent()) {
                dtoItem.setChildVisibility(!dtoItem.isChildVisibility());
                notifyDataSetChanged();
            } else {
                System.out.println("Дочерний элемент " + position);//todo возможность редактировать операцию
            }
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
            iconView = itemView.findViewById(R.id.iconView);
            leftUpView = itemView.findViewById(R.id.leftUpView);
            rightUpView = itemView.findViewById(R.id.rightUpView);
            leftDownView = itemView.findViewById(R.id.leftDownView);
            rightDownView = itemView.findViewById(R.id.rightDownView);
        }
    }


    private void setVisibility(View curV, boolean visible) {
        LinearLayout.LayoutParams params = visible ?
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) :
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);

        curV.setLayoutParams(params);
    }


}