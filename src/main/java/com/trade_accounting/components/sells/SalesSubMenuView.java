package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.authentication.LoginView;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;

@Route(value = "sells", layout = AppView.class)
@PageTitle("Продажи")
public class SalesSubMenuView extends Div implements AfterNavigationObserver {

    private final Div div;
    private final InvoiceService invoiceService;
    private final ContractorService contractorService;
    private final CompanyService companyService;

    public SalesSubMenuView(InvoiceService invoiceService,
                            ContractorService contractorService,
                            CompanyService companyService) {
        this.invoiceService = invoiceService;
        this.contractorService = contractorService;
        this.companyService = companyService;
        div = new Div();
        add(configurationSubMenu(), div);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
//        div.removeAll();
//        div.add(new SalesSubCustomersOrders(invoiceService, contractorService, companyService));

        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(2);
            }
        });
        try {
            getUI().ifPresent(ui -> {
                div.removeAll();
                div.add(new SalesSubCustomersOrdersView(invoiceService, contractorService, companyService));
            });
        } catch (NullPointerException e) {
            WrappedSession wrappedSession = VaadinSession.getCurrent().getSession();
            wrappedSession.setAttribute("redirectDestination", "/sells");
            UI.getCurrent().navigate(LoginView.class);
        }
    }


    private Tabs configurationSubMenu() {
        Tabs tabs = new Tabs(
                new Tab("Заказы покупателей"),
                new Tab("Счета покупателям"),
                new Tab("Отгрузки"),
                new Tab("Отчеты комиссионера"),
                new Tab("Возвраты покупателей"),
                new Tab("Счета-фактуры выданные"),
                new Tab("Прибыльность"),
                new Tab("Товары на реализации"),
                new Tab("Воронка продаж")
        );


        tabs.addSelectedChangeListener(event -> {
            String tabName = event.getSelectedTab().getLabel();
            switch (tabName) {
                case "Заказы покупателей":
                    div.removeAll();
                    div.add(new SalesSubCustomersOrdersView(invoiceService, contractorService, companyService));
                    break;
                case "Счета покупателям":
                    div.removeAll();
                    div.add(new SalesSubInvoicesToBuyersView(invoiceService, contractorService, companyService));
                    break;
                case "Отгрузки":
                    div.removeAll();
                    div.add(new SalesSubShipmentView(invoiceService, contractorService, companyService));
                    break;
                case "Отчеты комиссионера":
                    div.removeAll();
                    div.add(new SalesSubAgentReportsView(invoiceService, contractorService, companyService));
                    break;
                case "Возвраты покупателей":
                    div.removeAll();
                    div.add(new String("SalesSubBuyersReturnsView"));
                    break;
                case "Счета-фактуры выданные":
                    div.removeAll();
                    div.add(new String("SalesSubIssuedInvoicesView"));
                    break;
                case "Прибыльность":
                    div.removeAll();
                    div.add(new String("SalesSubProfitabilityView"));
                    break;
                case "Товары на реализации":
                    div.removeAll();
                    div.add(new String("SalesSubGoodsForSaleView"));
                    break;
                case "Воронка продаж":
                    div.removeAll();
                    div.add(new String("SalesSubSalesFunnelView"));
                    break;
            }
        });
        return tabs;
    }
}

