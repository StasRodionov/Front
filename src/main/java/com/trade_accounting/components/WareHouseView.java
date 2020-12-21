package com.trade_accounting.components;

import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@Route(value = "warehouse", layout = AppView.class)
@PageTitle("Склады")
public class WareHouseView extends VerticalLayout {

    private final WarehouseService warehouseService;

    private Grid<WarehouseDto> grid = new Grid<>(WarehouseDto.class);

//    private final Button button = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
//    private final Button button2 = new Button(new Icon(VaadinIcon.REFRESH));
//    private final Button button3 = new Button("Склад");
//    private final Button button4 = new Button("Фильтр");
//    private final Button button5 = new Button(new TextField());
    //private final Button button6 = new Button("Фильтр");

    private final HorizontalLayout tools = new HorizontalLayout();

    @Autowired
    public WareHouseView(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;

        //Icon logo = new Icon(VaadinIcon.QUESTION_CIRCLE_O);
        Button logo = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        Button button = new Button("Склады");
        //Icon logo2 = new Icon(VaadinIcon.REFRESH);
        Button logo2 = new Button(new Icon(VaadinIcon.REFRESH));
        Button button1 = new Button("Склад");
        button1.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
        Button button2 = new Button("Фильтр");
        TextField placeholderField = new TextField();
        placeholderField.setPlaceholder("Наименование или код");
        Select<String> valueSelect = new Select<>();
        valueSelect.setItems("Изменить");
        Button logo4 = new Button(new Icon(VaadinIcon.COG_O));

        tools.add(logo,button, logo2, button1, button2, placeholderField, valueSelect, logo4);
        add(tools, grid);

//        add(placeholderField);

        grid.addColumn(WarehouseDto::getName).setHeader("Наименование");
        grid.addColumn(WarehouseDto::getAddress).setHeader("Адрес");
//        List<WarehouseDto > warehouseDtoList = null;
//        for(int i=0; i<warehouseService.getAll().size(); i++){
//            warehouseDtoList.add(warehouseService.getAll().get(i).getName());
//        }
//        grid.setItems( warehouseDtoList);

//        grid.setItems(warehouseService.getAll());

    }

}
