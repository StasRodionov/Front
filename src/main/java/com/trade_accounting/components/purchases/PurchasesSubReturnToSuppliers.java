package com.trade_accounting.components.purchases;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.goods.GoodsModalWindow;
import com.trade_accounting.components.purchases.print.PrintReturnToSupplierXml;
import com.trade_accounting.components.util.*;
import com.trade_accounting.components.util.configure.components.select.Action;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.components.util.configure.components.select.SelectConstants;
import com.trade_accounting.components.util.configure.components.select.SelectExt;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.finance.ReturnToSupplierDto;
import com.trade_accounting.models.dto.util.ProjectDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.finance.ReturnToSupplierService;
import com.trade_accounting.services.interfaces.util.ProjectService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Text;
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
import java.util.Objects;
import java.util.stream.Collectors;

import static com.trade_accounting.config.SecurityConstants.*;

@Slf4j
@Route(value = PURCHASES_RETURNS_TO_SUPPLIERS_VIEW, layout = AppView.class)
@PageTitle("Возвраты поставщикам")
@SpringComponent
@UIScope
public class PurchasesSubReturnToSuppliers extends VerticalLayout implements AfterNavigationObserver {

    private final EmployeeService employeeService;
    private final ReturnToSupplierService returnToSupplierService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final ContractorService contractorService;
    private final ContractService contractService;
    private final ProjectService projectService;

    private final Notifications notifications;
    private final ReturnToSupplierModalView modalView;
    private final GoodsModalWindow goodsModalWindow;

    private final List<ReturnToSupplierDto> data;

    private final Grid<ReturnToSupplierDto> grid = new Grid<>(ReturnToSupplierDto.class, false);
    private final GridConfigurer<ReturnToSupplierDto> gridConfigurer = new GridConfigurer<>(grid);
    private GridPaginator<ReturnToSupplierDto> paginator;
    private final GridFilter<ReturnToSupplierDto> filter;
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};
    private final String pathForSaveXlsTemplate = "src/main/resources/xls_templates/purchases_templates/return/";

    private final TextField textField = new TextField();

    @Autowired
    public PurchasesSubReturnToSuppliers(EmployeeService employeeService, ReturnToSupplierService returnToSupplierService,
                                         WarehouseService warehouseService, CompanyService companyService,
                                         ContractorService contractorService, ContractService contractService,
                                         ProjectService projectService,
                                         @Lazy Notifications notifications, ReturnToSupplierModalView modalView,
                                         GoodsModalWindow goodsModalWindow) {
        this.employeeService = employeeService;
        this.returnToSupplierService = returnToSupplierService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.contractService = contractService;
        this.projectService = projectService;
        this.notifications = notifications;
        this.modalView = modalView;
        this.goodsModalWindow = goodsModalWindow;
        this.data = loadReturnToSuppliers();
        paginator = new GridPaginator<>(grid, data, 50);
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        add(configureActions(), filter, grid, paginator);
    }

    private HorizontalLayout configureActions() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(buttonQuestion(),
                title(), buttonRefresh(), buttonAdd(),
                buttonFilter(), filterTextField(), numberField(), valueSelect(),
                valueStatus(), valuePrint(), buttonSettings());
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("Возврат поставщику можно создать на основе приемки или вручную. " +
                        "Читать инструкцию: "),
                new Anchor("#", "Возврат поставщику"));
    }

    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(dto -> formatDate(dto.getDate())).setHeader("Дата и время")
                .setKey("date")
                .setId("Дата и время");
        grid.addColumn(dto -> warehouseService.getById(dto.getWarehouseId()).getName()).setHeader("Со склада")
                .setKey("warehouseDto")
                .setId("Со склада");
        grid.addColumn(dto -> companyService.getById(dto.getCompanyId()).getName()).setHeader("Организация")
                .setKey("companyDto")
                .setId("Организация");
        grid.addColumn(dto -> contractorService.getById(dto.getContractorId()).getName()).setHeader("Контрагент")
                .setKey("contractorDto")
                .setId("Контрагент");
        grid.addColumn(dto -> dto.getProjectId() != null ?
                        projectService.getById(dto.getProjectId()).getName() : "").setHeader("Проект")
                .setKey("projectDto")
                .setId("Проект");
        grid.addColumn(this::getTotalPrice).setHeader("Сумма").setTextAlign(ColumnTextAlign.END)
                .setId("Сумма");
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedSend)).setHeader("Отправлено")
                .setKey("send")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedPrint)).setHeader("Напечатано")
                .setKey("print")
                .setId("Напечатано");
        grid.addColumn("comment").setHeader("Комментарий")
                .setId("Комментарий");
        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        gridConfigurer.addConfigColumnToGrid();

        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addItemClickListener(e -> {
            ReturnToSupplierDto dto = e.getItem();
            ReturnToSupplierModalView modalView = new ReturnToSupplierModalView(returnToSupplierService,
                    companyService,
                    warehouseService,
                    contractorService,
                    contractService,
                    projectService,
                    goodsModalWindow, notifications);
            modalView.setReturnToSupplierForEdit(dto);
            modalView.open();
        });
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("warehouseDto", WarehouseDto::getName, warehouseService.getAll());
        filter.setFieldToComboBox("companyDto", CompanyDto::getName, companyService.getAll());
        filter.setFieldToComboBox("contractorDto", ContractorDto::getName, contractorService.getAll());
        filter.setFieldToComboBox("projectDto", ProjectDto::getName, projectService.getAll());
        filter.setFieldToComboBox("send", Boolean.TRUE, Boolean.FALSE);
        filter.setFieldToComboBox("print", Boolean.TRUE, Boolean.FALSE);
        filter.onSearchClick(e -> paginator
                .setData(returnToSupplierService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(returnToSupplierService.getAll()));
    }

    private List<ReturnToSupplierDto> loadReturnToSuppliers() {
        return returnToSupplierService.getAll();
    }

    private H4 title() {
        H4 title = new H4("Возвраты поставщикам");
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
        Button button = new Button("Возврат", new Icon(VaadinIcon.PLUS_CIRCLE));
        button.addClickListener(e -> modalView.open());
        updateList();
        return button;
    }

    private Button buttonFilter() {
        Button button = new Button("Фильтр");
        button.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return button;
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
        Action onDelete = () -> {
            deleteSelectReturnToSuppliers();
            grid.deselectAll();
            paginator.setData(loadReturnToSuppliers());
        };
        return new SelectExt.SelectBuilder<String>()
                .item(SelectConstants.SELECT_ACTION_SELECT_ITEM)
                .defaultValue(SelectConstants.SELECT_ACTION_SELECT_ITEM)
                .item(SelectConstants.BULK_EDIT_SELECT_ITEM)
                .item("Провести")
                .item("Снять проведение")
                .itemWithAction(SelectConstants.DELETE_SELECT_ITEM, onDelete)
                .width(SelectConstants.SELECT_WIDTH_130PX)
                .build();
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
        List<ReturnToSupplierDto> list1 = returnToSupplierService.getAll();
        for (ReturnToSupplierDto returnDto : list1) {
            sumList.add(getTotalPrice(returnDto));
        }
        PrintReturnToSupplierXml printReturnToSupplierXml = new PrintReturnToSupplierXml(file.getPath(), returnToSupplierService.getAll(), contractorService,warehouseService ,companyService, sumList, employeeService);
        return new Anchor(new StreamResource(templateName, printReturnToSupplierXml::createReport), templateName);
    }



    // Это класс для отладки библиотеки JXLS. Сейчас нерабочий. Нужно доделать.

