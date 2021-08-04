package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@PageTitle("Перемещения")
@Route(value = "movementTab", layout = AppView.class)
@UIScope
public class MovementView extends VerticalLayout {
    public void updateList() {
    }
}
