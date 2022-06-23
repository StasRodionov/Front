package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.services.interfaces.company.ContractorStatusService;
import com.trade_accounting.services.interfaces.finance.FunnelService;
import com.trade_accounting.services.interfaces.invoice.InvoicesStatusService;
import com.trade_accounting.services.interfaces.warehouse.BuyersReturnService;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.invoice.InvoiceProductService;
import com.trade_accounting.services.interfaces.invoice.InvoiceService;
import com.trade_accounting.services.interfaces.invoice.IssuedInvoiceService;
import com.trade_accounting.services.interfaces.finance.PaymentService;
import com.trade_accounting.services.interfaces.client.PositionService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.trade_accounting.services.interfaces.retail.RetailStoreService;
import com.trade_accounting.services.interfaces.finance.ReturnAmountByProductService;
import com.trade_accounting.services.interfaces.warehouse.SalesSubGoodsForSaleService;
import com.trade_accounting.services.interfaces.warehouse.ShipmentProductService;
import com.trade_accounting.services.interfaces.warehouse.ShipmentService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import static com.trade_accounting.config.SecurityConstants.*;

@Route(value = SELLS, layout = AppView.class)
@PageTitle("Продажи")
@SpringComponent
@UIScope
public class SalesSubMenuView extends Div implements AfterNavigationObserver {   //некорректно задаётся id при добавлении

    private final ContractService contractService;
    private final InvoiceService invoiceService;
    private final ContractorService contractorService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final InvoiceProductService invoiceProductService;
    private final BuyersReturnService buyersReturnService;
    private final IssuedInvoiceService issuedInvoiceService;
    private final PaymentService paymentService;
    private final ProductService productService;
    private final ReturnAmountByProductService returnAmountByProductService;
    private final EmployeeService employeeService;
    private final PositionService positionService;
    private final RetailStoreService retailStoreService;
    private final SalesSubGoodsForSaleService salesSubGoodsForSaleService;
    private final SalesSubCustomersOrdersView salesSubCustomersOrdersView;
    private final SalesSubShipmentView salesSubShipmentView;
    private final SalesSubInvoicesToBuyersView salesSubInvoicesToBuyersView;
    private final CommissionAgentReportModalView commissionAgentReportModalView;
    private final SalesSubGoodsForSaleView salesSubGoodsForSaleView;
    private final ReturnBuyersReturnModalView returnBuyersReturnModalView;
    private final Notifications notifications;
    private final ShipmentService shipmentService;
    private final ShipmentProductService shipmentProductService;
    private final SalesSubBuyersReturnsView salesSubBuyersReturnsView;
    private final InvoicesStatusService invoicesStatusService;
    private final SalesEditCreateInvoiceView salesEditCreateInvoiceView;
    private final ContractorStatusService contractorStatusService;
    private final FunnelService funnelService;

    private final Tab customerOrdersLayout = new Tab("Заказы покупателей");
    private final Tab invoicesToBuyersLayout = new Tab("Счета покупателям");
    private final Tab shipmentLayout = new Tab("Отгрузки");
    private final Tab agentReportsLayout = new Tab("Отчеты комиссионера");
    private final Tab buyersReturnsLayout = new Tab("Возвраты покупателей");
    private final Tab issuedInvoicesLayout = new Tab("Счета-фактуры выданные");
    private final Tab profitabilityLayout = new Tab("Прибыльность");
    private final Tab goodsForSaleLayout = new Tab("Товары на реализации");
    private final Tab salesFunnelLayout = new Tab("Воронка продаж");

    private final Div div;
    private final Tabs tabs;
    private final Dialog dialogOnTabSwitch = new Dialog();
    private boolean isTabSwitchProtected;
    private Tab currentTab;

