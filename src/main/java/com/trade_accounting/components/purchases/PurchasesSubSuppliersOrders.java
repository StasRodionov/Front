package com.trade_accounting.components.purchases;


import com.trade_accounting.components.purchases.print.PrintInvoicesXls;
import com.trade_accounting.components.sells.SalesEditCreateInvoiceView;
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
import com.trade_accounting.models.dto.invoice.InvoiceProductDto;
import com.trade_accounting.models.dto.util.ProjectDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.invoice.InvoiceService;
import com.trade_accounting.services.interfaces.util.ColumnsMaskService;
import com.trade_accounting.services.interfaces.util.ProjectService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.trade_accounting.config.SecurityConstants.*;

@Slf4j
/* @Route(value = PURCHASES_SUPPLIERS_ORDERS_VIEW, layout = AppView.class)
@PageTitle("Заказы поставщикам") */
@SpringComponent
@UIScope
public class PurchasesSubSuppliersOrders extends VerticalLayout implements AfterNavigationObserver {

    private final ContractorService contractorService;
    private final CompanyService companyService;
    private final InvoiceService invoiceService;
    private final SalesEditCreateInvoiceView salesEditCreateInvoiceView;
    private final Notifications notifications;
    private final EmployeeService employeeService;
    private final ProjectService projectService;

    private List<InvoiceDto> data;

    private final String typeOfInvoice = "EXPENSE";
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES,
            GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};

    private final Grid<InvoiceDto> grid = new Grid<>(InvoiceDto.class, false);
    private final GridConfigurer<InvoiceDto> gridConfigurer;

    private GridPaginator<InvoiceDto> paginator;
    private GridFilter<InvoiceDto> filter;
    private final TextField textField = new TextField();

    private final String pathForSaveXlsTemplate = "src/main/resources/xls_templates/purchases_templates/invoices";

    @Autowired
    public PurchasesSubSuppliersOrders(ContractorService contractorService, CompanyService companyService,
                                       InvoiceService invoiceService, ProjectService projectService,
                                       EmployeeService employeeService, ColumnsMaskService columnsMaskService,
                                       @Lazy SalesEditCreateInvoiceView salesEditCreateInvoiceView,
                                       @Lazy Notifications notifications) {
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.projectService = projectService;
        this.employeeService = employeeService;
        this.invoiceService = invoiceService;
        this.salesEditCreateInvoiceView = salesEditCreateInvoiceView;
        this.notifications = notifications;
        this.gridConfigurer = new GridConfigurer<>(grid, columnsMaskService, GRID_PURCHASES_MAIN_SUPPLIERS_ORDERS);
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();

        this.paginator = new GridPaginator<>(grid);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);

        refreshContent();
    }

    public void refreshContent() {
        this.data = getData();
        this.paginator.setData(data);
        this.paginator.setItemsPerPage(50);
        removeAll();
        add(configureActions(), filter, grid, paginator);
    }

    private HorizontalLayout configureActions() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonUnit(), buttonFilter(), filterTextField(),
                numberField(), valueSelect(), valueStatus(), valueCreate(), valuePrint(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("Заказы поставщикам позволяют планировать закупки товаров и услуг. " +
                        "Они не меняют количество товара на складе — для этого нужно создать приемку. " +
                        "Товары из заказов поставщику можно поставить в ожидание. " +
                        "Читать инструкцию: "),
                new Anchor("#", "Заказы поставщикам"));
    }

    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(iDto -> formatDate(iDto.getDate())).setHeader("Дата и время")
                .setKey("date")
                .setId("Дата и время");
        grid.addColumn(iDto -> iDto.getContractorId() != null ?
                                contractorService.getById(iDto.getContractorId()).getName() : "Неизвестный поставщик")
                .setHeader("Контрагент")
                .setKey("contractorDto")
                .setId("Контрагент");
//        grid.addColumn("typeOfInvoice").setHeader("Счет-фактура").setId("Счет-фактура");
        grid.addColumn(iDto -> companyService.getById(iDto.getCompanyId()).getName()).setHeader("Компания")
                .setKey("companyDto")
                .setId("Компания");
