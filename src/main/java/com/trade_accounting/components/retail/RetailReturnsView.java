package com.trade_accounting.components.retail;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridConfigurer;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.components.util.configure.components.select.SelectConstants;
import com.trade_accounting.components.util.configure.components.select.SelectExt;
import com.trade_accounting.models.dto.retail.RetailReturnsDto;
import com.trade_accounting.services.interfaces.retail.RetailReturnsService;
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

import java.util.Arrays;
import java.util.List;

import static com.trade_accounting.config.SecurityConstants.GRID_RETAIL_MAIN_RETURNS;
import static com.trade_accounting.config.SecurityConstants.RETAIL_RETAIL_RETURNS_VIEW;

//Если на страницу не ссылаются по URL или она не является отдельной страницей, а подгружается родительским классом, то URL и Title не нужен
/*@Route(value = RETAIL_RETAIL_RETURNS_VIEW, layout = AppView.class)
@PageTitle("Возвраты")*/
@SpringComponent
@UIScope
public class RetailReturnsView extends VerticalLayout implements AfterNavigationObserver {

    private final RetailReturnsService retailReturnsService;
    private List<RetailReturnsDto> data;
    private final TextField textField = new TextField();
    private final GridFilter<RetailReturnsDto> filter;
    private final Grid<RetailReturnsDto> grid = new Grid<>(RetailReturnsDto.class, false);
    private final GridConfigurer<RetailReturnsDto> gridConfigurer;
    private final GridPaginator<RetailReturnsDto> paginator;
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES,
            GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};

    public RetailReturnsView(RetailReturnsService retailReturnsService,
                             ColumnsMaskService columnsMaskService) {
        this.retailReturnsService = retailReturnsService;
        this.data = retailReturnsService.getAll();
        this.gridConfigurer = new GridConfigurer<>(grid, columnsMaskService, GRID_RETAIL_MAIN_RETURNS);
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        this.paginator = new GridPaginator<>(grid, data, 100);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);
        add(upperLayout(), filter, grid, paginator);
    }

    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn("date").setHeader("Дата").setId("Дата");
        grid.addColumn("retailStoreId").setHeader("Точка продаж").setId("Точка продаж");
        grid.addColumn("cashAmount").setHeader("Сумма нал.").setTextAlign(ColumnTextAlign.END)
                .setId("Сумма нал.");
        grid.addColumn("cashlessAmount").setHeader("Сумма безнал.").setTextAlign(ColumnTextAlign.END)
                .setId("Сумма безнал.");
        grid.addColumn("totalAmount").setHeader("Итого").setTextAlign(ColumnTextAlign.END)
                .setId("Итого");
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

        GridSortOrder<RetailReturnsDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
    }

    private Component isSentCheckedIcon(RetailReturnsDto retailReturnsDto) {
        if (retailReturnsDto.getIsSend()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component isPrintedCheckedIcon(RetailReturnsDto retailReturnsDto) {
        if (retailReturnsDto.getIsPrint()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonFilter(), numberField(), textField(), getSelect(),getStatus(), getPrint(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private H2 title() {
        H2 title = new H2("Возвраты");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("Документы фиксируют возвраты товаров в точки продаж. " +
                        "Возврат создается на основе отгрузки или без основания. " +
                        "Читать инструкции: "),
                new Anchor("https://support.moysklad.ru/hc/ru/articles/231107628", "Возврат покупателя"),
                new Text(", "),
                new Anchor("https://support.moysklad.ru/hc/ru/articles/203325423", "Розница"));
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

    private void configureFilter() {
        filter.onSearchClick(e ->
                paginator.setData(retailReturnsService.searchRetailReturns(filter.getFilterData())));
        filter.onClearClick(e ->
                paginator.setData(retailReturnsService.getAll()));
    }

    private void updateList() {
        GridPaginator<RetailReturnsDto> paginatorUpdateList
                = new GridPaginator<>(grid, retailReturnsService.getAll(), 100);
        GridSortOrder<RetailReturnsDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(upperLayout(), grid, paginatorUpdateList);
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
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        textField.setClearButtonVisible(true);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(e -> updateListTextField());
        return textField;
    }

    public void updateListTextField() {
        if (!(textField.getValue().equals(""))) {
            grid.setItems(retailReturnsService.search(textField.getValue()));
        } else {
            grid.setItems(retailReturnsService.search("null"));
        }
    }


    private Select<String> getSelect() {
        return new SelectExt.SelectBuilder<String>()
                .item(SelectConstants.CHANGE_SELECT_ITEM)
                .defaultValue(SelectConstants.CHANGE_SELECT_ITEM)
                .width(SelectConstants.SELECT_WIDTH_130PX)
                .build();
    }

    private Select<String> getStatus() {
        return SelectConfigurer.configureStatusSelect();
    }


    private Select<String> getPrint() {
        return SelectConfigurer.configurePrintSelect();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }

}