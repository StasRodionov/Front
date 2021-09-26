package com.trade_accounting.components.production;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.TechnicalOperationsDto;
import com.trade_accounting.services.interfaces.TechnicalCardService;
import com.trade_accounting.services.interfaces.TechnicalOperationsService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.ArrayList;
import java.util.List;


@SpringComponent
@UIScope
@PageTitle("Тех. операции")
@Route(value = "technological", layout = AppView.class)
public class TechnologicalOperationsViewTab extends VerticalLayout implements AfterNavigationObserver {

    private final TextField textField = new TextField();
    private final MenuBar selectXlsTemplateButton = new MenuBar();

    private final List<TechnicalOperationsDto> data;

    private final GridPaginator<TechnicalOperationsDto> paginator;
    private final Grid<TechnicalOperationsDto> grid = new Grid<>(TechnicalOperationsDto.class, false);

    private final TechnicalCardService technicalCardService;
    private final TechnicalOperationsService technicalOperationsService;
    private final Notifications notifications;
    private final WarehouseService warehouseService;
    private final TechnologicalOperationsModalView view;

    TechnologicalOperationsViewTab(TechnicalCardService technicalCardService, TechnicalOperationsService technicalOperationsService,
                                   Notifications notifications, WarehouseService warehouseService, TechnologicalOperationsModalView view) {
        this.technicalOperationsService = technicalOperationsService;
        this.notifications = notifications;
        this.warehouseService = warehouseService;
        this.view = view;

        paginator = new GridPaginator<>(grid, this.technicalOperationsService.getAll(), 100);
        this.technicalCardService = technicalCardService;
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(getTollBar(), grid, paginator);
        configureGrid();
        setSizeFull();
        this.data = getData();

    }

    private void configureGrid() {
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(TechnicalOperationsDto::getDate).setKey("date").setHeader("время").setSortable(true);
        grid.addColumn(e -> warehouseService.getById(e.getWarehouse()).getName()).setHeader("организация").setId("организация");
        grid.addColumn(t -> technicalCardService.getById(t.getTechnicalCard()).getName()).setHeader("Технологическая карта").setId("Технологическая карта");
        grid.addColumn("volume").setHeader("Объем производства").setId("Объем производства");
        grid.addColumn(new ComponentRenderer<>(this::getIsSentIcon)).setKey("sent").setHeader("Отправлено")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsPrintIcon)).setKey("print").setHeader("Напечатано")
                .setId("Напечатано");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");


        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addItemDoubleClickListener(e -> {
            TechnicalOperationsDto dto = e.getItem();
            TechnologicalOperationsModalView view = new TechnologicalOperationsModalView(
                    technicalCardService,
                    technicalOperationsService,
                    warehouseService,
                    notifications
            );
            view.setTechnicalOperationsEdit(dto);
            view.open();
        });
    }


    private Component getIsSentIcon(TechnicalOperationsDto technicalOperationsDto) {
        if (technicalOperationsDto.getIsSent()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component getIsPrintIcon(TechnicalOperationsDto technicalOperationsDto) {
        if (technicalOperationsDto.getIsPrint()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private HorizontalLayout getTollBar() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(buttonQuestion(), getTextOrder(), buttonRefresh(),buttonPlusTechnologicalOperations(),
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
        dialog.add(new Text("Технологические операции позволяют планировать закупки " +
                "у поставщиков и перемещения товаров по складам " +
                "внутри организации. С их помощью можно пополнять " +
                "резервы при достижении неснижаемого остатка."));
        dialog.setWidth("400px");
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

    private void updateList() {
        grid.setItems(technicalOperationsService.getAll());

    }

    private Button buttonPlusTechnologicalOperations() {

        Button addTechnologicalOperationsButton = new Button("операция", new Icon(VaadinIcon.PLUS_CIRCLE));
        addTechnologicalOperationsButton.addClickListener(e -> view.open());
        updateList();
        return addTechnologicalOperationsButton;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        return buttonFilter;
    }

    private TextField text() {
        textField.setWidth("300px");
        textField.setPlaceholder("Номер или комментарий");
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
        Select<String> valueSelect = new Select<>();
        List<String> list = new ArrayList<>();
        list.add("Изменить");
        list.add("Удалить");
        valueSelect.setItems(list);
        valueSelect.setValue("Изменить");
        valueSelect.setWidth("120px");
        valueSelect.addValueChangeListener(event -> {
            if (valueSelect.getValue().equals("Удалить")) {
                deleteSelectedInternalOrders();
                grid.deselectAll();
                valueSelect.setValue("Изменить");
                paginator.setData(getData());
            }
        });
        return valueSelect;
    }

    private void deleteSelectedInternalOrders() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (TechnicalOperationsDto technicalOperationsDto : grid.getSelectedItems()) {
                technicalOperationsService.deleteById(technicalOperationsDto.getId());
                notifications.infoNotification("Выбранные заказы успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные заказы");
        }
    }

    private List<TechnicalOperationsDto> getData() {
        return technicalOperationsService.getAll();
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

    private H2 getTextOrder() {
        final H2 textOrder = new H2("Технологические операции");
        textOrder.setHeight("2.2em");
        return textOrder;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}