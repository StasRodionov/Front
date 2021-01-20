package com.trade_accounting.components;

import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.CurrencyService;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.UnitService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "profile", layout = AppView.class)
@PageTitle("Профиль")
public class ProfileView extends Div implements AfterNavigationObserver {

    private final UnitService unitService;
    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final WarehouseService warehouseService;
    private final CurrencyService currencyService;
    private final Div div;
    
    public ProfileView(UnitService unitService, CompanyService companyService,
                       EmployeeService employeeService, WarehouseService warehouseService,
                       CurrencyService currencyService) {
        this.unitService = unitService;
        this.companyService = companyService;
        this.employeeService = employeeService;
        this.warehouseService = warehouseService;
        this.currencyService = currencyService;
        div = new Div();
        add(configurationSubMenu(), div);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(12);
            }
        });
        getUI().ifPresent(ui -> {
            div.removeAll();
            div.add(new CompanyView(companyService));
        });
    }

    private Tabs configurationSubMenu() {
        Tabs tabs = new Tabs(
                new Tab("Юр. лица"),
                new Tab("Сотрудники"),
                new Tab("Склады"),
                new Tab("Валюты"),
                new Tab("Единицы измерения")
        );
        tabs.addSelectedChangeListener(event -> {
            String tabName = event.getSelectedTab().getLabel();
            switch (tabName) {
                case "Юр. лица":
                    div.removeAll();
                    div.add(new CompanyView(companyService));
                    break;
                case "Сотрудники":
                    div.removeAll();
                    div.add(new EmployeeView(employeeService));
                    break;
                case "Склады":
                    div.removeAll();
                    div.add(new WareHouseView(warehouseService));
                    break;
                case "Валюты":
                    div.removeAll();
                    div.add(new CurrencyView(currencyService));
                    break;
                case "Единицы измерения":
                    div.removeAll();
                    div.add(new UnitView(unitService));
                    break;
                default:
            }
        });
        return tabs;
    }
}
