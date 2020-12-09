package com.trade_accounting.components;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("profile")
@PageTitle("Профиль")
public class ProfileView extends AppLayout {

    public ProfileView() {
        addToDrawer(addList());
    }

    private Tabs addList() {
        Tabs tabs = new Tabs(
                new Tab(new Label("Юр. лица")),
                new Tab(new Label("Сотрудники")),
                new Tab(new Label("Склады")),
                new Tab(new Label("Валюты")),
                new Tab(new Label("Единицы измерения"))
        );
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }
}
