package com.trade_accounting.components.purchases;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.purchases.print.PrintPurchasingManagementXls;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.MenuBarIcon;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.TemplateDto;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.company.SupplierAccountDto;
import com.trade_accounting.models.dto.purchases.PurchaseControlDto;
import com.trade_accounting.models.dto.warehouse.ProductDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.purchases.PurchaseControlService;
import com.trade_accounting.services.interfaces.purchases.PurchaseCurrentBalanceService;
import com.trade_accounting.services.interfaces.purchases.PurchaseForecastService;
import com.trade_accounting.services.interfaces.purchases.PurchaseHistoryOfSalesService;
import com.trade_accounting.services.interfaces.warehouse.ProductPriceService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.trade_accounting.config.SecurityConstants.*;

@Slf4j
//Если на страницу не ссылаются по URL или она не является отдельной страницей, а подгружается родительским классом, то URL и Title не нужен
/*@Route(value = PURCHASES_PURCHASING_MANAGEMENT_VIEW, layout = AppView.class)
@PageTitle("Управление закупками")*/
@SpringComponent
@UIScope
public class PurchasesSubPurchasingManagement extends VerticalLayout implements AfterNavigationObserver {

    private final EmployeeService employeeService;
    private final PurchaseControlService purchaseControlService;
    private final Notifications notifications;
    private final SupplierAccountModalView modalView;
    private final TextField textField = new TextField();
    private final ProductPriceService productPriceService;
    private final ProductService productService;
    private final PurchaseHistoryOfSalesService purchaseHistoryOfSalesService;
    private final PurchaseCurrentBalanceService purchaseCurrentBalanceService;
    private final PurchaseForecastService purchaseForecastService;
    private final MenuItem print;
    private final MenuBar selectXlsTemplateButton = new MenuBar();
    PurchasesSubPurchasingManagementModalWindow purchasesSubPurchasingManagementModalWindow;
    //  private final String typeOfInvoice = "EXPENSE";
    //  private final SalesEditCreateInvoiceView salesEditCreateInvoiceView;

    private VerticalLayout toolbarWithFilters;
    private final CompanyService companyService;
    private final ContractorService contractorService;
    private final WarehouseService warehouseService;

    private List<DateTimePicker> dates = new ArrayList<>();
    private ComboBox<ProductDto> productComboBox = new ComboBox();
    private ComboBox<String> availableComboBox = new ComboBox();
    private ComboBox<String> soldComboBox = new ComboBox();
    private ComboBox<String> remainderComboBox = new ComboBox();
    private ComboBox<WarehouseDto> warehouseComboBox = new ComboBox();
    private ComboBox<ContractorDto> contractorComboBox = new ComboBox();
    private ComboBox<CompanyDto> companyComboBox = new ComboBox();

    private LocalDateTime departureDate;
    private LocalDateTime returnDate;
    private Long productId;
    private String available;
    private String sold;
    private String remainder;
    private Long contractorId;
    private Long companyId;
    private Long warehouseId;

    private Select<String> print1;

    private List<PurchaseControlDto> purchaseControl;
    private HorizontalLayout actions;
    private final Grid<PurchaseControlDto> grid = new Grid<>(PurchaseControlDto.class, false);
    private GridPaginator<PurchaseControlDto> paginator;
    private final GridFilter<PurchaseControlDto> filter;
    private final String pathForSaveXlsTemplate = "src/main/resources/xls_templates/purchases_templates/purchasesManagement/";

    @Autowired
    public PurchasesSubPurchasingManagement(EmployeeService employeeService, PurchaseControlService purchaseControlService,
                                            @Lazy Notifications notifications,
                                            SupplierAccountModalView modalView,
                                            ProductPriceService productPriceService,
                                            PurchaseHistoryOfSalesService purchaseHistoryOfSalesService,
                                            PurchaseCurrentBalanceService purchaseCurrentBalanceService,
                                            PurchaseForecastService purchaseForecastService,
                                            //   SalesEditCreateInvoiceView salesEditCreateInvoiceView,
                                            ProductService productService,
                                            CompanyService companyService,
                                            ContractorService contractorService,
                                            WarehouseService warehouseService,
                                            PurchasesSubPurchasingManagementModalWindow purchasesSubPurchasingManagementModalWindow) {
        this.purchasesSubPurchasingManagementModalWindow = purchasesSubPurchasingManagementModalWindow;
        this.productService = productService;
        this.employeeService = employeeService;
        this.purchaseControlService = purchaseControlService;
        this.notifications = notifications;
        this.modalView = modalView;
        this.productPriceService = productPriceService;
        this.purchaseHistoryOfSalesService = purchaseHistoryOfSalesService;
        this.purchaseCurrentBalanceService = purchaseCurrentBalanceService;
        this.purchaseForecastService = purchaseForecastService;
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.warehouseService = warehouseService;
        this.toolbarWithFilters = ToolbarFilters();
        print = selectXlsTemplateButton.addItem("Печать");

        loadSupplierAccounts();
        configureActions();
        configureGrid();
        configurePaginator();

        this.filter = new GridFilter<>(grid);
        configureFilter();
        add(actions, toolbarWithFilters, grid, paginator);
        toolbarWithFilters.setVisible(false);
        configureSelectXlsTemplateButton();
    }

