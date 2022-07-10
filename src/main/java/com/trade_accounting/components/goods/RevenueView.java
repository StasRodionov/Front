package com.trade_accounting.components.goods;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridConfigurer;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.warehouse.RevenueDto;
import com.trade_accounting.services.interfaces.warehouse.RevenueService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.Map;

import static com.trade_accounting.config.SecurityConstants.GOODS_REVENUE_VIEW;

@SpringComponent
//Если на страницу не ссылаются по URL или она не является отдельной страницей, а подгружается родительским классом, то URL и Title не нужен
/*@Route(value = GOODS_REVENUE_VIEW, layout = AppView.class)
@PageTitle("Обороты")*/
@UIScope
public class RevenueView extends VerticalLayout {

    private final RevenueService revenueService;
    private final Grid<RevenueDto> grid = new Grid<>(RevenueDto.class, false);
    private final GridConfigurer<RevenueDto> gridConfigurer = new GridConfigurer<>(grid);
    private final GridFilter<RevenueDto> filter;
    private final GridPaginator<RevenueDto> paginator;
    private final List<RevenueDto> data;
    private final RevenueModalWindow revenueModalWindow;
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};

    public RevenueView(RevenueService revenueService, @Lazy RevenueModalWindow revenueModalWindow) {
        this.revenueService = revenueService;
        this.data = getData();
        this.revenueModalWindow = revenueModalWindow;
        this.paginator = new GridPaginator<>(grid, data, 100);
        setSizeFull();
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(upperLayout(), filter, grid, paginator);
    }


    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToComboBox("description", RevenueDto::getDescription, revenueService.getAll());
        filter.setFieldToComboBox("unitShortName", RevenueDto::getUnitShortName, revenueService.getAll());
        filter.onSearchClick(e -> {
            Map<String, String> map = filter.getFilterData2();
            paginator.setData(revenueService.search(map));
        });
        filter.onClearClick(e -> paginator.setData(getData()));
    }

    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(RevenueDto::getDescription).setHeader("Наименование")
                .setKey("description")
                .setId("Наименование");
        grid.addColumn(RevenueDto::getItemNumber).setHeader("Код товара")
                .setKey("itemNumber")
                .setId("Код товара");
        grid.addColumn(RevenueDto::getUnitShortName).setHeader("Ед. изм.")
                .setKey("unitShortName")
                .setId("Ед. изм.");
        grid.addColumn(RevenueDto::getStartOfPeriodAmount).setHeader("Кол-во на начало периода")
                .setKey("startOfPeriodAmount")
                .setId("Кол-во на начало периода");
        grid.addColumn(RevenueDto::getStartOfPeriodSumOfPrice).setHeader("Сумма на начало периода")
                .setKey("startOfPeriodSumOfPrice")
                .setId("Сумма на начало периода");
        grid.addColumn(RevenueDto::getComingAmount).setHeader("Кол-во (приход)")
                .setKey("comingAmount")
                .setId("Кол-во (приход)");
        grid.addColumn(RevenueDto::getComingSumOfPrice).setHeader("Сумма (приход)")
                .setKey("comingSumOfPrice")
                .setId("Сумма (приход)");
        grid.addColumn(RevenueDto::getSpendingAmount).setHeader("Кол-во (расход)")
                .setKey("spendingAmount")
                .setId("Кол-во (расход)");
        grid.addColumn(RevenueDto::getSpendingSumOfPrice).setHeader("Сумма (расход)")
                .setKey("spendingSumOfPrice")
                .setId("Сумма (расход)");
        grid.addColumn(RevenueDto::getEndOfPeriodAmount).setHeader("Кол-во на конец периода")
                .setKey("endOfPeriodAmount")
                .setId("Кол-во на конец периода");
        grid.addColumn(RevenueDto::getEndOfPeriodSumOfPrice).setHeader("Сумма на конец периода")
                .setKey("endOfPeriodSumOfPrice")
                .setId("Сумма на конец периода");
        grid.getColumns().forEach(column -> column.setResizable(true).setTextAlign(ColumnTextAlign.END)
                .setAutoWidth(true).setSortable(true));
        grid.getColumnByKey("description").setTextAlign(ColumnTextAlign.START);
        grid.getColumnByKey("unitShortName").setTextAlign(ColumnTextAlign.START);
        gridConfigurer.addConfigColumnToGrid();

        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.getColumns().forEach(column -> column.setAutoWidth(true));

        grid.addItemDoubleClickListener(event -> {
            RevenueDto dto = event.getItem();
            RevenueModalWindow revenueModalWindow = new RevenueModalWindow(
                    revenueService
            );
            revenueModalWindow.setRevenueForEdit(dto);
            revenueModalWindow.open();
        });
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(),buttonUnit(), buttonFilter(), numberField(), getPrint(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private Button buttonUnit() {
        Button createRetailStoreButton = new Button("Обороты", new Icon(VaadinIcon.PLUS_CIRCLE));
        RevenueModalWindow revenueModalWindow =
                new RevenueModalWindow(revenueService);
        createRetailStoreButton.addClickListener(e -> {
            revenueModalWindow.addDetachListener(event -> updateList());
            revenueModalWindow.open();
        });
        createRetailStoreButton.getStyle().set("cursor", "pointer");
        return createRetailStoreButton;
    }

    private H2 title() {
        H2 title = new H2("Обороты");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion("В разделе представлены приход и расход товаров за определенный "+
                "временной промежуток. Можно посмотреть статистику по складу, поставщику, проекту и так далее. " +
                "Нажмите на строку с товаром — откроется список всех документов, " +
                "которые повлияли на оборот по данному товару.");
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(e -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }


    private Button buttonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
    }

    private void updateList() {
        grid.setItems(revenueService.getAll());
    }


    private Button buttonSettings() {
        Button buttonSettings = new Button(new Icon(VaadinIcon.COG_O));
        return buttonSettings;
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }


    private Select<String> getPrint() {
        return SelectConfigurer.configurePrintSelect();
    }

    private List<RevenueDto> getData() {
        return revenueService.getAll();
    }
}