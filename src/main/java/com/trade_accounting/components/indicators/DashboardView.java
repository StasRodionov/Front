package com.trade_accounting.components.indicators;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.LineChart;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.InvoiceProductDto;
import com.trade_accounting.services.interfaces.InvoiceProductService;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@SpringComponent
@Route(value = "dashboardView", layout = AppView.class)
@PageTitle("Показатели")
@UIScope
public class DashboardView extends VerticalLayout {

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private VerticalLayout chartLayout = new VerticalLayout();
    private List<String> category;
    private VerticalLayout content = new VerticalLayout();
    private Integer[] data;
    private List<InvoiceDto> invoiceDtoList;
    private List<InvoiceProductDto> invoiceProductDtoList;

    public DashboardView(InvoiceService invoiceService, InvoiceProductService invoiceProductService) {
        this.invoiceProductService = invoiceProductService;
        this.invoiceService = invoiceService;
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(getUpperLeftLayout(), chartLayout);
        horizontalLayout.setPadding(false);
        add(horizontalLayout);
        setSpacing(false);
        setPadding(false);
    }

    private void getChart() {
        LineChart lineChart = new LineChart(category, data, "Сумма продаж");
        lineChart.setHeight("200%");
        lineChart.setWidth("200%");
        chartLayout.removeAll();
        chartLayout.add(lineChart);
    }

    private Component getUpperLeftLayout() {
        VerticalLayout layout = new VerticalLayout();
        Tab week = new Tab("Неделя");
        Tab month = new Tab("Месяц");
        Tab year = new Tab("Год");
        Tabs tabs = new Tabs(week, month, year);
        tabs.addSelectedChangeListener(event -> setContent(event.getSelectedTab().getLabel()));
        tabs.setSelectedTab(week);
        setContent("Неделя");
        layout.setPadding(false);
        layout.setAlignItems(Alignment.CENTER);
        content.setAlignItems(Alignment.CENTER);
        layout.add(tabs, addDateToTab(), content);
        return layout;
    }


    private void setContent(String tabName) {
        switch (tabName) {
            case "Месяц":
                content.removeAll();
                content.add(addTodaysSells());
                content.add(addMonthSells());
                category = IntStream.range(1, YearMonth.now().lengthOfMonth() + 1).mapToObj(String::valueOf)
                        .collect(Collectors.toList());
                addMonthData();
                getChart();
                break;
            case "Год":
                content.removeAll();
                content.add(addTodaysSells());
                content.add(addYearSells());
                category = Arrays.asList("Янв", "Фев", "Март", "Апр", "Май", "Июнь", "Июль", "Авг", "Сент", "Окт",
                        "Нояб", "Дек");
                addYearData();
                getChart();
                break;
            default:
                content.removeAll();
                content.add(addTodaysSells());
                content.add(addWeekSells());
                category = Arrays.asList("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс");
                addWeekData();
                getChart();
                break;
        }
    }


    private void addWeekData() {
        List<Integer> integerList = new ArrayList<>();
        LocalDateTime datefrom = LocalDateTime.now().minusDays(LocalDateTime.now().getDayOfWeek().getValue())
                .with(LocalTime.MIN);
        for (int i = 1; i < LocalDateTime.now().getDayOfWeek().getValue() + 1; i++) {
            integerList.add(getSells(datefrom, datefrom.plusDays(1).with(LocalTime.MAX))[1]);
            datefrom = datefrom.plusDays(1);
        }
        data = integerList.toArray(new Integer[0]);
    }

    private void addMonthData() {
        List<Integer> integerList = new ArrayList<>();
        LocalDateTime datefrom = LocalDateTime.now().minusDays(LocalDateTime.now().getDayOfMonth())
                .with(LocalTime.MIN);
        for (int i = 1; i < LocalDateTime.now().getDayOfMonth() + 1; i++) {
            integerList.add(getSells(datefrom, datefrom.plusDays(1).with(LocalTime.MAX))[1]);
            datefrom = datefrom.plusDays(1);
        }
        data = integerList.toArray(new Integer[0]);
    }


    private void addYearData() {
        List<Integer> integerList = new ArrayList<>();
        LocalDateTime datefrom = LocalDateTime.now().minusDays(LocalDateTime.now().getDayOfYear())
                .with(LocalTime.MIN);
        for (int i = LocalDate.now().getMonthValue(); i > 0; i--) {
            integerList.add(getSells(datefrom, datefrom.plusMonths(1).with(LocalTime.MAX))[1]);
            datefrom = datefrom.plusMonths(1);
        }
        data = integerList.toArray(new Integer[0]);
    }


    private HorizontalLayout addYearSells() {
        HorizontalLayout layout = new HorizontalLayout();
        long daysInYear = Year.of(LocalDate
                .now().minusYears(1).getYear()).length();
        long yearDay = LocalDate.now().getDayOfYear();
        int[] yearData = getSells(LocalDateTime.of(LocalDate.now().minusDays(yearDay - 1), LocalTime.MIN), null);
        int[] lastYearData = getSells(LocalDateTime.of(LocalDate.now().minusDays(yearDay + daysInYear - 1), LocalTime.MIN),
                LocalDateTime.of(LocalDate.now().minusDays(yearDay), LocalTime.MIN));
        layout.add(new VerticalLayout(new H4(String.valueOf(yearData[0])), new H6("Продаж")));
        layout.add(new VerticalLayout(new H4(String.valueOf(yearData[1])), new H6("Руб")));
        layout.add(new VerticalLayout(new H4(lastYearData[0] + " " + "Руб" + " " + "(" + (percentage(lastYearData[1], yearData[1])) + "%)"),
                new H6("По сравнению с прошлым годом")));
        layout.setWidth("50%");
        layout.setHeight("50%");
        return layout;
    }


