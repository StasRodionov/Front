package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.MovementDto;
import com.trade_accounting.models.dto.MovementProductDto;
import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.MovementProductService;
import com.trade_accounting.services.interfaces.MovementService;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.UnitService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringComponent
@PageTitle("Перемещения")
@Route(value = "movementView", layout = AppView.class)
@UIScope
public class MovementView extends VerticalLayout implements AfterNavigationObserver {

    private final MovementService movementService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final MovementProductService movementProductService;
    private final Notifications notifications;
    private final MovementViewModalWindow view;
    private final UnitService unitService;
    private final ProductService productService;

    private final Grid<MovementDto> grid = new Grid<>(MovementDto.class, false);
    private final GridPaginator<MovementDto> paginator;
    private final GridFilter<MovementDto> filter;

    private final TextField textField = new TextField();
    private final MenuBar selectXlsTemplateButton = new MenuBar();

    @Autowired
    public MovementView(MovementService movementService,
                        WarehouseService warehouseService,
                        CompanyService companyService,
                        MovementProductService movementProductService,
                        Notifications notifications, MovementViewModalWindow view, UnitService unitService, ProductService productService) {
        this.movementService = movementService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.movementProductService = movementProductService;
        this.notifications = notifications;
        this.view = view;
        this.unitService = unitService;
        this.productService = productService;
        List<MovementDto> data = getData();
        paginator = new GridPaginator<>(grid, data, 50);
        setSizeFull();
        configureGrid();
        filter = new GridFilter<>(grid);
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(configureActions(), filter, grid, paginator);
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(MovementDto::getDate).setKey("date").setHeader("Дата").setSortable(true);
        grid.addColumn(movementDto -> warehouseService.getById(movementDto.getWarehouseId())
                .getName()).setKey("warehouseDto").setHeader("Со Склада").setId("Со Склада");
        grid.addColumn(movementDto -> warehouseService.getById(movementDto.getWarehouseToId())
                .getName()).setKey("warehouseToDto").setHeader("На Склад").setId("На Склад");
        grid.addColumn(movementDto -> companyService.getById(movementDto.getCompanyId())
                .getName()).setKey("companyDto").setHeader("Организация").setId("Организация");
        grid.addColumn(this::getTotalPrice).setHeader("Сумма").setSortable(true);
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");
        grid.addColumn(new ComponentRenderer<>(this::getIsSentIcon)).setKey("sent").setHeader("Отправлено")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsPrintIcon)).setKey("print").setHeader("Напечатано")
                .setId("Напечатано");
        grid.setHeight("66vh");
        grid.setMaxWidth("100%");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.getColumns().forEach(column -> column.setAutoWidth(true));

        grid.addItemDoubleClickListener(e -> {
            MovementDto dto = e.getItem();
            MovementViewModalWindow modalView = new MovementViewModalWindow(
                    productService,
                    movementService,
                    warehouseService,
                    companyService,
                    notifications,
                    unitService,
                    movementProductService);
            modalView.setMovementForEdit(dto);
            modalView.open();
        });
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("warehouseDto", WarehouseDto::getName, warehouseService.getAll());
        filter.setFieldToComboBox("warehouseToDto", WarehouseDto::getName, warehouseService.getAll());
        filter.setFieldToComboBox("companyDto", CompanyDto::getName, companyService.getAll());
        filter.setFieldToCheckBox("sent");
        filter.setFieldToCheckBox("print");
        filter.onSearchClick(e -> paginator.setData(movementService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(movementService.getAll()));
    }

    private List<MovementDto> getData() {
        return movementService.getAll();
    }

    private void deleteSelectedMovements() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (MovementDto movementDto : grid.getSelectedItems()) {
                movementService.deleteById(movementDto.getId());
                notifications.infoNotification("Выбранные заказы успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные заказы");
        }
    }

    private HorizontalLayout configureActions() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), getTextOrder(), buttonRefresh(), buttonUnit(), buttonFilter(),
                numberField(), valueSelect(), valueStatus(), valuePrint(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upper;
    }

    private Component getIsSentIcon(MovementDto movementDto) {
        if (movementDto.getIsSent()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component getIsPrintIcon(MovementDto movementDto) {
        if (movementDto.getIsPrint()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private HorizontalLayout getTollBar() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(buttonQuestion(), getTextOrder(), buttonRefresh(), buttonUnit(),
                buttonFilter(), text(), numberField(), valueSelect(), valueStatus(),
                valuePrint(), buttonSettings(), selectXlsTemplateButton);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    protected String getTotalPrice(MovementDto movementDto) {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
//        for (Long id : movementDto.getMovementProductIds()) {
        for (Long id : movementDto.getMovementProductsIds()) {
            MovementProductDto movementProductDto = movementProductService.getById(id);
            totalPrice = totalPrice.add(movementProductDto.getAmount()
                    .multiply(movementProductDto.getPrice()));
        }
        return String.format("%.2f", totalPrice);
    }

    private H2 getTextOrder() {
        final H2 textOrder = new H2("Перемещения");
        textOrder.setHeight("2.2em");
        return textOrder;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion("Перемещение  - добавить текст");
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(ev -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    public void updateList() {
        grid.setItems(movementService.getAll());
    }

    private Button buttonUnit() {
        Button buttonUnit = new Button("Перемещение", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickListener(e -> {

            MovementViewModalWindow modalView = new MovementViewModalWindow(
                    productService,
                    movementService,
                    warehouseService,
                    companyService,
                    notifications,
                    unitService,
                    movementProductService);
            modalView.open();
        });
        return buttonUnit;
    }

    private Button buttonFilter() {
        return new Button("Фильтр", clickEvent -> {
            filter.setVisible(!filter.isVisible());
        });
    }

    private TextField text() {
        textField.setWidth("300px");
        textField.setPlaceholder("Наименование или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setClearButtonVisible(true);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        setSizeFull();
        return textField;
    }

    private NumberField numberField() {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect() {
        Select<String> select = new Select<>();
        List<String> listItems = new ArrayList<>();
        listItems.add("Изменить");
        listItems.add("Удалить");
        select.setItems(listItems);
        select.setValue("Изменить");
        select.setWidth("130px");
        select.addValueChangeListener(event -> {
            if (select.getValue().equals("Удалить")) {
                deleteSelectedMovements();
                grid.deselectAll();
                select.setValue("Изменить");
                paginator.setData(getData());
            }
        });
        return select;
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private Select<String> valueStatus() {
        Select<String> status = new Select<>();
        status.setItems("Статус");
        status.setValue("Статус");
        status.setWidth("110px");
        return status;
    }

    private Select<String> valuePrint() {
        Select<String> print = new Select<>();
        print.setItems("Печать", "Добавить шаблон");
        print.setValue("Печать");
        print.setWidth("110px");
        return print;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
