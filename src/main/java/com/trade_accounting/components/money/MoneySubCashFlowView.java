package com.trade_accounting.components.money;

import com.trade_accounting.components.AppView;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "MoneySubCashFlowView", layout = AppView.class)
@PageTitle("Движение денежных средств")
public class MoneySubCashFlowView extends VerticalLayout {

    private H2 title() {
        H2 title = new H2("Движение денежных средств");
        title.setHeight("2.2em");
        return title;
    }

    public MoneySubCashFlowView() {
        add(title());
    }
}
