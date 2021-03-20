package com.trade_accounting.components.goods;


import com.trade_accounting.components.AppView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;


@Route(value = "goods", layout = AppView.class)
@PageTitle("Товары")
@SpringComponent
@UIScope
public class GoodsSubMenuView extends Div implements AfterNavigationObserver {

    private final GoodsView goodsView;

    private final Div div = new Div();

    public GoodsSubMenuView(GoodsView goodsView) {
        this.goodsView = goodsView;
        add(configurationSubMenu(), div);
    }

    private Tabs configurationSubMenu() {
        HorizontalLayout goodsLayout = new HorizontalLayout(new Label("Товары и услуги"));

        goodsLayout.addClickListener(e ->
                goodsLayout.getUI().ifPresent(ui -> {
                    div.removeAll();
                    goodsView.updateData();
                    div.add(goodsView);

                }));
        Tab realisationLayout = new Tab(new Label("Оприходывания"));
        Tab chargesLayout = new Tab("Списания");
        Tab interventarizationLayout = new Tab("Инвентаризация");
        Tab insideOrdersLayout = new Tab("Внутрение заказы");
        Tab transferLayout = new Tab("Перемещения");
        Tab priceLayout = new Tab("Прайс-лист");
        Tab balanceLayout = new Tab("Остататки");
        Tab revenueLayout = new Tab("Обороты");


        return new Tabs(
                new Tab(goodsLayout),
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
        getUI().ifPresent(ui -> {
            div.removeAll();
            goodsView.updateData();
            div.add(goodsView);

        });
    }
}
