package com.trade_accounting.components.purchases;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.SupplierAccountDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.InvoiceProductService;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.SupplierAccountService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Route(value = "purchasingManagement", layout = AppView.class)
@PageTitle("Управление закупками")
@SpringComponent
@UIScope
public class PurchasesSubPurchasingManagement extends VerticalLayout implements AfterNavigationObserver {


    private final SupplierAccountService supplierAccountService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final ContractorService contractorService;
    private final ContractService contractService;
    private final Notifications notifications;
    private final SupplierAccountModalView modalView;
    private final TextField textField = new TextField();
    private PurchasesChooseGoodsModalWin purchasesChooseGoodsModalWin;
    private final ProductService productService;
    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;

    private List<SupplierAccountDto> supplierAccount;

    private HorizontalLayout actions;
    private final Grid<SupplierAccountDto> grid = new Grid<>(SupplierAccountDto.class, false);
    private GridPaginator<SupplierAccountDto> paginator;
    private final GridFilter<SupplierAccountDto> filter;

    private final String textForQuestionButton = "<div><p>Счета поставщиков помогают планировать оплату товаров." +
            "Счета не меняют количество товара на складе — для этого нужно создать приемку, а чтобы учесть оплату — платеж.</p>" +
            "<p>Дату оплаты можно запланировать. Не оплаченные вовремя счета отображаются в разделе Показатели.</p>" +
            "<p>Счета можно создавать сразу из заказа поставщику.</p>"+
            "<p>Читать инструкцию: <a href=\"#\" target=\"_blank\">Счета поставщиков</a></p></div>";

    @Autowired
    public PurchasesSubPurchasingManagement(SupplierAccountService supplierAccountService,
                                      WarehouseService warehouseService, CompanyService companyService,
                                      ContractorService contractorService, ContractService contractService,
                                      @Lazy Notifications notifications,
                                      SupplierAccountModalView modalView,
                                      ProductService productService,
                                      InvoiceService invoiceService,
                                      InvoiceProductService invoiceProductService) {
        this.supplierAccountService = supplierAccountService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.contractService = contractService;
        this.notifications = notifications;
        this.modalView = modalView;
        this.productService = productService;
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        loadSupplierAccounts();
        configureActions();
        configureGrid();
        configurePaginator();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        add(actions, filter, grid, paginator);
    }

    private List<SupplierAccountDto> loadSupplierAccounts() {
        supplierAccount = supplierAccountService.getAll();
        return supplierAccount;
    }

    private void configureActions() {
        actions = new HorizontalLayout();
        actions.add(Buttons.buttonQuestion(textForQuestionButton,"300px"), title(), buttonRefresh(), buttonUnit(), buttonFilter(), filterTextField(),
                numberField(), valueSelect(), valueStatus(), valueCreate(), valuePrint(), buttonSettings());
        actions.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    }

    private Grid<SupplierAccountDto> configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn("id").setWidth("20px").setHeader("№").setId("№");
        grid.addColumn(SupplierAccountDto::getDate).setKey("date").setSortable(true)
                .setId("Дата");
        grid.addColumn(e -> contractorService.getById(e.getContractorId()).getName()).setWidth("200px")
                .setKey("contractorId").setId("Контрагент");
        grid.addColumn(iDto -> companyService.getById(iDto.getCompanyId()).getName()).setKey("companyId")
                .setId("Компания");
        grid.addColumn(dto -> warehouseService.getById(dto.getWarehouseId()).getName()).
                setKey("warehouseId").setId("На склад");
        grid.addColumn(this::getTotalPrice).setSortable(true);
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedIcon)).setKey("isSpend")
                .setId("Оплачено");
        grid.addColumn("comment").setId("Комментарий");


         HeaderRow groupingHeader = grid.prependHeaderRow();



        groupingHeader.
                join(
                        groupingHeader.getCell(grid.getColumnByKey("id")),
                        groupingHeader.getCell(grid.getColumnByKey("date")),
                        groupingHeader.getCell(grid.getColumnByKey("contractorId")),
                        groupingHeader.getCell(grid.getColumnByKey("companyId")),
                        groupingHeader.getCell(grid.getColumnByKey("warehouseId")))
                .setComponent(new Label("group 2"));



