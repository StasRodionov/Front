package com.trade_accounting.components.retail;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.PrepayoutDto;
import com.trade_accounting.services.interfaces.PrepayoutService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(value = "PrepayoutView", layout = AppView.class)
@PageTitle("Предоплаты")
@SpringComponent
@UIScope
public class PrepayoutView extends VerticalLayout implements AfterNavigationObserver {

    private final PrepayoutService prepayoutService;
    private List<PrepayoutDto> data;

    private final GridFilter<PrepayoutDto> filter;
    private final Grid<PrepayoutDto> grid = new Grid<>(PrepayoutDto.class, false);
    private final GridPaginator<PrepayoutDto> paginator;

    public PrepayoutView(PrepayoutService prepayoutService) {
        this.prepayoutService = prepayoutService;
        this.data = prepayoutService.getAll();
        grid.setItems(data);
        configureFilter();
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
        grid.addColumn("date").setFlexGrow(10).setHeader("Дата").setId("date");
        grid.addColumn("retailStoreId").setFlexGrow(5).setHeader("Точка продаж").setId("retailStoreId");
        grid.addColumn("contractorId").setFlexGrow(5).setHeader("Контрагент").setId("contractorId");
        grid.addColumn("companyId").setFlexGrow(5).setHeader("Организация").setId("companyId");
        grid.addColumn("cash").setFlexGrow(5).setHeader("Сумма нал.").setId("cash");
        grid.addColumn("cashless").setFlexGrow(5).setHeader("Сумма безнал.").setId("cashless");
        grid.addColumn("sum").setFlexGrow(5).setHeader("Итого").setId("sum");
        grid.addColumn(new ComponentRenderer<>(this::isSentCheckedIcon)).setFlexGrow(10).setWidth("35px").setKey("sent")
                .setHeader("Отправлена").setId("Отправлена");
        grid.addColumn(new ComponentRenderer<>(this::isPrintedCheckedIcon)).setFlexGrow(10).setWidth("35px").setKey("printed")
                .setHeader("Напечатана").setId("Напечатана");
        grid.addColumn("comment").setFlexGrow(7).setHeader("Комментарий").setId("comment");

        grid.addItemDoubleClickListener(event -> {
            PrepayoutDto prepayoutDto = event.getItem();
            PrepayoutModalWindow prepayoutModalWindow = new PrepayoutModalWindow(prepayoutService);
            prepayoutModalWindow.setPrepayoutForEdit(prepayoutDto);
            prepayoutModalWindow.open();
        });

        GridSortOrder<PrepayoutDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        grid.setColumnReorderingAllowed(true);
    }

    private void configureFilter() {
        grid.removeAllColumns();
        grid.addColumn("date").setFlexGrow(10).setHeader("Дата").setId("Дата");
        grid.addColumn("retailStoreId").setFlexGrow(5).setHeader("Точка продаж").setId("Точка продаж");
        grid.addColumn("contractorId").setFlexGrow(5).setHeader("Контрагент").setId("Контрагент");
        grid.addColumn("companyId").setFlexGrow(5).setHeader("Организация").setId("Организация");
        grid.addColumn("cash").setFlexGrow(5).setHeader("Сумма нал.").setId("Сумма нал.");
        grid.addColumn("cashless").setFlexGrow(5).setHeader("Сумма безнал.").setId("Сумма безнал.");
        grid.addColumn("sum").setFlexGrow(5).setHeader("Итого").setId("Итого");
    }

    private Component isSentCheckedIcon(PrepayoutDto retailReturnsDto) {
        if (retailReturnsDto.getIsSent()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Button buttonCreate() {
        Button createRetailStoreButton = new Button("Предоплата", new Icon(VaadinIcon.PLUS_CIRCLE));
        PrepayoutModalWindow prepayoutModalWindow =
                new PrepayoutModalWindow(prepayoutService);
        createRetailStoreButton.addClickListener(e -> {
            prepayoutModalWindow.addDetachListener(event -> updateList());
            prepayoutModalWindow.open();
        });
        createRetailStoreButton.getStyle().set("cursor", "pointer");
        return createRetailStoreButton;
    }

    private Component isPrintedCheckedIcon(PrepayoutDto retailReturnsDto) {
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
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonCreate(), buttonFilter(), numberField(), textField(), getSelect(), getStatus(), getPrint(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private H2 title() {
        H2 title = new H2("Предоплаты");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("При предоплате покупатель вносит полную или частичную " +
                        "стоимость товара, после чего имеет возможность вернуть эти деньги или получить " +
                        "предоплаченный товар (с внесением остатка суммы, если предоплата была неполной). " +
                        "Читать инструкцию: "),
                new Anchor("https://support.moysklad.ru/hc/ru/articles/360015823873", "Предоплата в кассе"));
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
        GridPaginator<PrepayoutDto> paginatorUpdateList
                = new GridPaginator<>(grid, prepayoutService.getAll(), 100);
        GridSortOrder<PrepayoutDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginatorUpdateList);
        removeAll();
        grid.setItems(data);
        configureFilter();
        add(upperLayout(), filter);
        configureGrid();
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);
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
                paginator.setData(prepayoutService.getAll());
            }
        });
        return select;
    }

    private void deleteSelectedInvoices() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (PrepayoutDto prepayoutDto : grid.getSelectedItems()) {
                prepayoutService.deleteById(prepayoutDto.getId());
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