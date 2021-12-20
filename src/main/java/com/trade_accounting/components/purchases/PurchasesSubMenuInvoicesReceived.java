package com.trade_accounting.components.purchases;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.InvoiceReceivedDto;
import com.trade_accounting.services.interfaces.AcceptanceService;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.InvoiceReceivedService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Route(value = "invoice_received", layout = AppView.class)
@PageTitle("Счета-фактуры полученные")
@SpringComponent
@UIScope
public class PurchasesSubMenuInvoicesReceived extends VerticalLayout implements AfterNavigationObserver {

    private final InvoiceReceivedService invoiceReceivedService;
    private final CompanyService companyService;
    private final AcceptanceService acceptanceService;
    private final ContractorService contractorService;
    private final Notifications notifications;

    private final List<InvoiceReceivedDto> data;

    private final Grid<InvoiceReceivedDto> grid = new Grid<>(InvoiceReceivedDto.class, false);
    private GridPaginator<InvoiceReceivedDto> paginator;
    private final GridFilter<InvoiceReceivedDto> filter;

    private final TextField textField = new TextField();

    private  final String textForQuestionButton = "<div><p>Входящие счета-фактуры создаются на основе приемки.</p>";

    @Autowired
    public PurchasesSubMenuInvoicesReceived(InvoiceReceivedService invoiceReceivedService,
                                            CompanyService companyService,
                                            AcceptanceService acceptanceService,
                                            ContractorService contractorService,
                                            @Lazy Notifications notifications) {
        this.invoiceReceivedService = invoiceReceivedService;
        this.companyService = companyService;
        this.acceptanceService = acceptanceService;
        this.contractorService = contractorService;
        this.notifications = notifications;
        this.data = getData();
        this.filter = new GridFilter<>(grid);
        paginator = new GridPaginator<>(grid, data, 50);
        configureGrid();
        add(configureActions(), filter, grid, paginator);
    }

    private List<InvoiceReceivedDto> getData() {
        for(InvoiceReceivedDto invoiceReceivedDto : invoiceReceivedService.getAll()){
            System.out.println(invoiceReceivedDto.getId());
        }
        return invoiceReceivedService.getAll();
    }

    private HorizontalLayout configureActions() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(Buttons.buttonQuestion(textForQuestionButton,"120px"), title(), buttonRefresh(),
                buttonFilter(), filterTextField(), numberField(), valueSelect(),
                valueStatus(), valuePrint(), buttonSettings());
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    private Grid<InvoiceReceivedDto> configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(dto -> formatDate(dto.getData())).setKey("data").setHeader("Время").setSortable(true).setId("Дата");
        grid.addColumn(dto -> contractorService.getById(dto.getContractorId()).getName()).setHeader("Контрагент").setKey("contractorDto")
                .setId("Контрагент");
        grid.addColumn(dto -> companyService.getById(dto.getCompanyId()).getName()).setHeader("Организация")
                .setKey("companyDto").setId("Организация");

        grid.addColumn(this::getTotalPrice).setHeader("Сумма").setSortable(true);

        grid.addColumn(InvoiceReceivedDto::getIncomNumber).setKey("incomNumber").setHeader("Входящий номер").setSortable(true).setId("Входящий номер");
        grid.addColumn(dto -> formatDate(dto.getIncomData())).setKey("incomData").setHeader("Входящая дата").setSortable(true).setId("Входящая дата");

        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedSend)).setKey("send").setHeader("Отправлено")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedPrint)).setKey("print").setHeader("Напечатано")
                .setId("Напечатано");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");
        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        return grid;
    }

    private String formatDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
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
        Button button = new Button("Фильтр");
        return button;
    }

    private TextField filterTextField() {
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
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
        Select<String> select = new Select<>();
        List<String> stringList = new ArrayList<>();
        stringList.add("Изменить");
        stringList.add("Удалить");
        select.setItems(stringList);
        select.setValue("Изменить");
        select.setWidth("130px");
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
        print.setItems("Печать");
        print.setValue("Печать");
        print.setWidth("130px");
        return print;
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private void updateList() {
        grid.setItems(invoiceReceivedService.getAll());
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
