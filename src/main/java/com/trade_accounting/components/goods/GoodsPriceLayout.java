package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Route(value = "goods_price_layout", layout = AppView.class)
@PageTitle("Прайс-лист")
@SpringComponent
@UIScope
public class GoodsPriceLayout extends VerticalLayout {

    private final TextField textField = new TextField();
    private final MenuBar selectXlsTemplateButton = new MenuBar();
    private final CompanyService companyService;
    private final PriceModalWindow modalWindow;
    PriceListService priceListService;
    private final Grid<PriceListDto> grid = new Grid<>(PriceListDto.class, false);

    @Autowired
    public GoodsPriceLayout( CompanyService companyService, PriceModalWindow modalWindow) {
        this.companyService = companyService;
        this.modalWindow = modalWindow;
        add(configureActions(),grid);
        configureGrid();
        setSizeFull();
    }

    private HorizontalLayout configureActions() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(buttonQuestion(), getTextOrder(), buttonRefresh(), buttonUnit(),
                buttonFilter(), text(), numberField(), valueSelect(), valueStatus(),
                valuePrint(), buttonSettings(), selectXlsTemplateButton);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);return horizontalLayout;
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(PriceListDto::getTime).setKey("date").setHeader("Время").setSortable(true).setId("Дата");
        grid.addColumn(priceListDto -> companyService.getById(priceListDto.getCompanyDtoId())
                .getName()).setKey("company").setHeader("Организация").setId("Организация");
        grid.addColumn(new ComponentRenderer<>(this::getIsSentIcon)).setKey("sent").setHeader("Отправлено")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsPrintIcon)).setKey("print").setHeader("Напечатано")
                .setId("Напечатано");
        grid.addColumn("commentary").setHeader("Комментарий").setId("Комментарий");
        grid.addItemDoubleClickListener(event -> {
            PriceListDto priceListDto = event.getItem();
            PriceModalWindow view = new PriceModalWindow(
                    priceListService,
                    companyService
            );
            view.setPostingEdit(priceListDto);
            view.open();
        });
        grid.setHeight("66vh");
        grid.setMaxWidth("2500px");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion("Добавьте описание");
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(ev -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    public void updateList() {}

    private Button buttonUnit() {

        Button button = new Button("Прайс-лист", new Icon(VaadinIcon.PLUS_CIRCLE));
        button.addClickListener(e -> modalWindow.open());
        updateList();
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
        return SelectConfigurer.configureDeleteSelect();
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
}
