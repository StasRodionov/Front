package com.trade_accounting.components.settings;

import com.trade_accounting.components.AppView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import static com.trade_accounting.config.SecurityConstants.*;

@Route(value = PROFILE_PROFILE__SETTINGS, layout = AppView.class)
@PageTitle("Настройки")
@Slf4j
public class SettingsView extends AppLayout {

    public SettingsView() {
        super();
        addToDrawer(lowerLayout());
    }

    private Tabs lowerLayout() {
        Tabs tabs = new Tabs(
                new Tab(""),
                new Tab(""),
                configureCompanySettingsTab(),
                configureScenarioTab(),
                configureDiscountTab(),
                configureImportTab(),
                configureExportTab(),
                configureLegalEntitiesTab(),
                configureEmployeesTab(),
                configureWarehousesTab(),
                configureSalesChannelTab(),
                configureCurrencyTab(),
                configureUnitsTab(),
                configureSalesChannelTab(),
                configureCountry(),
                configureProjectSettingsTab(),
                configureOnlineStore()
                );
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tab configureProjectSettingsTab() {
        Tab tab = new Tab();
        tab.setLabel("Проекты");
        tab.getElement().addEventListener("click", e -> {
            this.getUI().ifPresent(ui -> ui.navigate(PROFILE_PROFILE__SETTINGS__PROJECTS_SETTINGS));
        });
        return tab;
    }

    private Tab configureCompanySettingsTab() {
        Tab tab = new Tab();
        tab.setLabel("Настройки компании");
        tab.getElement().addEventListener("click", e -> {
            this.getUI().ifPresent(ui -> ui.navigate(PROFILE_PROFILE__SETTINGS__COMPANY_SETTINGS));
        });
        return tab;
    }

    private Tab configureScenarioTab() {
        Tab tab = new Tab();
        tab.setLabel("Сценарии");
        tab.getElement().addEventListener("click", e -> {
            this.getUI().ifPresent(ui -> ui.navigate(PROFILE_PROFILE__SETTINGS__SCENARIO_SETTINGS));
        });
        return tab;
    }

    private Tab configureDiscountTab() {
        Tab tab = new Tab();
        tab.setLabel("Скидки");
        tab.getElement().addEventListener("click", e->{
            this.getUI().ifPresent(ui->ui.navigate(PROFILE_PROFILE__SETTINGS__DISCOUNT_SETTINGS));
        });
        return tab;
    }

    private Tab configureLegalEntitiesTab() {
        Tab tab = new Tab();
        tab.setLabel("Юридические лица");
        tab.getElement().addEventListener("click", e -> {
            this.getUI().ifPresent(ui -> ui.navigate(PROFILE_PROFILE__SETTINGS__LEGAL_ENTITIES_SETTINGS));
        });
        return tab;
    }

    private Tab configureImportTab() {
        Tab tab = new Tab();
        tab.setLabel("Импорт");
        tab.getElement().addEventListener("click", e -> {
            this.getUI().ifPresent(ui -> ui.navigate(PROFILE_PROFILE__SETTINGS__IMPORT_SETTINGS));
        });
        return tab;
    }

    private Tab configureExportTab() {
        Tab tab = new Tab();
        tab.setLabel("Экспорт");
        tab.getElement().addEventListener("click", e -> {
            this.getUI().ifPresent(ui -> ui.navigate(PROFILE_PROFILE__SETTINGS__EXPORT_SETTINGS));
        });
        return tab;
    }

    private Tab configureEmployeesTab() {
        Tab tab = new Tab();
        tab.setLabel("Сотрудники");
        tab.getElement().addEventListener("click", e -> {
            this.getUI().ifPresent(ui -> ui.navigate(PROFILE_PROFILE__SETTINGS__EMPLOYEES_SETTINGS));
        });
        return tab;
    }

    private Tab configureWarehousesTab() {
        Tab tab = new Tab();
        tab.setLabel("Склады");
        tab.getElement().addEventListener("click", e -> {
            this.getUI().ifPresent(ui -> ui.navigate(PROFILE_PROFILE__SETTINGS__WAREHOUSES_SETTINGS));
        });
        return tab;
    }

    private Tab configureCurrencyTab() {
        Tab tab = new Tab();
        tab.setLabel("Валюта");
        tab.getElement().addEventListener("click", e -> {
            this.getUI().ifPresent(ui -> ui.navigate(PROFILE_PROFILE__SETTINGS__CURRENCY_SETTINGS));
        });
        return tab;
    }

    private Tab configureUnitsTab() {
        Tab tab = new Tab();
        tab.setLabel("Единицы измерения");
        tab.getElement().addEventListener("click", e -> {
            this.getUI().ifPresent(ui -> ui.navigate(PROFILE_PROFILE__SETTINGS__UNITS_SETTINGS));
        });
        return tab;
    }

    private Tab configureSalesChannelTab() {
        Tab tab = new Tab();
        tab.setLabel("Канала продаж");
        tab.getElement().addEventListener("click", e->{
            this.getUI().ifPresent(ui -> ui.navigate(PROFILE_PROFILE__SETTINGS__SALES_CHANNEL_SETTINGS));
        });
        return tab;
    }

    private Tab configureCountry() {
        Tab tab = new Tab();
        tab.setLabel("Страны");
        tab.getElement().addEventListener("click", e -> {
            this.getUI().ifPresent(ui -> ui.navigate(PROFILE_PROFILE__SETTINGS__COUNTRIES_SETTINGS));
        });
        return tab;
    }

    private Tab configureOnlineStore() {
        Tab tab = new Tab();
        tab.setLabel("Интернет-магазины");
        tab.getElement().addEventListener("click", e -> {
            this.getUI().ifPresent(ui -> ui.navigate(PROFILE_PROFILE__SETTINGS__ONLINE_STORES_SETTINGS));
        });
        return tab;
    }

}
