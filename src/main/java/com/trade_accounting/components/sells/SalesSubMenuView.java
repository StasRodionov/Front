package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.services.interfaces.BuyersReturnService;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.InvoiceProductService;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.trade_accounting.services.interfaces.IssuedInvoiceService;
import com.trade_accounting.services.interfaces.PaymentService;
import com.trade_accounting.services.interfaces.PositionService;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.RetailStoreService;
import com.trade_accounting.services.interfaces.ReturnAmountByProductService;
import com.trade_accounting.services.interfaces.WarehouseService;
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

    private final Div div;
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

    private final SalesSubCustomersOrdersView salesSubCustomersOrdersView;
    private final SalesSubShipmentView salesSubShipmentView;
    private final SalesSubInvoicesToBuyersView salesSubInvoicesToBuyersView;
    private final CommissionAgentReportModalView commissionAgentReportModalView;
    private final ReturnBuyersReturnModalView returnBuyersReturnModalView;
    private final Notifications notifications;


    @Autowired
    public SalesSubMenuView(InvoiceService invoiceService,
                            ContractorService contractorService,
                            CompanyService companyService,
                            WarehouseService warehouseService,
                            BuyersReturnService buyersReturnService,
                            InvoiceProductService invoiceProductService,
                            IssuedInvoiceService issuedInvoiceService,
                            PaymentService paymentService,
                            @Lazy SalesSubCustomersOrdersView salesSubCustomersOrdersView,
                            @Lazy SalesSubShipmentView salesSubShipmentView,
                            @Lazy SalesSubInvoicesToBuyersView salesSubInvoicesToBuyersView,
                            CommissionAgentReportModalView commissionAgentReportModalView,
                            ReturnBuyersReturnModalView returnBuyersReturnModalView,
                            Notifications notifications, ProductService productService,
                            ReturnAmountByProductService returnAmountByProductService,
                            EmployeeService employeeService,
                            PositionService positionService,
                            RetailStoreService retailStoreService) {


        this.invoiceProductService = invoiceProductService;
        this.issuedInvoiceService = issuedInvoiceService;
        this.paymentService = paymentService;
        this.salesSubCustomersOrdersView = salesSubCustomersOrdersView;
        this.invoiceService = invoiceService;
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.buyersReturnService = buyersReturnService;
        this.salesSubShipmentView = salesSubShipmentView;
        this.salesSubInvoicesToBuyersView = salesSubInvoicesToBuyersView;
        this.commissionAgentReportModalView = commissionAgentReportModalView;
        this.returnBuyersReturnModalView = returnBuyersReturnModalView;
        this.notifications = notifications;
        this.productService = productService;
        this.returnAmountByProductService = returnAmountByProductService;
        this.employeeService = employeeService;
        this.positionService = positionService;
        this.retailStoreService = retailStoreService;

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
            div.add(salesSubCustomersOrdersView);
        });
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
                    div.add(new SalesSubAgentReportsView(invoiceService, contractorService, companyService, warehouseService, commissionAgentReportModalView, notifications));
                    break;
                case "Возвраты покупателей":
                    div.removeAll();
                    div.add(new SalesSubBuyersReturnsView(buyersReturnService, contractorService, companyService, returnBuyersReturnModalView, warehouseService, notifications));
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
                    div.add("SalesSubGoodsForSaleView");
                    break;
                case "Воронка продаж":
                    div.removeAll();
                    div.add("SalesSubSalesFunnelView");
                    break;
            }
        });
        return tabs;
    }
}

