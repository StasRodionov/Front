package com.trade_accounting.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "contractors", layout = AppView.class)
@PageTitle("Контрагенты")
public class ContractorsView extends Div implements AfterNavigationObserver {

    public ContractorsView() {
        add(configurationSubMenu());
    }


    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(4);
            }
        });
    }
    private Tabs configurationSubMenu() {
        Tabs tabs = new Tabs(
                new Tab(new Label("Контрагенты")),
                new Tab(new Label("Договоры"))
        );
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        return tabs;
    }
}
