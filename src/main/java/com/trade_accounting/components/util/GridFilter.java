package com.trade_accounting.components.util;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Element;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * Server-side component for filtering grid.
 */
public class GridFilter<T> extends HorizontalLayout {

    private final Grid<T> grid;
    private final HashMap<String, String> columnKey;

    private final Button searchButton;
    private final Button clearFieldsButton;

    public GridFilter(Grid<T> grid) {
        this.grid = grid;
        this.columnKey = new LinkedHashMap<>();

        this.searchButton = new Button("Найти");
        this.clearFieldsButton = new Button("Очистить");

        add(searchButton, clearFieldsButton);

        configureFilterField();

        this.getChildren().forEach(e -> {
            if (e.getId().isPresent()) {
                columnKey.put(e.getId().get(), null);
            }
        });

        this.getStyle().set("background-color", "#e7eaef")
                .set("border-radius", "4px")
                .set("align-items", "baseline")
                .set("flex-flow", "row wrap");

        this.setJustifyContentMode(JustifyContentMode.CENTER);

        this.setVisible(false);
    }

    private void configureFilterField() {
        grid.getColumns().forEach(e -> this.add(getFilterField(e.getKey())));
    }

    @SafeVarargs
    public final <I> void setFieldToComboBox(String columnKey, ItemLabelGenerator<I> itemLabelGenerator, I... items) {
        ComboBox<I> comboBox = getFilterComboBox(columnKey, itemLabelGenerator, items);

        this.getChildren().forEach(e -> {
            if(e.getId().isPresent()) {
                if(e.getId().get().equals(columnKey)) {
                    this.replace(e, comboBox);
                }
            }
        });
    }

    @SafeVarargs
    public final <I> void setFieldToComboBox(String columnKey, I... items) {
        ComboBox<I> comboBox = getFilterComboBox(columnKey, items);

        this.getChildren().forEach(e -> {
            if(e.getId().isPresent()) {
                if(e.getId().get().equals(columnKey)) {
                    this.replace(e, comboBox);
                }
            }
        });
    }

    public void setFieldToDatePicker(String columnKey) {
        DatePicker datePicker = getFilterDatePicker(columnKey);

        this.getChildren().forEach(e -> {
            if(e.getId().isPresent()) {
                if(e.getId().get().equals(columnKey)) {
                    this.replace(e, datePicker);
                }
            }
        });
    }

    public void setVisibleFields(Boolean visible, String... columnKey) {
        this.getChildren().forEach(e -> {
            boolean notButton = !e.toString().contains("button");

            if (e.getId().isPresent() && notButton) {
                for (String key : columnKey) {
                    if (e.getId().get().equals(key)) {
                        e.setVisible(visible);
                    }
                }
            }
        });
    }

    private TextField getFilterField(String columnKey) {
        TextField filter = new TextField();
        filter.setId(columnKey);

        filter.addValueChangeListener(e -> onFieldChange(filter));
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        if (grid.getColumnByKey(columnKey).getId().isPresent()) {
            filter.setLabel(grid.getColumnByKey(columnKey).getId().get());
        }

        return filter;
    }

    @SafeVarargs
    private <I> ComboBox<I> getFilterComboBox(String columnKey, ItemLabelGenerator<I> itemLabelGenerator, I... items) {
        ComboBox<I> filter = new ComboBox<>();
        filter.setId(columnKey);

        filter.setItemLabelGenerator(itemLabelGenerator);
        filter.setItems(items);

        if (grid.getColumnByKey(columnKey).getId().isPresent()) {
            filter.setLabel(grid.getColumnByKey(columnKey).getId().get());
        }

        return filter;
    }

    @SafeVarargs
    private <I> ComboBox<I> getFilterComboBox(String columnKey, I... items) {
        ComboBox<I> filter = new ComboBox<>();
        filter.setId(columnKey);

        filter.setItems(items);

        if (grid.getColumnByKey(columnKey).getId().isPresent()) {
            filter.setLabel(grid.getColumnByKey(columnKey).getId().get());
        }

        return filter;
    }

    private DatePicker getFilterDatePicker(String columnKey) {
        DatePicker filter = new DatePicker();
        filter.setId(columnKey);

        filter.setLocale(Locale.GERMAN);

        if (grid.getColumnByKey(columnKey).getId().isPresent()) {
            filter.setLabel(grid.getColumnByKey(columnKey).getId().get());
        }

        return filter;
    }

    private void onFieldChange(TextField field) {
        columnKey.put(field.getId().get(), field.getValue());

        System.out.println(columnKey.keySet() + " | " + columnKey.values());

        grid.
    }
}
