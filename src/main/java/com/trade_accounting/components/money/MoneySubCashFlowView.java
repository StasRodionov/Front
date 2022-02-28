package com.trade_accounting.components.money;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;

import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.finance.MoneySubCashFlowDto;
import com.trade_accounting.models.dto.util.ProjectDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.finance.MoneySubCashFlowService;
import com.trade_accounting.services.interfaces.util.ProjectService;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Route(value = "MoneySubCashFlowView", layout = AppView.class)
@PageTitle("Движение денежных средств")
public class MoneySubCashFlowView extends VerticalLayout {

    private final MoneySubCashFlowService moneySubCashFlowService;
    private final CompanyService companyService;
    private final ContractorService contractorService;
    private final ProjectService projectService;

    private List<MoneySubCashFlowDto> data;
    private final Grid<MoneySubCashFlowDto> grid = new Grid<>(MoneySubCashFlowDto.class, false);
    private final GridPaginator<MoneySubCashFlowDto> paginator;
    private final CreditOrderModal creditOrderModal;

    private Long projectId;
    private Long companyId;
    private Long contractorId;
    private LocalDate departureDate;
    private LocalDate returnDate;

    private H2 title() {
        H2 title = new H2("Движение денежных средств");
        title.setHeight("2.2em");
        return title;
    }

    public MoneySubCashFlowView(MoneySubCashFlowService moneySubCashFlowService,
                                CompanyService companyService,
                                ContractorService contractorService,
                                ProjectService projectService,
                                ContractService contractService,
                                Notifications notifications,
                                CreditOrderModal creditOrderModal) {
        this.moneySubCashFlowService = moneySubCashFlowService;
        this.companyService = companyService;
        this.data = moneySubCashFlowService.getAll();
        this.contractorService = contractorService;
        this.projectService = projectService;
        this.creditOrderModal = creditOrderModal;
        configureGrid();
        this.paginator = new GridPaginator<>(grid, data, 100);
        //configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(getToolbar(), grid, paginator);
    }

//    private void configureFilter() {
//        filter.setFieldToDatePicker("time");
////        filter.setFieldToIntegerField("sum");
////        filter.setFieldToIntegerField("number");
////        filter.onSearchClick(e -> paginator.setData(paymentService.filter(filter.getFilterData())));
//        filter.onClearClick(e -> paginator.setData(data));
//    }

    public void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setItems(data);
        grid.addColumn("time").setFlexGrow(10).setHeader("Дата").setId("Дата");
        grid.addColumn("bankcoming").setFlexGrow(7).setHeader("Банк Приход").setId("Банк Приход");
        grid.addColumn("bankexpense").setFlexGrow(7).setHeader("Банк Расход").setId("Банк Расход");
        grid.addColumn("bankbalance").setFlexGrow(7).setHeader("Банк Баланс").setId("Банк Баланс");
        grid.addColumn("cashcoming").setFlexGrow(7).setHeader("Касса Приход").setId("Касса Приход");
        grid.addColumn("cashexpense").setFlexGrow(7).setHeader("Касса Расход").setId("Касса Расход");
        grid.addColumn("cashbalance").setFlexGrow(7).setHeader("Касса Баланс").setId("Касса Баланс");
        grid.addColumn("allcoming").setFlexGrow(7).setHeader("Все Приход").setId("Все Приход");
        grid.addColumn("allexpense").setFlexGrow(7).setHeader("Все Расход").setId("Все Расход");
        grid.addColumn("allbalance").setFlexGrow(7).setHeader("Все Баланс").setId("Все Баланс");
    }

    public void updateList() {
        GridPaginator<MoneySubCashFlowDto> paginatorUpdateList
                = new GridPaginator<>(grid, data, 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(getToolbar(), grid, paginator);
    }

    public HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), getTextContract(), getButtonRefresh(),
                getButtonFilter(), getPrint(), getComboBoxProject(), getComboBoxCompany(), getComboBoxContractor(),
                getDatePickerDateRange().get(0),
                getDatePickerDateRange().get(1));
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolbar;
    }

    public ComboBox<ProjectDto> getComboBoxProject(){
        ComboBox<ProjectDto> projectComboBox = new ComboBox();
        projectComboBox.setLabel("Выберете проект");
        projectComboBox.setItems(projectService.getAll());
        projectComboBox.setItemLabelGenerator(ProjectDto::getName);
        projectComboBox.addValueChangeListener(event -> projectId = event.getValue().getId());
        return projectComboBox;
    }

    public ComboBox<CompanyDto> getComboBoxCompany(){
        ComboBox<CompanyDto> companyComboBox = new ComboBox();
        companyComboBox.setLabel("Выберете компанию");
        companyComboBox.setItems(companyService.getAll());
        companyComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyComboBox.addValueChangeListener(event -> companyId = event.getValue().getId());
        return companyComboBox;
    }

    public ComboBox<ContractorDto> getComboBoxContractor(){
        ComboBox<ContractorDto> contractorComboBox = new ComboBox();
        contractorComboBox.setLabel("Выберете контрагента");
        contractorComboBox.setItems(contractorService.getAll());
        contractorComboBox.setItemLabelGenerator(ContractorDto::getName);
        contractorComboBox.addValueChangeListener(event -> contractorId = event.getValue().getId());
        return contractorComboBox;
    }

    public List<DatePicker> getDatePickerDateRange() {
        List<DatePicker> dates = new ArrayList<>();
        DatePicker dDate = new DatePicker("Начальная дата");
        DatePicker rDate = new DatePicker("Конечная дата");
        dDate.addValueChangeListener(event -> departureDate = event.getValue());
        rDate.addValueChangeListener(event -> returnDate = event.getValue());
        dates.add(dDate);
        dates.add(rDate);
        return dates;
    }

    private Button getButtonFilter() {
        Button filterButton = new Button("Отфильтровать");
        filterButton.addClickListener(event -> {
            data = moneySubCashFlowService.filter(departureDate, returnDate, projectId, companyId, contractorId);
            updateList();
            departureDate = null;
            returnDate = null;
            projectId = null;
            companyId = null;
            contractorId = null;
        });
        return filterButton;
    }

    public Button getButtonRefresh() {
        final Button buttonRefresh;
        Icon circle = new Icon(VaadinIcon.REFRESH);
        buttonRefresh = new Button();
        buttonRefresh.setIcon(circle);
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    public H2 getTextContract() {
        final H2 textCompany = new H2("Движение денежных средств");
        textCompany.setHeight("2.2em");
        return textCompany;
    }

    public Button getButtonQuestion() {
        return Buttons.buttonQuestion("Добавьте описание");
    }


    public Select<String> getPrint() {
        Select getPrint = new Select();
        getPrint.setWidth("130px");
        getPrint.setItems("Печать", "Движение денежных средств");
        getPrint.setValue("Печать");
//        uploadListCashFlow(getPrint);
        return getPrint;
    }

}