//        groupingHeader.join(
//                        groupingHeader.getCell(grid.getColumnByKey("id")),
//                        groupingHeader.getCell(grid.getColumnByKey("date")),
//                        groupingHeader.getCell(grid.getColumnByKey("contractorId")),
//                        groupingHeader.getCell(grid.getColumnByKey("companyId")))
//                .setComponent(new Label("group 1")
//                );
//
//        groupingHeader.join(
//                        groupingHeader.getCell(grid.getColumnByKey("warehouseId")),
//                        groupingHeader.getCell(grid.getColumnByKey("sum")),
//                        groupingHeader.getCell(grid.getColumnByKey("isSpend")),
//                        groupingHeader.getCell(grid.getColumnByKey("comment")))
//                .setComponent(new Label("group 2"));




        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addItemDoubleClickListener(event -> {
            SupplierAccountDto editSupplierAccounts = event.getItem();
            SupplierAccountModalView supplierAccountModalView = new SupplierAccountModalView(
                    supplierAccountService,
                    companyService,
                    warehouseService,
                    contractorService,
                    contractService,
                    notifications,
                    purchasesChooseGoodsModalWin,
                    productService,
                    invoiceService,
                    invoiceProductService);
            supplierAccountModalView.setSupplierAccountsForEdit(editSupplierAccounts);
            supplierAccountModalView.open();





        });
        return grid;


    }

//    private void configureHeaderRow() {
//
//    }

    private void configurePaginator() {
        paginator = new GridPaginator<>(grid, supplierAccount, 100);
        //setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.onSearchClick(e -> paginator.setData(supplierAccountService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(supplierAccountService.getAll()));
    }

    private H4 title() {
        H4 title = new H4("Счета поставщиков");
        title.setHeight("2.2em");
        title.setWidth("80px");
        return title;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        buttonRefresh.addClickListener(e -> updateList());
        return buttonRefresh;
    }

    private Button buttonUnit() {
        Button buttonUnit = new Button("Счёт", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickListener(e -> modalView.open());
        return buttonUnit;
    }

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
        if(!(textField.getValue().equals(""))) {
            grid.setItems(supplierAccountService.searchByString(nameFilter));
        } else {
            grid.setItems(supplierAccountService.searchByString("null"));
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
        print.setItems("Печать");
        print.setValue("Печать");
        print.setWidth("130px");
        return print;
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private TextField textField() {
        TextField textField = new TextField("", "1-1 из 1");
        textField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        return textField;
    }

    private String formatDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime formatDateTime = LocalDateTime.parse(stringDate);
        return formatDateTime.format(formatter);
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
        grid.setItems(supplierAccountService.getAll());
    }

    private String getTotalPrice(SupplierAccountDto invoice) {

//        InvoiceProductDto invoiceProductDtoList = supplierAccountsModalView
//                .getListOfInvoiceProductByInvoice(invoice.getInvoiceProductDto());
//        List<InvoiceProductDto> getTotal = new ArrayList<>();
//        getTotal.add(invoiceProductDtoList);
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
//        for (InvoiceProductDto invoiceProductDto : getTotal) {
//            totalPrice = totalPrice.add(invoiceProductDto.getPrice()
//                    .multiply(invoiceProductDto.getAmount()));
//        }
        return String.format("%.2f", totalPrice);
    }

    public void deleteSelectedInvoices() {
        if(!grid.getSelectedItems().isEmpty()) {
            for(SupplierAccountDto supp : grid.getSelectedItems()) {
                supplierAccountService.deleteById(supp.getId());
                notifications.infoNotification("Выбранные счета поставщиков успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные контрагенты");
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}




