package com.trade_accounting.components.util;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Component for grid filtering.
 */
@Slf4j
public class GridFilter<T> extends HorizontalLayout {

    private final Grid<T> grid;
    private final Map<String, String> filterData;

    private Button searchButton;
    private Button clearButton;
    private Button configureFieldsButton;
    private Button addBookmarkButton;

    /**
     * Creates a GridFilter.
     *
     * @param grid grid
     */
    public GridFilter(Grid<T> grid) {
        this.grid = grid;
        this.filterData = new HashMap<>();

        configureLayout();
        configureFilterField();
        configureButton();

       }

    /**
     * Sets field uses column key to ComboBox with specific item label generator and items.
     *
     * @param columnKey          columnKey
     * @param itemLabelGenerator itemLabelGenerator
     * @param items              items
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
     * Sets field uses column key to ComboBox with specific item label generator and items.
     *
     * @param columnKey          columnKey
     * @param itemLabelGenerator itemLabelGenerator
     * @param items              items
     */
    public final <I> void setFieldToComboBox(String columnKey, ItemLabelGenerator<I> itemLabelGenerator, List<I> items) {
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
     * @param items     items
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
        DateTimePicker dateTimePicker = getFilterDatePicker(columnKey);

        this.getChildren().forEach(e -> {
            if (e.getId().orElse("").equals(columnKey)) {
                this.replace(e, dateTimePicker);
            }
        });
    }

    /**
     * Sets field uses column key to IntengerField
     *
     * @param columnKey columnKey
     */
    public void setFieldToIntegerField(String columnKey) {
        IntegerField integerField = getFilterIntegerField(columnKey);

        this.getChildren().forEach(e -> {
            if (e.getId().orElse("").equals(columnKey)) {
                this.replace(e, integerField);
            }
        });
    }

    public void setFieldToCheckBox(String columnKey) {
        Checkbox checkbox = getFilterCheckbox(columnKey);

        this.getChildren().forEach(e -> {
            if (e.getId().orElse("").equals(columnKey)) {
                this.replace(e, checkbox);
            }
        });
    }

    /**
     * Sets visible to fields from column key.
     *
     * @param visible   visible
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
        String datetime = filterData.get("date");
        if (datetime != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                filterData.put("date", LocalDate.parse(datetime).atTime(0, 0, 0, 0).format(formatter));
            } catch (Exception ignored) {
            }
        }
        return filterData;
    }

    /**
     * Gets filter data from all fields.
     *
     * @return filter data
     * <p>
     * Conversion to format LocalDateTime.
     */
    public Map<String, String> getFilterData2() {
        String datetime = filterData.get("date");
        if (datetime != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                filterData.put("date", LocalDateTime.parse(datetime).format(formatter));
            } catch (Exception ignored) {
            }
        }

