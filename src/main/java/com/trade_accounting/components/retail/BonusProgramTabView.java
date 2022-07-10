package com.trade_accounting.components.retail;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridConfigurer;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.util.BonusProgramDto;
import com.trade_accounting.services.interfaces.util.BonusProgramService;
import com.trade_accounting.services.interfaces.company.ContractorGroupService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
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

import static com.trade_accounting.config.SecurityConstants.RETAIL_BONUS_PROGRAM_VIEW;

//Если на страницу не ссылаются по URL или она не является отдельной страницей, а подгружается родительским классом, то URL и Title не нужен
/*@Route(value = RETAIL_BONUS_PROGRAM_VIEW, layout = AppView.class)
@PageTitle("ACTION_4")*/
@SpringComponent
@UIScope
public class BonusProgramTabView  extends VerticalLayout implements AfterNavigationObserver  {
    transient private final BonusProgramService bonusProgramService;
    transient private final ContractorGroupService contractorGroupService;
    transient private List<BonusProgramDto> data;

    private final Grid<BonusProgramDto> grid = new Grid<>(BonusProgramDto.class, false);
    private final GridConfigurer<BonusProgramDto> gridConfigurer = new GridConfigurer<>(grid);
    private final GridPaginator<BonusProgramDto> paginator;
    static final String ACTION_4 = "Бонусная программа";
    private static final String ACTION_5 = "green";
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};

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
        grid.addThemeVariants(GRID_STYLE);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn("name").setHeader("Название").setId("Название");
        grid.addColumn(new ComponentRenderer<>(this::isActiveStatusCheckedIcon)).setKey("activeStatus")
                .setHeader("Статус").setId("Статус");
        grid.addColumn(new ComponentRenderer<>(this::isAllContractorsCheckedIcon)).setKey("allContractors")
                .setHeader("Все контрагенты").setId("Все контрагенты");
        grid.addColumn("accrualRule").setHeader("Правило начилсения").setTextAlign(ColumnTextAlign.END)
                .setId("Правило начилсения");
        grid.addColumn("writeOffRules").setHeader("Правило списания").setTextAlign(ColumnTextAlign.END)
                .setId("Правило списания");
        grid.addColumn("maxPaymentPercentage").setHeader("Макс. процент оплаты").setTextAlign(ColumnTextAlign.END)
                .setId("Макс. процент оплаты");
        grid.addColumn("numberOfDays").setHeader("Баллы начисл. через").setTextAlign(ColumnTextAlign.END)
                .setId("Баллы начисл. через");
        grid.addColumn(new ComponentRenderer<>(this::isWelcomePointsCheckedIcon)).setKey("welcomePoints")
                .setHeader("Приветственные баллы").setId("Приветственные баллы");
        grid.addColumn(bonusProgramDto -> {
            return bonusProgramService.getById(bonusProgramDto.getId()).getNumberOfPoints() == null ? "--" :
                    bonusProgramService.getById(bonusProgramDto.getId()).getNumberOfPoints();
        } ).setHeader("Кол-во нач. баллов").setTextAlign(ColumnTextAlign.END).setId("Кол-во нач. баллов");
        grid.addColumn(new ComponentRenderer<>(this::isRegistrationInBonusProgramCheckedIcon)
                ).setKey("registrationInBonusProgram")
                .setHeader("При регистр.").setId("При регистр.");
        grid.addColumn(new ComponentRenderer<>(this::isFirstPurchaseCheckedIcon)).setKey("firstPurchase")
                .setHeader("При первой покупке").setId("При первой покупке");
        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        gridConfigurer.addConfigColumnToGrid();

        grid.setHeight("64vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        GridSortOrder<BonusProgramDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
    }

    private Component isActiveStatusCheckedIcon(BonusProgramDto bonusProgramDto) {
        if (bonusProgramDto.getActiveStatus()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor(ACTION_5);
            return icon;
        } else {
            return new Span("");
        }

    }

    private Component isAllContractorsCheckedIcon(BonusProgramDto bonusProgramDto) {
        if (bonusProgramDto.getAllContractors()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor(ACTION_5);
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component isWelcomePointsCheckedIcon(BonusProgramDto bonusProgramDto) {
        if (bonusProgramDto.getWelcomePoints()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor(ACTION_5);
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
            icon.setColor(ACTION_5);
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
            icon.setColor(ACTION_5);
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
        H2 title = new H2(ACTION_4);
        title.setHeight("2.2em");
        return title;
    }

    // Здесь кнопка вопроса Розница -- ACTION_4
    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("Создание, управление и настройка бонусных программ, в которых может участвовать пользователь. " +
                        "Читать инструкцию: "),
                new Anchor("#", ACTION_4));
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(e -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonCreate() {
        Button createRetailStoreButton = new Button(ACTION_4, new Icon(VaadinIcon.PLUS_CIRCLE));
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
