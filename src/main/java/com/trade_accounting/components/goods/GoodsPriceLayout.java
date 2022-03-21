package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.company.PriceListDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.PriceListService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
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
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Route(value = "goods_price_layout", layout = AppView.class)
@PageTitle("Прайс-лист")
@SpringComponent
@UIScope
public class GoodsPriceLayout extends VerticalLayout implements AfterNavigationObserver {

    private final TextField textField = new TextField();
    private final MenuBar selectXlsTemplateButton = new MenuBar();
    private final PriceListService priceListService;
    private final CompanyService companyService;
    private final PriceModalWindow modalWindow;
    private final PriceModalEditWindow modalEditWindowWindow;
    private final List<PriceListDto> data;
    private final Grid<PriceListDto> grid = new Grid<>(PriceListDto.class, false);
    private final GridPaginator<PriceListDto> paginator;
    private final Notifications notifications;

    private final TextField filterNew = new TextField();

    @Autowired
    public GoodsPriceLayout(PriceListService priceListService, CompanyService companyService,
                            PriceModalWindow modalWindow, PriceModalEditWindow modalEditWindowWindow, Notifications notifications) {
        this.priceListService = priceListService;
        this.companyService = companyService;
        this.modalWindow = modalWindow;
        this.modalEditWindowWindow = modalEditWindowWindow;
        this.notifications = notifications;
        this.data = getData(null);
        paginator = new GridPaginator<>(grid, data, 50);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        setSizeFull();
        configureGrid();
        add(configureActions(), grid, paginator);
    }

    private HorizontalLayout configureActions() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(buttonQuestion(), getTextOrder(), buttonRefresh(), buttonUnit(),
                buttonFilter(), text(), numberField(), valueSelect(), valueStatus(),
                valuePrint(), buttonSettings(),getFilterButton(), filter(), selectXlsTemplateButton);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    private Component filter() {
        Label label = new Label("Фильтр");
        filterNew.setWidth("400px");
        return new HorizontalLayout(label, filterNew);
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
//        grid.addColumn("id").setHeader("№").setId("№");

        grid.addColumn(PriceListDto::getNumber).setKey("number").setHeader("№").setSortable(true).setId("№");
        grid.addColumn(PriceListDto::getTime).setKey("date").setHeader("Дата").setSortable(true).setId("Дата");
        grid.addColumn(priceListDto -> companyService.getById(priceListDto.getCompanyId())
                .getName()).setKey("company").setHeader("Организация").setId("Организация");
        grid.addColumn(new ComponentRenderer<>(this::getIsSentIcon)).setKey("sent").setHeader("Отправлено")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsPrintIcon)).setKey("print").setHeader("Напечатано")
                .setId("Напечатано");
        grid.addColumn("commentary").setHeader("Комментарий").setId("Комментарий");
        grid.addItemDoubleClickListener(event -> {
            PriceListDto priceListDto = event.getItem();
            PriceModalEditWindow view = new PriceModalEditWindow(
                    priceListService,
                    companyService
            );
            view.setPriceListEdit(priceListDto);
            view.open();
        });
        grid.setHeight("66vh");
        grid.setMaxWidth("2500px");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
    }

    private List<PriceListDto> getData(String string) {
        if (string!=null){
            return priceListService.newFilter(string);
        }else {
            return priceListService.getAll();
        }
    }

    private Button getFilterButton() {
        Button filterButton = new Button("Отфильтровать", event -> {
            paginator.setData(priceListService.newFilter(filterNew.getValue()));
        });
        return filterButton;
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
        grid.setItems(priceListService.getAll());
    }

    private Button buttonUnit() {
        Button button = new Button("Прайс-лист", new Icon(VaadinIcon.PLUS_CIRCLE));
        button.addClickListener(e -> modalWindow.open());
        modalWindow.clearAll();
        modalWindow.setParentLocation("goods_price_layout");
        button.getUI().ifPresent(ui -> ui.navigate("goods/priceList-create"));
        return button;
    }

    private Button buttonFilter() {
        return new Button("Фильтр");
    }

    private TextField text() {
        textField.setWidth("300px");
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setClearButtonVisible(true);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
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
        return SelectConfigurer.configureDeleteSelect(()->{
            deleteSelectedPriceList();
            grid.deselectAll();
            paginator.setData(getData(null));
        });
    }

    private void deleteSelectedPriceList() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (PriceListDto priceListDto : grid.getSelectedItems()) {
                priceListService.deleteById(priceListDto.getId());
                notifications.infoNotification("Выбранные прайс-листы успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные прайс-листы");
        }
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
        if (priceListDto.getSent()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component getIsPrintIcon(PriceListDto priceListDto) {
        if (priceListDto.getPrinted()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
