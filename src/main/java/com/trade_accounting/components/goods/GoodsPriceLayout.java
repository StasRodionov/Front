package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.PriceListDto;
import com.trade_accounting.models.dto.company.PriceListProductPercentsDto;
import com.trade_accounting.models.dto.company.TypeOfPriceDto;
import com.trade_accounting.models.dto.warehouse.ProductDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.PriceListProductPercentsService;
import com.trade_accounting.services.interfaces.company.PriceListProductService;
import com.trade_accounting.services.interfaces.company.PriceListService;
import com.trade_accounting.services.interfaces.company.TypeOfPriceService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.trade_accounting.config.SecurityConstants.*;

@Slf4j
@Route(value = GOODS_GOODS_PRICE_VIEW, layout = AppView.class)
@PageTitle("Прайс-лист")
@SpringComponent
@UIScope
public class GoodsPriceLayout extends VerticalLayout implements AfterNavigationObserver {

    private final TextField textField = new TextField();
    private final MenuBar selectXlsTemplateButton = new MenuBar();
    private final PriceListService priceListService;
    private final CompanyService companyService;
    private final ProductService productService;
    private List<PriceListDto> data;
    private final HorizontalLayout actions;
    private final Grid<PriceListDto> grid = new Grid<>(PriceListDto.class, false);
    private final GridPaginator<PriceListDto> paginator;
    private final GridFilter<PriceListDto> filter;
    private final Notifications notifications;
    private final GoodsPriceLayoutPriceListView priceListContent;
    private final PriceListProductPercentsService priceListProductPercentsService;
    private final TypeOfPriceService typeOfPriceService;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public GoodsPriceLayout(PriceListService priceListService,
                            CompanyService companyService,
                            ProductService productService,
                            PriceListProductService priceListProductService,
                            Notifications notifications,
                            @Qualifier("goodsPriceLayoutPriceListView") GoodsPriceLayoutPriceListView priceListContent,
                            PriceListProductPercentsService priceListProductPercentsService, TypeOfPriceService typeOfPriceService) {
        this.priceListService = priceListService;
        this.companyService = companyService;
        this.productService = productService;
        this.notifications = notifications;
        this.priceListProductPercentsService = priceListProductPercentsService;
        this.typeOfPriceService = typeOfPriceService;
        this.data = getData();
        actions = new HorizontalLayout();
        paginator = new GridPaginator<>(grid, data, 50);
        this.priceListContent = priceListContent;
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        setSizeFull();
        configureGrid();

        Map<ValueProvider<PriceListDto, ?>, List<String>> filterMap = new LinkedHashMap<>();
        filterMap.put((priceListDto -> priceListProductService
                .getByPriceListId(priceListDto.getId())), List.of("productName", "Товар"));
        filterMap.put(priceListDto -> typeOfPriceService
                .getById(priceListDto.getTypeOfPriceId()), List.of("typeOfPrice", "Тип цены"));
        filter = new GridFilter<>(grid, filterMap);

        add(configureActions(), configureFilter(), grid, paginator);
    }

