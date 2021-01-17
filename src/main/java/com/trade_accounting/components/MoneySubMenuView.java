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
public class MoneySubMenuView extends Div implements AfterNavigationObserver{
    MoneySubMenuView() { add(configurationSubMenu()); }

    private Tabs configurationSubMenu() {
        Tab paymentsLayout = new Tab("Платежи");
        Tab cashFlowLayout = new Tab("Движение денежных средств");
        Tab profitAndLossLayout = new Tab("Прибыли и убытки");
        Tab mutualSettlementsLayout = new Tab("Взаиморасчеты");
        Tab correctionLayout = new Tab("Корректировки");

        return new Tabs(
                paymentsLayout,
                cashFlowLayout,
                profitAndLossLayout,
                mutualSettlementsLayout,
                correctionLayout
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
