package com.trade_accounting.components.goods;


import com.trade_accounting.components.AppView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;


@Route(value = "goods", layout = AppView.class)
@PageTitle("Товары")
@SpringComponent
@UIScope
public class GoodsSubMenuView extends Div implements AfterNavigationObserver {
    private final GoodsView goodsView;
    private final PostingTabView postingTabView;
    private final GoodsSubInventory goodsSubInventory;
    private final GoodsSubInternalOrder goodsSubInternalOrder;
    private final MovementView movementView;
    private final RemainView remainView;
    private final RevenueView revenueView;
    private final LossView lossView;
    private final GoodsPriceLayout priceLayoutView;
    private final SerialNumbersView serialNumbersView;

    private final Div div = new Div();

    public GoodsSubMenuView(GoodsView goodsView, PostingTabView postingTabView,
                            GoodsSubInventory goodsSubInventory, GoodsSubInternalOrder goodsSubInternalOrder,
                            MovementView movementView, RemainView remainView, LossView lossView,
                            GoodsPriceLayout priceLayoutView, RevenueView revenueView, SerialNumbersView serialNumbersView) {
        this.goodsView = goodsView;
        this.revenueView = revenueView;
        this.postingTabView = postingTabView;
        this.goodsSubInventory = goodsSubInventory;
        this.goodsSubInternalOrder = goodsSubInternalOrder;
        this.movementView = movementView;
        this.remainView = remainView;
        this.priceLayoutView = priceLayoutView;
        this.lossView = lossView;
        this.serialNumbersView = serialNumbersView;


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

        HorizontalLayout postingTab = new HorizontalLayout(new Label("Оприходования"));

        postingTab.addClickListener(event ->
                postingTab.getUI().ifPresent(ui -> {
                    div.removeAll();
                    postingTabView.updateList();
                    div.add(postingTabView);
                }));

        HorizontalLayout inventoryTab = new HorizontalLayout(new Label("Инвентаризации"));

        inventoryTab.addClickListener(event ->
                inventoryTab.getUI().ifPresent(ui -> {
                    div.removeAll();
                    goodsSubInventory.updateList();
                    div.add(goodsSubInventory);
                }));

        HorizontalLayout internalOrderTab = new HorizontalLayout(new Label("Внутренние заказы"));
        internalOrderTab.addClickListener(event ->
                internalOrderTab.getUI().ifPresent(ui -> {
                    div.removeAll();
                    goodsSubInternalOrder.updateList();
                    div.add(goodsSubInternalOrder);
                }));

        HorizontalLayout lossTab = new HorizontalLayout(new Label("Списания"));

        lossTab.addClickListener(event ->
                lossTab.getUI().ifPresent(ui -> {
                    div.removeAll();
                    lossView.updateList();
                    div.add(lossView);
                }));

        HorizontalLayout movementTab = new HorizontalLayout(new Label("Перемещения"));

        movementTab.addClickListener(event ->
                movementTab.getUI().ifPresent(ui -> {
                    div.removeAll();
                    movementView.updateList();
                    div.add(movementView);
                }));


        Tab interventarizationLayout = new Tab("Инвентаризации");
        Tab internalOrdersLayout = new Tab("Внутрение заказы");

        HorizontalLayout remainTab = new HorizontalLayout(new Label("Остатки"));

        remainTab.addClickListener(event ->
                remainTab.getUI().ifPresent(ui -> {
                    div.removeAll();
                    remainView.updateList();
                    div.add(remainView);
                }));

        HorizontalLayout revenueTab = new HorizontalLayout(new Label("Обороты"));

        revenueTab.addClickListener(event ->
                revenueTab.getUI().ifPresent(ui -> {
                    div.removeAll();
              //      revenueView.updateList();
                    div.add(revenueView);
                }));



        HorizontalLayout priceLayoutTab = new HorizontalLayout(new Label("Прайс-листы"));

        priceLayoutTab.addClickListener(event ->
                priceLayoutTab.getUI().ifPresent(ui -> {
                    div.removeAll();
                    priceLayoutView.updateList();
                    div.add(priceLayoutView);
                }));

        HorizontalLayout serialNumbersTab = new HorizontalLayout(new Label("Сер.номера"));

        serialNumbersTab.addClickListener(event ->
                serialNumbersTab.getUI().ifPresent(ui -> {
                    div.removeAll();
                    serialNumbersView.updateList();
                    div.add(serialNumbersView);
                }));

        return new Tabs(
                new Tab(goodsLayout),
                new Tab(postingTab),
                new Tab(inventoryTab),
                new Tab(internalOrderTab),
                new Tab(lossTab),
                new Tab(movementTab),
                new Tab(priceLayoutTab),
                new Tab(remainTab),
                new Tab(revenueTab),
                new Tab(serialNumbersTab)
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
