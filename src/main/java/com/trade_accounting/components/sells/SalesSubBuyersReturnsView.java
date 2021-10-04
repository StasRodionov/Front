package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.BuyersReturnDto;
import com.trade_accounting.services.interfaces.BuyersReturnService;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

@Slf4j
@Route(value = "buyersReturns", layout = AppView.class)
@PageTitle("Возвраты покупателей")
@SpringComponent
@UIScope
public class SalesSubBuyersReturnsView extends VerticalLayout {

    private final BuyersReturnService buyersReturnService;
    private final ContractorService contractorService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final ReturnBuyersReturnModalView returnBuyersReturnModalView;
    private final List<BuyersReturnDto> data;
    private final Grid<BuyersReturnDto> grid;
    private final GridPaginator<BuyersReturnDto> paginator;
    private final GridFilter<BuyersReturnDto> filter;

    @Autowired
    public SalesSubBuyersReturnsView(BuyersReturnService buyersReturnService,
                                     ContractorService contractorService,
                                     CompanyService companyService, ReturnBuyersReturnModalView returnBuyersReturnModalView,
                                     WarehouseService warehouseService) {
        this.buyersReturnService = buyersReturnService;
        this.warehouseService = warehouseService;
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.returnBuyersReturnModalView = returnBuyersReturnModalView;
        this.data = buyersReturnService.getAll();

        grid = new Grid<>(BuyersReturnDto.class, false);
        this.paginator = new GridPaginator<>(grid, data, 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        configureGrid();
        filter = new GridFilter<>(grid);
        configureFilter();
        add(getToolbar(), filter, grid, paginator);
    }


    private void configureGrid() {
        grid.removeAllColumns();
        grid.setItems(data);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(dto -> formatDate(dto.getDate())).setFlexGrow(7).setHeader("Время")
                .setKey("date").setId("Дата");
        grid.addColumn(dto -> warehouseService.getById(dto.getWarehouseId()).getName()).setFlexGrow(7)
                .setFlexGrow(7).setHeader("На склад").setKey("warehouseId").setId("На склад");
        grid.addColumn(dto -> contractorService.getById(dto.getContractorId()).getName())
                .setFlexGrow(7).setHeader("Контрагент").setKey("contractorId").setId("Контрагент");
        grid.addColumn(dto -> companyService.getById(dto.getCompanyId()).getName())
                .setFlexGrow(7).setHeader("Компания").setKey("companyId").setId("Компания");
        grid.addColumn("sum").setFlexGrow(7).setHeader("Сумма").setId("Сумма");
        grid.addColumn("isSent").setFlexGrow(7).setHeader("Отправлено").setId("Отправлено");
        grid.addColumn("isPrint").setFlexGrow(7).setHeader("Напечатано").setId("Напечатано");
        grid.addColumn("comment").setFlexGrow(7).setHeader("Комментарий").setId("Комментарий");
        grid.setHeight("66vh");
        grid.setMaxWidth("2500px");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        add(grid, paginator);

    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), title(), getButtonRefresh(), buttonUnit(), getButtonFilter(), getPrint(), textField(),
                numberField(), getSelect(), getStatus(), buttonSettings());
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolbar;
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.onSearchClick(e -> paginator.setData(buyersReturnService.search(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(buyersReturnService.getAll()));
    }

    private H2 title() {
        H2 title = new H2("Возвраты покупателей");
        title.setHeight("2.2em");
        return title;
    }

    public Button buttonUnit() {
        Button buttonUnit = new Button("Возврат покупателя", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickListener(event -> returnBuyersReturnModalView.open());
        return buttonUnit;
    }

    private Button getButtonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
    }

    private Button getButtonRefresh() {
        final Button buttonRefresh;
        Icon circle = new Icon(VaadinIcon.REFRESH);
        buttonRefresh = new Button();
        buttonRefresh.setIcon(circle);
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button getButtonQuestion() {
        final Button buttonQuestion = new Button();
        Icon question = new Icon(VaadinIcon.QUESTION_CIRCLE_O);
        buttonQuestion.setIcon(question);
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }



    private Select<String> getPrint() {
        Select getPrint = new Select();
        getPrint.setWidth("130px");
        getPrint.setItems("Печать", "Взаиморасчеты");
        getPrint.setValue("Печать");
        return getPrint;
    }

    private static String formatDate(String date) {
        return LocalDateTime.parse(date)
                .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
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
        final TextField textField = new TextField();
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        return textField;
    }

    private Select<String> getSelect() {
        Select<String> select = new Select<>();
        select.setItems("Изменить", "Удалить", "Массовое редактирование", "Провести", "Снять проведение");
        select.setValue("Изменить");
        select.setWidth("130px");
        return select;
    }

    private Select<String> getStatus() {
        Select<String> status = new Select<>();
        status.setItems("Статус", "Настроить");
        status.setValue("Статус");
        status.setWidth("130px");
        return status;
    }
}
