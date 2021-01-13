package com.trade_accounting.components;

import com.trade_accounting.services.interfaces.ContractService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "contractors", layout = AppView.class)
@PageTitle("Контрагенты")
public class ContractorsView extends Div implements AfterNavigationObserver {

    private final Div div;

    private final ContractService contractService;

    public ContractorsView(ContractService contractService) {
        this.contractService = contractService;
        div = new Div();

        add(configurationSubMenu(), div);
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

        HorizontalLayout contractors = new HorizontalLayout(new Label("Контрагенты"));
        HorizontalLayout contracts = new HorizontalLayout(new Label("Договоры"));
        contracts.addClickListener(e -> contracts.getUI().ifPresent(ui -> {
            div.removeAll();
            div.add(new ContractorsView(contractService));
        }));

        Tabs tabs = new Tabs(
                new Tab(contractors),
                new Tab(contracts)
        );
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        return tabs;
    }
}
