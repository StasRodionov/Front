package com.trade_accounting.components.money;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.configure.components.select.SelectConstants;
import com.trade_accounting.components.util.configure.components.select.SelectExt;
import com.trade_accounting.models.dto.finance.MoneyArticleProfitLossDto;
import com.trade_accounting.models.dto.finance.MoneySubProfitLossDto;
import com.trade_accounting.services.interfaces.finance.MoneySubProfitLossService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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

import java.util.ArrayList;
import java.util.List;

@Route(value = "MoneySubProfitLossView", layout = AppView.class)
@PageTitle("Прибыли и убытки")
public class MoneySubProfitLossView extends VerticalLayout {


    private final MoneySubProfitLossService moneySubProfitLossService;
//    private final CompanyService companyService;
//    private final ContractorService contractorService;
//    private final ProjectService projectService;
//    private final ContractService contractService;
//    private final Notifications notifications;

    private final MoneySubProfitLossDto data;
    List<MoneyArticleProfitLossDto> listDataView = new ArrayList<>();
    private final Grid<MoneyArticleProfitLossDto> grid = new Grid<>(MoneyArticleProfitLossDto.class, false);
    private final CreditOrderModal creditOrderModal;
   // private final GridPaginator<MoneySubProfitLossDto> paginator;
    private final GridFilter<MoneyArticleProfitLossDto> filter;

    private H2 title() {
        H2 title = new H2("Прибыли и убытки");
        title.setHeight("2.2em");
        return title;
    }

    public MoneySubProfitLossView(MoneySubProfitLossService moneySubProfitLossService,
                                  CreditOrderModal creditOrderModal) {
        this.moneySubProfitLossService = moneySubProfitLossService;
        this.data = moneySubProfitLossService.getAll();
//        this.companyService = companyService;
//        this.contractorService = contractorService;
//        this.projectService = projectService;
//        this.contractService = contractService;
//        this.notifications = notifications;
        this.creditOrderModal = creditOrderModal;
        configureListDataView();
        configureGrid();
        this.filter = new GridFilter<>(grid);
        //this.paginator = new GridPaginator<>(grid, data, 100);
        //configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER);
        add(getToolbar(), filter, grid);
    }

//    private void configureFilter() {
//        filter.setFieldToDatePicker("itemsList");
//    }

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

//    private void updateList() {
//        GridPaginator<MoneySubProfitLossDto> paginatorUpdateList
//                = new GridPaginator<>(grid, moneySubProfitLossService.getAll(), 100);
//        setHorizontalComponentAlignment(Alignment.CENTER, paginatorUpdateList);
//        removeAll();
//        add(getToolbar(), grid, paginator);
//    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), getTextContract(), getButtonRefresh(),
                getButtonFilter(), getPrint());
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolbar;
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

    private H2 getTextContract() {
        final H2 textCompany = new H2("Прибыли и убытки");
        textCompany.setHeight("2.2em");
        return textCompany;
    }

    private Button getButtonQuestion() {
        final Button buttonQuestion = new Button();
        Icon question = new Icon(VaadinIcon.QUESTION_CIRCLE_O);
        buttonQuestion.setIcon(question);
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }


    private Select<String> getPrint() {
        return new SelectExt.SelectBuilder<String>()
                .item(SelectConstants.PRINT_SELECT_ITEM)
                .defaultValue(SelectConstants.PRINT_SELECT_ITEM)
                .item("Прибыли и убытки")
                .width(SelectConstants.SELECT_WIDTH_130PX)
                .build();
    }
}