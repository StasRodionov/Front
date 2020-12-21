package com.trade_accounting.components;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route
public class AppView extends AppLayout {

    public AppView() {
        addToNavbar(configurationMenu());
    }

    private Tabs configurationMenu() {
        VerticalLayout indicators = new VerticalLayout(VaadinIcon.TRENDING_UP.create(), new Label("Показатели"));
        indicators.addClickListener(e -> indicators.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout purchases = new VerticalLayout(VaadinIcon.CART.create(), new Label("Закупки"));
        purchases.addClickListener(e -> purchases.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout sales = new VerticalLayout(VaadinIcon.BRIEFCASE.create(), new Label("Продажи"));
        sales.addClickListener(e -> sales.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout products = new VerticalLayout(VaadinIcon.STOCK.create(), new Label("Товары"));
        products.addClickListener(e -> products.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout counterparties = new VerticalLayout(VaadinIcon.USERS.create(), new Label("Контрагенты"));
        counterparties.addClickListener(e -> counterparties.getUI().ifPresent(ui -> ui.navigate("contractors")));

        VerticalLayout money = new VerticalLayout(VaadinIcon.MONEY.create(), new Label("Деньги"));
        money.addClickListener(e -> money.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout retail = new VerticalLayout(VaadinIcon.SHOP.create(), new Label("Розница"));
        retail.addClickListener(e -> retail.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout production = new VerticalLayout(VaadinIcon.FACTORY.create(), new Label("Производство"));
        production.addClickListener(e -> production.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout tasks = new VerticalLayout(VaadinIcon.CHECK_SQUARE_O.create(), new Label("Задачи"));
        tasks.addClickListener(e -> tasks.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout applications = new VerticalLayout(VaadinIcon.COGS.create(), new Label("Приложения"));
        applications.addClickListener(e -> applications.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout notifications = new VerticalLayout(VaadinIcon.BELL.create(), new Label("Уведомления"));
        notifications.addClickListener(e -> notifications.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout faq = new VerticalLayout(VaadinIcon.QUESTION_CIRCLE_O.create(), new Label("FAQ"));
        faq.addClickListener(e -> faq.getUI().ifPresent(ui -> ui.navigate("")));

        VerticalLayout profile = new VerticalLayout(VaadinIcon.USER.create(), new Label("Профиль"));
        profile.addClickListener(e -> profile.getUI().ifPresent(ui -> ui.navigate("profile")));

        List<VerticalLayout> verticalLayouts = List.of(
                indicators,
                purchases,
                sales,
                products,
                counterparties,
                money,
                retail,
                production,
                tasks,
                applications,
                notifications,
                faq,
                profile
        );

        Tabs tabs =  new Tabs();
        verticalLayouts.forEach(verticalLayout -> verticalLayout.getStyle().set("alignItems", "center"));
        verticalLayouts.forEach(verticalLayout -> verticalLayout.setSpacing(false));
        verticalLayouts.forEach(verticalLayout -> verticalLayout.setPadding(false));
        verticalLayouts.forEach(verticalLayout -> tabs.add(new Tab(verticalLayout)));
        tabs.setWidthFull();
        tabs.setFlexGrowForEnclosedTabs(1);
        return tabs;
    }
}
