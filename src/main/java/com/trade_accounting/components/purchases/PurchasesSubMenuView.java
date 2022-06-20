package com.trade_accounting.components.purchases;

import com.trade_accounting.components.AppView;
import com.trade_accounting.services.interfaces.invoice.InvoiceService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import static com.trade_accounting.config.SecurityConstants.PURCHASES;


// Добавить отдельный метод-ивент-лисенер под обработку события смены вкладки (вытащить из метода конфигурации)

@Route(value = PURCHASES, layout = AppView.class)
@PageTitle("Закупки")
@SpringComponent
@UIScope
public class PurchasesSubMenuView extends Div implements AfterNavigationObserver { //некорректно задаётся id при добавлении

    private final InvoiceService invoiceService;
    private final PurchasesSubSuppliersOrders purchasesSubSuppliersOrders;
    private final PurchasesSubVendorAccounts purchasesSubVendorAccounts;
    private final PurchasesSubReturnToSuppliers purchasesSubReturnToSuppliers;
    private final PurchasesSubMenuInvoicesReceived purchasesSubMenuInvoicesReceived;
    private final PurchasesSubAcceptances purchasesSubAcceptances;
    private final PurchasesSubPurchasingManagement purchasesSubPurchasingManagement;

    private final Div div;
    private final Tabs tabs;
    private final Tab supplierOrdersLayout = new Tab(new Label("Заказы поставщикам"));
    private final Tab vendorAccountsLayout = new Tab(new Label("Счета поставщиков"));
    private final Tab admissionsLayout = new Tab(new Label("Приемки"));
    private final Tab refundsToSuppliersLayout = new Tab(new Label("Возвраты поставщикам"));
    private final Tab invoicesReceivedLayout = new Tab(new Label("Счета-фактуры полученные"));
    private final Tab purchasingManagementLayout = new Tab(new Label("Управление закупками"));

    @Autowired
    public PurchasesSubMenuView(InvoiceService invoiceService,
                                @Lazy PurchasesSubSuppliersOrders purchasesSubSuppliersOrders,
                                @Lazy PurchasesSubVendorAccounts purchasesSubVendorAccounts, PurchasesSubReturnToSuppliers purchasesSubReturnToSuppliers,
                                PurchasesSubMenuInvoicesReceived purchasesSubMenuInvoicesReceived,
                                PurchasesSubAcceptances purchasesSubAcceptances,
                                PurchasesSubPurchasingManagement purchasesSubPurchasingManagement) {
        this.invoiceService = invoiceService;
        this.purchasesSubSuppliersOrders = purchasesSubSuppliersOrders;
        this.purchasesSubVendorAccounts = purchasesSubVendorAccounts;
        this.purchasesSubReturnToSuppliers = purchasesSubReturnToSuppliers;
        this.purchasesSubMenuInvoicesReceived = purchasesSubMenuInvoicesReceived;
        this.purchasesSubAcceptances = purchasesSubAcceptances;
        this.purchasesSubPurchasingManagement = purchasesSubPurchasingManagement;
        this.tabs = configurationSubMenu();
        div = new Div();
        add(tabs, div);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        tabs.addSelectedChangeListener(event -> {
            Tab tab = event.getSelectedTab();
            div.removeAll();

            if (supplierOrdersLayout.equals(tab)) {
                div.add(purchasesSubSuppliersOrders);
                purchasesSubSuppliersOrders.refreshContent();
            } else if (vendorAccountsLayout.equals(tab)) {
                div.add(purchasesSubVendorAccounts);
            } else if (refundsToSuppliersLayout.equals(tab)) {
                div.add(purchasesSubReturnToSuppliers);
            } else if (invoicesReceivedLayout.equals(tab)) {
                div.add(purchasesSubMenuInvoicesReceived);
            } else if (admissionsLayout.equals(tab)) {
                div.add(purchasesSubAcceptances);
            } else if (purchasingManagementLayout.equals(tab)) {
                div.add(purchasesSubPurchasingManagement);
            }
        });

        resetTabSelection(0);

//        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
//        appView.getChildren().forEach(e -> {
//            if (e.getClass() == Tabs.class) {
//                ((Tabs) e).setSelectedIndex(1);
//            }
//        });
//        getUI().ifPresent(ui -> {
//            div.removeAll();
//            div.add(purchasesSubSuppliersOrders);
//        });
    }

    private Tabs configurationSubMenu() {
        return new Tabs(
                supplierOrdersLayout,
                vendorAccountsLayout,
                admissionsLayout,
                refundsToSuppliersLayout,
                invoicesReceivedLayout,
                purchasingManagementLayout
        );
    }

    // PARDON
    // обязательно нормально параметризовать (не интом) - завязать на классы/табсы
    public void resetTabSelection(int index) {
        tabs.setSelectedIndex(-1);              // PARDON
        tabs.setSelectedIndex(index);
    }
}
