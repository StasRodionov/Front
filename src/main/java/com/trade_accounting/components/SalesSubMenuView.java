package com.trade_accounting.components;


import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "sells", layout = AppView.class)
@PageTitle("Продажи")
public class SalesSubMenuView extends Div {

    SalesSubMenuView() {
        add(configurationSubMenu());
    }

    private Tabs configurationSubMenu() {
        HorizontalLayout customersOrdersLayout = new HorizontalLayout(new Label("Заказы покупателей"));
        HorizontalLayout invoicesToBuyersLayout = new HorizontalLayout(new Label("Счета покупателям"));
        HorizontalLayout shipmentLayout = new HorizontalLayout(new Label("Отгрузки"));
        HorizontalLayout agentReportsLayout = new HorizontalLayout(new Label("Отчеты комиссионера"));
        HorizontalLayout buyersReturnsLayout = new HorizontalLayout(new Label("Возвраты покупателей"));
        HorizontalLayout issuedInvoicesLayout = new HorizontalLayout(new Label("Счета-фактуры выданные"));
        HorizontalLayout profitabilityLayout = new HorizontalLayout(new Label("Прибыльность"));
        HorizontalLayout goodsForSaleLayout = new HorizontalLayout(new Label("Товары на реализации"));
        HorizontalLayout salesFunnelLayout = new HorizontalLayout(new Label("Воронка продаж"));

        return new Tabs(
                new Tab(customersOrdersLayout),
                new Tab(invoicesToBuyersLayout),
                new Tab(shipmentLayout),
                new Tab(agentReportsLayout),
                new Tab(buyersReturnsLayout),
                new Tab(issuedInvoicesLayout),
                new Tab(profitabilityLayout),
                new Tab(goodsForSaleLayout),
                new Tab(salesFunnelLayout)
        );
    }
}
