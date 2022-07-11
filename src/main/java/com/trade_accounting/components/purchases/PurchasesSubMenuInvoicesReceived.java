package com.trade_accounting.components.purchases;

import com.trade_accounting.components.purchases.print.PrintInvoiceReceivedXls;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridConfigurer;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.invoice.InvoiceReceivedDto;
import com.trade_accounting.services.interfaces.warehouse.AcceptanceService;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.invoice.InvoiceReceivedService;
import com.vaadin.flow.component.Component;
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
import com.vaadin.flow.component.html.Label;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
//Если на страницу не ссылаются по URL или она не является отдельной страницей, а подгружается родительским классом, то URL и Title не нужен
/*@Route(value = PURCHASES_INVOICE_RECEIVED_VIEW, layout = AppView.class)
@PageTitle("Счета-фактуры полученные")*/
@SpringComponent
@UIScope
public class PurchasesSubMenuInvoicesReceived extends VerticalLayout implements AfterNavigationObserver {

    private final EmployeeService employeeService;
    private final InvoiceReceivedService invoiceReceivedService;
    private final CompanyService companyService;
    private final AcceptanceService acceptanceService;
    private final ContractorService contractorService;
    private final Notifications notifications;

    private final List<InvoiceReceivedDto> data;

    private final Grid<InvoiceReceivedDto> grid = new Grid<>(InvoiceReceivedDto.class, false);
    private final GridConfigurer<InvoiceReceivedDto> gridConfigurer = new GridConfigurer<>(grid);
    private GridPaginator<InvoiceReceivedDto> paginator;
    private final GridFilter<InvoiceReceivedDto> filter;
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};
    private final String pathForSaveXlsTemplate = "src/main/resources/xls_templates/purchases_templates/invoices_received/";
    private final String typeOfInvoice = "RECEIPT";

    private final TextField textField = new TextField();

    @Autowired
    public PurchasesSubMenuInvoicesReceived(EmployeeService employeeService, InvoiceReceivedService invoiceReceivedService,
                                            CompanyService companyService,
                                            AcceptanceService acceptanceService,
                                            ContractorService contractorService,
                                            @Lazy Notifications notifications) {
        this.employeeService = employeeService;
        this.invoiceReceivedService = invoiceReceivedService;
        this.companyService = companyService;
        this.acceptanceService = acceptanceService;
        this.contractorService = contractorService;
        this.notifications = notifications;
        this.data = getData();

        configureActions();
        configureGrid();

        this.filter = new GridFilter<>(grid);
        configureFilter();

        paginator = new GridPaginator<>(grid, data, 50);

        add(configureActions(), filter, grid, paginator);
    }

    private List<InvoiceReceivedDto> getData() {
        for (InvoiceReceivedDto invoiceReceivedDto : invoiceReceivedService.getAll()) {
            System.out.println(invoiceReceivedDto.getId());
        }
        return invoiceReceivedService.getAll();
    }

    private HorizontalLayout configureActions() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(buttonQuestion(), title(), buttonRefresh(),
                buttonFilter(), filterTextField(), numberField(), valueSelect(),
                valueStatus(), valuePrint(), buttonSettings());
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion("Входящие счета-фактуры создаются на основе приемки.");
    }

    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(dto -> formatDate(dto.getData())).setKey("data").setHeader("Дата и время")
                .setId("Дата и время");
        grid.addColumn(dto -> contractorService.getById(dto.getContractorId()).getName()).setHeader("Контрагент")
                .setKey("contractorDto")
                .setId("Контрагент");
        grid.addColumn(dto -> companyService.getById(dto.getCompanyId()).getName()).setHeader("Организация")
                .setKey("companyDto")
                .setId("Организация");
        grid.addColumn(this::getTotalPrice).setHeader("Сумма").setTextAlign(ColumnTextAlign.END)
                .setId("Сумма");
        grid.addColumn(InvoiceReceivedDto::getIncomNumber).setHeader("Входящий номер")
                .setKey("incomNumber")
                .setId("Входящий номер");
        grid.addColumn(dto -> formatDate(dto.getIncomData())).setHeader("Входящая дата")
                .setKey("incomData")
                .setId("Входящая дата");
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedSend)).setHeader("Отправлено")
                .setKey("send")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedPrint)).setHeader("Напечатано")
                .setKey("print")
                .setId("Напечатано");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");
        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        gridConfigurer.addConfigColumnToGrid();

        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("data");
        filter.setFieldToComboBox("contractorDto", ContractorDto::getName, contractorService.getAll());
        filter.setFieldToComboBox("companyDto", CompanyDto::getName, companyService.getAll());
        filter.setFieldToIntegerField("id");
        filter.onSearchClick(e -> paginator
                .setData(invoiceReceivedService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(invoiceReceivedService.getAll()));
    }

    private String formatDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime formatDateTime = LocalDateTime.parse(stringDate);
        return formatDateTime.format(formatter);
    }

    private H4 title() {
        H4 title = new H4("Счета-фактуры полученные");
        title.setHeight("2.2em");
        title.setWidth("80px");
        return title;
    }

    private Button buttonRefresh() {
        Button button = new Button(new Icon(VaadinIcon.REFRESH));
        button.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        button.addClickListener(e -> updateList());
        return button;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e->filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private TextField filterTextField() {
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> updateList(textField.getValue()));
        textField.setClearButtonVisible(true);
        textField.setWidth("300px");
        return textField;
    }

    private NumberField numberField() {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect() {
        return SelectConfigurer.configureDeleteSelect(() -> {
            deleteSelectedCorrections();
            grid.deselectAll();
            paginator.setData(getData());
        });
    }

    private void deleteSelectedCorrections() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (InvoiceReceivedDto invoiceReceivedDto : grid.getSelectedItems()) {
                invoiceReceivedService.deleteById(invoiceReceivedDto.getId());
                notifications.infoNotification("Выбранные счета-фактуры успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные счета-фактуры");
        }
    }

    private Select<String> valueStatus() {
        return SelectConfigurer.configureStatusSelect();
    }

    private Select<String> valuePrint() {
        Select<String> print = SelectConfigurer.configurePrintSelect();
        getXlsFiles().forEach(x -> print.add(getLinkToXlsTemplate(x)));
//        getXlsFiles().forEach(x -> print.add(getLinkToPDFTemplate(x)));
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
        List<InvoiceReceivedDto> list1 = invoiceReceivedService.getAll();
        for (InvoiceReceivedDto invoiceDto : list1) {
            sumList.add(getTotalPrice(invoiceDto));
        }
        PrintInvoiceReceivedXls printInvoiceReceivedXls = new PrintInvoiceReceivedXls(file.getPath(), invoiceReceivedService.getAll(), contractorService, companyService, sumList, employeeService);
        return new Anchor(new StreamResource(templateName, printInvoiceReceivedXls::createReport), templateName);
    }

