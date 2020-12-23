package com.trade_accounting.components;

import com.trade_accounting.models.dto.UnitDto;
import com.trade_accounting.services.interfaces.UnitService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Route(value = "unit", layout = AppView.class)
@PageTitle("Единицы измерения")
public class UnitView extends Div {

    private final UnitService unitService;
    private Grid<UnitDto> grid = new Grid<>(UnitDto.class);
    private List<UnitDto> unitDtoList = new ArrayList<>();

    public UnitView(UnitService unitService){
        this.unitService = unitService;

        HorizontalLayout layout = new HorizontalLayout();

        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));

        Label label = new Label("Единицы измерения");

        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));

        Button buttonAdd = new Button("Единица измерения", new Icon(VaadinIcon.PLUS));

        Button buttonFilter = new Button("Фильтр");

        TextField titleField = new TextField();
        titleField.setPlaceholder("Наименование");

        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");

        Button buttonEdit = new Button("Изменить", new Icon(VaadinIcon.ANGLE_DOWN));

        grid.setItems(unitService.getAll());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        layout.add(buttonQuestion, label, buttonRefresh, buttonAdd, buttonFilter, titleField, numberField, buttonEdit);
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        layout.setPadding(false);
        layout.setMargin(true);
        layout.setSpacing(true);
        add(layout);


        add(grid);
    }

}


