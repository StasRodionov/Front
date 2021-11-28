package com.trade_accounting.components.retail;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.BonusProgramDto;
import com.trade_accounting.services.interfaces.BonusProgramService;
import com.trade_accounting.services.interfaces.ContractorGroupService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
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

@Route(value = "BonusProgramTabView", layout = AppView.class)
@PageTitle("Бонусная программа")
@SpringComponent
@UIScope
public class BonusProgramTabView  extends VerticalLayout implements AfterNavigationObserver  {
    private final BonusProgramService bonusProgramService;
    private final ContractorGroupService contractorGroupService;
    private List<BonusProgramDto> data;

    private final Grid<BonusProgramDto> grid = new Grid<>(BonusProgramDto.class, false);
    private final GridPaginator<BonusProgramDto> paginator;

    public BonusProgramTabView(BonusProgramService bonusProgramService, ContractorGroupService contractorGroupService) {
        this.bonusProgramService = bonusProgramService;
        this.data = bonusProgramService.getAll();
        this.contractorGroupService = contractorGroupService;
        configureGrid();
        this.paginator = new GridPaginator<>(grid, data, 100);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);
        add(upperLayout(), grid, paginator);
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn("id").setWidth("20px").setHeader("№").setId("№");
        grid.addColumn("name").setHeader("Название").setId("name");
        grid.addColumn(new ComponentRenderer<>(this::isActiveStatusCheckedIcon)).setWidth("35px").setKey("activeStatus")
                .setHeader("Статус").setId("activeStatus");
        grid.addColumn(new ComponentRenderer<>(this::isAllContractorsCheckedIcon)).setWidth("35px").setKey("allContractors")
                .setHeader("Все Контрагенты").setId("allContractors");


        grid.addColumn("accrualRule").setHeader("Правило начилсения").setId("accrualRule");
        grid.addColumn("writeOffRules").setHeader("Правило списания").setId("writeOffRules");
        grid.addColumn("maxPaymentPercentage").setHeader("Макс. процент оплаты").setId("maxPaymentPercentage");
        grid.addColumn("numberOfDays").setHeader("Баллы начисл. через").setId("numberOfDays");
        grid.addColumn(new ComponentRenderer<>(this::isWelcomePointsCheckedIcon)).setWidth("35px").setKey("welcomePoints")
                .setHeader("Приветственные баллы").setId("welcomePoints");
        grid.addColumn(bonusProgramDto -> {
            return bonusProgramService.getById(bonusProgramDto.getId()).getNumberOfPoints() == null ? "--" :
                    bonusProgramService.getById(bonusProgramDto.getId()).getNumberOfPoints();
        } ).setHeader("Колл-во нач. баллов").setId("numberOfPoints");
        grid.addColumn(new ComponentRenderer<>(this::isRegistrationInBonusProgramCheckedIcon)
                ).setWidth("35px")
                .setKey("registrationInBonusProgram")
                .setHeader("При регистр.").setId("registrationInBonusProgram");
        grid.addColumn(new ComponentRenderer<>(this::isFirstPurchaseCheckedIcon)).setWidth("35px").setKey("firstPurchase")
                .setHeader("При первой покупке").setId("firstPurchase");

        GridSortOrder<BonusProgramDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        grid.setColumnReorderingAllowed(true);
    }

    private Component isActiveStatusCheckedIcon(BonusProgramDto bonusProgramDto) {
        if (bonusProgramDto.getActiveStatus()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }

    }

    private Component isAllContractorsCheckedIcon(BonusProgramDto bonusProgramDto) {
        if (bonusProgramDto.getAllContractors()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component isWelcomePointsCheckedIcon(BonusProgramDto bonusProgramDto) {
        if (bonusProgramDto.getWelcomePoints()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component isRegistrationInBonusProgramCheckedIcon(BonusProgramDto bonusProgramDto) {
        if (bonusProgramDto.getRegistrationInBonusProgram() == null) {
            return new Span("--");
        }
        if (bonusProgramDto.getRegistrationInBonusProgram()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component isFirstPurchaseCheckedIcon(BonusProgramDto bonusProgramDto) {
        if (bonusProgramDto.getFirstPurchase() == null) {
            return new Span("--");
        }
        if (bonusProgramDto.getFirstPurchase()) {
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
        H2 title = new H2("Бонусная программа");
        title.setHeight("2.2em");
        return title;
    }

    // Здесь кнопка вопроса Розница -- бонусная программа
    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Dialog modal = new Dialog();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Html content = new Html("<div>" +
                "<p>" +
                "Создание, управление и настройка бонусных программ, в которых может участвовать пользователь. \n" +
                "</p>" +
                "<p>Читать инструкцию: <a href=\"#\" target=\"_blank\">Бонусная программа</a></p></div>");
        Button close = new Button(new Icon(VaadinIcon.CLOSE));
        close.setWidth("30px");
        close.addClickListener(e -> modal.close());
        horizontalLayout.add(content, new Div(close));
        modal.add(horizontalLayout);
        modal.setWidth("500px");
        modal.setHeight("200px");
        buttonQuestion.addClickListener(e -> modal.open());
        Shortcuts.addShortcutListener(modal, modal::close, Key.ESCAPE);
        return buttonQuestion;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(e -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonCreate() {
        Button createRetailStoreButton = new Button("Бонусная программа", new Icon(VaadinIcon.PLUS_CIRCLE));
        BonusProgramModalWindow bonusProgramModalWindow =
                new BonusProgramModalWindow(bonusProgramService, contractorGroupService);
        createRetailStoreButton.addClickListener(e -> {
            bonusProgramModalWindow.addDetachListener(event -> updateList());
            bonusProgramModalWindow.open();
        });
        createRetailStoreButton.getStyle().set("cursor", "pointer");
        return createRetailStoreButton;
    }

    private Button buttonFilter() {
        return new Button("Фильтр");
    }

    private void updateList() {
        GridPaginator<BonusProgramDto> paginatorUpdateList
                = new GridPaginator<>(grid, bonusProgramService.getAll(), 100);
        GridSortOrder<BonusProgramDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
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