        return filterData;
    }

    private void configureButton() {
        clearButton.addClickListener(e -> this.getChildren().forEach(component -> {
            if (component instanceof AbstractField) {
                ((AbstractField<?, ?>) component).clear();
            }
        }));

        addBookmarkButton.addClickListener(e -> {
            Dialog dialog = new Dialog();
            H3 title = new H3("Закладки");
            Label label = new Label("Название");
            TextField textField = new TextField();
            textField.setAutofocus(true);
            textField.setWidth("350px");
            HorizontalLayout hl1 = new HorizontalLayout(label, textField);
            Button save = new Button("Сохранить закладку");
            save.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
            Button save1 = new Button("Сохранить закладку");
            save1.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
            Button cancel = new Button("Отменить", new Icon(VaadinIcon.CLOSE));
            Button del = new Button("Удалить");
            del.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
            del.setVisible(false);
            HorizontalLayout hl2 = new HorizontalLayout(save,cancel, del);
            dialog.setHeight("200px");
            dialog.setWidth("550px");
            dialog.add(title, hl1, hl2);

            //кнопка сохранить при нажатии на значок закладки
            save.addClickListener(clickEvent -> {
                Button bookmarksButton = new Button(textField.getValue());
                bookmarksButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
                Button edit = new Button(new Icon(VaadinIcon.PENCIL));
                add(bookmarksButton, edit);

                bookmarksButton.addClickListener(b -> {
                        });

                edit.addClickListener(click -> {
                    del.setVisible(true);
                    save.setVisible(false);
                    HorizontalLayout hl = new HorizontalLayout(save1,cancel,del);
                    dialog.add(hl);
                    dialog.open();
                    del.addClickListener(d -> {
                        bookmarksButton.setVisible(false);
                       edit.setVisible(false);
                       dialog.close();
                    });
                    save1.addClickListener(s -> //кнопка сохранить при редактировании закладки
                        dialog.close());
                });
                dialog.close();
            });
            cancel.addClickListener(clickEvent -> dialog.close());
            dialog.open();
        });
        configureFieldsButton.addClickListener(e -> {
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.setTarget(configureFieldsButton);
            contextMenu.setOpenOnClick(true);

            CheckboxGroup<Component> checkboxGroup = new CheckboxGroup<>();
            contextMenu.addItem(checkboxGroup);

            List<Component> components = this.getChildren()
                    .filter(c -> c instanceof AbstractField).collect(Collectors.toList());

            checkboxGroup.setItems(components.stream());
            checkboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
            checkboxGroup.setItemLabelGenerator(component -> grid.getColumnByKey(component.getId().orElse("")).getId().orElse(""));
            checkboxGroup.setValue(components.stream().filter(Component::isVisible).collect(Collectors.toSet()));
            checkboxGroup.addSelectionListener(selection -> {
                selection.getAddedSelection().forEach(i -> i.setVisible(true));
                selection.getRemovedSelection().forEach(i -> i.setVisible(false));
            });
        });
    }

    private void configureLayout() {
        this.searchButton = new Button("Найти");
        this.clearButton = new Button("Очистить");
        this.addBookmarkButton = new Button(new Icon(VaadinIcon.BOOKMARK_O));
        this.configureFieldsButton = new Button(new Icon(VaadinIcon.COG_O));

        add(searchButton, clearButton, addBookmarkButton, configureFieldsButton);

        this.getStyle().set("background-color", "#e7eaef")
                .set("border-radius", "4px")
                .set("align-items", "baseline")
                .set("flex-flow", "row wrap");

        this.setJustifyContentMode(JustifyContentMode.CENTER);
        this.setWidthFull();

        this.setVisible(false);
    }

    private void configureFilterField() {
        try {
            grid.getColumns().forEach(e -> {
                if (!e.getKey().equals("imageDto") && !e.getKey().equals("sumOut")) {
                    this.add(getFilterTextField(e.getKey()));
                }
            });
        } catch (NullPointerException e) {
        }
    }

    private TextField getFilterTextField(String columnKey) {
        TextField filter = new TextField();
        filter.setMaxWidth("150px");
        filter.setId(columnKey);
        filter.addValueChangeListener(e -> onFilterChange(filter));
        filter.setValueChangeMode(ValueChangeMode.TIMEOUT);
        Grid.Column<T> column = grid.getColumnByKey(columnKey);
        Optional<String> id = column.getId();
        if (grid.getColumnByKey(columnKey).getId().isPresent()) {
            filter.setLabel(grid.getColumnByKey(columnKey).getId().orElse(""));
        }

        return filter;
    }

    private IntegerField getFilterIntegerField(String columnKey) {
        IntegerField filter = new IntegerField();
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

    private <I> ComboBox<I> getFilterComboBox(String columnKey, ItemLabelGenerator<I> itemLabelGenerator, List<I> items) {
        ComboBox<I> filter = new ComboBox<>();
        filter.setId(columnKey);

        filter.setItemLabelGenerator(itemLabelGenerator);
        filter.setItems(items);
        filter.addValueChangeListener(e -> onFilterComboBoxChange(filter, itemLabelGenerator));

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


    private DateTimePicker getFilterDatePicker(String columnKey) {
        DateTimePicker filter = new DateTimePicker();
        filter.setId(columnKey);
        filter.setLocale(Locale.GERMAN);
        filter.addValueChangeListener(e -> onFilterChange(filter));
        filter.setLabel(grid.getColumnByKey(columnKey).getId().orElse(""));

        return filter;
    }

    private Checkbox getFilterCheckbox(String columnKey) {
        Checkbox checkbox = new Checkbox();
        checkbox.setId(columnKey);
        checkbox.addValueChangeListener(e -> onFilterChange(checkbox));
        checkbox.setLabel(grid.getColumnByKey(columnKey).getId().orElse(""));

        return checkbox;
    }

    private void onFilterChange(Component filter) {
        if (filter instanceof AbstractField) {
            if (!((AbstractField<?, ?>) filter).isEmpty()) {
                filterData.put(filter.getId().orElse(""), ((AbstractField<?, ?>) filter).getValue().toString());
            } else {
                filterData.remove(filter.getId().orElse(""));
            }
        }
    }

    private <I> void onFilterComboBoxChange(ComboBox<I> comboBox, ItemLabelGenerator<I> itemLabelGenerator) {
        if (comboBox != null) {
            if (!comboBox.isEmpty()) {
                filterData.put(comboBox.getId().orElse(""), itemLabelGenerator.apply(comboBox.getValue()));
            } else {
                filterData.remove(comboBox.getId().orElse(""));
            }
        }
    }
}


