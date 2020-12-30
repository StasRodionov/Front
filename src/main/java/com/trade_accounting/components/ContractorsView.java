package com.trade_accounting.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "contractors", layout = AppView.class)
@PageTitle("Контрагенты")
public class ContractorsView extends Div{

    public ContractorsView() {
        add(addList());
    }

    private Tabs addList() {
        Tabs tabs = new Tabs(
                new Tab(new Label("Контрагенты")),
                new Tab(new Label("Договоры"))
        );
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        return tabs;
    }
}
