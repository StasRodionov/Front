package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridConfigurer;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.components.util.configure.components.select.SelectConstants;
import com.trade_accounting.components.util.configure.components.select.SelectExt;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.invoice.InvoiceDto;
import com.trade_accounting.models.dto.warehouse.ShipmentDto;
import com.trade_accounting.models.dto.warehouse.ShipmentProductDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.units.SalesChannelService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.trade_accounting.services.interfaces.util.ProjectService;
import com.trade_accounting.services.interfaces.warehouse.ShipmentProductService;
import com.trade_accounting.services.interfaces.warehouse.ShipmentService;
import com.trade_accounting.services.interfaces.units.UnitService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
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
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.trade_accounting.config.SecurityConstants.*;

@Slf4j
@Route(value = SELLS_SHIPMENT_VIEW, layout = AppView.class)
@PageTitle("Отгрузки")
@SpringComponent
@UIScope
public class SalesSubShipmentView extends VerticalLayout implements AfterNavigationObserver {

    private final ProductService productService;
    ProjectService projectService;
    UnitService unitService;

    private final TextField textField = new TextField();
    private final WarehouseService warehouseService;
    private final ShipmentService invoiceService;
    private final ContractorService contractorService;
    private final CompanyService companyService;
    private final SalesEditShipmentView salesEditShipmentView;
    private final List<ShipmentDto> data;
    private final ShipmentService shipmentService;
    private final ShipmentProductService shipmentProductService;
    private final SalesChannelService salesChannelService;
    private HorizontalLayout actions;
    private final Grid<ShipmentDto> grid = new Grid<>(ShipmentDto.class, false);;
    private final GridConfigurer<ShipmentDto> gridConfigurer = new GridConfigurer<>(grid);
    private GridPaginator<ShipmentDto> paginator;
    private final GridFilter<ShipmentDto> filter;
    private final Notifications notifications;
    private final String typeOfInvoice = "RECEIPT";
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};

    @Autowired
    public SalesSubShipmentView(WarehouseService warehouseService,
                                ShipmentService invoiceService,
                                ContractorService contractorService,
                                CompanyService companyService,
                                SalesEditShipmentView salesEditShipmentView,
                                ShipmentService shipmentService,
                                ShipmentProductService shipmentProductService,
                                ProductService productService,
                                @Lazy Notifications notifications,
                                SalesChannelService salesChannelService) {
        this.warehouseService = warehouseService;
        this.invoiceService = invoiceService;
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.salesEditShipmentView = salesEditShipmentView;
        this.shipmentService = shipmentService;
        this.shipmentProductService = shipmentProductService;
        this.productService = productService;
        this.notifications = notifications;
        this.salesChannelService = salesChannelService;
        this.data = getData();

        configureActions();
        configureGrid();
        configurePaginator();

        this.filter = new GridFilter<>(grid);
        configureFilter();

        add(actions, filter, grid, paginator);
    }

    private void configureActions() {
        actions = new HorizontalLayout();
        actions.add(buttonQuestion(), title(), buttonRefresh(), buttonUnit(), buttonFilter(), textField(),
                numberField(), valueSelect(), valueStatus(), valueCreate(), valuePrint(), buttonSettings());
        actions.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("Отгрузки фиксируют отпуск товаров покупателям и влияют на остатки по складам в разделе Товары → Остатки. " +
                        "Товары отпускаются с указанного в документе склада. " +
                        "Если товары отпускаются с двух разных складов, то для каждого создается отдельная отгрузка. " +
                        "Отгрузку можно распечатать в виде расходной накладной, ТОРГ-12 или Товарно-транспортной накладной. " +
                        "На основе отгрузки создаются и печатаются счета-фактуры. " +
                        "Читать инструкцию: "),
                new Anchor("#", "Отгрузка товаров"));
    }

    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(ShipmentDto::getDate).setHeader("Дата и время")
                .setKey("date").setId("Дата и время");
        grid.addColumn(dto -> warehouseService.getById(dto.getWarehouseId()).getName()).setHeader("Со склада")
                .setKey("warehouseId").setId("Со склада");
        grid.addColumn(dto -> contractorService.getById(dto.getContractorId()).getName()).setHeader("Контрагент")
                .setKey("contractorId").setId("Контрагент");
        grid.addColumn(dto -> companyService.getById(dto.getCompanyId()).getName()).setHeader("Организация")
                .setKey("companyId").setId("Организация");
        grid.addColumn(this::getTotalPrice).setHeader("Сумма").setTextAlign(ColumnTextAlign.END)
                .setId("Сумма");
        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        gridConfigurer.addConfigColumnToGrid();

        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addItemClickListener(e -> {
            ShipmentDto dto = e.getItem();
            SalesEditShipmentView modalView = new SalesEditShipmentView(productService,
                    contractorService,
                    companyService,
                    projectService,
                    warehouseService,
                    invoiceService,
                    notifications,
                    unitService,
                    shipmentProductService, salesChannelService);
            modalView.setReturnToShiptmentForEdit(dto);
            UI.getCurrent().navigate(SELLS_SELLS__SHIPMENT_EDIT);
            //modalView.open();
        });
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("companyId", CompanyDto::getName, companyService.getAll());
        filter.setFieldToComboBox("contractorId", ContractorDto::getName, contractorService.getAll());
        filter.setFieldToComboBox("warehouseId", WarehouseDto::getName, warehouseService.getAll());
        filter.setFieldToIntegerField("id");
        filter.onSearchClick(e -> paginator
                .setData(invoiceService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(invoiceService.getAll(typeOfInvoice)));
    }

    private Component getIsCheckedIcon(InvoiceDto invoiceDto) {
        if (invoiceDto.getIsSpend()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private void configurePaginator() {
        paginator = new GridPaginator<>(grid, data, 50); //сделал 50
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        buttonRefresh.addClickListener(e -> updateList());
        return buttonRefresh;
    }

    private Button buttonUnit() {
        Button buttonUnit = new Button("Отгрузка", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickListener(event -> {
            salesEditShipmentView.resetView();
//            salesEditShipmentView.setType("RECEIPT");
            salesEditShipmentView.setLocation(SELLS);
            buttonUnit.getUI().ifPresent(ui -> ui.navigate(SELLS_SELLS__SHIPMENT_EDIT));
        });
        return buttonUnit;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private static String formatDate(String date) {
        return LocalDateTime.parse(date)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
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
        textField.setPlaceholder("Номер, склад или организация");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> updateList(textField.getValue()));
        textField.setWidth("300px");
        return textField;
    }

    private H4 title() {
        H4 title = new H4("Отгрузки");
        title.setHeight("2.2em");
        title.setWidth("80px");
        return title;
    }

    private Select<String> valueSelect() {
        return SelectConfigurer.configureDeleteSelect(() -> {
            deleteSelectedCorrections();
            grid.deselectAll();
            paginator.setData(getData());
        });
    }

    private void deleteSelectedCorrections() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (ShipmentDto shipmentDto : grid.getSelectedItems()) {
                shipmentService.deleteById(shipmentDto.getId());
                notifications.infoNotification("Выбранные счета-фактуры успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные счета-фактуры");
        }
    }

    private Select<String> valueStatus() {
        return SelectConfigurer.configureStatusSelect();
    }

    private Select<String> valueCreate() {
        return new SelectExt.SelectBuilder<String>()
                .item(SelectConstants.CREATE_SELECT_ITEM)
                .defaultValue(SelectConstants.CREATE_SELECT_ITEM)
                .width(SelectConstants.SELECT_WIDTH_130PX)
                .build();
    }

    private Select<String> valuePrint() {
        return SelectConfigurer.configurePrintSelect();
    }

    private String getTotalPrice(ShipmentDto shipmentDto) {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        for (Long id : shipmentDto.getShipmentProductsIds()) {
            ShipmentProductDto shipmentProductDto = shipmentProductService.getById(id);
            totalPrice = totalPrice.add(shipmentProductDto.getAmount()
                    .multiply(shipmentProductDto.getPrice()));
        }
        return String.format("%.2f", totalPrice);
    }

    public void updateList(String nameFilter) {
        if (nameFilter.isEmpty()) {
            grid.setItems(shipmentService.getAll());
        } else {
            grid.setItems(shipmentService.searchByString(nameFilter));
        }
    }

    private void updateList() {
        grid.setItems(shipmentService.getAll());
    }

    private List<ShipmentDto> getData() {
        return shipmentService.getAll();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
