package com.trade_accounting.components.retail;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.PrepaymentReturnDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.PrepaymentReturnService;
import com.trade_accounting.services.interfaces.RetailStoreService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Div;
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

@Route(value = "PrepaymentReturnView", layout = AppView.class)
@PageTitle("Возвраты предоплат")
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
    private final GridPaginator<PrepaymentReturnDto> paginator;

    public PrepaymentReturnView(PrepaymentReturnService prepaymentReturnService, ContractorService contractorService, RetailStoreService retailStoreService, CompanyService companyService) {
        this.prepaymentReturnService = prepaymentReturnService;
        this.data = prepaymentReturnService.getAll();
        this.contractorService = contractorService;
        this.retailStoreService = retailStoreService;
        this.companyService = companyService;
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
        grid.removeAllColumns();
        grid.addColumn("id").setWidth("30px").setHeader("№").setId("№");
        grid.addColumn("time").setFlexGrow(7).setHeader("Время").setId("time");
        grid.addColumn(prepaymentReturnDto -> retailStoreService.getById(prepaymentReturnDto.getRetailStoreId())
                .getName()).setHeader("Точка продаж").setId("retailStore");
        grid.addColumn(prepaymentReturnDto -> contractorService.getById(prepaymentReturnDto.getContractorId())
                .getName()).setHeader("Контрагент").setId("contractor");
        grid.addColumn(prepaymentReturnDto -> companyService.getById(prepaymentReturnDto.getCompanyId())
                .getName()).setHeader("Точка Организация").setId("company");
        grid.addColumn("sumCash").setFlexGrow(5).setHeader("Сумма нал.").setId("sumCash");
        grid.addColumn("sumNonСash").setFlexGrow(5).setHeader("Сумма безнал.").setId("sumNonСash");
        grid.addColumn(this::getSum).setFlexGrow(5).setHeader("Итого").setId("sum");
        grid.addColumn(new ComponentRenderer<>(this::isSentCheckedIcon)).setFlexGrow(7).setWidth("35px").setKey("sent")
                .setHeader("Отправлена").setId("Отправлена");
        grid.addColumn(new ComponentRenderer<>(this::isPrintedCheckedIcon)).setFlexGrow(7).setWidth("35px").setKey("printed")
                .setHeader("Напечатана").setId("Напечатана");
        grid.addColumn("comment").setFlexGrow(7).setHeader("Комментарий").setId("comment");

        GridSortOrder<PrepaymentReturnDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        grid.setColumnReorderingAllowed(true);
    }

    private void configureFilter() {
        grid.removeAllColumns();
        grid.addColumn("time").setFlexGrow(10).setHeader("Время").setId("Время");
        grid.addColumn("retailStoreId").setFlexGrow(5).setHeader("Точка продаж").setId("Точка продаж");
        grid.addColumn("contractorId").setFlexGrow(5).setHeader("Контрагент").setId("Контрагент");
        grid.addColumn("companyId").setFlexGrow(5).setHeader("Организация").setId("Организация");
        grid.addColumn("sumCash").setFlexGrow(5).setHeader("Сумма нал.").setId("Сумма нал.");
        grid.addColumn("sumNonСash").setFlexGrow(5).setHeader("Сумма безнал.").setId("Сумма безнал.");
        grid.addColumn("sum").setFlexGrow(5).setHeader("Итого").setId("Итого");
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
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Dialog modal = new Dialog();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Html content = new Html("<div><p>При предоплате покупатель вносит полную или частичную стоимость " +
                "товара, после чего имеет возможность вернуть эти деньги или получить предоплаченный товар (с " +
                "внесением остатка суммы, если предоплата была неполной).</p>" +
                "<p>Читать инструкцию: <a href=\"#\" target=\"_blank\">Предоплата в кассе</a></p></div>");
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
        Select<String> select = new Select<>();
        List<String> listItems = new ArrayList<>();
        listItems.add("Изменить");
        listItems.add("Удалить");
        select.setItems(listItems);
        select.setValue("Изменить");
        select.setWidth("130px");
        select.addValueChangeListener(event -> {
            if (select.getValue().equals("Удалить")) {
                deleteSelectedInvoices();
                grid.deselectAll();
                select.setValue("Изменить");
                paginator.setData(prepaymentReturnService.getAll());
            }
        });
        return select;
    }

    private void deleteSelectedInvoices() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (PrepaymentReturnDto prepaymentReturnDto : grid.getSelectedItems()) {
                prepaymentReturnService.deleteById(prepaymentReturnDto.getId());
            }
        }
    }

    private Select<String> getStatus() {
        Select<String> status = new Select<>();
        status.setItems("Статус", "Настроить...");
        status.setValue("Статус");
        status.setWidth("130px");
        return status;
    }


    private Select<String> getPrint() {
        Select<String> print = new Select<>();
        print.setItems("Печать", "Список выплат", "Комплект...", "Настроить...");
        print.setValue("Печать");
        print.setWidth("130px");
        return print;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }

}