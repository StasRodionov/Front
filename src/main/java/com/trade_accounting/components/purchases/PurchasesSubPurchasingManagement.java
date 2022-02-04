package com.trade_accounting.components.purchases;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.purchases.print.PrintPurchasingManagementXls;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.MenuBarIcon;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.PurchaseControlDto;
import com.trade_accounting.models.dto.SupplierAccountDto;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.ProductPriceService;
import com.trade_accounting.services.interfaces.PurchaseControlService;
import com.trade_accounting.services.interfaces.PurchaseCurrentBalanceService;
import com.trade_accounting.services.interfaces.PurchaseForecastService;
import com.trade_accounting.services.interfaces.PurchaseHistoryOfSalesService;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
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
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Route(value = "purchasingManagement", layout = AppView.class)
@PageTitle("Управление закупками")
@SpringComponent
@UIScope
public class PurchasesSubPurchasingManagement extends VerticalLayout implements AfterNavigationObserver {

    private final EmployeeService employeeService;
    private final PurchaseControlService purchaseControlService;
    private final Notifications notifications;
    private final SupplierAccountModalView modalView;
    private final TextField textField = new TextField();
    private final ProductPriceService productPriceService;
    private final PurchaseHistoryOfSalesService purchaseHistoryOfSalesService;
    private final PurchaseCurrentBalanceService purchaseCurrentBalanceService;
    private final PurchaseForecastService purchaseForecastService;


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
                                            PurchaseForecastService purchaseForecastService
    ) {
        this.employeeService = employeeService;
        this.purchaseControlService = purchaseControlService;
        this.notifications = notifications;
        this.modalView = modalView;
        this.productPriceService = productPriceService;
        this.purchaseHistoryOfSalesService = purchaseHistoryOfSalesService;
        this.purchaseCurrentBalanceService = purchaseCurrentBalanceService;
        this.purchaseForecastService = purchaseForecastService;

        loadSupplierAccounts();
        configureActions();
        configureGrid();
        configurePaginator();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        add(actions, filter, grid, paginator);
    }

    private List<PurchaseControlDto> loadSupplierAccounts() {
        purchaseControl = purchaseControlService.getAll();
        return purchaseControl;
    }

    private void configureActions() {
        actions = new HorizontalLayout();
        actions.add(buttonQuestion(), title(), buttonRefresh(), buttonFilter(),valuePrint(), orderSupplier(),
                createLabel("Прогноз на"), numberField(), createLabel("дней"), buttonSettings());
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

        grid.addColumn(PurchaseControlDto::getProductName)
                .setHeader("Наименование")
                .setKey("product_name")
                .setResizable(true)
                .setSortable(true)
                .setId("Наименование");

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
                .setId("Остаток на складе");

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
                .setId("Заказанть");

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
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        return grid;
    }

    private void configurePaginator() {
        paginator = new GridPaginator<>(grid, purchaseControl, 100);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.onSearchClick(e -> paginator.setData(purchaseControlService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(purchaseControlService.getAll()));
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
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
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

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("75px");
        numberField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        return numberField;
    }

    private MenuBar orderSupplier() {
        MenuBar menuBar = new MenuBar();

        MenuItem supplierOrder = MenuBarIcon.createIconItem(menuBar, VaadinIcon.PLUS_CIRCLE, "Заказ поставщику", null);
        SubMenu orderSub = supplierOrder.getSubMenu();
        orderSub.addItem("Общий");                   /*Заглушка: требует реализации*/
        orderSub.addItem("Разбить по поставщикам");  /*Заглушка: требует реализации*/

        return menuBar;
    }

    private MenuBar valuePrint() {
        MenuBar menuBar = new MenuBar();

        MenuItem supplierOrder = MenuBarIcon.createIconItem(menuBar, VaadinIcon.PRINT, "Печать", null);
        SubMenu orderSub = supplierOrder.getSubMenu();
        orderSub.addItem("Управление закупками");    /*Заглушка: требует реализации*/
        orderSub.addItem("Настроить...");            /*Заглушка: требует реализации*/
        return menuBar;
    }

//    private Select<String> valuePrint() {
//        Select<String> print = new Select<>();
//        print.setItems("Печать","Добавить");
//        print.setValue("Печать");
//        getXlsFiles().forEach(x -> print.add(getLinkToXlsTemplate(x)));
//        getXlsFiles().forEach(x -> print.add(getLinkToPDFTemplate(x)));
//        uploadXlsMenuItem(print);
//        print.setWidth("130px");
//        return print;
//    }

    private List<File> getXlsFiles() {
        File dir = new File(pathForSaveXlsTemplate);
        return Arrays.stream(Objects.requireNonNull(dir.listFiles())).filter(File::isFile).filter(x -> x.getName()
                .contains(".xls")).collect(Collectors.toList());
    }

    private void uploadXlsMenuItem(Select<String> print) {
        Dialog dialog = new Dialog();
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        configureUploadFinishedListener(upload, buffer, dialog, print);
        dialog.add(upload);
        print.addValueChangeListener(x -> {
            if (print.getValue().equals("Добавить шаблон")) {
                dialog.open();
            }
        });
    }

    private void configureUploadFinishedListener(Upload upload, MemoryBuffer buffer, Dialog dialog,
                                                 Select<String> print) {
        upload.addFinishedListener(event -> {
            if (getXlsFiles().stream().map(File::getName).anyMatch(x -> x.equals(event.getFileName()))) {
                getErrorNotification("Файл с таким именем уже существует");
            } else {
                File exelTemplate = new File(pathForSaveXlsTemplate + event.getFileName());
                try (FileOutputStream fos = new FileOutputStream(exelTemplate)) {
                    fos.write(buffer.getInputStream().readAllBytes());
                    getInfoNotification("Файл успешно загружен");
                    log.info("xls шаблон успешно загружен");
                    print.removeAll();
                    getXlsFiles().forEach(x -> print.add(getLinkToXlsTemplate(x)));

                } catch (IOException e) {
                    getErrorNotification("При загрузке шаблона произошла ошибка");
                    log.error("при загрузке xls шаблона произошла ошибка");
                }
                print.setValue("Печать");
                dialog.close();
            }
        });
    }

    private Anchor getLinkToXlsTemplate(File file) {
        String templateName = file.getName();
//        List<String> sumList = new ArrayList<>();
//        List<InvoiceReceivedDto> list1 = invoiceReceivedService.getAll();
//        for (InvoiceReceivedDto invoiceDto : list1) {
//            sumList.add(getTotalPrice(invoiceDto));
//        }
        PrintPurchasingManagementXls printPurchasingManagementXls = new PrintPurchasingManagementXls(file.getPath(), purchaseControlService.getAll(), employeeService, productPriceService, purchaseHistoryOfSalesService);
        return new Anchor(new StreamResource(templateName, printPurchasingManagementXls::createReport), templateName);
    }

//    private Anchor getLinkToPDFTemplate(File file) {
//        String templateName = file.getName();
//        PrintPurchasingManagementXls printPurchasingManagementXls = new PrintPurchasingManagementXls(file.getPath(), purchaseControlService.getAll(), employeeService,productPriceService,purchaseHistoryOfSalesService);
//        return new Anchor(new StreamResource("purchases.pdf", printPurchasingManagementXls::createReportPDF), "purchases.pdf");
//    }

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