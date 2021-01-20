package com.trade_accounting.components;

import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.vaadin.flow.component.html.Div;
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
    private final ContractorService contractorService;

    public ContractorsView(ContractService contractService,
                           ContractorService contractorService) {
        this.contractorService = contractorService;
        this.contractService = contractService;
        div = new Div();
        add(configurationSubMenu(), div);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
            div.removeAll();
            div.add(new ContractorsTabView(contractorService));

            AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
            appView.getChildren().forEach(e -> {
                if (e.getClass() == Tabs.class) {
                    ((Tabs) e).setSelectedIndex(4);
                }
            });
    }

    private Tabs configurationSubMenu() {

        Tabs tabs = new Tabs(new Tab("Контрагенты"), new Tab("Договоры"));

        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);

        tabs.addSelectedChangeListener(event -> {
            String tabName = event.getSelectedTab().getLabel();
            switch (tabName) {
                case "Контрагенты":
                    div.removeAll();
                    div.add(new ContractorsTabView(contractorService));
                    break;
                case "Договоры":
                    div.removeAll();
                    div.add(new ContractsView(contractService));
            }
        });
        return tabs;
    }
}
