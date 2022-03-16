package com.trade_accounting.components.money;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.finance.MoneyArticleProfitLossDto;
import com.trade_accounting.models.dto.finance.MoneySubCashFlowDto;
import com.trade_accounting.models.dto.finance.MoneySubProfitLossDto;
import com.trade_accounting.models.dto.util.ProjectDto;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.finance.MoneySubProfitLossService;
import com.trade_accounting.services.interfaces.util.ProjectService;
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

@Route(value = "MoneySubProfitLossView", layout = AppView.class)
@PageTitle("Прибыли и убытки")
public class MoneySubProfitLossView extends VerticalLayout {


    private final MoneySubProfitLossService moneySubProfitLossService;
    private final CompanyService companyService;
    //    private final ContractorService contractorService;
    private final ProjectService projectService;
//    private final ContractService contractService;
//    private final Notifications notifications;

    private Long projectId;
    private Long companyId;
    private LocalDate startDatePeriod;
    private LocalDate endDatePeriod;
    private MoneySubProfitLossDto data;

    List<MoneyArticleProfitLossDto> listDataView = new ArrayList<>();
    private final Grid<MoneyArticleProfitLossDto> grid = new Grid<>(MoneyArticleProfitLossDto.class, false);
    //private final Grid<MoneySubProfitLossDto> grid = new Grid<>(MoneySubProfitLossDto.class, false);
    private final CreditOrderModal creditOrderModal;
    private final EmployeeService employeeService;
    //private final GridPaginator<MoneySubProfitLossDto> paginator;
    //private final GridFilter<MoneyArticleProfitLossDto> filter;
    private HorizontalLayout filter;

    private H2 title() {
        H2 title = new H2("Прибыли и убытки");
        title.setHeight("2.2em");
        return title;
    }

    public MoneySubProfitLossView(MoneySubProfitLossService moneySubProfitLossService,
                                  CreditOrderModal creditOrderModal,
                                  EmployeeService employeeService,
                                  CompanyService companyService,
                                  ProjectService projectService) {
        this.moneySubProfitLossService = moneySubProfitLossService;
        this.data = getData();
        this.companyService = companyService;
//        this.contractorService = contractorService;
        this.projectService = projectService;
//        this.contractService = contractService;
//        this.notifications = notifications;
        this.creditOrderModal = creditOrderModal;
        this.employeeService = employeeService;
        configureListDataView();
        configureGrid();
        //this.filter = new GridFilter<>(grid);
        //this.paginator = new GridPaginator<>(grid, data, 100);
        configureFilter();
        //paginator = new GridPaginator<>(grid, data, 100);
        setHorizontalComponentAlignment(Alignment.CENTER);
        add(getToolbar(), filter, grid);
    }

    private MoneySubProfitLossDto getData() {
        return moneySubProfitLossService.getAll();
    }

//    private void configureFilter() {
//        filter.setFieldToDatePicker("itemsList");
//    }

//    private void configureFilter() {
//        filter.add(getComboBoxCompany());
//        //filter.setFieldToIntegerField("number");
////        filter.setFieldToDatePicker("time");
////        filter.setFieldToComboBox("paymentMethods", "CASH", "BANK");
////        filter.setFieldToComboBox("expenseItem", "RETURN",
////                "PURCHACE",
////                "TAXESANDFEES",
////                "MOVEMENT",
////                "RENTAL",
////                "SALARY",
////                "MARKETING");
////        filter.setFieldToComboBox("companyDto", CompanyDto::getName, companyService.getAll());
//        //filter.setFieldToComboBox("contractorDto", ContractorDto::getName, contractorService.getAll());
//        //filter.setFieldToComboBox("projectDto", ProjectDto::getName, projectService.getAll());
//        filter.onSearchClick(e -> moneySubProfitLossService.filter(filter.getFilterData()));
//        //filter.onClearClick(e -> paginator.setData(getDate()));
//    }

