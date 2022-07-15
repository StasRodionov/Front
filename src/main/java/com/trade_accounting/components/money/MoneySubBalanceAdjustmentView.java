package com.trade_accounting.components.money;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridConfigurer;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.Action;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.finance.BalanceAdjustmentDto;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.services.interfaces.finance.BalanceAdjustmentService;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.finance.PaymentService;
import com.trade_accounting.services.interfaces.util.ColumnsMaskService;
import com.trade_accounting.services.interfaces.util.ProjectService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.trade_accounting.config.SecurityConstants.GRID_MONEY_MAIN_BALANCE_ADJUSTMENT;
import static com.trade_accounting.config.SecurityConstants.MONEY_BALANCE_ADJUSTMENT_VIEW;

@Slf4j
@Route(value = MONEY_BALANCE_ADJUSTMENT_VIEW, layout = AppView.class)
@PageTitle("Корректировки")
@SpringComponent
@UIScope
public class MoneySubBalanceAdjustmentView extends VerticalLayout implements AfterNavigationObserver {

    private final BalanceAdjustmentService balanceAdjustmentService;
    private final CompanyService companyService;
    private final ContractorService contractorService;

    private final Notifications notifications;
    private final BalanceAdjustmentModalView modalView;

    private final List<BalanceAdjustmentDto> data;

    private final Grid<BalanceAdjustmentDto> grid = new Grid<>(BalanceAdjustmentDto.class, false);
    private final GridConfigurer<BalanceAdjustmentDto> gridConfigurer;
    private GridPaginator<BalanceAdjustmentDto> paginator;
    private final GridFilter<BalanceAdjustmentDto> filter;

    private final String pathForSaveXlsTemplate = "src/main/resources/xls_templates/adjustments_templates/";
    private final transient ProjectService projectService;
    private final transient ContractService contractService;
    private final transient PaymentService paymentService;
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES,
            GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};

    private final TextField textField = new TextField();

    @Autowired
    public MoneySubBalanceAdjustmentView(BalanceAdjustmentService balanceAdjustmentService, CompanyService companyService,
                                         ContractorService contractorService, @Lazy Notifications notifications,
                                         BalanceAdjustmentModalView modalView, ProjectService projectService,
                                         ContractService contractService, PaymentService paymentService,
                                         ColumnsMaskService columnsMaskService) {
        this.balanceAdjustmentService = balanceAdjustmentService;
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.notifications = notifications;
        this.modalView = modalView;
        this.projectService = projectService;
        this.contractService = contractService;
        this.paymentService = paymentService;
        this.data = loadBalanceAdjustments();
        this.gridConfigurer = new GridConfigurer<>(grid, columnsMaskService, GRID_MONEY_MAIN_BALANCE_ADJUSTMENT);
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
                valuePrint(), buttonSettings());
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(dto -> formatDate(dto.getDate())).setKey("date").setHeader("Время").setId("Дата");
        grid.addColumn(dto -> companyService.getById(dto.getCompanyId()).getName()).setHeader("Организация")
                .setKey("companyDto").setId("Организация");
        grid.addColumn(dto -> contractorService.getById(dto.getContractorId()).getName()).setHeader("Контрагент")
                .setKey("contractorDto").setId("Контрагент");
        grid.addColumn("account").setHeader("Счет").setId("Счет");
        grid.addColumn("cashOffice").setHeader("Касса").setId("Касса");
        grid.addColumn(this::getTotalPrice).setHeader("Сумма").setTextAlign(ColumnTextAlign.END)
                .setKey("sum").setId("Сумма");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");
        grid.addColumn("dateChanged").setHeader("Когда изменен").setId("Когда изменен");
        grid.addColumn("whoChanged").setHeader("Кто изменил").setId("Кто изменил");
        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        gridConfigurer.addConfigColumnToGrid();

        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addItemDoubleClickListener(e -> {
            BalanceAdjustmentDto dto = e.getItem();
            BalanceAdjustmentModalView modalView = new BalanceAdjustmentModalView(balanceAdjustmentService,
                    companyService,
                    contractorService,
                    notifications);
            modalView.setBalanceAdjustmentForEdit(dto);
            modalView.open();
        });
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("companyDto", CompanyDto::getName, companyService.getAll());
        filter.setFieldToComboBox("contractorDto", ContractorDto::getName, contractorService.getAll());
        filter.setFieldToComboBox("account", BalanceAdjustmentDto::getAccount, balanceAdjustmentService.getAll());
        filter.setFieldToComboBox("cashOffice", BalanceAdjustmentDto::getCashOffice, balanceAdjustmentService.getAll());
        filter.onSearchClick(e -> paginator
                .setData(balanceAdjustmentService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(balanceAdjustmentService.getAll()));
    }

    private List<BalanceAdjustmentDto> loadBalanceAdjustments() {
        return balanceAdjustmentService.getAll();
    }

    private H2 title() {
        H2 title = new H2("Корректировки");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion("Добавьте описание");
    }

    private Button buttonRefresh() {
        Button button = new Button(new Icon(VaadinIcon.REFRESH));
        button.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        button.addClickListener(e -> updateList());
        return button;
    }

    private Button buttonAdd() {
        Button button = new Button("Корректировка", new Icon(VaadinIcon.PLUS_CIRCLE));
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
        return SelectConfigurer.configureDeleteSelect(() -> {
            deleteSelectBalanceAdjustments();
            grid.deselectAll();
            paginator.setData(loadBalanceAdjustments());
        });
    }

    private Select<String> valuePrint() {
        Select<String> print = SelectConfigurer.configurePrintSelect();
        getXlsFiles().forEach(x -> print.add(getLinkToXlsTemplate(x)));
        return print;
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
        grid.setItems(balanceAdjustmentService.getAll());
    }

    public void updateList(String nameFilter) {
        if (!(textField.getValue().equals(""))) {
            grid.setItems(balanceAdjustmentService.searchByString(nameFilter));
        } else {
            grid.setItems(balanceAdjustmentService.searchByString("null"));
        }
    }

    private String getTotalPrice(BalanceAdjustmentDto dto) {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        return String.format("%.2f", totalPrice);
    }

    private void deleteSelectBalanceAdjustments() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (BalanceAdjustmentDto item : grid.getSelectedItems()) {
                balanceAdjustmentService.deleteById(item.getId());
                notifications.infoNotification("Выбранные корректировки успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные корректировки");
        }
    }

    private List<File> getXlsFiles() {
        File dir = new File(pathForSaveXlsTemplate);
        return Arrays.stream(Objects.requireNonNull(dir.listFiles())).filter(File::isFile).filter(x -> x.getName()
                .contains(".xls")).collect(Collectors.toList());
    }

    private Anchor getLinkToXlsTemplate(File file) {
        String paymentsTemplate = file.getName();
        PrintPaymentsXls printPaymentsXls = new PrintPaymentsXls(
                file.getPath(), paymentService.getAll(), companyService, contractorService, contractService, projectService);
        return new Anchor(new StreamResource(paymentsTemplate, printPaymentsXls::createReport), "Скачать в формате Excel");
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}