    private HorizontalLayout addWeekSells() {
        HorizontalLayout layout = new HorizontalLayout();
        long weekDay = LocalDate.now().getDayOfWeek().getValue();
        int[] weeksData = getSells(LocalDateTime.of(LocalDate.now().minusDays(weekDay - 1), LocalTime.MIN), null);
        int[] lastWeeksData = getSells(LocalDateTime.of(LocalDate.now().minusDays(weekDay + 6), LocalTime.MIN),
                LocalDateTime.of(LocalDate.now().minusDays(weekDay - 1), LocalTime.MIN));
        layout.add(new VerticalLayout(new H4(String.valueOf(weeksData[0])), new H6("Продаж")));
        layout.add(new VerticalLayout(new H4(String.valueOf(weeksData[1])), new H6("Руб")));
        layout.add(new VerticalLayout(new H4(lastWeeksData[0] + " " + "Руб" + " " + "(" +
                (percentage(lastWeeksData[1], weeksData[1])) + "%)"), new H6("По сравнению с прошлой неделей")));
        layout.setWidth("50%");
        layout.setHeight("50%");
        return layout;
    }


    private HorizontalLayout addMonthSells() {
        HorizontalLayout layout = new HorizontalLayout();
        LocalDate date = LocalDate.now();
        long monthDay = LocalDate.now().getDayOfMonth();
        int[] monthData = getSells(LocalDateTime.of(date.minusDays(monthDay - 1), LocalTime.MIN), null);
        int[] lastMonthData = getSells(LocalDateTime.of(date.minusDays(monthDay +
                        YearMonth.of(date.getYear(), date.getMonthValue()).lengthOfMonth() - 1), LocalTime.MIN),
                LocalDateTime.of(LocalDate.now().minusDays(monthDay - 1), LocalTime.MIN));
        layout.add(new VerticalLayout(new H4(String.valueOf(monthData[0])), new H6("Продаж")));
        layout.add(new VerticalLayout(new H4(String.valueOf(monthData[1])), new H6("Руб")));
        layout.add(new VerticalLayout(new H4(lastMonthData[0] + " " + "Руб" + " " + "(" +
                (percentage(lastMonthData[1], monthData[1])) + "%)"), new H6("По сравнению с прошлым месяцем")));
        layout.setWidth("50%");
        layout.setHeight("50%");
        return layout;
    }


    private HorizontalLayout addTodaysSells() {
        HorizontalLayout layout = new HorizontalLayout();
        int[] todaysData = getSells(LocalDateTime.of(LocalDate.now(), LocalTime.MIN), null);
        int[] yesterdaysData = getSells(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN), LocalDateTime
                .now().with(LocalTime.MIN));
        VerticalLayout v1 = new VerticalLayout(new H4(String.valueOf(todaysData[0])), new H6("Продаж"));
        layout.add(v1);
        VerticalLayout v2 = new VerticalLayout(new H4(String.valueOf(todaysData[1])), new H6("Руб"));
        layout.add(v2);
        VerticalLayout v3 = new VerticalLayout(new H4(yesterdaysData[1] + " " + "Руб" + " " + "(" +
                (percentage(yesterdaysData[1], todaysData[1])) + "%)"), new H6("По сравнению со вчера"));
        layout.add(v3);
        layout.setSpacing(false);
        layout.setWidth("50%");
        layout.setHeight("50%");
        return layout;
    }


    private void setInvoiceDtoAndProductList(LocalDateTime from, LocalDateTime to) {
        invoiceDtoList = invoiceService.searchFromDate(from);
        if(to != null) {
            invoiceDtoList = invoiceDtoList.stream().filter(invoiceDto -> LocalDateTime.parse(invoiceDto.getDate())
                            .isBefore(to))
                    .collect(Collectors.toList());
        }
        invoiceProductDtoList = new ArrayList<>();
        for (InvoiceDto i:
                invoiceDtoList) {
            invoiceProductDtoList.addAll(invoiceProductService.getByInvoiceId(i.getId()));
        }
    }


    private int[] getSells(LocalDateTime dateFrom, LocalDateTime to) {
        setInvoiceDtoAndProductList(dateFrom, to);
        int numberOfSales = invoiceDtoList.size();
        int revenue = invoiceProductDtoList.stream().mapToInt(s -> s.getPrice().intValue() * s.getAmount().intValue())
                .sum();

        return new int[]{numberOfSales, revenue};
    }

    private HorizontalLayout addDateToTab() {
        LocalDate date = LocalDate.now();
        int dayOfWeek = DayOfWeek.from(date).getValue();
        String[] daysOfWeek = {"понедельник", "вторник", "среда", "четверг", "пятница", "суббота", "воскресенье"};
        String[] months = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября",
                "октября", "ноября", "декабря"};
        HorizontalLayout hl = new HorizontalLayout(new H6("Сегодня " + daysOfWeek[dayOfWeek - 1] + " " + date.getDayOfMonth() +
                " " + months[date.getMonthValue() - 1]));
        hl.setPadding(false);
        return hl;
    }

    private long percentage(int first, int second) {
        if(first == 0 && second == 0) {
            return 0;
        }
       return first == 0 ? 100 : Math.round(((double) second/first) * 100);
    }
}
