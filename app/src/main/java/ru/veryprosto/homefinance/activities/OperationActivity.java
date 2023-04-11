package ru.veryprosto.homefinance.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.SimpleExpandableListAdapter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import ru.veryprosto.homefinance.MainController;
import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.db.model.Operation;
import ru.veryprosto.homefinance.db.model.OperationType;
import ru.veryprosto.homefinance.util.DateRange;
import ru.veryprosto.homefinance.util.Util;

public class OperationActivity extends AppCompatActivity {
    private MainController mainController;
    private ExpandableListView operationElv;

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
        mainController = MainController.getInstance();

        dateRange = new DateRange();

        isOutput = true;
        isInput = true;
        isTransfer = true;

        initPickDateButton();
        initAddOperationButtons();
        initReturnMenuButton();
        initCheckBoxes();
        fillOperationElv();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void fillOperationElv() {
        List<OperationType> operationTypes = new ArrayList<>();
        if (isOutput) operationTypes.add(OperationType.OUTPUT);
        if (isInput) operationTypes.add(OperationType.INPUT);
        if (isTransfer) operationTypes.add(OperationType.TRANSFER);

        operations = mainController.getOperationByDatesAndTypes(dateRange, operationTypes);

        List<Map<String, String>> parentData = new ArrayList<>();
        List<List<Map<String, String>>> childData = new ArrayList<>();

        Map<String, String> tempMap;

        Map<Date, List<Operation>> operationMapByDate = operations.stream().collect(Collectors.groupingBy(Operation::getDate));

        Comparator<Date> comparator = Comparator.naturalOrder();

        SortedMap<Date, List<Operation>> sortedMap = new TreeMap<>(comparator);

        sortedMap.putAll(operationMapByDate);


        for (Map.Entry<Date, List<Operation>> entry : sortedMap.entrySet()) {
            tempMap = new HashMap<>();
            tempMap.put("date", Util.dateToString(entry.getKey()));
            parentData.add(tempMap);

            List<Map<String, String>> childDataItem = new ArrayList<>();

            List<Operation> value = entry.getValue();

            for (Operation operation : value) {
                tempMap = new HashMap<>();
                tempMap.put("operation", operation.getAccount() + " " + operation.getCategory() + " " + operation.getSumm().toString());
                childDataItem.add(tempMap);
            }

            childData.add(childDataItem);
        }

        String[] groupFrom = new String[]{"date"};
        int[] groupTo = new int[]{android.R.id.text1};
        String[] childFrom = new String[]{"operation"};
        int[] childTo = new int[]{android.R.id.text1};

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                this,
                parentData,
                android.R.layout.simple_expandable_list_item_1,
                groupFrom,
                groupTo,
                childData,
                android.R.layout.simple_list_item_1,
                childFrom,
                childTo);

        operationElv = findViewById(R.id.elvMain);
        operationElv.setAdapter(adapter);
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
                    Pair<Long, Long> pairOfDates = (Pair<Long, Long>) materialDatePicker.getSelection();
                    if (pairOfDates != null) {
                        startPeriod = new Date(pairOfDates.first);
                        endPeriod = new Date(pairOfDates.second);
                    }
                    pickDateButton.setText(Util.dateToString(startPeriod) + "\n" + Util.dateToString(endPeriod));
                    fillOperationElv();
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
            fillOperationElv();
        });

        CheckBox inputOperationsCheckBox = findViewById(R.id.inputOperationsCheckBox);
        boolean inputOperations = inputOperationsCheckBox.isChecked();

        inputOperationsCheckBox.setChecked(true);

        inputOperationsCheckBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            isInput = isChecked;
            fillOperationElv();
        });

    }
}
