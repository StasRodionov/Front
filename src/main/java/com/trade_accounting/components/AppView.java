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
        indicators.addClickListener(e -> indicators.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout purchases = new VerticalLayout(VaadinIcon.CART.create(), new Label("Закупки"));
        purchases.getStyle().set("alignItems", "center");
        purchases.addClickListener(e -> purchases.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout sales = new VerticalLayout(VaadinIcon.BRIEFCASE.create(), new Label("Продажи"));
        sales.getStyle().set("alignItems", "center");
        sales.addClickListener(e -> sales.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout products = new VerticalLayout(VaadinIcon.STOCK.create(), new Label("Товары"));
        products.getStyle().set("alignItems", "center");
        products.addClickListener(e -> products.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout counterparties = new VerticalLayout(VaadinIcon.USERS.create(), new Label("Контрагенты"));
        counterparties.getStyle().set("alignItems", "center");
        counterparties.addClickListener(e -> counterparties.getUI().ifPresent(ui -> ui.navigate("contractors")));

        VerticalLayout money = new VerticalLayout(VaadinIcon.MONEY.create(), new Label("Деньги"));
        money.getStyle().set("alignItems", "center");
        money.addClickListener(e -> money.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout retail = new VerticalLayout(VaadinIcon.SHOP.create(), new Label("Розница"));
        retail.getStyle().set("alignItems", "center");
        retail.addClickListener(e -> retail.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout production = new VerticalLayout(VaadinIcon.FACTORY.create(), new Label("Производство"));
        production.getStyle().set("alignItems", "center");
        production.addClickListener(e -> production.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout tasks = new VerticalLayout(VaadinIcon.CHECK_SQUARE_O.create(), new Label("Задачи"));
        tasks.getStyle().set("alignItems", "center");
        tasks.addClickListener(e -> tasks.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout applications = new VerticalLayout(VaadinIcon.COGS.create(), new Label("Приложения"));
        applications.getStyle().set("alignItems", "center");
        applications.addClickListener(e -> applications.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout notifications = new VerticalLayout(VaadinIcon.BELL.create(), new Label("Уведомления"));
        notifications.getStyle().set("alignItems", "center");
        notifications.addClickListener(e -> notifications.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout faq = new VerticalLayout(VaadinIcon.QUESTION_CIRCLE_O.create(), new Label("FAQ"));
        faq.getStyle().set("alignItems", "center");
        faq.addClickListener(e -> faq.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout profile = new VerticalLayout(VaadinIcon.USER.create(), new Label("Профиль"));
        profile.getStyle().set("alignItems", "center");
        profile.addClickListener(e -> profile.getUI().ifPresent(ui -> ui.navigate("profile")));

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
