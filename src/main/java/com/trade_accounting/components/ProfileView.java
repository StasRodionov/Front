package com.trade_accounting.components;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "profile", layout = AppView.class)
@PageTitle("Профиль")
public class ProfileView extends Div {

    public ProfileView() {
        add(addList());
    }

    private Tabs addList() {
        HorizontalLayout legal = new HorizontalLayout(new Label("Юр. лица"));
        legal.addClickListener(e -> legal.getUI().ifPresent(ui -> ui.navigate("")));

        HorizontalLayout employees = new HorizontalLayout(new Label("Сотрудники"));
        employees.addClickListener(e -> employees.getUI().ifPresent(ui -> ui.navigate("")));

        HorizontalLayout wareHouse = new HorizontalLayout(new Label("Склады"));
        wareHouse.addClickListener(e -> wareHouse.getUI().ifPresent(ui -> ui.navigate("warehouse")));

        HorizontalLayout currency = new HorizontalLayout(new Label("Валюты"));
        currency.addClickListener(e -> currency.getUI().ifPresent(ui -> ui.navigate("")));

        HorizontalLayout units = new HorizontalLayout(new Label("Единицы измерения"));
        units.addClickListener(e -> units.getUI().ifPresent(ui -> ui.navigate("")));

        Tabs tabs = new Tabs(
                new Tab(legal),
                new Tab(employees),
                new Tab(wareHouse),
                new Tab(currency),
                new Tab(units)
        );
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }
}
