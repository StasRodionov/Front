package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.BuyersReturnDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.InvoiceProductDto;
import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.services.interfaces.BuyersReturnService;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.InvoiceProductService;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.ReturnAmountByProductService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private Grid<ContractorDto> gridCostumers = new Grid<>(ContractorDto.class, false);
    private final GridFilter<InvoiceProductDto> productsFilter;
    private Grid<InvoiceProductDto> gridProducts = new Grid<>(InvoiceProductDto.class, false);
    private GridPaginator<ContractorDto> paginatorCustomers;
    private GridPaginator<InvoiceProductDto> paginatorProducts;

    private List<ContractorDto> contractorDtos;
    private List<InvoiceProductDto> invoiceProductDtos;
    private List<ProductDto> productDtos;
    private List<InvoiceDto> invoiceDtos;

    private final GridFilter<ContractorDto> costumersFilter;

    public SalesSubProfitabilityView(InvoiceService invoiceService, CompanyService companyService,
                                     ContractorService contractorService,
                                     InvoiceProductService invoiceProductService,
                                     ProductService productService,
                                     BuyersReturnService buyersReturnService,
                                     ReturnAmountByProductService returnAmountByProductService) {
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.productService = productService;
        this.buyersReturnService = buyersReturnService;
        this.returnAmountByProductService = returnAmountByProductService;

        this.contractorDtos = getContractorDtos();
        this.invoiceProductDtos = getInvoiceProductDtos();
        this.productDtos = getProductDtos();
        this.invoiceDtos = getInvoiceDtos();

        paginatorCustomers = new GridPaginator<>(gridCostumers, contractorDtos, 50);
        paginatorProducts = new GridPaginator<>(gridProducts, invoiceProductDtos, 50);

        setHorizontalComponentAlignment(Alignment.CENTER, paginatorCustomers);
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorProducts);

        add(upperLayout());
        configureCostumersGrid(false);
        configureProductsGrid(false);

        this.costumersFilter = new GridFilter<>(gridCostumers);
        this.productsFilter = new GridFilter<>(gridProducts);

        configureProductFilter();
        configureCostumerFilter();

        add(productsFilter, costumersFilter, gridProducts, paginatorProducts, gridCostumers, paginatorCustomers);
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

        gridProducts.addColumn(iDto -> getTotalReturnPriceForProducts(iDto.getProductId(), iDto.getInvoiceId()))
                .setHeader("Сумма возвратов")
                .setKey("totalReturnPrice")
                .setSortable(true)
                .setId("Сумма возвратов");

        gridProducts.addColumn(iDto -> getProfitByProducts(iDto.getId(), iDto.getProductId(), iDto.getInvoiceId()))
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

    private void configureCostumerFilter() {
        costumersFilter.onClearClick(e -> {
            refreshData();
            reloadTabs();
        });
        costumersFilter.onSearchClick(e -> {
            int i = 1;
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
                ip.setId((long)i++);
            }
            this.invoiceProductDtos = additionalProductsFiltering(invoiceProductDtos, map);
            i = 1;
            for (InvoiceProductDto ip : invoiceProductDtos) {
                ip.setId((long)i++);
            }
            reloadTabs();
        });
    }

    private List<InvoiceProductDto> additionalProductsFiltering(List<InvoiceProductDto> inputList, Map<String, String> query) {
        List<InvoiceProductDto> result = inputList;
        if (query.containsKey("profit")) {
            result = result.stream()
                    .filter(e -> getProfitByProducts(e.getId(), e.getProductId(), e.getInvoiceId()).equals(query.get("profit")))
                    .collect(Collectors.toList());
        }
        if (query.containsKey("totalReturnPrice")) {
            result = result.stream()
                    .filter(e -> getTotalReturnPriceForProducts(e.getProductId(), e.getInvoiceId()).equals(query.get("totalReturnPrice")))
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

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), configurationSubMenu(), buttonFilter(), getPrint(), buttonGraph());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private void refreshData() {
        this.invoiceDtos = getInvoiceDtos();
        this.productDtos = getProductDtos();
        this.contractorDtos = getContractorDtos();
        this.invoiceProductDtos = getInvoiceProductDtos();
    }

    private void fillByContractors() {
        remove(gridCostumers, paginatorCustomers);
        add(gridProducts, paginatorProducts);
        gridProducts.removeAllColumns();
        configureProductsGrid(false);
        currentTab = "По товарам";
    }

    private void fillByEmployees() {
        //Реализовать прибыльность по сотрудникам
        remove(gridCostumers, paginatorCustomers);
        remove(gridProducts, paginatorProducts);
        currentTab = "По сотрудникам";
    }

    private void fillByCustomers() {
        remove(gridProducts, paginatorProducts);
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
            refreshData();
            reloadTabs();
        });
        return tabs;
    }

    private String getProfitByProducts(Long id, Long productId, Long invoiceId) {
        BigDecimal profit;

        String buyerBuying = getTotalPriceForProducts(id);
        buyerBuying = buyerBuying.replace(',', '.');
        String buyersReturn = getTotalReturnPriceForProducts(productId, invoiceId);
        buyersReturn = buyersReturn.replace(',', '.');
        String costPrice = getTotalCostPriceForProducts(id);
        costPrice = costPrice.replace(',', '.');

        profit = new BigDecimal(buyerBuying).subtract(new BigDecimal(costPrice)).subtract(new BigDecimal(buyersReturn));
        return String.format("%.2f", profit);
    }

    private String getTotalReturnPriceForProducts(Long productId, Long invoiceId) {
        return String.format("%.2f", returnAmountByProductService
                .getTotalReturnAmountByProduct(productId, invoiceId)
                .getAmount());
    }

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

    private H2 title() {
        H2 title = new H2("Прибыльность");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Dialog dialog = new Dialog();
        Button cancelButton = new Button("Закрыть", event -> dialog.close());
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponentAsFirst(cancelButton);
        dialog.add(new Text("В разделе представлена прибыль от реализации товаров и услуг. Здесь отображаются товары и услуги из документов " +
                "\n" +
                "розничной продажи и отгрузок за указанный период. Если период не задать, будет показана прибыльность за последний месяц.\n" +
                "\n" +
                "Отчет позволяет оценить рентабельность продукции. Он может быть сформирован по товарам, сотрудникам и покупателям.\n" +
                "\n" +
                "Отчет по сотрудникам позволяет отслеживать работу каждого конкретного сотрудника и рассчитывать вознаграждение по результатам продаж.\n" +
                "\n" +
                "По каждому товару можно увидеть список отгрузок, розничных продаж и возвратов. Продажи в минус выделяются красным.\n" +
                "\n" +
                "Читать инструкцию: Прибыльность"));
        dialog.setWidth("500px");
        dialog.setHeight("300px");
        buttonQuestion.addClickListener(event -> dialog.open());
        Shortcuts.addShortcutListener(dialog, dialog::close, Key.ESCAPE);
        dialog.add(new Div(cancelButton));
        return buttonQuestion;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(e -> {
            switch (currentTab) {
                case "По товарам" : {
                    refreshData();
                    fillByContractors();
                    break;
                }
                case "По сотрудникам" : {
                    // Требует реализации
                    refreshData();
                    fillByEmployees();
                    break;
                }
                case "По покупателям" : {
                    refreshData();
                    fillByCustomers();
                    break;
                }
                default: break;
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
