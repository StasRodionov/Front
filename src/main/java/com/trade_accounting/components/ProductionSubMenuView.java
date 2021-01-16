package com.trade_accounting.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "production", layout = AppView.class)
@PageTitle("Производство")
public class ProductionSubMenuView extends Div implements AfterNavigationObserver {
    ProductionSubMenuView() {
        add(configurationSubMenu());
    }

    private Tabs configurationSubMenu() {
        HorizontalLayout cardsLayout = new HorizontalLayout(new Label("Тех. карты"));
        HorizontalLayout ordersLayout = new HorizontalLayout(new Label("Заказы на производство"));
        HorizontalLayout operationsLayout = new HorizontalLayout(new Label("Тех. операции"));


        return new Tabs(
                new Tab(cardsLayout),
                new Tab(ordersLayout),
                new Tab(operationsLayout)
        );

    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(3);
            }
        });
    }
}

