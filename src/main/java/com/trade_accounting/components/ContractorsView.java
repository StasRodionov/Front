package com.trade_accounting.components;

import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "contractors", layout = AppView.class)
@PageTitle("Контрагенты")
public class ContractorsView extends Div {

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

    private Tabs configurationSubMenu() {

        Tabs tabs = new Tabs(new Tab("контрагенты"), new Tab("договоры"));

        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);

        tabs.addSelectedChangeListener(event -> {
            String tabName = event.getSelectedTab().getLabel();
            switch (tabName) {
                case "контрагенты":
                    div.removeAll();
                    div.add(new ContractorsTabView(contractorService));
                    break;
                case "договоры":
                    div.removeAll();
                    div.add(new ContractsView(contractService));
            }
        });
        return tabs;
    }
}
