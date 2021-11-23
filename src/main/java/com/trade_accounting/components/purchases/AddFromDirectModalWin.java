package com.trade_accounting.components.sells;


import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.AcceptanceDto;
import com.trade_accounting.models.dto.AcceptanceProductionDto;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.models.dto.RemainDto;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.RemainService;
import com.trade_accounting.services.interfaces.UnitService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringComponent
@PageTitle("Выбор товара из списка")
@UIScope

public class AddFromDirectModalWin extends Dialog {
    private final ProductService productService;
    private final RemainService remainService;
    private final UnitService unitService;
    private final Notifications notifications;

    private final Grid<ProductDto> grid = new Grid<>(ProductDto.class, false);
    private final GridPaginator<ProductDto> paginator;
    private List<ProductDto> data;
    private final TextField textField = new TextField();
    private final MenuBar selectXlsTemplateButton = new MenuBar();
    private final GridFilter<ProductDto> filter;
    private String typeOfRemain = "111";
    List<AcceptanceProductionDto> acceptanceProduction;
    Map<ProductDto, String> updateRemainForVendor = new HashMap<>();
//    ReturnBuyersReturnModalView returnBuyersReturnModalView;

    public AddFromDirectModalWin(RemainService remainService,
                                 UnitService unitService,
                                 Notifications notifications,
                                 ProductService productService) {
        this.remainService = remainService;
        this.unitService = unitService;
        this.notifications = notifications;
        this.productService = productService;
        data = getData();
        paginator = new GridPaginator<>(grid, data, 50);
        setSizeFull();
        configureGrid();
        this.filter = new GridFilter<>(grid);
//        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(configureActions(), filter, grid, paginator);
    }

    private void configureGrid() {
        grid.setItems(productService.getAll());
        grid.addColumn(inPrDto -> inPrDto.getName()).setKey("name").setHeader("Наименование").setId("Наименование");
        grid.addColumn(inPrDto -> inPrDto.getVolume()).setKey("kount").setHeader("Количество").setId("Количество");
//        grid.addColumn(inPrDto -> inPrDto.getVolume()).setKey("kount").setHeader("Количество").setId("Количество");
        grid.addColumn(inPrDto -> inPrDto.getVolume()).setKey("available").setHeader("Остаток").setId("Остаток");

//        grid.addColumn(inPrDto -> inPrDto.getReserve()).setKey("reserve").setHeader("Резерв").setId("Резерв");
//        grid.addColumn(inPrDto -> inPrDto.getExpectation()).setKey("expectation").setHeader("Ожидание").setId("Ожидание");
//        grid.addColumn(inPrDto -> inPrDto.getVolume()).setKey("available").setHeader("Доступно").setId("Доступно"); //Проверить!
        //Код, артикул, ед.изм.
//        grid.addColumn(inPrDto -> inPrDto.getId()).setKey("salesCost").setHeader("Артикул").setId("Артикул");
//        grid.addColumn(inPrDto -> inPrDto.get.getById(remainDto.getUnitId()).getFullName()).setKey("unitId").setHeader("Единицы измерения").setId("Единицы измерения");
        grid.addColumn(inPrDto -> inPrDto.getPurchasePrice()).setKey("salesCost").setHeader("Цена на продажу").setId("Цена на продажу");

        grid.setHeight("66vh");
        grid.setMaxWidth("100%");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        grid.addItemClickListener(event -> {
            ProductDto productDto = event.getItem();

//            CountModalWin view = new CountModalWin();
//
//
//            AcceptanceProductionDto acceptanceProductionDto = new AcceptanceProductionDto();
//            view.setReturnEdit(productDto);
//            acceptanceProduction.add(acceptanceProductionDto);
//            view.open();

        });
    }

    private List<ProductDto> getData() {
        return new ArrayList<>(productService.getAll());
    }

    private HorizontalLayout configureActions() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), getTextOrder(),
                numberField(), buttonSettings());
//        upper.setDefaultVerticalComponentAlignment(GroupLayout.Alignment.CENTER);
        return upper;
    }

    private H2 getTextOrder() {
        final H2 textOrder = new H2("Товары");
        textOrder.setHeight("2.2em");
        return textOrder;
    }

    private HorizontalLayout getTollBar() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(buttonQuestion(), getTextOrder(),
                text(), numberField(), buttonSettings(), selectXlsTemplateButton);
//        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Dialog dialog = new Dialog();
        Button cancelButton = new Button("Закрыть", event -> dialog.close());
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponentAsFirst(cancelButton);
        dialog.add(new Text("Продекты  - В этом окне Вы можете добавить необходимые продукты из прайс-листа"));
        dialog.setWidth("450px");
        dialog.setHeight("250px");
        buttonQuestion.addClickListener(event -> dialog.open());
        Shortcuts.addShortcutListener(dialog, dialog::close, Key.ESCAPE);
        dialog.add(new Div(cancelButton));
        return buttonQuestion;
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

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

}
