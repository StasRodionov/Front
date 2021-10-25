package com.trade_accounting.components.production;

import com.trade_accounting.components.AppView;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
@PageTitle("Производственные Задания")
@Route(value = "productionTargets", layout = AppView.class)
public class ProductionTargetsViewTab {
}
