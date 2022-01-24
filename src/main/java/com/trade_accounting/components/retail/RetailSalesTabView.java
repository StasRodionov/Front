package com.trade_accounting.components.retail;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.controllers.dto.RetailSalesDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.RetailSalesService;
import com.trade_accounting.services.interfaces.RetailStoreService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
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
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Route(value = "RetailSalesTabView", layout = AppView.class)
@PageTitle("Продажи")
@SpringComponent
@UIScope
public class RetailSalesTabView extends VerticalLayout implements AfterNavigationObserver {

    private final TextField textField = new TextField();
    private final RetailSalesService retailSalesService;
    private final RetailStoreService retailStoreService;
    private final CompanyService companyService;
    private final ContractorService contractorService;
    private List<RetailSalesDto> data;

    private final Grid<RetailSalesDto> grid = new Grid<>(RetailSalesDto.class, false);
    private final GridPaginator<RetailSalesDto> paginator;
    private final GridFilter<RetailSalesDto> filter;

    public RetailSalesTabView(RetailSalesService retailSalesService, RetailStoreService retailStoreService,
                              CompanyService companyService, ContractorService contractorService) {
        this.retailSalesService = retailSalesService;
        this.data = retailSalesService.getAll();
        this.retailStoreService = retailStoreService;
        this.companyService = companyService;
        this.contractorService = contractorService;
        grid.removeAllColumns();
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();

        this.paginator = new GridPaginator<>(grid, data, 100);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);
        add(upperLayout(), filter, grid, paginator);
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.removeAllColumns();
        grid.addColumn("id").setHeader("№").setId("id");
        grid.addColumn("sumCash").setHeader("Сумма нал.").setId("Сумма нал.");
        grid.addColumn("sumNonСash").setHeader("Сумма безнал.").setId("Сумма безнал.");
        grid.addColumn("prepayment").setHeader("Сумма предопл.").setId("Сумма предопл.");
        grid.addColumn("sumDiscount").setHeader("Сумма скидок").setId("Сумма скидок");
        grid.addColumn("sum").setHeader("Итого").setId("Итого");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");

        grid.addColumn("time").setHeader("Время").setId("Время");
        grid.addColumn("retailStoreId").setHeader("Точка продаж").setId("Точка продаж");
        grid.addColumn("contractorId").setHeader("Контрагент").setId("Контрагент");
        grid.addColumn(e -> companyService.getById(e.getCompanyId()).getName()).setKey("companyName").setHeader("Организация").setId("Организация");

        grid.addColumn(new ComponentRenderer<>(this::isSentCheckedIcon)).setWidth("35px").setKey("sent")
                .setHeader("Отправлена").setId("Отправлена");
        grid.addColumn(new ComponentRenderer<>(this::isPrintedCheckedIcon)).setWidth("35px").setKey("printed")
                .setHeader("Напечатана").setId("Напечатана");

        GridSortOrder<RetailSalesDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        grid.setColumnReorderingAllowed(true);
    }

    private Button getButtonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("time");
        filter.setVisibleFields(false, "sent", "printed");
        filter.onSearchClick(e -> {
            paginator.setData(retailSalesService.searchRetailSales(filter.getFilterData()));
        });
        filter.onClearClick(e ->
                paginator.setData(retailSalesService.getAll()));
    }

    private Component isSentCheckedIcon(RetailSalesDto retailSalesDto) {
        if (retailSalesDto.getSent()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component isPrintedCheckedIcon(RetailSalesDto retailSalesDto) {
        if (retailSalesDto.getPrinted()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(),  getButtonFilter(),
                textField(), numberField(), valueSelect(), valueStatus(), valuePrint(),
                buttonSettings());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private H2 title() {
        H2 title = new H2("Продажи");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("В разделе представлен список розничных продаж — так фиксируется реализация товаров. " +
                        "Для того чтобы создать розничную продажу, необходимо открыть точку продаж. " +
                        "Читать инструкции: "),
                new Anchor("#", "Продажа в кассе"),
                new Text(", "),
                new Anchor("#", "Розница"));
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(e -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }


    private void updateList() {
        GridPaginator<RetailSalesDto> paginatorUpdateList
                = new GridPaginator<>(grid, retailSalesService.getAll(), 100);
        grid.removeAllColumns();
        grid.addColumn("id").setWidth("30px").setHeader("№").setId("id");
        grid.addColumn("time").setHeader("Время");
        grid.addColumn("retailStoreId").setHeader("Точка продаж").setId("retailStoreId");
        grid.addColumn("contractorId").setHeader("Контрагент").setId("contractorId");
        grid.addColumn("companyId").setHeader("Организация").setId("companyId");
        grid.addColumn("sumCash").setHeader("Сумма нал.").setId("sumCash");
        grid.addColumn("sumNonСash").setHeader("Сумма безнал.").setId("sumNonСash");
        grid.addColumn("prepayment").setHeader("Сумма предопл.").setId("prepayment");
        grid.addColumn("sumDiscount").setHeader("Сумма скидок").setId("sumDiscount");
        grid.addColumn("sum").setHeader("Итого").setId("sum");
        grid.addColumn(new ComponentRenderer<>(this::isSentCheckedIcon)).setWidth("35px").setKey("sent")
                .setHeader("Отправлена").setId("sent");
        grid.addColumn(new ComponentRenderer<>(this::isPrintedCheckedIcon)).setWidth("35px").setKey("printed")
                .setHeader("Напечатана").setId("printed");
        grid.addColumn("comment").setHeader("Комментарий").setId("comment");
        GridSortOrder<RetailSalesDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(upperLayout(), filter, grid, paginatorUpdateList);
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
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        textField.setClearButtonVisible(true);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(e -> updateListTextField());
        setSizeFull();
        return textField;
    }

    public void updateListTextField() {
        if (!(textField.getValue().equals(""))) {
            grid.setItems(retailSalesService.search(textField.getValue()));
        } else {
            grid.setItems(retailSalesService.search("null"));
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
                grid.removeAllColumns();
                select.setValue("Изменить");
            }
        });
        return select;
    }

    private Select<String> valueStatus() {
        Select<String> status = new Select<>();
        status.setItems("Статус", "Настроить");
        status.setValue("Статус");
        status.setWidth("130px");
        return status;
    }

    private Select<String> valuePrint() {
        Select<String> print = new Select<>();
        print.setItems("Печать", "Список продаж", "Товарный чек", "Комплект", "Настроить");
        print.setValue("Печать");
        print.setWidth("130px");
        return print;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }

}
