package ru.veryprosto.homefinance.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
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

import ru.veryprosto.homefinance.DTO.DTO;
import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.controller.OperationController;
import ru.veryprosto.homefinance.model.Account;
import ru.veryprosto.homefinance.model.Operation;
import ru.veryprosto.homefinance.model.OperationType;
import ru.veryprosto.homefinance.temp.OperationDTOAdapter;
import ru.veryprosto.homefinance.util.DateRange;
import ru.veryprosto.homefinance.util.Util;

public class OperationActivity extends AppCompatActivity {
    private OperationController operationController;
    private List<DTO> states;
    private RecyclerView operationRecyclerView;

    private Date startPeriod;
    private Date endPeriod;
    private boolean isOutput;
    private boolean isInput;
    private boolean isTransfer;
    private List<Operation> operations;
    private DateRange dateRange;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operation_layout);
        init();
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {
        operationController = OperationController.getInstance();

        dateRange = new DateRange();

        isOutput = true;
        isInput = true;
        isTransfer = true;

        initPickDateButton();
        initAddOperationButtons();
        initReturnMenuButton();
        initCheckBoxes();
        fillOperationRecyclerView();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void fillOperationRecyclerView() {
        states = new ArrayList<>();

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
            String parentDate = Util.dateToString(entry.getKey());
            DTO parent = new DTO(parentDate);
            parent.setParentId(parentDate);
            parent.setParent(true);

            BigDecimal eventSumm = BigDecimal.ZERO;

            states.add(parent);

            List<Operation> value = entry.getValue();

            for (Operation operation : value) {
                String category = operation.getCategory().getName();
                BigDecimal summ = operation.getSumm();
                String description = operation.getDescription();
                Account account = operation.getAccount();
                int icon = operation.getCategory().getType().getIcon();

                DTO child = new DTO(category, summ.toString(), description, account.getName(), icon);
                child.setParentId(parentDate);

                eventSumm = eventSumm.add(summ);
                parent.setRightUp(eventSumm.toString());
                states.add(child);
            }
        }

        operationRecyclerView = findViewById(R.id.operationList);
        OperationDTOAdapter adapter = new OperationDTOAdapter(this, states);
        operationRecyclerView.setAdapter(adapter);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initPickDateButton() {
        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();

        Button pickDateButton = findViewById(R.id.pickDateBtn);

        materialDateBuilder.setTitleText("SELECT A DATE");

        final MaterialDatePicker<Pair<Long, Long>> materialDatePicker = materialDateBuilder.build();

        pickDateButton.setOnClickListener(
                v -> materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER"));

        materialDatePicker.addOnPositiveButtonClickListener(
                (MaterialPickerOnPositiveButtonClickListener<? super Pair<Long, Long>>) selection -> {
                    Pair<Long, Long> pairOfDates = materialDatePicker.getSelection();
                    if (pairOfDates != null) {
                        startPeriod = new Date(pairOfDates.first);
                        endPeriod = new Date(pairOfDates.second);
                    }
                    pickDateButton.setText(Util.dateToString(startPeriod) + "\n" + Util.dateToString(endPeriod));
                    fillOperationRecyclerView();
                });

        pickDateButton.setText(dateRange.getStringStart() + "\n" + dateRange.getStringEnd());
    }

    private void initAddOperationButtons() {
        ImageButton addInputOperationButton = findViewById(R.id.addInputOperationBtn);

        addInputOperationButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, OperationEditActivity.class);
            intent.putExtra("operationType", OperationType.INPUT);
            startActivity(intent);
        });

        ImageButton addOutputOperationButton = findViewById(R.id.addOutputOperationBtn);

        addOutputOperationButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, OperationEditActivity.class);
            intent.putExtra("operationType", OperationType.OUTPUT);
            startActivity(intent);
        });

        ImageButton addTransferOperationButton = findViewById(R.id.addTransferOperationBtn);

        addTransferOperationButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, OperationEditActivity.class);
            intent.putExtra("operationType", OperationType.TRANSFER);
            startActivity(intent);
        });
    }

    private void initReturnMenuButton() {
        ImageButton returnMenuBtn = findViewById(R.id.returnMenuBtn);
        returnMenuBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initCheckBoxes() {
        CheckBox outputOperationsCheckBox = findViewById(R.id.outputOperationsCheckBox);

        outputOperationsCheckBox.setChecked(true);

        outputOperationsCheckBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            isOutput = isChecked;
            fillOperationRecyclerView();
        });

        CheckBox inputOperationsCheckBox = findViewById(R.id.inputOperationsCheckBox);

        inputOperationsCheckBox.setChecked(true);

        inputOperationsCheckBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            isInput = isChecked;
            fillOperationRecyclerView();
        });

        CheckBox transferOperationsCheckBox = findViewById(R.id.transferOperationsCheckBox);

        transferOperationsCheckBox.setChecked(true);

        transferOperationsCheckBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            isTransfer = isChecked;
            fillOperationRecyclerView();
        });

    }
}
