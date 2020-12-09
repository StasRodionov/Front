package com.trade_accounting.components;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;

@Route
public class AppView extends AppLayout {

    public AppView() {
        addToNavbar(configurationMenu());
    }

    private Tabs configurationMenu() {
        VerticalLayout indicators = new VerticalLayout(VaadinIcon.TRENDING_UP.create(), new Label("Показатели"));
        indicators.getStyle().set("alignItems", "center");

        VerticalLayout purchases = new VerticalLayout(VaadinIcon.CART.create(), new Label("Закупки"));
        purchases.getStyle().set("alignItems", "center");

        VerticalLayout sales = new VerticalLayout(VaadinIcon.BRIEFCASE.create(), new Label("Продажи"));
        sales.getStyle().set("alignItems", "center");

        VerticalLayout products = new VerticalLayout(VaadinIcon.STOCK.create(), new Label("Товары"));
        products.getStyle().set("alignItems", "center");

        VerticalLayout counterparties = new VerticalLayout(VaadinIcon.USERS.create(), new Label("Контрагенты"));
        counterparties.getStyle().set("alignItems", "center");

        VerticalLayout money = new VerticalLayout(VaadinIcon.MONEY.create(), new Label("Деньги"));
        money.getStyle().set("alignItems", "center");

        VerticalLayout retail = new VerticalLayout(VaadinIcon.SHOP.create(), new Label("Розница"));
        retail.getStyle().set("alignItems", "center");

        VerticalLayout production = new VerticalLayout(VaadinIcon.FACTORY.create(), new Label("Производство"));
        production.getStyle().set("alignItems", "center");

        VerticalLayout tasks = new VerticalLayout(VaadinIcon.CHECK_SQUARE_O.create(), new Label("Задачи"));
        tasks.getStyle().set("alignItems", "center");

        VerticalLayout applications = new VerticalLayout(VaadinIcon.COGS.create(), new Label("Приложения"));
        applications.getStyle().set("alignItems", "center");

        VerticalLayout notifications = new VerticalLayout(VaadinIcon.BELL.create(), new Label("Уведомления"));
        notifications.getStyle().set("alignItems", "center");

        VerticalLayout faq = new VerticalLayout(VaadinIcon.QUESTION_CIRCLE_O.create(), new Label("FAQ"));
        faq.getStyle().set("alignItems", "center");

        VerticalLayout profile = new VerticalLayout(VaadinIcon.USER.create(), new Label("Профиль"));
        profile.getStyle().set("alignItems", "center");

        return new Tabs(
                new Tab(indicators),
                new Tab(purchases),
                new Tab(sales),
                new Tab(products),
                new Tab(counterparties),
                new Tab(money),
                new Tab(retail),
                new Tab(production),
                new Tab(tasks),
                new Tab(applications),
                new Tab(notifications),
                new Tab(faq),
                new Tab(profile)
        );
    }
}
