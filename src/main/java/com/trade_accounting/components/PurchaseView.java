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

@Route(value = "purchase", layout = AppView.class)
@PageTitle("Закупки")
public class PurchaseView extends Div implements AfterNavigationObserver {

    PurchaseView() {
        add(configurationSubMenu());
    }

    private Tabs configurationSubMenu() {
        HorizontalLayout ordersLayout = new HorizontalLayout(new Label("Заказы поставщикам"));
        HorizontalLayout billsLayout = new HorizontalLayout(new Label("Счета поставщикам"));
        HorizontalLayout takingLayout = new HorizontalLayout(new Label("Приемки"));
        HorizontalLayout refundsLayout = new HorizontalLayout(new Label("Возвраты поставщикам"));
        HorizontalLayout recivedLayout = new HorizontalLayout(new Label("Счета-фактуры полученные"));
        HorizontalLayout manageLayout = new HorizontalLayout(new Label("Управление закупками"));



        return new Tabs(
                new Tab(ordersLayout),
                new Tab(billsLayout),
                new Tab(takingLayout),
                new Tab(refundsLayout),
                new Tab(recivedLayout),
                new Tab(manageLayout)
        );

    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(1);
            }
        });
    }
}
