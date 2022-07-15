package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridConfigurer;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.warehouse.MovementDto;
import com.trade_accounting.models.dto.warehouse.MovementProductDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.company.BankAccountService;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.LegalDetailService;
import com.trade_accounting.services.interfaces.util.ColumnsMaskService;
import com.trade_accounting.services.interfaces.warehouse.MovementProductService;
import com.trade_accounting.services.interfaces.warehouse.MovementService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.trade_accounting.services.interfaces.units.UnitService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
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
import com.vaadin.flow.data.provider.SortDirection;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.trade_accounting.config.SecurityConstants.GOODS_MOVEMENT_VIEW;
import static com.trade_accounting.config.SecurityConstants.GRID_GOODS_MAIN_MOVEMENT;

@SpringComponent
@PageTitle("Перемещения")
@Route(value = GOODS_MOVEMENT_VIEW, layout = AppView.class)
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
    private final LegalDetailService legalDetailService;
    private final BankAccountService bankAccountService;
    private final EmployeeService employeeService;

    private final Grid<MovementDto> grid = new Grid<>(MovementDto.class, false);
    private final GridConfigurer<MovementDto> gridConfigurer;
    private final GridPaginator<MovementDto> paginator;
    private final GridFilter<MovementDto> filter;

    private final TextField textField = new TextField();
    private final MenuBar selectXlsTemplateButton = new MenuBar();
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES,
            GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};

    @Autowired
    public MovementView(MovementService movementService,
                        WarehouseService warehouseService,
                        CompanyService companyService,
                        MovementProductService movementProductService,
                        Notifications notifications,
                        MovementViewModalWindow view,
                        UnitService unitService,
                        ProductService productService,
                        LegalDetailService legalDetailService,
                        BankAccountService bankAccountService,
                        EmployeeService employeeService,
                        ColumnsMaskService columnsMaskService) {
        this.movementService = movementService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.movementProductService = movementProductService;
        this.notifications = notifications;
        this.view = view;
        this.unitService = unitService;
        this.productService = productService;
        this.legalDetailService = legalDetailService;
        this.bankAccountService = bankAccountService;
        this.employeeService = employeeService;
        this.gridConfigurer = new GridConfigurer<>(grid, columnsMaskService, GRID_GOODS_MAIN_MOVEMENT);
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
        grid.addThemeVariants(GRID_STYLE);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(MovementDto::getDate).setHeader("Время").setKey("dateAfter").setId("Время");
        grid.addColumn(movementDto -> warehouseService.getById(movementDto.getWarehouseId())
                        .getName()).setHeader("Со Склада")
                .setKey("warehouseDto")
                .setId("Со Склада");
        grid.addColumn(movementDto -> warehouseService.getById(movementDto.getWarehouseToId())
                        .getName()).setHeader("На Склад")
                .setKey("warehouseToDto")
                .setId("На Склад");
        grid.addColumn(movementDto -> companyService.getById(movementDto.getCompanyId())
                        .getName()).setHeader("Организация")
                .setKey("companyDto")
                .setId("Организация");
        grid.addColumn(this::getTotalPrice).setHeader("Сумма").setTextAlign(ColumnTextAlign.END)
                .setId("Сумма");
        grid.addColumn("comment").setHeader("Комментарий")
                .setId("Комментарий");
        grid.addColumn(new ComponentRenderer<>(this::getIsSentIcon)).setHeader("Отправлено").setKey("sent")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsPrintIcon)).setHeader("Напечатано").setKey("print")
                .setId("Напечатано");
        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        gridConfigurer.addConfigColumnToGrid();

        grid.setHeight("66vh");
        GridSortOrder<MovementDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
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
                    movementProductService,
                    legalDetailService,
                    bankAccountService,
                    employeeService);
            modalView.setMovementForEdit(dto);
            modalView.open();
        });
    }


    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("dateAfter");
        filter.setFieldToComboBox("warehouseDto", WarehouseDto::getName, warehouseService.getAll());
        filter.setFieldToComboBox("warehouseToDto", WarehouseDto::getName, warehouseService.getAll());
        filter.setFieldToComboBox("companyDto", CompanyDto::getName, companyService.getAll());
        filter.setFieldToCheckBox("sent");
        filter.setFieldToCheckBox("print");
        filter.onSearchClick(e -> {
            Map<String, String> map = filter.getFilterDataBetween();
            if(map.get("dateAfter") != null && map.get("dateBefore") != null){
                paginator.setData(movementService.searchByBetweenDataFilter(map));
            } else {
                paginator.setData(movementService.searchByFilter(map));
            }        });
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
                    movementProductService,
                    legalDetailService,
                    bankAccountService,
                    employeeService);
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
        return SelectConfigurer.configureDeleteSelect(() -> {
            deleteSelectedMovements();
            grid.deselectAll();
            paginator.setData(getData());
        });
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private Select<String> valueStatus() {
        return SelectConfigurer.configureStatusSelect();
    }

    private Select<String> valuePrint() {
        return SelectConfigurer.configurePrintSelect();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
