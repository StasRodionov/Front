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

@Route(value = "money", layout = AppView.class)
@PageTitle("Деньги")
public class MoneyView extends Div implements AfterNavigationObserver {

    MoneyView() {
        add(configurationSubMenu());
    }

    private Tabs configurationSubMenu() {
        HorizontalLayout paymentLayout = new HorizontalLayout(new Label("Платежи"));
        HorizontalLayout cashFlowLayout = new HorizontalLayout(new Label("Движение денежных средств"));
        HorizontalLayout profitLossLayout = new HorizontalLayout(new Label("Прибыли и убытки"));
        HorizontalLayout accountingLayout = new HorizontalLayout(new Label("Взаиморасчеты"));
        HorizontalLayout correctionLayout = new HorizontalLayout(new Label("Взаиморасчеты"));


        return new Tabs(
                new Tab(paymentLayout),
                new Tab(cashFlowLayout),
                new Tab(profitLossLayout),
                new Tab(accountingLayout),
                new Tab(correctionLayout)
        );

    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(5);
            }
        });
    }
}
