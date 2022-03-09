package com.trade_accounting.components.sells;


import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.finance.FunnelDto;
import com.trade_accounting.models.dto.invoice.InvoiceDto;
import com.trade_accounting.models.dto.invoice.InvoiceProductDto;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.company.ContractorStatusService;
import com.trade_accounting.services.interfaces.invoice.InvoiceProductService;
import com.trade_accounting.services.interfaces.invoice.InvoiceService;
import com.trade_accounting.services.interfaces.invoice.InvoicesStatusService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import org.springframework.context.annotation.Lazy;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SalesSubSalesFunnelView extends VerticalLayout {

    private final Notifications notifications;
    private final InvoiceProductService invoiceProductService;
    private final InvoiceService invoiceService;
    private final InvoicesStatusService invoicesStatusService;
    private final ContractorService contractorService;
    private final ContractorStatusService contractorStatusService;

    List<FunnelDto> listOrdersDataView = new ArrayList<>();
    List<FunnelDto> listContractorsDataView = new ArrayList<>();
    private final List<InvoiceDto> data;
    private final Grid<FunnelDto> grid = new Grid<>(FunnelDto.class, false);
    //    private final GridPaginator<InvoiceDto> paginator;
    private final GridFilter<FunnelDto> filter;
    private final String typeOfInvoice = "RECEIPT";

    public SalesSubSalesFunnelView(ContractorService contractorService, InvoiceService invoiceService,
                                   InvoicesStatusService invoicesStatusService, InvoiceProductService invoiceProductService,
                                   @Lazy Notifications notifications, ContractorStatusService contractorStatusService) {
        this.contractorService = contractorService;

        this.invoiceService = invoiceService;
        this.invoicesStatusService = invoicesStatusService;
        this.notifications = notifications;
        this.invoiceProductService = invoiceProductService;
        this.data = invoiceService.getAll(typeOfInvoice);
        this.contractorStatusService = contractorStatusService;
//        paginator = new GridPaginator<>(grid, this.data, 50);
        configureGrid();
        configureListDataView();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(upperLayout(), filter, grid);
    }

    private void configureFilter() {
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setItems(listOrdersDataView);
        grid.addColumn("statusName").setFlexGrow(11).setHeader("Статус").setId("Статус");
        grid.addColumn("count").setFlexGrow(11).setHeader("Количество").setId("Количество");
        grid.addColumn("time").setFlexGrow(11).setHeader("Время").setId("Время");
        grid.addColumn("conversion").setFlexGrow(11).setHeader("Конверсия").setId("Конверсия");
        grid.addColumn("price").setFlexGrow(11).setHeader("Сумма").setId("Сумма");

    }

    private void configureListDataView() {
        listOrdersDataView.add(new FunnelDto(invoicesStatusService.getById(1L).getStatusName(), countByInvoiceStatusId(1L), getOrdersTime(1L), "", getPrice(1L)));
        listOrdersDataView.add(new FunnelDto(invoicesStatusService.getById(2L).getStatusName(), countByInvoiceStatusId(2L), getOrdersTime(2L), calcOrdersConversion(2L), getPrice(2L)));
        listOrdersDataView.add(new FunnelDto(invoicesStatusService.getById(3L).getStatusName(), countByInvoiceStatusId(3L), getOrdersTime(3L), calcOrdersConversion(3L), getPrice(3L)));
        listOrdersDataView.add(new FunnelDto(invoicesStatusService.getById(4L).getStatusName(), countByInvoiceStatusId(4L), getOrdersTime(4L), calcOrdersConversion(4L), getPrice(4L)));

        listContractorsDataView.add(new FunnelDto(contractorStatusService.getById(1L).getName(), countByContractorStatusId(1L), ""));
        listContractorsDataView.add(new FunnelDto(contractorStatusService.getById(2L).getName(), countByContractorStatusId(1L), calcContractorsConversion(2L)));
        listContractorsDataView.add(new FunnelDto(contractorStatusService.getById(3L).getName(), countByContractorStatusId(1L), calcContractorsConversion(3L)));
        listContractorsDataView.add(new FunnelDto(contractorStatusService.getById(4L).getName(), countByContractorStatusId(1L), calcContractorsConversion(4L)));
        listContractorsDataView.add(new FunnelDto(contractorStatusService.getById(5L).getName(), countByContractorStatusId(1L), calcContractorsConversion(5L)));

    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(),configurationSubMenu(), buttonFilter(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upper;
    }

    private Tabs configurationSubMenu() {
        Tab orders = new Tab("По заказам");
        Tab contractors = new Tab("По контрагентам");
        Tabs tabs = new Tabs(orders, contractors);

        tabs.addSelectedChangeListener(event -> {
            String tabName = event.getSelectedTab().getLabel();
            if ("По заказам".equals(tabName)) {
                grid.removeAllColumns();
                grid.setItems(listOrdersDataView);
                grid.addColumn("statusName").setFlexGrow(11).setHeader("Статус").setId("Статус");
                grid.addColumn("count").setFlexGrow(11).setHeader("Количество").setId("Количество");
                grid.addColumn("time").setFlexGrow(11).setHeader("Время").setId("Время");
                grid.addColumn("conversion").setFlexGrow(11).setHeader("Конверсия").setId("Конверсия");
                grid.addColumn("price").setFlexGrow(11).setHeader("Сумма").setId("Сумма");
            } else if ("По контрагентам".equals(tabName)) {
                grid.removeAllColumns();
                grid.setItems(listContractorsDataView);
                grid.addColumn("statusName").setFlexGrow(11).setHeader("Статус").setId("Статус");
                grid.addColumn("count").setFlexGrow(11).setHeader("Количество").setId("Количество");
//                grid.addColumn("time").setFlexGrow(11).setHeader("Время").setId("Время");
                grid.addColumn("conversion").setFlexGrow(11).setHeader("Конверсия").setId("Конверсия");
            }
        });
        return tabs;
    }

    public Long countByInvoiceStatusId(Long statusId) {
        Long i = 0L;
        for (InvoiceDto dto : invoiceService.getAll(typeOfInvoice)) {
            if (Objects.equals(dto.getInvoicesStatusId(), statusId)) {
                i++;
            }
        }
        return i;
    }

    public Long countByContractorStatusId(Long statusId) {
        Long i = 0L;
        for (ContractorDto dto : contractorService.getAll()) {
            if (Objects.equals(dto.getContractorStatusId(), statusId)) {
                i++;
            }
        }
        return i;
    }

    public String calcOrdersConversion(Long statusId) {
        double res = 0;
        if (statusId == 1L) {
            return "";
        } else if (statusId > 1L && countByInvoiceStatusId(statusId) > 0) {
            res = ((double) countByInvoiceStatusId(statusId) / (double) countByInvoiceStatusId(statusId - 1) * 100);
        }
        return String.format("%s%%", res);
    }

    public String calcContractorsConversion(Long statusId) {
        double res = 0;
        if (statusId == 1L) {
            return "";
        } else if (statusId > 1L && countByContractorStatusId(statusId) > 0) {
            res = ((double) countByContractorStatusId(statusId) / (double) countByContractorStatusId(statusId - 1) * 100);
        }
        return String.format("%s%%", res);
    }

    private String getPrice(Long statusId) {
        List<InvoiceProductDto> invoiceProductDtoList = invoiceProductService.getAll();
        List<InvoiceDto> invoiceDtoList = invoiceService.getAll(typeOfInvoice);
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        for (InvoiceDto invoiceDto : invoiceDtoList) {
            for (InvoiceProductDto invoiceProductDto : invoiceProductDtoList) {
                if (Objects.equals(invoiceProductDto.getInvoiceId(), invoiceDto.getId()) &&
                        Objects.equals(invoiceDto.getInvoicesStatusId(), statusId))
                    totalPrice = totalPrice.add(invoiceProductDto.getPrice()
                            .multiply(invoiceProductDto.getAmount()));
            }
        }
        return String.format("%.2f", totalPrice);
    }

    private String getOrdersTime(Long id) {
        List<InvoiceDto> list = invoiceService.getAll(typeOfInvoice);
        Long alltime = 0L;
        for (InvoiceDto invoiceDto : list) {
            if (Objects.equals(invoiceDto.getInvoicesStatusId(), id)) {
                LocalDateTime startTime = LocalDateTime.parse(invoiceService.getById(invoiceDto.getId()).getDate());
                LocalDateTime endTime = LocalDateTime.now();
                Long invoiceDuration = Duration.between(startTime, endTime).toMillis();
                alltime += invoiceDuration;
                alltime /= countByInvoiceStatusId(id);
            }
        }
        return String.format("%02dд %02dч %02dм %02dс",
                TimeUnit.MILLISECONDS.toDays(alltime),
                TimeUnit.MILLISECONDS.toHours(alltime) -
                        TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(alltime)),
                TimeUnit.MILLISECONDS.toMinutes(alltime) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(alltime)),
                TimeUnit.MILLISECONDS.toSeconds(alltime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(alltime)));
    }


    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("Воронка продаж — это способ распределения заказов (клиентов) по этапам процесса заключения сделки. " +
                        "Воронка показывает, сколько заказов (клиентов) на каких этапах сделки находятся. " +
                        "Если заказ не был выполнен, на воронке будет отмечен последний этап. " +
                        "Отчет позволяет выяснить, какой этап заключения сделки является «бутылочным горлышком». " +
                        "В отчете представлены разрезы по заказам покупателей и по контрагентам.\n" +
                        "Читать инструкцию: "),
                new Anchor("#", "Воронка Продаж"));
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }


    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private H4 title() {
        H4 title = new H4("Воронка Продаж");
        title.setHeight("2.2em");
        title.setWidth("80px");
        return title;
    }
}