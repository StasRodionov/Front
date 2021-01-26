package com.trade_accounting.components.production;

import com.trade_accounting.components.AppView;
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
        Tab cardsLayout = new Tab("Тех. карты");
        Tab ordersLayout = new Tab("Заказы на производство");
        Tab operationsLayout = new Tab("Тех. операции");


        return new Tabs(
                cardsLayout,
                ordersLayout,
                operationsLayout
        );

    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(7);
            }
        });
    }
}

