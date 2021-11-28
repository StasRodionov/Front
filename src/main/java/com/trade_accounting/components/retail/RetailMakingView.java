package com.trade_accounting.components.retail;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.RetailMakingDto;
import com.trade_accounting.services.interfaces.RetailMakingService;
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
@Route(value = "RetailMakingView", layout = AppView.class)
@PageTitle("Внесения")
@SpringComponent
@UIScope
public class RetailMakingView extends VerticalLayout implements AfterNavigationObserver {

    private final RetailMakingService retailMakingService;
    private List<RetailMakingDto> data;

    private final GridFilter<RetailMakingDto> filter;
    private final Grid<RetailMakingDto> grid = new Grid<>(RetailMakingDto.class, false);
    private final GridPaginator<RetailMakingDto> paginator;

    public RetailMakingView(RetailMakingService retailMakingService) {
        this.retailMakingService = retailMakingService;
        this.data = retailMakingService.getAll();
        grid.addColumn("date").setFlexGrow(10).setHeader("Время").setId("Время");
        grid.addColumn("retailStoreId").setFlexGrow(5).setHeader("Точка продаж").setId("Точка продаж");
        grid.addColumn("fromWhom").setFlexGrow(5).setHeader("От кого").setId("От кого");
        grid.addColumn("companyId").setFlexGrow(5).setHeader("Организация").setId("Организация");
        grid.addColumn("sum").setFlexGrow(5).setHeader("Сумма").setId("Сумма");
        this.filter = new GridFilter<>(grid);
        add(upperLayout(), filter);
        this.paginator = new GridPaginator<>(grid, data, 100);
        configureGrid();
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);
        add(grid, paginator);
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.removeAllColumns();
        grid.addColumn("id").setWidth("30px").setHeader("№").setId("№");
        grid.addColumn("date").setFlexGrow(10).setHeader("Время").setId("date");
        grid.addColumn("retailStoreId").setFlexGrow(5).setHeader("Точка продаж").setId("retailStoreId");
        grid.addColumn("fromWhom").setFlexGrow(5).setHeader("От кого").setId("fromWhom");
        grid.addColumn("companyId").setFlexGrow(5).setHeader("Организация").setId("companyId");
        grid.addColumn("sum").setFlexGrow(5).setHeader("Сумма").setId("sum");
        grid.addColumn(new ComponentRenderer<>(this::isSentCheckedIcon)).setFlexGrow(10).setWidth("35px").setKey("sent")
                .setHeader("Отправлена").setId("Отправлена");
        grid.addColumn(new ComponentRenderer<>(this::isPrintedCheckedIcon)).setFlexGrow(10).setWidth("35px").setKey("printed")
                .setHeader("Напечатана").setId("Напечатана");
        grid.addColumn("comment").setFlexGrow(7).setHeader("Комментарий").setId("comment");

        GridSortOrder<RetailMakingDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        grid.setColumnReorderingAllowed(true);
    }

    private Component isSentCheckedIcon(RetailMakingDto retailMakingDto) {
        if (retailMakingDto.getIsSent()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component isPrintedCheckedIcon(RetailMakingDto retailMakingDto) {
        if (retailMakingDto.getIsPrint()) {
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
        H2 title = new H2("Внесения");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Dialog modal = new Dialog();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Html content = new Html("<div><p>Внесения фиксируют поступление наличных денег на точку продаж.</p>" +
                "<p>Читать инструкцию: </p><p><a href=\"#\" target=\"_blank\">Розница</a></p></div>");
        Button close = new Button(new Icon(VaadinIcon.CLOSE));
        close.setWidth("30px");
        close.addClickListener(e -> modal.close());
        horizontalLayout.add(content, new Div(close));
        modal.add(horizontalLayout);
        modal.setWidth("500px");
        modal.setHeight("300px");
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

    private void updateList() {
        GridPaginator<RetailMakingDto> paginatorUpdateList
                = new GridPaginator<>(grid, retailMakingService.getAll(), 100);
        GridSortOrder<RetailMakingDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
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
        final TextField textField = new TextField();
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        return textField;
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
