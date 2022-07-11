package com.trade_accounting.components.retail;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridConfigurer;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.retail.RetailPointsDto;
import com.trade_accounting.services.interfaces.util.BonusProgramService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.retail.RetailPointsService;
import com.trade_accounting.services.interfaces.util.TaskService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
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
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.Arrays;
import java.util.List;

import static com.trade_accounting.config.SecurityConstants.RETAIL_RETAIL_POINTS_VIEW;

//Если на страницу не ссылаются по URL или она не является отдельной страницей, а подгружается родительским классом, то URL и Title не нужен
/*@Route(value = RETAIL_RETAIL_POINTS_VIEW, layout = AppView.class)
@PageTitle("Операции с баллами")*/
@SpringComponent
@UIScope
public class RetailPointsView extends VerticalLayout implements AfterNavigationObserver {

    private final RetailPointsService retailPointsService;
    private final BonusProgramService bonusProgramService;
    private final ContractorService contractorService;
    private final TaskService taskService;
    private final RetailPointsModalWindow retailPointsModalWindow;
    private List<RetailPointsDto> data;
    private final Grid<RetailPointsDto> grid = new Grid<>(RetailPointsDto.class, false);
    private final GridConfigurer<RetailPointsDto> gridConfigurer = new GridConfigurer<>(grid);
    private final GridFilter<RetailPointsDto> filter;
    private final GridPaginator<RetailPointsDto> paginator;
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};

    public RetailPointsView(RetailPointsService retailPointsService, BonusProgramService bonusProgramService,
                            ContractorService contractorService, TaskService taskService) {
        this.retailPointsService = retailPointsService;
        this.bonusProgramService = bonusProgramService;
        this.taskService = taskService;
        this.contractorService = contractorService;
        this.data = retailPointsService.getAll();
        this.retailPointsModalWindow = new RetailPointsModalWindow(retailPointsService);
        this.paginator = new GridPaginator<>(grid, data, 100);
        configureGrid();

        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);
        grid.setItems(data);
        configureFilter();
        this.filter = new GridFilter<>(grid);
        add(upperLayout(), filter);
        add(grid, paginator);
    }

    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.setItems(data);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn("number").setHeader("Номер").setId("Номер");
        grid.addColumn("currentTime").setHeader("Дата создания").setId("Дата создания");
        grid.addColumn("typeOperation").setHeader("Тип операции").setId("Тип операции");
        grid.addColumn("numberOfPoints").setHeader("Бонусные баллы").setTextAlign(ColumnTextAlign.END)
                .setId("Бонусные баллы");
        grid.addColumn("accrualDate").setHeader("Дата начисления").setId("Дата начисления");
        grid.addColumn(retailPointsDto -> bonusProgramService.getById(retailPointsDto.getBonusProgramId()))
                .setHeader("Бонусная программа").setId("Бонусная программа");
        grid.addColumn(retailPointsDto -> contractorService.getById(retailPointsDto.getContractorId()))
                .setHeader("Контрагент").setId("Контрагент");
        grid.addColumn("taskId").setHeader("Номер случая").setId("Номер случая");
        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        gridConfigurer.addConfigColumnToGrid();

        grid.setHeight("64vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addItemDoubleClickListener(event -> {
            retailPointsModalWindow.addDetachListener(e -> updateList());
            retailPointsModalWindow.open();
        });

        GridSortOrder<RetailPointsDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
    }


    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonCreate(), getButtonFilter(), textField(), numberField(), valueSelect());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private void configureFilter() {

    }

    private H2 title() {
        H2 title = new H2("Операции с баллами");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("Выплаты фиксируют выдачу наличных денег из точки продаж. " +
                        "Читать инструкцию: "),
                new Anchor("#", "Выплаты"));
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(e -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private TextField textField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        return textField;
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect() {
        return SelectConfigurer.configureBulkEditAndDeleteSelect();
        //select.setItems("Изменить", "Удалить", "Копировать", "Массовое удаление");
    }


    private Button buttonCreate() {
        Button createRetailStoreButton = new Button("Операция", new Icon(VaadinIcon.PLUS_CIRCLE));
        RetailPointsModalWindow retailPointsModalWindow =
                new RetailPointsModalWindow(retailPointsService);
        createRetailStoreButton.addClickListener(e -> {
            retailPointsModalWindow.addDetachListener(event -> updateList());
            retailPointsModalWindow.open();
        });
        createRetailStoreButton.getStyle().set("cursor", "pointer");
        return createRetailStoreButton;
    }

    private Button getButtonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
    }

    private void updateList() {
        GridPaginator<RetailPointsDto> paginatorUpdateList
                = new GridPaginator<>(grid, retailPointsService.getAll(), 100);
        GridSortOrder<RetailPointsDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(upperLayout(), filter, grid, paginatorUpdateList);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}