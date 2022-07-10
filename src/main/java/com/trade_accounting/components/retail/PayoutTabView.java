package com.trade_accounting.components.retail;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridConfigurer;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.finance.PayoutDto;
import com.trade_accounting.models.dto.retail.RetailStoreDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.finance.PayoutService;
import com.trade_accounting.services.interfaces.retail.RetailStoreService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
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
import com.vaadin.flow.data.provider.SortDirection;
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
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.trade_accounting.config.SecurityConstants.RETAIL_PAYOUT_VIEW;

@Slf4j
//Если на страницу не ссылаются по URL или она не является отдельной страницей, а подгружается родительским классом, то URL и Title не нужен
/*@Route(value = RETAIL_PAYOUT_VIEW, layout = AppView.class)
@PageTitle("Выплаты")*/
@SpringComponent
@UIScope
public class PayoutTabView extends VerticalLayout implements AfterNavigationObserver {

    private final PayoutService payoutService;
    private final RetailStoreService retailStoreService;
    private final CompanyService companyService;
    private final EmployeeService employeeService;

    private final Grid<PayoutDto> grid = new Grid<>(PayoutDto.class, false);
    private final GridConfigurer<PayoutDto> gridConfigurer = new GridConfigurer<>(grid);
    private final GridPaginator<PayoutDto> paginator;
    private final Notifications notifications;

    private final List<PayoutDto> data;

