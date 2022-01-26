package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.BuyersReturnDto;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.BuyersReturnService;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.ShipmentProductService;
import com.trade_accounting.services.interfaces.ShipmentService;
import com.trade_accounting.services.interfaces.WarehouseService;
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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Route(value = "buyersReturns", layout = AppView.class)
@PageTitle("Возвраты покупателей")
@SpringComponent
@UIScope
public class SalesSubBuyersReturnsView extends VerticalLayout implements AfterNavigationObserver {
    private final TextField textField = new TextField();
    private final BuyersReturnService buyersReturnService;
    private final ContractorService contractorService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final Notifications notifications;
    //    private List<BuyersReturnDto> data;
    private final Grid<BuyersReturnDto> grid = new Grid<>(BuyersReturnDto.class, false);
    private final GridPaginator<BuyersReturnDto> paginator;
    private final GridFilter<BuyersReturnDto> filter;
    ContractService contractService;
    private final MenuBar selectXlsTemplateButton = new MenuBar();
    private final MenuItem print;
    private final ProductService productService;
    private final ShipmentService shipmentService;
    private final ShipmentProductService shipmentProductService;
    private final String pathForSaveXlsTemplate = "src/main/resources/xls_templates/salesSubBuyersReturns_templates/";
    private final ReturnBuyersReturnModalView returnBuyersReturnModalView;

    @Autowired
    public SalesSubBuyersReturnsView(BuyersReturnService buyersReturnService,
                                     ContractorService contractorService,
                                     CompanyService companyService,
                                     WarehouseService warehouseService,
                                     Notifications notifications,
                                     ProductService productService,
                                     ShipmentService shipmentService,
                                     ShipmentProductService shipmentProductService,
                                     ReturnBuyersReturnModalView returnBuyersReturnModalView) {
        this.buyersReturnService = buyersReturnService;
        this.warehouseService = warehouseService;
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.shipmentProductService = shipmentProductService;
        this.productService = productService;
        this.shipmentService = shipmentService;
        this.notifications = notifications;
        this.returnBuyersReturnModalView = returnBuyersReturnModalView;
        print = selectXlsTemplateButton.addItem("Печать");

        List<BuyersReturnDto> data = getData();
        paginator = new GridPaginator<>(grid, data, 50);
        configureGrid();
        this.filter = new GridFilter<>(this.grid);
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(getToolbar(), filter, grid, paginator);
        configureSelectXlsTemplateButton();
    }

    private void configureFilter() {
        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("isSent", Boolean.TRUE, Boolean.FALSE);
        filter.setFieldToComboBox("isPrint", Boolean.TRUE, Boolean.FALSE);
        filter.setFieldToComboBox("company", CompanyDto::getName,companyService.getAll());
        filter.setFieldToComboBox("contractor", ContractorDto::getName,contractorService.getAll());
        filter.setFieldToComboBox("warehouse", WarehouseDto::getName,warehouseService.getAll());
        filter.onSearchClick(e -> {
            Map<String, String> map = filter.getFilterData2();
            paginator.setData(buyersReturnService.searchByFilter(map));
        });
        filter.onClearClick(e -> paginator.setData(getData()));
    }



    private void configureSelectXlsTemplateButton() {
        SubMenu printSubMenu = print.getSubMenu();
        printSubMenu.removeAll();
        templatesXlsMenuItems(printSubMenu);
        uploadXlsMenuItem(printSubMenu);
    }

    private void templatesXlsMenuItems(SubMenu subMenu) {
        getXlsFiles().forEach(x -> subMenu.addItem(getLinkToXlsTemplate(x)));
    }

    private List<File> getXlsFiles() {
        File dir = new File(pathForSaveXlsTemplate);
        return Arrays.stream(Objects.requireNonNull(dir.listFiles())).filter(File::isFile).filter(x -> x.getName()
                .contains(".xls")).collect(Collectors.toList());
    }

    private Anchor getLinkToXlsTemplate(File file) {
        String templateName = file.getName();
        List<String> sumList = new ArrayList<>();
        List<BuyersReturnDto> list1 = buyersReturnService.getAll();
        PrintSalesSubBuyersReturnsXls printSalesSubBuyersReturnsXls = new PrintSalesSubBuyersReturnsXls(file.getPath(), buyersReturnService.getAll(),
                contractorService, companyService, sumList);
        return new Anchor(new StreamResource(templateName, printSalesSubBuyersReturnsXls::createReport), templateName);
    }

