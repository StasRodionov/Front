package com.trade_accounting.components.retail;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.components.util.configure.components.select.SelectConstants;
import com.trade_accounting.components.util.configure.components.select.SelectExt;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.retail.RetailMakingDto;
import com.trade_accounting.models.dto.retail.RetailStoreDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.retail.RetailMakingService;
import com.trade_accounting.services.interfaces.retail.RetailStoreService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Route(value = "RetailMakingView", layout = AppView.class)
@PageTitle("Внесения")
@SpringComponent
@UIScope
public class RetailMakingView extends VerticalLayout implements AfterNavigationObserver {

    private final RetailMakingService retailMakingService;
    private final RetailStoreService retailStoreService;
    private final CompanyService companyService;

    private final Grid<RetailMakingDto> grid = new Grid<>(RetailMakingDto.class, false);
    private final GridPaginator<RetailMakingDto> paginator;
    private final Notifications notifications;

    private final List<RetailMakingDto> data;

    private final TextField textFieldUpdateTextField = new TextField();

    private final GridFilter<RetailMakingDto> filter;

    private final TextField textField = new TextField();


    public RetailMakingView(RetailMakingService retailMakingService, RetailStoreService retailStoreService,
                            CompanyService companyService, Notifications notifications) {
        this.retailMakingService = retailMakingService;
        this.retailStoreService = retailStoreService;
        this.companyService = companyService;
        this.data = retailMakingService.getAll();
        this.notifications = notifications;

        this.paginator = new GridPaginator<>(grid, data, 100);
        configureGrid();

        this.filter = new GridFilter<>(grid);
        configureFilter();

        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(upperLayout(), filter, grid, paginator);
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        grid.addColumn("id").setWidth("5%").setHeader("№").setId("№");
        grid.addColumn(retailMakingDto -> formatDate(retailMakingDto.getDate()))
                .setKey("date").setWidth("12%").setHeader("Время").setSortable(true).setId("Дата");
        grid.addColumn(retailMakingDto -> retailStoreService.getById(retailMakingDto.getRetailStoreId())
                        .getName()).setKey("retail_store_id").setWidth("10%").setHeader("Точка продаж")
                .setSortable(true).setId("Точка продаж");
        grid.addColumn("fromWhom").setWidth("10%").setHeader("От кого").setId("От кого");
        grid.addColumn(retailMakingDto -> companyService.getById(retailMakingDto.getCompanyId())
                        .getName()).setKey("company_id").setWidth("15%").setHeader("Организация")
                .setSortable(true).setId("Организация");
        grid.addColumn("sum").setWidth("10%").setHeader("Сумма").setId("Сумма");
        grid.addColumn(new ComponentRenderer<>(this::isSentCheckedIcon)).setWidth("10%").setKey("sent")
                .setHeader("Отправлено").setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::isPrintedCheckedIcon)).setWidth("10%").setKey("print")
                .setHeader("Напечатано").setId("Напечатано");
        grid.addColumn("comment").setWidth("18%").setHeader("Комментарий").setId("Комментарий");

        grid.setHeight("66vh");
        grid.setMaxWidth("2500px");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("retail_store_id", RetailStoreDto::getName, retailStoreService.getAll());
//    filter.setFieldToComboBox("fromWhom", RetailMakingDto::getFromWhom, retailMakingService.getAll()); // разкомментить в случае необходимости выборки по ComboBox
        filter.setFieldToComboBox("company_id", CompanyDto::getName, companyService.getAll());
        filter.setFieldToIntegerField("sum");
        filter.setFieldToCheckBox("sent");
        filter.setFieldToCheckBox("print");
        filter.onSearchClick(e -> paginator.setData(retailMakingService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(retailMakingService.getAll()));
    }

    private Component isSentCheckedIcon(RetailMakingDto retailMakingDto) {
        if (retailMakingDto.getIsSent()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component isPrintedCheckedIcon(RetailMakingDto retailMakingDto) {
        if (retailMakingDto.getIsPrint()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private String formatDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime formatDateTime = LocalDateTime.parse(stringDate);
        return formatDateTime.format(formatter);
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonFilter(), numberField(), textField(), getSelect(), getStatus(), getPrint(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private H2 title() {
        H2 title = new H2("Внесения");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("Внесения фиксируют поступление наличных денег на точку продаж. " +
                        "Читать инструкцию: "),
                new Anchor("#", "Розница"));
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

    private void updateList(String search) {
        if (search.isEmpty()) {
            paginator.setData(retailMakingService.getAll());
        } else paginator.setData(retailMakingService.search(search));
    }

    public void updateList() {
        grid.setItems(retailMakingService.getAll());
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private TextField textField() {
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        textField.setClearButtonVisible(true);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(e -> updateList(textField.getValue()));
        setSizeFull();
        return textField;
    }

    private Select<String> getSelect() {
        return new SelectExt.SelectBuilder<String>()
                .item(SelectConstants.CHANGE_SELECT_ITEM)
                .defaultValue(SelectConstants.CHANGE_SELECT_ITEM)
                .width(SelectConstants.SELECT_WIDTH_130PX)
                .build();
    }

    private Select<String> getStatus() {
        return SelectConfigurer.configureStatusSelect();
    }

    private Select<String> getPrint() {
        return SelectConfigurer.configurePrintSelect();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }

}
