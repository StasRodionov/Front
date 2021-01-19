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
        Tab goodsLayout = new Tab("Товары и услуги");
        Tab realisationLayout = new Tab(new Label("Оприходывания"));
        Tab chargesLayout = new Tab("Списания");
        Tab interventarizationLayout = new Tab("Инвентаризация");
        Tab insideOrdersLayout = new Tab("Внутрение заказы");
        Tab transferLayout = new Tab("Перемещения");
        Tab priceLayout = new Tab("Прайс-лист");
        Tab balanceLayout = new Tab("Остататки");
        Tab revenueLayout = new Tab("Обороты");


        return new Tabs(
                goodsLayout,
                realisationLayout,
                chargesLayout,
                interventarizationLayout,
                insideOrdersLayout,
                transferLayout,
                priceLayout,
                balanceLayout,
                revenueLayout
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
