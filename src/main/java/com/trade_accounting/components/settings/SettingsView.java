package com.trade_accounting.components.settings;

import com.trade_accounting.components.AppView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

@Route(value = "profile/settings", layout = AppView.class)
@PageTitle("Настройки")
@Slf4j
public class SettingsView extends AppLayout {

    public SettingsView() {
        super();
        addToDrawer(lowerLayout());

    }

    private Tabs lowerLayout() {

        Tabs tabs = new Tabs(
                configureCompanySettingsTab(),
                configureDiscountTab(),
                configureExportTab(),
                configureImportTab(),
                configureScenarioTab(),
                configureLegalEntitiesTab(),
                configureEmployeesTab(),
                configureWarehousesTab(),
                configureCurrencyTab(),
                configureUnitsTab()
        );
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tab configureCompanySettingsTab() {
        Tab tab = new Tab();
        tab.setLabel("Настройки компании");
        tab.getElement().addEventListener("click", e->{
            this.getUI().ifPresent(ui -> ui.navigate("profile/settings/company_settings"));
        });
        return tab;
    }


    private Tab configureScenarioTab() {
        Tab tab = new Tab();
        tab.setLabel("Сценарии");
        return tab;
    }


    private Tab configureDiscountTab() {
        Tab tab = new Tab();
        tab.setLabel("Скидки");
        return tab;
    }


    private Tab configureLegalEntitiesTab() {
        Tab tab = new Tab();
        tab.setLabel("Юридические лица");
        tab.getElement().addEventListener("click", e->{
            this.getUI().ifPresent(ui -> ui.navigate( "profile/settings/legal_entities_settings"));
        });
        return tab;
    }


    private Tab configureImportTab() {
        Tab tab = new Tab();
        tab.setLabel("Импорт");
        return tab;
    }


    private Tab configureExportTab() {
        Tab tab = new Tab();
        tab.setLabel("Экспорт");
        return tab;
    }


    private Tab configureEmployeesTab() {
        Tab tab = new Tab();
        tab.setLabel("Сотрудники");
        tab.getElement().addEventListener("click", e->{
            this.getUI().ifPresent(ui -> ui.navigate( "profile/settings/employees_settings"));
        });
        return tab;
    }

    private Tab configureWarehousesTab() {
        Tab tab = new Tab();
        tab.setLabel("Склады");
        tab.getElement().addEventListener("click", e->{
            this.getUI().ifPresent(ui -> ui.navigate( "profile/settings/warehouses_settings"));
        });
        return tab;
    }


    private Tab configureCurrencyTab() {
        Tab tab = new Tab();
        tab.setLabel("Валюта");
        tab.getElement().addEventListener("click", e->{
            this.getUI().ifPresent(ui -> ui.navigate("profile/settings/currency_settings"));
        });
        return tab;
    }



    private Tab configureUnitsTab() {
        Tab tab = new Tab();
        tab.setLabel("Единицы измерения");
        tab.getElement().addEventListener("click", e->{
            this.getUI().ifPresent(ui -> ui.navigate("profile/settings/units_settings"));
        });
        return tab;
    }
}
