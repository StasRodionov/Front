package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.invoice.InvoiceDto;
import com.trade_accounting.models.dto.warehouse.ShipmentDto;
import com.trade_accounting.models.dto.warehouse.ShipmentProductDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.trade_accounting.services.interfaces.util.ProjectService;
import com.trade_accounting.services.interfaces.warehouse.ShipmentProductService;
import com.trade_accounting.services.interfaces.warehouse.ShipmentService;
import com.trade_accounting.services.interfaces.units.UnitService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H4;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

@Slf4j
@Route(value = "shipment", layout = AppView.class)
@PageTitle("Отгрузки")
@SpringComponent
@UIScope
public class SalesSubShipmentView extends VerticalLayout {

    private final ProductService productService;
    ProjectService projectService;
    Notifications notifications;
    UnitService unitService;

    private final WarehouseService warehouseService;
    private final ShipmentService invoiceService;
    private final ContractorService contractorService;
    private final CompanyService companyService;
    private final SalesEditShipmentView salesEditShipmentView;
    private final List<ShipmentDto> data;
    private final ShipmentService shipmentService;
    private final ShipmentProductService shipmentProductService;
    private HorizontalLayout actions;
    private Grid<ShipmentDto> grid;
    private GridPaginator<ShipmentDto> paginator;

//    private final String typeOfInvoice = "RECEIPT";

    @Autowired
    public SalesSubShipmentView(WarehouseService warehouseService,
                                ShipmentService invoiceService,
                                ContractorService contractorService,
                                CompanyService companyService,
                                SalesEditShipmentView salesEditShipmentView,
                                ShipmentService shipmentService,
                                ShipmentProductService shipmentProductService,
                                ProductService productService) {
        this.warehouseService = warehouseService;
        this.invoiceService = invoiceService;
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.salesEditShipmentView = salesEditShipmentView;
        this.shipmentService = shipmentService;
        this.shipmentProductService = shipmentProductService;
        this.productService = productService;
        this.data = getData();

        configureActions();
        configureGrid();
        configurePaginator();

        add(actions, grid, paginator);
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
        grid = new Grid<>(ShipmentDto.class, false);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(dto -> formatDate(dto.getDate())).setHeader("Время")
                .setKey("date").setId("Дата");
        grid.addColumn(dto -> warehouseService.getById(dto.getWarehouseId()).getName()).setHeader("Со склада")
                .setKey("typeOfInvoiceDTO").setId("Склад");
        grid.addColumn(dto -> contractorService.getById(dto.getContractorId()).getName()).setHeader("Контрагент")
                .setKey("contractorId").setId("Контрагент");
        grid.addColumn(dto -> companyService.getById(dto.getCompanyId()).getName()).setHeader("Организация")
                .setKey("companyId").setId("Компания");
        grid.addColumn(this::getTotalPrice).setHeader("Сумма").setSortable(true);
        grid.addItemDoubleClickListener(e -> {
            ShipmentDto dto = e.getItem();
            SalesEditShipmentView modalView = new SalesEditShipmentView(productService,
                    contractorService,
                    companyService,
                    projectService,
                    warehouseService,
                    invoiceService,
                    notifications,
                    unitService,
                    shipmentProductService);
            modalView.setReturnToShiptmentForEdit(dto);
            modalView.open();
        });
        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

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
        paginator = new GridPaginator<>(grid, data, 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonUnit() {
        Button buttonUnit = new Button("Отгрузка", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickListener(event -> {
            salesEditShipmentView.resetView();
//            salesEditShipmentView.setType("RECEIPT");
            salesEditShipmentView.setLocation("sells");
            buttonUnit.getUI().ifPresent(ui -> ui.navigate("sells/shipment-edit"));
        });
        return buttonUnit;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        return buttonFilter;
    }

    private static String formatDate(String date) {
        return LocalDateTime.parse(date)
                .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
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
        textField.addValueChangeListener(event -> updateList());
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
        Select<String> select = new Select<>();
        select.setItems("Изменить");
        select.setValue("Изменить");
        select.setWidth("130px");
        return select;
    }

    private Select<String> valueStatus() {
        Select<String> status = new Select<>();
        status.setItems("Статус");
        status.setValue("Статус");
        status.setWidth("130px");
        return status;
    }

    private Select<String> valueCreate() {
        Select<String> create = new Select<>();
        create.setItems("Создать");
        create.setValue("Создать");
        create.setWidth("130px");
        return create;
    }

    private Select<String> valuePrint() {
        Select<String> print = new Select<>();
        print.setItems("Печать");
        print.setValue("Печать");
        print.setWidth("130px");
        return print;
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

//    private void updateList() {
//        grid.setItems(invoiceService.getAll(typeOfInvoice));
//        System.out.println("Обновлен");
//    }

//    private void updateList(String text) {
//        grid.setItems(invoiceService.findBySearchAndTypeOfInvoice(text, typeOfInvoice));
//    }

    private void updateList(){
        grid.setItems(shipmentService.getAll());
    }

    private List<ShipmentDto> getData() {
        return shipmentService.getAll();
    }
}