//    private Anchor getLinkToXlsTemplate(File file) {
//        String templateName = file.getName();
//        List<String> sumList = new ArrayList<>();
//        List<ReturnToSupplierDto> list1 = returnToSupplierService.getAll();
//        for (ReturnToSupplierDto returnDto : list1) {
//            sumList.add(getTotalPrice(returnDto));
//        }
//        PrintTestServiceJXLS printTestServiceJXLS = new PrintTestServiceJXLS();
//        return new Anchor(new StreamResource(templateName, printTestServiceJXLS::createClientReport), templateName);
//    }

//    private Anchor getLinkToPDFTemplate(File file) {
//        List<String> sumList = new ArrayList<>();
//        List<ReturnToSupplierDto> list1 = returnToSupplierService.getAll();
//        for (ReturnToSupplierDto returnDto : list1) {
//            sumList.add(getTotalPrice(returnDto));
//        }
//        PrintReturnToSupplierXml printReturnToSupplierXml = new PrintReturnToSupplierXml(file.getPath(), returnToSupplierService.getAll(), contractorService,warehouseService ,companyService, sumList, employeeService);
//        return new Anchor(new StreamResource("returnToSupplier.pdf", printReturnToSupplierXml::createReportPDF), "returnToSupplier.pdf");
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

    private void updateList() {
        grid.setItems(returnToSupplierService.getAll());
    }

    public void updateList(String nameFilter) {
        if (!(textField.getValue().equals(""))) {
            grid.setItems(returnToSupplierService.searchByString(nameFilter));
        } else {
            grid.setItems(returnToSupplierService.searchByString("null"));
        }
    }

    private String getTotalPrice(ReturnToSupplierDto dto) {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        return String.format("%.2f", totalPrice);
    }

    private Component getIsCheckedSend(ReturnToSupplierDto dto) {
        if (dto.getIsSend()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component getIsCheckedPrint(ReturnToSupplierDto dto) {
        if (dto.getIsPrint()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private void deleteSelectReturnToSuppliers() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (ReturnToSupplierDto item : grid.getSelectedItems()) {
                returnToSupplierService.deleteById(item.getId());
                notifications.infoNotification("Выбранные возвраты успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные возвраты");
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
