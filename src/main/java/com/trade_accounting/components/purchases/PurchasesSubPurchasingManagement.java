package com.trade_accounting.components.purchases;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.purchases.print.PrintPurchasingManagementXls;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.controllers.dto.PurchaseControlDto;
import com.trade_accounting.controllers.dto.SupplierAccountDto;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.ProductPriceService;
import com.trade_accounting.services.interfaces.PurchaseControlService;
import com.trade_accounting.services.interfaces.PurchaseHistoryOfSalesService;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
import com.vaadin.flow.data.value.ValueChangeMode;
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
import java.util.ArrayList;
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
                                            PurchaseHistoryOfSalesService purchaseHistoryOfSalesService
    ) {
        this.employeeService = employeeService;
        this.purchaseControlService = purchaseControlService;
        this.notifications = notifications;
        this.modalView = modalView;
        this.productPriceService = productPriceService;
        this.purchaseHistoryOfSalesService = purchaseHistoryOfSalesService;

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
        actions.add(buttonQuestion(), title(), buttonRefresh(), buttonFilter(), filterTextField(),
                numberField(), valueSelect(), valueStatus(), valueCreate(), valuePrint(), buttonSettings());
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

        grid.addColumn("id").setWidth("20px").setHeader("№").setId("№");
        grid.addColumn(PurchaseControlDto::getProductName).setHeader("Наименование").setKey("product_name").setSortable(true)
                .setId("Наименование");
        grid.addColumn(PurchaseControlDto::getArticleNumber).setHeader("Артикул").setKey("article_number").setSortable(true)
                .setId("Артикул");
        grid.addColumn(PurchaseControlDto::getProductMeasure).setHeader("Единица измерения").setKey("product_measure").setSortable(true)
                .setId("Единица_измерения");
        grid.addColumn(PurchaseControlDto::getProductQuantity).setHeader("Количество").setKey("product_quantity").setSortable(true)
                .setId("Количество");

        grid.addColumn(dto -> purchaseHistoryOfSalesService.getById(dto.getHistoryOfSalesId()).getSumOfProducts())
                .setHeader("Сумма").setKey("sum_of_products").setId("Сумма");
        grid.addColumn(dto -> productPriceService.getById(dto.getHistoryOfSalesId()).getValue())
                .setHeader("Себестоимость").setKey("product_price").setId("Себестоимость");
        grid.addColumn(dto -> purchaseHistoryOfSalesService.getById(dto.getHistoryOfSalesId()).getProductMargin())
                .setHeader("Прибыль").setKey("product_margin").setId("Прибыль");
        grid.addColumn(dto -> purchaseHistoryOfSalesService.getById(dto.getHistoryOfSalesId()).getProductProfitMargin())
                .setHeader("Рентабельность").setKey("product_profit_margin").setId("Рентабельность");
        grid.addColumn(dto -> purchaseHistoryOfSalesService.getById(dto.getHistoryOfSalesId()).getProductSalesPerDay())
                .setHeader("Продаж в день").setKey("product_sales_per_day").setId("Продаж в день");

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

        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

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

    private H4 title() {
        H4 title = new H4("Управление закупками");
        title.setHeight("2.2em");
        title.setWidth("100px");
        return title;
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

    private TextField filterTextField() {
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addValueChangeListener(e -> updateList(textField.getValue()));
        textField.setWidth("300px");
        return textField;
    }

    public void updateList(String nameFilter) {
        if (!(textField.getValue().equals(""))) {

        } else {

        }
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect() {
        Select<String> select = new Select<>();
        List<String> stringList = new ArrayList<>();
        stringList.add("Изменить");
        stringList.add("Удалить");
        select.setItems(stringList);
        select.setValue("Изменить");
        select.setWidth("130px");
        select.addValueChangeListener(event -> {
            if (select.getValue().equals("Удалить")) {
                deleteSelectedInvoices();
                grid.deselectAll();
                select.setValue("Изменить");
                paginator.setData(loadSupplierAccounts());
            }
        });
        return select;
    }

    private Select<String> valueStatus() {
        Select<String> status = new Select<>();
        status.setItems("Статус");
        status.setValue("Статус");
        status.setWidth("130px");
        return status;
    }

    private Select<String> valueCreate() {
        Select<String> create = new Select<>();
        create.setItems("Создать");
        create.setValue("Создать");
        create.setWidth("130px");
        return create;
    }

    private Select<String> valuePrint() {
        Select<String> print = new Select<>();
        print.setItems("Печать", "Добавить");
        print.setValue("Печать");
        getXlsFiles().forEach(x -> print.add(getLinkToXlsTemplate(x)));
        uploadXlsMenuItem(print);
        print.setWidth("130px");
        return print;
    }

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
        PrintPurchasingManagementXls printPurchasingManagementXls = new PrintPurchasingManagementXls(file.getPath(), purchaseControlService.getAll(), employeeService,productPriceService,purchaseHistoryOfSalesService);
        return new Anchor(new StreamResource(templateName, printPurchasingManagementXls::createReport), templateName);
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
        return new Button(new Icon(VaadinIcon.COG_O));
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




