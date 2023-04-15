package ru.veryprosto.homefinance.temp;

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

public class OperationDTOAdapter extends RecyclerView.Adapter<OperationDTOAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<DTO> states;

    public OperationDTOAdapter(Context context, List<DTO> states) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public OperationDTOAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(OperationDTOAdapter.ViewHolder holder, int position) {
        DTO state = states.get(position);

        holder.flagView.setImageResource(state.getIconResource());
        holder.nameView.setText(state.getLeftUp());
        holder.capitalView.setText(state.getRightUp());
        holder.descriptionView.setText(state.getLeftDown());
        holder.accountView.setText(state.getRightDown());

        if (state.isParent()) {
            setVisibility(holder.itemView, true);
        } else {
            String parentId = state.getParentId();
            Optional<DTO> parentOpt = states.stream().filter(s -> s.isParent() && s.getParentId().equals(parentId)).findFirst();
            DTO parent = parentOpt.get();
            setVisibility(holder.itemView, parent.isChildVisibility());
        }

        holder.itemView.setOnClickListener(view -> {
            DTO stateItem = states.get(position);
            if (stateItem.isParent()) {
                stateItem.setChildVisibility(!stateItem.isChildVisibility());
                notifyDataSetChanged();
            } else {
                System.out.println("Дочерний элемент");//todo возможность редактировать операцию
            }
        });
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView flagView;
        final TextView nameView, capitalView, descriptionView, accountView;

        ViewHolder(View itemView) {
            super(itemView);
            flagView = itemView.findViewById(R.id.flag);
            nameView = itemView.findViewById(R.id.category);
            capitalView = itemView.findViewById(R.id.summ);
            descriptionView = itemView.findViewById(R.id.description);
            accountView = itemView.findViewById(R.id.account);
        }
    }


    private void setVisibility(View curV, boolean visible) {
        LinearLayout.LayoutParams params = visible ?
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) :
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);

        curV.setLayoutParams(params);
    }
}