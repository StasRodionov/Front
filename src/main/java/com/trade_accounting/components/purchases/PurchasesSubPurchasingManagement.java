package com.trade_accounting.components.purchases;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.PurchaseControlDto;
import com.trade_accounting.models.dto.SupplierAccountDto;
import com.trade_accounting.services.interfaces.ProductPriceService;
import com.trade_accounting.services.interfaces.PurchaseControlService;
import com.trade_accounting.services.interfaces.PurchaseHistoryOfSalesService;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Route(value = "purchasingManagement", layout = AppView.class)
@PageTitle("Управление закупками")
@SpringComponent
@UIScope
public class PurchasesSubPurchasingManagement extends VerticalLayout implements AfterNavigationObserver {


    private final PurchaseControlService purchaseControlService;
    private final Notifications notifications;
    private final SupplierAccountModalView modalView;
    private final TextField textField = new TextField();
    private final ProductPriceService productPriceService;
    private final PurchaseHistoryOfSalesService purchaseHistoryOfSalesService;


    private List<PurchaseControlDto> purchaseControl;


    private HorizontalLayout actions;
    private final Grid<PurchaseControlDto> grid = new Grid<>(PurchaseControlDto.class, false);
    private GridPaginator<PurchaseControlDto> paginator;
    private final GridFilter<PurchaseControlDto> filter;

    private final String textForQuestionButton = "<div><p>Раздел позволяет проанализировать продажи и на основе этих " +
            "данных сформировать заказ поставщику.</p>" +
            "<p>Общий заказ создается без указания поставщика — добавить его" +
            "можно позже. Если разбить заказы по поставщикам, будет создано" +
            "несколько заказов на поставщиков, указанных в карточках" +
            "товаров.</p></div>";

    @Autowired
    public PurchasesSubPurchasingManagement(PurchaseControlService purchaseControlService,
                                            @Lazy Notifications notifications,
                                            SupplierAccountModalView modalView,
                                            ProductPriceService productPriceService,
                                            PurchaseHistoryOfSalesService purchaseHistoryOfSalesService
    ) {
        this.purchaseControlService = purchaseControlService;
        this.notifications = notifications;
        this.modalView = modalView;
        this.productPriceService = productPriceService;
        this.purchaseHistoryOfSalesService = purchaseHistoryOfSalesService;

        loadSupplierAccounts();
        configureActions();
        configureGrid();
        configurePaginator();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        add(actions, filter, grid, paginator);
    }

    private List<PurchaseControlDto> loadSupplierAccounts() {
        purchaseControl = purchaseControlService.getAll();
        return purchaseControl;
    }

    private void configureActions() {
        actions = new HorizontalLayout();
        actions.add(Buttons.buttonQuestion(textForQuestionButton, "300px"), title(), buttonRefresh(), buttonFilter(), filterTextField(),
                numberField(), valueSelect(), valueStatus(), valueCreate(), valuePrint(), buttonSettings());
        actions.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    }

    private Grid<PurchaseControlDto> configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        grid.addColumn("id").setWidth("20px").setHeader("№").setId("№");
        grid.addColumn(PurchaseControlDto::getProductName).setHeader("Наименование").setKey("product_name").setSortable(true)
                .setId("Наименование");
        grid.addColumn(PurchaseControlDto::getArticleNumber).setHeader("Артикул").setKey("article_number").setSortable(true)
                .setId("Артикул");
        grid.addColumn(PurchaseControlDto::getProductMeasure).setHeader("Единица измерения").setKey("product_measure").setSortable(true)
                .setId("Единица_измерения");
        grid.addColumn(PurchaseControlDto::getProductQuantity).setHeader("Количество").setKey("product_quantity").setSortable(true)
                .setId("Количество");

        grid.addColumn(dto -> purchaseHistoryOfSalesService.getById(dto.getHistoryOfSalesId()).getSumOfProducts())
                .setHeader("Сумма").setKey("sum_of_products").setId("Сумма");
        grid.addColumn(dto -> productPriceService.getById(dto.getHistoryOfSalesId()).getValue())
                .setHeader("Себестоимость").setKey("product_price").setId("Себестоимость");
        grid.addColumn(dto -> purchaseHistoryOfSalesService.getById(dto.getHistoryOfSalesId()).getProductMargin())
                .setHeader("Прибыль").setKey("product_margin").setId("Прибыль");
        grid.addColumn(dto -> purchaseHistoryOfSalesService.getById(dto.getHistoryOfSalesId()).getProductProfitMargin())
                .setHeader("Рентабельность").setKey("product_profit_margin").setId("Рентабельность");
        grid.addColumn(dto -> purchaseHistoryOfSalesService.getById(dto.getHistoryOfSalesId()).getProductSalesPerDay())
                .setHeader("Продаж в день").setKey("product_sales_per_day").setId("Продаж в день");

        HeaderRow groupingHeader2 = grid.prependHeaderRow();

        groupingHeader2.
                join(
                        groupingHeader2.getCell(grid.getColumnByKey("sum_of_products")),
                        groupingHeader2.getCell(grid.getColumnByKey("product_price")),
                        groupingHeader2.getCell(grid.getColumnByKey("product_margin")),
                        groupingHeader2.getCell(grid.getColumnByKey("product_profit_margin")),
                        groupingHeader2.getCell(grid.getColumnByKey("product_sales_per_day"))
                )
                .setComponent(new Label("История продаж"));

        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        return grid;
    }

    private void configurePaginator() {
        paginator = new GridPaginator<>(grid, purchaseControl, 100);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.onSearchClick(e -> paginator.setData(purchaseControlService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(purchaseControlService.getAll()));
    }

    private H4 title() {
        H4 title = new H4("Управление закупками");
        title.setHeight("2.2em");
        title.setWidth("100px");
        return title;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        buttonRefresh.addClickListener(e -> updateList());
        return buttonRefresh;
    }

//    private Button buttonUnit() {
//        Button buttonUnit = new Button("Счёт", new Icon(VaadinIcon.PLUS_CIRCLE));
//        buttonUnit.addClickListener(e -> modalView.open());
//        return buttonUnit;
//    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private TextField filterTextField() {
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addValueChangeListener(e -> updateList(textField.getValue()));
        textField.setWidth("300px");
        return textField;
    }

    public void updateList(String nameFilter) {
        if (!(textField.getValue().equals(""))) {

        } else {

        }
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect() {
        Select<String> select = new Select<>();
        List<String> stringList = new ArrayList<>();
        stringList.add("Изменить");
        stringList.add("Удалить");
        select.setItems(stringList);
        select.setValue("Изменить");
        select.setWidth("130px");
        select.addValueChangeListener(event -> {
            if (select.getValue().equals("Удалить")) {
                deleteSelectedInvoices();
                grid.deselectAll();
                select.setValue("Изменить");
                paginator.setData(loadSupplierAccounts());
            }
        });
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

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }


    private Component getIsCheckedIcon(SupplierAccountDto supplierAccountDto) {
        if (supplierAccountDto.getIsSpend()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private void updateList() {
        grid.setItems(purchaseControlService.getAll());
    }


    public void deleteSelectedInvoices() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (PurchaseControlDto supp : grid.getSelectedItems()) {
                purchaseControlService.deleteById(supp.getId());
                notifications.infoNotification("");
            }
        } else {
            notifications.errorNotification("");
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}




