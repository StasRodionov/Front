package com.trade_accounting.components;

import com.trade_accounting.services.interfaces.UnitService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "profile", layout = AppView.class)
@PageTitle("Профиль")
public class ProfileView extends Div {

    private final UnitService unitService;

    public ProfileView(UnitService unitService) {
        this.unitService = unitService;
        add(configurationSubMenu());
    }

    private Tabs configurationSubMenu() {

        HorizontalLayout companyLayout = new HorizontalLayout(new Label("Юр. лица"));
        companyLayout.addClickListener(e ->
                companyLayout.getUI().ifPresent(ui -> add(/* add your view class */)));

        HorizontalLayout employeeLayout = new HorizontalLayout(new Label("Сотрудники"));
        employeeLayout.addClickListener(e ->
                employeeLayout.getUI().ifPresent(ui -> add(/* add your view class */)));

        HorizontalLayout warehouseLayout = new HorizontalLayout(new Label("Склады"));
        warehouseLayout.addClickListener(e ->
                warehouseLayout.getUI().ifPresent(ui -> add(/* add your view class */)));

        HorizontalLayout currencyLayout = new HorizontalLayout(new Label("Валюты"));
        warehouseLayout.addClickListener(e ->
                warehouseLayout.getUI().ifPresent(ui -> add(/* add your view class */)));

        HorizontalLayout unitLayout = new HorizontalLayout(new Label("Единицы измерения"));
        unitLayout.addClickListener(e ->
                unitLayout.getUI().ifPresent(ui -> add(new UnitView(unitService))));

        Tabs tabs = new Tabs(
                new Tab(companyLayout),
                new Tab(employeeLayout),
                new Tab(warehouseLayout),
                new Tab(currencyLayout),
                new Tab(unitLayout)
        );
        return tabs;
    }
}