    private void configureFilter() {
        filter = new HorizontalLayout(getFindButton(),
                getClearButton(),
                //new Button("Очистить", e -> this.updateList()),
                new Button(new Icon(VaadinIcon.COG_O)),
                getDatePickerDateRange().get(0),
                getDatePickerDateRange().get(1),
                //getComboBoxProject(),
                getComboBoxCompany());

        filter.getStyle().set("background-color", "#e7eaef")
                .set("border-radius", "4px")
                .set("align-items", "baseline")
                .set("flex-flow", "row wrap");

        filter.setJustifyContentMode(JustifyContentMode.CENTER);
        filter.setWidthFull();

        filter.setVisible(false);
        //filter.setFieldToIntegerField("number");
//        filter.setFieldToDatePicker("time");
//        filter.setFieldToComboBox("paymentMethods", "CASH", "BANK");
//        filter.setFieldToComboBox("expenseItem", "RETURN",
//                "PURCHACE",
//                "TAXESANDFEES",
//                "MOVEMENT",
//                "RENTAL",
//                "SALARY",
//                "MARKETING");
//        filter.setFieldToComboBox("companyDto", CompanyDto::getName, companyService.getAll());
        //filter.setFieldToComboBox("contractorDto", ContractorDto::getName, contractorService.getAll());
        //filter.setFieldToComboBox("projectDto", ProjectDto::getName, projectService.getAll());
        //filter.onSearchClick(e -> moneySubProfitLossService.filter(filter.getFilterData()));
        //filter.onClearClick(e -> paginator.setData(getDate()));
    }

    private Component getClearButton() {
        Button filterButton = new Button("Очистить");
        filterButton.addClickListener(event -> {
            data = moneySubProfitLossService.getAll();
            updateList();
//            startDatePeriod = null;
//            endDatePeriod = null;
//            projectId = null;
//            companyId = null;
        });
        return filterButton;
    }

    private Button getFindButton() {
        Button filterButton = new Button("Найти");
        filterButton.addClickListener(event -> {
            data = moneySubProfitLossService.filter(startDatePeriod, endDatePeriod, companyId);
            //System.out.println(startDatePeriod.toString() + endDatePeriod.toString() + projectId + companyId);
            //data = moneySubProfitLossService.getAll();
            updateList();
//            startDatePeriod = null;
//            endDatePeriod = null;
//            projectId = null;
//            companyId = null;
        });
        return filterButton;
    }

    public void updateList() {
        removeAll();
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER);
        add(getToolbar(), filter, grid);
        filter.setVisible(true);
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setItems(listDataView);
        grid.addColumn("article").setFlexGrow(11).setHeader("Статья").setId("Статья");
        grid.addColumn("profitLoss").setFlexGrow(11).setHeader("Прибыль(убытки)").setId("Прибыль(убытки)");
    }

    private void configureListDataView() {
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
        return buttonRefresh;
    }

    private H2 getTextContract() {
        final H2 textCompany = new H2("Прибыли и убытки");
        textCompany.setHeight("2.2em");
        return textCompany;
    }

    private Button getButtonQuestion() {
//        final Button buttonQuestion = new Button();
//        Icon question = new Icon(VaadinIcon.QUESTION_CIRCLE_O);
//        buttonQuestion.setIcon(question);
//        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
//        return buttonQuestion;
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

//    public ComboBox<ProjectDto> getComboBoxProject(){
//        ComboBox<ProjectDto> projectComboBox = new ComboBox();
//        projectComboBox.setLabel("Выберете проект");
//        projectComboBox.setItems(projectService.getAll());
//        projectComboBox.setItemLabelGenerator(ProjectDto::getName);
//        projectComboBox.addValueChangeListener(event -> projectId = event.getValue().getId());
//        return projectComboBox;
//    }

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
            ProfitLossPrintModal profitLossPrintModalModal = new ProfitLossPrintModal(moneySubProfitLossService, employeeService);
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