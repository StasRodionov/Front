package com.trade_accounting.components.production;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.DepartmentService;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.OrdersOfProductionService;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.StagesProductionService;
import com.trade_accounting.services.interfaces.TechnicalCardGroupService;
import com.trade_accounting.services.interfaces.TechnicalCardProductionService;
import com.trade_accounting.services.interfaces.TechnicalCardService;
import com.trade_accounting.services.interfaces.TechnicalOperationsService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "production", layout = AppView.class)
@PageTitle("Производство")
public class ProductionSubMenuView extends Div implements AfterNavigationObserver {

    private final Div div;
    private final TechnicalCardService technicalCardService;
    private final TechnicalCardGroupService technicalCardGroupService;
    private final ProductService productService;
    private final TechnicalCardProductionService technicalCardProductionService;
    private final Notifications notifications;
    private final TechnicalOperationsService technicalOperationsService;
    private final WarehouseService warehouseService;
    private final OrdersOfProductionService ordersOfProductionService;
    private final CompanyService companyService;
    private final TechnologicalOperationsModalView view;
    private final StagesProductionService stagesProductionService;
    private final StageProductionModalView stageProductionModalView;
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;


    @Autowired
    public ProductionSubMenuView(TechnicalCardService technicalCardService,
                                 TechnicalCardGroupService technicalCardGroupService,
                                 ProductService productService,
                                 TechnicalCardProductionService technicalCardProductionService,
                                 Notifications notifications,
                                 TechnicalOperationsService technicalOperationsService,
                                 WarehouseService warehouseService,
                                 OrdersOfProductionService ordersOfProductionService,
                                 CompanyService companyService,
                                 TechnologicalOperationsModalView view,
                                 StagesProductionService stagesProductionService,
                                 StageProductionModalView stageProductionModalView, DepartmentService departmentService, EmployeeService employeeService) {
        this.notifications = notifications;
        this.technicalOperationsService = technicalOperationsService;
        this.warehouseService = warehouseService;
        this.ordersOfProductionService = ordersOfProductionService;
        this.companyService = companyService;
        this.view = view;
        this.stagesProductionService = stagesProductionService;
        this.stageProductionModalView = stageProductionModalView;
        this.departmentService = departmentService;
        this.employeeService = employeeService;
        div = new Div();
        add(configurationSubMenu(), div);
        this.technicalCardService = technicalCardService;
        this.technicalCardGroupService = technicalCardGroupService;
        this.productService = productService;
        this.technicalCardProductionService = technicalCardProductionService;
    }

    private Tabs configurationSubMenu() {
        Tabs tabs = new Tabs(
                new Tab("Тех. карты"),
                new Tab("Заказы на производство"),
                new Tab("Тех. операции"),
                new Tab("Произведственные Задания"),
                new Tab("Тех. процессы"),
                new Tab("Этапы")

        );
        tabs.addSelectedChangeListener(event -> {
            String name = event.getSelectedTab().getLabel();

            switch (name) {
                case "Тех. карты":
                    div.removeAll();
                    div.add(new FlowchartsViewTab(technicalCardService, technicalCardGroupService, productService,
                            technicalCardProductionService, notifications));
                    break;
                case "Заказы на производство":
                    div.removeAll();
                    div.add(new OrdersOfProductionViewTab(ordersOfProductionService, companyService, technicalCardService,
                            notifications));
                    break;
                case "Тех. операции":
                    div.removeAll();
                    div.add(new TechnologicalOperationsViewTab(technicalCardService, technicalOperationsService,
                            notifications, warehouseService, view));
                    break;
                case "Этапы":
                    div.removeAll();
                    div.add(new StageProductionViewTab(stagesProductionService, notifications, stageProductionModalView, departmentService, employeeService));
                    break;
            }
        });
        return tabs;

    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        div.removeAll();
        div.add(new FlowchartsViewTab(technicalCardService, technicalCardGroupService, productService, technicalCardProductionService, notifications));
        //div.add(new OrdersOfProductionViewTab(ordersOfProductionService, companyService,technicalCardService, notifications, modalWindow));
    }
}