//    private Anchor getLinkToPDFTemplate(File file) {
//        String templateName = file.getName();
//        List<String> sumList = new ArrayList<>();
//        List<InvoiceReceivedDto> list1 = invoiceReceivedService.getAll();
//        for (InvoiceReceivedDto invoiceDto : list1) {
//            sumList.add(getTotalPrice(invoiceDto));
//        }
//        PrintInvoiceReceivedXls printInvoiceReceivedXls = new PrintInvoiceReceivedXls(file.getPath(), invoiceReceivedService.getAll(), contractorService, companyService, sumList, employeeService);
//        return new Anchor(new StreamResource("invoiceReceived.pdf", printInvoiceReceivedXls::createReportPDF), "invoiceReceived.pdf");
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

    private void updateList() {
        grid.setItems(invoiceReceivedService.getAll());
    }

    private void updateList(String search) {
        grid.setItems(invoiceReceivedService.getAll());
        if(!(textField.getValue().equals(""))) {
            grid.setItems(invoiceReceivedService.searchByString(search));
        } else {
            grid.setItems(invoiceReceivedService.getAll());
        }
    }

    private String getTotalPrice(InvoiceReceivedDto dto) {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        return String.format("%.2f", totalPrice);
    }


    private Component getIsCheckedSend(InvoiceReceivedDto dto) {
        if (dto.getIsSend()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component getIsCheckedPrint(InvoiceReceivedDto dto) {
        if (dto.getIsPrint()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
