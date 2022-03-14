package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.finance.ReturnAmountByProductDto;
import com.trade_accounting.models.dto.warehouse.BuyersReturnDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.client.EmployeeDto;
import com.trade_accounting.models.dto.invoice.InvoiceDto;
import com.trade_accounting.models.dto.invoice.InvoiceProductDto;
import com.trade_accounting.models.dto.warehouse.ProductDto;
import com.trade_accounting.models.dto.retail.RetailStoreDto;
import com.trade_accounting.services.interfaces.warehouse.BuyersReturnService;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.invoice.InvoiceProductService;
import com.trade_accounting.services.interfaces.invoice.InvoiceService;
import com.trade_accounting.services.interfaces.client.PositionService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.trade_accounting.services.interfaces.retail.RetailStoreService;
import com.trade_accounting.services.interfaces.finance.ReturnAmountByProductService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Route(value = "profitability", layout = AppView.class)
@PageTitle("Прибыльность")
@SpringComponent
@UIScope
public class SalesSubProfitabilityView extends VerticalLayout {
    private final CompanyService companyService;
    private final ContractorService contractorService;
    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final ProductService productService;
    private final BuyersReturnService buyersReturnService;
    private final ReturnAmountByProductService returnAmountByProductService;
    private final EmployeeService employeeService;
    private final PositionService positionService;
    private final RetailStoreService retailStoreService;
    private final MenuItem print;
    private final MenuBar selectXlsTemplateButton = new MenuBar();

    private Grid<ContractorDto> gridCostumers = new Grid<>(ContractorDto.class, false);
    private Grid<InvoiceProductDto> gridProducts = new Grid<>(InvoiceProductDto.class, false);
    private Grid<EmployeeDto> gridEmployees = new Grid<>(EmployeeDto.class, false);

    private GridPaginator<EmployeeDto> paginatorEmployees;
    private GridPaginator<ContractorDto> paginatorCustomers;
    private GridPaginator<InvoiceProductDto> paginatorProducts;

    private List<ContractorDto> contractorDtos;
    private List<InvoiceProductDto> invoiceProductDtos;
    private List<ProductDto> productDtos;
    private List<InvoiceDto> invoiceDtos;
    private List<EmployeeDto> employeeDtos;
    private List<RetailStoreDto> retailStoreDtos;

    private final GridFilter<EmployeeDto> employeeFilter;
    private final GridFilter<InvoiceProductDto> productsFilter;
    private final GridFilter<ContractorDto> costumersFilter;

    private final String pathForSaveXlsTemplate =
            "src/main/resources/xls_templates/profitability_templates/";

