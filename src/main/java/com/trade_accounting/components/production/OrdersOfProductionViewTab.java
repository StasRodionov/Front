package com.trade_accounting.components.production;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.OrdersOfProductionDto;
import com.trade_accounting.models.dto.TechnicalOperationsDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.OrdersOfProductionService;
import com.trade_accounting.services.interfaces.TechnicalCardService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
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
@PageTitle("Заказы на производство")
@Route(value = "ordersOfProductionViewTab", layout = AppView.class)
public class OrdersOfProductionViewTab extends VerticalLayout implements AfterNavigationObserver {


        private final TextField textField = new TextField();
        private final MenuBar selectXlsTemplateButton = new MenuBar();

    private final GridPaginator<OrdersOfProductionDto> paginator;
    private final Grid<OrdersOfProductionDto> grid = new Grid<>(OrdersOfProductionDto.class, false);
    private final OrdersOfProductionService ordersOfProductionService;
    private final CompanyService companyService;
    private final TechnicalCardService technicalCardService;
    private final List<OrdersOfProductionDto> data;
    private GridFilter<OrdersOfProductionDto> filter;
    private final Notifications notifications;
    private final OrdersOfProductionModalWindow modalWindow;

    OrdersOfProductionViewTab(OrdersOfProductionService ordersOfProductionService, CompanyService companyService, TechnicalCardService technicalCardService, Notifications notifications, OrdersOfProductionModalWindow modalWindow) {
            this.ordersOfProductionService = ordersOfProductionService;
            this.companyService = companyService;
        this.technicalCardService = technicalCardService;
        this.notifications = notifications;
        this.modalWindow = modalWindow;
        paginator = new GridPaginator<>(grid, this.ordersOfProductionService.getAll(), 100);
        add(getTollBar(), grid, paginator);
        configureGrid();
        setSizeFull();
        //configureFilter();
        this.data = getData();
        this.filter = new GridFilter<>(grid);
    }

    private void configureGrid () {
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(OrdersOfProductionDto::getDate).setKey("date").setHeader("время").setSortable(true);
        grid.addColumn(e -> companyService.getById(e.getCompanyId()).getName()).setHeader("организация").setId("организация");
        grid.addColumn(t -> technicalCardService.getById(t.getTechnicalCardId()).getName()).setHeader("Технологическая карта")
                .setId("Технологическая карта");
        grid.addColumn("volume").setHeader("Объем производства").setId("Объем производства");
        grid.addColumn("produce").setHeader("Произведено").setId("Произведено");
        grid.addColumn(OrdersOfProductionDto::getPlannedProductionDate).setKey("PlannedProductionDate")
                .setHeader("План. дата производства").setSortable(true);
        grid.addColumn(new ComponentRenderer<>(this::getIsSentIcon)).setKey("sent").setHeader("Отправлено")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsPrintIcon)).setKey("print").setHeader("Напечатано")
                .setId("Напечатано");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");

        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addItemDoubleClickListener(e -> {
            OrdersOfProductionDto dto = e.getItem();
            OrdersOfProductionModalWindow modalWindow = new OrdersOfProductionModalWindow(
            technicalCardService,
            companyService,
            ordersOfProductionService,
            notifications
            );
           modalWindow.setOrdersOfProductionEdit(dto);
           modalWindow.open();
        });



    }

    private Component getIsSentIcon(OrdersOfProductionDto ordersOfProductionDto) {
        if (ordersOfProductionDto.getIsSent()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component getIsPrintIcon(OrdersOfProductionDto ordersOfProductionDto) {
        if (ordersOfProductionDto.getIsPrint()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private List<OrdersOfProductionDto> getData() {
        return ordersOfProductionService.getAll();
    }

        private HorizontalLayout getTollBar () {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(buttonQuestion(), getTextOrder(), buttonRefresh(), buttonUnit(),
                buttonFilter(), text(), numberField(), valueSelect(), valueStatus(),
                valuePrint(), buttonSettings(), selectXlsTemplateButton);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

        private Button buttonQuestion () {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

        private Button buttonRefresh () {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(ev -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

        private void updateList () {
            grid.setItems(ordersOfProductionService.getAll());
    }

        private Button buttonUnit () {
        Button buttonUnit = new Button("Заказ", new Icon(VaadinIcon.PLUS_CIRCLE));
            buttonUnit.addClickListener(e -> modalWindow.open());
            updateList();
        return buttonUnit;
    }

        private Button buttonFilter () {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.onSearchClick(e ->
                paginator.setData(ordersOfProductionService.searchOrdersOfProduction(filter.getFilterData())));
        filter.onClearClick(e ->
                paginator.setData(ordersOfProductionService.getAll()));
    }

        private TextField text () {
        textField.setWidth("300px");
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setClearButtonVisible(true);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        setSizeFull();
        return textField;
    }

        private NumberField numberField () {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

        private Select<String> valueSelect () {
            Select<String> valueSelect = new Select<>();
            List<String> list = new ArrayList<>();
            list.add("Изменить");
            list.add("Удалить");
            valueSelect.setItems(list);
            valueSelect.setValue("Изменить");
            valueSelect.setWidth("120px");
            valueSelect.addValueChangeListener(event -> {
                if (valueSelect.getValue().equals("Удалить")) {
                    deleteSelectedOrdersOfProduction();
                    grid.deselectAll();
                    valueSelect.setValue("Изменить");
                    paginator.setData(getData());
                }
            });
            return valueSelect;
    }

    private void deleteSelectedOrdersOfProduction() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (OrdersOfProductionDto ordersOfProductionDto : grid.getSelectedItems()) {
                ordersOfProductionService.deleteById(ordersOfProductionDto.getId());
                notifications.infoNotification("Выбранные заказы успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные заказы");
        }
    }

        private Button buttonSettings () {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

        private Select<String> valueStatus () {
        Select<String> status = new Select<>();
        status.setItems("Статус");
        status.setValue("Статус");
        status.setWidth("110px");
        return status;
    }

        private Select<String> valuePrint () {
        Select<String> print = new Select<>();
        print.setItems("Печать", "Добавить шаблон");
        print.setValue("Печать");
        print.setWidth("110px");
        return print;
    }

        private H2 getTextOrder () {
        final H2 textOrder = new H2("Заказы на производство");
        textOrder.setHeight("2.2em");
        return textOrder;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
