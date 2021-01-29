package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
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
@Route(value = "customersOrders", layout = AppView.class)
@PageTitle("Заказы покупателей")
public class SalesSubCustomersOrdersView extends VerticalLayout {

    private final InvoiceService invoiceService;
    private final ContractorService contractorService;
    private final CompanyService companyService;
    private final List<InvoiceDto> data;
    private final Grid<InvoiceDto> grid = new Grid<>(InvoiceDto.class);

//    private static final int ITEMS_PER_PAGE = 100;

    public SalesSubCustomersOrdersView(InvoiceService invoiceService,
                                       ContractorService contractorService,
                                       CompanyService companyService) {
        this.invoiceService = invoiceService;
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.data = getData();

        add(upperLayout(), grid, lowerLayout());
        configureGrid();
        updateList();
    }

    private void configureGrid() {
        grid.setColumns("id", "date", "typeOfInvoice", "company", "contractor", "spend");

        grid.getColumnByKey("id").setHeader("id");
        grid.getColumnByKey("date").setHeader("Дата");
        grid.getColumnByKey("typeOfInvoice").setHeader("Счет-фактура");
        grid.getColumnByKey("company").setHeader("Компания");
        grid.getColumnByKey("contractor").setHeader("Контрагент");
        grid.getColumnByKey("spend").setHeader("Проведена");
        grid.setHeight("66vh");

        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addItemDoubleClickListener(event -> {
            InvoiceDto editInvoice = event.getItem();
            SalesModalWinCustomersOrders addModalWin = new SalesModalWinCustomersOrders(editInvoice,
                    invoiceService, contractorService, companyService);
            addModalWin.addDetachListener(e -> updateList());
            addModalWin.open();
        });

//        loadItemsToGrid(grid,1);

    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonUnit(), buttonFilter(), textField(),
                numberField(), valueSelect(), valueStatus(), valueCreate(), valuePrint(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upper;
    }

    private HorizontalLayout lowerLayout() {
        HorizontalLayout lower = new HorizontalLayout();
        lower.add(new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT)),
                new Button(new Icon(VaadinIcon.ANGLE_LEFT)),
                textField(),
                new Button(new Icon(VaadinIcon.ANGLE_RIGHT)),
                new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT)));
        return lower;
    }

    private Button buttonQuestion(){
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private Button buttonRefresh(){
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonUnit(){
        Button buttonUnit = new Button("Заказ", new Icon(VaadinIcon.PLUS_CIRCLE));
        SalesModalWinCustomersOrders addModalWin = new SalesModalWinCustomersOrders(new InvoiceDto(), invoiceService,
                contractorService, companyService);
        addModalWin.addDetachListener(event -> updateList());
        buttonUnit.addClickListener(event -> addModalWin.open());
        return buttonUnit;
    }

    private Button buttonFilter(){
        Button buttonFilter = new Button("Фильтр");
        return buttonFilter;
    }

    private Button buttonSettings(){
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

    private H2 title(){
        H2 title = new H2("Заказы покупателей");
        title.setHeight("2.2em");
        return title;
    }

    private Select<String> valueSelect(){
        Select<String> select = new Select<>();
        select.setItems("Изменить");
        select.setValue("Изменить");
        select.setWidth("130px");
        return select;
    }

    private Select<String> valueStatus(){
        Select<String> status = new Select<>();
        status.setItems("Статус");
        status.setValue("Статус");
        status.setWidth("130px");
        return status;
    }

    private Select<String> valueCreate(){
        Select<String> create = new Select<>();
        create.setItems("Создать");
        create.setValue("Создать");
        create.setWidth("130px");
        return create;
    }

    private Select<String> valuePrint(){
        Select<String> print = new Select<>();
        print.setItems("Печать");
        print.setValue("Печать");
        print.setWidth("130px");
        return print;
    }

    private void updateList() {
        grid.setItems(invoiceService.getAll());
        System.out.println("Обновлен");
    }




//    private void loadItemsToGrid(Grid<InvoiceDto> grid, int page) {
//        int from = (page - 1) * ITEMS_PER_PAGE;
//
//        int to = (from + ITEMS_PER_PAGE);
//        to = Math.min(to, data.size());
//
//        grid.setItems(data.subList(from, to));
//    }

    private List<InvoiceDto> getData() {
        return invoiceService.getAll();
    }




}