    private HorizontalLayout configureActions() {
        actions.add(buttonQuestion(), getTextOrder(), buttonRefresh(), buttonUnit(),
                buttonFilter(), text(), numberField(), valueSelect(), valueStatus(),
                valuePrint(), buttonSettings(), selectXlsTemplateButton);
        actions.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return actions;
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
//        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(PriceListDto::getNumber).setKey("number").setHeader("№").setSortable(true).setId("№");
        grid.addColumn(priceListDto -> dateTimeFormatter.format(LocalDateTime.parse(priceListDto.getDate())))
                .setKey("date").setHeader("Дата после").setSortable(true).setId("Дата после");
        grid.addColumn(priceListDto -> companyService.getById(priceListDto.getCompanyId())
                .getName()).setKey("company").setHeader("Организация").setId("Организация");
        grid.addColumn(new ComponentRenderer<>(this::getIsSentIcon)).setKey("sent").setHeader("Отправлено")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsPrintIcon)).setKey("print").setHeader("Напечатано")
                .setId("Напечатано");
        grid.addColumn(PriceListDto::getComment).setKey("comment").setHeader("Комментарий").setId("Комментарий");
        grid.setHeight("66vh");
        grid.setMaxWidth("2500px");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addItemClickListener(event -> {
            PriceListDto priceList = event.getItem();
            PriceListProductPercentsDto priceListProductPercentsDto = priceListProductPercentsService
                    .getByPriceListId(priceList.getId()).get(0);
            priceListContent.setPriceListForEdit(priceList, priceListProductPercentsDto, (byte) 1);
            this.getUI().ifPresent(ui -> ui.navigate(GOODS_GOODS__PRICE_LIST_EDIT));
        });
    }

    private List<PriceListDto> getData() {
        return priceListService.getAll().stream().filter(pl -> pl.getIsRecyclebin()
                .equals(false)).collect(Collectors.toList());
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion("Прайс-листы позволяют быстро назначать цены в документах: заказах," +
                " счетах и расходных накладных.\n" +
                "Прайс-лист может быть заполнен вручную, по одному товару или автоматически — из номенклатуры " +
                "или из остатков. Можно создать несколько независимых прайс-листов.");
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(ev -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    public void updateList() {
        grid.setItems(priceListService.getAll().stream().filter(pl -> pl.getIsRecyclebin()
                .equals(false)).collect(Collectors.toList()));
    }

    private Button buttonUnit() {
        Button button = new Button("Прайс-лист", new Icon(VaadinIcon.PLUS_CIRCLE));
        button.addClickListener(e -> button.getUI()
                .ifPresent(ui -> ui.navigate(GOODS_GOODS__PRICE_LIST_CREATE))
        );
        return button;
    }

    private Button buttonFilter() {
        Button button = new Button("Фильтр");
        button.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return button;
    }

    private HorizontalLayout configureFilter() {
        filter.setFieldToDatePicker("date");

        filter.setFieldToComboBox("company", CompanyDto::getName, companyService.getAll());
        filter.setFieldToComboBox("sent", Boolean.TRUE, Boolean.FALSE);
        filter.setFieldToComboBox("print", Boolean.TRUE, Boolean.FALSE);

        filter.setFieldToComboBox("productName", ProductDto::getName, productService.getAll());
        filter.setFieldToComboBox("typeOfPrice", TypeOfPriceDto::getName, typeOfPriceService.getAll());

        filter.onSearchClick(e -> {
            Map<String, String> map = filter.getFilterDataBetween();
            if(map.get("date") != null && map.get("dateBefore") != null){
                paginator.setData(priceListService.searchByBetweenDataFilter(map));
            } else {
                paginator.setData(priceListService.searchByFilter(map));
            }
        });
        filter.onClearClick(e -> paginator.setData(getData()));
        return filter;
    }

    private void updateListQsearch(String text) {
        grid.setItems(priceListService.quickSearch(text));
    }

    private TextField text() {
        textField.setWidth("300px");
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setClearButtonVisible(true);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> updateListQsearch(textField.getValue()));
        setSizeFull();
        return textField;
    }

    private NumberField numberField() {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect() {
        return SelectConfigurer.configureDeleteSelect(() -> {
            moveToRecycleBinSelectedPriceLists();
            grid.deselectAll();
            paginator.setData(getData());
        });
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private Select<String> valueStatus() {
        return SelectConfigurer.configureStatusSelect();
    }

    private Select<String> valuePrint() {
        return SelectConfigurer.configurePrintSelect();
    }

    private H2 getTextOrder() {
        final H2 textOrder = new H2("Прайс-лист");
        textOrder.setHeight("2.2em");
        return textOrder;
    }

    private Component getIsSentIcon(PriceListDto priceListDto) {
        if (priceListDto.getIsSent()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component getIsPrintIcon(PriceListDto priceListDto) {
        if (priceListDto.getIsPrint()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    public List<PriceListDto> moveToRecycleBinSelectedPriceLists() {
        List<PriceListDto> moved = new ArrayList<>();
        if (!grid.getSelectedItems().isEmpty()) {
            for (PriceListDto priceListDto : grid.getSelectedItems()) {
                moved.add(priceListService.getById(priceListDto.getId()));
                priceListService.moveToIsRecyclebin(priceListDto.getId());
                notifications.infoNotification("Выбранные прайс-листы помещены в корзину");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные прайс-листы");
        }

        return moved;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
