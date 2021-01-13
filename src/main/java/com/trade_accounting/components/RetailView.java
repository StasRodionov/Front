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

@Route(value = "retail", layout = AppView.class)
@PageTitle("Розница")
public class RetailView extends Div implements AfterNavigationObserver {

    private final Div div;

    public RetailView() {
        div = new Div();

        add(configurationSubMenu(), div);
    }

    HorizontalLayout companyLayout = new HorizontalLayout(new Label("Юр. лица"));

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        companyLayout.getUI().ifPresent(ui -> {
            div.removeAll();
        });
    }

    private Tabs configurationSubMenu() {
        HorizontalLayout pointsOfSalesLayout = new HorizontalLayout(new Label("Точки продаж"));
        pointsOfSalesLayout.addClickListener(e ->
                pointsOfSalesLayout.getUI().ifPresent(ui -> {
                    div.removeAll();
                }));

        HorizontalLayout shiftsLayout = new HorizontalLayout(new Label("Смены"));
        shiftsLayout.addClickListener(e ->
                shiftsLayout.getUI().ifPresent(ui -> {
                    div.removeAll();
                }));

        HorizontalLayout salesLayout = new HorizontalLayout(new Label("Продажи"));
        salesLayout.addClickListener(e ->
                salesLayout.getUI().ifPresent(ui -> {
                    div.removeAll();
                }));

        HorizontalLayout returnsLayout = new HorizontalLayout(new Label("Возвраты"));
        returnsLayout.addClickListener(e ->
                returnsLayout.getUI().ifPresent(ui -> {
                    div.removeAll();
                }));

        HorizontalLayout depositingLayout = new HorizontalLayout(new Label("Внесения"));
        depositingLayout.addClickListener(e ->
                depositingLayout.getUI().ifPresent(ui -> {
                    div.removeAll();
                }));

        HorizontalLayout paymentsLayout = new HorizontalLayout(new Label("Выплаты"));
        paymentsLayout.addClickListener(e ->
                paymentsLayout.getUI().ifPresent(ui -> {
                    div.removeAll();
                }));

        HorizontalLayout operationsWithPointsLayout = new HorizontalLayout(new Label("Операции с балами"));
        operationsWithPointsLayout.addClickListener(e ->
                operationsWithPointsLayout.getUI().ifPresent(ui -> {
                    div.removeAll();
                }));

        HorizontalLayout prepaymentsLayout = new HorizontalLayout(new Label("Предоплаты"));
        prepaymentsLayout.addClickListener(e ->
                prepaymentsLayout.getUI().ifPresent(ui -> {
                    div.removeAll();
                }));

        HorizontalLayout refundsOfPrepaymentsLayout = new HorizontalLayout(new Label("Возвраты предоплат"));
        refundsOfPrepaymentsLayout.addClickListener(e ->
                refundsOfPrepaymentsLayout.getUI().ifPresent(ui -> {
                    div.removeAll();
                }));

        HorizontalLayout cloudReceiptQueueLayout = new HorizontalLayout(new Label("Очередь облачных чеков"));
        cloudReceiptQueueLayout.addClickListener(e ->
                cloudReceiptQueueLayout.getUI().ifPresent(ui -> {
                    div.removeAll();
                }));

        return new Tabs(
                new Tab(pointsOfSalesLayout),
                new Tab(shiftsLayout),
                new Tab(salesLayout),
                new Tab(returnsLayout),
                new Tab(depositingLayout),
                new Tab(paymentsLayout),
                new Tab(operationsWithPointsLayout),
                new Tab(prepaymentsLayout),
                new Tab(cloudReceiptQueueLayout)
                );
    }
}
