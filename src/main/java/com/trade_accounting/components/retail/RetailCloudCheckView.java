package com.trade_accounting.components.retail;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.RetailCloudCheckDto;
import com.trade_accounting.services.interfaces.CurrencyService;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.RetailCloudCheckService;
import com.trade_accounting.services.interfaces.RetailStoreService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Route(value = "RetailCloudCheckView", layout = AppView.class)
@PageTitle("Очередь облачных чеков")
@SpringComponent
@UIScope
public class RetailCloudCheckView extends VerticalLayout implements AfterNavigationObserver {

    private final RetailCloudCheckService retailCloudCheckService;
    private final CurrencyService currencyService;
    private final RetailStoreService retailStoreService;
    private final EmployeeService employeeService;
    private List<RetailCloudCheckDto> data;

    private final GridFilter<RetailCloudCheckDto> filter;
    private final Grid<RetailCloudCheckDto> grid = new Grid<>(RetailCloudCheckDto.class, false);
    private final GridPaginator<RetailCloudCheckDto> paginator;

    public RetailCloudCheckView(RetailCloudCheckService retailCloudCheckService, CurrencyService currencyService, RetailStoreService retailStoreService, EmployeeService employeeService) {
        this.retailCloudCheckService = retailCloudCheckService;
        this.data = retailCloudCheckService.getAll();
        this.currencyService = currencyService;
        this.retailStoreService = retailStoreService;
        this.employeeService = employeeService;
        this.filter = new GridFilter<>(grid);
        add(upperLayout(), filter);
        this.paginator = new GridPaginator<>(grid, data, 100);
        configureGrid();
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);
        add(grid, paginator);
    }

    private void configureGrid() {
        grid.removeAllColumns();
        grid.addColumn("id").setWidth("30px").setHeader("№").setId("№");
        grid.addColumn("date").setFlexGrow(10).setHeader("Время").setId("date");
        grid.addColumn(retailCloudCheckDto -> retailStoreService.getById(retailCloudCheckDto.getInitiatorId())
                .getName()).setHeader("Инициатор").setId("initiator");
        grid.addColumn(retailCloudCheckDto -> retailStoreService.getById(retailCloudCheckDto.getFiscalizationPointId())
                .getName()).setFlexGrow(10).setHeader("Точка фискализации").setId("fiscalizationPoint");
        grid.addColumn("status").setFlexGrow(5).setHeader("Статус").setId("status");
        grid.addColumn("cheskStatus").setFlexGrow(5).setHeader("Статус чека").setId("cheskStatus");
        grid.addColumn("total").setFlexGrow(5).setHeader("Итого").setId("total");

        grid.addColumn("currencyId").setFlexGrow(5).setHeader("Валюта").setId("currencyId");

        grid.addColumn(retailCloudCheckDto -> employeeService.getById(retailCloudCheckDto.getCashierId())
                .getFirstName()).setHeader("Кассир").setId("cashier");

        GridSortOrder<RetailCloudCheckDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        grid.setColumnReorderingAllowed(true);
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonFilter(), buttonCancel(), buttonEventLog(), textField(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private H2 title() {
        H2 title = new H2("Очередь облачных чеков");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(e -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }


    private Button buttonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
    }

    private void updateList() {
        GridPaginator<RetailCloudCheckDto> paginatorUpdateList
                = new GridPaginator<>(grid, retailCloudCheckService.getAll(), 100);
        GridSortOrder<RetailCloudCheckDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(upperLayout(), grid, paginatorUpdateList);
    }


    private Button buttonSettings() {
        Button buttonSettings = new Button(new Icon(VaadinIcon.COG_O));
        return buttonSettings;
    }

    private TextField textField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        return textField;
    }

    private Button buttonCancel() {
        Button status = new Button("Отменить фискализацию");
        status.setWidth("220px");
        return status;
    }

    private Button buttonEventLog() {
        Button status = new Button("Журнал событий");
        status.setWidth("170px");
        return status;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }

}
