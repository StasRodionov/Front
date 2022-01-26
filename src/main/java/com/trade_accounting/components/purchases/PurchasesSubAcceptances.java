package com.trade_accounting.components.purchases;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.purchases.print.PrintAcceptancesXls;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.AcceptanceDto;
import com.trade_accounting.models.dto.AcceptanceProductionDto;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.AcceptanceProductionService;
import com.trade_accounting.services.interfaces.AcceptanceService;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.WarehouseService;
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
import com.vaadin.flow.component.menubar.MenuBar;
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Route(value = "admissions", layout = AppView.class)
@PageTitle("Приемки")
@SpringComponent
@UIScope
public class PurchasesSubAcceptances extends VerticalLayout implements AfterNavigationObserver {

    private final EmployeeService employeeService;
    private final CompanyService companyService;
    private final AcceptanceService acceptanceService;
    private final WarehouseService warehouseService;
    private final ContractorService contractorService;
    private final ContractService contractService;
    private final Notifications notifications;
    private final AcceptanceModalView modalView;
    private final List<AcceptanceDto> data;
    private final Grid<AcceptanceDto> grid = new Grid<>(AcceptanceDto.class, false);
    private GridPaginator<AcceptanceDto> paginator;
    private final GridFilter<AcceptanceDto> filter;
    private final TextField textField = new TextField();
    private final MenuBar selectXlsTemplateButton = new MenuBar();
    private final AddFromDirectModalWin addFromDirectModalWin;
    private final ProductService productService;
    private final AcceptanceProductionService acceptanceProductionService;
    private final String pathForSaveXlsTemplate = "src/main/resources/xls_templates/purchases_templates/purchases";
    private  final String textForQuestionButton = "<div><p>Приемки позволяют учитывать закупки товаров." +
            "Приемку создают, когда покупают новый товар." +
            "Если товар уже лежит у вас на складе или вы не хотите указывать поставщиков, лучше воспользоваться оприходованием.</p>" +
            "<p>В результате приемки увеличиваются остатки товаров в разделе Товары → Остатки и фиксируется долг перед поставщиком в     разделе Деньги → Взаиморасчеты." +
            "Также на основе приемки формируется себестоимость товара.</p>" +
            "<p>Приемку можно создать вручную или импортировать, в том числе из систем ЭДО.</p>"+
            "<p>Читать инструкцию: <a href=\"#\" target=\"_blank\">Приемка товаров</a></p></div>";


    public PurchasesSubAcceptances(EmployeeService employeeService, CompanyService companyService, AcceptanceService acceptanceService,
                                   WarehouseService warehouseService,
                                   ContractorService contractorService,
                                   ContractService contractService,
                                   Notifications notifications,
                                   AcceptanceModalView modalView,
                                   AddFromDirectModalWin addFromDirectModalWin,
                                   ProductService productService,
                                   AcceptanceProductionService acceptanceProductionService) {
        this.employeeService = employeeService;
        this.companyService = companyService;
        this.acceptanceService = acceptanceService;
        this.warehouseService = warehouseService;
        this.contractorService = contractorService;
        this.contractService = contractService;
        this.notifications = notifications;
        this.productService = productService;
        this.acceptanceProductionService = acceptanceProductionService;
        this.modalView = modalView;
        this.data = getData();
        this.addFromDirectModalWin = addFromDirectModalWin;
        paginator = new GridPaginator<>(grid, data, 50);
        configureGrid();
        filter = new GridFilter<>(grid);
        configureFilter();
        setSizeFull();
        add(configureActions(), filter, grid, paginator);
    }

    private List<AcceptanceDto> getData() {
        return acceptanceService.getAll();
    }

