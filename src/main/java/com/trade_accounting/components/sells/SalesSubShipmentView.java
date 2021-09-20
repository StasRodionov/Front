package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.InvoiceProductService;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

@Slf4j
@Route(value = "shipment", layout = AppView.class)
@PageTitle("Отгрузки")
@SpringComponent
@UIScope
public class SalesSubShipmentView extends VerticalLayout {

    private final WarehouseService warehouseService;
    private final InvoiceService invoiceService;
    private final ContractorService contractorService;
    private final CompanyService companyService;
    private final SalesEditShipmentView salesEditShipmentView;
    private final List<InvoiceDto> data;
    private final InvoiceProductService invoiceProductService;
    private HorizontalLayout actions;
    private Grid<InvoiceDto> grid;
    private GridPaginator<InvoiceDto> paginator;

    private final String typeOfInvoice = "RECEIPT";

    @Autowired
    public SalesSubShipmentView(WarehouseService warehouseService, InvoiceService invoiceService,
                                ContractorService contractorService,
                                CompanyService companyService, SalesEditShipmentView salesEditShipmentView, InvoiceProductService invoiceProductService) {
        this.warehouseService = warehouseService;
        this.invoiceService = invoiceService;
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.salesEditShipmentView = salesEditShipmentView;
        this.invoiceProductService = invoiceProductService;
        this.data = getData();

        configureActions();
        configureGrid();
        configurePaginator();

        add(actions, grid, paginator);
    }

    private void configureActions() {
        actions = new HorizontalLayout();
        actions.add(buttonQuestion(), title(), buttonRefresh(), buttonUnit(), buttonFilter(), textField(),
                numberField(), valueSelect(), valueStatus(), valueCreate(), valuePrint(), buttonSettings());
        actions.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    }

    private void configureGrid() {
        grid = new Grid<>(InvoiceDto.class, false);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(dto -> formatDate(dto.getDate())).setHeader("Время")
                .setKey("date").setId("Дата");
        grid.addColumn(dto -> warehouseService.getById(dto.getWarehouseId()).getName()).setHeader("Со склада")
                .setKey("typeOfInvoiceDTO").setId("Склад");
        grid.addColumn(dto -> contractorService.getById(dto.getContractorId()).getName()).setHeader("Контрагент")
                .setKey("contractorId").setId("Контрагент");
        grid.addColumn(dto -> companyService.getById(dto.getCompanyId()).getName()).setHeader("Организация")
                .setKey("companyId").setId("Компания");
        grid.addColumn(dto -> getTotalPrice(dto.getId())).setHeader("Сумма");
        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

    }

    private Component getIsCheckedIcon(InvoiceDto invoiceDto) {
        if (invoiceDto.getIsSpend()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private void configurePaginator() {
        paginator = new GridPaginator<>(grid, data, 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonUnit() {
        Button buttonUnit = new Button("Отгрузка", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickListener(event -> {
            salesEditShipmentView.resetView();
            salesEditShipmentView.setType("RECEIPT");
            salesEditShipmentView.setLocation("sells");
            buttonUnit.getUI().ifPresent(ui -> ui.navigate("sells/shipment-edit"));
        });
        return buttonUnit;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        return buttonFilter;
    }

    private static String formatDate(String date) {
        return LocalDateTime.parse(date)
                .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }

    private Button buttonSettings() {
        Button buttonSettings = new Button(new Icon(VaadinIcon.COG_O));
        return buttonSettings;
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private TextField textField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        return textField;
    }

    private H2 title() {
        H2 title = new H2("Отгрузки");
        title.setHeight("2.2em");
        return title;
    }

    private Select<String> valueSelect() {
        Select<String> select = new Select<>();
        select.setItems("Изменить");
        select.setValue("Изменить");
        select.setWidth("130px");
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

    private String getTotalPrice(Long id) {
        var totalPrice = invoiceProductService.getByInvoiceId(id).stream()
                .map(ipdto -> ipdto.getPrice().multiply(ipdto.getAmount()))
                .reduce(BigDecimal.valueOf(0.0), BigDecimal::add);
        return String.format("%.2f", totalPrice);
    }

    private void updateList() {
        grid.setItems(invoiceService.getAll(typeOfInvoice));
        System.out.println("Обновлен");
    }

    private List<InvoiceDto> getData() {
        return invoiceService.getAll(typeOfInvoice);
    }
}
