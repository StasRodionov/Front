package com.trade_accounting.components.sells;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.company.ContractorStatusDto;
import com.trade_accounting.models.dto.finance.FunnelDto;
import com.trade_accounting.models.dto.invoice.InvoicesStatusDto;
import com.trade_accounting.services.interfaces.company.ContractorStatusService;
import com.trade_accounting.services.interfaces.finance.FunnelService;
import com.trade_accounting.services.interfaces.invoice.InvoicesStatusService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Route(value = "salesSubSalesFunnelView", layout = AppView.class)
@PageTitle("Воронка Продаж")
public class SalesSubSalesFunnelView extends VerticalLayout {
    private final ContractorStatusService contractorStatusService;
    private final InvoicesStatusService invoicesStatusService;
    private final FunnelService funnelService;

    private final List<FunnelDto> invoiceData;
    private final List<FunnelDto> contractorData;
    private final Grid<FunnelDto> invoiceGrid = new Grid<>(FunnelDto.class, false);
    private final Grid<FunnelDto> contractorGrid = new Grid<>(FunnelDto.class, false);
    private final GridPaginator<FunnelDto> invoicePaginator;
    private final GridPaginator<FunnelDto> contractorPaginator;
    private final GridFilter<FunnelDto> invoiceFilter;
    private final GridFilter<FunnelDto> contractorFilter;
    private final List<String> contractorStatuses;
    private final List<String> invoiceStatuses;
    private final Tab invoices = new Tab("По заказам");
    private final Tab contractors = new Tab("По контрагентам");
    private final Tabs tabs = new Tabs(invoices, contractors);
    private final MenuBar selectXlsTemplateButton = new MenuBar();
    private final MenuItem print;
    private final String pathForSaveXlsTemplate = "src/main/resources/xls_templates/sales_templates/funnel/";



