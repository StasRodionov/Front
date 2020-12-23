package com.trade_accounting.components;

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

    public ProfileView() {
        add(addList());
    }

    private Tabs addList() {
        HorizontalLayout company = new HorizontalLayout(new Label("Юр. лица"));
        company.addClickListener(e -> company.getUI().ifPresent(ui -> ui.navigate("company")));

        Tabs tabs = new Tabs(
                new Tab(company),
                new Tab(new Label("Сотрудники")),
                new Tab(new Label("Склады")),
                new Tab(new Label("Валюты")),
                new Tab(new Label("Единицы измерения"))
        );
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }
}
