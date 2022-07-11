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
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.invoice.InvoiceService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
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
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Map;

import static com.trade_accounting.config.SecurityConstants.SELLS_AGENT_REPORTS_VIEW;

@Slf4j
@Route(value = SELLS_AGENT_REPORTS_VIEW, layout = AppView.class)
@PageTitle("Отчеты комиссионера")
@SpringComponent
@UIScope
public class SalesSubAgentReportsView extends VerticalLayout implements AfterNavigationObserver {

    private final InvoiceService invoiceService;
    private final ContractorService contractorService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final List<InvoiceDto> data;
    private final Notifications notifications;
    private final ContractService contractService;
    private HorizontalLayout actions;
    private Grid<InvoiceDto> grid = new Grid<>(InvoiceDto.class, false);
    private final GridConfigurer<InvoiceDto> gridConfigurer = new GridConfigurer<>(grid);
    private final GridPaginator<InvoiceDto> paginator;
    private CommissionAgentReportModalView commissionAgentReportModalView;

    private final GridFilter<InvoiceDto> filter;

    private final String typeOfInvoice = "RECEIPT";
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};

    public SalesSubAgentReportsView(InvoiceService invoiceService,
                                    ContractorService contractorService,
                                    CompanyService companyService,
                                    WarehouseService warehouseService,
                                    CommissionAgentReportModalView commissionAgentReportModalView,
                                    Notifications notifications, ContractService contractService) {
        this.invoiceService = invoiceService;
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.commissionAgentReportModalView = commissionAgentReportModalView;
        this.notifications = notifications;
        this.contractService = contractService;

        this.data = getData();
        paginator = new GridPaginator<>(grid, data, 50);
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        add(configureActions(), filter, grid, paginator);
    }


    private void configureFilter() {

        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("spend", Boolean.TRUE, Boolean.FALSE);
        filter.setFieldToComboBox("companyDto", CompanyDto::getName, companyService.getAll());
        filter.setFieldToComboBox("contractorDto", ContractorDto::getName, contractorService.getAll());
        filter.onSearchClick(e -> {
            Map<String, String> map = filter.getFilterData2();
            paginator.setData(invoiceService.search(map));
        });

        filter.onClearClick(e -> paginator.setData(getData()));

    }

    private HorizontalLayout configureActions() {
        HorizontalLayout actions1 = new HorizontalLayout();
        actions1.add(buttonQuestion(), title(), buttonRefresh(), buttonUnit(), buttonFilter(), textField(),
                numberField(), valueSelect(), valueStatus(), valueCreate(), valuePrint(), buttonSettings());
        actions1.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return actions1;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("В разделе представлены выданные и полученные отчеты комиссионера. " +
                        "В отчетах указываются проданные товары, сумма продажи, вознаграждение комиссионера. " +
                        "На основе отчетов формируется долг комиссионера перед комитентом. " +
                        "Выданные отчеты создает комиссионер. Полученные — комитент. " +
                        "Читать инструкции: "),
                new Anchor("#", "Комиссионная торговля. Комиссионеру"),
                new Text(", "),
                new Anchor("#", "Комиссионная торговля. Комитенту"));
    }

    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(dto -> formatDate(dto.getDate())).setHeader("Дата и время")
                .setKey("date").setId("Дата и время");
        grid.addColumn(dto -> companyService.getById(dto.getCompanyId()).getName()).setHeader("Счет-фактура")
                .setKey("typeOfInvoice").setId("Счет-фактура");
        grid.addColumn(dto -> companyService.getById(dto.getCompanyId()).getName()).setHeader("Компания")
                .setKey("companyDto").setId("Компания");
        grid.addColumn(dto -> contractorService.getById(dto.getContractorId()).getName()).setHeader("Контрагент")
                .setKey("contractorDto").setId("Контрагент");
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedIcon)).setHeader("Проведена")
                .setKey("spend").setId("Проведена");
        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        gridConfigurer.addConfigColumnToGrid();

        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addItemClickListener(event -> {
            InvoiceDto editInvoice = event.getItem();
            CommissionAgentReportModalView commissionAgentReportModalView = new CommissionAgentReportModalView(
                    contractorService,
                    contractService,
                    companyService);
            commissionAgentReportModalView.setReturnEdit(editInvoice);
            commissionAgentReportModalView.open();
//            salesEditCreateInvoiceView.setUpdateState(true);
//            salesEditCreateInvoiceView.setType("RECEIPT");
//            salesEditCreateInvoiceView.setLocation(SELLS);
//            UI.getCurrent().navigate(SELLS_SELLS__CUSTOMER_ORDER_EDIT);
        });
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

    private static String formatDate(String date) {
        return LocalDateTime.parse(date)
                .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    public Button buttonUnit() {
        Button buttonUnit = new Button("Отчет комиссионера", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickListener(event -> {
            commissionAgentReportModalView.open();

        });
        return buttonUnit;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
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
        textField.setPlaceholder("Номер или компания");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> updateList(textField.getValue()));
        textField.setWidth("300px");
        return textField;
    }

    private H4 title() {
        H4 title = new H4("Отчеты комиссионера");
        title.setHeight("2.2em");
        title.setWidth("80px");
        return title;
    }

    private Select<String> valueSelect() {
        return SelectConfigurer.configureDeleteSelect(() -> {
            deleteSelectedInvoices();
            grid.deselectAll();
            paginator.setData(getData());
        });
    }

    private void deleteSelectedInvoices() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (InvoiceDto invoiceDto : grid.getSelectedItems()) {
                invoiceService.deleteById(invoiceDto.getId());
                notifications.infoNotification("Выбранные отчеты успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные отчеты");
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

    private void updateList(String text) {
        grid.setItems(invoiceService.getAll());
        System.out.println("Обновлен");
    }

    private void updateList() {
        GridPaginator<InvoiceDto> paginatorUpdateList
                = new GridPaginator<>(grid, invoiceService.getAll(), 50);
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(configureActions(), grid, paginator);
    }

    private List<InvoiceDto> getData() {
        return invoiceService.getAll();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
