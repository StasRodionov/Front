package com.trade_accounting.components.retail;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.services.interfaces.*;
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

@Route(value = "retail", layout = AppView.class)
@PageTitle("Розница")
@SpringComponent
@UIScope
public class RetailView extends Div implements AfterNavigationObserver {

    private final Div div;
    private final RetailOperationWithPointsService retailOperationWithPointsService;
    private final RetailPointsService retailPointsService;
    private final BonusProgramService bonusProgramService;
    private final TaskService taskService;
    private final ContractorService contractorService;
    private final RetailStoreService retailStoreService;
    private final RetailSalesService retailSalesService;
    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final PayoutService payoutService;
    private final Notifications notifications;
    private final RetailReturnsService retailReturnsService;
    private final ContractorGroupService contractorGroupService;
    private final RetailShiftService retailShiftService;
    private final RetailMakingService retailMakingService;
    private final RetailCloudCheckService retailCloudCheckService;
    private final CurrencyService currencyService;
    private final PrepayoutService prepayoutService;
    private final PrepaymentReturnService prepaymentReturnService;
    private final WarehouseService warehouseService;
    private final PositionService positionService;
    private final BankAccountService bankAccountService;

    @Autowired
    public RetailView(RetailOperationWithPointsService retailOperationWithPointsService, BonusProgramService bonusProgramService, TaskService taskService, ContractorService contractorService, RetailStoreService retailStoreService, RetailSalesService retailSalesService,
                      CompanyService companyService, EmployeeService employeeService,
                      PayoutService payoutService, Notifications notifications, RetailReturnsService retailReturnsService, ContractorGroupService contractorGroupService,
                      RetailPointsService retailPointsService, RetailShiftService retailShiftService, RetailMakingService retailMakingService, RetailCloudCheckService retailCloudCheckService,
                      CurrencyService currencyService, PrepayoutService prepayoutService, PrepaymentReturnService prepaymentReturnService, WarehouseService warehouseService, PositionService positionService,
                      BankAccountService bankAccountService) {
        this.retailOperationWithPointsService = retailOperationWithPointsService;
        this.bonusProgramService = bonusProgramService;
        this.taskService = taskService;
        this.contractorService = contractorService;
        this.retailStoreService = retailStoreService;
        this.retailSalesService = retailSalesService;
        this.retailPointsService = retailPointsService;
        this.companyService = companyService;
        this.employeeService = employeeService;
        this.payoutService = payoutService;
        this.notifications = notifications;
        this.retailReturnsService = retailReturnsService;
        this.contractorGroupService = contractorGroupService;
        this.retailShiftService = retailShiftService;
        this.retailMakingService = retailMakingService;
        this.retailCloudCheckService = retailCloudCheckService;
        this.currencyService = currencyService;
        this.prepayoutService = prepayoutService;
        this.prepaymentReturnService = prepaymentReturnService;
        this.warehouseService = warehouseService;
        this.positionService = positionService;
        this.bankAccountService = bankAccountService;
        div = new Div();
        add(configurationSubMenu(), div);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        div.removeAll();
        div.add(new RetailStoresTabView(retailStoreService, companyService, employeeService, positionService));

        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(6);
            }
        });
    }

    private Tabs configurationSubMenu() {

        Tabs tabs = new Tabs(
                new Tab("Точки продаж"),
                new Tab("Смены"),
                new Tab("Продажи"),
                new Tab("Возвраты"),
                new Tab("Внесения"),
                new Tab("Выплаты"),
                new Tab("Операции с баллами"),
                new Tab("Предоплаты"),
                new Tab("Возвраты предоплат"),
                new Tab("Очередь облачных чеков"),
                new Tab("Бонусные программы")
        );

        tabs.addSelectedChangeListener(event -> {
            String tabName = event.getSelectedTab().getLabel();
            switch (tabName) {
                case "Точки продаж":
                    div.removeAll();
                    div.add(new RetailStoresTabView(retailStoreService, companyService, employeeService, positionService));
                    break;
                case "Смены":
                    div.removeAll();
                    div.add(new RetailShiftView(retailShiftService, retailStoreService, warehouseService, companyService, bankAccountService));
                    break;
                case "Продажи":
                    div.removeAll();
                    div.add(new RetailSalesTabView(retailSalesService,retailStoreService,companyService, contractorService));
                    break;
                case "Возвраты":
                    div.removeAll();
                    div.add(new RetailReturnsView(retailReturnsService));
                    break;
                case "Внесения":
                    div.removeAll();
                    div.add(new RetailMakingView(retailMakingService, retailStoreService, companyService, notifications));
                    break;
                case "Выплаты":
                    div.removeAll();
                    div.add(new PayoutTabView(payoutService, retailStoreService, companyService, employeeService, notifications));
                    break;
                case "Операции с баллами":
                    div.removeAll();
                    div.add(new RetailPointsView(retailPointsService, bonusProgramService, contractorService, taskService));

                    break;
                case "Предоплаты":
                    div.removeAll();
                    div.add(new PrepayoutView(prepayoutService));
                    break;
                case "Возвраты предоплат":
                    div.removeAll();
                    div.add(new PrepaymentReturnView(prepaymentReturnService, contractorService, retailStoreService, companyService));
                    break;
                case "Очередь облачных чеков":
                    div.removeAll();
                    div.add(new RetailCloudCheckView(retailCloudCheckService,currencyService,retailStoreService,employeeService));
                    break;
                case "Бонусные программы":
                    div.removeAll();
                    div.add(new BonusProgramTabView(bonusProgramService, contractorGroupService));
                    break;
            }
        });

        return tabs;
    }
}
