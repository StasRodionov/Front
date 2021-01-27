package com.trade_accounting.components.money;

import com.trade_accounting.components.AppView;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "MoneySubPaymentsView", layout = AppView.class)
@PageTitle("Платежи")
public class MoneySubPaymentsView {

    private H2 title(){
        H2 title = new H2("Платежи");
        title.setHeight("2.2em");
        return title;
    }

}
