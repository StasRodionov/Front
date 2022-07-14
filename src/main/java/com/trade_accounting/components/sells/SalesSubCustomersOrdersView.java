package com.trade_accounting.components.sells;

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
import com.trade_accounting.models.dto.invoice.InvoicesStatusDto;
import com.trade_accounting.models.dto.util.ProjectDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.invoice.InvoiceService;
import com.trade_accounting.services.interfaces.invoice.InvoicesStatusService;
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
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.provider.SortDirection;
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
/* @Route(value = SELLS_CUSTOMERS_ORDERS_VIEW, layout = AppView.class)
@PageTitle("Заказы покупателей") */
@SpringComponent
@UIScope
public class SalesSubCustomersOrdersView extends VerticalLayout implements AfterNavigationObserver {

    private final CompanyService companyService;
    private final ContractorService contractorService;
    private final InvoiceService invoiceService;
    private final EmployeeService employeeService;
    private final InvoicesStatusService invoicesStatusService;
    private final ProjectService projectService;

    private final SalesEditCreateInvoiceView salesEditCreateInvoiceView;

    private final Notifications notifications;

    private List<InvoiceDto> data;
    private final Grid<InvoiceDto> grid = new Grid<>(InvoiceDto.class, false);
    private final GridConfigurer<InvoiceDto> gridConfigurer;
    private final GridPaginator<InvoiceDto> paginator;
    private final GridFilter<InvoiceDto> filter;

