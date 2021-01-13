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
public class ProductionView extends Div implements AfterNavigationObserver {

    ProductionView() {
        add(configurationSubMenu());
    }

    private Tabs configurationSubMenu() {
        HorizontalLayout techCardsLayout = new HorizontalLayout(new Label("Тех. карты"));
        HorizontalLayout ordersLayout = new HorizontalLayout(new Label("Заказ на производство"));
        HorizontalLayout techOperationLayout = new HorizontalLayout(new Label("Тех. операции"));

        return new Tabs(
                new Tab(techCardsLayout),
                new Tab(ordersLayout),
                new Tab(techOperationLayout)
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
