package com.trade_accounting.components.production;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.TechnicalCardDto;
import com.trade_accounting.models.dto.TechnicalOperationsDto;
import com.trade_accounting.services.interfaces.TechnicalCardService;
import com.trade_accounting.services.interfaces.TechnicalOperationsService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
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

    private final GridFilter<TechnicalOperationsDto> filter;

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
        this.technicalCardService = technicalCardService;
        this.data = getData();
        paginator = new GridPaginator<>(grid, this.technicalOperationsService.getAll(), 100);
        setSizeFull();
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(getTollBar(), filter, grid, paginator);

    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(TechnicalOperationsDto::getDate).setKey("date").setHeader("время").setSortable(true).setId("Дата");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");
        grid.addColumn(e -> warehouseService.getById(e.getWarehouse()).getName()).setHeader("организация").setId("организация");
        grid.addColumn(t -> technicalCardService.getById(t.getTechnicalCard()).getName()).setHeader("Технологическая карта").setId("Технологическая карта");
        grid.addColumn("volume").setHeader("Объем производства").setId("Объем производства");
        grid.addColumn(new ComponentRenderer<>(this::getIsSentIcon)).setKey("sent").setHeader("Отправлено")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsPrintIcon)).setKey("print").setHeader("Напечатано")
                .setId("Напечатано");



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
        Dialog modal = new Dialog();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Html content = new Html("<div><p>Технологические операции позволяют регистрировать в системе операции по сборке и производству." +
                " В результате списываются материалы и добавляются готовые изделия.</p>" +
                "<p>Читать инструкцию: </p><p><a href=\"#\" target=\"_blank\">Сборочные и производственные операции</a></p>" +
                "<p>Видео: </p><p><a href=\"#\" target=\"_blank\">Возможности для производства</a></p></div>");
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
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.onSearchClick(e ->
                paginator.setData(technicalOperationsService.searchTechnicalOperations(filter.getFilterData())));
        filter.onClearClick(e ->
                paginator.setData(technicalOperationsService.getAll()));
    }

    private TextField text() {
        textField.setWidth("300px");
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setClearButtonVisible(true);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(e -> updateListTextField());
        setSizeFull();
        return textField;
    }

    public void updateListTextField() {
        if (!(textField.getValue().equals(""))) {
            grid.setItems(technicalOperationsService.search(textField.getValue()));
        } else {
            grid.setItems(technicalOperationsService.search("null"));
        }
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
                deleteSelectedTechnicalOperations();
                grid.deselectAll();
                valueSelect.setValue("Изменить");
                paginator.setData(getData());
            }
        });
        return valueSelect;
    }

    private void deleteSelectedTechnicalOperations() {
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