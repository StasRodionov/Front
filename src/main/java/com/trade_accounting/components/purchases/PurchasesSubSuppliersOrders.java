package com.trade_accounting.components.purchases;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.sells.SalesEditCreateInvoiceView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.InvoiceProductDto;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
import java.util.List;

@Slf4j
@Route(value = "suppliersOrders", layout = AppView.class)
@PageTitle("Заказы поставщикам")
@SpringComponent
@UIScope
public class PurchasesSubSuppliersOrders extends VerticalLayout implements AfterNavigationObserver {

    private final InvoiceService invoiceService;
    private final SalesEditCreateInvoiceView salesEditCreateInvoiceView;
    private final Notifications notifications;

    private List<InvoiceDto> invoices;

    private final String typeOfInvoice = "EXPENSE";

    private HorizontalLayout actions;
    private final Grid<InvoiceDto> grid = new Grid<>(InvoiceDto .class, false);
    private GridPaginator<InvoiceDto> paginator;
    private final GridFilter<InvoiceDto> filter;

    @Autowired
    public PurchasesSubSuppliersOrders(InvoiceService invoiceService,
                                       @Lazy SalesEditCreateInvoiceView salesEditCreateInvoiceView,
                                       @Lazy Notifications notifications) {
        this.invoiceService = invoiceService;
        this.salesEditCreateInvoiceView = salesEditCreateInvoiceView;
        this.notifications = notifications;
        loadInvoices();
        configureActions();
        configureGrid();
        configurePaginator();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        add(actions, filter, grid, paginator);
    }

    private List<InvoiceDto> loadInvoices() {
        invoices = invoiceService.getAll(typeOfInvoice);
        return invoices;
    }

    private void configureActions() {
        actions = new HorizontalLayout();
        actions.add(buttonQuestion(), title(), buttonRefresh(), buttonUnit(), buttonFilter(), filterTextField(),
                numberField(), valueSelect(), valueStatus(), valueCreate(), valuePrint(), buttonSettings());
        actions.setDefaultVerticalComponentAlignment(Alignment.CENTER);
    }

    private Grid<InvoiceDto> configureGrid() {
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(iDto -> formatDate(iDto.getDate())).setKey("date").setHeader("Дата").setSortable(true)
                .setId("Дата");
        grid.addColumn(iDto -> iDto.getContractorDto().getName()).setHeader("Контрагент").setKey("contractorDto")
                .setId("Контрагент");
//        grid.addColumn("typeOfInvoice").setHeader("Счет-фактура").setId("Счет-фактура");
//        grid.addColumn("spend").setHeader("Проведена").setId("Проведена");
        grid.addColumn(iDto -> iDto.getCompanyDto().getName()).setHeader("Компания").setKey("companyDto")
                .setId("Компания");
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedIcon)).setKey("spend").setHeader("Проведена")
                .setId("Проведена");
        grid.addColumn(this::getTotalPrice).setHeader("Сумма").setSortable(true);
//        grid.addColumn(iDto -> iDto.getWarehouseDto().getName()).setHeader("Склад").setKey("warehouseDto").setId("Склад");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");

        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addItemDoubleClickListener(event -> {
            InvoiceDto editInvoice = event.getItem();
            salesEditCreateInvoiceView.setInvoiceDataForEdit(editInvoice);
            salesEditCreateInvoiceView.setUpdateState(true);
            salesEditCreateInvoiceView.setType("EXPENSE");
            salesEditCreateInvoiceView.setLocation("purchases");
            UI.getCurrent().navigate("sells/customer-order-edit");
        });
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
        buttonUnit.addClickListener(event -> {
            salesEditCreateInvoiceView.resetView();
            salesEditCreateInvoiceView.setUpdateState(false);
            salesEditCreateInvoiceView.setType("EXPENSE");
            salesEditCreateInvoiceView.setLocation("purchases");
            buttonUnit.getUI().ifPresent(ui -> ui.navigate("sells/customer-order-edit"));
        });
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
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addValueChangeListener(e -> updateList(textField.getValue()));
        textField.setWidth("300px");
        return textField;
    }

    private void updateList(String text) {
        if (text.isEmpty()) {
            paginator.setData(invoiceService.getAll(typeOfInvoice));
        } else paginator.setData(invoiceService
                .findBySearchAndTypeOfInvoice(text, typeOfInvoice));
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
        select.addValueChangeListener(event -> {
            if (select.getValue().equals("Удалить")) {
                deleteSelectedInvoices();
                grid.deselectAll();
                select.setValue("Изменить");
                paginator.setData(loadInvoices());
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
    private Component getIsCheckedIcon(InvoiceDto invoiceDto) {
        if (invoiceDto.isSpend()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }
    private void updateList() {
        grid.setItems(invoiceService.getAll(typeOfInvoice));
    }
    private String getTotalPrice(InvoiceDto invoiceDto) {
        List<InvoiceProductDto> invoiceProductDtoList = salesEditCreateInvoiceView.getListOfInvoiceProductByInvoice(invoiceDto);
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        for (InvoiceProductDto invoiceProductDto : invoiceProductDtoList) {
            totalPrice = totalPrice.add(invoiceProductDto.getPrice()
                    .multiply(invoiceProductDto.getAmount()));
        }
        return String.format("%.2f", totalPrice);
    }
    private void deleteSelectedInvoices() {
        if (grid.getSelectedItems().isEmpty()) {
            for (InvoiceDto invoiceDto : grid.getSelectedItems()) {
                invoiceService.deleteById(invoiceDto.getId());
                notifications.infoNotification("Выбранные заказы успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные заказы");
        }
    }
    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
