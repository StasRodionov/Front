package com.trade_accounting.components.sells;


import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.company.ContractorStatusDto;
import com.trade_accounting.models.dto.finance.FunnelDto;
import com.trade_accounting.models.dto.invoice.InvoicesStatusDto;
import com.trade_accounting.services.interfaces.company.ContractorStatusService;
import com.trade_accounting.services.interfaces.finance.FunnelService;
import com.trade_accounting.services.interfaces.invoice.InvoicesStatusService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import java.util.ArrayList;
import java.util.List;

public class SalesSubSalesFunnelView extends VerticalLayout {
    private final ContractorStatusService contractorStatusService;
    private final InvoicesStatusService invoicesStatusService;
    private final FunnelService funnelService;


    List<FunnelDto> listOrdersDataView = new ArrayList<>();
    List<FunnelDto> listContractorsDataView = new ArrayList<>();
    private final List<FunnelDto> data;
    private final Grid<FunnelDto> grid = new Grid<>(FunnelDto.class, false);
    private final GridPaginator<FunnelDto> paginator;
    private final GridFilter<FunnelDto> filter;

    private final String statusName = "statusName";
    private final String count = "count";
    private final String status = "Статус";
    private final String value = "Количество";
    private final String time = "Время";
    private final String conversion = "Конверсия";
    private final String price = "price";
    private final Tab orders = new Tab("По заказам");
    private final Tab contractors = new Tab("По контрагентам");
    private final Tabs tabs = new Tabs(orders, contractors);

    private List<FunnelDto> getData() {
        return funnelService.getAll();
    }

    public SalesSubSalesFunnelView(ContractorStatusService contractorStatusService, InvoicesStatusService invoicesStatusService, FunnelService funnelService) {
        this.contractorStatusService = contractorStatusService;
        this.invoicesStatusService = invoicesStatusService;
        this.funnelService = funnelService;
        this.data = getData();
        configureListDataView();
        paginator = new GridPaginator<>(grid, data, 50);
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(upperLayout(), filter, grid, paginator);
    }

    private void configureFilter() {
        filter.setFieldToComboBox(statusName, FunnelDto::getStatusName, funnelService.getAll());
        filter.setFieldToIntegerField(count);
        filter.setFieldToIntegerField("time");
        filter.setFieldToIntegerField("conversion");
        filter.setFieldToIntegerField(price);
        filter.onSearchClick(e -> paginator
                .setData(funnelService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> updateList());
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        configListOrders();
    }

    private void configureListDataView() {
        listOrdersDataView.add(getData().get(0));
        listOrdersDataView.add(getData().get(1));
        listOrdersDataView.add(getData().get(2));
        listOrdersDataView.add(getData().get(3));
        listContractorsDataView.add(getData().get(4));
        listContractorsDataView.add(getData().get(5));
        listContractorsDataView.add(getData().get(6));
        listContractorsDataView.add(getData().get(7));
        listContractorsDataView.add(getData().get(8));
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), configurationSubMenu(), buttonFilter(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upper;
    }

    private Tabs configurationSubMenu() {
        tabs.addSelectedChangeListener(event -> {
            String tabName = event.getSelectedTab().getLabel();
            if ("По заказам".equals(tabName)) {
                configListOrders();
            } else if ("По контрагентам".equals(tabName)) {
                configListContractors();
            }
        });
        return tabs;
    }

    private void configListOrders() {
        grid.removeAllColumns();
        grid.setItems(listOrdersDataView);
        grid.addColumn(statusName).setFlexGrow(11).setHeader(status).setId(status);
        grid.addColumn(count).setFlexGrow(11).setHeader(value).setId(value);
        grid.addColumn("time").setFlexGrow(11).setHeader(time).setId(time);
        grid.addColumn("conversion").setFlexGrow(11).setHeader(conversion).setId(conversion);
        grid.addColumn(price).setFlexGrow(11).setHeader("Сумма").setId("Сумма");
    }

    private void configListContractors() {
        grid.removeAllColumns();
        grid.setItems(listContractorsDataView);
        grid.addColumn(statusName).setFlexGrow(11).setHeader(status).setId(status);
        grid.addColumn(count).setFlexGrow(11).setHeader(value).setId(value);
        grid.addColumn("conversion").setFlexGrow(11).setHeader(conversion).setId(conversion);
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("Воронка продаж — это способ распределения заказов (клиентов) по этапам процесса заключения сделки. " +
                        "Воронка показывает, сколько заказов (клиентов) на каких этапах сделки находятся. " +
                        "Если заказ не был выполнен, на воронке будет отмечен последний этап. " +
                        "Отчет позволяет выяснить, какой этап заключения сделки является «бутылочным горлышком». " +
                        "В отчете представлены разрезы по заказам покупателей и по контрагентам.\n" +
                        "Читать инструкцию: "),
                new Anchor("#", "Воронка Продаж"));
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        buttonRefresh.addClickListener(e -> updateList());
        return buttonRefresh;
    }


    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private H4 title() {
        H4 title = new H4("Воронка Продаж");
        title.setHeight("2.2em");
        title.setWidth("80px");
        return title;
    }

    private void updateList() {
        if (orders.isSelected()) {
            configListOrders();
        } else if (contractors.isSelected()) {
            configListContractors();
        }
    }

    private void configOrderGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.removeAllColumns();
        grid.setItems(listOrdersDataView);
        grid.addColumn(statusName).setFlexGrow(11).setHeader(status).setId(status);
        grid.addColumn(count).setFlexGrow(11).setHeader(value).setId(value);
        grid.addColumn("time").setFlexGrow(11).setHeader(time).setId(time);
        grid.addColumn(conversion).setFlexGrow(11).setHeader(conversion).setId(conversion);
        grid.addColumn(price).setFlexGrow(11).setHeader("Сумма").setId("Сумма");
    }

}