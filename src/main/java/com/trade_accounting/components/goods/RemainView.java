package com.trade_accounting.components.goods;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.Action;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.warehouse.RemainDto;
import com.trade_accounting.services.interfaces.warehouse.RemainService;
import com.trade_accounting.services.interfaces.units.UnitService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.ArrayList;
import java.util.List;

import static com.trade_accounting.config.SecurityConstants.GOODS_REMAIN_VIEW;

@SpringComponent
//Если на страницу не ссылаются по URL или она не является отдельной страницей, а подгружается родительским классом, то URL и Title не нужен
/*@PageTitle("Остатки")
@Route(value = GOODS_REMAIN_VIEW, layout = AppView.class)*/
@UIScope
public class RemainView extends VerticalLayout {

    private final RemainService remainService;
    private final UnitService unitService;
    private final Notifications notifications;

    private final Grid<RemainDto> grid = new Grid<>(RemainDto.class, false);
    private final GridPaginator<RemainDto> paginator;
    private List<RemainDto> data;
    private final TextField textField = new TextField();
    private final MenuBar selectXlsTemplateButton = new MenuBar();
    private final RemainModalWindow remainModalWindow;
    private final GridFilter<RemainDto> filter;
    private final String typeOfRemain = "EXPENSE";

    public RemainView(RemainService remainService, UnitService unitService, Notifications notifications) {
        this.remainService = remainService;
        this.unitService = unitService;
        this.notifications = notifications;
        data = getData();
        this.remainModalWindow = new RemainModalWindow(remainService);
        paginator = new GridPaginator<>(grid, data, 50);
        setSizeFull();
        configureGrid();
        this.filter = new GridFilter<>(grid);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(configureActions(), filter, grid, paginator);
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(RemainDto::getName).setKey("name").setHeader("Наименование").setId("Наименование");
        grid.addColumn(RemainDto::getVendorCode).setKey("vendorCode").setHeader("Код Продавца").setId("Код Продавца");
        grid.addColumn(RemainDto::getBalance).setKey("balance").setHeader("Баланс").setId("Баланс");
        grid.addColumn(RemainDto::getIrreducibleBalance).setKey("irreducibleBalance").setHeader("Неснижаемый Остаток").setId("Неснижаемый Остаток");
        grid.addColumn(RemainDto::getReserve).setKey("reserve").setHeader("Резерв").setId("Резерв");
        grid.addColumn(RemainDto::getExpectation).setKey("expectation").setHeader("Ожидание").setId("Ожидание");
        grid.addColumn(RemainDto::getAvailable).setKey("available").setHeader("Доступно").setId("Доступно");
        grid.addColumn(remainDto -> unitService.getById(remainDto.getUnitId()).getFullName()).setKey("unitId").setHeader("Единицы измерения").setId("Единицы измерения");
        grid.addColumn(RemainDto::getDaysOnWarehouse).setKey("daysOnWarehouse").setHeader("Дней на складе").setId("Дней на складе");
        grid.addColumn(RemainDto::getCostPrice).setKey("costPrice").setHeader("Закупочная цена").setId("Закупочная цена");
        grid.addColumn(RemainDto::getSumOfCostPrice).setKey("sumOfCostPrice").setHeader("Сумма Закупки").setId("Сумма Закупки");
        grid.addColumn(RemainDto::getSalesCost).setKey("salesCost").setHeader("Цена на продажу").setId("Цена на продажу");
        grid.addColumn(RemainDto::getSalesSum).setKey("salesSum").setHeader("Сумма продаж").setId("Сумма продаж");
        grid.setHeight("66vh");
        grid.setMaxWidth("100%");
        grid.addItemDoubleClickListener(event -> {
            remainModalWindow.addDetachListener(e -> updateList());
            remainModalWindow.open();
        });
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
    }

    private List<RemainDto> getData() {
        return new ArrayList<>(remainService.getAll());
    }

    private void deleteSelectedRemains() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (RemainDto remainDto : grid.getSelectedItems()) {
                remainService.deleteById(remainDto.getId());
                notifications.infoNotification("Выбранные заказы успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные заказы");
        }
    }
    private HorizontalLayout configureActions() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), getTextOrder(), buttonRefresh(), buttonUnit(), buttonFilter(),
                numberField(), valueSelect(), valueStatus(), valuePrint(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upper;
    }

    private Button buttonUnit() {
        Button createRetailStoreButton = new Button("Остатки", new Icon(VaadinIcon.PLUS_CIRCLE));
        RemainModalWindow remainModalWindow =
                new RemainModalWindow(remainService);
        createRetailStoreButton.addClickListener(e -> {
            remainModalWindow.addDetachListener(event -> updateList());
            remainModalWindow.open();
        });
        createRetailStoreButton.getStyle().set("cursor", "pointer");
        return createRetailStoreButton;
    }

    private H2 getTextOrder() {
        final H2 textOrder = new H2("Остатки");
        textOrder.setHeight("2.2em");
        return textOrder;
    }

    private HorizontalLayout getTollBar() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(buttonQuestion(), getTextOrder(), buttonRefresh(), buttonUnit(),
                buttonFilter(), text(), numberField(), valueSelect(), valueStatus(),
                valuePrint(), buttonSettings(), selectXlsTemplateButton);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion("Остатки  - добавить текст");
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(ev -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    public void updateList() {
        paginator.setData(data, false);
    }


    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private TextField filterTextField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addValueChangeListener(e -> updateList(textField.getValue()));
        textField.setWidth("300px");
        textField.setWidth("220px");
        return textField;
    }

    private void updateList(String search) {
        if (search.isEmpty()) {
            paginator.setData(remainService.getAll(typeOfRemain));
        } else paginator.setData(remainService
                .findBySearchAndTypeOfRemain(search, typeOfRemain));
    }

    private TextField text() {
        textField.setWidth("300px");
        textField.setPlaceholder("Наименование или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setClearButtonVisible(true);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        setSizeFull();
        return textField;
    }

    private NumberField numberField() {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect() {
        return SelectConfigurer.configureDeleteSelect(() -> {
            deleteSelectedRemains();
            grid.deselectAll();
            paginator.setData(getData());
        });
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private Select<String> valueStatus() {
        return SelectConfigurer.configureStatusSelect();
    }

    private Select<String> valuePrint() {
        return SelectConfigurer.configurePrintSelect();
    }
}
