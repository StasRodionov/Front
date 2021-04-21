package com.trade_accounting.components.money;

import com.trade_accounting.components.AppView;
import com.trade_accounting.services.interfaces.PaymentService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "money", layout = AppView.class)
@PageTitle("Деньги")
public class MoneySubMenuView extends Div implements AfterNavigationObserver{

    private final Div div;
    private final PaymentService paymentService;
    private final PaymentModalWin paymentModalWin;

    public MoneySubMenuView(PaymentService paymentService, PaymentModalWin paymentModalWin) {
        this.paymentService = paymentService;
        this.paymentModalWin = paymentModalWin;
        div = new Div();
        add(configurationSubMenu(), div);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        div.removeAll();
        div.add(new MoneySubPaymentsView(paymentService, paymentModalWin));

        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(5);
            }
        });
    }

    private Tabs configurationSubMenu() {
        Tabs tabs = new Tabs(
                new Tab("Платежи"),
                new Tab("Движение денежных средств"),
                new Tab("Прибыли и убытки"),
                new Tab("Взаиморасчеты"),
                new Tab("Корректировки")
        );

        tabs.addSelectedChangeListener(event -> {
            String tabName = event.getSelectedTab().getLabel();
            switch (tabName) {
                case "Платежи":
                    div.removeAll();
                    div.add(new MoneySubPaymentsView(paymentService, paymentModalWin));
                    break;
                case "Движение денежных средств":
                    div.removeAll();
                    div.add(String.valueOf(new MoneySubCashFlowView()));
                    break;
                case "Прибыли и убытки":
                    div.removeAll();
                    div.add(String.valueOf(new MoneySubProfitLossView()));
                    break;
                case "Взаиморасчеты":
                    div.removeAll();
                    div.add(String.valueOf(new MoneySubMutualSettlementsView()));
                    break;
                case "Корректировки":
                    div.removeAll();
                    div.add(String.valueOf(new MoneySubCorrectionView()));
                    break;
            }
        });
        return tabs;

    }



}
