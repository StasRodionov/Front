package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.finance.LossDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.finance.LossProductService;
import com.trade_accounting.services.interfaces.finance.LossService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringComponent
@PageTitle("Списания")
@Route(value = "lossView", layout = AppView.class)
@UIScope
public class LossView extends VerticalLayout {
    private final LossService lossService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final LossProductService lossProductService;
    private final Notifications notifications;

    private final Grid<LossDto> grid = new Grid<>(LossDto.class, false);
    private final GridPaginator<LossDto> paginator;

    private final TextField textField = new TextField();
    private final MenuBar selectXlsTemplateButton = new MenuBar();
    private List<LossDto> data;
    private final LossCreateView lossCreateView;
    private final LossModalWindow lossModalWindow;

    @Autowired
    public LossView(LossService lossService,
                    WarehouseService warehouseService,
                    CompanyService companyService,
                    LossProductService lossProductService,
                    Notifications notifications,
                    @Lazy LossCreateView lossCreateView,
                    LossModalWindow lossModalWindow) {
        this.lossService = lossService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.lossProductService = lossProductService;
        this.notifications = notifications;
        this.lossCreateView = lossCreateView;
        this.lossModalWindow = lossModalWindow;
        data = getData();
        paginator = new GridPaginator<>(grid, data, 50);
        setSizeFull();
        configureGrid();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(configureActions(), grid, paginator);
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(LossDto::getDate).setKey("date").setHeader("Дата").setSortable(true);
        grid.addColumn(lossDto -> warehouseService.getById(lossDto.getWarehouseId())
                .getName()).setKey("warehouseFrom").setHeader("Со Склада").setId("Со Склада");
        grid.addColumn(lossDto -> companyService.getById(lossDto.getCompanyId())
                .getName()).setKey("company").setHeader("Организация").setId("Организация");
        grid.addColumn(this::getTotalPrice).setHeader("Сумма").setSortable(true);
        grid.addColumn(new ComponentRenderer<>(this::getIsSentIcon)).setKey("sent").setHeader("Отправлено")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsPrintIcon)).setKey("print").setHeader("Напечатано")
                .setId("Напечатано");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");
        grid.setHeight("66vh");
        grid.setMaxWidth("100%");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.getColumns().forEach(column -> column.setAutoWidth(true));

//        grid.addItemDoubleClickListener(e-> {
//            LossDto dto = e.getItem();
//            modalWindow.setLossEdit(dto);
//            modalWindow.open();
//        });
    }

    private List<LossDto> getData() {
        return new ArrayList<>(lossService.getAll());
    }

    private void deleteSelectedLoss() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (LossDto lossDto : grid.getSelectedItems()) {
                lossService.deleteById(lossDto.getId());
                notifications.infoNotification("Выбранные списания успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные списания");
        }
    }

    private HorizontalLayout configureActions() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), getTextOrder(), buttonRefresh(), buttonUnit(), buttonFilter(),
                numberField(), valueSelect(), valueStatus(), valuePrint(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upper;
    }

    private Component getIsSentIcon(LossDto lossDto) {
        if (lossDto.getIsSent()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component getIsPrintIcon(LossDto lossDto) {
        if (lossDto.getIsPrint()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private HorizontalLayout getTollBar() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(buttonQuestion(), getTextOrder(), buttonRefresh(), buttonUnit(),
                buttonFilter(), text(), numberField(), valueSelect(), valueStatus(),
                valuePrint(), buttonSettings(), selectXlsTemplateButton);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    protected String getTotalPrice(LossDto lossDto) {
        BigDecimal totalPrice = lossDto.getLossProductsIds().stream()
                .map(lossProductService::getById)
                .map(lossProductDto -> lossProductDto.getPrice().multiply(lossProductDto.getAmount()))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

        return String.format("%.2f", totalPrice);
    }

    private H2 getTextOrder() {
        final H2 textOrder = new H2("Списания");
        textOrder.setHeight("2.2em");
        return textOrder;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion("Списание  - добавить текст");
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(ev -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    public void updateList() {
        paginator.setData(getData(),false);
    }


    private Button buttonUnit() {
        Button button = new Button("Списание", new Icon(VaadinIcon.PLUS_CIRCLE));
        button.addClickListener(e -> {
            lossCreateView.clearForm();
            lossCreateView.setParentLocation("lossView");
            button.getUI().ifPresent(ui -> ui.navigate("goods/loss-create"));
        });
        updateList();
        return button;
    }

    private Button buttonFilter() {

        Button buttonFilter = new Button("Фильтр");
        return buttonFilter;
    }

    private TextField text() {
        textField.setWidth("300px");
        textField.setPlaceholder("Наименование или комментарий");
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
        Select<String> select = new Select<>();
        List<String> listItems = new ArrayList<>();
        listItems.add("Изменить");
        listItems.add("Удалить");
        select.setItems(listItems);
        select.setValue("Изменить");
        select.setWidth("130px");
        select.addValueChangeListener(event -> {
            if (select.getValue().equals("Удалить")) {
                deleteSelectedLoss();
                grid.deselectAll();
                select.setValue("Изменить");
                paginator.setData(getData());
            }
        });
        return select;
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private Select<String> valueStatus() {
        Select<String> status = new Select<>();
        status.setItems("Статус");
        status.setValue("Статус");
        status.setWidth("110px");
        return status;
    }

    private Select<String> valuePrint() {
        Select<String> print = new Select<>();
        print.setItems("Печать", "Добавить шаблон");
        print.setValue("Печать");
        print.setWidth("110px");
        return print;
    }

}
