package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Slf4j
@Route(value = "customersOrders", layout = AppView.class)
@PageTitle("Заказы покупателей")
@SpringComponent
@UIScope
public class SalesSubCustomersOrdersView extends VerticalLayout implements AfterNavigationObserver {

    private final InvoiceService invoiceService;

    private final SalesEditCreateInvoiceView salesEditCreateInvoiceView;

    private final List<InvoiceDto> data;
    private final Grid<InvoiceDto> grid = new Grid<>(InvoiceDto.class, false);
    private final GridPaginator<InvoiceDto> paginator;
    private final GridFilter<InvoiceDto> filter;

    @Autowired
    public SalesSubCustomersOrdersView(InvoiceService invoiceService,
                                       @Lazy SalesEditCreateInvoiceView salesEditCreateInvoiceView
    ) {
        this.salesEditCreateInvoiceView = salesEditCreateInvoiceView;
        this.invoiceService = invoiceService;
        this.data = getData();
        paginator = new GridPaginator<>(grid, data, 100);
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(upperLayout(), filter, grid, paginator);
    }

    private void configureGrid() {
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

        grid.addItemDoubleClickListener(event -> {
            InvoiceDto editInvoice = event.getItem();
            salesEditCreateInvoiceView.setInvoiceDataForEdit(editInvoice);
            salesEditCreateInvoiceView.setUpdateState(true);
            UI.getCurrent().navigate("sells/customer-order-edit");
        });
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("spend", Boolean.TRUE, Boolean.FALSE);
        filter.onSearchClick(e -> paginator.setData(invoiceService.search(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(invoiceService.getAll()));
    }


    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonUnit(), buttonFilter(), textField(),
                numberField(), valueSelect(), valueStatus(), valueCreate(), valuePrint(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upper;
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
        Button buttonUnit = new Button("Заказ", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickListener(event -> {
            salesEditCreateInvoiceView.resetView();
            salesEditCreateInvoiceView.setUpdateState(false);
            buttonUnit.getUI().ifPresent(ui -> ui.navigate("sells/customer-order-edit"));
        });
        return buttonUnit;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
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
        H2 title = new H2("Заказы покупателей");
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

    private void updateList() {
        grid.setItems(invoiceService.getAll());
    }

    private List<InvoiceDto> getData() {
        return invoiceService.getAll();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}

