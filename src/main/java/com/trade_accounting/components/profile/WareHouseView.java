package com.trade_accounting.components.profile;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route(value = "warehouse", layout = AppView.class)
@PageTitle("Склады")
public class WareHouseView extends VerticalLayout {

    private final WarehouseService warehouseService;
    private Grid<WarehouseDto> grid = new Grid<>(WarehouseDto.class);
    private GridPaginator<WarehouseDto> paginator;
    private final GridFilter<WarehouseDto> filter;

    public WareHouseView(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
        paginator = new GridPaginator<>(grid, warehouseService.getAll(), 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        grid();
        this.filter = new GridFilter<>(grid);
        updateList();
        configureFilter();
        add(toolsUp(), filter, grid, paginator);
    }

    private void configureFilter() {
        filter.onSearchClick(e -> paginator.setData(warehouseService.search(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(warehouseService.getAll()));
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion("Добавьте описание");
    }

    private H2 title() {
        H2 title = new H2("Склады");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonWareHouse() {
        Button warehouseButton = new Button("Склад", new Icon(VaadinIcon.PLUS_CIRCLE));
        WareHouseModalWindow addWareHouseModalWindow =
                new WareHouseModalWindow(new WarehouseDto(), warehouseService);
        warehouseButton.addClickListener(event -> addWareHouseModalWindow.open());
        addWareHouseModalWindow.addDetachListener(event -> updateList());
        return warehouseButton;
    }

    private void updateList() {
        grid.setItems(warehouseService.getAll());
        GridSortOrder<WarehouseDto> gridSortOrder = new GridSortOrder(grid.getColumnByKey("sortNumber"), SortDirection.ASCENDING);
        List<GridSortOrder<WarehouseDto>> gridSortOrderList = new ArrayList<>();
        gridSortOrderList.add(gridSortOrder);
        grid.sort(gridSortOrderList);
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private TextField text() {
        TextField text = new TextField();
        text.setPlaceholder("Поиск");
        text.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        text.setValueChangeMode(ValueChangeMode.EAGER);
        text.addValueChangeListener(event -> updateListAfterSearch(text.getValue()));
        text.setWidth("300px");
        return text;
    }

    private void updateListAfterSearch(String text) {
        grid.setItems(warehouseService.findBySearch(text));
    }

    private NumberField numberField() {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect() {
        Select<String> valueSelect = new Select<>();
        valueSelect.setItems("Изменить");
        valueSelect.setValue("Изменить");
        valueSelect.setWidth("130px");
        return valueSelect;
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private HorizontalLayout toolsUp() {
        HorizontalLayout toolsUp = new HorizontalLayout();
        toolsUp.add(buttonQuestion(), title(), buttonRefresh(), buttonWareHouse(), buttonFilter(), text(), numberField(),
                valueSelect(), buttonSettings());
        toolsUp.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return toolsUp;
    }

    private TextField textField() {
        TextField textField = new TextField("", "1-1 из 1");
        textField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        return textField;
    }

    private Button doubleLeft() {
        return new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT));
    }

    private Button left() {
        return new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    }

    private Button doubleRight() {
        return new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT));
    }

    private Button right() {
        return new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    }

    private void grid() {
        grid.setItems(warehouseService.getAll());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setColumns("id", "name", "sortNumber", "address", "commentToAddress", "comment");
        grid.getColumnByKey("id").setHeader("id").setId("id");
        grid.getColumnByKey("name").setHeader("Имя").setId("Имя");
        grid.getColumnByKey("sortNumber").setHeader("Сортировочный номер").setId("Сортировочный номер");
        grid.getColumnByKey("address").setHeader("Адрес").setId("Адрес");
        grid.getColumnByKey("commentToAddress").setHeader("Комментарий к адресу").setId("Комментарий к адресу");
        grid.getColumnByKey("comment").setHeader("Комментарий").setId("Комментарий");
        grid.setHeight("64vh");
        grid.addItemDoubleClickListener(event -> {
            WarehouseDto editWarehouse = event.getItem();
            WareHouseModalWindow wareHouseModalWindow =
                    new WareHouseModalWindow(editWarehouse, warehouseService);
            wareHouseModalWindow.addDetachListener(e -> updateList());
            wareHouseModalWindow.open();
        });
    }
}
