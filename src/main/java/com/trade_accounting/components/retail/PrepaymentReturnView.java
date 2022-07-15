package com.trade_accounting.components.retail;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridConfigurer;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.finance.PrepaymentReturnDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.finance.PrepaymentReturnService;
import com.trade_accounting.services.interfaces.retail.RetailStoreService;
import com.trade_accounting.services.interfaces.util.ColumnsMaskService;
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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.trade_accounting.config.SecurityConstants.GRID_RETAIL_MAIN_PREPAYMENT_RETURN;
import static com.trade_accounting.config.SecurityConstants.RETAIL_PREPAYMENT_RETURN_VIEW;

//Если на страницу не ссылаются по URL или она не является отдельной страницей, а подгружается родительским классом, то URL и Title не нужен
/*@Route(value = RETAIL_PREPAYMENT_RETURN_VIEW, layout = AppView.class)
@PageTitle("Возвраты предоплат")*/
@SpringComponent
@UIScope
public class PrepaymentReturnView extends VerticalLayout implements AfterNavigationObserver {

    private final PrepaymentReturnService prepaymentReturnService;
    private final ContractorService contractorService;
    private final RetailStoreService retailStoreService;
    private final CompanyService companyService;
    private List<PrepaymentReturnDto> data;

    private final GridFilter<PrepaymentReturnDto> filter;
    private final Grid<PrepaymentReturnDto> grid = new Grid<>(PrepaymentReturnDto.class, false);
    private final GridConfigurer<PrepaymentReturnDto> gridConfigurer;
    private final GridPaginator<PrepaymentReturnDto> paginator;
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES,
            GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};

    public PrepaymentReturnView(PrepaymentReturnService prepaymentReturnService,
                                ContractorService contractorService,
                                RetailStoreService retailStoreService,
                                CompanyService companyService,
                                ColumnsMaskService columnsMaskService) {
        this.prepaymentReturnService = prepaymentReturnService;
        this.data = prepaymentReturnService.getAll();
        this.contractorService = contractorService;
        this.retailStoreService = retailStoreService;
        this.companyService = companyService;
        this.gridConfigurer = new GridConfigurer<>(grid, columnsMaskService, GRID_RETAIL_MAIN_PREPAYMENT_RETURN);
        grid.setItems(data);
        configureFilter();
        this.filter = new GridFilter<>(grid);
        add(upperLayout(), filter);
        this.paginator = new GridPaginator<>(grid, data, 100);
        configureGrid();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(grid, paginator);
    }

    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn("time").setHeader("Дата").setId("Дата");
        grid.addColumn(prepaymentReturnDto -> retailStoreService.getById(prepaymentReturnDto.getRetailStoreId())
                .getName()).setHeader("Точка продаж").setId("Точка продаж");
        grid.addColumn(prepaymentReturnDto -> contractorService.getById(prepaymentReturnDto.getContractorId())
                .getName()).setHeader("Контрагент").setId("Контрагент");
        grid.addColumn(prepaymentReturnDto -> companyService.getById(prepaymentReturnDto.getCompanyId())
                .getName()).setHeader("Точка Организация").setId("Точка Организация");
        grid.addColumn("sumCash").setHeader("Сумма нал.").setTextAlign(ColumnTextAlign.END).setId("Сумма нал.");
        grid.addColumn("sumNonСash").setHeader("Сумма безнал.").setTextAlign(ColumnTextAlign.END).setId("Сумма безнал.");
        grid.addColumn(this::getSum).setHeader("Итого").setTextAlign(ColumnTextAlign.END).setId("Итого");
        grid.addColumn(new ComponentRenderer<>(this::isSentCheckedIcon)).setKey("sent")
                .setHeader("Отправлена").setId("Отправлена");
        grid.addColumn(new ComponentRenderer<>(this::isPrintedCheckedIcon)).setKey("printed")
                .setHeader("Напечатана").setId("Напечатана");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");
        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        gridConfigurer.addConfigColumnToGrid();

        grid.setHeight("64vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        GridSortOrder<PrepaymentReturnDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
    }

    private void configureFilter() {

    }

    private BigDecimal getSum(PrepaymentReturnDto prepaymentReturnDto) {
        return prepaymentReturnDto.getSumCash().add(prepaymentReturnDto.getSumNonСash());
    }

    private Button buttonCreate() {
        Button createRetailStoreButton = new Button("Возврат преоплаты", new Icon(VaadinIcon.PLUS_CIRCLE));
        PrepaymentReturnModalWindow prepaymentReturnModalWindow =
                new PrepaymentReturnModalWindow(prepaymentReturnService, companyService, retailStoreService, contractorService);
        createRetailStoreButton.addClickListener(e -> {
            prepaymentReturnModalWindow.addDetachListener(event -> updateList());
            prepaymentReturnModalWindow.open();
        });
        createRetailStoreButton.getStyle().set("cursor", "pointer");
        return createRetailStoreButton;
    }

    private Component isSentCheckedIcon(PrepaymentReturnDto prepaymentReturnDto) {
        if (prepaymentReturnDto.getSent()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component isPrintedCheckedIcon(PrepaymentReturnDto prepaymentReturnDto) {
        if (prepaymentReturnDto.getPrinted()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonCreate(), buttonFilter(), numberField(), textField(), getSelect(), getStatus(), getPrint(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upper;
    }

    private H2 title() {
        H2 title = new H2("Возвраты предоплат");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("При предоплате покупатель вносит полную или частичную стоимость " +
                        "товара, после чего имеет возможность вернуть эти деньги или получить предоплаченный товар " +
                        "(с внесением остатка суммы, если предоплата была неполной). " +
                        "Читать инструкцию: "),
                new Anchor("#", "Предоплата в кассе"));
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
        GridPaginator<PrepaymentReturnDto> paginatorUpdateList
                = new GridPaginator<>(grid, prepaymentReturnService.getAll(), 100);
        GridSortOrder<PrepaymentReturnDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorUpdateList);
        removeAll();
        grid.setItems(data);
        configureFilter();
        add(upperLayout(), filter);
        configureGrid();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(grid, paginator);
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


    private Select<String> getSelect() {
        return SelectConfigurer.configureDeleteSelect(() -> {
            deleteSelectedInvoices();
            grid.deselectAll();
            paginator.setData(prepaymentReturnService.getAll());
        });
    }

    private void deleteSelectedInvoices() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (PrepaymentReturnDto prepaymentReturnDto : grid.getSelectedItems()) {
                prepaymentReturnService.deleteById(prepaymentReturnDto.getId());
            }
        }
    }

    private Select<String> getStatus() {
        return SelectConfigurer.configureStatusSelect();
    }


    private Select<String> getPrint() {
        return SelectConfigurer.configurePrintSelect();
//        print.setItems("Печать", "Список выплат", "Комплект...", "Настроить...");
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }

}