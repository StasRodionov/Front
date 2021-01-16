package com.trade_accounting.components;

import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.UnitService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "profile", layout = AppView.class)
@PageTitle("Профиль")
public class ProfileView extends Div implements AfterNavigationObserver {


    private final UnitService unitService;
    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final WarehouseService warehouseService;
    //TODO
    //private final CurrencyService currencyService;
    private final Div div;

    public ProfileView(UnitService unitService, CompanyService companyService,
                       EmployeeService employeeService, WarehouseService warehouseService /*CurrencyService currencyService*/) {
        this.unitService = unitService;
        this.companyService = companyService;
        this.employeeService = employeeService;
        this.warehouseService = warehouseService;
        //this.currencyService = currencyService;
        div = new Div();

        add(configurationSubMenu(), div);
    }
    HorizontalLayout companyLayout = new HorizontalLayout(new Label("Юр. лица"));
    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(12);
            }
        });
        companyLayout.getUI().ifPresent(ui -> {
            div.removeAll();
            div.add(new CompanyView(companyService));
        });
    }

    private Tabs configurationSubMenu() {
        companyLayout.addClickListener(e ->
                companyLayout.getUI().ifPresent(ui -> {
                    div.removeAll();
                    div.add(new CompanyView(companyService));
                }));

        HorizontalLayout employeeLayout = new HorizontalLayout(new Label("Сотрудники"));
        employeeLayout.addClickListener(e ->
                employeeLayout.getUI().ifPresent(ui -> {
                    div.removeAll();
                    div.add(new EmployeeView(employeeService));
                }));

        HorizontalLayout warehouseLayout = new HorizontalLayout(new Label("Склады"));
        warehouseLayout.addClickListener(e ->
                warehouseLayout.getUI().ifPresent(ui -> {
                    div.removeAll();
                    div.add(new WareHouseView(warehouseService));
                }));

        HorizontalLayout currencyLayout = new HorizontalLayout(new Label("Валюты"));
        currencyLayout.addClickListener(e ->
                currencyLayout.getUI().ifPresent(ui -> {
                    div.removeAll();
                    //div.add(new CurrencyView(currenceService));
                }));

        HorizontalLayout unitLayout = new HorizontalLayout(new Label("Единицы измерения"));
        unitLayout.addClickListener(e ->
                unitLayout.getUI().ifPresent(ui -> {
                    div.removeAll();
                    div.add(new UnitView(unitService));
                }));

        return new Tabs(
                new Tab(companyLayout),
                new Tab(employeeLayout),
                new Tab(warehouseLayout),
                new Tab(currencyLayout),
                new Tab(unitLayout)
        );
    }
}