    private List<PurchaseControlDto> loadSupplierAccounts() {
        purchaseControl = purchaseControlService.getAll();
        return purchaseControl;
    }

    private void configureActions() {
        actions = new HorizontalLayout();
        actions.add(buttonQuestion(),
                title(),
                buttonRefresh(),
                buttonFilter(),
                orderSupplier(),
                createLabel("Прогноз на"),
                numberField(),
                createLabel("дней"),
//                valuePrint(),
                selectXlsTemplateButton,
                buttonSettings()
        );
        actions.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion("Раздел позволяет проанализировать продажи и на основе этих " +
                "данных сформировать заказ поставщику. " +
                "Общий заказ создается без указания поставщика — добавить его " +
                "можно позже. Если разбить заказы по поставщикам, будет создано " +
                "несколько заказов на поставщиков, указанных в карточках " +
                "товаров.");
    }


    public VerticalLayout ToolbarFilters() {
        VerticalLayout filterToolbar = new VerticalLayout();
        filterToolbar.add(ToolbarFiltersLineOne(), ToolbarFiltersLineTwo());
        filterToolbar.addClassName("toolbarWithFilters");
        return filterToolbar;
    }

    public HorizontalLayout ToolbarFiltersLineOne() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(ButtonFilterSearch(), ButtonClearFilter(), getDatePickerDateRange().get(0), getDatePickerDateRange().get(1),
                getComboBoxProduct(), getComboBoxRemainder(), getComboBoxAvailable());
        toolbar.addClassName("toolbarWithFilters");

        return toolbar;
    }

    public HorizontalLayout ToolbarFiltersLineTwo() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(getComboBoxSold(), getComboBoxWarehouse(), getComboBoxContractor(), getComboBoxCompany());
        toolbar.addClassName("toolbarWithFilters");

        return toolbar;
    }

    private Button ButtonFilterSearch() {
        Button search = new Button("Найти");
        search.addClickListener(event -> {
            Map<String, String> map = new HashMap();
            if (departureDate != null) {
                map.put("startDate", departureDate.toString());
            }
            if (returnDate != null) {
                map.put("endDate", returnDate.toString());
            }
            if (productId != null) {
                map.put("productId", productId.toString());
            }
            if (availableComboBox.getValue() != null) {
                map.put("available", availableComboBox.getValue());
            }
            if (soldComboBox.getValue() != null) {
                map.put("sold", soldComboBox.getValue());
            }
            if (remainderComboBox.getValue() != null) {
                map.put("remainder", remainderComboBox.getValue());
            }
            if (warehouseId != null) {
                map.put("warehouseId", warehouseId.toString());
            }
            if (contractorId != null) {
                map.put("contractorId", contractorId.toString());
            }
            if (companyId != null) {
                map.put("companyId", companyId.toString());
            }
            List<PurchaseControlDto> list = purchaseControlService.newFilter(map);
            if (list != null) {
                paginator.setData(list);
            }
        });
        return search;
    }

    private Button ButtonClearFilter() {
        Button clearButton = new Button("Очистить");
        clearButton.addClickListener(e -> {
            toolbarWithFilters = ToolbarFilters();
            purchaseControl = purchaseControlService.getAll();
            updateList();
        });
        return clearButton;
    }

    public List<DateTimePicker> getDatePickerDateRange() {
        DateTimePicker dDate = new DateTimePicker("Начальная дата");
        DateTimePicker rDate = new DateTimePicker("Конечная дата");
        dDate.addValueChangeListener(event -> departureDate = event.getValue());
        rDate.addValueChangeListener(event -> returnDate = event.getValue());
        dates.add(dDate);
        dates.add(rDate);
        return dates;
    }

    public ComboBox<ProductDto> getComboBoxProduct() {
        productComboBox.setLabel("Выберите товар");
        productComboBox.setItems(productService.getAll());
        productComboBox.setItemLabelGenerator(ProductDto::getName);
        productComboBox.addValueChangeListener(event -> productId = event.getValue().getId());
        return productComboBox;
    }

    public ComboBox<String> getComboBoxAvailable() {
        availableComboBox.setLabel("Доступно");
        availableComboBox.setItems("Любой", "Положительный", "Отрицательный", "Нулевой", "Ненулевой", "Ниже неснижаемого остатка");
        available = availableComboBox.getValue();
        return availableComboBox;
    }

    public ComboBox<String> getComboBoxSold() {
        soldComboBox.setLabel("Проданные товары");
        soldComboBox.setItems("Все", "Только проданные", "Только непроданные");
        sold = soldComboBox.getValue();
        return soldComboBox;
    }

    public ComboBox<String> getComboBoxRemainder() {
        remainderComboBox.setLabel("Остаток");
        remainderComboBox.setItems("Любой", "Положительный", "Отрицательный", "Нулевой", "Ненулевой", "Ниже неснижаемого остатка");
        remainder = remainderComboBox.getValue();
        return remainderComboBox;
    }

    public ComboBox<WarehouseDto> getComboBoxWarehouse() {
        warehouseComboBox.setLabel("Склад");
        warehouseComboBox.setItems(warehouseService.getAll());
        warehouseComboBox.setItemLabelGenerator(WarehouseDto::getName);
        warehouseComboBox.addValueChangeListener(event -> warehouseId = event.getValue().getId());
        return warehouseComboBox;
    }

    public ComboBox<ContractorDto> getComboBoxContractor() {
        contractorComboBox.setLabel("Поставщик");
        contractorComboBox.setItems(contractorService.getAll());
        contractorComboBox.setItemLabelGenerator(ContractorDto::getName);
        contractorComboBox.addValueChangeListener(event -> contractorId = event.getValue().getId());
        return contractorComboBox;
    }

    public ComboBox<CompanyDto> getComboBoxCompany() {
        companyComboBox.setLabel("Организация");
        companyComboBox.setItems(companyService.getAll());
        companyComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyComboBox.addValueChangeListener(event -> companyId = event.getValue().getId());
        return companyComboBox;
    }


    private Grid<PurchaseControlDto> configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

