package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.warehouse.ProductDto;
import com.trade_accounting.models.dto.warehouse.SalesSubGoodsForSaleDto;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.trade_accounting.services.interfaces.warehouse.SalesSubGoodsForSaleService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Route(value = "goodsForSale", layout = AppView.class)
@PageTitle("Товары на реализации")
@SpringComponent
@UIScope
public class SalesSubGoodsForSaleView extends VerticalLayout {

    private final SalesSubGoodsForSaleService salesSubGoodsForSaleService;
    private final List<SalesSubGoodsForSaleDto> data;
    private final ProductService productService;
    private final Grid<SalesSubGoodsForSaleDto> grid = new Grid<>(SalesSubGoodsForSaleDto.class, false);
    private final GridFilter<SalesSubGoodsForSaleDto> filter;
    GridPaginator<SalesSubGoodsForSaleDto> paginator;

    public SalesSubGoodsForSaleView(SalesSubGoodsForSaleService salesSubGoodsForSaleService, ProductService productService) {
        this.salesSubGoodsForSaleService = salesSubGoodsForSaleService;
        this.productService = productService;
        configureGrid();

        this.data = salesSubGoodsForSaleService.getAll();
        this.paginator = new GridPaginator<>(grid, data, 50 );
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);

        this.filter = new GridFilter<>(grid);
        configureFilter();

