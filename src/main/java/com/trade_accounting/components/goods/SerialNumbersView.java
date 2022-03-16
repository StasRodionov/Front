package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.warehouse.ProductDto;
import com.trade_accounting.models.dto.warehouse.SerialNumbersDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.trade_accounting.services.interfaces.warehouse.SerialNumbersService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.ArrayList;
import java.util.List;

@SpringComponent
@Route(value = "serialNumbersView", layout = AppView.class)
@PageTitle("Серийные номера")
@UIScope
public class SerialNumbersView extends VerticalLayout {

    private final SerialNumbersService serialNumbersService;
    private final ProductService productService;
    private final WarehouseService warehouseService;

    private final Grid<SerialNumbersDto> grid = new Grid<>(SerialNumbersDto.class, false);
    private final GridFilter<SerialNumbersDto> filter;
    private final GridPaginator<SerialNumbersDto> paginator;
    private final List<SerialNumbersDto> data;

    public SerialNumbersView(SerialNumbersService serialNumbersService, ProductService productService, WarehouseService warehouseService, List<SerialNumbersDto> data) {
        this.serialNumbersService = serialNumbersService;
        this.productService = productService;
        this.warehouseService = warehouseService;
        this.paginator = new GridPaginator<>(grid, data, 100);
        this.data = getData();
        configureGrid();
        setSizeFull();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(getTollBar(), filter, grid, paginator);

    }

    private HorizontalLayout getTollBar() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(buttonQuestion(), getTextOrder(), buttonRefresh(),
                buttonFilter(),  getPrint(), buttonSettings());
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn("code").setHeader("Код").setId("Код");
        grid.addColumn("vendorCode").setHeader("Артикул").setId("Артикул");
        grid.addColumn(dto -> productService.getById(dto.getProductId()).getName()).setKey("productId").setHeader("Продукт").setId("Продукт");
        grid.addColumn(dto -> warehouseService.getById(dto.getWarehouseId()).getName()).setKey("warehouseId").setHeader("Склад").setId("Склад");
        grid.addColumn("typeDocument").setHeader("Тип документа").setId("Тип документа");
        grid.addColumn("documentNumber").setHeader("№ документа").setId("№ документа");
        grid.addColumn("description").setHeader("Описание").setId("Описание");

    }

    private List<SerialNumbersDto> getData() {
        return new ArrayList<>(serialNumbersService.getAll());
    }

    private H2 getTextOrder() {
        final H2 textOrder = new H2("Серийные номера");
        textOrder.setHeight("2.2em");
        return textOrder;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion("В разделе представлены остатки товаров на складах. " +
                "Для каждого товара показан его фактический остаток, зарезервированный объем и доступное для продажи количество.\n" +
                "\n" +
                "Начальные остатки можно внести с помощью оприходования или импорта. На остатки также влияют документы приемки, отгрузки и списания. " +
                "Нажмите на строку с товаром — откроется список всех документов, которые повлияли на остатки по данному товару.\n" +
                "\n" +
                "Читать инструкцию: Остатки\n" +
                "\n" +
                "Видео: Закупка и продажа товаров");
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

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToComboBox("productId", ProductDto::getName, productService.getAll());
        filter.setFieldToComboBox("warehouseId", WarehouseDto::getName, warehouseService.getAll());
        filter.onSearchClick(e ->
                paginator.setData(serialNumbersService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(getData()));
    }

    private Button buttonSettings() {
        Button buttonSettings = new Button(new Icon(VaadinIcon.COG_O));
        return buttonSettings;
    }

    private Select<String> getPrint() {
        return SelectConfigurer.configurePrintSelect();
    }
}