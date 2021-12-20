package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Route(value = "agentReports", layout = AppView.class)
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

    private HorizontalLayout actions;
    private Grid<InvoiceDto> grid = new Grid<>(InvoiceDto.class, false);
    private final GridPaginator<InvoiceDto> paginator;
    private CommissionAgentReportModalView commissionAgentReportModalView;

    private final GridFilter<InvoiceDto> filter;

    private final String typeOfInvoice = "RECEIPT";

    private final String textForQuestionButton = "<div><p>В разделе представлены выданные и полученные отчеты комиссионера." +
            "В отчетах указываются проданные товары, сумма продажи, вознаграждение комиссионера." +
            "На основе отчетов формируется долг комиссионера перед комитентом.</p>" +
            "<p>Выданные отчеты создает комиссионер. Полученные — комитент.</p>" +
            "<p>Читать инструкцию: <a href=\"#\" target=\"_blank\">Комиссионная торговля. Комиссионеру</a></p>" +
            "<a href=\"#\" target=\"_blank\">Комиссионная торговля. Комитенту</a></p></div>";

    public SalesSubAgentReportsView(InvoiceService invoiceService,
                                    ContractorService contractorService,
                                    CompanyService companyService,
                                    WarehouseService warehouseService,
                                    CommissionAgentReportModalView commissionAgentReportModalView,
                                    Notifications notifications) {
        this.invoiceService = invoiceService;
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.commissionAgentReportModalView = commissionAgentReportModalView;
        this.notifications = notifications;

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
        actions1.add(Buttons.buttonQuestion(textForQuestionButton, "350px"), title(), buttonRefresh(), buttonUnit(), buttonFilter(), textField(),
                numberField(), valueSelect(), valueStatus(), valueCreate(), valuePrint(), buttonSettings());
        actions1.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return actions1;
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn("id").setHeader("id").setId("id");
        grid.addColumn(dto -> formatDate(dto.getDate())).setHeader("Время")
                .setKey("date").setId("Дата");
        grid.addColumn(dto -> companyService.getById(dto.getCompanyId()).getName()).setHeader("Счет-фактура")
                .setKey("typeOfInvoice").setId("Счет-фактура");
        grid.addColumn(dto -> companyService.getById(dto.getCompanyId()).getName()).setHeader("Компания")
                .setKey("companyDto").setId("Компания");
        grid.addColumn(dto -> contractorService.getById(dto.getContractorId()).getName()).setHeader("Контрагент")
                .setKey("contractorDto").setId("Контрагент");
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedIcon)).setKey("spend").setHeader("Проведена")
                .setId("Проведена");
        grid.setHeight("66vh");

        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);


        grid.addItemDoubleClickListener(event -> {
//            InvoiceDto editInvoice = event.getItem();
//            salesEditCreateInvoiceView.setInvoiceDataForEdit(editInvoice);
//            salesEditCreateInvoiceView.setUpdateState(true);
//            salesEditCreateInvoiceView.setType("RECEIPT");
//            salesEditCreateInvoiceView.setLocation("sells");
//            UI.getCurrent().navigate("sells/customer-order-edit");
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
        Select<String> select = new Select<>();
        List<String> listItems = new ArrayList<>();
        listItems.add("Изменить");
        listItems.add("Удалить");
        select.setItems(listItems);
        select.setValue("Изменить");
        select.setWidth("130px");
        select.addValueChangeListener(event -> {
            if (select.getValue().equals("Удалить")) {
                deleteSelectedInvoices();
                grid.deselectAll();
                select.setValue("Изменить");
                paginator.setData(getData());
            }
        });
        return select;
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
