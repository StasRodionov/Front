package com.trade_accounting.components.profile;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.services.interfaces.client.DepartmentService;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.client.PositionService;
import com.trade_accounting.services.interfaces.client.RoleService;
import com.trade_accounting.services.interfaces.company.AddressService;
import com.trade_accounting.services.interfaces.company.BankAccountService;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.LegalDetailService;
import com.trade_accounting.services.interfaces.company.TypeOfContractorService;
import com.trade_accounting.services.interfaces.units.CurrencyService;
import com.trade_accounting.services.interfaces.units.SalesChannelService;
import com.trade_accounting.services.interfaces.units.UnitService;
import com.trade_accounting.services.interfaces.util.ImageService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import static com.trade_accounting.config.SecurityConstants.PROFILE;

@Route(value = PROFILE, layout = AppView.class)
@PageTitle("Профиль")
public class ProfileView extends Div implements AfterNavigationObserver {

    private final Notifications notifications;
    private final UnitService unitService;
    private final CompanyService companyService;
    private final AddressService addressService;
    private final LegalDetailService legalDetailService;
    private final EmployeeService employeeService;
    private final WarehouseService warehouseService;
    private final CurrencyService currencyService;
    private final RoleService roleService;
    private final ImageService imageService;
    private final Div div;
    private final Div divMenu;
    private final TypeOfContractorService typeOfContractorService;
    private final BankAccountService bankAccountService;
    private final DepartmentService departmentService;
    private final PositionService positionService;

    private final SalesChannelService salesChannelService;

    public ProfileView(Notifications notifications, UnitService unitService, CompanyService companyService,
                       AddressService addressService, LegalDetailService legalDetailService, EmployeeService employeeService, WarehouseService warehouseService,
                       CurrencyService currencyService, RoleService roleService, ImageService imageService, TypeOfContractorService typeOfContractorService,
                       BankAccountService bankAccountService, DepartmentService departmentService, PositionService positionService, SalesChannelService salesChannelService) {
        this.notifications = notifications;
        this.unitService = unitService;
        this.companyService = companyService;
        this.addressService = addressService;
        this.legalDetailService = legalDetailService;
        this.employeeService = employeeService;
        this.warehouseService = warehouseService;
        this.currencyService = currencyService;
        this.roleService = roleService;
        this.imageService = imageService;
        this.typeOfContractorService = typeOfContractorService;
        this.bankAccountService = bankAccountService;
        this.departmentService = departmentService;
        this.positionService = positionService;
        this.salesChannelService = salesChannelService;
        div = new Div();
        divMenu = new Div();
        add(divMenu, div);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        AppView appView = (AppView) afterNavigationEvent.getActiveChain().get(1);
        appView.getChildren().forEach(e -> {
            if (e.getClass() == Tabs.class) {
                ((Tabs) e).setSelectedIndex(12);
            }
        });
        getUI().ifPresent(ui -> {
            divMenu.removeAll();
//            divMenu.add(configurationSubMenu());
            div.removeAll();
//            div.add(new CompanyView(notifications, companyService, addressService, legalDetailService, typeOfContractorService, bankAccountService));
        });
    }

    private Tabs configurationSubMenu() {
        Tabs tabs = new Tabs(
                new Tab("Юр. лица"),
                new Tab("Сотрудники"),
                new Tab("Склады"),
                new Tab("Валюты"),
                new Tab("Единицы измерения"),
                new Tab("Каналы продаж")
        );
        tabs.addSelectedChangeListener(event -> {
            String tabName = event.getSelectedTab().getLabel();
            switch (tabName) {
                case "Юр. лица":
                    div.removeAll();
                    div.add(new CompanyView(notifications, companyService, addressService, legalDetailService, typeOfContractorService, bankAccountService));
                    break;
                case "Сотрудники":
                    div.removeAll();
                    div.add(new EmployeeView(employeeService, roleService, imageService, notifications, departmentService, positionService));
                    break;
                case "Склады":
                    div.removeAll();
                    div.add(new WareHouseView(warehouseService));
                    break;
                case "Валюты":
                    div.removeAll();
                    div.add(new CurrencyView(currencyService));
                    break;
                case "Единицы измерения":
                    div.removeAll();
                    div.add(new UnitView(unitService));
                    break;
                case "Каналы продаж":
                    div.removeAll();
                    div.add(new SalesChannelView(salesChannelService, notifications));
                default:
            }
        });
        return tabs;
    }
}
