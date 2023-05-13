package ru.veryprosto.homefinance.ui.category;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import ru.veryprosto.homefinance.controller.CategoryController;
import ru.veryprosto.homefinance.databinding.FragmentCategoryBinding;
import ru.veryprosto.homefinance.model.Category;
import ru.veryprosto.homefinance.model.OperationType;

public class CategoryFragment extends Fragment {
    private FragmentCategoryBinding binding;
    private GridLayout llCategories;
    private List<Button> buttonList;
    private List<Integer> colorList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        llCategories = binding.llCategories;

        buttonList = new ArrayList<>();

        initColorList();

        CategoryController categoryController = CategoryController.getInstance();

        List<Category> categoriesByTypes = categoryController.getCategoriesByTypes(OperationType.OUTPUT);

        for (Category category : categoriesByTypes) {
            addButton(category.getName());
        }

        for (Category category : categoriesByTypes) {
            addButton(category.getName());
        }

        return root;
    }

    private void initColorList() {
        colorList = new ArrayList<>();

        colorList.add(Color.rgb(255, 0, 0));
        colorList.add(Color.rgb(255, 64, 0));
        colorList.add(Color.rgb(255, 128, 0));
        colorList.add(Color.rgb(255, 191, 0));
        colorList.add(Color.rgb(255, 255, 0));
        colorList.add(Color.rgb(191, 255, 0));
        colorList.add(Color.rgb(128, 255, 0));
        colorList.add(Color.rgb(64, 255, 0));
        colorList.add(Color.rgb(0, 255, 0));
        colorList.add(Color.rgb(0, 255, 64));
        colorList.add(Color.rgb(0, 255, 128));
        colorList.add(Color.rgb(0, 255, 191));
        colorList.add(Color.rgb(0, 255, 255));
        colorList.add(Color.rgb(0, 191, 255));
        colorList.add(Color.rgb(0, 128, 255));
        colorList.add(Color.rgb(0, 64, 255));
        colorList.add(Color.rgb(0, 0, 255));
        colorList.add(Color.rgb(64, 0, 255));
        colorList.add(Color.rgb(128, 0, 255));
        colorList.add(Color.rgb(191, 0, 255));
        colorList.add(Color.rgb(255, 0, 255));
        colorList.add(Color.rgb(255, 0, 191));
        colorList.add(Color.rgb(255, 0, 128));
        colorList.add(Color.rgb(255, 0, 64));
        colorList.add(Color.rgb(255, 0, 0));
    }

    public void addButton(String name) {
        Button button = new Button(this.getContext());

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(250, 50);


        buttonParams.setMargins(0, 0, 20, 20);

        Integer colorInt = buttonList.size() * 9;

        while (colorInt > 24) {
            colorInt -= 25;
        }

        colorInt = colorList.get(colorInt);

        button.setLayoutParams(buttonParams);
        button.setId(buttonList.size());
        button.setTextSize(10);
        button.setText(name);
        button.setPadding(0, 0, 0, 0);

        Random random = new Random();

        //colorInt = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));

        button.setBackgroundColor(colorInt);
        button.setTextColor(Color.BLACK);

        // Устанавливаем слушателя
        button.setOnClickListener(v -> {
            int position = v.getId();  //  Получаем id (индекс в списке)
            List<Button> collect = buttonList.stream().filter(btn -> btn.getId() == position).collect(Collectors.toList());

            if (!collect.isEmpty()) {
                Button button1 = collect.get(0);
                buttonList.remove(button1);
                llCategories.removeView(button1);
            }
        });

        buttonList.add(button);
        llCategories.addView(button);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}