package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.SupplierAccountDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.InvoiceProductService;
import com.trade_accounting.services.interfaces.SupplierAccountService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
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
import java.util.Map;

@Slf4j
@Route(value = "invoicesToBuyers", layout = AppView.class)
@PageTitle("Счета покупателям")
@SpringComponent
@UIScope
public class SalesSubInvoicesToBuyersView extends VerticalLayout {

    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final ContractorService contractorService;
    private final SupplierAccountService supplierAccountService;
    private final InvoiceProductService invoiceProductService;

    private final SalesAddNewInvoicesToBuyersView salesAddNewInvoicesToBuyersView;

    private final List<SupplierAccountDto> data;

    private final Notifications notifications;

    private HorizontalLayout actions;
    private Grid<SupplierAccountDto> grid = new Grid<>(SupplierAccountDto.class, false);
    private GridPaginator<SupplierAccountDto> paginator;
    private final GridFilter<SupplierAccountDto> filter;

    private final String typeOfInvoice = "RECEIPT";

    private  final String textForQuestionButton = "<div><p>Счет является основанием для получения оплаты и проведения отгрузки.</p>" +
            "<p>Счет можно создать на основе заказа покупателя или вручную." +
            "Несколько счетов можно объединить: отметьте нужные счета флажками, нажмите справа вверху Изменить → Объединить." +
            "Исходные счета также сохранятся.</p>" +
            "<p>Счета можно распечатывать или отправлять покупателям в виде файлов.</p>"+
            "<p>Читать инструкцию: <a href=\"#\" target=\"_blank\">Счета покупателям</a></p></div>";

    @Autowired
    public SalesSubInvoicesToBuyersView(CompanyService companyService, WarehouseService warehouseService,
                                        ContractorService contractorService,
                                        InvoiceProductService invoiceProductService,
                                        @Lazy Notifications notifications,
                                        @Lazy SalesAddNewInvoicesToBuyersView salesAddNewInvoicesToBuyersView,
                                        SupplierAccountService supplierAccountService) {
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.contractorService = contractorService;
        this.supplierAccountService = supplierAccountService;
        this.invoiceProductService = invoiceProductService;
        this.salesAddNewInvoicesToBuyersView = salesAddNewInvoicesToBuyersView;
        this.notifications = notifications;
        this.data = getData();

        configureActions();
        configureGrid();
        configurePaginator();

        this.filter = new GridFilter<>(grid);
        configureFilter();

        add(actions, filter, grid, paginator);
    }

    private void configureActions() {
        actions = new HorizontalLayout();
        actions.add(Buttons.buttonQuestion(textForQuestionButton, "350px"), title(), buttonRefresh(), buttonUnit(), buttonFilter(), textField(),
                numberField(), valueSelect(), valueStatus(), valueCreate(), valuePrint(), buttonSettings());
        actions.setDefaultVerticalComponentAlignment(Alignment.CENTER);
    }

    private void configureGrid() {
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(dto -> formatDate(dto.getDate())).setHeader("Время")
                .setKey("date").setId("Дата");
        grid.addColumn(dto -> companyService.getById(dto.getCompanyId()).getName()).setHeader("Компания")
                .setKey("companyId").setId("Компания");
        grid.addColumn(dto -> contractorService.getById(dto.getContractorId()).getName()).setHeader("Контрагент")
                .setKey("contractorId").setId("Контрагент");
        grid.addColumn(dto -> warehouseService.getById(dto.getWarehouseId()).getName()).setHeader("Со склада")
                .setKey("warehouseId").setId("Со склада");
        grid.addColumn(dto -> getTotalPrice(dto.getId())).setHeader("Сумма");
        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addItemDoubleClickListener(event -> {
            SupplierAccountDto editInvoice = event.getItem();
            salesAddNewInvoicesToBuyersView.setSupplierDataForEdit(editInvoice);
            salesAddNewInvoicesToBuyersView.setUpdateState(true);
            salesAddNewInvoicesToBuyersView.setLocation("sells");
            UI.getCurrent().navigate("sells/add-new-invoices-to-buyers");
        });
    }

    private void configurePaginator() {
        paginator = new GridPaginator<>(grid, data, 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.onSearchClick(e -> {
            Map<String, String> map = filter.getFilterData();
            map.put("typeOfInvoice", typeOfInvoice);
            paginator.setData(supplierAccountService.searchByFilter(map));
        });
        filter.onClearClick(e -> paginator.setData(supplierAccountService.getAll(typeOfInvoice)));
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonUnit() {
        Button buttonUnit = new Button("Счет", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickListener(event -> {
            salesAddNewInvoicesToBuyersView.resetView();
            salesAddNewInvoicesToBuyersView.setUpdateState(false);
            salesAddNewInvoicesToBuyersView.setLocation("sells");
            buttonUnit.getUI().ifPresent(ui -> ui.navigate("sells/add-new-invoices-to-buyers"));
        });
        return buttonUnit;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private TextField textField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("Номер, склад или компания");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> updateList(textField.getValue()));
        return textField;
    }

    private void updateList(String text) {
        grid.setItems(supplierAccountService.getAll());
        grid.setItems(supplierAccountService.findBySearchAndTypeOfInvoice(text, typeOfInvoice));
    }

    private H4 title() {
        H4 title = new H4("Счета покупателям");
        title.setHeight("2.2em");
        title.setWidth("80px");
        return title;
    }

    private void deleteSelectedInvoices() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (SupplierAccountDto supplierAccountDto : grid.getSelectedItems()) {
                supplierAccountService.deleteById(supplierAccountDto.getId());
                notifications.infoNotification("Выбранные счета успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные счета");
        }
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
                deleteSelectedInvoices();
                grid.deselectAll();
                select.setValue("Изменить");
                paginator.setData(getData());
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

    private List<SupplierAccountDto> getData() {
        return supplierAccountService.getAll(typeOfInvoice);
    }


    private String getTotalPrice(Long id) {
        var totalPrice = invoiceProductService.getByInvoiceId(id).stream()
                .map(ipdto -> ipdto.getPrice().multiply(ipdto.getAmount()))
                .reduce(BigDecimal.valueOf(0.0), BigDecimal::add);
        return String.format("%.2f", totalPrice);
    }

    private static String formatDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime formatDateTime = LocalDateTime.parse(date);
        return formatDateTime.format(formatter);

    }
}
