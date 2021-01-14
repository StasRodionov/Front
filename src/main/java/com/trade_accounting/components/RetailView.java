package com.trade_accounting.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "retail", layout = AppView.class)
@PageTitle("Розница")
public class RetailView extends Div implements AfterNavigationObserver {

    public RetailView() {
        add(configurationSubMenu());
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(6);
            }
        });
    }

    private Tabs configurationSubMenu() {
        Tab pointsOfSalesLayout = new Tab("Точки продаж");
        Tab shiftsLayout = new Tab("Смены");
        Tab salesLayout = new Tab("Продажи");
        Tab returnsLayout = new Tab("Возвраты");
        Tab depositingLayout = new Tab("Внесения");
        Tab paymentsLayout = new Tab("Выплаты");
        Tab operationsWithPointsLayout = new Tab("Операции с баллами");
        Tab prepaymentsLayout = new Tab("Предоплаты");
        Tab refundsOfPrepaymentsLayout = new Tab("Возвраты предоплат");
        Tab cloudReceiptQueueLayout = new Tab("Очередь облачных чеков");

        return new Tabs(
                pointsOfSalesLayout,
                shiftsLayout,
                salesLayout,
                returnsLayout,
                depositingLayout,
                paymentsLayout,
                operationsWithPointsLayout,
                prepaymentsLayout,
                refundsOfPrepaymentsLayout,
                cloudReceiptQueueLayout
                );
    }
}