//        grid.addColumn("buttonSetting")         /*смотри в классе PurchaseControlDto*/
//                .setHeader(buttonSettings())
//                .setSortable(false)
//                .setWidth("50px")
//                .setTextAlign(ColumnTextAlign.START)
//                .setFrozen(true);

        grid.addColumn("id")   /*колонка не соответствует оригинальному сайту*/
                .setHeader("№")
                .setWidth("1px")
                .setId("№");

        grid.addColumn(PurchaseControlDto::getProductNameId)
                .setHeader("Наименование")
                .setKey("product_name")
                .setResizable(true)
                .setSortable(true)
                .setId("Товар или группа");

        grid.addColumn(PurchaseControlDto::getProductCode)
                .setHeader("Код")
                .setKey("product_code")
                .setResizable(true)
                .setSortable(true)
                .setId("Код");

        grid.addColumn(PurchaseControlDto::getArticleNumber)
                .setHeader("Артикул")
                .setKey("article_number")
                .setResizable(true)
                .setSortable(true)
                .setId("Артикул");

        grid.addColumn(PurchaseControlDto::getProductMeasure)
                .setHeader("Ед. изм.")
                .setKey("product_measure")
                .setResizable(true)
                .setSortable(true)
                .setId("Единица_измерения");

        grid.addColumn(PurchaseControlDto::getProductQuantity)
                .setHeader("Кол-во")
                .setKey("product_quantity")
                .setTextAlign(ColumnTextAlign.END)
                .setResizable(true)
                .setSortable(true)
                .setId("Количество");

        grid.addColumn(dto -> purchaseHistoryOfSalesService.getById(dto.getHistoryOfSalesId()).getSumOfProducts())
                .setHeader("Сумма")
                .setKey("sum_of_products")
                .setTextAlign(ColumnTextAlign.END)
                .setResizable(true)
                .setSortable(true)
                .setId("Сумма");

        grid.addColumn(dto -> productPriceService.getById(dto.getHistoryOfSalesId()).getValue())
                .setHeader("Себестоимость")
                .setKey("product_price")
                .setTextAlign(ColumnTextAlign.END)
                .setResizable(true)
                .setSortable(true)
                .setId("Себестоимость");

        grid.addColumn(dto -> purchaseHistoryOfSalesService.getById(dto.getHistoryOfSalesId()).getProductMargin())
                .setHeader("Прибыль")
                .setKey("product_margin")
                .setTextAlign(ColumnTextAlign.END)
                .setResizable(true)
                .setSortable(true)
                .setId("Прибыль");

        grid.addColumn(dto -> purchaseHistoryOfSalesService.getById(dto.getHistoryOfSalesId()).getProductProfitMargin())
                .setHeader("Рентабельность")
                .setKey("product_profit_margin")
                .setTextAlign(ColumnTextAlign.END)
                .setResizable(true)
                .setSortable(true)
                .setId("Рентабельность");

        grid.addColumn(dto -> purchaseHistoryOfSalesService.getById(dto.getHistoryOfSalesId()).getProductSalesPerDay())
                .setHeader("Продаж в день")
                .setKey("product_sales_per_day")
                .setTextAlign(ColumnTextAlign.END)
                .setResizable(true)
                .setSortable(true)
                .setId("Продаж в день");

        grid.addColumn(dto -> purchaseCurrentBalanceService.getById(dto.getCurrentBalanceId()).getRestOfTheWarehouse())
                .setHeader("Остаток")
                .setKey("rest_of_the_warehouse")
                .setTextAlign(ColumnTextAlign.END)
                .setResizable(true)
                .setSortable(true)
                .setId("Остаток");

        grid.addColumn(dto -> purchaseCurrentBalanceService.getById(dto.getCurrentBalanceId()).getProductsReserve())
                .setHeader("Резерв")
                .setKey("products_reserve")
                .setTextAlign(ColumnTextAlign.END)
                .setResizable(true)
                .setSortable(true)
                .setId("Резерв товара");

        grid.addColumn(dto -> purchaseCurrentBalanceService.getById(dto.getCurrentBalanceId()).getProductsAwaiting())
                .setHeader("Ожидание")
                .setKey("products_awaiting")
                .setTextAlign(ColumnTextAlign.END)
                .setResizable(true)
                .setSortable(true)
                .setId("Товар в ожидании");

        grid.addColumn(dto -> purchaseCurrentBalanceService.getById(dto.getCurrentBalanceId()).getProductsAvailableForOrder())
                .setHeader("Доступно")
                .setKey("products_available_for_order")
                .setTextAlign(ColumnTextAlign.END)
                .setResizable(true)
                .setSortable(true)
                .setId("Товара доступно для заказа");

        grid.addColumn(dto -> purchaseCurrentBalanceService.getById(dto.getCurrentBalanceId()).getDaysStoreOnTheWarehouse())
                .setHeader("Дней на складе")
                .setKey("days_store_on_the_warehouse")
                .setTextAlign(ColumnTextAlign.END)
                .setResizable(true)
                .setSortable(true)
                .setId("Дни хранения на складе");

        grid.addColumn(dto -> purchaseForecastService.getById(dto.getForecastId()).getReservedDays())
                .setHeader("Дней запаса")
                .setKey("reserved_days")
                .setTextAlign(ColumnTextAlign.END)
                .setResizable(true)
                .setSortable(true)
                .setId("Дней в запасе");

        grid.addColumn(dto -> purchaseForecastService.getById(dto.getForecastId()).getReservedProducts())
                .setHeader("Запас")
                .setKey("reserved_products")
                .setTextAlign(ColumnTextAlign.END)
                .setResizable(true)
                .setSortable(true)
                .setId("Запас товара");

        grid.addColumn(dto -> purchaseForecastService.getById(dto.getForecastId()).getOrdered())
                .setHeader("Заказать")
                .setKey("ordered")
                .setTextAlign(ColumnTextAlign.END)
                .setResizable(true)
                .setSortable(true)
                .setId("Проданные товары");

        HeaderRow groupingHeader2 = grid.prependHeaderRow();

        groupingHeader2.
                join(
                        groupingHeader2.getCell(grid.getColumnByKey("sum_of_products")),
                        groupingHeader2.getCell(grid.getColumnByKey("product_price")),
                        groupingHeader2.getCell(grid.getColumnByKey("product_margin")),
                        groupingHeader2.getCell(grid.getColumnByKey("product_profit_margin")),
                        groupingHeader2.getCell(grid.getColumnByKey("product_sales_per_day"))
                )
                .setComponent(new Label("История продаж"));

        groupingHeader2.
                join(
                        groupingHeader2.getCell(grid.getColumnByKey("rest_of_the_warehouse")),
                        groupingHeader2.getCell(grid.getColumnByKey("products_reserve")),
                        groupingHeader2.getCell(grid.getColumnByKey("products_awaiting")),
                        groupingHeader2.getCell(grid.getColumnByKey("products_available_for_order")),
                        groupingHeader2.getCell(grid.getColumnByKey("days_store_on_the_warehouse"))
                )
                .setComponent(new Label("Текущий остаток"));

        groupingHeader2.
                join(
                        groupingHeader2.getCell(grid.getColumnByKey("reserved_days")),
                        groupingHeader2.getCell(grid.getColumnByKey("reserved_products")),
                        groupingHeader2.getCell(grid.getColumnByKey("ordered"))
                )
                .setComponent(new Label("Прогноз"));


        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        return grid;
    }

    private void configurePaginator() {
        paginator = new GridPaginator<>(grid, purchaseControl, 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);

    }

    private void configureFilter() {
//        //filter.setFieldToDatePicker("date");
//        filter.setFieldToComboBox("product_name", ProductDto::getName, productService.getAll());
//        filter.setFieldToIntegerField("rest_of_the_warehouse");
//        filter.setFieldToIntegerField("products_available_for_order");
//        filter.setFieldToComboBox("ordered", Boolean.TRUE, Boolean.FALSE );
//
//        /**TODO: Исправить методы поиска согласно оригинальному сайту
//        //filter.setFieldToComboBox("rest_of_the_warehouse",
//        //          "Любое", "Положительное", "Отрицательное", "Нулевое", "Ненулевое", "Ниже неснижаемого остатка");
//        //filter.setFieldToComboBox("products_available_for_order",
//        //          "Любое", "Положительное", "Отрицательное", "Нулевое", "Ненулевое", "Ниже неснижаемого остатка");
//        //  filter.setFieldToComboBox("ordered",
//        //          "Все", "Только проданные товары", "Только не проданные");**/
//
//        filter.setVisibleFields(false, "id");
//        filter.setVisibleFields(false, "product_quantity");
//        filter.setVisibleFields(false, "reserved_products");
//        filter.setVisibleFields(false, "product_measure");
//        filter.setVisibleFields(false, "article_number");
//        filter.setVisibleFields(false, "product_code");
//        filter.setVisibleFields(false, "sum_of_products");
//        filter.setVisibleFields(false, "product_price");
//        filter.setVisibleFields(false, "product_margin");
//        filter.setVisibleFields(false, "product_profit_margin");
//        filter.setVisibleFields(false, "product_sales_per_day");
//        filter.setVisibleFields(false, "products_reserve");
//        filter.setVisibleFields(false, "products_awaiting");
//        filter.setVisibleFields(false, "days_store_on_the_warehouse");
//        filter.setVisibleFields(false, "reserved_days");
//
//        filter.onSearchClick(e -> paginator.setData(purchaseControlService.searchByFilter(filter.getFilterData())));
//        filter.onClearClick(e -> paginator.setData(purchaseControlService.getAll()));
    }

    private Label title() {
        Label label = new Label("Управление закупками");
        label.getStyle().set("font-weight", "bold").set("font-size", "22px").set("margin-bottom", "5px");
        return label;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        return label;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        buttonRefresh.addClickListener(e -> updateList());
        return buttonRefresh;
    }

