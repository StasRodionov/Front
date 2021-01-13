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

@Route(value = "goods", layout = AppView.class)
@PageTitle("Товары")
public class GoodsSubMenuView extends Div implements AfterNavigationObserver {

    GoodsSubMenuView() {
        add(configurationSubMenu());
    }

    private Tabs configurationSubMenu() {
        HorizontalLayout goodsLayout = new HorizontalLayout(new Label("Товары и услуги"));
        HorizontalLayout chargesLayout = new HorizontalLayout(new Label("Списания"));
        HorizontalLayout transferLayout = new HorizontalLayout(new Label("Перемещения"));
        HorizontalLayout priceLayout = new HorizontalLayout(new Label("Прайс-лист"));
        HorizontalLayout balanceLayout = new HorizontalLayout(new Label("Остататки"));


        return new Tabs(
                new Tab(goodsLayout),
                new Tab(chargesLayout),
                new Tab(transferLayout),
                new Tab(priceLayout),
                new Tab(balanceLayout)
        );

    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(3);
            }
        });
    }
}
