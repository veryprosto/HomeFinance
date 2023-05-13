package ru.veryprosto.homefinance.ui.budget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

import ru.veryprosto.homefinance.adapter.DtoAdapter;
import ru.veryprosto.homefinance.controller.OperationController;
import ru.veryprosto.homefinance.databinding.FragmentBudgetBinding;
import ru.veryprosto.homefinance.model.DTO;
import ru.veryprosto.homefinance.model.Operation;
import ru.veryprosto.homefinance.util.DateRange;


public class BudgetFragment extends Fragment {
    private FragmentBudgetBinding binding;
    private OperationController operationController;
    private List<DTO> dtos;
    private List<Operation> operations;
    private Button pickDateButton;
    private Date startPeriod;
    private Date endPeriod;
    private DateRange dateRange;
    private boolean isOutput;
    private boolean isInput;
    private boolean isTransfer;
    private RecyclerView operationRecyclerView;
    private DtoAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentBudgetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();
        return root;
    }

    private void init() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}