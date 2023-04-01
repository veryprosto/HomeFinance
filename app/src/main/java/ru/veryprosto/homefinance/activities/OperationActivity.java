package ru.veryprosto.homefinance.activities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import ru.veryprosto.homefinance.MainController;
import ru.veryprosto.homefinance.R;
import ru.veryprosto.homefinance.db.model.Operation;
import ru.veryprosto.homefinance.util.Util;

public class OperationActivity extends AppCompatActivity {
    private MainController mainController;
    private ExpandableListView operationElv;
    private Button pickDateButton;
    private Date startPeriod;
    private Date endPeriod;
    private List<Operation> operations;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operation_layout);

        init();
        fillOperationElv();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {
        mainController = MainController.getInstance();

        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
        startPeriod = cal.getTime();
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        endPeriod = cal.getTime();

        pickDateButton = findViewById(R.id.pickDateBtn);
        pickDateButton.setText(Util.dateToString(startPeriod) + "\n" + Util.dateToString(endPeriod));

        initPickDateButton();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void fillOperationElv(){
        operations = mainController.getOperationBetweenDates(startPeriod, endPeriod);

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
                tempMap.put("operation", operation.getCategory() + " " + operation.getSumm().toString());
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initPickDateButton(){
        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();

        materialDateBuilder.setTitleText("SELECT A DATE");

        final MaterialDatePicker<Pair<Long, Long>> materialDatePicker = materialDateBuilder.build();

        pickDateButton.setOnClickListener(
                v -> materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER"));

        materialDatePicker.addOnPositiveButtonClickListener(
                (MaterialPickerOnPositiveButtonClickListener) selection -> {
                    Pair<Long, Long> pairOfDates = (Pair<Long, Long>) materialDatePicker.getSelection();
                    startPeriod = new Date(pairOfDates.first);
                    endPeriod = new Date(pairOfDates.second);

                    pickDateButton.setText(Util.dateToString(startPeriod) + "\n" + Util.dateToString(endPeriod));
                    fillOperationElv();
                });
    }
}
