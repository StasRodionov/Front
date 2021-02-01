package com.trade_accounting.components.util;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Component for grid filtering.
 */
public class GridFilter<T> extends HorizontalLayout {

    private final Grid<T> grid;
    private final Map<String, String> filterData;

    private final Button searchButton;
    private final Button clearButton;

    /**
     * Creates a GridFilter.
     *
     * @param grid grid
     */
    public GridFilter(Grid<T> grid) {
        this.grid = grid;
        this.filterData = new HashMap<>();

        this.searchButton = new Button("Найти");
        this.clearButton = new Button("Очистить");

        add(searchButton, clearButton);

        configureFilterField();
        configureButton();

        this.getStyle().set("background-color", "#e7eaef")
                .set("border-radius", "4px")
                .set("align-items", "baseline")
                .set("flex-flow", "row wrap");

        this.setJustifyContentMode(JustifyContentMode.CENTER);

        this.setVisible(false);
    }

    /**
     * Sets field uses column key to ComboBox with specific item label generator and items.
     *
     * @param columnKey columnKey
     * @param itemLabelGenerator itemLabelGenerator
     * @param items items
     */
    @SafeVarargs
    public final <I> void setFieldToComboBox(String columnKey, ItemLabelGenerator<I> itemLabelGenerator, I... items) {
        ComboBox<I> comboBox = getFilterComboBox(columnKey, itemLabelGenerator, items);

        this.getChildren().forEach(e -> {
            if (e.getId().orElse("").equals(columnKey)) {
                this.replace(e, comboBox);
            }
        });
    }

    /**
     * Sets field uses column key to ComboBox with specific items.
     *
     * @param columnKey columnKey
     * @param items items
     */
    @SafeVarargs
    public final <I> void setFieldToComboBox(String columnKey, I... items) {
        ComboBox<I> comboBox = getFilterComboBox(columnKey, items);

        this.getChildren().forEach(e -> {
            if (e.getId().orElse("").equals(columnKey)) {
                this.replace(e, comboBox);
            }
        });
    }

    /**
     * Sets field uses column key to DatePicker.
     *
     * @param columnKey columnKey
     */
    public void setFieldToDatePicker(String columnKey) {
        DatePicker datePicker = getFilterDatePicker(columnKey);

        this.getChildren().forEach(e -> {
            if (e.getId().orElse("").equals(columnKey)) {
                this.replace(e, datePicker);
            }
        });
    }

    /**
     * Sets visible to fields from column key.
     *
     * @param visible visible
     * @param columnKey columnKey
     */
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

    /**
     * Add specific click listener to SearchButton.
     *
     * @param listener click listener
     */
    public void onSearchClick(ComponentEventListener<ClickEvent<Button>> listener) {
        searchButton.addClickListener(listener);
    }

    /**
     * Add specific click listener to ClearButton.
     *
     * @param listener click listener
     */
    public void onClearClick(ComponentEventListener<ClickEvent<Button>> listener) {
        clearButton.addClickListener(listener);
    }

    /**
     * Gets filter data from all fields.
     *
     * @return filter data
     */
    public Map<String, String> getFilterData() {
        return filterData;
    }

    private void configureButton() {
        clearButton.addClickListener(e -> this.getChildren().forEach(i -> {
            filterData.clear();

            if (i instanceof TextField) {
                ((TextField) i).clear();
            }

            if (i instanceof ComboBox) {
                ((ComboBox<?>) i).clear();
            }

            if (i instanceof DatePicker) {
                ((DatePicker) i).clear();
            }
        }));
    }

    private void configureFilterField() {
        grid.getColumns().forEach(e -> this.add(getFilterField(e.getKey())));
    }

    private TextField getFilterField(String columnKey) {
        TextField filter = new TextField();
        filter.setId(columnKey);

        filter.addValueChangeListener(e -> onFilterChange(filter));
        filter.setValueChangeMode(ValueChangeMode.TIMEOUT);
        if (grid.getColumnByKey(columnKey).getId().isPresent()) {
            filter.setLabel(grid.getColumnByKey(columnKey).getId().orElse(""));
        }

        return filter;
    }

    @SafeVarargs
    private <I> ComboBox<I> getFilterComboBox(String columnKey, ItemLabelGenerator<I> itemLabelGenerator, I... items) {
        ComboBox<I> filter = new ComboBox<>();
        filter.setId(columnKey);

        filter.setItemLabelGenerator(itemLabelGenerator);
        filter.setItems(items);
        filter.addValueChangeListener(e -> onFilterChange(filter));

        filter.setLabel(grid.getColumnByKey(columnKey).getId().orElse(""));

        return filter;
    }

    @SafeVarargs
    private <I> ComboBox<I> getFilterComboBox(String columnKey, I... items) {
        ComboBox<I> filter = new ComboBox<>();
        filter.setId(columnKey);

        filter.setItems(items);
        filter.addValueChangeListener(e -> onFilterChange(filter));

        filter.setLabel(grid.getColumnByKey(columnKey).getId().orElse(""));

        return filter;
    }

    private DatePicker getFilterDatePicker(String columnKey) {
        DatePicker filter = new DatePicker();
        filter.setId(columnKey);

        filter.setLocale(Locale.GERMAN);
        filter.addValueChangeListener(e -> onFilterChange(filter));

        filter.setLabel(grid.getColumnByKey(columnKey).getId().orElse(""));

        return filter;
    }

    private void onFilterChange(Component filter) {
        if (filter instanceof TextField) {
            filterData.put(filter.getId().orElse(""), ((TextField) filter).getValue());
        }

        if (filter instanceof ComboBox) {
            filterData.put(filter.getId().orElse(""), ((ComboBox<?>) filter).getValue().toString());
        }

        if (filter instanceof DatePicker) {
            filterData.put(filter.getId().orElse(""), ((DatePicker) filter).getValue().toString());
        }
    }
}
