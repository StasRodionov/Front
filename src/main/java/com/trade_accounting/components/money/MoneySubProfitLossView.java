package com.trade_accounting.components.money;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.finance.MoneyArticleProfitLossDto;
import com.trade_accounting.models.dto.finance.MoneySubProfitLossDto;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.finance.MoneySubProfitLossService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.trade_accounting.config.SecurityConstants.MONEY_MONEY_SUB_PROFIT_LOSS_VIEW;

//Если на страницу не ссылаются по URL или она не является отдельной страницей, а подгружается родительским классом, то URL и Title не нужен
/*@Route(value = MONEY_MONEY_SUB_PROFIT_LOSS_VIEW, layout = AppView.class)
@PageTitle("Прибыли и убытки")*/
public class MoneySubProfitLossView extends VerticalLayout {


    private final MoneySubProfitLossService moneySubProfitLossService;
    private final CompanyService companyService;

    private Long companyId;
    private LocalDate startDatePeriod;
    private LocalDate endDatePeriod;
    private MoneySubProfitLossDto data;

    private List<MoneyArticleProfitLossDto> listDataView;
    private Grid<MoneyArticleProfitLossDto> grid;
    private final EmployeeService employeeService;
    private HorizontalLayout filter;

    public MoneySubProfitLossView(MoneySubProfitLossService moneySubProfitLossService,
                                  EmployeeService employeeService,
                                  CompanyService companyService) {
        this.moneySubProfitLossService = moneySubProfitLossService;
        this.data = getData();
        this.companyService = companyService;
        this.employeeService = employeeService;
        this.listDataView = new ArrayList<>();
        this.grid = new Grid<>(MoneyArticleProfitLossDto.class, false);
        this.filter = new HorizontalLayout();
        configureListDataView();
        configureGrid();
        configureFilter();
        //setHorizontalComponentAlignment(Alignment.BASELINE, filter);
        add(getToolbar(), filter, grid);
    }

    private MoneySubProfitLossDto getData() {
        return moneySubProfitLossService.getAll();
    }

    private void configureFilter() {
        filter.removeAll();
        filter.add(getFindButton(),
                getClearButton(),
                new Button(new Icon(VaadinIcon.COG_O)),
                getDatePickerDateRange().get(0),
                getDatePickerDateRange().get(1),
                getComboBoxCompany());
        filter.getStyle().set("background-color", "#e7eaef")
                .set("border-radius", "4px")
                .set("align-items", "baseline")
                .set("flex-flow", "row wrap");

        filter.setJustifyContentMode(JustifyContentMode.CENTER);
        filter.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        filter.setWidthFull();

        filter.setVisible(false);
    }

    private Component getClearButton() {
        Button filterButton = new Button("Очистить");
        filterButton.addClickListener(event -> {
            data = moneySubProfitLossService.getAll();
            configureFilter();
            updateList();
        });
        return filterButton;
    }

    private Button getFindButton() {
        Button filterButton = new Button("Найти");
        filterButton.addClickListener(event -> {
            data = moneySubProfitLossService.filter(startDatePeriod, endDatePeriod, companyId);
            updateList();
//            companyId = null;
//            startDatePeriod = null;
//            endDatePeriod = null;
        });
        return filterButton;
    }

    public void updateList() {
        //removeAll();
        configureListDataView();
        configureGrid();
        //configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER);
        //add(getToolbar(), filter, grid);
        //filter.setVisible(true);
    }

    private void configureGrid() {
        grid.removeAllColumns();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setItems(listDataView);
        grid.addColumn("article").setFlexGrow(11).setHeader("Статья").setId("Статья");
        grid.addColumn("profitLoss").setFlexGrow(11).setHeader("Прибыль(убытки)").setId("Прибыль(убытки)");
    }

