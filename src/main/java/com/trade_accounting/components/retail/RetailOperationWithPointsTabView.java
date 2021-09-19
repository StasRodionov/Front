package com.trade_accounting.components.retail;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.RetailOperationWithPointsDto;
import com.trade_accounting.services.interfaces.BonusProgramService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.RetailOperationWithPointsService;
import com.trade_accounting.services.interfaces.TaskService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.Arrays;
import java.util.List;

@Route(value = "RetailOperationWithPointsTabView", layout = AppView.class)
@PageTitle("Операции с баллами")
@SpringComponent
@UIScope
public class RetailOperationWithPointsTabView extends VerticalLayout implements AfterNavigationObserver {

    private final RetailOperationWithPointsService retailOperationWithPointsService;
    private final BonusProgramService bonusProgramService;
    private final ContractorService contractorService;
    private final TaskService taskService;
    private List<RetailOperationWithPointsDto> data;

    private final Grid<RetailOperationWithPointsDto> grid = new Grid<>(RetailOperationWithPointsDto.class, false);
    private final GridPaginator<RetailOperationWithPointsDto> paginator;

    public RetailOperationWithPointsTabView(RetailOperationWithPointsService retailOperationWithPointsService, BonusProgramService bonusProgramService, ContractorService contractorService, TaskService taskService) {
        this.retailOperationWithPointsService = retailOperationWithPointsService;
        this.data = retailOperationWithPointsService.getAll();
        this.bonusProgramService = bonusProgramService;
        this.contractorService = contractorService;
        this.taskService = taskService;
        configureGrid();
        this.paginator = new GridPaginator<>(grid, data, 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(upperLayout(), grid, paginator);
    }

    private void configureGrid() {
        grid.addColumn("id").setWidth("1px").setHeader("№").setId("№");
        //grid.addColumn("number").setHeader("№").setId("number");
        grid.addColumn("currentTime").setHeader("Время").setId("currentTime");
        grid.addColumn("typeOperation").setHeader("Тип операции").setId("typeOperation");
        grid.addColumn("numberOfPoints").setHeader("Колличество баллов").setId("numberOfPoints");
        grid.addColumn("accrualDate").setHeader("Дата начисления").setId("accrualDate");
        grid.addColumn(retailOperationWithPointsDto -> bonusProgramService.getById(retailOperationWithPointsDto.getBonusProgramId())
                .getName()).setHeader("Бонусная программа").setId("bonusProgram");
        grid.addColumn(retailOperationWithPointsDto -> contractorService.getById(retailOperationWithPointsDto.getContractorId())
                .getName()).setHeader("Контрагент").setId("contractor");

        GridSortOrder<RetailOperationWithPointsDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        grid.setColumnReorderingAllowed(true);
    }


    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonCreate(), buttonFilter());
        upper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upper;
    }

    private H2 title() {
        H2 title = new H2("Точки продаж");
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
        GridPaginator<RetailOperationWithPointsDto> paginatorUpdateList
                = new GridPaginator<>(grid, retailOperationWithPointsService.getAll(), 100);
        GridSortOrder<RetailOperationWithPointsDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(upperLayout(), grid, paginatorUpdateList);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }

}
