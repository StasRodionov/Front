package com.trade_accounting.components.purchases;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.goods.GoodsModalWindow;
import com.trade_accounting.components.purchases.print.PrintReturnToSupplierXml;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.ReturnToSupplierDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.ReturnToSupplierService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
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

@Slf4j
@Route(value = "returnsToSuppliers", layout = AppView.class)
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

    private final Notifications notifications;
    private final ReturnToSupplierModalView modalView;
    private final GoodsModalWindow goodsModalWindow;

    private final List<ReturnToSupplierDto> data;

    private final Grid<ReturnToSupplierDto> grid = new Grid<>(ReturnToSupplierDto.class, false);
    private GridPaginator<ReturnToSupplierDto> paginator;
    private final GridFilter<ReturnToSupplierDto> filter;
    private final String pathForSaveXlsTemplate = "src/main/resources/xls_templates/purchases_templates/return/";

    private final TextField textField = new TextField();

    @Autowired
    public PurchasesSubReturnToSuppliers(EmployeeService employeeService, ReturnToSupplierService returnToSupplierService, WarehouseService warehouseService,
                                         CompanyService companyService, ContractorService contractorService, ContractService contractService,
                                         @Lazy Notifications notifications, ReturnToSupplierModalView modalView, GoodsModalWindow goodsModalWindow) {
        this.employeeService = employeeService;
        this.returnToSupplierService = returnToSupplierService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.contractService = contractService;
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
        horizontalLayout.add(buttonQuestion(), title(), buttonRefresh(), buttonAdd(),
                buttonFilter(), filterTextField(), numberField(), valueSelect(),
                valueStatus(), valuePrint(), buttonSettings());
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    private Grid<ReturnToSupplierDto> configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(dto -> formatDate(dto.getDate())).setKey("date").setHeader("Время").setSortable(true).setId("Дата");
        grid.addColumn(dto -> warehouseService.getById(dto.getWarehouseId()).getName()).setHeader("Со склада")
                .setKey("warehouseDto").setId("Со склада");
        grid.addColumn(dto -> companyService.getById(dto.getCompanyId()).getName()).setHeader("Организация")
                .setKey("companyDto").setId("Организация");
        grid.addColumn(dto -> contractorService.getById(dto.getContractorId()).getName()).setHeader("Контрагент")
                .setKey("contractorDto").setId("Контрагент");
        grid.addColumn(this::getTotalPrice).setHeader("Сумма").setSortable(true);
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedSend)).setKey("send").setHeader("Отправлено")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedPrint)).setKey("print").setHeader("Напечатано")
                .setId("Напечатано");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");
        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addItemDoubleClickListener(e -> {
            ReturnToSupplierDto dto = e.getItem();
            ReturnToSupplierModalView modalView = new ReturnToSupplierModalView(returnToSupplierService,
                    companyService,
                    warehouseService,
                    contractorService,
                    contractService,
                    goodsModalWindow, notifications);
            modalView.setReturnToSupplierForEdit(dto);
            modalView.open();
        });
        return grid;
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("send", Boolean.TRUE, Boolean.FALSE);
        filter.setFieldToComboBox("print", Boolean.TRUE, Boolean.FALSE);
        filter.onSearchClick(e -> paginator
                .setData(returnToSupplierService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(returnToSupplierService.getAll()));
    }

    private List<ReturnToSupplierDto> loadReturnToSuppliers() {
        return returnToSupplierService.getAll();
    }

    private H2 title() {
        H2 title = new H2("Возвраты поставщикам");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Dialog modal = new Dialog();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Html content = new Html("<div><p>Возврат поставщику можно создать на основе <a href=\"#\" target=\"_blank\">приемки</a></p>" +
                "<p>Читать инструкцию: <a href=\"#\" target=\"_blank\">Возврат поставщику</a></p></div>");
        Button close = new Button(new Icon(VaadinIcon.CLOSE));
        close.setWidth("30px");
        close.addClickListener(e -> modal.close());
        horizontalLayout.add(content, new Div(close));
        modal.add(horizontalLayout);
        modal.setWidth("500px");
        modal.setHeight("150px");
        buttonQuestion.addClickListener(e -> modal.open());
        Shortcuts.addShortcutListener(modal, modal::close, Key.ESCAPE);
        return buttonQuestion;
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
        Select<String> select = new Select<>();
        List<String> stringList = new ArrayList<>();
        stringList.add("Изменить");
        stringList.add("Массовое редактирование");
        stringList.add("Провести");
        stringList.add("Снять проведение");
        stringList.add("Удалить");
        select.setItems(stringList);
        select.setValue("Изменить");
        select.setWidth("130px");
        select.addValueChangeListener(e -> {
            if (select.getValue().equals("Удалить")) {
                deleteSelectReturnToSuppliers();
                grid.deselectAll();
                select.setValue("Изменить");
                paginator.setData(loadReturnToSuppliers());
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
        List<ReturnToSupplierDto> list1 = returnToSupplierService.getAll();
        for (ReturnToSupplierDto returnDto : list1) {
            sumList.add(getTotalPrice(returnDto));
        }
        PrintReturnToSupplierXml printReturnToSupplierXml = new PrintReturnToSupplierXml(file.getPath(), returnToSupplierService.getAll(), contractorService,warehouseService ,companyService, sumList, employeeService);
        return new Anchor(new StreamResource(templateName, printReturnToSupplierXml::createReport), templateName);
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
