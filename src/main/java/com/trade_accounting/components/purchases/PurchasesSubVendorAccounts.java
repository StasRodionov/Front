package com.trade_accounting.components.purchases;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.purchases.print.PrintSupplierXls;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.components.util.configure.components.select.SelectConstants;
import com.trade_accounting.components.util.configure.components.select.SelectExt;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.company.SupplierAccountDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.warehouse.SupplierAccountProductsListService;
import com.trade_accounting.services.interfaces.company.SupplierAccountService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
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
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
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

@Slf4j
@Route(value = "suppliersInvoices", layout = AppView.class)
@PageTitle("Счета поставщиков")
@SpringComponent
@UIScope
public class PurchasesSubVendorAccounts extends VerticalLayout implements AfterNavigationObserver {

    private final EmployeeService employeeService;
    private final SupplierAccountService supplierAccountService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final ContractorService contractorService;
    private final Notifications notifications;
    private final SupplierAccountModalView modalView;
    private final TextField textField = new TextField();
    private final SupplierAccountModalView supplierAccountModalView;
    private final SupplierAccountProductsListService supplierAccountProductsListService;

    private List<SupplierAccountDto> supplierAccount;
    private final String typeOfInvoice = "EXPENSE";

    private HorizontalLayout actions;
    private final Grid<SupplierAccountDto> grid = new Grid<>(SupplierAccountDto.class, false);
    private GridPaginator<SupplierAccountDto> paginator;
    private final GridFilter<SupplierAccountDto> filter;
    private final String pathForSaveXlsTemplate = "src/main/resources/xls_templates/purchases_templates/supplier";

    @Autowired
    public PurchasesSubVendorAccounts(EmployeeService employeeService, SupplierAccountService supplierAccountService,
                                      WarehouseService warehouseService, CompanyService companyService,
                                      ContractorService contractorService,
                                      @Lazy Notifications notifications,
                                      SupplierAccountModalView modalView,
                                      SupplierAccountModalView supplierAccountModalView,
                                      SupplierAccountProductsListService supplierAccountProductsListService) {
        this.employeeService = employeeService;
        this.supplierAccountService = supplierAccountService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.notifications = notifications;
        this.modalView = modalView;
        this.supplierAccountModalView = supplierAccountModalView;
        this.supplierAccountProductsListService = supplierAccountProductsListService;
        loadSupplierAccounts();
        configureActions();
        configureGrid();
        configurePaginator();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        add(actions, filter, grid, paginator);
    }

    private List<SupplierAccountDto> loadSupplierAccounts() {
        supplierAccount = supplierAccountService.getAll(typeOfInvoice);
        return supplierAccount;
    }