    private HorizontalLayout configureActions() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(buttonQuestion(), title(), buttonRefresh(), buttonAdd(),
                buttonFilter(), filterTextField(), numberField(), valueSelect(),
                valueStatus(), valuePrint(), buttonSettings(), selectXlsTemplateButton);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("Приемки позволяют учитывать закупки товаров. " +
                        "Приемку создают, когда покупают новый товар. " +
                        "Если товар уже лежит у вас на складе или вы не хотите указывать поставщиков, лучше воспользоваться оприходованием. " +
                        "В результате приемки увеличиваются остатки товаров в разделе Товары → Остатки и фиксируется долг перед поставщиком в разделе Деньги → Взаиморасчеты. " +
                        "Также на основе приемки формируется себестоимость товара. " +
                        "Приемку можно создать вручную или импортировать, в том числе из систем ЭДО. "+
                        "Читать инструкцию: "),
                new Anchor("#", "Приемка товаров"));
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(dto -> dto.getDate()).setKey("date").setHeader("Время").setSortable(true).setId("Дата");
        grid.addColumn(dto -> warehouseService.getById(dto.getWarehouseId()).getName()).setHeader("На склад")
                .setKey("warehouseDto").setId("На склад");
        grid.addColumn(dto -> contractorService.getById(dto.getContractorId()).getName()).setHeader("Контрагент").setKey("contractorDto")
                .setId("Контрагент");
        grid.addColumn(dto -> companyService.getById(contractService.getById(dto.getContractId()).getCompanyId()).getName()).setHeader("Организация")
                .setKey("companyDto").setId("Организация");
        grid.addColumn(this::getTotalPrice).setHeader("Сумма").setSortable(true);
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedSend)).setKey("send").setHeader("Отправлено")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedPrint)).setKey("print").setHeader("Напечатано")
                .setId("Напечатано");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");
        grid.addItemDoubleClickListener(event -> {
            AcceptanceDto acceptanceDto = event.getItem();
            AcceptanceModalView modalView = new AcceptanceModalView(
                    companyService,
                    acceptanceService,
                    contractService,
                    warehouseService,
                    contractorService,
                    notifications,
                    productService,
                    acceptanceProductionService);
            modalView.setAcceptanceForEdit(acceptanceDto);
            modalView.open();
        });
        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("warehouseDto", WarehouseDto::getName, warehouseService.getAll());
        filter.setFieldToComboBox("contractorDto", ContractorDto::getName, contractorService.getAll());
        filter.setFieldToComboBox("companyDto", CompanyDto::getName, companyService.getAll());
        filter.onSearchClick(e -> paginator.setData(acceptanceService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(acceptanceService.getAll()));
    }

    private H4 title() {
        H4 title = new H4("Приемки");
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

    private Button buttonAdd() {
        Button button = new Button("Приемка", new Icon(VaadinIcon.PLUS_CIRCLE));
        button.addClickListener(e -> modalView.open());
        return button;
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
        return textField;
    }

    private NumberField numberField() {
        NumberField numberField = new NumberField();
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
        select.addValueChangeListener(e -> {
            if (select.getValue().equals("Удалить")) {
                deleteAcceptance();
                grid.deselectAll();
                select.setValue("Изменить");
                paginator.setData(getData());
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

    private Select<String> valuePrint() {
        Select<String> print = new Select<>();
        print.setItems("Печать","Добавить");
        print.setValue("Печать");
        getXlsFiles().forEach(x -> print.add(getLinkToXlsTemplate(x)));
        getXlsFiles().forEach(x -> print.add(getLinkToPDFTemplate(x)));
        uploadXlsMenuItem(print);
        print.setWidth("130px");
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
        List<AcceptanceDto> list1 = acceptanceService.getAll();
        for (AcceptanceDto aDto : list1) {
            sumList.add(getTotalPrice(aDto));
        }
        PrintAcceptancesXls printAcceptancesXls = new PrintAcceptancesXls(file.getPath(), acceptanceService.getAll(), contractorService, warehouseService, companyService, sumList, employeeService);
        return new Anchor(new StreamResource(templateName, printAcceptancesXls::createReport), templateName);
    }

    private Anchor getLinkToPDFTemplate(File file) {
        List<String> sumList = new ArrayList<>();
        List<AcceptanceDto> list1 = acceptanceService.getAll();
        for (AcceptanceDto aDto : list1) {
            sumList.add(getTotalPrice(aDto));
        }
        PrintAcceptancesXls printAcceptancesXls = new PrintAcceptancesXls(file.getPath(), acceptanceService.getAll(), contractorService, warehouseService, companyService, sumList, employeeService);
        return new Anchor(new StreamResource("purchases.pdf", printAcceptancesXls::createReportPDF), "purchases.pdf");
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

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private void updateList() {
        grid.setItems(acceptanceService.getAll());
    }

    public void updateList(String search) {
//        if (!(textField.getValue().equals(""))) {
//            grid.setItems(acceptanceService.search(search));
//        } else {
//            grid.setItems(acceptanceService.search("null"));
//        }
        if (search.isEmpty()) {
            paginator.setData(acceptanceService.getAll());
        } else paginator.setData(acceptanceService.search(search));
    }

    private String getTotalPrice(AcceptanceDto dto) {
        List<AcceptanceProductionDto> acceptanceProductionDto = dto.getAcceptanceProduction();
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        for (AcceptanceProductionDto apd : acceptanceProductionDto) {
            totalPrice = totalPrice.add(apd.getPrice()
                    .multiply(apd.getAmount()));
        }
        return String.format("%.2f", totalPrice);
    }


    private Component getIsCheckedSend(AcceptanceDto dto) {
        if (dto.getIsSent()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component getIsCheckedPrint(AcceptanceDto dto) {
        if (dto.getIsPrint()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private void deleteAcceptance() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (AcceptanceDto item : grid.getSelectedItems()) {
                acceptanceService.deleteById(item.getId());
                notifications.infoNotification("Выбранные приемки успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные приемки");
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
