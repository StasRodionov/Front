package com.trade_accounting.components.retail;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.RetailSalesDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.RetailSalesService;
import com.trade_accounting.services.interfaces.RetailStoreService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;


import java.util.Arrays;
import java.util.List;


@Route(value = "RetailSalesTabView", layout = AppView.class)
@PageTitle("Продажи")
@SpringComponent
@UIScope
public class RetailSalesTabView extends VerticalLayout implements AfterNavigationObserver {

    private final RetailSalesService retailSalesService;
    private final RetailStoreService retailStoreService;
    private final CompanyService companyService;
    private final ContractorService contractorService;
    private List<RetailSalesDto> data;

    private final Grid<RetailSalesDto> grid = new Grid<>(RetailSalesDto.class, false);
    private final GridPaginator<RetailSalesDto> paginator;

    public RetailSalesTabView(RetailSalesService retailSalesService, RetailStoreService retailStoreService,
                              CompanyService companyService, ContractorService contractorService) {
        this.retailSalesService = retailSalesService;
        this.data = retailSalesService.getAll();
        this.retailStoreService = retailStoreService;
        this.companyService = companyService;
        this.contractorService = contractorService;
        configureGrid();
        this.paginator = new GridPaginator<>(grid, data, 100);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);
        add(upperLayout(), grid, paginator);
    }

    private void configureGrid() {
        grid.addColumn("id").setWidth("30px").setHeader("№").setId("№");
        grid.addColumn("time").setHeader("Время").setId("time");
        grid.addColumn(retailSalesDto -> retailStoreService.getById(retailSalesDto.getRetailStoreId()).getName())
                .setHeader("Точка продаж").setId("retailStoreId");
        grid.addColumn(retailSalesDto -> contractorService.getById(retailSalesDto.getContractorId()).getName())
                .setHeader("Контрагент").setId("contractorId");
        grid.addColumn(retailSalesDto -> companyService.getById(retailSalesDto.getCompanyId()).getName())
                .setHeader("Организация").setId("companyId");
        grid.addColumn("sumCash").setHeader("Сума нал.").setId("sumCash");
        grid.addColumn("sumNonСash").setHeader("Сумма безнал.").setId("sumNonСash");
        grid.addColumn("prepayment").setHeader("Сумма предопл.").setId("prepayment");
        grid.addColumn("sumDiscount").setHeader("Сумма скидок").setId("sumDiscount");
        grid.addColumn("sum").setHeader("Итого").setId("sum");
        grid.addColumn(new ComponentRenderer<>(this::isSentCheckedIcon)).setWidth("35px").setKey("sent")
                .setHeader("Отправлена").setId("Отправлена");
        grid.addColumn(new ComponentRenderer<>(this::isPrintedCheckedIcon)).setWidth("35px").setKey("printed")
                .setHeader("Напечатана").setId("Напечатана");
        grid.addColumn("comment").setHeader("Комментарий").setId("comment");

        GridSortOrder<RetailSalesDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        grid.setColumnReorderingAllowed(true);
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
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonCreate(), buttonFilter());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private H2 title() {
        H2 title = new H2("Продажи");
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

    private Button buttonCreate() {
        Button createRetailStoreButton = new Button("Продажи", new Icon(VaadinIcon.PLUS_CIRCLE));
        /*RetailStoreModalWindow retailStoreModalWindow =
                new RetailStoreModalWindow(retailStoreService, companyService, employeeService);
        createRetailStoreButton.addClickListener(e -> {
            retailStoreModalWindow.addDetachListener(event -> updateList());
            retailStoreModalWindow.open();
        });*/
        createRetailStoreButton.getStyle().set("cursor", "pointer");
        return createRetailStoreButton;
    }

    private Button buttonFilter() {
        return new Button("Фильтр");
    }

    private void updateList() {
        GridPaginator<RetailSalesDto> paginatorUpdateList
                = new GridPaginator<>(grid, retailSalesService.getAll(), 100);
        GridSortOrder<RetailSalesDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(upperLayout(), grid, paginatorUpdateList);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }

}
