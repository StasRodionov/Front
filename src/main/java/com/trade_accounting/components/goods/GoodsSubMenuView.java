package com.trade_accounting.components.goods;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.authentication.LoginView;
import com.trade_accounting.services.interfaces.ProductGroupService;
import com.trade_accounting.services.interfaces.ProductService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;

import javax.validation.constraints.Null;

@Route(value = "goods", layout = AppView.class)
@PageTitle("Товары")
public class GoodsSubMenuView extends Div implements AfterNavigationObserver {
    private final ProductService productService;
    private final ProductGroupService productGroupService;

    private final Div div;

    GoodsSubMenuView(ProductService productService, ProductGroupService productGroupService) {
        this.productService = productService;
        this.productGroupService = productGroupService;
        div = new Div();
        add(configurationSubMenu(), div);
    }

    private Tabs configurationSubMenu() {
        HorizontalLayout goodsLayout = new HorizontalLayout(new Label("Товары и услуги"));

        goodsLayout.addClickListener(e ->
                goodsLayout.getUI().ifPresent(ui -> {
                    div.removeAll();
                    div.add(new GoodsView(productService, productGroupService));
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
        try {
            getUI().ifPresent(ui -> {
                div.removeAll();
                div.add(new GoodsView(productService, productGroupService));
            });
        } catch (NullPointerException e) {
            WrappedSession wrappedSession = VaadinSession.getCurrent().getSession();
            wrappedSession.setAttribute("redirectDestination", "/goods");
            UI.getCurrent().navigate(LoginView.class);
        }
    }
}
