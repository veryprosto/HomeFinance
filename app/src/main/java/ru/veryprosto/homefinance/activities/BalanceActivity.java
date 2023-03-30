package ru.veryprosto.homefinance.activities;

import android.os.Build;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import ru.veryprosto.homefinance.util.Util;

public class BalanceActivity extends AppCompatActivity {

    private MainController mainController;
    private ExpandableListView elvMain;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_layout);

        mainController = MainController.getInstance();

        List<Operation> operations = mainController.getOperationBetweenDates(null, null);

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

        elvMain = findViewById(R.id.elvMain);
        elvMain.setAdapter(adapter);
    }
}