//    private Button buttonUnit() {
//        Button buttonUnit = new Button("Счёт", new Icon(VaadinIcon.PLUS_CIRCLE));
//        buttonUnit.addClickListener(e -> modalView.open());
//        return buttonUnit;
//    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> toolbarWithFilters.setVisible(!toolbarWithFilters.isVisible()));
        return buttonFilter;
    }

//    private TextField filterTextField() {
//        textField.setPlaceholder("Номер или комментарий");
//        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
//        textField.setValueChangeMode(ValueChangeMode.EAGER);
//        textField.setClearButtonVisible(true);
//        textField.addValueChangeListener(e -> updateList(textField.getValue()));
//        textField.setWidth("300px");
//        return textField;
//    }

    public void updateList(String nameFilter) {
        if (!(textField.getValue().equals(""))) {

        } else {

        }
    }

    private String formatDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime formatDateTime = LocalDateTime.parse(stringDate);
        return formatDateTime.format(formatter);
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("75px");
        numberField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        return numberField;
    }

    private MenuBar orderSupplier() {
        //Text selected = new Text("");
        //ComponentEventListener<ClickEvent<MenuItem>> listener = e -> selected.setText(e.getSource().getText());
        MenuBar menuBar = new MenuBar();

        MenuItem supplierOrder = MenuBarIcon.createIconItem(menuBar, VaadinIcon.PLUS_CIRCLE, "Заказ поставщику", null);
        SubMenu orderSub = supplierOrder.getSubMenu();

        //orderSub.addItem("Общий", listener);
        //orderSub.addItem("Разбить по поставщикам", listener);

        supplierOrder.addClickListener(event -> {
                purchasesSubPurchasingManagementModalWindow.resetView();
                purchasesSubPurchasingManagementModalWindow.setUpdateState(false);
                purchasesSubPurchasingManagementModalWindow.setType("EXPENSE");
                purchasesSubPurchasingManagementModalWindow.setLocation(PURCHASES);
                supplierOrder.getUI().ifPresent(ui -> ui.navigate(PURCHASES_PURCHASES__NEW_ORDER_PURCHASES));
        });
        return menuBar;
    }

