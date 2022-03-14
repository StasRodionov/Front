package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.Action;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.invoice.InternalOrderDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.invoice.InternalOrderProductsDtoService;
import com.trade_accounting.services.interfaces.invoice.InternalOrderService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Route(value = "internalorder", layout = AppView.class)
@PageTitle("Внутренние заказы")
@SpringComponent
@UIScope
public class GoodsSubInternalOrder extends VerticalLayout implements AfterNavigationObserver {
    private final TextField textField = new TextField();
    private final MenuBar selectXlsTemplateButton = new MenuBar();
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final InternalOrderService internalOrderService;
    private final InternalOrderProductsDtoService internalOrderProductsDtoService;
    private final ProductService productService;
    private final Grid<InternalOrderDto> grid = new Grid<>(InternalOrderDto.class, false);
    private final GridPaginator<InternalOrderDto> paginator;
    private final GridFilter<InternalOrderDto> filter;
    private final Notifications notifications;
    private final InternalOrderModalView modalView;
    private final GoodsModalWindow goodsModalWindow;
    private final TitleForModal titleForEdit;
    private final TitleForModal titleForСreate;
    private final TitleForModal titleForEditSelected;

    @Autowired
    public GoodsSubInternalOrder(CompanyService companyService,
                                 WarehouseService warehouseService,
                                 InternalOrderService internalOrderService,
                                 Notifications notifications, InternalOrderModalView modalView, GoodsModalWindow goodsModalWindow,
                                 InternalOrderProductsDtoService internalOrderProductsDtoService, ProductService productService
    ) {
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.internalOrderService = internalOrderService;
        this.internalOrderProductsDtoService = internalOrderProductsDtoService;
        this.productService = productService;
        this.modalView = modalView;
        this.goodsModalWindow = goodsModalWindow;
        this.notifications = notifications;
        titleForEdit = new TitleForModal("Редактирование внутреннего заказа");
        titleForСreate = new TitleForModal("Добавление внутреннего заказа");
        titleForEditSelected = new TitleForModal("Массовое редактирование заказов");

        List<InternalOrderDto> data = getData();
        paginator = new GridPaginator<>(grid, data, 50);
        configureGrid();
        this.filter = new GridFilter<>(this.grid);
        configureFilter();
        add(configureActions(), filter, grid, paginator);
        setSizeFull();
    }

    private void configureFilter() {
        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("sent", Boolean.TRUE, Boolean.FALSE);
        filter.setFieldToComboBox("print", Boolean.TRUE, Boolean.FALSE);
        filter.setFieldToComboBox("warehouse", WarehouseDto::getName,warehouseService.getAll());
        filter.setFieldToComboBox("company", CompanyDto::getName,companyService.getAll());

        filter.onSearchClick(e -> {
            Map<String, String> map = filter.getFilterData2();
            paginator.setData(internalOrderService.searchByFilter(map).stream()
                    .filter(dto -> !map.containsKey("totalPrice") || getTotalPrice(dto).compareTo(new BigDecimal(map.get("totalPrice"))) == 0)
                    .filter(dto -> !map.containsKey("totalAmount") || getTotalAmount(dto).compareTo(new BigDecimal(map.get("totalAmount"))) == 0)
                    .collect(Collectors.toList()));
        });
        filter.onClearClick(e -> paginator.setData(getData()));
    }

    private List<InternalOrderDto> getData() {
        return internalOrderService.getAll();
    }