        add(upperLayout(), grid, filter, paginator);
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(),
                configurationSubMenu(), buttonFilter(), valuePrint());
        upper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upper;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("В разделе представлены товары, переданные и принятые на реализацию по договору комиссии." +
                        "Здесь можно создать возврат на весь нереализованный остаток товаров и выданный отчет " +
                        "комиссионера по всем проданным товарам, по которым вы еще не отчитались." +
                        "Читать инструкцию: "),
                new Anchor("#", "Товары на реализации"));
    }

    private H4 title() {
        H4 title = new H4("Товары на реализации");
        title.setHeight("2.5em");
        title.setWidth("150px");
        return title;
    }

    private Button buttonRefresh() {
        final Button buttonRefresh;
        Icon circle = new Icon(VaadinIcon.REFRESH);
        buttonRefresh = new Button();
        buttonRefresh.setIcon(circle);
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }


    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private Select<String> valuePrint() {
        Select<String> print = new Select<>();
        print.setItems("Печать", "Добавить шаблон");
        print.setValue("Печать");
//        getXlsFile().forEach(x -> print.add(getLinkToSalesXls(x)));
//        uploadXlsTemplates(print);
        print.setWidth("130px");
        return print;
    }

    private List<SalesSubGoodsForSaleDto> getData() {
        return salesSubGoodsForSaleService.getAll();
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn(dto -> productService.getById(dto.getProductId()).getName()).setFlexGrow(2).setHeader("Наименование")
                .setKey("productId").setId("Наименование");
        grid.addColumn(SalesSubGoodsForSaleDto::getCode).setKey("code").setHeader("Код").setId("Код");
        grid.addColumn(SalesSubGoodsForSaleDto::getVendorCode).setKey("vendorCode").setHeader("Артикул").setId("Артикул");
        grid.addColumn(SalesSubGoodsForSaleDto::getTransferred).setKey("transferred").setHeader("Передано").setId("Передано");
        grid.addColumn(SalesSubGoodsForSaleDto::getAccepted).setKey("accepted").setHeader("Принято").setId("Принято");
        Grid.Column<SalesSubGoodsForSaleDto> amountColumn = grid
                .addColumn(SalesSubGoodsForSaleDto::getAmount).setHeader("Количество");
        Grid.Column<SalesSubGoodsForSaleDto> sumColumn = grid
                .addColumn(SalesSubGoodsForSaleDto::getSum).setHeader("Сумма");
        grid.addColumn(SalesSubGoodsForSaleDto::getReturned).setHeader("Вернули");
        grid.addColumn(SalesSubGoodsForSaleDto::getRemainder).setHeader("Остаток");
        Grid.Column<SalesSubGoodsForSaleDto> amountColumn1 = grid
                .addColumn(SalesSubGoodsForSaleDto::getReportAmount).setHeader("Количество");
        Grid.Column<SalesSubGoodsForSaleDto> sumColumn1 = grid
                .addColumn(SalesSubGoodsForSaleDto::getNotReportSum).setHeader("Сумма");
        Grid.Column<SalesSubGoodsForSaleDto> amountColumn2 = grid
                .addColumn(SalesSubGoodsForSaleDto::getNotReportAmount).setHeader("Количество");
        Grid.Column<SalesSubGoodsForSaleDto> sumColumn2 = grid
                .addColumn(SalesSubGoodsForSaleDto::getNotReportSum).setHeader("Сумма");

        HeaderRow headerRow = grid.prependHeaderRow();
        headerRow.join(amountColumn, sumColumn).setText("Продано/Списано");
        headerRow.join(amountColumn1, sumColumn1).setText("Отчитались");
        headerRow.join(amountColumn2, sumColumn2).setText("Не отчитались");
    }

    private void configureFilter() {
        filter.setFieldToComboBox("productId", ProductDto::getName, productService.getAll());
        filter.setFieldToIntegerField("code");
        filter.setFieldToIntegerField("vendorCode");
        filter.setFieldToIntegerField("transferred");
        filter.setFieldToIntegerField("accepted");
        filter.onSearchClick(e ->
                paginator.setData(salesSubGoodsForSaleService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(getData()));
    }

    private Tabs configurationSubMenu() {
        Tab adopted = new Tab("Принятые");
        Tab transferred = new Tab("Переданные");
        Tabs tabs = new Tabs(adopted, transferred);

        tabs.addSelectedChangeListener(event -> {
            String name = event.getSelectedTab().getLabel();
            if ("Принятые".equals(name)) {
                grid.removeAllColumns();
                grid.setItems(data);
                grid.addColumn(SalesSubGoodsForSaleDto::getProductId).setFlexGrow(2).setHeader("Наименование");
                grid.addColumn(SalesSubGoodsForSaleDto::getCode).setHeader("Код");
                grid.addColumn(SalesSubGoodsForSaleDto::getVendorCode).setHeader("Артикул");
                grid.addColumn(SalesSubGoodsForSaleDto::getAccepted).setHeader("Принято");
                Grid.Column<SalesSubGoodsForSaleDto> amountColumn = grid
                        .addColumn(SalesSubGoodsForSaleDto::getAmount).setHeader("Количество");
                Grid.Column<SalesSubGoodsForSaleDto> sumColumn = grid
                        .addColumn(SalesSubGoodsForSaleDto::getSum).setHeader("Сумма");
                grid.addColumn(SalesSubGoodsForSaleDto::getReturned).setHeader("Вернули");
                grid.addColumn(SalesSubGoodsForSaleDto::getRemainder).setHeader("Остаток");
                Grid.Column<SalesSubGoodsForSaleDto> amountColumn1 = grid
                        .addColumn(SalesSubGoodsForSaleDto::getReportAmount).setHeader("Количество");
                Grid.Column<SalesSubGoodsForSaleDto> sumColumn1 = grid
                        .addColumn(SalesSubGoodsForSaleDto::getNotReportSum).setHeader("Сумма");
                Grid.Column<SalesSubGoodsForSaleDto> amountColumn2 = grid
                        .addColumn(SalesSubGoodsForSaleDto::getNotReportAmount).setHeader("Количество");
                Grid.Column<SalesSubGoodsForSaleDto> sumColumn2 = grid
                        .addColumn(SalesSubGoodsForSaleDto::getNotReportSum).setHeader("Сумма");

                HeaderRow headerRow = grid.prependHeaderRow();
                headerRow.join(amountColumn, sumColumn).setText("Продано/Списано");
                headerRow.join(amountColumn1, sumColumn1).setText("Отчитались");
                headerRow.join(amountColumn2, sumColumn2).setText("Не отчитались");
                add(grid, paginator);
            } else if ("Переданные".equals(name)) {
                grid.removeAllColumns();
                grid.setItems(data);
                grid.addColumn(SalesSubGoodsForSaleDto::getProductId).setFlexGrow(2).setHeader("Наименование");
                grid.addColumn(SalesSubGoodsForSaleDto::getCode).setHeader("Код");
                grid.addColumn(SalesSubGoodsForSaleDto::getVendorCode).setHeader("Артикул");
                grid.addColumn(SalesSubGoodsForSaleDto::getTransferred).setHeader("Передано");
                Grid.Column<SalesSubGoodsForSaleDto> amountColumn1 = grid
                        .addColumn(SalesSubGoodsForSaleDto::getReportAmount).setHeader("Количество");
                Grid.Column<SalesSubGoodsForSaleDto> sumColumn1 = grid
                        .addColumn(SalesSubGoodsForSaleDto::getNotReportSum).setHeader("Сумма");
                grid.addColumn(SalesSubGoodsForSaleDto::getReturned).setHeader("Вернули");
                grid.addColumn(SalesSubGoodsForSaleDto::getRemainder).setHeader("Остаток");

                HeaderRow headerRow = grid.prependHeaderRow();
                headerRow.join(amountColumn1, sumColumn1).setText("Отчитались");
                add(grid, paginator);
            }
        });
        return tabs;
    }
}