//    private MenuBar valuePrint() {
//        MenuBar menuBar = new MenuBar();
//
//        MenuItem supplierOrder = MenuBarIcon.createIconItem(menuBar, VaadinIcon.PRINT, "Печать", null);
//        SubMenu orderSub = supplierOrder.getSubMenu();
//        orderSub.addItem("Управление закупками");    /*Заглушка: требует реализации*/
//        orderSub.addItem("Настроить...");            /*Заглушка: требует реализации*/
//        return menuBar;
//    }

    private Dialog getProcurementManagementDialog() {
        Dialog dialog = new Dialog();
        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        Checkbox checkbox = new Checkbox();
        Label rememberChoice = new Label("Запомнить выбор");
        Label creatingPrintedForm = new Label("Создание печатной формы");
        Label creatingFormTemplate = new Label("Создать печатную форму по шаблону 'Управление закупками'?");
        ComboBox<String> comboBox = new ComboBox<>();
        Button yes = new Button("Да");
        Button no = new Button("Нет", e -> dialog.close());
        Label empty = new Label("    ");

        comboBox.setWidth("300px");
        comboBox.setItems("Открыть в браузере", "Скачать в формате Excel", "Скачать в формате PDF", "Скачать в формате Open Office Calc");
        comboBox.setValue("Открыть в браузере");
        creatingPrintedForm.getStyle().set("font-weight", "bold").set("font-size", "22px");
        comboBox.setAllowCustomValue(true);
        horizontalLayout1.setWidth("97%");
        horizontalLayout1.setAlignItems(Alignment.END);
        add(comboBox);
        horizontalLayout1.add(yes, no);
        horizontalLayout.add(checkbox, rememberChoice);
        verticalLayout.add(creatingPrintedForm, creatingFormTemplate, comboBox, horizontalLayout, empty, horizontalLayout1);
        dialog.add(verticalLayout);

        return dialog;
    }

    private Dialog getSettingDialog() {
        Dialog dialoh = new Dialog();
        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Icon icon = new Icon((VaadinIcon.QUESTION_CIRCLE_O));
        Button button = new Button(" Добавить шаблон");
        Label configureTemplateLabel = new Label("Настройка шаблонов");
        Label procurementManagementLabel = new Label("Управление закупками");

        button.addClickListener(e -> addTemplate().open());
        configureTemplateLabel.getStyle().set("font-size", "22px");
        procurementManagementLabel.getStyle().set("font-weight", "bold");
        dialoh.setHeight("99%");
        dialoh.setWidth("33%");
        verticalLayout.add(horizontalLayout, procurementManagementLabel, createTemplateGrid(), button);
        horizontalLayout.add(icon, configureTemplateLabel);
        dialoh.setCloseOnEsc(true);
        dialoh.setCloseOnOutsideClick(true);
        dialoh.add(verticalLayout);


        return dialoh;
    }

    private Dialog addTemplate() {
        Dialog dialog = new Dialog();
        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label limitation = new Label("Ограничение подписки");
        Label tariffPlan = new Label("Ваш тарифный план не позволяет загружать собственные шаблоны");
        Label empty = new Label("  ");
        Button changeSubscription = new Button("Изменить подписку");
        Button close = new Button("Закрыть", e -> dialog.close());

        changeSubscription.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        limitation.getStyle().set("font-weight", "bold").set("font-size", "22px");
        horizontalLayout.add(changeSubscription, close);
        verticalLayout.add(limitation, tariffPlan, empty, horizontalLayout);
        dialog.add(verticalLayout);
        return dialog;
    }

    private Grid<TemplateDto> createTemplateGrid() {
        Grid<TemplateDto> gridTemplate = new Grid<>();
        Button button = new Button("Авто");
        gridTemplate.addColumn(new ComponentRenderer<>(templateDto -> new Checkbox()))
                .setHeader("Видимость")
                .setWidth("5px");
        gridTemplate.addColumn(TemplateDto::getNameOfProduct)
                .setHeader("Наименование")
                .setWidth("100px");
        gridTemplate.addColumn(new ComponentRenderer<>(templateDto -> print()))
                .setWidth("20px");
        gridTemplate.addColumn(new ComponentRenderer<>(templateDto -> button))
                .setWidth("10px");
        gridTemplate.setItems(new TemplateDto(1L, "Управление закупками"));
        button.addClickListener(e -> purchasingManagementDialog().open());
//        downloadButton.addClickListener(e -> getLinkToXlsTemplate(getXlsFiles()));
        return gridTemplate;
    }

    private Button print() {
        Button button = new Button("Скачать");
//        button.addClickListener(e -> getLinkToXlsTemplate(getXlsFiles()));
        return button;
    }


    private Dialog purchasingManagementDialog() {
        Dialog dialog = new Dialog();
        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        HorizontalLayout horizontalLayout3 = new HorizontalLayout();
        Label label = new Label("Управление закупками");
        Label label2 = new Label("Автоматически формировать отчет");
        Label label3 = new Label("Время формирования:");
        Label label4 = new Label("Email получателя:");
        Checkbox checkbox = new Checkbox();
        TextField center = new TextField();
        IntegerField integerField = new IntegerField();
        IntegerField integerField2 = new IntegerField();
        EmailField emailField = new EmailField();
        Button templateCustomization = new Button("<- Настройка шаблонов", e -> dialog.close());
        Span confirmed = new Span("\t\n" +
                "Максимальное количество строк в документе — 65435 " +
                " Если документ содержит больше строк, добавьте в" +
                " шаблон печатной формы фильтры. Создать такой" +
                " шаблон помогут в службе поддержки.");

        confirmed.getStyle().set("font-size", "12px");
        confirmed.getElement().getThemeList().add("badge success");
        add(confirmed);
        dialog.setHeight("99%");
        dialog.setWidth("33%");
        label.getStyle().set("font-size", "22px");
        integerField.setMin(0);
        integerField.setMax(23);
        integerField.setValue(0);
        integerField.setHasControls(true);
        add(integerField);
        integerField2.setMin(0);
        integerField2.setMax(23);
        integerField2.setValue(0);
        integerField2.setHasControls(true);
        add(integerField2);
        center.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        horizontalLayout.add(checkbox, label2);
        horizontalLayout2.add(label3, integerField, integerField2);
        horizontalLayout3.add(label4, emailField);
        verticalLayout.add(templateCustomization, label, horizontalLayout, horizontalLayout2, horizontalLayout3, confirmed);
        dialog.add(verticalLayout);
        return dialog;
    }

    private void configureSelectXlsTemplateButton() {
        SubMenu printSubMenu = print.getSubMenu();
        printSubMenu.removeAll();
        templatesXlsMenuItems(printSubMenu);
        uploadXlsMenuItem(printSubMenu);
    }

    private void templatesXlsMenuItems(SubMenu subMenu) {
        getXlsFiles().forEach(x -> subMenu.addItem(getLinkToHtmlTemplate(x)));
        getXlsFiles().forEach(x -> subMenu.addItem(getLinkToXlsTemplate(x)));
        getXlsFiles().forEach(x -> subMenu.addItem(getLinkToPdfTemplate(x)));
        getXlsFiles().forEach(x -> subMenu.addItem(getLinkToOdsTemplate(x)));
    }

    private void uploadXlsMenuItem(SubMenu subMenu) {
        MenuItem menuItem = subMenu.addItem("Добавить шаблон");
        Dialog dialog = new Dialog();
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        configureUploadFinishedListener(upload, buffer, dialog, print1);
        dialog.add(upload);
        menuItem.addClickListener(x -> dialog.open());
    }

    private void configureUploadFinishedListener(Upload upload, MemoryBuffer buffer, Dialog dialog, Select<String> print1) {
        upload.addFinishedListener(event -> {
            if (getXlsFiles().stream().map(File::getName).anyMatch(x -> x.equals(event.getFileName()))) {
                getErrorNotification("Файл с таким именем уже существует");
            } else {
                File exelTemplate = new File(pathForSaveXlsTemplate + event.getFileName());
                try (FileOutputStream fos = new FileOutputStream(exelTemplate)) {
                    fos.write(buffer.getInputStream().readAllBytes());
                    configureSelectXlsTemplateButton();
                    getInfoNotification("Файл успешно загружен");
                    log.info("Excel шаблон успешно загружен");
                } catch (IOException e) {
                    getErrorNotification("При загрузке шаблона произошла ошибка");
                    log.error("При загрузке Excel шаблона произошла ошибка");
                }
                dialog.close();
            }
        });
    }

    private List<File> getXlsFiles() {
        File dir = new File(pathForSaveXlsTemplate);
        return Arrays.stream(Objects.requireNonNull(dir.listFiles())).filter(File::isFile).filter(x -> x.getName()
                .contains(".xls")).collect(Collectors.toList());
    }

    private Anchor getLinkToXlsTemplate(File file) {
        String templateName = file.getName();
        List<PurchaseControlDto> products = purchaseControlService.getAll();
        PrintPurchasingManagementXls printPurchasingManagementXls = new PrintPurchasingManagementXls(file.getPath(),
                products,
                employeeService,
                productPriceService,
                purchaseHistoryOfSalesService,
                productService
        );
        return new Anchor(new StreamResource(templateName, printPurchasingManagementXls::createReport), "Печать в формате Excel: " + templateName);
    }

    private Anchor getLinkToPdfTemplate(File file) {
        String templateName = file.getName().substring(0, file.getName().lastIndexOf(".")) + ".pdf";
        List<PurchaseControlDto> products = purchaseControlService.getAll();
        PrintPurchasingManagementXls printPurchasingManagementXls = new PrintPurchasingManagementXls(file.getPath(),
                products,
                employeeService,
                productPriceService,
                purchaseHistoryOfSalesService,
                productService
        );
        return new Anchor(new StreamResource(templateName, printPurchasingManagementXls::createReportPDF), "Печать в формате PDF: " + templateName);
    }

    private Anchor getLinkToOdsTemplate(File file) {
        String templateName = file.getName().substring(0, file.getName().lastIndexOf(".")) + ".ods";
        List<PurchaseControlDto> products = purchaseControlService.getAll();
        PrintPurchasingManagementXls printPurchasingManagementXls = new PrintPurchasingManagementXls(file.getPath(),
                products,
                employeeService,
                productPriceService,
                purchaseHistoryOfSalesService,
                productService
        );
        return new Anchor(new StreamResource(templateName, printPurchasingManagementXls::createReportODS), "Печать в формате Office Calc: " + templateName);
    }

    private Anchor getLinkToHtmlTemplate(File file) {
        String templateName = file.getName().substring(0, file.getName().lastIndexOf(".")) + ".html";
        List<PurchaseControlDto> products = purchaseControlService.getAll();
        PrintPurchasingManagementXls printPurchasingManagementXls = new PrintPurchasingManagementXls(file.getPath(),
                products,
                employeeService,
                productPriceService,
                purchaseHistoryOfSalesService,
                productService
        );
        return new Anchor(new StreamResource(templateName, printPurchasingManagementXls::createReportHTML), "Открыть в браузере: " + templateName);
    }

    private void getInfoNotification(String message) {
        Notification notification = new Notification(message, 5000);
        notification.open();
    }

    private void getErrorNotification(String message) {
        Div content = new Div();
        content.addClassName("my-style");
        content.setText(message);
        Notification notification = new Notification(content);
        notification.setDuration(5000);
        String styles = ".my-style { color: red; }";
        StreamRegistration resource = UI.getCurrent().getSession()
                .getResourceRegistry()
                .registerResource(new StreamResource("styles.css", () ->
                        new ByteArrayInputStream(styles.getBytes(StandardCharsets.UTF_8))));
        UI.getCurrent().getPage().addStyleSheet(
                "base://" + resource.getResourceUri().toString());
        notification.open();
    }

    private Button buttonSettings() {
        Button button = new Button(new Icon(VaadinIcon.COG));
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        button.addClickListener(enotEvent -> {
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.setTarget(button);
            contextMenu.setOpenOnClick(true);
            List<Grid.Column<PurchaseControlDto>> stringList = grid.getColumns();
            CheckboxGroup<Grid.Column<PurchaseControlDto>> checkboxGroup = new CheckboxGroup<>();
            checkboxGroup.addThemeVariants(CheckboxGroupVariant.MATERIAL_VERTICAL);
            checkboxGroup.setItems(stringList);
            checkboxGroup.setItemLabelGenerator(column -> column.getId().orElse(""));
            contextMenu.add(checkboxGroup);
            checkboxGroup.setValue(stringList.stream().filter(Component::isVisible).collect(Collectors.toSet()));
            checkboxGroup.addSelectionListener(selection -> {
                selection.getAddedSelection().forEach(i -> i.setVisible(true));
                selection.getRemovedSelection().forEach(i -> i.setVisible(false));
            });
        });
        return button;
    }

    private Component getIsCheckedIcon(SupplierAccountDto supplierAccountDto) {
        if (supplierAccountDto.getIsSpend()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private void updateList() {
        grid.setItems(purchaseControlService.getAll());
    }

    public void deleteSelectedInvoices() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (PurchaseControlDto supp : grid.getSelectedItems()) {
                purchaseControlService.deleteById(supp.getId());
                notifications.infoNotification("");
            }
        } else {
            notifications.errorNotification("");
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}