    public SalesSubSalesFunnelView(ContractorStatusService contractorStatusService, InvoicesStatusService invoicesStatusService, FunnelService funnelService) {
        this.contractorStatusService = contractorStatusService;
        this.invoicesStatusService = invoicesStatusService;
        this.funnelService = funnelService;
        this.contractorStatuses = contractorStatusService.getAll().stream().map(ContractorStatusDto::getName).collect(Collectors.toList());
        this.invoiceStatuses = invoicesStatusService.getAll().stream().map(InvoicesStatusDto::getStatusName).collect(Collectors.toList());
        this.invoiceData = funnelService.getAllByType("invoice");
        this.contractorData = funnelService.getAllByType("contractor");
        print = selectXlsTemplateButton.addItem("Печать");
        invoicePaginator = new GridPaginator<>(invoiceGrid, invoiceData, 50);
        contractorPaginator = new GridPaginator<>(contractorGrid, contractorData, 50);
        configureInvoiceGrid();
        configureContractorGrid();
        this.invoiceFilter = new GridFilter<>(invoiceGrid);
        this.contractorFilter = new GridFilter<>(contractorGrid);
        configureInvoiceFilter();
        configureConractorFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, invoicePaginator);
        add(upperLayout(), invoiceFilter, invoiceGrid, invoicePaginator);
        configureSelectXlsTemplateButton();
    }

    private void configureInvoiceFilter() {

        invoiceFilter.setFieldToComboBox("statusName", FunnelDto::getStatusName, funnelService.getAllByType("invoice"));
        invoiceFilter.setFieldToIntegerField("count");
        invoiceFilter.setFieldToIntegerField("conversion");
        invoiceFilter.setFieldToIntegerField("price");
        invoiceFilter.onSearchClick(e -> invoicePaginator.setData(funnelService.searchByFilter(invoiceFilter.getFilterData())
                .stream().filter(this::isInvoiceFunnelDto).sorted().collect(Collectors.toList())));
        invoiceFilter.onClearClick(e -> updateList());
    }

    private void configureConractorFilter() {

        contractorFilter.setFieldToComboBox("statusName", FunnelDto::getStatusName, funnelService.getAllByType("contractor"));
        contractorFilter.setFieldToIntegerField("count");
        contractorFilter.setFieldToIntegerField("conversion");
        contractorFilter.onSearchClick(e -> contractorPaginator.setData(funnelService.searchByFilter(contractorFilter.getFilterData())
                        .stream().filter(this::isContractorFunnelDto).collect(Collectors.toList())));
        contractorFilter.onClearClick(e -> updateList());
    }

    private boolean isInvoiceFunnelDto(FunnelDto funnelDto) {
        return funnelDto.getType().equals("invoice");
    }

    private boolean isContractorFunnelDto(FunnelDto funnelDto) {
        return funnelDto.getType().equals("contractor");
    }


    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), configurationSubMenu(), buttonFilter(), selectXlsTemplateButton);
        upper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upper;
    }

    private Tabs configurationSubMenu() {
        tabs.addSelectedChangeListener(event -> {
            String tabName = event.getSelectedTab().getLabel();
            if ("По заказам".equals(tabName)) {
                removeAll();
                configureInvoiceGrid();
                configureInvoiceFilter();
                setHorizontalComponentAlignment(Alignment.CENTER, invoicePaginator);
                add(upperLayout(), invoiceFilter, invoiceGrid, invoicePaginator);
            } else if ("По контрагентам".equals(tabName)) {
                removeAll();
                configureContractorGrid();
                configureConractorFilter();
                setHorizontalComponentAlignment(Alignment.CENTER, contractorPaginator);
                add(upperLayout(), contractorFilter, contractorGrid, contractorPaginator);
            }
        });
        return tabs;
    }

    private void configListInvoices() {
        invoiceGrid.removeAllColumns();
        invoiceGrid.setItems(invoiceData);
        invoiceGrid.addColumn("statusName").setFlexGrow(11).setHeader("Статус").setId("Статус");
        invoiceGrid.addColumn("count").setFlexGrow(11).setHeader("Количество").setId("Количество");
        invoiceGrid.addColumn("time").setFlexGrow(11).setHeader("Время").setId("Время");
        invoiceGrid.addColumn("conversion").setFlexGrow(11).setHeader("Конверсия").setId("Конверсия");
        invoiceGrid.addColumn("price").setFlexGrow(11).setHeader("Сумма").setId("Сумма");
    }

    private void configListContractors() {
        contractorGrid.removeAllColumns();
        contractorGrid.setItems(contractorData);
        contractorGrid.addColumn("statusName").setFlexGrow(11).setHeader("Статус").setId("Статус");
        contractorGrid.addColumn("count").setFlexGrow(11).setHeader("Количество").setId("Количество");
        contractorGrid.addColumn("conversion").setFlexGrow(11).setHeader("Конверсия").setId("Конверсия");
    }

    private void configureInvoiceGrid() {
        contractorGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        configListInvoices();
    }

    private void configureContractorGrid() {
        contractorGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        configListContractors();
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("Воронка продаж — это способ распределения заказов (клиентов) по этапам процесса заключения сделки. " +
                        "Воронка показывает, сколько заказов (клиентов) на каких этапах сделки находятся. " +
                        "Если заказ не был выполнен, на воронке будет отмечен последний этап. " +
                        "Отчет позволяет выяснить, какой этап заключения сделки является «бутылочным горлышком». " +
                        "В отчете представлены разрезы по заказам покупателей и по контрагентам.\n" +
                        "Читать инструкцию: "),
                new Anchor("#", "Воронка Продаж"));
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        buttonRefresh.addClickListener(e -> updateList());
        return buttonRefresh;
    }


    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> invoiceFilter.setVisible(!invoiceFilter.isVisible()));
        buttonFilter.addClickListener(e -> contractorFilter.setVisible(!contractorFilter.isVisible()));
        return buttonFilter;
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private H4 title() {
        H4 title = new H4("Воронка Продаж");
        title.setHeight("2.2em");
        title.setWidth("80px");
        return title;
    }

    private void updateList() {
        if (invoices.isSelected()) {
            configListInvoices();
        } else if (contractors.isSelected()) {
            configListContractors();
        }
    }

    private void uploadXlsMenuItem(SubMenu subMenu) {
        MenuItem menuItem = subMenu.addItem("Добавить шаблон");
        Dialog dialog = new Dialog();
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        configureUploadFinishedListener(upload, buffer, dialog);
        dialog.add(upload);
        menuItem.addClickListener(x -> dialog.open());
    }

    private void configureUploadFinishedListener(Upload upload, MemoryBuffer buffer, Dialog dialog) {
        upload.addFinishedListener(event -> {
            if (getXlsFiles().stream().map(File::getName).anyMatch(x -> x.equals(event.getFileName()))) {
                getErrorNotification("Файл с таким именем уже существует");
            } else {
                File exelTemplate = new File(pathForSaveXlsTemplate + event.getFileName());
                try (FileOutputStream fos = new FileOutputStream(exelTemplate)) {
                    fos.write(buffer.getInputStream().readAllBytes());
                    configureSelectXlsTemplateButton();
                    getInfoNotification("Файл успешно загружен");
                    log.info("xls шаблон успешно загружен");
                } catch (IOException e) {
                    getErrorNotification("При загрузке шаблона произошла ошибка");
                    log.error("при загрузке xls шаблона произошла ошибка");
                }
                dialog.close();
            }
        });
    }

    private void configureSelectXlsTemplateButton() {
        SubMenu printSubMenu = print.getSubMenu();
        printSubMenu.removeAll();
        templatesXlsMenuItems(printSubMenu);
        uploadXlsMenuItem(printSubMenu);
    }

    private void templatesXlsMenuItems(SubMenu subMenu) {

        getXlsFiles().forEach(x -> subMenu.addItem(getLinkToXlsInvoiceTemplate(x)));
        getXlsFiles().forEach(x -> subMenu.addItem(getLinkToPdfInvoiceTemplate(x)));
        getXlsFiles().forEach(x -> subMenu.addItem(getLinkToOdsInvoiceTemplate(x)));
        getXlsFiles().forEach(x -> subMenu.addItem(getLinkToXlsContractorTemplate(x)));
        getXlsFiles().forEach(x -> subMenu.addItem(getLinkToPdfContractorTemplate(x)));
        getXlsFiles().forEach(x -> subMenu.addItem(getLinkToOdsContractorTemplate(x)));
    }

    private List<File> getXlsFiles() {
        File dir = new File(pathForSaveXlsTemplate);
        return Arrays.stream(Objects.requireNonNull(dir.listFiles())).filter(File::isFile).filter(x -> x.getName()
                .contains(".xls")).collect(Collectors.toList());
    }


    private Anchor getLinkToXlsInvoiceTemplate(File file) {
        String templateName = file.getName();
        PrintFunnelXls printFunnelXls = new PrintFunnelXls(file.getPath(), funnelService.getAllByType("invoice"));
        return new Anchor(new StreamResource(templateName, printFunnelXls::createReport), "Скачать по заказам в формате Excel");
    }

    private Anchor getLinkToPdfInvoiceTemplate(File file) {
        String templateName = file.getName().substring(0, file.getName().lastIndexOf(".")) + ".pdf";
        PrintFunnelXls printFunnelXls = new PrintFunnelXls(file.getPath(), funnelService.getAllByType("invoice"));
        return new Anchor(new StreamResource(templateName, printFunnelXls::createReportPDF), "Скачать по заказам в формате PDF");
    }

    private Anchor getLinkToOdsInvoiceTemplate(File file) {
        String templateName = file.getName().substring(0, file.getName().lastIndexOf(".")) + ".ods";
        PrintFunnelXls printFunnelXls = new PrintFunnelXls(file.getPath(), funnelService.getAllByType("invoice"));
        return new Anchor(new StreamResource(templateName, printFunnelXls::createReportODS), "Скачать по заказам в формате Office Calc");
    }

    private Anchor getLinkToXlsContractorTemplate(File file) {
        String templateName = file.getName();
        PrintFunnelXls printFunnelXls = new PrintFunnelXls(file.getPath(), funnelService.getAllByType("contractor"));
        return new Anchor(new StreamResource(templateName, printFunnelXls::createReport), "Скачать по контрагентам в формате Excel");
    }

    private Anchor getLinkToPdfContractorTemplate(File file) {
        String templateName = file.getName().substring(0, file.getName().lastIndexOf(".")) + ".pdf";
        PrintFunnelXls printFunnelXls = new PrintFunnelXls(file.getPath(), funnelService.getAllByType("contractor"));
        return new Anchor(new StreamResource(templateName, printFunnelXls::createReportPDF), "Скачать по контрагентам в формате PDF");
    }

    private Anchor getLinkToOdsContractorTemplate(File file) {
        String templateName = file.getName().substring(0, file.getName().lastIndexOf(".")) + ".ods";
        PrintFunnelXls printFunnelXls = new PrintFunnelXls(file.getPath(), funnelService.getAllByType("contractor"));
        return new Anchor(new StreamResource(templateName, printFunnelXls::createReportODS), "Скачать по контрагентам в формате Office Calc");
    }


    private void getErrorNotification(String message) {
        Div content = new Div();
        content.addClassName("my-style");
        content.setText(message);
        Notification notification = new Notification(content);
        notification.setDuration(5000);
        String styles = ".my-style { color: red; }";
        StreamRegistration resource = UI.getCurrent().getSession()
                .getResourceRegistry()
                .registerResource(new StreamResource("styles.css", () ->
                        new ByteArrayInputStream(styles.getBytes(StandardCharsets.UTF_8))));
        UI.getCurrent().getPage().addStyleSheet(
                "base://" + resource.getResourceUri().toString());
        notification.open();
    }
    private void getInfoNotification(String message) {
        Notification notification = new Notification(message, 5000);
        notification.open();
    }
}