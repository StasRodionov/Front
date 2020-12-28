package com.trade_accounting.components;

import com.trade_accounting.models.dto.UnitDto;
import com.trade_accounting.services.interfaces.UnitService;
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

@Route(value = "unit", layout = AppView.class)
@PageTitle("Единицы измерения")
public class UnitView extends VerticalLayout {

    private final UnitService unitService;

    private Grid<UnitDto> grid = new Grid<>(UnitDto.class);

    private final HorizontalLayout layout = new HorizontalLayout();

    private final HorizontalLayout layout1 = new HorizontalLayout();

    public UnitView(UnitService unitService) {
        this.unitService = unitService;

        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        H2 title = new H2("Единицы измерения");
        title.setHeight("2.2em");

        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);

        Button buttonUnit = new Button("Единица измерения", new Icon(VaadinIcon.PLUS_CIRCLE));

        Button buttonFilter = new Button("Фильтр");

        TextField text = new TextField();
        text.setPlaceholder("Наименование или код");
        text.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        text.setWidth("300px");

        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");

        Select<String> valueSelect = new Select<>();
        valueSelect.setItems("Изменить");
        valueSelect.setValue("Изменить");
        valueSelect.setWidth("130px");

        Button buttonSettings = new Button(new Icon(VaadinIcon.COG_O));

        layout.add(buttonQuestion,title, buttonRefresh, buttonUnit, buttonFilter, text, numberField,
                valueSelect, buttonSettings);
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        TextField textField = new TextField("", "1-1 из 1");
        textField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);

        layout1.add(new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT)),
                new Button(new Icon(VaadinIcon.ANGLE_LEFT)),
                textField,
                new Button(new Icon(VaadinIcon.ANGLE_RIGHT)),
                new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT)));

        grid.setItems(unitService.getAll());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        add(layout, grid, layout1);
    }
}