    private HorizontalLayout configureActions() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(buttonQuestion(), getTextOrder(), buttonRefresh(), buttonUnit(),
                buttonFilter(), text(), numberField(), valueSelect(), valueStatus(),
                valuePrint(), buttonSettings(), selectXlsTemplateButton);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn("id").setWidth("1px").setHeader("№").setId("№");
        grid.addColumn(InternalOrderDto::getDate).setKey("date").setWidth("1px").setHeader("Время").setSortable(true).setId("Дата");
        grid.addColumn(internalOrderDto -> companyService.getById(internalOrderDto.getCompanyId())
                .getName()).setKey("company").setHeader("Организация").setId("Организация");
        grid.addColumn(internalOrderDto -> warehouseService.getById(internalOrderDto.getWarehouseId())
                .getName()).setKey("warehouse").setWidth("1px").setHeader("На склад").setId("На склад");
        grid.addColumn(new ComponentRenderer<>(this::getIsSentIcon)).setWidth("1px").setWidth("1px").setKey("sent").setHeader("Отправлено")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsPrintIcon)).setWidth("1px").setKey("print").setHeader("Напечатано")
                .setId("Напечатано");
        grid.addColumn("comment").setWidth("1px").setHeader("Комментарий").setId("Комментарий");
        grid.addColumn(this::getTotalPriceString).setWidth("1px").setHeader("Сумма").setKey("totalPrice").setSortable(true).setId("Сумма");
        grid.addColumn(this::getTotalAmountString).setWidth("1px").setHeader("Количество").setKey("totalAmount").setSortable(true).setId("Количество");

        grid.addColumn(internalOrderDto -> (internalOrderDto.getInternalOrderProductsIds()).stream()
                        .map(internalOrderProductsDtoService::getById)
                        .map(map -> productService.getById(map.getProductId()).getName())
                        .collect(Collectors.toList()))
                .setHeader("Товары внутреннего заказа").setId("Товары внутреннего заказа");

        grid.setHeight("66vh");
        grid.setMaxWidth("2500px");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addItemDoubleClickListener(e -> {
            InternalOrderDto dto = e.getItem();
            InternalOrderModalView modalView = new InternalOrderModalView(
                    companyService,
                    warehouseService,
                    internalOrderService,
                    notifications,
                    internalOrderProductsDtoService,
                    productService,
                    titleForEdit
            );
            modalView.setInternalOrderForEdit(dto);
            modalView.open();
        });
    }

    private BigDecimal getTotalPrice(InternalOrderDto dto) {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        List<Long> list = dto.getInternalOrderProductsIds();
        for (Long id : list) {
            totalPrice = totalPrice.add(internalOrderProductsDtoService.getById(id).getPrice());
        }
        return totalPrice;
    }

    private String getTotalPriceString(InternalOrderDto dto) {
        return String.format("%.2f", getTotalPrice(dto));
    }

    private BigDecimal getTotalAmount(InternalOrderDto dto) {
        BigDecimal totalAmount = BigDecimal.valueOf(0.0);
        List<Long> list = dto.getInternalOrderProductsIds();
        for (Long id : list) {
            totalAmount = totalAmount.add(internalOrderProductsDtoService.getById(id).getAmount());
        }
        return totalAmount;
    }

    private String getTotalAmountString(InternalOrderDto dto) {
        return String.format("%.2f", getTotalAmount(dto));
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Dialog dialog = new Dialog();
        Button cancelButton = new Button("Закрыть", event -> dialog.close());
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponentAsFirst(cancelButton);
        dialog.add(new Text("Внутренние заказы позволяют планировать закупки " +
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

    public void updateList() {
        grid.setItems(internalOrderService.getAll());
    }

    private Button buttonUnit() {
        Button buttonUnit = new Button("Внутренний заказ", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickListener(e -> {

            InternalOrderModalView modalView = new InternalOrderModalView(
                    companyService,
                    warehouseService,
                    internalOrderService,
                    notifications,
                    internalOrderProductsDtoService,
                    productService,
                    titleForСreate
            );
            modalView.open();
        });
        return buttonUnit;
    }

    private Button buttonFilter() {
        Button button = new Button("Фильтр");
        button.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return button;
    }

    private TextField text() {
        textField.setWidth("300px");
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setClearButtonVisible(true);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(e -> updateTextField());
        setSizeFull();

        return textField;
    }

    private void updateTextField() {

        if (!(textField.getValue().equals(""))) {
            grid.setItems(internalOrderService.searchByTerm(textField.getValue()));
        } else {
            grid.setItems(internalOrderService.searchByTerm("null"));
        }
    }

    private NumberField numberField() {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect() {
        Action onBulkEdit = () -> {
            editSelectedInternalOrders();
            grid.deselectAll();
            paginator.setData(getData());
        };
        Action onDelete = () -> {
            deleteSelectedInternalOrders();
            grid.deselectAll();
            paginator.setData(getData());
        };
        return SelectConfigurer.configureBulkEditAndDeleteSelect(
                onBulkEdit,
                onDelete
        );

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
        final H2 textOrder = new H2("Внутренние заказы");
        textOrder.setHeight("2.2em");
        return textOrder;
    }

    private Component getIsSentIcon(InternalOrderDto internalOrderDto) {
        if (internalOrderDto.getIsSent()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component getIsPrintIcon(InternalOrderDto internalOrderDto) {
        if (internalOrderDto.getIsPrint()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private void deleteSelectedInternalOrders() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (InternalOrderDto internalOrderDto : grid.getSelectedItems()) {
                internalOrderService.deleteById(internalOrderDto.getId());
                notifications.infoNotification("Выбранные заказы успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные заказы");
        }
    }

    private void editSelectedInternalOrders() {
        if (!grid.getSelectedItems().isEmpty()) {
            Set<InternalOrderDto> list = grid.getSelectedItems();
            EditSelectedModalWindow editSelectedModalWindow = new EditSelectedModalWindow(
                    companyService,
                    warehouseService,
                    internalOrderService,
                    notifications,
                    list
            );

            editSelectedModalWindow.open();
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные заказы");
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
