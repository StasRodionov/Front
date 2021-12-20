package com.trade_accounting.components.retail;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.MoneySubMutualSettlementsDto;
import com.trade_accounting.models.dto.RetailReturnsDto;
import com.trade_accounting.services.interfaces.RetailReturnsService;
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

@Route(value = "RetailReturnsView", layout = AppView.class)
@PageTitle("Возвраты")
@SpringComponent
@UIScope
public class RetailReturnsView extends VerticalLayout implements AfterNavigationObserver {

    private final RetailReturnsService retailReturnsService;
    private List<RetailReturnsDto> data;
    private final TextField textField = new TextField();
    private final GridFilter<RetailReturnsDto> filter;
    private final Grid<RetailReturnsDto> grid = new Grid<>(RetailReturnsDto.class, false);
    private final GridPaginator<RetailReturnsDto> paginator;

    public RetailReturnsView(RetailReturnsService retailReturnsService) {
        this.retailReturnsService = retailReturnsService;
        this.data = retailReturnsService.getAll();
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn("date").setFlexGrow(10).setHeader("Время").setId("Время");
        grid.addColumn("cashAmount").setFlexGrow(5).setHeader("Сумма нал.").setId("Сумма нал.");
        grid.addColumn("cashlessAmount").setFlexGrow(5).setHeader("Сумма безнал.").setId("Сумма безнал.");
        grid.addColumn("totalAmount").setFlexGrow(5).setHeader("Итого").setId("Итого");
        this.filter = new GridFilter<>(grid);
        configureFilter();
        this.paginator = new GridPaginator<>(grid, data, 100);
        configureGrid();
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);
        add(upperLayout(), filter, grid, paginator);
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.removeAllColumns();
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn("date").setFlexGrow(10).setHeader("Время").setId("date");
        grid.addColumn("retailStoreId").setFlexGrow(5).setHeader("Точка продаж").setId("retailStoreId");
        grid.addColumn("cashAmount").setFlexGrow(5).setHeader("Сумма нал.").setId("cashAmount");
        grid.addColumn("cashlessAmount").setFlexGrow(5).setHeader("Сумма безнал.").setId("cashlessAmount");
        grid.addColumn("totalAmount").setFlexGrow(5).setHeader("Итого").setId("totalAmount");
        grid.addColumn(new ComponentRenderer<>(this::isSentCheckedIcon)).setFlexGrow(10).setWidth("35px").setKey("sent")
                .setHeader("Отправлена").setId("Отправлена");
        grid.addColumn(new ComponentRenderer<>(this::isPrintedCheckedIcon)).setFlexGrow(10).setWidth("35px").setKey("printed")
                .setHeader("Напечатана").setId("Напечатана");
        grid.addColumn("comment").setFlexGrow(7).setHeader("Комментарий").setId("comment");

        GridSortOrder<RetailReturnsDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        grid.setColumnReorderingAllowed(true);
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
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Dialog modal = new Dialog();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Html content = new Html("<div><p>Документы фиксируют возвраты товаров в точки продаж. " +
                "Возврат создается на основе отгрузки или без основания.</p>" +
                "<p>Читать инструкции:</p><p> <a href=\"https://support.moysklad.ru/hc/ru/articles/231107628\" " +
                "target=\"_blank\">Возврат покупателя</a></p>" +
                "<p> <a href=\"https://support.moysklad.ru/hc/ru/articles/203325423\" target=\"_blank\">Розница</a></p></div>");
        Button close = new Button(new Icon(VaadinIcon.CLOSE));
        close.setWidth("30px");
        close.addClickListener(e -> modal.close());
        horizontalLayout.add(content, new Div(close));
        modal.add(horizontalLayout);
        modal.setWidth("500px");
        modal.setHeight("250px");
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
        Select<String> select = new Select<>();
        select.setItems("Изменить");
        select.setValue("Изменить");
        select.setWidth("130px");
        return select;
    }

    private Select<String> getStatus() {
        Select<String> status = new Select<>();
        status.setItems("Статус");
        status.setValue("Статус");
        status.setWidth("130px");
        return status;
    }


    private Select<String> getPrint() {
        Select<String> print = new Select<>();
        print.setItems("Печать");
        print.setValue("Печать");
        print.setWidth("130px");
        return print;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }

}