    private final String typeOfInvoice = "RECEIPT";
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES,
            GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};
    private final String pathForSaveSalesXlsTemplate = "src/main/resources/xls_templates/sales_templates/";

    @Autowired
    public SalesSubCustomersOrdersView(CompanyService companyService, ContractorService contractorService,
                                       InvoiceService invoiceService, EmployeeService employeeService,
                                       InvoicesStatusService invoicesStatusService, ProjectService projectService,
                                       ColumnsMaskService columnsMaskService,
                                       @Lazy SalesEditCreateInvoiceView salesEditCreateInvoiceView,
                                       @Lazy Notifications notifications) {
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.projectService = projectService;
        this.employeeService = employeeService;
        this.invoiceService = invoiceService;
        this.invoicesStatusService = invoicesStatusService;
        this.notifications = notifications;
        this.salesEditCreateInvoiceView = salesEditCreateInvoiceView;
        this.gridConfigurer = new GridConfigurer<>(grid, columnsMaskService, GRID_SALES_MAIN_CUSTOMERS_ORDERS);
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();

        paginator = new GridPaginator<>(grid);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);

        refreshContent();
    }

    public void refreshContent() {
        this.data = getData();
        this.paginator.setData(data);
        this.paginator.setItemsPerPage(50);
        removeAll();
        add(upperLayout(), filter, grid, paginator);
    }


    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(iDto -> formatDate(iDto.getDate())).setHeader("Дата и время")
                .setKey("date").setId("Дата и время");
        grid.addColumn(iDto -> contractorService.getById(iDto.getContractorId()).getName()).setHeader("Контрагент")
                .setKey("contractorDto").setId("Контрагент");
        grid.addColumn(iDto -> companyService.getById(iDto.getCompanyId()).getName()).setHeader("Компания")
                .setKey("companyDto").setId("Компания");
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedIcon)).setHeader("Проведена")
                .setKey("spend").setId("Проведена");
        grid.addColumn(iDto -> iDto.getProjectId() != null ?
                                projectService.getById(iDto.getProjectId()).getName() : "").setHeader("Проект")
                .setKey("projectDto").setId("Проект");
        grid.addColumn(iDto -> invoicesStatusService.getById(iDto.getInvoicesStatusId()).getStatusName()).setHeader("Статус")
                .setKey("invoicesStatusDto").setId("Статус");
        grid.addColumn(this::getTotalPrice).setHeader("Сумма").setTextAlign(ColumnTextAlign.END)
                .setId("Сумма");
        grid.addColumn("comment").setHeader("Комментарий")
                .setId("Комментарий");
        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        gridConfigurer.addConfigColumnToGrid();

        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addItemClickListener(event -> {
            InvoiceDto editInvoice = event.getItem();
            salesEditCreateInvoiceView.setInvoiceDataForEdit(editInvoice);
            salesEditCreateInvoiceView.setUpdateState(true);
            salesEditCreateInvoiceView.setType("RECEIPT");
            salesEditCreateInvoiceView.setLocation(SELLS_CUSTOMERS_ORDERS_VIEW);
            salesEditCreateInvoiceView.setProtectedTabSwitch();
//            UI.getCurrent().navigate(SELLS_SELLS__CUSTOMER_ORDER_EDIT);
            removeAll();
            add(salesEditCreateInvoiceView);
        });
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("contractorDto", ContractorDto::getName, contractorService.getAll());
        filter.setFieldToComboBox("companyDto", CompanyDto::getName, companyService.getAll());
        filter.setFieldToComboBox("invoicesStatusDto", InvoicesStatusDto::getStatusName, invoicesStatusService.getAll());
        filter.setFieldToComboBox("spend", Boolean.TRUE, Boolean.FALSE);
        filter.setFieldToComboBox("projectDto", ProjectDto::getName, projectService.getAll());
        filter.onSearchClick(e -> {
            Map<String, String> map = filter.getFilterData();
            map.put("typeOfInvoice", typeOfInvoice);
            paginator.setData(invoiceService.search(map));
        });
        filter.onClearClick(e -> paginator.setData(invoiceService.getAll(typeOfInvoice)));
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

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonUnit(), buttonFilter(), textField(),
                numberField(), valueSelect(), valueStatus(), valueCreate(), valuePrint(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upper;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("Заказы покупателей позволяют планировать продажи. " +
                        "Они не влияют на остатки по складам — для этого нужно создать отгрузку. " +
                        "На основе заказов можно формировать резервы и выставлять счета. " +
                        "Читать инструкцию: "),
                new Anchor("#", "Заказы покупателей"));
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonUnit() {
        Button buttonUnit = new Button("Заказ", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickListener(event -> {
            salesEditCreateInvoiceView.resetView();
            salesEditCreateInvoiceView.setUpdateState(false);
            salesEditCreateInvoiceView.setType("RECEIPT");
            salesEditCreateInvoiceView.setLocation(SELLS_CUSTOMERS_ORDERS_VIEW);
            salesEditCreateInvoiceView.setProtectedTabSwitch();
//            buttonUnit.getUI().ifPresent(ui -> ui.navigate(SELLS_SELLS__CUSTOMER_ORDER_EDIT));
            removeAll();
            add(salesEditCreateInvoiceView);
        });
        return buttonUnit;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private TextField textField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("Номер, компания или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> updateList(textField.getValue()));
        return textField;
    }

    private H4 title() {
        H4 title = new H4("Заказы покупателей");
        title.setHeight("2.2em");
        title.setWidth("80px");
        return title;
    }

    private Select<String> valueSelect() {
        return SelectConfigurer.configureDeleteSelect(() -> {
            deleteSelectedInvoices();
            grid.deselectAll();
            paginator.setData(getData());;
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
        getXlsFile().forEach(x -> print.add(getLinkToSalesXls(x)));
        uploadXlsTemplates(print);
        return print;
    }

    private void uploadXlsTemplates(Select<String> print) {
        Dialog dialog = new Dialog();
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        configureUploadFinishedListener(upload, buffer, dialog, print);
        dialog.add(upload);
        print.addValueChangeListener(x -> {
            if (x.getValue().equals("Добавить шаблон")) {
                dialog.open();
            }
        });
    }

    private void configureUploadFinishedListener(Upload upload, MemoryBuffer buffer, Dialog dialog, Select<String> print) {
        upload.addFinishedListener(event -> {
            if (getXlsFile().stream().map(File::getName).anyMatch(x -> x.equals(event.getFileName()))) {
                getErrorNotification("Файл с таки именем уже существует");
            } else {
                File exelTemplate = new File(pathForSaveSalesXlsTemplate + event.getFileName());
                try (FileOutputStream fos = new FileOutputStream(exelTemplate)) {
                    fos.write(buffer.getInputStream().readAllBytes());
                    print.removeAll();
                    getXlsFile().forEach(x -> print.add(getLinkToSalesXls(x)));
                    log.info("xls шаблон успешно загружен");
                    getInfoNotification("Файл успешно загружен");
                } catch (IOException e) {
                    getErrorNotification("При загрузке шаблона произошла ошибка");
                    log.error("при загрузке xls шаблона произошла ошибка");
                }
                dialog.close();
            }
        });
    }


    private List<File> getXlsFile() {
        File dir = new File(pathForSaveSalesXlsTemplate);
        return Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(File::isFile).filter(x -> x.getName().contains(".xls"))
                .collect(Collectors.toList());
    }

    private Anchor getLinkToSalesXls(File file) {
        String salesTemplate = file.getName();
        List<String> sumList = new ArrayList<>();
        List<InvoiceDto> list1 = invoiceService.getAll(typeOfInvoice);
        for (InvoiceDto inc : list1) {
            sumList.add(getTotalPrice(inc));
        }
        PrintSalesXls printSalesXls = new PrintSalesXls(file.getPath(), invoiceService.getAll(typeOfInvoice),
                sumList, employeeService, companyService, contractorService);
        return new Anchor(new StreamResource(salesTemplate, printSalesXls::createReport), salesTemplate);
    }

    private void updateList() {
        GridPaginator<InvoiceDto> paginatorUpdateList = new GridPaginator<>(grid, invoiceService.getAll(),100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorUpdateList);
        GridSortOrder<InvoiceDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        removeAll();
        add(upperLayout(), grid, paginator);
    }

    private void updateList(String text) {
        grid.setItems(invoiceService.findBySearchAndTypeOfInvoice(text, typeOfInvoice));
    }

    private String getTotalPrice(InvoiceDto invoiceDto) {
        List<InvoiceProductDto> invoiceProductDtoList = salesEditCreateInvoiceView.getListOfInvoiceProductByInvoice(invoiceDto);
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        for (InvoiceProductDto invoiceProductDto : invoiceProductDtoList) {
            totalPrice = totalPrice.add(invoiceProductDto.getPrice()
                    .multiply(invoiceProductDto.getAmount()));
        }
        return String.format("%.2f", totalPrice);
    }

    private String formatDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime formatDateTime = LocalDateTime.parse(stringDate);
        return formatDateTime.format(formatter);
    }

    private List<InvoiceDto> getData() {
        return invoiceService.getAll(typeOfInvoice);
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
}

