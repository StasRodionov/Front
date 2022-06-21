package com.trade_accounting.components.purchases;

import com.trade_accounting.components.AppView;
import com.trade_accounting.services.interfaces.invoice.InvoiceService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import static com.trade_accounting.config.SecurityConstants.*;

@Slf4j
@Route(value = PURCHASES, layout = AppView.class)
@PageTitle("Закупки")
@SpringComponent
@UIScope
public class PurchasesSubMenuView extends Div implements AfterNavigationObserver { // некорректно задаётся id при добавлении

    private final InvoiceService invoiceService;
    private final PurchasesSubSuppliersOrders purchasesSubSuppliersOrders;
    private final PurchasesSubVendorAccounts purchasesSubVendorAccounts;
    private final PurchasesSubReturnToSuppliers purchasesSubReturnToSuppliers;
    private final PurchasesSubMenuInvoicesReceived purchasesSubMenuInvoicesReceived;
    private final PurchasesSubAcceptances purchasesSubAcceptances;
    private final PurchasesSubPurchasingManagement purchasesSubPurchasingManagement;

    private final Tab supplierOrdersLayout = new Tab(new Label("Заказы поставщикам"));
    private final Tab vendorAccountsLayout = new Tab(new Label("Счета поставщиков"));
    private final Tab admissionsLayout = new Tab(new Label("Приемки"));
    private final Tab refundsToSuppliersLayout = new Tab(new Label("Возвраты поставщикам"));
    private final Tab invoicesReceivedLayout = new Tab(new Label("Счета-фактуры полученные"));
    private final Tab purchasingManagementLayout = new Tab(new Label("Управление закупками"));

    private final Div div;
    private final Tabs tabs;
    private final Dialog dialogOnTabSwitch = new Dialog();
    private boolean isTabSwitchProtected;
    private Tab currentTab;

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

        this.tabs = configureSubMenu();
        this.isTabSwitchProtected = false;
        this.div = new Div();
        add(tabs, div);
        configureDialogOnTabSwitch();
    }

    private void configureDialogOnTabSwitch() {
        dialogOnTabSwitch.setCloseOnEsc(false);
        dialogOnTabSwitch.setCloseOnOutsideClick(false);
        Shortcuts.addShortcutListener(dialogOnTabSwitch,
                () -> {
                    tabs.setSelectedTab(currentTab);
                    dialogOnTabSwitch.close();
                },
                Key.ESCAPE);
    }

    private Tabs configureSubMenu() {
        return new Tabs(
                supplierOrdersLayout,
                vendorAccountsLayout,
                admissionsLayout,
                refundsToSuppliersLayout,
                invoicesReceivedLayout,
                purchasingManagementLayout
        );
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        tabs.addSelectedChangeListener(event -> {
            if (isTabSwitchProtected) {
                confirmTabSwitch(event.getSelectedTab());
            } else {
                executeTabSwitch(event.getSelectedTab());
            }
        });

        resetTabSelection(PURCHASES);
    }

    private void executeTabSwitch(Tab pressedTab) {
        div.removeAll();
        if (supplierOrdersLayout.equals(pressedTab)) {
            div.add(purchasesSubSuppliersOrders);
            purchasesSubSuppliersOrders.refreshContent();
        } else if (vendorAccountsLayout.equals(pressedTab)) {
            div.add(purchasesSubVendorAccounts);
        } else if (refundsToSuppliersLayout.equals(pressedTab)) {
            div.add(purchasesSubReturnToSuppliers);
        } else if (invoicesReceivedLayout.equals(pressedTab)) {
            div.add(purchasesSubMenuInvoicesReceived);
        } else if (admissionsLayout.equals(pressedTab)) {
            div.add(purchasesSubAcceptances);
        } else if (purchasingManagementLayout.equals(pressedTab)) {
            div.add(purchasesSubPurchasingManagement);
        }

        this.currentTab = pressedTab;
        isTabSwitchProtected = false;
    }

    private void confirmTabSwitch(Tab pressedTab) {
        dialogOnTabSwitch.removeAll();
        Button confirmButton = new Button("Продолжить", event -> {
            executeTabSwitch(pressedTab);
            dialogOnTabSwitch.close();
        });
        Button cancelButton = new Button("Отменить", event -> {
            tabs.setSelectedTab(currentTab);
            dialogOnTabSwitch.close();
        });
        dialogOnTabSwitch.add(new VerticalLayout(
                new Text("Вы уверены? Несохраненные данные будут утеряны!"),
                new HorizontalLayout(cancelButton, confirmButton))
        );
        dialogOnTabSwitch.open();
    }

    private void routeTabSwitch(String subTabUrl) {
        this.tabs.setSelectedIndex(-1);
        switch (subTabUrl) {
            case PURCHASES_SUPPLIERS_ORDERS_VIEW:
                this.tabs.setSelectedTab(supplierOrdersLayout);
                break;
            case PURCHASES_SUPPLIERS_INVOICES_VIEW:
                this.tabs.setSelectedTab(vendorAccountsLayout);
                break;
            case PURCHASES_ADMISSIONS_VIEW:
                this.tabs.setSelectedTab(admissionsLayout);
                break;
            case PURCHASES_RETURNS_TO_SUPPLIERS_VIEW:
                this.tabs.setSelectedTab(refundsToSuppliersLayout);
                break;
            case PURCHASES_INVOICE_RECEIVED_VIEW:
                this.tabs.setSelectedTab(invoicesReceivedLayout);
                break;
            case PURCHASES_PURCHASING_MANAGEMENT_VIEW:
                this.tabs.setSelectedTab(purchasingManagementLayout);
                break;
        }
    }

    public void resetTabSelection(String subTabURL, boolean isProtected) {
        this.isTabSwitchProtected = isProtected;
        resetTabSelection(subTabURL);
    }

    public void resetTabSelection(String subTabUrl) {
        routeTabSwitch(subTabUrl);
        this.currentTab = this.tabs.getSelectedTab();
    }

    public void releaseProtectedTabSwitch() {
        this.isTabSwitchProtected = false;
    }

    public void setProtectedTabSwitch() {
        this.isTabSwitchProtected = true;
    }
}