    public SalesSubProfitabilityView(InvoiceService invoiceService, CompanyService companyService,
                                     ContractorService contractorService,
                                     InvoiceProductService invoiceProductService,
                                     ProductService productService,
                                     BuyersReturnService buyersReturnService,
                                     ReturnAmountByProductService returnAmountByProductService,
                                     EmployeeService employeeService,
                                     PositionService positionService,
                                     RetailStoreService retailStoreService) {
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.productService = productService;
        this.buyersReturnService = buyersReturnService;
        this.returnAmountByProductService = returnAmountByProductService;
        this.employeeService = employeeService;
        this.positionService = positionService;
        this.retailStoreService = retailStoreService;
        print = selectXlsTemplateButton.addItem("Печать");

        this.contractorDtos = getContractorDtos();
        this.invoiceProductDtos = getInvoiceProductDtos();
        this.productDtos = getProductDtos();
        this.invoiceDtos = getInvoiceDtos();
        this.employeeDtos = getEmployeeDtos();
        this.retailStoreDtos = getRetailSoresDtos();

        paginatorCustomers = new GridPaginator<>(gridCostumers, contractorDtos, 50);
        paginatorProducts = new GridPaginator<>(gridProducts, invoiceProductDtos, 50);
        paginatorEmployees = new GridPaginator<>(gridEmployees, employeeDtos, 50);

        setHorizontalComponentAlignment(Alignment.CENTER, paginatorCustomers);
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorProducts);
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorEmployees);

        add(upperLayout());

        configureCostumersGrid(false);
        configureEmployeeGrid(false);
        configureProductsGrid(false);

        this.costumersFilter = new GridFilter<>(gridCostumers);
        this.productsFilter = new GridFilter<>(gridProducts);
        this.employeeFilter = new GridFilter<>(gridEmployees);

        configureProductFilter();
        configureEmployeeFilter();
        configureCostumerFilter();

        add(productsFilter, costumersFilter, employeeFilter,
                gridProducts, paginatorProducts,
                gridCostumers, paginatorCustomers,
                gridEmployees, paginatorEmployees);

        getCashiersIdsRevenues();
        configureSelectXlsTemplateButton();
    }

    private void configureSelectXlsTemplateButton() {
        SubMenu printSubMenu = print.getSubMenu();
        printSubMenu.removeAll();
        templatesXlsMenuItems(printSubMenu);

    }

    private List<File> getXlsFiles() {
        File dir = new File(pathForSaveXlsTemplate);
        return Arrays.stream(Objects.requireNonNull(dir.listFiles())).filter(File::isFile).filter(x -> x.getName()
                .contains(".xls")).collect(Collectors.toList());
    }


    private void templatesXlsMenuItems(SubMenu subMenu) {
        getXlsFiles().forEach(x -> subMenu.addItem(getLinkToXlsTemplate(x)));
    }

    private Anchor getLinkToXlsTemplate(File file) {
        String templateName = file.getName();
        cashiersIdsTotalRevenues = getCashiersIdsTotalRevenues();
        if (templateName.contains("product")) {
            PrintSalesSubProfitabilityByProduct printSalesSubProfitabilityByProduct =
                    new PrintSalesSubProfitabilityByProduct(file.getPath(), invoiceProductDtos,
                            productService, returnAmountByProductService);
            return new Anchor(new StreamResource(templateName, printSalesSubProfitabilityByProduct::createReport), templateName);
        } else if (templateName.contains("employee")) {
            PrintSalesSubProfitabilityByEmpolyee printSalesSubProfitabilityByEmpolyee =
                    new PrintSalesSubProfitabilityByEmpolyee(file.getPath(), employeeDtos, contractorService,
                            positionService, retailStoreService, cashiersIdsTotalRevenues);
            return new Anchor(new StreamResource(templateName, printSalesSubProfitabilityByEmpolyee::createReport), templateName);

        } else if (templateName.contains("costumers")) {

            PrintSalesSubProfitabilityByCostumers printSalesSubProfitabilityByCostumers =
                    new PrintSalesSubProfitabilityByCostumers(file.getPath(), contractorDtos, contractorService,
                            invoiceDtos, invoiceProductDtos, productDtos, buyersReturnService);
            return new Anchor(new StreamResource(templateName, printSalesSubProfitabilityByCostumers::createReport), templateName);
        }
        return null;
    }

    private void configureEmployeeGrid(boolean refreshing) {
        gridEmployees.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        gridEmployees.setItems(employeeDtos);

        gridEmployees.addColumn(iDto -> employeeDtos.get(iDto.getId().intValue() - 1))
                .setHeader("Работник")
                .setKey("employeeDto")
                .setId("Работник");

        gridEmployees.addColumn(iDto ->
                        positionService.getById(employeeDtos.get(iDto.getId().intValue() - 1).getPositionDtoId()).getName())
                .setHeader("Должность")
                .setKey("position")
                .setId("Должность");

        gridEmployees.addColumn(iDto -> averagePrice(iDto.getId()))
                .setHeader("Средний чек")
                .setKey("average")
                .setId("Средний чек");

        gridEmployees.addColumn(iDto -> cashiersIdsTotalRevenues.get(iDto.getId()))
                .setHeader("Прибыль")
                .setKey("profit")
                .setId("Прибыль");

        gridEmployees.setHeight("100vh");
        gridEmployees.setColumnReorderingAllowed(true);
        if (!refreshing) {
            gridEmployees.setSelectionMode(Grid.SelectionMode.MULTI);
        }
    }

    private List<BigDecimal> revs = new ArrayList<>();
    private List<List<Long>> ids = new ArrayList<>();
    private Map<Long, BigDecimal> cashiersIdsTotalRevenues;

    public Map<Long, BigDecimal> getCashiersIdsTotalRevenues() {
        return cashiersIdsTotalRevenues;
    }

    private void getCashiersIdsRevenues() {
        for (RetailStoreDto rs : retailStoreDtos) {
            revs.add(rs.getRevenue());
            ids.add(rs.getCashiersIds());
        }
        Map<Long, BigDecimal> result = new HashMap<>();
        for (int i = 1; i <= revs.size(); i++) {
            if (ids.get(i - 1).size() > 1) {
                List<Long> list = new ArrayList<>(ids.get(i - 1));
                for (int a = 0; a < list.size(); a++) {
                    if (result.containsKey(list.get(a))) {
                        result.put(list.get(a), revs.get(i - 1).divide(new BigDecimal(list.size())).add(result.get(list.get(a))));
                    } else {
                        result.put(list.get(a), revs.get(i - 1).divide(new BigDecimal(list.size())));
                    }
                }
            } else {
                if (result.containsKey(ids.get(i - 1).get(0))) {
                    result.put(ids.get(i - 1).get(0), revs.get(i - 1).add(result.get(ids.get(i - 1).get(0))));
                } else {
                    result.put(ids.get(i - 1).get(0), revs.get(i - 1));
                }
            }
        }
        cashiersIdsTotalRevenues = result;
    }

    private void configureProductsGrid(boolean refreshing) {
        gridProducts.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        gridProducts.setItems(invoiceProductDtos);

        gridProducts.addColumn(iDto -> productDtos.get(invoiceProductDtos.get(iDto.getId().intValue() - 1).getProductId().intValue() - 1).getName())
                .setHeader("Продукт")
                .setKey("productDto")
                .setId("Продукт");

        gridProducts.addColumn(iDto -> productDtos.get(invoiceProductDtos.get(iDto.getId().intValue() - 1).getProductId().intValue() - 1).getDescription())
                .setHeader("Арктикул")
                .setKey("description")
                .setId("Арктикул");

        gridProducts.addColumn(iDto -> invoiceProductDtos.get(iDto.getId().intValue() - 1).getAmount())
                .setHeader("Количество")
                .setKey("amount")
                .setId("Количество");

        gridProducts.addColumn(iDto -> invoiceProductDtos.get(iDto.getId().intValue() - 1).getPrice())
                .setHeader("Цена")
                .setKey("price")
                .setId("Цена");

        gridProducts.addColumn(iDto -> productDtos.get(invoiceProductDtos.get(iDto.getId().intValue() - 1).getProductId().intValue() - 1).getPurchasePrice())
                .setHeader("Себестоимость")
                .setKey("costPrice")
                .setId("Себестоимость");

        gridProducts.addColumn(iDto -> getTotalPriceForProducts(iDto.getId()))
                .setHeader("Сумма продаж")
                .setKey("totalSalesPrice")
                .setId("Сумма продаж");

        gridProducts.addColumn(iDto -> getTotalCostPriceForProducts(iDto.getId()))
                .setHeader("Сумма себестоимости")
                .setKey("totalCostPrice")
                .setId("Сумма себестоимости");

//        gridProducts.addColumn(iDto -> getTotalReturnPriceForProducts(iDto.getProductId(), iDto.getInvoiceId()))
//                .setHeader("Сумма возвратов")
//                .setKey("totalReturnPrice")
//                .setSortable(true)
//                .setId("Сумма возвратов");
//
        gridProducts.addColumn(iDto -> getTotalReturnPriceForProducts(iDto.getAmount()))
                .setHeader("Сумма возвратов")
                .setKey("totalReturnPrice")
                .setSortable(true)
                .setId("Сумма возвратов");

        gridProducts.addColumn(iDto -> getProfitByProducts(iDto.getId(), iDto.getAmount()))
                .setHeader("Прибыль")
                .setKey("profit")
                .setSortable(true)
                .setId("Прибыль");

        gridProducts.setHeight("100vh");
        gridProducts.setColumnReorderingAllowed(true);
        if (!refreshing) {
            gridProducts.setSelectionMode(Grid.SelectionMode.MULTI);
        }
    }

    private void configureCostumersGrid(boolean refreshing) {
        gridCostumers.setItems(contractorDtos);

        gridCostumers.addColumn(iDto -> contractorService.getById(iDto.getId()).getName())
                .setHeader("Контрагент")
                .setKey("name")
                .setId("Контрагент");

        gridCostumers.addColumn(iDto -> getTotalPrice(iDto.getId()))
                .setHeader("Сумма продаж")
                .setKey("salesTotal")
                .setId("Cумма продаж");

        gridCostumers.addColumn(iDto -> getTotalCostPrice(iDto.getId()))
                .setHeader("Сумма себестоимости")
                .setKey("costTotal")
                .setId("Сумма себестоимости");

        gridCostumers.addColumn(iDto -> averagePrice(iDto.getId()))
                .setHeader("Средний чек")
                .setKey("average")
                .setId("Средний чек");

        gridCostumers.addColumn(iDto -> getReturnsTotalPrice(iDto.getId()))
                .setHeader("Сумма возврата")
                .setKey("returnTotal")
                .setId("Сумма возврата");

        gridCostumers.addColumn(iDto -> getProfit(iDto.getId()))
                .setHeader("Прибыль")
                .setKey("profit")
                .setId("Прибыль");

        gridCostumers.setHeight("100vh");
        gridCostumers.setColumnReorderingAllowed(true);
        if (!refreshing) {
            gridCostumers.setSelectionMode(Grid.SelectionMode.MULTI);
        }
    }

    private void configureEmployeeFilter() {
        employeeFilter.onClearClick(e -> {
            refreshData();
            reloadTabs();
        });
        employeeFilter.onSearchClick(e -> {
            Map<String, String> map = employeeFilter.getFilterData();
            List<EmployeeDto> list = employeeService.search(map);
            list = additionalEmployeesFiltering(list, map);
            paginatorEmployees.setData(list);
            paginatorEmployees.reloadGrid();
        });
    }

    private void configureCostumerFilter() {
        costumersFilter.onClearClick(e -> {
            refreshData();
            reloadTabs();
        });
        costumersFilter.onSearchClick(e -> {
            Map<String, String> map = costumersFilter.getFilterData();
            List<ContractorDto> list = contractorService.searchContractor(map);
            list = additionalCostumersFiltering(list, map);
            paginatorCustomers.setData(list);
            paginatorCustomers.reloadGrid();
        });
    }

    private void configureProductFilter() {
        productsFilter.onClearClick(e -> {
            refreshData();
            reloadTabs();
        });
        productsFilter.onSearchClick(e -> {
            int i = 1;
            Map<String, String> map = productsFilter.getFilterData();
            this.invoiceProductDtos = invoiceProductService.search(map);
            for (InvoiceProductDto ip : invoiceProductDtos) {
                ip.setId((long) i++);
            }
            this.invoiceProductDtos = additionalProductsFiltering(invoiceProductDtos, map);
            i = 1;
            for (InvoiceProductDto ip : invoiceProductDtos) {
                ip.setId((long) i++);
            }
            reloadTabs();
        });
    }

    private List<EmployeeDto> additionalEmployeesFiltering(List<EmployeeDto> inputList, Map<String, String> query) {
        List<EmployeeDto> result = inputList;
        if (query.containsKey("employeeDto")) {
            result = result.stream()
                    .filter(e -> e.toString().equals(query.get("employeeDto")))
                    .collect(Collectors.toList());
        }
        if (query.containsKey("profit")) {
            result = result.stream()
                    .filter(e -> {
                        if (cashiersIdsTotalRevenues.get(e.getId()) != null) {
                            if (cashiersIdsTotalRevenues.get(e.getId()).equals(new BigDecimal(query.get("profit")))) {
                                return true;
                            } else return false;
                        } else return false;
                    })
                    .collect(Collectors.toList());
        }
        if (query.containsKey("average")) {
            result = result.stream()
                    .filter(e -> averagePrice(e.getId()).equals(query.get("average")))
                    .collect(Collectors.toList());
        }
        return result;
    }

    private List<InvoiceProductDto> additionalProductsFiltering(List<InvoiceProductDto> inputList, Map<String, String> query) {
        List<InvoiceProductDto> result = inputList;
        if (query.containsKey("profit")) {
            result = result.stream()
                    .filter(e -> getProfitByProducts(e.getId(), e.getAmount()).equals(query.get("profit")))
                    .collect(Collectors.toList());
        }
        if (query.containsKey("totalReturnPrice")) {
            result = result.stream()
                    .filter(e -> getTotalReturnPriceForProducts(e.getAmount()).equals(query.get("totalReturnPrice")))
                    .collect(Collectors.toList());
        }
        if (query.containsKey("totalCostPrice")) {
            result = result.stream()
                    .filter(e -> getTotalCostPriceForProducts(e.getId()).equals(query.get("totalCostPrice")))
                    .collect(Collectors.toList());
        }
        if (query.containsKey("totalSalesPrice")) {
            result = result.stream()
                    .filter(e -> getTotalPriceForProducts(e.getId()).equals(query.get("totalSalesPrice")))
                    .collect(Collectors.toList());
        }
        return result;
    }

    private List<ContractorDto> additionalCostumersFiltering(List<ContractorDto> inputList, Map<String, String> query) {
        List<ContractorDto> result = inputList;
        if (query.containsKey("average")) {
            result = result.stream()
                    .filter(e -> averagePrice(e.getId()).equals(query.get("average")))
                    .collect(Collectors.toList());
        }
        if (query.containsKey("returnTotal")) {
            result = result.stream()
                    .filter(e -> getReturnsTotalPrice(e.getId()).equals(query.get("returnTotal")))
                    .collect(Collectors.toList());
        }
        if (query.containsKey("salesTotal")) {
            result = result.stream()
                    .filter(e -> getTotalPrice(e.getId()).equals(query.get("salesTotal")))
                    .collect(Collectors.toList());
        }
        if (query.containsKey("costTotal")) {
            result = result.stream()
                    .filter(e -> getTotalCostPrice(e.getId()).equals(query.get("costTotal")))
                    .collect(Collectors.toList());
        }
        if (query.containsKey("profit")) {
            result = result.stream()
                    .filter(e -> getProfit(e.getId()).equals(query.get("profit")))
                    .collect(Collectors.toList());
        }
        return result;
    }

    private void reloadTabs() {
        if ("По товарам".equals(currentTab)) {
            fillByContractors();
        } else if ("По сотрудникам".equals(currentTab)) {
            fillByEmployees();
        } else if ("По покупателям".equals(currentTab)) {
            fillByCustomers();
        }
    }

    private String currentTab = "По товарам";

    //Загрузчики данных
    private List<ContractorDto> getContractorDtos() {
        return contractorService.getAll();
    }

    private List<InvoiceProductDto> getInvoiceProductDtos() {
        return invoiceProductService.getAll();
    }

    private List<ProductDto> getProductDtos() {
        return productService.getAll();
    }

    private List<InvoiceDto> getInvoiceDtos() {
        return invoiceService.getAll();
    }

    private List<EmployeeDto> getEmployeeDtos() {
        return employeeService.getAll();
    }

    private List<RetailStoreDto> getRetailSoresDtos() {
        return retailStoreService.getAll();
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), configurationSubMenu(), buttonFilter(), selectXlsTemplateButton, buttonGraph());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("В разделе представлена прибыль от реализации товаров и услуг. " +
                        "Здесь отображаются товары и услуги из документов розничной продажи и отгрузок за указанный период. " +
                        "Если период не задать, будет показана прибыльность за последний месяц. " +
                        "Отчет позволяет оценить рентабельность продукции. " +
                        "Он может быть сформирован по товарам, сотрудникам и покупателям. " +
                        "<p>Отчет по сотрудникам позволяет отслеживать работу каждого конкретного сотрудника и рассчитывать вознаграждение по результатам продаж. " +
                        "<p>По каждому товару можно увидеть список отгрузок, розничных продаж и возвратов. " +
                        "Продажи в минус выделяются красным. " +
                        "Читать инструкцию: "),
                new Anchor("#", "Прибыльность"));
    }

    private void refreshData() {
        this.invoiceDtos = getInvoiceDtos();
        this.productDtos = getProductDtos();
        this.contractorDtos = getContractorDtos();
        this.invoiceProductDtos = getInvoiceProductDtos();
        this.employeeDtos = getEmployeeDtos();
        this.retailStoreDtos = getRetailSoresDtos();
    }

    private void fillByContractors() {
        remove(gridCostumers, paginatorCustomers, gridEmployees, paginatorEmployees);
        add(gridProducts, paginatorProducts);
        gridProducts.removeAllColumns();
        configureProductsGrid(false);
        currentTab = "По товарам";
    }

    private void fillByEmployees() {
        remove(gridCostumers, paginatorCustomers, gridProducts, paginatorProducts);
        add(gridEmployees, paginatorEmployees);
        gridEmployees.removeAllColumns();
        configureEmployeeGrid(false);
        currentTab = "По сотрудникам";
    }

    private void fillByCustomers() {
        remove(gridProducts, paginatorProducts, gridEmployees, paginatorEmployees);
        add(gridCostumers, paginatorCustomers);
        gridCostumers.removeAllColumns();
        configureCostumersGrid(false);
        currentTab = "По покупателям";
    }

    private Tabs configurationSubMenu() {
        Tab contractors = new Tab("По товарам");
        Tab employees = new Tab("По сотрудникам");
        Tab customer = new Tab("По покупателям");
        Tabs tabs = new Tabs(contractors, employees, customer);

        tabs.addSelectedChangeListener(event -> {
            currentTab = event.getSelectedTab().getLabel();
            productsFilter.setVisible(false);
            costumersFilter.setVisible(false);
            employeeFilter.setVisible(false);
            refreshData();
            reloadTabs();
        });
        return tabs;
    }

    private String getProfitByProducts(Long id, BigDecimal amount) {
        BigDecimal profit;

        String buyerBuying = getTotalPriceForProducts(id);
        buyerBuying = buyerBuying.replace(',', '.');
        String buyersReturn = getTotalReturnPriceForProducts(amount);
        buyersReturn = buyersReturn.replace(',', '.');
        String costPrice = getTotalCostPriceForProducts(id);
        costPrice = costPrice.replace(',', '.');

        profit = new BigDecimal(buyerBuying).subtract(new BigDecimal(costPrice)).subtract(new BigDecimal(buyersReturn));
        return String.format("%.2f", profit);
    }

