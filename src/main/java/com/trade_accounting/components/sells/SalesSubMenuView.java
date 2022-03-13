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
import com.vaadin.flow.component.html.Div;
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

@Route(value = "sells", layout = AppView.class)
@PageTitle("Продажи")
@SpringComponent
@UIScope
public class SalesSubMenuView extends Div implements AfterNavigationObserver {//некорректно задаётся id при добавлении

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
    private final Div div;
    private final InvoicesStatusService invoicesStatusService;
    private final SalesEditCreateInvoiceView salesEditCreateInvoiceView;
    private final ContractorStatusService contractorStatusService;
    private final FunnelService funnelService;

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


        div = new Div();
        add(configurationSubMenu(), div);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(2);
            }
        });
        getUI().ifPresent(ui -> {
            div.removeAll();
            salesSubBuyersReturnsView.updateData();
            div.add(salesSubBuyersReturnsView);
        });
        configurationSubMenu();
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
                    div.add(salesSubCustomersOrdersView);
                    break;
                case "Счета покупателям":
                    div.removeAll();
                    div.add(salesSubInvoicesToBuyersView);
                    break;
                case "Отгрузки":
                    div.removeAll();
                    div.add(salesSubShipmentView);
                    break;
                case "Отчеты комиссионера":
                    div.removeAll();
                    div.add(new SalesSubAgentReportsView(invoiceService, contractorService, companyService, warehouseService, commissionAgentReportModalView, notifications, contractService));
                    break;
                case "Возвраты покупателей":
                    div.removeAll();
                    salesSubBuyersReturnsView.updateData();

                    div.add(salesSubBuyersReturnsView);
                    break;
                case "Счета-фактуры выданные":
                    div.removeAll();
                    div.add(new SalesSubIssuedInvoicesView(issuedInvoiceService, companyService, contractorService, paymentService));
                    break;
                case "Прибыльность":
                    div.removeAll();
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
                    break;
                case "Товары на реализации":
                    div.removeAll();
                    div.add(new SalesSubGoodsForSaleView(salesSubGoodsForSaleService, productService));
                    break;
                case "Воронка продаж":
                    div.removeAll();
                    div.add(new SalesSubSalesFunnelView(contractorStatusService, invoicesStatusService, funnelService));
                    break;
            }
        });
        return tabs;
    }
}

