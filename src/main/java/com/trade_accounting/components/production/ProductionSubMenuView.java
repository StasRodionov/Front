package com.trade_accounting.components.production;

import com.trade_accounting.components.AppView;
import com.trade_accounting.services.interfaces.ProductService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "production", layout = AppView.class)
@PageTitle("Производство")
public class ProductionSubMenuView extends Div implements AfterNavigationObserver {

    private final Div div;

    @Autowired
    public ProductionSubMenuView() {
        div = new Div();
        add(configurationSubMenu(), div);
    }



    private Tabs configurationSubMenu() {

        Tabs tabs = new Tabs(
                new Tab("Тех. карты"),
                new Tab("Заказы на производство"),
                new Tab("Тех. операции")
        );
        tabs.addSelectedChangeListener(event -> {
            String name = event.getSelectedTab().getLabel();

            switch (name) {
                case "Тех. карты":
                    div.removeAll();
                    break;
                case "Заказы на производство":
                    div.removeAll();
                    div.add(new OrdersMenuView());
                    break;
                case "Тех. операции":
                    div.removeAll();
                    break;
            }
        });
        return tabs;


//        Tab cardsLayout = new Tab("Тех. карты");
//        Tab ordersLayout = new Tab("Тех. карты");
//        Tab operationsLayout = new Tab("Тех. операции");
//
//
//        return new Tabs(
//                cardsLayout,
//                ordersLayout,
//                operationsLayout
//        );


    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        div.removeAll();
        div.add(new OrdersMenuView());
//        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
//        appView.getChildren().forEach(e -> {
//            if (e.getClass() == Tabs.class) {
//                ((Tabs) e).setSelectedIndex(7);
//            }
//        });
    }
}

