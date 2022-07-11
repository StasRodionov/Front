package com.trade_accounting.components.sells;

import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridConfigurer;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.invoice.IssuedInvoiceDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.invoice.IssuedInvoiceService;
import com.trade_accounting.services.interfaces.finance.PaymentService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
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
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.List;

@Slf4j
//Если на страницу не ссылаются по URL или она не является отдельной страницей, а подгружается родительским классом, то URL и Title не нужен
/*@Route(value = SELLS_ISSUED_INVOICE_VIEW, layout = AppView.class)
@PageTitle("Счета-фактуры выданные")*/
@SpringComponent
@UIScope
public class SalesSubIssuedInvoicesView extends VerticalLayout implements AfterNavigationObserver {

    private final IssuedInvoiceService issuedInvoiceService;
    private final CompanyService companyService;
    private final ContractorService contractorService;
    private final PaymentService paymentService;
    private List<IssuedInvoiceDto> data;

    private final GridFilter<IssuedInvoiceDto> filter;
    private final Grid<IssuedInvoiceDto> grid = new Grid<>(IssuedInvoiceDto.class, false);
    private final GridConfigurer<IssuedInvoiceDto> gridConfigurer = new GridConfigurer<>(grid);
    private final GridPaginator<IssuedInvoiceDto> paginator;
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};

    public SalesSubIssuedInvoicesView(IssuedInvoiceService issuedInvoiceService, CompanyService companyService, ContractorService contractorService, PaymentService paymentService) {
        this.issuedInvoiceService = issuedInvoiceService;
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.paymentService = paymentService;
        this.data = this.issuedInvoiceService.getAll();
        this.filter = new GridFilter<>(grid);
        add(upperLayout(), filter);
        this.paginator = new GridPaginator<>(grid, data, 100);
        configureGrid();
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);
        add(grid, paginator);
    }

    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.removeAllColumns();
        grid.addColumn("id").setWidth("30px").setHeader("№").setId("№");
        grid.addColumn(dto -> formatDate(dto.getDate())).setHeader("Дата и время").setId("Дата и время");
        grid.addColumn(issuedInvoiceDto -> contractorService.getById(issuedInvoiceDto.getContractorId()).getName())
                .setHeader("Контрагент").setId("Контрагент");
        grid.addColumn(issuedInvoiceDto -> companyService.getById(issuedInvoiceDto.getCompanyId()).getName()).setHeader("Организация")
                .setId("Организация");
        grid.addColumn((issuedInvoiceDto -> paymentService.getById(issuedInvoiceDto.getPaymentId()).getSum())).setHeader("Сумма")
                .setTextAlign(ColumnTextAlign.END)
                .setId("Сумма");
        grid.addColumn(new ComponentRenderer<>(this::isSendCheckedIcon)).setHeader("Отправлено")
                .setKey("send").setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::isPrintCheckedIcon)).setHeader("Напечатано")
                .setKey("print").setId("Напечатано");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");
        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        gridConfigurer.addConfigColumnToGrid();

        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        GridSortOrder<IssuedInvoiceDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
    }

    private Component isSendCheckedIcon(IssuedInvoiceDto issuedInvoiceDto) {
        if (issuedInvoiceDto.getIsSend()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component isPrintCheckedIcon(IssuedInvoiceDto issuedInvoiceDto) {
        if (issuedInvoiceDto.getIsPrint()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private static String formatDate(String date) {
        return LocalDateTime.parse(date)
                .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonFilter(), textField(), numberField(), getSelect(), getStatus(),getPrint(),buttonSettings());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion("Выданные счета-фактуры создаются на основе отгрузки. " +
                "Счета-фактуры можно распечатывать или отправлять покупателям в виде файлов.");
    }

    private H4 title() {
        H4 title = new H4("Счета-фактуры выданные");
        title.setHeight("2.2em");
        title.setWidth("80px");
        return title;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(e -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private void updateList() {
        GridPaginator<IssuedInvoiceDto> paginatorUpdateList
                = new GridPaginator<>(grid, issuedInvoiceService.getAll(), 100);
        GridSortOrder<IssuedInvoiceDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(upperLayout(), grid, paginatorUpdateList);
    }

    private Button buttonSettings() {
        Button buttonSettings = new Button(new Icon(VaadinIcon.COG_O));
        return buttonSettings;
    }

    private Button buttonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
    }

    private TextField textField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        return textField;
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> getSelect() {
        return SelectConfigurer.configureBulkEditAndDeleteSelect();
        //select.setItems("Изменить", "Удалить", "Массовое редактирование", "Провести", "Снять проведение");
    }

    private Select<String> getStatus() {
        return SelectConfigurer.configureStatusSelect();
//        status.setItems("Статус", "Настроить");
    }

    private Select<String> getPrint() {
//        getPrint.setItems("Печать", "Взаиморасчеты");
        return SelectConfigurer.configurePrintSelect();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
