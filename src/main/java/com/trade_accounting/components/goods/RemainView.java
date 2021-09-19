package com.trade_accounting.components.goods;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.RemainDto;
import com.trade_accounting.services.interfaces.RemainService;
import com.trade_accounting.services.interfaces.UnitService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
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
import java.util.stream.Collectors;

@SpringComponent
@PageTitle("Остатки")
@Route(value = "remainView", layout = AppView.class)
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

    public RemainView(RemainService remainService, UnitService unitService, Notifications notifications) {
        this.remainService = remainService;
        this.unitService = unitService;
        this.notifications = notifications;
        data = getData();
        paginator = new GridPaginator<>(grid, data, 50);
        setSizeFull();
        configureGrid();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(configureActions(), grid, paginator);
    }

    private void configureGrid() {
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
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
    }

    private List<RemainDto> getData() {
        List<RemainDto> list = new ArrayList<>();
        RemainDto remainDto1 = new RemainDto(4L, "name1", "vendorCode1", 23134, 39535, 93078, 28034, 46973, 38L, 11, 45773, 66039, 56196, 64115 );
        RemainDto remainDto2 = new RemainDto(5L, "name2", "vendorCode2", 23134, 39535, 93078, 28034, 46973, 38L, 11, 45773, 66039, 56196, 64115 );
        list.add(remainDto1);
        list.add(remainDto2);
        return list;
//        return new ArrayList<>(remainService.getAll());  //надо разобраться с RemainServiceImpl
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
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Dialog dialog = new Dialog();
        Button cancelButton = new Button("Закрыть", event -> dialog.close());
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponentAsFirst(cancelButton);
        dialog.add(new Text("Остатки  - добавить текст"));
        dialog.setWidth("450px");
        dialog.setHeight("250px");
        buttonQuestion.addClickListener(event -> dialog.open());
        Shortcuts.addShortcutListener(dialog, dialog::close, Key.ESCAPE);
        dialog.add(new Div(cancelButton));
        return buttonQuestion;
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

    private Button buttonUnit() {
        return new Button("Остатки", new Icon(VaadinIcon.PLUS_CIRCLE));
    }

    private Button buttonFilter() {
        return new Button("Фильтр");
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
        Select<String> select = new Select<>();
        List<String> listItems = new ArrayList<>();
        listItems.add("Изменить");
        listItems.add("Удалить");
        select.setItems(listItems);
        select.setValue("Изменить");
        select.setWidth("130px");
        select.addValueChangeListener(event -> {
            if (select.getValue().equals("Удалить")) {
                deleteSelectedRemains();
                grid.deselectAll();
                select.setValue("Изменить");
                paginator.setData(getData());
            }
        });
        return select;
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private Select<String> valueStatus() {
        Select<String> status = new Select<>();
        status.setItems("Статус");
        status.setValue("Статус");
        status.setWidth("110px");
        return status;
    }

    private Select<String> valuePrint() {
        Select<String> print = new Select<>();
        print.setItems("Печать", "Добавить шаблон");
        print.setValue("Печать");
        print.setWidth("110px");
        return print;
    }
}
