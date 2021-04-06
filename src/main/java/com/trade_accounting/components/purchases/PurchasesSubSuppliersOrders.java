package com.trade_accounting.components.purchases;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Route(value = "suppliersOrders", layout = AppView.class)
@PageTitle("Заказы поставщикам")
public class PurchasesSubSuppliersOrders extends VerticalLayout {

    private final InvoiceService invoiceService;

    private List<InvoiceDto> invoices;

    private final String typeOfInvoice = "EXPENSE";

    private HorizontalLayout actions;
    private Grid<InvoiceDto> grid = new Grid<>(InvoiceDto .class, false);
    private GridPaginator<InvoiceDto> paginator;
    private final GridFilter<InvoiceDto> filter;

    public PurchasesSubSuppliersOrders(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;

        loadInvoices();
        configureActions();
        configureGrid();
        configurePaginator();
        this.filter = new GridFilter<>(grid);
        configureFilter();

        add(actions, filter, grid, paginator);
    }

    private void loadInvoices() {
        invoices = invoiceService.getAll(typeOfInvoice);
    }

    private void configureActions() {
        actions = new HorizontalLayout();
        actions.add(buttonQuestion(), title(), buttonRefresh(), buttonUnit(), buttonFilter(), filterTextField(),
                numberField(), valueSelect(), valueStatus(), valueCreate(), valuePrint(), buttonSettings());
        actions.setDefaultVerticalComponentAlignment(Alignment.CENTER);
    }

    private Grid<InvoiceDto> configureGrid() {
        grid.addColumn("id").setHeader("ID").setId("ID");
        grid.addColumn("date").setHeader("Дата").setId("Дата");
        grid.addColumn("typeOfInvoice").setHeader("Счет-фактура").setId("Счет-фактура");
        grid.addColumn("spend").setHeader("Проведена").setId("Проведена");
        grid.addColumn(iDto -> iDto.getCompanyDto().getName()).setHeader("Компания").setKey("companyDto").setId("Компания");
        grid.addColumn(iDto -> iDto.getContractorDto().getName()).setHeader("Контрагент").setKey("contractorDto").setId("Контрагент");
        grid.addColumn(iDto -> iDto.getWarehouseDto().getName()).setHeader("Склад").setKey("warehouseDto").setId("Склад");

        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        return grid;
    }

    private void configurePaginator() {
        paginator = new GridPaginator<>(grid, invoices, 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("spend", Boolean.TRUE, Boolean.FALSE);
        filter.onSearchClick(e -> paginator.setData(invoiceService.search(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(invoiceService.getAll(typeOfInvoice)));
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private H2 title() {
        H2 title = new H2("Заказы поставщикам");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonUnit() {
        Button buttonUnit = new Button("Заказ", new Icon(VaadinIcon.PLUS_CIRCLE));
        return buttonUnit;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private TextField filterTextField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        return textField;
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
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

    private Button buttonSettings() {
        Button buttonSettings = new Button(new Icon(VaadinIcon.COG_O));
        return buttonSettings;
    }

    private TextField textField() {
        TextField textField = new TextField("", "1-1 из 1");
        textField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        return textField;
    }
}
