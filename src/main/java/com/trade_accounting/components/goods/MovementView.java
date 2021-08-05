package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.MovementDto;
import com.trade_accounting.models.dto.MovementProductDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.MovementProductService;
import com.trade_accounting.services.interfaces.MovementService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringComponent
@PageTitle("Перемещения")
@Route(value = "movementView", layout = AppView.class)
@UIScope
public class MovementView extends VerticalLayout {

    private final MovementService movementService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final MovementProductService movementProductService;
    private Notifications notifications;

    private final List<MovementDto> data;

    private final Grid<MovementDto> grid = new Grid<>(MovementDto.class, false);
    private GridPaginator<MovementDto> paginator;

    private final TextField textField = new TextField();
    private final MenuBar selectXlsTemplateButton = new MenuBar();

    @Autowired
    public MovementView(MovementService movementService,
                        WarehouseService warehouseService,
                        CompanyService companyService,
                        MovementProductService movementProductService,
                        Notifications notifications) {
        this.movementService = movementService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.movementProductService = movementProductService;
        this.notifications = notifications;
        this.data = getData();
        paginator = new GridPaginator<>(grid, data, 50);
        setSizeFull();
        configureGrid();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(configureActions(), grid, paginator);
    }

    private void configureGrid() {
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(MovementDto::getDate).setKey("date").setHeader("Дата").setSortable(true);
        grid.addColumn(movementDto -> warehouseService.getById(movementDto.getWarehouseFromId())
                .getName()).setKey("warehouseFrom").setHeader("Со Склада").setId("Со Склада");
        grid.addColumn(movementDto -> warehouseService.getById(movementDto.getWarehouseToId())
                .getName()).setKey("warehouseTo").setHeader("На Склад").setId("На Склад");
        grid.addColumn(movementDto -> companyService.getById(movementDto.getCompanyId())
                .getName()).setKey("company").setHeader("Организация").setId("Организация");
        grid.addColumn(this::getTotalPrice).setHeader("Сумма").setSortable(true);
        grid.addColumn(new ComponentRenderer<>(this::getIsSentIcon)).setKey("sent").setHeader("Отправлено")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsPrintIcon)).setKey("print").setHeader("Напечатано")
                .setId("Напечатано");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");
        grid.setHeight("66vh");
        grid.setMaxWidth("2500px");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
    }

    private List<MovementDto> getData() {
        return movementService.getAll();
    }

    private void deleteSelectedMovements() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (MovementDto movementDto : grid.getSelectedItems()) {
                movementService.deleteById(movementDto.getId());
                notifications.infoNotification("Выбранные заказы успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные заказы");
        }
    }

    private HorizontalLayout configureActions() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), getTextOrder(), buttonRefresh(), buttonUnit(), buttonFilter(),
                numberField(), valueSelect(), valueStatus(), valuePrint(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upper;
    }

    private Component getIsSentIcon(MovementDto movementDto) {
        if (movementDto.getIsSent()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component getIsPrintIcon(MovementDto movementDto) {
        if (movementDto.getIsPrint()) {
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

    protected String getTotalPrice(MovementDto movementDto) {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
//        for (Long id : movementDto.getMovementProductIds()) {
        for (Long id : movementDto.getMovementProductsIds()) {
            MovementProductDto movementProductDto = movementProductService.getById(id);
            totalPrice = totalPrice.add(movementProductDto.getAmount()
                    .multiply(movementProductDto.getPrice()));
        }
        return String.format("%.2f", totalPrice);
    }

    private H2 getTextOrder() {
        final H2 textOrder = new H2("Перемещения");
        textOrder.setHeight("2.2em");
        return textOrder;
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Dialog dialog = new Dialog();
        Button cancelButton = new Button("Закрыть", event -> {
            dialog.close();
        });
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponentAsFirst(cancelButton);
        dialog.add(new Text("Перемещение  - добавить текст"));
        dialog.setWidth("450px");
        dialog.setHeight("250px");
        buttonQuestion.addClickListener(event -> dialog.open());
        Shortcuts.addShortcutListener(dialog, () -> {
            dialog.close();
        }, Key.ESCAPE);
        dialog.add(new Div(cancelButton));
        return buttonQuestion;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(ev -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    public void updateList() {
    }

    private Button buttonUnit() {
        Button buttonUnit = new Button("Перемещение", new Icon(VaadinIcon.PLUS_CIRCLE));
        return buttonUnit;
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
                deleteSelectedMovements();
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
