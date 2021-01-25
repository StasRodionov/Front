package com.trade_accounting.components.profile;

import com.trade_accounting.components.AppView;
import com.trade_accounting.models.dto.WarehouseDto;
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
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "warehouse", layout = AppView.class)
@PageTitle("Склады")
public class WareHouseView extends VerticalLayout {

    private final WarehouseService warehouseService;

    @Autowired
    public WareHouseView(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
        add(toolsUp(), grid(), toolsDown());
    }

    private Button buttonQuestion(){
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private H2 title(){
        H2 title = new H2("Склады");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonRefresh(){
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private  Button buttonWareHouse(){
        return new Button("Склад", new Icon(VaadinIcon.PLUS_CIRCLE));
    }

    private Button buttonFilter(){
        return new Button("Фильтр");
    }

    private TextField text(){
        TextField text = new TextField();
        text.setPlaceholder("Наименование или код");
        text.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        text.setWidth("300px");
        return text;
    }

    private NumberField numberField(){
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect(){
        Select<String> valueSelect = new Select<>();
        valueSelect.setItems("Изменить");
        valueSelect.setValue("Изменить");
        valueSelect.setWidth("130px");
        return valueSelect;
    }

    private Button buttonSettings(){
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private HorizontalLayout toolsUp(){
        HorizontalLayout toolsUp = new HorizontalLayout();
        toolsUp.add(buttonQuestion(),title(), buttonRefresh(), buttonWareHouse(), buttonFilter(), text(), numberField(),
                valueSelect(), buttonSettings());
        toolsUp.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return toolsUp;
    }

    private TextField textField(){
        TextField textField = new TextField("", "1-1 из 1");
        textField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        return textField;
    }

    private Button doubleLeft(){
        return new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT));
    }

    private Button left(){
        return new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    }

    private Button doubleRight(){
        return new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT));
    }

    private Button right(){
        return new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    }

    private HorizontalLayout toolsDown(){
        HorizontalLayout toolsDown = new HorizontalLayout();
        toolsDown.add(doubleLeft(),left(),textField(),right(),doubleRight());
        return toolsDown;
    }

    private Grid<WarehouseDto> grid(){
        Grid<WarehouseDto> grid = new Grid<>(WarehouseDto.class);
        grid.setItems(warehouseService.getAll());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.setColumns("id","name","sortNumber","address","commentToAddress","comment");
        grid.getColumnByKey("name").setHeader("Имя");
        grid.getColumnByKey("sortNumber").setHeader("Сортировочный номер");
        grid.getColumnByKey("address").setHeader("Адрес");
        grid.getColumnByKey("commentToAddress").setHeader("Комментарий к адресу");
        grid.getColumnByKey("comment").setHeader("Комментарий");
        grid.setHeight("66vh");
        return grid;
    }
}