//    private String getTotalReturnPriceForProducts(Long productId, Long invoiceId) {
//        return String.format("%.2f", returnAmountByProductService
//                .getTotalReturnAmountByProduct(productId, invoiceId)
//                .getAmount());
//    }

    private String getTotalReturnPriceForProducts(BigDecimal amount) {

        BigDecimal totalPrice = BigDecimal.valueOf(0.0);

        List<ReturnAmountByProductDto> list = returnAmountByProductService.getByAmount(amount);

        for (ReturnAmountByProductDto dto : list) {
            totalPrice = totalPrice.add(dto.getAmount());
        }
        return String.format("%.2f", totalPrice);
    }

//    private String getReturnsTotalPrice(Long id) {
//        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
//
//        List<BuyersReturnDto> list = buyersReturnService.getByContractorId(id);
//
//        for (BuyersReturnDto dto : list) {
//            totalPrice = totalPrice.add(dto.getSum());
//        }
//        return String.format("%.2f", totalPrice);
//    }

    private String getTotalCostPriceForProducts(Long id) {
        BigDecimal totalPrice;

        InvoiceProductDto invoiceProductDto = invoiceProductDtos.get(id.intValue() - 1);
        totalPrice = productDtos.get(invoiceProductDto.getProductId().intValue() - 1).getPurchasePrice().multiply(invoiceProductDto.getAmount());

        return String.format("%.2f", totalPrice);
    }

    private String getTotalPriceForProducts(Long id) {
        BigDecimal totalPrice;

        InvoiceProductDto invoiceProductDto = invoiceProductDtos.get(id.intValue() - 1);
        totalPrice = invoiceProductDto.getPrice().multiply(invoiceProductDto.getAmount());

        return String.format("%.2f", totalPrice);
    }

    private String getProfit(Long id) {
        BigDecimal profit;

        String buyerBuying = getTotalPrice(id);
        buyerBuying = buyerBuying.replace(',', '.');
        String buyersReturn = getReturnsTotalPrice(id);
        buyersReturn = buyersReturn.replace(',', '.');
        String costPrice = getTotalCostPrice(id);
        costPrice = costPrice.replace(',', '.');

        profit = new BigDecimal(buyerBuying).subtract(new BigDecimal(costPrice)).subtract(new BigDecimal(buyersReturn));
        return String.format("%.2f", profit);
    }

    private String averagePrice(Long id) {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        return String.format("%.2f", totalPrice);
    }

    private String getTotalCostPrice(Long id) {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);

        List<InvoiceDto> list = invoiceDtos.stream()
                .filter(a -> a.getContractorId().equals(id))
                .collect(Collectors.toList());
        List<InvoiceProductDto> list2 = new ArrayList<>();

        for (InvoiceDto ids : list) {
            list2.addAll(invoiceProductDtos.stream().filter(a -> a.getInvoiceId().equals(ids.getId())).collect(Collectors.toList()));
        }

        for (InvoiceProductDto dto : list2) {
            totalPrice = totalPrice.add(productDtos.get(dto.getProductId().intValue() - 1).getPurchasePrice().multiply(dto.getAmount()));
        }
        return String.format("%.2f", totalPrice);
    }

    private String getTotalPrice(Long id) {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);

        List<InvoiceDto> list = invoiceDtos.stream()
                .filter(a -> a.getContractorId().equals(id))
                .collect(Collectors.toList());
        List<InvoiceProductDto> list2 = new ArrayList<>();

        for (InvoiceDto ids : list) {
            list2.addAll(invoiceProductDtos.stream().filter(a -> a.getInvoiceId().equals(ids.getId())).collect(Collectors.toList()));
        }

        for (InvoiceProductDto dto : list2) {
            totalPrice = totalPrice.add(dto.getPrice().multiply(dto.getAmount()));
        }
        return String.format("%.2f", totalPrice);
    }

    private String getReturnsTotalPrice(Long id) {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);

        List<BuyersReturnDto> list = buyersReturnService.getByContractorId(id);

        for (BuyersReturnDto dto : list) {
            totalPrice = totalPrice.add(dto.getSum());
        }
        return String.format("%.2f", totalPrice);
    }

    private H4 title() {
        H4 title = new H4("Прибыльность");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(e -> {
            switch (currentTab) {
                case "По товарам": {
                    refreshData();
                    fillByContractors();
                    break;
                }
                case "По сотрудникам": {
                    refreshData();
                    fillByEmployees();
                    break;
                }
                case "По покупателям": {
                    refreshData();
                    fillByCustomers();
                    break;
                }
                default:
                    break;
            }
        });
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> {
            if (currentTab.equals("По товарам")) {
                productsFilter.setVisible(!productsFilter.isVisible());
            } else if (currentTab.equals("По покупателям")) {
                costumersFilter.setVisible(!costumersFilter.isVisible());
            } else if (currentTab.equals("По сотрудникам")) {
                employeeFilter.setVisible(!employeeFilter.isVisible());
            }
        });
        return filterButton;
    }

    private Select<String> getPrint() {
        Select getPrint = new Select();
        getPrint.setWidth("130px");
        getPrint.setItems("Печать");
        getPrint.setValue("Печать");
        return getPrint;
    }

    private Button buttonGraph() {
        Button graphButton = new Button("График");
//        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return graphButton;
    }
}
