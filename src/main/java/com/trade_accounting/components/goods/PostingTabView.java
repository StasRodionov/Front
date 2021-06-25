package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.CorrectionDto;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.InvoiceProductDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.CorrectionService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
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
@PageTitle("Оприходования")
@Route(value = "positingTab", layout = AppView.class)
@UIScope
public class PostingTabView extends VerticalLayout {

    private final CorrectionService correctionService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private Notifications notifications;

    private final List<CorrectionDto> data;

    private final Grid<CorrectionDto> grid = new Grid<>(CorrectionDto.class, false);
    private GridPaginator<CorrectionDto> paginator;

    private final TextField textField = new TextField();
    private final MenuBar selectXlsTemplateButton = new MenuBar();

    @Autowired
    public PostingTabView(CorrectionService correctionService,
                          WarehouseService warehouseService,
                          CompanyService companyService,
                          Notifications notifications) {
        this.correctionService = correctionService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
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
        grid.addColumn(CorrectionDto::getDate).setKey("date").setHeader("Дата").setSortable(true);
        grid.addColumn(correctionDto -> warehouseService.getById(correctionDto.getWarehouseId())
                .getName()).setKey("warehouse").setHeader("Склад").setId("Склад");
        grid.addColumn(correctionDto -> companyService.getById(correctionDto.getCompanyId())
                .getName()).setKey("company").setHeader("Компания").setId("Компания");
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedIcon)).setKey("summ").setHeader("Сумма")
                .setId("Сумма"); //изменить реализацию, когда будет готова модель CorrectionProductDto
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedIcon)).setKey("sent").setHeader("Отправлено")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsCheckedIcon)).setKey("print").setHeader("Напечатано")
                .setId("Напечатано");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");
        grid.setHeight("66vh");
        grid.setMaxWidth("2500px");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
    }

    private List<CorrectionDto> getData() {
        return correctionService.getAll();
    }

    private void deleteSelectedCorrections() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (CorrectionDto correctionDto : grid.getSelectedItems()) {
                correctionService.deleteById(correctionDto.getId());
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

    private Component getIsCheckedIcon(CorrectionDto correctionDto) {
        if (correctionDto.getIsSent() || correctionDto.getIsPrint()) {
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

    protected String getTotalPrice() {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        return String.format("%.2f", totalPrice);
    }

    private H2 getTextOrder() {
        final H2 textOrder = new H2("Оприходования");
        textOrder.setHeight("2.2em");
        return textOrder;
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
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
        Button buttonUnit = new Button("Оприходование", new Icon(VaadinIcon.PLUS_CIRCLE));
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
                deleteSelectedCorrections();
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
