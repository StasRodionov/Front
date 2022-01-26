package com.trade_accounting.components.production;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.ProductionTargetsDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ProductionTargetsService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
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
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringComponent
@UIScope
@PageTitle("Производственные Задания")
@Route(value = "productionTargets", layout = AppView.class)
public class ProductionTargetsViewTab extends VerticalLayout implements AfterNavigationObserver {

    private final TextField textField = new TextField();
    private final GridFilter<ProductionTargetsDto> filter;
    private final List<ProductionTargetsDto> data;
    private final GridPaginator<ProductionTargetsDto> paginator;
    private final Grid<ProductionTargetsDto> grid = new Grid<>(ProductionTargetsDto.class, false);
    private final ProductionTargetsService productionTargetsService;
    private final Notifications notifications;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;

    ProductionTargetsViewTab(ProductionTargetsService productionTargetsService,
                             Notifications notifications,
                             CompanyService companyService,
                             WarehouseService warehouseService) {
        this.productionTargetsService = productionTargetsService;
        this.notifications = notifications;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.data = getData();
        this.paginator = new GridPaginator<>(grid, this.productionTargetsService.getAll(), 100);
        setSizeFull();
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(getToolBar(), filter, grid, paginator);

    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(ProductionTargetsDto::getDate).setKey("date").setHeader(" Время").setSortable(true).setId("Дата");
        grid.addColumn("description").setHeader("Комментарий").setId("Комментарий");
        grid.addColumn(e -> companyService.getById(e.getCompanyId()).getName()).setHeader("Организация").setId("Организация");
        grid.addColumn("productionStart").setHeader("Начало производства").setId("Начало производства");
        grid.addColumn("productionEnd").setHeader("Завершение производства").setId("Завершение производства");
        grid.addColumn(new ComponentRenderer<>(this::getIsSentIcon)).setKey("sent").setHeader("Отправлено").setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsPrintIcon)).setKey("print").setHeader("Напечатано").setId("Напечатано");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addItemDoubleClickListener(e -> {
            ProductionTargetsDto productionTargetsDto = e.getItem();
            ProductionTargetsModalWindow modalWindow = new ProductionTargetsModalWindow(
                    companyService,
                    warehouseService,
                    productionTargetsService,
                    notifications);
//           modalWindow.setProductionTargetsEdit(productionTargetsDto);
           modalWindow.open();
        });
    }


    private Component getIsSentIcon(ProductionTargetsDto productionTargetsDto) {
        if (productionTargetsDto.getPublished()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component getIsPrintIcon(ProductionTargetsDto productionTargetsDto) {
        if (productionTargetsDto.getPrinted()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private HorizontalLayout getToolBar() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(buttonQuestion(), getTextOrder(), buttonRefresh(), buttonAddProductionTarget(),
                buttonFilter(), text(), numberField(), valueSelect(), valueStatus(),
                valuePrint(), buttonSettings());
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new VerticalLayout(
                        new Text("Производственное задание позволяет выстроить " +
                                "процесс изготовления продукции по техкартам и рассчитать расходы. Тут же можно отмечать " +
                                "выполнение производственных этапов и контролировать результаты."),
                        new Div(
                                new Text("Читать инструкцию: "),
                                new Anchor("#", "Расширенный учет производственных операций")),
                        new Div(
                                new Text("Видео: "),
                                new Anchor("#", "Расширенный способ"))
                )
        );
    }

    private H2 getTextOrder() {
        final H2 textOrder = new H2("Производственные задания");
        textOrder.setHeight("2.2em");
        return textOrder;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(ev -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private void updateList() {
        grid.setItems(productionTargetsService.getAll());

    }

    private Button buttonAddProductionTarget() {
        Button addProductionTargetButton = new Button("Задание", new Icon(VaadinIcon.PLUS_CIRCLE));
        addProductionTargetButton.addClickListener(e -> {
            ProductionTargetsModalWindow modalWindow = new ProductionTargetsModalWindow(
                    companyService,
                    warehouseService,
                    productionTargetsService,
                    notifications
            );
            modalWindow.open();
        });
        updateList();
        return addProductionTargetButton;
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
                paginator.setData(productionTargetsService.searchProductionTargets(filter.getFilterData())));
        filter.onClearClick(e ->
                paginator.setData(productionTargetsService.getAll()));
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
            grid.setItems(productionTargetsService.search(textField.getValue()));
        } else {
            grid.setItems(productionTargetsService.search("null"));
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
            for (ProductionTargetsDto productionTargetsDto : grid.getSelectedItems()) {
                productionTargetsService.deleteById(productionTargetsDto.getId());
                notifications.infoNotification("Выбранные заказы успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные заказы");
        }
    }

    private List<ProductionTargetsDto> getData() {
        return productionTargetsService.getAll();
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

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