    @Autowired
    public SalesSubMenuView(ContractService contractService, InvoiceService invoiceService,
                            ContractorService contractorService,
                            CompanyService companyService,
                            WarehouseService warehouseService,
                            BuyersReturnService buyersReturnService,
                            InvoiceProductService invoiceProductService,
                            IssuedInvoiceService issuedInvoiceService,
                            PaymentService paymentService,
                            SalesSubGoodsForSaleService salesSubGoodsForSaleService,
                            @Lazy SalesSubCustomersOrdersView salesSubCustomersOrdersView,
                            @Lazy SalesSubShipmentView salesSubShipmentView,
                            @Lazy SalesSubInvoicesToBuyersView salesSubInvoicesToBuyersView,
                            @Lazy SalesSubGoodsForSaleView salesSubGoodsForSaleView,
                            @Lazy SalesSubBuyersReturnsView salesSubBuyersReturnsView,
                            CommissionAgentReportModalView commissionAgentReportModalView,
                            ReturnBuyersReturnModalView returnBuyersReturnModalView,
                            Notifications notifications, ProductService productService,
                            ReturnAmountByProductService returnAmountByProductService,
                            EmployeeService employeeService,
                            PositionService positionService,
                            RetailStoreService retailStoreService,
                            ShipmentService shipmentService,
                            ShipmentProductService shipmentProductService,
                            InvoicesStatusService invoicesStatusService, SalesEditCreateInvoiceView salesEditCreateInvoiceView, ContractorStatusService contractorStatusService, FunnelService funnelService) {
        this.contractService = contractService;
        this.shipmentService = shipmentService;
        this.shipmentProductService = shipmentProductService;
        this.invoiceProductService = invoiceProductService;
        this.issuedInvoiceService = issuedInvoiceService;
        this.paymentService = paymentService;
        this.salesSubCustomersOrdersView = salesSubCustomersOrdersView;
        this.salesSubGoodsForSaleView = salesSubGoodsForSaleView;
        this.invoiceService = invoiceService;
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.buyersReturnService = buyersReturnService;
        this.salesSubShipmentView = salesSubShipmentView;
        this.salesSubInvoicesToBuyersView = salesSubInvoicesToBuyersView;
        this.commissionAgentReportModalView = commissionAgentReportModalView;
        this.returnBuyersReturnModalView = returnBuyersReturnModalView;
        this.salesSubGoodsForSaleService = salesSubGoodsForSaleService;
        this.notifications = notifications;
        this.productService = productService;
        this.returnAmountByProductService = returnAmountByProductService;
        this.employeeService = employeeService;
        this.positionService = positionService;
        this.retailStoreService = retailStoreService;
        this.salesSubBuyersReturnsView = salesSubBuyersReturnsView;
        this.invoicesStatusService = invoicesStatusService;
        this.salesEditCreateInvoiceView = salesEditCreateInvoiceView;
        this.contractorStatusService = contractorStatusService;
        this.funnelService = funnelService;

//        this.div = new Div();
//        add(configurationSubMenu(), div);

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
                customerOrdersLayout,
                invoicesToBuyersLayout,
                shipmentLayout,
                agentReportsLayout,
                buyersReturnsLayout,
                issuedInvoicesLayout,
                profitabilityLayout,
                goodsForSaleLayout,
                salesFunnelLayout
        );
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
//        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
//        appView.getChildren().forEach(e -> {
//            if (e.getClass() == Tabs.class) {
//                ((Tabs) e).setSelectedIndex(2);
//            }
//        });
//        getUI().ifPresent(ui -> {
//            div.removeAll();
//            salesSubBuyersReturnsView.updateData();
//            div.add(salesSubBuyersReturnsView);
//        });
//        configureSubMenu();

        tabs.addSelectedChangeListener(event -> {
            if (isTabSwitchProtected) {
                confirmTabSwitch(event.getSelectedTab());
            } else {
                executeTabSwitch(event.getSelectedTab());
            }
        });

        resetTabSelection(SELLS);
    }

    private void executeTabSwitch(Tab pressedTab) {
        div.removeAll();
        if (customerOrdersLayout.equals(pressedTab)) {
            div.add(salesSubCustomersOrdersView);
            salesSubCustomersOrdersView.refreshContent();
        } else if (invoicesToBuyersLayout.equals(pressedTab)) {
            div.add(salesSubInvoicesToBuyersView);
        } else if (shipmentLayout.equals(pressedTab)) {
            div.add(salesSubShipmentView);
        } else if (agentReportsLayout.equals(pressedTab)) {
            div.add(new SalesSubAgentReportsView(invoiceService, contractorService, companyService, warehouseService, commissionAgentReportModalView, notifications, contractService));
        } else if (buyersReturnsLayout.equals(pressedTab)) {
            salesSubBuyersReturnsView.updateData();
            div.add(salesSubBuyersReturnsView);
        } else if (issuedInvoicesLayout.equals(pressedTab)) {
            div.add(new SalesSubIssuedInvoicesView(issuedInvoiceService, companyService, contractorService, paymentService));
        } else if (profitabilityLayout.equals(pressedTab)) {
            div.add(new SalesSubProfitabilityView(
                    invoiceService,
                    companyService,
                    contractorService,
                    invoiceProductService,
                    productService,
                    buyersReturnService,
                    returnAmountByProductService,
                    employeeService,
                    positionService,
                    retailStoreService));
        } else if (goodsForSaleLayout.equals(pressedTab)) {
            div.add(new SalesSubGoodsForSaleView(salesSubGoodsForSaleService, productService));
        } else if (salesFunnelLayout.equals(pressedTab)) {
            div.add(new SalesSubSalesFunnelView(contractorStatusService, invoicesStatusService, funnelService));
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
            case SELLS_CUSTOMERS_ORDERS_VIEW:
                this.tabs.setSelectedTab(customerOrdersLayout);
                break;
            case SELLS_INVOICES_TO_BUYERS_VIEW:
                this.tabs.setSelectedTab(invoicesToBuyersLayout);
                break;
            case SELLS_SHIPMENT_VIEW:
                this.tabs.setSelectedTab(shipmentLayout);
                break;
            case SELLS_AGENT_REPORTS_VIEW:
                this.tabs.setSelectedTab(agentReportsLayout);
                break;
            case SELLS_BUYERS_RETURNS_VIEW:
                this.tabs.setSelectedTab(buyersReturnsLayout);
                break;
            case SELLS_ISSUED_INVOICE_VIEW:
                this.tabs.setSelectedTab(issuedInvoicesLayout);
                break;
            case SELLS_PROFITABILITY_VIEW:
                this.tabs.setSelectedTab(profitabilityLayout);
                break;
            case SELLS_GOODS_FOR_SALE_VIEW:
                this.tabs.setSelectedTab(goodsForSaleLayout);
                break;
            case SELLS_SALES_SUB_SALES_FUNNEL_VIEW:
                this.tabs.setSelectedTab(salesFunnelLayout);
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

