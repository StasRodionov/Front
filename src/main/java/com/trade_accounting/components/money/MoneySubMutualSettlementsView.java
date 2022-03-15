package com.trade_accounting.components.money;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.finance.MoneySubMutualSettlementsDto;
import com.trade_accounting.services.interfaces.finance.MoneySubMutualSettlementsService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "MoneySubMutualSettlementsView", layout = AppView.class)
@PageTitle("Взаиморасчеты")
public class MoneySubMutualSettlementsView extends VerticalLayout {

    private final MoneySubMutualSettlementsService moneySubMutualSettlementsService;

    private final List<MoneySubMutualSettlementsDto> data;
    private final Grid<MoneySubMutualSettlementsDto> grid = new Grid<>(MoneySubMutualSettlementsDto.class, false);
    private final GridFilter<MoneySubMutualSettlementsDto> filter;
    GridPaginator<MoneySubMutualSettlementsDto> paginator;

    private H2 title() {
        H2 title = new H2("Взаиморасчеты");
        title.setHeight("2.2em");
        return title;
    }


    public MoneySubMutualSettlementsView(MoneySubMutualSettlementsService moneySubMutualSettlementsService) {
        this.moneySubMutualSettlementsService = moneySubMutualSettlementsService;
        configureGrid();
        this.data = moneySubMutualSettlementsService.getAll();
        this.paginator = new GridPaginator<>(grid, data, 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);

        this.filter = new GridFilter<>(grid);
        configureFilter();

        add(getToolbar(), filter);
        add(grid, paginator);
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn("contractorId").setFlexGrow(10).setHeader("Контрагент").setId("Контрагент");
        grid.addColumn("employeeId").setFlexGrow(7).setHeader("Сотрудник").setId("Сотрудник");
        grid.addColumn("initialBalance").setFlexGrow(7).setHeader("Начальный остаток").setId("Начальный остаток");
        grid.addColumn("income").setFlexGrow(7).setHeader("Приход").setId("Приход");
        grid.addColumn("expenses").setFlexGrow(7).setHeader("Расход").setId("Расход");
        grid.addColumn("finalBalance").setFlexGrow(7).setHeader("Конечный остаток").setId("Конечный остаток");
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("contractorId");
        filter.setFieldToIntegerField("employeeId");
        filter.setFieldToIntegerField("initialBalance");
        filter.setFieldToIntegerField("income");
        filter.setFieldToIntegerField("expenses");
        filter.setFieldToIntegerField("finalBalance");
        filter.onSearchClick(e ->
            paginator.setData(moneySubMutualSettlementsService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(getData()));
    }

    private  List<MoneySubMutualSettlementsDto> getData(){
        return moneySubMutualSettlementsService.getAll();
    }




    private Tabs configurationSubMenu() {
        Tab contractors = new Tab("C контрагентами");
        Tab employees = new Tab("С сотрудниками");
        Tabs tabs = new Tabs(contractors, employees);

        tabs.addSelectedChangeListener(event -> {
            String tabName = event.getSelectedTab().getLabel();
            if ("C контрагентами".equals(tabName)) {
                grid.removeAllColumns();
                grid.setItems(data);
                grid.addColumn("contractorId").setFlexGrow(7).setHeader("Контрагент").setId("Контрагент");
                grid.addColumn("initialBalance").setFlexGrow(7).setHeader("Начальный остаток").setId("Начальный остаток");
                grid.addColumn("income").setFlexGrow(7).setHeader("Приход").setId("Приход");
                grid.addColumn("expenses").setFlexGrow(7).setHeader("Расход").setId("Расход");
                grid.addColumn("finalBalance").setFlexGrow(7).setHeader("Конечный остаток").setId("Конечный остаток");
                add(grid, paginator);
            } else if ("С сотрудниками".equals(tabName)) {
                grid.removeAllColumns();
                grid.setItems(data);
                grid.addColumn("employeeId").setFlexGrow(7).setHeader("Сотрудник").setId("Сотрудник");
                grid.addColumn("initialBalance").setFlexGrow(7).setHeader("Начальный остаток").setId("Начальный остаток");
                grid.addColumn("income").setFlexGrow(7).setHeader("Приход").setId("Приход");
                grid.addColumn("expenses").setFlexGrow(7).setHeader("Расход").setId("Расход");
                grid.addColumn("finalBalance").setFlexGrow(7).setHeader("Конечный остаток").setId("Конечный остаток");
                add(grid, paginator);
            }
        });
        return tabs;
    }

    private void updateList() {
        GridPaginator<MoneySubMutualSettlementsDto> paginatorUpdateList
                = new GridPaginator<>(grid, moneySubMutualSettlementsService.getAll(), 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(getToolbar(), grid, paginator);
    }


    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), getTextContract(), getButtonRefresh(), configurationSubMenu(), getButtonFilter(), getPrint());
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolbar;
    }

    private Button getButtonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
    }

    private Button getButtonRefresh() {
        final Button buttonRefresh;
        Icon circle = new Icon(VaadinIcon.REFRESH);
        buttonRefresh = new Button();
        buttonRefresh.setIcon(circle);
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private H2 getTextContract() {
        final H2 textCompany = new H2("Взаиморасчеты");
        textCompany.setHeight("2.2em");
        return textCompany;
    }

    private Button getButtonQuestion() {
        return Buttons.buttonQuestion("Добавьте описание");
    }


    private Select<String> getPrint() {
        return SelectConfigurer.configurePrintSelect();
    }

}