//        grid.addColumn("spend").setHeader("Проведена").setId("Проведена");
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedIcon)).setHeader("Проведена")
                .setKey("isSpend")
                .setId("Проведена");
        grid.addColumn(iDto -> iDto.getProjectId() != null ?
                                projectService.getById(iDto.getProjectId()).getName() : "").setHeader("Проект")
                .setKey("projectDto")
                .setId("Проект");
        grid.addColumn(this::getTotalPrice).setHeader("Сумма").setTextAlign(ColumnTextAlign.END).setId("Сумма");
//        grid.addColumn(iDto -> iDto.getWarehouseDto().getName()).setHeader("Склад").setKey("warehouseDto").setId("Склад");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");
        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        gridConfigurer.addConfigColumnToGrid();

        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addItemClickListener(event -> {
            InvoiceDto editInvoice = event.getItem();
            salesEditCreateInvoiceView.setInvoiceDataForEdit(editInvoice);
            salesEditCreateInvoiceView.setUpdateState(true);
            salesEditCreateInvoiceView.setType("EXPENSE");
            salesEditCreateInvoiceView.setLocation(PURCHASES_SUPPLIERS_ORDERS_VIEW);
            salesEditCreateInvoiceView.setProtectedTabSwitch();
            removeAll();
            add(salesEditCreateInvoiceView);
        });
    }

    private List<InvoiceDto> getData() {
        return invoiceService.getAll(typeOfInvoice);
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("contractorDto", ContractorDto::getName, contractorService.getAll());
        filter.setFieldToComboBox("companyDto", CompanyDto::getName, companyService.getAll());
        filter.setFieldToComboBox("projectDto", ProjectDto::getName, projectService.getAll());
        filter.setFieldToComboBox("isSpend", Boolean.TRUE, Boolean.FALSE);
        filter.onSearchClick(e -> {
            Map<String, String> map = filter.getFilterData();
            map.put("typeOfInvoice", typeOfInvoice);
            paginator.setData(invoiceService.search(map));
        });
        filter.onClearClick(e -> paginator.setData(invoiceService.getAll(typeOfInvoice)));
    }

    private H4 title() {
        H4 title = new H4("Заказы поставщикам");
        title.setHeight("2.2em");
        title.setWidth("60px");
        return title;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        buttonRefresh.addClickListener(e -> updateList());
        return buttonRefresh;
    }

    private Button buttonUnit() {
        Button buttonUnit = new Button("Заказ", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickListener(event -> {
            salesEditCreateInvoiceView.resetView();
            salesEditCreateInvoiceView.setUpdateState(false);
            salesEditCreateInvoiceView.setType("EXPENSE");
            salesEditCreateInvoiceView.setLocation(PURCHASES_SUPPLIERS_ORDERS_VIEW);
            salesEditCreateInvoiceView.setProtectedTabSwitch();
            removeAll();
            add(salesEditCreateInvoiceView);
        });
        return buttonUnit;
    }

    private Button buttonFilter() {
        return new Button("Фильтр", clickEvent -> {
            filter.setVisible(!filter.isVisible());
        });
    }

    private TextField filterTextField() {
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addValueChangeListener(e -> updateList(textField.getValue()));
        textField.setWidth("300px");
        textField.setWidth("220px");
        return textField;
    }

    private void updateList(String search) {
        if (search.isEmpty()) {
            paginator.setData(invoiceService.getAll(typeOfInvoice));
        } else {
            paginator.setData(invoiceService
                .findBySearchAndTypeOfInvoice(search, typeOfInvoice));
        }
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect() {
        return SelectConfigurer.configureDeleteSelect(() -> {
            deleteSelectedInvoices();
            grid.deselectAll();
            paginator.setData(getData());
        });
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
        Select<String> print = SelectConfigurer.configurePrintSelect();
        getXlsFiles().forEach(x -> print.add(getLinkToXlsTemplate(x)));
        uploadXlsMenuItem(print);
        return print;
    }

    private List<File> getXlsFiles() {
        File dir = new File(pathForSaveXlsTemplate);
        return Arrays.stream(Objects.requireNonNull(dir.listFiles())).filter(File::isFile).filter(x -> x.getName()
                .contains(".xls")).collect(Collectors.toList());
    }

    private void uploadXlsMenuItem(Select<String> print) {
        Dialog dialog = new Dialog();
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        configureUploadFinishedListener(upload, buffer, dialog, print);
        dialog.add(upload);
        print.addValueChangeListener(x -> {
            if (print.getValue().equals("Добавить шаблон")) {
                dialog.open();
            }
        });
    }

    private void configureUploadFinishedListener(Upload upload, MemoryBuffer buffer, Dialog dialog,
                                                 Select<String> print) {
        upload.addFinishedListener(event -> {
            if (getXlsFiles().stream().map(File::getName).anyMatch(x -> x.equals(event.getFileName()))) {
                getErrorNotification("Файл с таким именем уже существует");
            } else {
                File exelTemplate = new File(pathForSaveXlsTemplate + event.getFileName());
                try (FileOutputStream fos = new FileOutputStream(exelTemplate)) {
                    fos.write(buffer.getInputStream().readAllBytes());
                    getInfoNotification("Файл успешно загружен");
                    log.info("xls шаблон успешно загружен");
                    print.removeAll();
                    getXlsFiles().forEach(x -> print.add(getLinkToXlsTemplate(x)));

                } catch (IOException e) {
                    getErrorNotification("При загрузке шаблона произошла ошибка");
                    log.error("при загрузке xls шаблона произошла ошибка");
                }
                print.setValue("Печать");
                dialog.close();
            }
        });
    }


    private Anchor getLinkToXlsTemplate(File file) {
        String templateName = file.getName();
        List<String> sumList = new ArrayList<>();
        List<InvoiceDto> list1 = invoiceService.getAll(typeOfInvoice);
        for (InvoiceDto inc : list1) {
            sumList.add(getTotalPrice(inc));
        }
        PrintInvoicesXls printInvoicesXls = new PrintInvoicesXls(file.getPath(), invoiceService.getAll(typeOfInvoice), contractorService, companyService, sumList, employeeService);
        return new Anchor(new StreamResource(templateName, printInvoicesXls::createReport), templateName);
    }

//    private Anchor getLinkToPDFTemplate(File file) {
//        List<String> sumList = new ArrayList<>();
//        List<InvoiceDto> list1 = invoiceService.getAll(typeOfInvoice);
//        for (InvoiceDto inc : list1) {
//            sumList.add(getTotalPrice(inc));
//        }
//        PrintInvoicesXls printInvoicesXls = new PrintInvoicesXls(file.getPath(), invoiceService.getAll(typeOfInvoice), contractorService, companyService, sumList, employeeService);
//        return new Anchor(new StreamResource("invoices.pdf", printInvoicesXls::createReportPDF), "invoices.pdf");
//    }

    private void getInfoNotification(String message) {
        Notification notification = new Notification(message, 5000);
        notification.open();
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

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private String formatDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime formatDateTime = LocalDateTime.parse(stringDate);
        return formatDateTime.format(formatter);
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

    private void updateList() {
        grid.setItems(invoiceService.getAll(typeOfInvoice));
    }

    protected String getTotalPrice(InvoiceDto invoiceDto) {
        List<InvoiceProductDto> invoiceProductDtoList = salesEditCreateInvoiceView.getListOfInvoiceProductByInvoice(invoiceDto);
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        for (InvoiceProductDto invoiceProductDto : invoiceProductDtoList) {
            totalPrice = totalPrice.add(invoiceProductDto.getPrice()
                    .multiply(invoiceProductDto.getAmount()));
        }
        return String.format("%.2f", totalPrice);
    }

    private void deleteSelectedInvoices() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (InvoiceDto invoiceDto : grid.getSelectedItems()) {
                invoiceService.deleteById(invoiceDto.getId());
                notifications.infoNotification("Выбранные заказы успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные заказы");
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }

}