    private void configureActions() {
        actions = new HorizontalLayout();
        actions.add(buttonQuestion(), title(), buttonRefresh(), buttonUnit(), buttonFilter(), filterTextField(),
                numberField(), valueSelect(), valueStatus(), valueCreate(), valuePrint(), buttonSettings());
        actions.setDefaultVerticalComponentAlignment(Alignment.CENTER);
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("Счета поставщиков помогают планировать оплату товаров. " +
                        "Счета не меняют количество товара на складе — для этого нужно создать приемку, а чтобы учесть оплату — платеж. " +
                        "Дату оплаты можно запланировать. Не оплаченные вовремя счета отображаются в разделе Показатели. " +
                        "Счета можно создавать сразу из заказа поставщику. " +
                        "Читать инструкцию: "),
                new Anchor("#", "Счета поставщиков"));
    }

    private Grid<SupplierAccountDto> configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn("id").setWidth("20px").setHeader("№").setId("№");
        grid.addColumn(SupplierAccountDto::getDate).setKey("date").setHeader("Время").setSortable(true)
                .setId("Дата");
        grid.addColumn(e -> contractorService.getById(e.getContractorId()).getName()).setWidth("200px")
                .setHeader("Контрагент").setKey("contractorDto").setId("Контрагент");
        grid.addColumn(iDto -> companyService.getById(iDto.getCompanyId()).getName()).setHeader("Компания").setKey("companyDto")
                .setId("Компания");
        grid.addColumn(dto -> warehouseService.getById(dto.getWarehouseId()).getName()).setHeader("На склад")
                .setKey("warehouseDto").setId("На склад");
        grid.addColumn(this::getTotalPrice).setHeader("Сумма").setSortable(true);
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedIcon)).setKey("isSpend").setHeader("Оплачено")
                .setId("Оплачено");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");

        grid.setHeight("100vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addItemDoubleClickListener(event -> {
            SupplierAccountDto editSupplierAccounts = event.getItem();
            /*SupplierAccountModalView supplierAccountModalView = new SupplierAccountModalView(
                    supplierAccountService,
                    companyService,
                    warehouseService,
                    contractorService,
                    notifications);*/
            supplierAccountModalView.setSupplierAccountsForEdit(editSupplierAccounts);
            supplierAccountModalView.open();


        });
        return grid;
    }

    private void configurePaginator() {
        paginator = new GridPaginator<>(grid, supplierAccount, 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("contractorDto", ContractorDto::getName, contractorService.getAll());
        filter.setFieldToComboBox("companyDto", CompanyDto::getName, companyService.getAll());
        filter.setFieldToComboBox("warehouseDto", WarehouseDto::getName, warehouseService.getAll());
        filter.onSearchClick(e -> {
            Map<String, String> map = filter.getFilterData();
            map.put("typeOfInvoice", typeOfInvoice);
            paginator.setData(supplierAccountService.searchByFilter(map));
        });
        filter.onClearClick(e -> paginator.setData(supplierAccountService.getAll(typeOfInvoice)));
    }

    private H4 title() {
        H4 title = new H4("Счета поставщиков");
        title.setHeight("2.2em");
        title.setWidth("80px");
        return title;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        buttonRefresh.addClickListener(e -> updateList());
        return buttonRefresh;
    }

    private Button buttonUnit() {
        Button buttonUnit = new Button("Счёт", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickListener(e -> modalView.open());
        return buttonUnit;
    }

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
        if(!(textField.getValue().equals(""))) {
            grid.setItems(supplierAccountService.searchByString(nameFilter));
        } else {
            grid.setItems(supplierAccountService.getAll(typeOfInvoice));
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
            moveToRecycleBinSelectedInvoices();
            grid.deselectAll();
            paginator.setData(loadSupplierAccounts());
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
        List<SupplierAccountDto> list1 = supplierAccountService.getAll(typeOfInvoice);
        for (SupplierAccountDto sad : list1) {
            sumList.add(getTotalPrice(sad));
        }
        PrintSupplierXls printSupplierXls = new PrintSupplierXls(file.getPath(), supplierAccountService.getAll(typeOfInvoice), contractorService,warehouseService ,companyService, sumList, employeeService);
        return new Anchor(new StreamResource(templateName, printSupplierXls::createReport), templateName);

    }

//    private Anchor getLinkToPDFTemplate(File file) {
//        List<String> sumList = new ArrayList<>();
//        List<SupplierAccountDto> list1 = supplierAccountService.getAll();
//        for (SupplierAccountDto sad : list1) {
//            sumList.add(getTotalPrice(sad));
//        }
//        PrintSupplierXls printSupplierXls = new PrintSupplierXls(file.getPath(), supplierAccountService.getAll(), contractorService,warehouseService ,companyService, sumList, employeeService);
//        return new Anchor(new StreamResource("supplierInvoices.pdf", printSupplierXls::createReportPDF), "supplierInvoices.pdf");
//
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

    private TextField textField() {
        TextField textField = new TextField("", "1-1 из 1");
        textField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        return textField;
    }

    private String formatDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime formatDateTime = LocalDateTime.parse(stringDate);
        return formatDateTime.format(formatter);
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
        grid.setItems(supplierAccountService.getAll(typeOfInvoice));
    }

    private String getTotalPrice(SupplierAccountDto invoice) {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
//        for(SupplierAccountProductsListDto supplierAccountProductsListDto: supplierAccountProductsListService.getBySupplierId(invoice.getId())) {
//            totalPrice = totalPrice.add(supplierAccountProductsListDto.getTotal());
//        }
        return String.format("%.2f", totalPrice);
    }

    public List<SupplierAccountDto> moveToRecycleBinSelectedInvoices() {
        List<SupplierAccountDto> moved = new ArrayList<>();
        if(!grid.getSelectedItems().isEmpty()) {
            for(SupplierAccountDto supp : grid.getSelectedItems()) {
             moved.add(supplierAccountService.getById(supp.getId()));
            supplierAccountService.moveToIsRecyclebin(supp.getId());
                notifications.infoNotification("Выбранные счета поставщиков помещены в корзину");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные контрагенты");
        }

        return moved;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
