package com.trade_accounting.components.production;

import com.trade_accounting.components.AppView;
//import com.trade_accounting.components.purchases.print.PrintInvoicesXls;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridConfigurer;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.Action;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.production.TechnicalOperationsDto;
import com.trade_accounting.services.interfaces.production.TechnicalCardService;
import com.trade_accounting.services.interfaces.production.TechnicalOperationsService;
import com.trade_accounting.services.interfaces.util.ColumnsMaskService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
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
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.trade_accounting.config.SecurityConstants.GRID_PRODUCTION_MAIN_TECHNOLOGICAL_OPERATIONS;
import static com.trade_accounting.config.SecurityConstants.PRODUCTION_TECHNOLOGICAL_VIEW;


@SpringComponent
@UIScope
@PageTitle("Тех. операции")
@Route(value = PRODUCTION_TECHNOLOGICAL_VIEW, layout = AppView.class)
public class TechnologicalOperationsViewTab extends VerticalLayout implements AfterNavigationObserver {

    private final TextField textField = new TextField();
    private final MenuBar selectXlsTemplateButton = new MenuBar();

    private final GridFilter<TechnicalOperationsDto> filter;
    private final List<TechnicalOperationsDto> data;
    private final GridPaginator<TechnicalOperationsDto> paginator;
    private final Grid<TechnicalOperationsDto> grid = new Grid<>(TechnicalOperationsDto.class, false);
    private final GridConfigurer<TechnicalOperationsDto> gridConfigurer;
    private final TechnicalCardService technicalCardService;
    private final TechnicalOperationsService technicalOperationsService;
    private final Notifications notifications;
    private final WarehouseService warehouseService;
    private final TechnologicalOperationsModalView view;
    private final MenuItem print;
    private final String pathForSaveXlsTemplate = "src/main/resources/xls_templates/technologicalOperations_templates/";
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES,
            GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};

    TechnologicalOperationsViewTab(TechnicalCardService technicalCardService, TechnicalOperationsService technicalOperationsService,
                                   Notifications notifications, WarehouseService warehouseService,
                                   TechnologicalOperationsModalView view, ColumnsMaskService columnsMaskService) {
        this.technicalOperationsService = technicalOperationsService;
        this.notifications = notifications;
        this.warehouseService = warehouseService;
        this.view = view;
        this.technicalCardService = technicalCardService;
        this.data = getData();
        this.print = selectXlsTemplateButton.addItem("Печать");
        this.gridConfigurer = new GridConfigurer<>(grid, columnsMaskService, GRID_PRODUCTION_MAIN_TECHNOLOGICAL_OPERATIONS);
        paginator = new GridPaginator<>(grid, this.technicalOperationsService.getAll(), 100);
        setSizeFull();
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(getTollBar(), filter, grid, paginator);
        valuePrint();
    }

    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(TechnicalOperationsDto::getDate).setHeader("Дата и время").setKey("date").setId("Дата и время");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");
        grid.addColumn(e -> warehouseService.getById(e.getWarehouse()).getName()).setHeader("Организация").setId("Организация");
        grid.addColumn(t -> technicalCardService.getById(t.getTechnicalCard()).getName()).setHeader("Технологическая карта")
                .setId("Технологическая карта");
        grid.addColumn("volume").setHeader("Объем производства").setTextAlign(ColumnTextAlign.END)
                .setId("Объем производства");
        grid.addColumn(new ComponentRenderer<>(this::getIsSentIcon)).setKey("sent").setHeader("Отправлено")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsPrintIcon)).setKey("print").setHeader("Напечатано")
                .setId("Напечатано");
        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        gridConfigurer.addConfigColumnToGrid();

        grid.setHeight("64vh");
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
                buttonFilter(), text(), numberField(), valueSelect(), valueStatus(), selectXlsTemplateButton, buttonSettings());

        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new VerticalLayout(
                        new Text("Технологические операции позволяют регистрировать в системе операции по сборке и производству. " +
                                "В результате списываются материалы и добавляются готовые изделия."),
                        new Div(
                                new Text("Читать инструкцию: "),
                                new Anchor("#", "Сборочные и производственные операции")),
                        new Div(
                                new Text("Видео: "),
                                new Anchor("#", "Возможности для производства"))
                )
        );
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
        return SelectConfigurer.configureDeleteSelect(() -> {
            deleteSelectedTechnicalOperations();
            grid.deselectAll();
            paginator.setData(getData());
        });
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
        return SelectConfigurer.configureStatusSelect();
    }

    private void valuePrint() {
        SubMenu printSubMenu = print.getSubMenu();
        printSubMenu.removeAll();
        templatesXlsMenuItems(printSubMenu);
        uploadXlsMenuItem(printSubMenu);

    }

    private List<File> getXlsFiles() {
        File dir = new File(pathForSaveXlsTemplate);
        return Arrays.stream(Objects.requireNonNull(dir.listFiles())).filter(File::isFile).filter(x -> x.getName()
                .contains(".xls")).collect(Collectors.toList());
    }

    private Anchor getLinkToXlsTemplate(File file) {
        String templateName = file.getName();
        PrintTechnologicalOperationsXls printTechnologicalOperationsXls
                = new PrintTechnologicalOperationsXls(file.getPath(), technicalOperationsService.getAll(), warehouseService, technicalCardService);

        return new Anchor(new StreamResource(templateName, printTechnologicalOperationsXls::createReport), templateName);
    }

    private void templatesXlsMenuItems(SubMenu subMenu) {
        getXlsFiles().forEach(x -> subMenu.addItem(getLinkToXlsTemplate(x)));
    }

    private void uploadXlsMenuItem(SubMenu subMenu) {
        MenuItem menuItem = subMenu.addItem("добавить шаблон");

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