    private void configureListDataView() {
        listDataView.clear();
        listDataView.add(new MoneyArticleProfitLossDto("Выручка", data.getRevenue()));
        listDataView.add(new MoneyArticleProfitLossDto("Себестоимость", data.getCostPrice()));
        listDataView.add(new MoneyArticleProfitLossDto("Валовая прибыль", data.getGrossProfit()));
        listDataView.add(new MoneyArticleProfitLossDto("Операционные расходы", data.getOperatingExpenses()));
        listDataView.add(new MoneyArticleProfitLossDto("Списания", data.getWriteOffs()));
        listDataView.add(new MoneyArticleProfitLossDto("Аренда", data.getRental()));
        listDataView.add(new MoneyArticleProfitLossDto("Зарплата", data.getSalary()));
        listDataView.add(new MoneyArticleProfitLossDto("Маркетинг и реклама", data.getMarketing()));
        listDataView.add(new MoneyArticleProfitLossDto("Операционная прибыль", data.getOperatingProfit()));
        listDataView.add(new MoneyArticleProfitLossDto("Налоги и сборы", data.getTaxesAndFees()));
        listDataView.add(new MoneyArticleProfitLossDto("Чистая прибыль", data.getNetProfit()));
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), getTextContract(), getButtonRefresh(),
                getButtonFilter(), getPrintMenu());
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolbar;
    }

    private Button getButtonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
    }

    public List<DatePicker> getDatePickerDateRange() {
        List<DatePicker> dates = new ArrayList<>();
        DatePicker dDate = new DatePicker("Начальная дата");
        DatePicker rDate = new DatePicker("Конечная дата");
        dDate.addValueChangeListener(event -> startDatePeriod = event.getValue());
        rDate.addValueChangeListener(event -> endDatePeriod = event.getValue());
        dates.add(dDate);
        dates.add(rDate);
        return dates;
    }

    private Button getButtonRefresh() {
        final Button buttonRefresh;
        Icon circle = new Icon(VaadinIcon.REFRESH);
        buttonRefresh = new Button();
        buttonRefresh.setIcon(circle);
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        buttonRefresh.addClickListener(event -> {
            data = moneySubProfitLossService.filter(startDatePeriod, endDatePeriod, companyId);
            updateList();
//            companyId = null;
//            startDatePeriod = null;
//            endDatePeriod = null;
        });
        return buttonRefresh;
    }

    private H2 getTextContract() {
        final H2 textCompany = new H2("Прибыли и убытки");
        textCompany.setHeight("2.2em");
        return textCompany;
    }

    private Button getButtonQuestion() {
        String description = "Отчет отражает доходы и расходы организации и позволяет увидеть чистую прибыль за выбранный период.\n" +
                "\n" +
                "Выручка = Отгрузки + Розничные продажи − Возвраты\n" +
                "\n" +
                "Себестоимость — себестоимость проданных товаров\n" +
                "\n" +
                "Валовая прибыль = Выручка − Себестоимость\n" +
                "\n" +
                "Операционные расходы — сумма затрат, кроме статей Закупка товаров, Налоги и сборы и Возврат\n" +
                "\n" +
                "Операционная прибыль = Валовая прибыль − Операционные расходы\n" +
                "\n" +
                "Налоги и сборы — сумма затрат со статьей Налоги и сборы\n" +
                "\n" +
                "Чистая прибыль = Операционная прибыль − Сумма налогов и сборов";
        return Buttons.buttonQuestion(description);
    }

    public ComboBox<CompanyDto> getComboBoxCompany() {
        ComboBox<CompanyDto> companyComboBox = new ComboBox();
        companyComboBox.setLabel("Выберете компанию");
        companyComboBox.setItems(companyService.getAll());
        companyComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyComboBox.addValueChangeListener(event -> companyId = event.getValue().getId());
        return companyComboBox;
    }

    /*Меню Печать*/
    private MenuBar getPrintMenu() {

        MenuBar menuBar = new MenuBar();
        Icon printIcon = new Icon(VaadinIcon.PRINT);
        menuBar.addThemeVariants(MenuBarVariant.LUMO_ICON);

        MenuItem print = menuBar.addItem(printIcon, e -> {
        });
        print.add("Печать");
        SubMenu printSubMenu = print.getSubMenu();
        printSubMenu.addItem("Прибыль и убытки", menuItemClickEvent -> {
            ProfitLossPrintModal profitLossPrintModalModal = new ProfitLossPrintModal(data, employeeService, startDatePeriod, endDatePeriod);
            profitLossPrintModalModal.open();
        });
        return menuBar;
    }

    private MenuItem createIconItem(HasMenuItems menu, VaadinIcon iconName, String label, String ariaLabel) {
        return createIconItem(menu, iconName, label, ariaLabel, false);
    }

    private MenuItem createIconItem(HasMenuItems menu, VaadinIcon iconName, String label, String ariaLabel, boolean isChild) {
        Icon icon = new Icon(iconName);

        if (isChild) {
            icon.getStyle().set("width", "var(--lumo-icon-size-s)");
            icon.getStyle().set("height", "var(--lumo-icon-size-s)");
            icon.getStyle().set("marginRight", "var(--lumo-space-s)");
        }

        MenuItem item = menu.addItem(icon, e -> {
        });

        if (ariaLabel != null) {
            item.getElement().setAttribute("aria-label", ariaLabel);
        }

        if (label != null) {
            item.add(new Text(label));
        }

        return item;
    }
}