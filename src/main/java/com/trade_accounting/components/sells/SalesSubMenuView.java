package com.trade_accounting.components.sells;


import com.trade_accounting.components.AppView;
import com.trade_accounting.services.interfaces.CustomersOrdersService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "sells", layout = AppView.class)
@PageTitle("Продажи")
public class SalesSubMenuView extends Div implements AfterNavigationObserver {

    private final Div div;
//    private final CustomersOrdersService customersOrdersService;

    public SalesSubMenuView() {
        div = new Div();
        add(configurationSubMenu(), div);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        div.removeAll();
        div.add(new String("SalesSubCustomersOrdersView"));

        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(2);
            }
        });
    }


    private Tabs configurationSubMenu() {
        Tabs tabs = new Tabs(
                new Tab("Заказы покупателей"),
                new Tab("Счета покупателям"),
                new Tab("Отгрузки"),
                new Tab("Отчеты комиссионера"),
                new Tab("Возвраты покупателей"),
                new Tab("Счета-фактуры выданные"),
                new Tab("Прибыльность"),
                new Tab("Товары на реализации"),
                new Tab("Воронка продаж")
        );


        tabs.addSelectedChangeListener(event -> {
            String tabName = event.getSelectedTab().getLabel();
            switch (tabName) {
                case "Заказы покупателей":
                    div.removeAll();
                    div.add(new String("SalesSubCustomersOrdersView"));
                    break;
                case "Счета покупателям":
                    div.removeAll();
                    div.add(new String("SalesSubInvoicesToBuyersView"));
                    break;
                case "Отгрузки":
                    div.removeAll();
                    div.add(new String("SalesSubShipmentView"));
                    break;
                case "Отчеты комиссионера":
                    div.removeAll();
                    div.add(new String("SalesSubAgentReportsView"));
                    break;
                case "Возвраты покупателей":
                    div.removeAll();
                    div.add(new String("SalesSubBuyersReturnsView"));
                    break;
                case "Счета-фактуры выданные":
                    div.removeAll();
                    div.add(new String("SalesSubIssuedInvoicesView"));
                    break;
                case "Прибыльность":
                    div.removeAll();
                    div.add(new String("SalesSubProfitabilityView"));
                    break;
                case "Товары на реализации":
                    div.removeAll();
                    div.add(new String("SalesSubGoodsForSaleView"));
                    break;
                case "Воронка продаж":
                    div.removeAll();
                    div.add(new String("SalesSubSalesFunnelView"));
                    break;
            }
        });
        return tabs;
    }
}

