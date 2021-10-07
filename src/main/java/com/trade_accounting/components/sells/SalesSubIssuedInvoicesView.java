package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.IssuedInvoiceDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.IssuedInvoiceService;
import com.trade_accounting.services.interfaces.PaymentService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Route(value = "IssuedInvoice", layout = AppView.class)
@PageTitle("Счета-фактуры выданные")
@SpringComponent
@UIScope
public class SalesSubIssuedInvoicesView extends VerticalLayout implements AfterNavigationObserver {

    private final IssuedInvoiceService issuedInvoiceService;
    private final CompanyService companyService;
    private final ContractorService contractorService;
    private final PaymentService paymentService;
    private List<IssuedInvoiceDto> data;

    private final GridFilter<IssuedInvoiceDto> filter;
    private final Grid<IssuedInvoiceDto> grid = new Grid<>(IssuedInvoiceDto.class, false);
    private final GridPaginator<IssuedInvoiceDto> paginator;

    public SalesSubIssuedInvoicesView(IssuedInvoiceService issuedInvoiceService, CompanyService companyService, ContractorService contractorService, PaymentService paymentService) {
        this.issuedInvoiceService = issuedInvoiceService;
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.paymentService = paymentService;
        this.data = this.issuedInvoiceService.getAll();
        this.filter = new GridFilter<>(grid);
        add(upperLayout(), filter);
        this.paginator = new GridPaginator<>(grid, data, 100);
        configureGrid();
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);
        add(grid, paginator);
    }

    private void configureGrid() {
        grid.removeAllColumns();
        grid.addColumn("id").setWidth("30px").setHeader("№").setId("№");
        grid.addColumn(dto -> formatDate(dto.getDate())).setFlexGrow(4).setHeader("Время").setId("date");
        grid.addColumn(issuedInvoiceDto -> contractorService.getById(issuedInvoiceDto.getContractorId())
                .getName()).setFlexGrow(10).setHeader("Контрагент").setId("contractor");
        grid.addColumn(issuedInvoiceDto -> companyService.getById(issuedInvoiceDto.getCompanyId())
                .getName()).setHeader("Организация").setId("company");
        grid.addColumn((issuedInvoiceDto -> paymentService.getById(issuedInvoiceDto.getPaymentId()).getSum())).setHeader("Сумма").setId("sum");
        grid.addColumn(new ComponentRenderer<>(this::isSendCheckedIcon)).setFlexGrow(5).setWidth("25px").setKey("send")
                .setHeader("Отправлено").setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::isPrintCheckedIcon)).setFlexGrow(5).setWidth("25px").setKey("print")
                .setHeader("Напечатано").setId("Напечатано");
        grid.addColumn("comment").setFlexGrow(7).setHeader("Комментарий").setId("Комментарий");
        GridSortOrder<IssuedInvoiceDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        grid.setColumnReorderingAllowed(true);
    }

    private Component isSendCheckedIcon(IssuedInvoiceDto issuedInvoiceDto) {
        if (issuedInvoiceDto.getIsSend()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component isPrintCheckedIcon(IssuedInvoiceDto issuedInvoiceDto) {
        if (issuedInvoiceDto.getIsPrint()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private static String formatDate(String date) {
        return LocalDateTime.parse(date)
                .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonFilter(), textField(), numberField(), getSelect(), getStatus(),getPrint(),buttonSettings());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private H2 title() {
        H2 title = new H2("Счета-фактуры выданные");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(e -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private void updateList() {
        GridPaginator<IssuedInvoiceDto> paginatorUpdateList
                = new GridPaginator<>(grid, issuedInvoiceService.getAll(), 100);
        GridSortOrder<IssuedInvoiceDto> order = new GridSortOrder<>(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        grid.sort(Arrays.asList(order));
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(upperLayout(), grid, paginatorUpdateList);
    }

    private Button buttonSettings() {
        Button buttonSettings = new Button(new Icon(VaadinIcon.COG_O));
        return buttonSettings;
    }

    private Button buttonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
    }

    private TextField textField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        return textField;
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> getSelect() {
        Select<String> select = new Select<>();
        select.setItems("Изменить", "Удалить", "Массовое редактирование", "Провести", "Снять проведение");
        select.setValue("Изменить");
        select.setWidth("130px");
        return select;
    }

    private Select<String> getStatus() {
        Select<String> status = new Select<>();
        status.setItems("Статус", "Настроить");
        status.setValue("Статус");
        status.setWidth("130px");
        return status;
    }

    private Select<String> getPrint() {
        Select getPrint = new Select();
        getPrint.setWidth("130px");
        getPrint.setItems("Печать", "Взаиморасчеты");
        getPrint.setValue("Печать");
        return getPrint;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
