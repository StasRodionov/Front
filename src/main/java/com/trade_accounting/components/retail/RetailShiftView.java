package com.trade_accounting.components.retail;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.components.util.configure.components.select.SelectConstants;
import com.trade_accounting.components.util.configure.components.select.SelectExt;
import com.trade_accounting.models.dto.company.BankAccountDto;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.retail.RetailShiftDto;
import com.trade_accounting.models.dto.retail.RetailStoreDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.company.BankAccountService;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.retail.RetailShiftService;
import com.trade_accounting.services.interfaces.retail.RetailStoreService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
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
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static com.trade_accounting.config.SecurityConstants.RETAIL_RETAIL_SHIFT_VIEW;

@Slf4j
//Если на страницу не ссылаются по URL или она не является отдельной страницей, а подгружается родительским классом, то URL и Title не нужен
/*@Route(value = RETAIL_RETAIL_SHIFT_VIEW, layout = AppView.class)
@PageTitle("Смены")*/
@SpringComponent
@UIScope
public class RetailShiftView extends VerticalLayout implements AfterNavigationObserver {
    private final TextField textField = new TextField();
    private final RetailShiftService retailShiftService;
    private final RetailStoreService retailStoreService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final BankAccountService bankAccountService;
    private List<RetailShiftDto> data;

    private final GridFilter<RetailShiftDto> filter;
    private final Grid<RetailShiftDto> grid = new Grid<>(RetailShiftDto.class, false);
    private final GridPaginator<RetailShiftDto> paginator;

    public RetailShiftView(RetailShiftService retailShiftService, RetailStoreService retailStoreService,
                           WarehouseService warehouseService, CompanyService companyService,
                           BankAccountService bankAccountService) {
        this.retailShiftService = retailShiftService;
        this.data = retailShiftService.getAll();
        this.retailStoreService = retailStoreService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.bankAccountService = bankAccountService;
        this.paginator = new GridPaginator<>(grid, data, 100);
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);
        add(upperLayout(), filter, grid, paginator);
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.removeAllColumns();
        grid.addColumn("id").setWidth("30px").setHeader("№").setId("№");
        grid.addColumn(dto -> formatDate(dto.getDataOpen())).setKey("dataOpen").setFlexGrow(10).setHeader("Дата открытия")
                .setSortable(true).setId("Дата открытия");
        grid.addColumn(dto -> formatDate(dto.getDataClose())).setKey("dataClose").setFlexGrow(10).setHeader("Дата закрытия")
                .setSortable(true).setId("Дата закрытия");
//        grid.addColumn("dataOpen").setFlexGrow(10).setHeader("Дата открытия").setId("Дата открытия");
//        grid.addColumn("dataClose").setFlexGrow(10).setHeader("Дата закрытия").setId("Дата закрытия");
        grid.addColumn(dto -> retailStoreService.getById(dto.getRetailStoreId()).getName()).setFlexGrow(5)
                .setHeader("Точка продаж").setKey("retailStoreDto").setId("Точка продаж");
        grid.addColumn(dto -> warehouseService.getById(dto.getWarehouseId()).getName()).setFlexGrow(5)
                .setHeader("Склад").setKey("warehouseDto").setId("Склад");
        grid.addColumn(dto -> companyService.getById(dto.getCompanyId()).getName()).setFlexGrow(5)
                .setHeader("Организация").setKey("companyDto").setId("Организация");
        grid.addColumn("bank").setFlexGrow(5).setHeader("Банк-эквайер").setId("Банк-эквайер");
        grid.addColumn("revenuePerShift").setFlexGrow(5).setHeader("Выручка за смену").setId("Выручка за смену");
        grid.addColumn("received").setFlexGrow(5).setHeader("Поступило").setId("Поступило");
        grid.addColumn("amountOfDiscounts").setFlexGrow(5).setHeader("Сумма скидок").setId("Сумма скидок");
        grid.addColumn("commission_amount").setFlexGrow(5).setHeader("Сумма комиссии").setId("Сумма комиссии");
        grid.addColumn(new ComponentRenderer<>(this::isSentCheckedIcon)).setFlexGrow(10).setWidth("35px").setKey("sent")
                .setHeader("Отправлено").setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::isPrintedCheckedIcon)).setFlexGrow(10).setWidth("35px").setKey("printed")
                .setHeader("Напечатано").setId("Напечатано");
        grid.addColumn("comment").setFlexGrow(5).setHeader("Комментарий").setId("Комментарий");
        GridSortOrder<RetailShiftDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.setHeight("66vh");
        grid.sort(Arrays.asList(order));
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("dataOpen");
        filter.setFieldToDatePicker("dataClose");
        filter.setFieldToComboBox("retailStoreDto", RetailStoreDto::getName, retailStoreService.getAll());
        filter.setFieldToComboBox("warehouseDto", WarehouseDto::getName, warehouseService.getAll());
        filter.setFieldToComboBox("companyDto", CompanyDto::getName, companyService.getAll());
//        filter.setFieldToComboBox("bank", BankAccountDto::getBank, bankAccountService.getAll());
        filter.setFieldToCheckBox("sent");
        filter.setFieldToCheckBox("printed");
        filter.onSearchClick(e -> paginator.setData(retailShiftService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(retailShiftService.getAll()));
    }

    private Component isSentCheckedIcon(RetailShiftDto retailShiftDto) {
        if (retailShiftDto.getSent()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component isPrintedCheckedIcon(RetailShiftDto retailShiftDto) {
        if (retailShiftDto.getPrinted()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private String formatDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime formatDateTime = LocalDateTime.parse(stringDate, formatter);
        return formatDateTime.format(formatter);
    }

    private List<RetailShiftDto> getData() {
        return retailShiftService.getAll();
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonFilter(), numberField(), textField(), getSelect(), getStatus(), getPrint(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private H2 title() {
        H2 title = new H2("Смены");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("В отчете можно увидеть все продажи и возвраты за смену, " +
                        "а также задолженность банка по безналичной оплате. Розничные смены создаются автоматически " +
                        "при совершении первой продажи. " +
                        "Читать инструкции: "),
                new Anchor("https://support.moysklad.ru/hc/ru/articles/115004375987", "Открыть и закрыть смену в кассе"),
                new Text(", "),
                new Anchor("https://support.moysklad.ru/hc/ru/articles/203325423", "Розница"));
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(e -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
    }

    private void updateList() {
        GridPaginator<RetailShiftDto> paginatorUpdateList
                = new GridPaginator<>(grid, retailShiftService.getAll(), 100);
        GridSortOrder<RetailShiftDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(upperLayout(), grid, paginatorUpdateList);
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
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        textField.setClearButtonVisible(true);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(e -> updateListTextField());
        setSizeFull();
        return textField;
    }

    public void updateListTextField() {
        if (!(textField.getValue().equals(""))) {
            grid.setItems(retailShiftService.search(textField.getValue()));
        } else {
            grid.setItems(retailShiftService.search("null"));
        }
    }

    private Select<String> getSelect() {
        return new SelectExt.SelectBuilder<String>()
                .item(SelectConstants.CHANGE_SELECT_ITEM)
                .defaultValue(SelectConstants.CHANGE_SELECT_ITEM)
                .width(SelectConstants.SELECT_WIDTH_130PX)
                .build();
    }

    private Select<String> getStatus() {
        return SelectConfigurer.configureStatusSelect();
    }

    private Select<String> getPrint() {
        return SelectConfigurer.configurePrintSelect();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }

}
