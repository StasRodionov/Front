package com.trade_accounting.components;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "MoneySubMutualSettlementsView", layout = AppView.class)
@PageTitle("Взаиморасчеты")
public class MoneySubMutualSettlementsView {

    private H2 title(){
        H2 title = new H2("Взаиморасчеты");
        title.setHeight("2.2em");
        return title;
    }

}
