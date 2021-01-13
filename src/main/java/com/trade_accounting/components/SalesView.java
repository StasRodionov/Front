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

@Route(value = "sales", layout = AppView.class)
@PageTitle("Продажи")
public class SalesView extends Div implements AfterNavigationObserver {

    SalesView() {
        add(configurationSubMenu());
    }

    private Tabs configurationSubMenu() {
        HorizontalLayout ordersLayout = new HorizontalLayout(new Label("Заказы покупателей"));
        HorizontalLayout billsLaouy = new HorizontalLayout(new Label("Счета покупателям"));
        HorizontalLayout dispatchLayout = new HorizontalLayout(new Label("Отгрузки"));
        HorizontalLayout reportsLayout = new HorizontalLayout(new Label("Отчеты комиссионера"));
        HorizontalLayout refundLayout = new HorizontalLayout(new Label("Возвраты покупателей"));
        HorizontalLayout invoiceLayout = new HorizontalLayout(new Label("Счета-фактуры выданные"));
        HorizontalLayout profitabilityLaout = new HorizontalLayout(new Label("Прибыльность"));
        HorizontalLayout realisationLayout = new HorizontalLayout(new Label("Товары на реализации"));
        HorizontalLayout funnelLayout = new HorizontalLayout(new Label("Воронка продаж"));



        return new Tabs(
                new Tab(ordersLayout),
                new Tab(billsLaouy),
                new Tab(dispatchLayout),
                new Tab(reportsLayout),
                new Tab(refundLayout),
                new Tab(invoiceLayout),
                new Tab(profitabilityLaout),
                new Tab(realisationLayout),
                new Tab(funnelLayout)
        );

    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(2);
            }
        });
    }
}