    private void uploadXlsMenuItem(SubMenu subMenu) {
        MenuItem menuItem = subMenu.addItem("добавить шаблон");
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

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(BuyersReturnDto::getDate).setFlexGrow(7).setHeader("Время").setKey("date").setId("Дата");
        grid.addColumn(dto -> warehouseService.getById(dto.getWarehouseId())
                .getName()).setKey("warehouse").setFlexGrow(7).setFlexGrow(7).setHeader("На склад").setId("На склад");
        grid.addColumn(dto -> contractorService.getById(dto.getContractorId())
                .getName()).setFlexGrow(7).setHeader("Контрагент").setKey("contractor").setId("Контрагент");
        grid.addColumn(dto -> companyService.getById(dto.getCompanyId()).getName()).setFlexGrow(7).setHeader("Компания")
                .setKey("company").setId("Компания");
        grid.addColumn("sum").setFlexGrow(7).setHeader("Сумма").setId("Сумма");
        grid.addColumn("isSent").setFlexGrow(7).setHeader("Отправлено").setId("Отправлено");
        grid.addColumn("isPrint").setFlexGrow(7).setHeader("Напечатано").setId("Напечатано");
        grid.addColumn("comment").setFlexGrow(7).setHeader("Комментарий").setId("Комментарий");
        grid.addItemDoubleClickListener(event -> {
            BuyersReturnDto buyersReturnDto = event.getItem();
            ReturnBuyersReturnModalView view = new ReturnBuyersReturnModalView(buyersReturnService,
                    contractorService,
                    warehouseService,
                    companyService,
                    notifications,
                    productService,
                    shipmentService,
                    shipmentProductService,
                    contractService);
            view.setReturnEdit(buyersReturnDto);
            view.open();
        });
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(buttonQuestion(), title(), getButtonRefresh(), buttonUnit(), getButtonFilter(), selectXlsTemplateButton, textField(),
                numberField(), getSelect(), getStatus(), buttonSettings());
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return toolbar;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("Возврат можно создать на основании отгрузки или без основания — это удобно, " +
                        "если нужно сделать возврат сразу на несколько отгрузок. " +
                        "Возвраты влияют на остатки по складам в разделе Товары → Остатки. " +
                        "Читать инструкцию: "),
                new Anchor("#", "Возвраты покупателей"));
    }

    private H4 title() {
        H4 title = new H4("Возвраты покупателей");
        title.setHeight("2.2em");
        title.setWidth("80px");
        return title;
    }

    public Button buttonUnit() {
        Button buttonUnit = new Button("Возврат покупателя", new Icon(VaadinIcon.PLUS_CIRCLE));
        BuyersReturnDto buyersReturnDto = new BuyersReturnDto();
        buttonUnit.addClickListener(event -> {
            buyersReturnDto.setIsNew(true);
            ReturnBuyersReturnModalView view = new ReturnBuyersReturnModalView(
                    buyersReturnService,
                    contractorService,
                    warehouseService,
                    companyService,
                    notifications,
                    productService,
                    shipmentService,
                    shipmentProductService,
                    contractService);
            view.setReturnEdit(buyersReturnDto);
            returnBuyersReturnModalView.open();
        });
        return buttonUnit;
    }

    private Button getButtonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
    }

    private Button getButtonRefresh() {
        final Button buttonRefresh;
        Icon circle = new Icon(VaadinIcon.REFRESH);
        buttonRefresh = new Button();
        buttonRefresh.setIcon(circle);
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private static String formatDate(String date) {
        return LocalDateTime.parse(date)
                .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
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
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> updateList(textField.getValue()));
        return textField;
    }

    private void updateList(String text) {
        grid.setItems(buyersReturnService.findBySearch(text));
    }

    private Select<String> getSelect() {
        Select<String> select = new Select<>();
        select.setItems("Изменить", "Удалить", "Массовое редактирование", "Провести", "Снять проведение");
        select.setValue("Изменить");
        select.setWidth("130px");
        select.addValueChangeListener(event -> {
            if (select.getValue().equals("Удалить")) {
                deleteSelectedBuyersReturn();
                grid.deselectAll();
                select.setValue("Изменить");
                paginator.setData(getData());
            }
        });
        return select;
    }

    private Select<String> getStatus() {
        Select<String> status = new Select<>();
        status.setItems("Статус", "Настроить");
        status.setValue("Статус");
        status.setWidth("130px");
        return status;
    }

    private void deleteSelectedBuyersReturn() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (BuyersReturnDto buyersReturnDto : grid.getSelectedItems()) {
                buyersReturnService.deleteById(buyersReturnDto.getId());
                notifications.infoNotification("Выбранные заказы успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные заказы");
        }
    }

    private List<BuyersReturnDto> getData() {
        return buyersReturnService.getAll();
    }

    public void updateData() {
        paginator.setData(getData(), false);
    }

    private void updateList() {

        grid.setItems(buyersReturnService.getAll());
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
