package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridConfigurer;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.Action;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.warehouse.InventarizationDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.util.ColumnsMaskService;
import com.trade_accounting.services.interfaces.warehouse.InventarizationService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.Component;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static com.trade_accounting.config.SecurityConstants.GOODS_INVENTORY_VIEW;
import static com.trade_accounting.config.SecurityConstants.GRID_GOODS_MAIN_INVENTORY;

@Slf4j
//Если на страницу не ссылаются по URL или она не является отдельной страницей, а подгружается родительским классом, то URL и Title не нужен
/*@Route(value = GOODS_INVENTORY_VIEW, layout = AppView.class)
@PageTitle("Инвентаризации")*/
@SpringComponent
@UIScope
public class GoodsSubInventory extends VerticalLayout implements AfterNavigationObserver {

    private final TextField textField = new TextField();
    private final MenuBar selectXlsTemplateButton = new MenuBar();
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final InventarizationService inventarizationService;
    private final Grid<InventarizationDto> grid = new Grid<>(InventarizationDto.class, false);
    private final GridConfigurer<InventarizationDto> gridConfigurer;
    private final GridPaginator<InventarizationDto> paginator;
    private final Notifications notifications;
    private final GoodsSubInventoryModalWindow modalWindow;
    private final GridFilter<InventarizationDto> filter;
    private final List<InventarizationDto> data;
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES,
            GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};

    @Autowired
    public GoodsSubInventory(WarehouseService warehouseService,
                             CompanyService companyService, InventarizationService inventarizationService,
                             ColumnsMaskService columnsMaskService,
                             Notifications notifications, GoodsSubInventoryModalWindow modalWindow) {
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.inventarizationService = inventarizationService;
        this.notifications = notifications;
        this.modalWindow = modalWindow;
        this.data = getData();
        this.gridConfigurer = new GridConfigurer<>(grid, columnsMaskService, GRID_GOODS_MAIN_INVENTORY);
        paginator = new GridPaginator<>(grid, data, 50);
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        add(configureActions(), filter, grid, paginator);
        setSizeFull();
    }

    private List<InventarizationDto> getData() {
        return inventarizationService.getAll();
    }

    private HorizontalLayout configureActions() {
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
        dialog.add(new Text("Инвентаризация позволяет выявить несоответствие фактических " +
                "и учетных остатков. Проводится по каждому складу отдельно. Для изменения " +
                "расчетных остатков вам необходимо создать документ оприходования или списания."));
        dialog.setWidth("400px");
        dialog.setHeight("250px");
        buttonQuestion.addClickListener(event -> dialog.open());
        Shortcuts.addShortcutListener(dialog, dialog::close, Key.ESCAPE);
        dialog.add(new Div(cancelButton));
        return buttonQuestion;
    }


    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        buttonRefresh.addClickListener(e -> updateList());
        return buttonRefresh;
    }

    public void updateList() {
        grid.setItems(inventarizationService.getAll());
    }

    private Button buttonUnit() {

        Button button = new Button("Инвентаризация", new Icon(VaadinIcon.PLUS_CIRCLE));
        button.addClickListener(e -> modalWindow.open());
        updateList();
        return button;
    }

    private Button buttonFilter() {
        return new Button("Фильтр", clickEvent -> {
            filter.setVisible(!filter.isVisible());
        });
    }

    private TextField text() {
        textField.setWidth("300px");
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setClearButtonVisible(true);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(e -> updateList(textField.getValue()));
        setSizeFull();
        return textField;
    }

    public void updateList(String search) {
        if (search.isEmpty()) {
            paginator.setData(inventarizationService.getAll());
        } else paginator.setData(inventarizationService.search(search));
    }

    private NumberField numberField() {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect() {
        return SelectConfigurer.configureDeleteSelect(() -> {
            deleteSelectedInventarizations();
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

    private H2 getTextOrder() {
        final H2 textOrder = new H2("Инвентаризации");
        textOrder.setHeight("2.2em");
        return textOrder;
    }

    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(InventarizationDto::getDate).setHeader("Дата и время")
                .setKey("date")
                .setId("Дата и время");
        grid.addColumn(inventarizationDto -> warehouseService.getById(inventarizationDto.getWarehouseId())
                        .getName()).setHeader("Со склада")
                .setKey("warehouseId")
                .setId("Со склада");
        grid.addColumn(inventarizationDto -> companyService.getById(inventarizationDto.getCompanyId())
                        .getName()).setHeader("Организация")
                .setKey("companyId")
                .setId("Организация");
        grid.addColumn(new ComponentRenderer<>(this::getIsSentIcon)).setHeader("Отправлено")
                .setKey("sent")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsPrintIcon)).setHeader("Напечатано")
                .setKey("print")
                .setId("Напечатано");
        grid.addColumn("comment").setHeader("Комментарий")
                .setId("Комментарий");
        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        gridConfigurer.addConfigColumnToGrid();

        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addItemDoubleClickListener(e -> {
            InventarizationDto inventarizationDto = e.getItem();
            GoodsSubInventoryModalWindow modalWindow = new GoodsSubInventoryModalWindow(
                    inventarizationService,
                    warehouseService,
                    companyService,
                    notifications
            );
            modalWindow.setInventarizationEdit(inventarizationDto);
            modalWindow.open();
        });
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("warehouseId", WarehouseDto::getName, warehouseService.getAll());
        filter.setFieldToComboBox("companyId", CompanyDto::getName, companyService.getAll());
        filter.setFieldToComboBox("sent", Boolean.TRUE, Boolean.FALSE);
        filter.setFieldToComboBox("print", Boolean.TRUE, Boolean.FALSE);
        filter.onSearchClick(e -> paginator.setData(inventarizationService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(inventarizationService.getAll()));
    }

    private Component getIsSentIcon(InventarizationDto inventarizationDto) {
        if (inventarizationDto.getStatus()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component getIsPrintIcon(InventarizationDto inventarizationDto) {
        if (inventarizationDto.getStatus()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private void deleteSelectedInventarizations() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (InventarizationDto inventarizationDto : grid.getSelectedItems()) {
                inventarizationService.deleteById(inventarizationDto.getId());
                notifications.infoNotification("Выбранные заказы успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные заказы");
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }

}
