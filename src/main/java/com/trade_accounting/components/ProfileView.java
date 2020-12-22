package com.trade_accounting.components;

import com.trade_accounting.services.interfaces.UnitService;
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

    private final UnitService unitService;

    public ProfileView(UnitService unitService) {
        this.unitService = unitService;
        add(addList());
    }

    private Tabs addList() {

        HorizontalLayout unit = new HorizontalLayout(new Label("Единицы измерения"));
        unit.addClickListener(e -> {
            unit.getUI().ifPresent(ui -> add(new UnitView(unitService)));
        });

        Tabs tabs = new Tabs(
                new Tab(new Label("Юр. лица")),
                new Tab(new Label("Сотрудники")),
                new Tab(new Label("Склады")),
                new Tab(new Label("Валюты")),
                new Tab(unit)
        );
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }
}