    private final TextField textFieldUpdateTextField = new TextField();
    private final PayoutModalWindow payoutModalWindow;
    private final String typeOfInvoice = "RECEIPT";
    private final String pathForSaveSalesXlsTemplate = "src/main/resources/xls_templates/payouts_templates/";
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};

    private final GridFilter<PayoutDto> filter;

    private final TextField textField = new TextField();

    public PayoutTabView(PayoutService payoutService, RetailStoreService retailStoreService,
                         CompanyService companyService, EmployeeService employeeService, Notifications notifications) {
        this.payoutService = payoutService;
        this.retailStoreService = retailStoreService;
        this.companyService = companyService;
        this.payoutModalWindow = new PayoutModalWindow(payoutService);
        this.data = payoutService.getAll();
        this.employeeService = employeeService;
        this.notifications = notifications;

        this.paginator = new GridPaginator<>(grid, data, 100);
        configureGrid();

        this.filter = new GridFilter<>(grid);
        configureFilter();

        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(upperLayout(), filter, grid, paginator);
    }

    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(dto -> formatDate(dto.getDate())).setKey("date").setHeader("Дата и время").setId("Дата и время");
        grid.addColumn(dto -> retailStoreService.getById(dto.getRetailStoreId()).getName()).setHeader("Точка продаж")
                .setKey("retailDto").setId("Точка продаж");
        grid.addColumn("whoWasPaid").setHeader("Кому").setId("Кому");
        grid.addColumn(dto -> companyService.getById(dto.getCompanyId()).getName()).setHeader("Организация")
                .setKey("companyDto").setId("Организация");
        grid.addColumn(dto -> retailStoreService.getById(dto.getRetailStoreId()).getRevenue()).setHeader("Сумма")
                .setKey("retailSumDto").setId("Сумма");
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedSend)).setKey("send").setHeader("Отправлено")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedPrint)).setKey("print").setHeader("Напечатано")
                .setId("Напечатано");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");
        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        gridConfigurer.addConfigColumnToGrid();

        grid.setHeight("64vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addItemDoubleClickListener(event -> {
            payoutModalWindow.addDetachListener(e -> updateList());
            payoutModalWindow.open();
        });

        GridSortOrder<PayoutDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("retailDto", RetailStoreDto::getName, retailStoreService.getAll());
        filter.setFieldToComboBox("whoWasPaid",
                s -> s,
                payoutService.getAll().stream()
                        .map(PayoutDto::getWhoWasPaid)
                        .distinct()
                        .collect(Collectors.toList()));
        filter.setFieldToComboBox("companyDto", CompanyDto::getName, companyService.getAll());
        filter.setFieldToCheckBox("print");
        filter.setFieldToCheckBox("send");
        filter.onSearchClick(e -> {
            Map<String, String> map = filter.getFilterData();
            map.put("typeOfInvoice", typeOfInvoice);
            paginator.setData(payoutService.searchByFilter(map));
        });
        filter.onClearClick(e -> paginator.setData(payoutService.getAll()));
    }

    private Component getIsCheckedSend(PayoutDto dto) {
        if (dto.getIsSent()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component getIsCheckedPrint(PayoutDto dto) {
        if (dto.getIsPrint()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private String formatDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime formatDateTime = LocalDateTime.parse(stringDate);
        return formatDateTime.format(formatter);
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonCreate(), buttonFilter(), textField(),
                numberField(), valueSelect(), valueStatus(), valuePrint(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upper;
    }

    private H2 title() {
        H2 title = new H2("Выплаты");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("Выплаты фиксируют выдачу наличных денег из точки продаж. " +
                        "Читать инструкцию: "),
                new Anchor("#", "Выплаты"));
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(e -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
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

    private void updateList(String search) {
        if (search.isEmpty()) {
            paginator.setData(payoutService.getAll());
        } else paginator.setData(payoutService
                .findBySearchAndTypeOfPayout(search, typeOfInvoice));
    }

    private TextField textField() {
        textFieldUpdateTextField.setPlaceholder("Номер или комментарий");
        textFieldUpdateTextField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textFieldUpdateTextField.setWidth("300px");
        textFieldUpdateTextField.setValueChangeMode(ValueChangeMode.EAGER);
        textFieldUpdateTextField.addValueChangeListener(e ->
                updateListTextField());
        return textFieldUpdateTextField;
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

    private void deleteSelectedInvoices() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (PayoutDto payoutDto : grid.getSelectedItems()) {
                payoutService.deleteById(payoutDto.getId());
                notifications.infoNotification("Выбранные выплаты успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные выплаты");
        }
    }

    private Select<String> valueStatus() {
        return SelectConfigurer.configureStatusSelect();
    }

    private Button buttonCreate() {
        Button createRetailStoreButton = new Button("Выплата", new Icon(VaadinIcon.PLUS_CIRCLE));
        PayoutModalWindow payoutModalWindow =
                new PayoutModalWindow(payoutService);
        createRetailStoreButton.addClickListener(e -> {
            payoutModalWindow.addDetachListener(event -> updateList());
            payoutModalWindow.open();
        });
        createRetailStoreButton.getStyle().set("cursor", "pointer");
        return createRetailStoreButton;
    }

    private Select<String> valuePrint() {
        return SelectConfigurer.configurePrintSelect();
    }


    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }


    private void updateList() {
        GridPaginator<PayoutDto> paginatorUpdateList
                = new GridPaginator<>(grid, payoutService.getAll(), 100);
        GridSortOrder<PayoutDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(upperLayout(), grid, paginatorUpdateList);
    }

    public void updateListTextField() {
        if (!(textFieldUpdateTextField.getValue().equals(""))) {
            grid.setItems(payoutService.getAllByParameters(textFieldUpdateTextField.getValue()));
        } else {
            grid.setItems(payoutService.getAllByParameters("null"));
        }
    }

    private List<PayoutDto> getData() {
        return payoutService.getAll();
    }


    private Anchor getLinkToSalesXls(File file) {
        String salesTemplate = file.getName();
        List<String> sumList = new ArrayList<>();
        List<RetailStoreDto> list1 = retailStoreService.getAll();
        for (RetailStoreDto rS : list1) {
            sumList.add(getTotalPrice(rS));
        }
        PrintPayoutXls printSalesXls = new PrintPayoutXls(file.getPath(), payoutService.getAll(),
                sumList, employeeService);
        return new Anchor(new StreamResource(salesTemplate, printSalesXls::createReport), salesTemplate);
    }

    private String getTotalPrice(RetailStoreDto retailStoreDto) {
        List<RetailStoreDto> payoutDtoList = retailStoreService.getAll();
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        for (RetailStoreDto retailStoreDto1 : payoutDtoList) {
            totalPrice = totalPrice.add(retailStoreDto.getRevenue()
                    .multiply(retailStoreDto1.getRevenue()));
        }
        return String.format("%.2f", totalPrice);
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

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
