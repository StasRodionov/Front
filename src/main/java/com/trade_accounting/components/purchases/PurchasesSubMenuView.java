package com.trade_accounting.components.purchases;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.sells.SalesSubCustomersOrders;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "purchases", layout = AppView.class)
@PageTitle("Закупки")
public class PurchasesSubMenuView extends Div implements AfterNavigationObserver {

    private final Div div;
    private final InvoiceService invoiceService;

    public PurchasesSubMenuView(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
        div = new Div();
        add(configurationSubMenu(), div);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(1);
            }
        });
        getUI().ifPresent(ui -> {
            div.removeAll();
            div.add(new PurchasesSubSuppliersOrders(invoiceService));
        });
    }

    private Tabs configurationSubMenu() {
        Tab supplierOrdersLayout = new Tab(new Label("Заказы поставщикам"));
        Tab vendorAccountsLayout = new Tab(new Label("Счета поставщиков"));
        Tab admissionsLayout = new Tab(new Label("Приемки"));
        Tab refundsToSuppliersLayout = new Tab(new Label("Возвраты поставщикам"));
        Tab invoicesReceivedLayout = new Tab(new Label("Счета-фактуры полученные"));
        Tab purchasingManagementLayout = new Tab(new Label("Управление закупками"));

        return new Tabs(
                supplierOrdersLayout,
                vendorAccountsLayout,
                admissionsLayout,
                refundsToSuppliersLayout,
                invoicesReceivedLayout,
                purchasingManagementLayout
        );
    }
}
