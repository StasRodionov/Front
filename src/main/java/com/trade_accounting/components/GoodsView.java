package com.trade_accounting.components;


import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.models.dto.ProductGroupDto;
import com.trade_accounting.services.interfaces.ProductGroupService;
import com.trade_accounting.services.interfaces.ProductService;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.klaudeta.PaginatedGrid;

import java.util.List;
import java.util.stream.Collectors;


@Route(value = "good", layout = AppView.class)
@PageTitle("Товары и услуги")
public class GoodsView extends VerticalLayout {

    private final ProductService productService;
    private final ProductGroupService productGroupService;

    public GoodsView(ProductService productService, ProductGroupService productGroupService) {
        this.productService = productService;
        this.productGroupService = productGroupService;
        add(upperLayout(), middleLayout());
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_SMALL);
        return buttonQuestion;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonPlusGoods() {
        Button button = new Button("Товар", new Icon(VaadinIcon.PLUS_CIRCLE));
        button.addThemeVariants(ButtonVariant.LUMO_SMALL);
        return button;
    }

    private Button buttonPlusService() {
        Button button = new Button("Услуга", new Icon(VaadinIcon.PLUS_CIRCLE));
        button.addThemeVariants(ButtonVariant.LUMO_SMALL);
        return button;
    }

    private Button buttonPlusSet() {
        Button button = new Button("Комплект", new Icon(VaadinIcon.PLUS_CIRCLE));
        button.addThemeVariants(ButtonVariant.LUMO_SMALL);

        return button;
    }

    private Button buttonPlusGroup() {
        Button button = new Button("Группа", new Icon(VaadinIcon.PLUS_CIRCLE));
        button.addThemeVariants(ButtonVariant.LUMO_SMALL);

        return button;
    }

    private Button buttonFilter() {
        Button button = new Button("Фильтр");
        button.addThemeVariants(ButtonVariant.LUMO_SMALL);

        return button;
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private TextField text() {
        TextField text = new TextField();
        text.setPlaceholder("Наименование, код или артикул");
        text.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        text.setWidth("300px");
        return text;
    }


    private H2 title() {
        return new H2("Товары и услуги");
    }

    private NumberField numberField() {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect() {
        Select<String> valueSelect = new Select<>();
        valueSelect.setItems("Изменить");
        valueSelect.setValue("Изменить");
        valueSelect.setWidth("100px");
        return valueSelect;
    }

    private Select<String> valueSelectPrint() {
        Select<String> valueSelect = new Select<>();
        valueSelect.setItems("Печать");
        valueSelect.setValue("Печать");
        valueSelect.setWidth("100px");
        return valueSelect;
    }

    private Select<String> valueSelectImport() {
        Select<String> valueSelect = new Select<>();
        valueSelect.setItems("Импорт");
        valueSelect.setValue("Импорт");
        valueSelect.setWidth("100px");
        return valueSelect;
    }

    private Select<String> valueSelectExport() {
        Select<String> valueSelect = new Select<>();
        valueSelect.setItems("Экспорт");
        valueSelect.setValue("Экспорт");
        valueSelect.setWidth("100px");
        return valueSelect;
    }

    private SplitLayout middleLayout() {
        SplitLayout layout = new SplitLayout();

        layout.addToPrimary(accordion());
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(grid());
        layout.addToSecondary(verticalLayout);
        layout.setWidth("100%");

        return layout;
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upperLayout = new HorizontalLayout();
        upperLayout.add(buttonQuestion(), title(), buttonRefresh(), buttonPlusGoods(), buttonPlusService(),
                buttonPlusSet(), buttonPlusGroup(),
                buttonFilter(), text(), numberField(), valueSelect(), valueSelectPrint(),
                valueSelectImport(),
                valueSelectExport(), buttonSettings());
        upperLayout.setSpacing(false);
        upperLayout.setMargin(true);
        upperLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upperLayout;
    }

    private Grid<ProductDto> grid() {
        PaginatedGrid<ProductDto> grid = new PaginatedGrid<>(ProductDto.class);

        grid.setItems(productService.getAll());

        grid.setColumns("name", "description", "weight", "volume",
                "purchasePrice");

        grid.getColumnByKey("name").setHeader("Наименование");
        grid.getColumnByKey("description").setHeader("Артикул");
        grid.getColumnByKey("weight").setHeader("Вес");
        grid.getColumnByKey("volume").setHeader("Объем");
        grid.getColumnByKey("purchasePrice").setHeader("Закупочная цена");

        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setPageSize(10);

        return grid;
    }


    List<ProductGroupDto> filterList(Long id) {
        return productGroupService.getAll()
                .stream().filter(x -> x.getParentId() == id)
                .collect(Collectors.toList());
    }

    private Accordion accordion() {
        Accordion accordion = new Accordion();

        for (ProductGroupDto pG : filterList(null)
        ) {
            Accordion subAccordion = new Accordion();
            for (ProductGroupDto subPG : filterList(pG.getId())
            ) {
                Accordion sub2Accordion = new Accordion();
                for (ProductGroupDto sub2PG : filterList(subPG.getId())
                ) {
                    Accordion sub3Accordion = new Accordion();
                    for (ProductGroupDto sub3PG : filterList(sub2PG.getId())
                    ) {
                        sub3Accordion.add(sub3PG.getName(), null)
                                .addThemeVariants(DetailsVariant.SMALL);
                    }
                    sub2Accordion.add(sub2PG.getName(), sub3Accordion)
                            .addThemeVariants(DetailsVariant.FILLED);
                }
                subAccordion.add(subPG.getName(), sub2Accordion)
                        .addThemeVariants(DetailsVariant.REVERSE);
            }
            accordion.add(pG.getName(), subAccordion);
        }
        return accordion;
    }
}
