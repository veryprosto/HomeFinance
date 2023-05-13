package ru.veryprosto.homefinance.ui.operation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.adapter.DtoAdapter;
import ru.veryprosto.homefinance.controller.OperationController;
import ru.veryprosto.homefinance.databinding.FragmentOperationBinding;
import ru.veryprosto.homefinance.model.Account;
import ru.veryprosto.homefinance.model.Category;
import ru.veryprosto.homefinance.model.DTO;
import ru.veryprosto.homefinance.model.Operation;
import ru.veryprosto.homefinance.model.OperationType;
import ru.veryprosto.homefinance.util.DateRange;

public class OperationFragment extends Fragment {

    private FragmentOperationBinding binding;
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
        binding = FragmentOperationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();
        return root;
    }

    private void init() {
        operationController = OperationController.getInstance();

        dateRange = new DateRange();

        isOutput = true;
        isInput = true;
        isTransfer = true;

        initPickDateButton();
        initCheckBoxes();
        initAddOperationButton();
        fillOperationRecyclerView();
    }

    private void initAddOperationButton(){
        ImageButton addOperationButton = binding.btnAddOperation;
        addOperationButton.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.nav_edit_operation);
        });
    }

    private void initCheckBoxes() {
        CheckBox outputOperationsCheckBox = binding.chbxOuputOperations;

        outputOperationsCheckBox.setChecked(true);

        outputOperationsCheckBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            isOutput = isChecked;
            fillOperationRecyclerView();
        });

        CheckBox inputOperationsCheckBox = binding.chbxInputOperations;

        inputOperationsCheckBox.setChecked(true);

        inputOperationsCheckBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            isInput = isChecked;
            fillOperationRecyclerView();
        });

        CheckBox transferOperationsCheckBox = binding.chbxTransferOperations;

        transferOperationsCheckBox.setChecked(true);

        transferOperationsCheckBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            isTransfer = isChecked;
            fillOperationRecyclerView();
        });
    }

    private void fillOperationRecyclerView() {
        dtos = new ArrayList<>();
        List<OperationType> operationTypes = new ArrayList<>();

        if (isOutput) operationTypes.add(OperationType.OUTPUT);
        if (isInput) operationTypes.add(OperationType.INPUT);
        if (isTransfer) operationTypes.add(OperationType.TRANSFER);

        operations = operationController.getOperationByDatesAndTypes(dateRange, operationTypes);

        Map<Date, List<Operation>> operationMapByDate = operations.stream().collect(Collectors.groupingBy(Operation::getDate));

        Comparator<Date> comparator = Comparator.reverseOrder();
        SortedMap<Date, List<Operation>> sortedMap = new TreeMap<>(comparator);
        sortedMap.putAll(operationMapByDate);

        for (Map.Entry<Date, List<Operation>> entry : sortedMap.entrySet()) {
            String parentDate = DateRange.dateToString(entry.getKey());
            DTO empty = new DTO("");
            DTO parent = new DTO("    "+ parentDate);

            BigDecimal eventSumm = BigDecimal.ZERO;

            if (!dtos.isEmpty()){
                dtos.add(empty);
            }

            dtos.add(parent);

            List<Operation> operationsOneDay = entry.getValue();

            for (Operation operation : operationsOneDay) {
                Category category = operation.getCategory();
                BigDecimal summ = operation.getSumm();
                String description = operation.getDescription();
                Account firstAccount = operation.getFirstAccount();
                Account secondAccount = operation.getSecondAccount();
                int icon = operation.getCategory().getType().getIcon();

                summ = category.getType() == OperationType.INPUT ? summ : summ.negate();

                eventSumm = eventSumm.add(summ);
                parent.setRightUp(eventSumm.toString());

                DTO child = new DTO(category.getName(), summ.toString(), description, firstAccount.getName(), icon, category.getType());
                child.setId(operation.getId());
                child.setParentClassName(operation.getClass().getSimpleName());
                dtos.add(child);
            }
        }

        operationRecyclerView = binding.operationList;

        adapter = new DtoAdapter(this.requireActivity().getLayoutInflater(), dtos);

        operationRecyclerView.setAdapter(adapter);
    }

    @SuppressLint("SetTextI18n")
    private void initPickDateButton() {
        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();

        pickDateButton = binding.btnPickDate;

        final MaterialDatePicker<Pair<Long, Long>> materialDatePicker = materialDateBuilder.build();

        pickDateButton.setOnClickListener(
                v -> materialDatePicker.show(this.getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER"));

        materialDatePicker.addOnPositiveButtonClickListener(
                (MaterialPickerOnPositiveButtonClickListener<? super Pair<Long, Long>>) selection -> {
                    Pair<Long, Long> pairOfDates = materialDatePicker.getSelection();
                    if (pairOfDates != null) {
                        startPeriod = new Date(pairOfDates.first);
                        endPeriod = new Date(pairOfDates.second);
                        dateRange.setStart(startPeriod);
                        dateRange.setEnd(endPeriod);
                    }
                    pickDateButton.setText(dateRange.getStringStart() + "\n" + dateRange.getStringEnd());
                    fillOperationRecyclerView();
                });

        pickDateButton.setText(dateRange.getStringStart() + "\n" + dateRange.getStringEnd());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}