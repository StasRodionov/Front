package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Route(value = "goods/revenue-edit", layout = AppView.class)
@PageTitle("Оборот")
@PreserveOnRefresh
@SpringComponent
@UIScope
public class RevenueModalWindow extends Dialog {
}
