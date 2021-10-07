package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.BuyersReturnDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.InvoiceProductDto;
import com.trade_accounting.services.interfaces.BuyersReturnService;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.InvoiceProductService;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.trade_accounting.services.interfaces.ProductService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
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
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Route(value = "profitability", layout = AppView.class)
@PageTitle("Прибыльность")
@SpringComponent
@UIScope
public class SalesSubProfitabilityView extends VerticalLayout implements AfterNavigationObserver {
    private final CompanyService companyService;
    private final ContractorService contractorService;
    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final ProductService productService;
    private final BuyersReturnService buyersReturnService;

    private final Grid<ContractorDto> grid = new Grid<>(ContractorDto.class, false);
    private final GridPaginator<ContractorDto> paginator;
    private final GridFilter<ContractorDto> filter;
    private final List<ContractorDto> data;

    public SalesSubProfitabilityView(InvoiceService invoiceService, CompanyService companyService,
                                     ContractorService contractorService, InvoiceProductService invoiceProductService,
                                     ProductService productService, BuyersReturnService buyersReturnService) {
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.productService = productService;
        this.buyersReturnService = buyersReturnService;
        this.data = getData();
        paginator = new GridPaginator<>(grid, data, 50);
        this.filter = new GridFilter<>(grid);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(upperLayout());
        add(grid, paginator);

    }

    private List<ContractorDto> getData() {
        return contractorService.getAll();
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), configurationSubMenu(), buttonFilter(), getPrint(), buttonGraph());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private Tabs configurationSubMenu() {
        Tab contractors = new Tab("По товарам");
        Tab employees = new Tab("По сотрудникам");
        Tab customer = new Tab("По покупателям");
        Tabs tabs = new Tabs(contractors, employees, customer);

        tabs.addSelectedChangeListener(event -> {
            String tabName = event.getSelectedTab().getLabel();
            if ("По товарам".equals(tabName)) {

            } else if ("По сотрудникам".equals(tabName)) {

            } else if ("По покупателям".equals(tabName)) {
                grid.removeAllColumns();
                grid.setItems(data);

                grid.addColumn(iDto -> contractorService.getById(iDto.getId()).getName()).setHeader("Контрагент").setKey("contractorDto")
                        .setId("Контрагент");
                grid.addColumn(iDto -> getTotalPrice(iDto.getId())).setHeader("Сумма продаж").setSortable(true);
                grid.addColumn(iDto -> getTotalCostPrice(iDto.getId())).setHeader("Сумма себестоимости").setSortable(true);
                grid.addColumn(iDto -> averagePrice(iDto.getId())).setHeader("Средний чек").setSortable(true);

                grid.addColumn(iDto -> getReturnsTotalPrice(iDto.getId())).setHeader("Сумма возврата").setSortable(true);
                grid.addColumn(iDto -> getProfit(iDto.getId())).setHeader("Прибыль").setSortable(true);

                grid.setHeight("100vh");
                grid.setColumnReorderingAllowed(true);
                grid.setSelectionMode(Grid.SelectionMode.MULTI);
            }
        });
        return tabs;
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

        List<InvoiceDto> list = invoiceService.getByContractorId(id);
        List<InvoiceProductDto> list2 = new ArrayList<>();

        for(InvoiceDto ids : list) {
            list2.addAll(invoiceProductService.getByInvoiceId(ids.getId()));
        }

        for(InvoiceProductDto dto : list2) {
            totalPrice = totalPrice.add(productService.getById(dto.getProductId()).getPurchasePrice().multiply(dto.getAmount()));
        }
        return String.format("%.2f", totalPrice);
    }

    private String getTotalPrice(Long id) {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);

        List<InvoiceDto> list = invoiceService.getByContractorId(id);
        List<InvoiceProductDto> list2 = new ArrayList<>();

        for(InvoiceDto ids : list) {
            list2.addAll(invoiceProductService.getByInvoiceId(ids.getId()));
        }

        for(InvoiceProductDto dto : list2) {
            totalPrice = totalPrice.add(dto.getPrice().multiply(dto.getAmount()));
        }

        return String.format("%.2f", totalPrice);
    }

    private String getReturnsTotalPrice(Long id) {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);

        List<BuyersReturnDto> list = buyersReturnService.getByContractorId(id);

        for(BuyersReturnDto dto : list) {
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
        dialog.add(new Text("В разделе представлена прибыль от реализации товаров и услуг. Здесь отображаются товары и услуги из документов розничной продажи и отгрузок за указанный период. Если период не задать, будет показана прибыльность за последний месяц.\n" +
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
//        buttonRefresh.addClickListener(e -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonFilter() {
        Button filterButton = new Button("Фильтр");
//        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
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

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

    }
}
