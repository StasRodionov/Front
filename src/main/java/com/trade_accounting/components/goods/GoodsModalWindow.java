package com.trade_accounting.components.goods;

import com.trade_accounting.models.dto.ProductDto;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class GoodsModalWindow extends Dialog {
    private final Map<String, Component> map = new LinkedHashMap<>();


    private final String labelWidth = "100px";
    private final String fieldWidth = "400px";

    public GoodsModalWindow() {
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        add(getHeader());

        map.put("Наиминование", new TextField());
        map.put("Вес", new NumberField());

        map.forEach((key, value) -> {
            if (value instanceof TextField) {
                add(getHorizontalLayout(key, (TextField) value));
            }
            if (value instanceof NumberField) {
                add(getHorizontalLayout(key, (NumberField) value));
            }
        });


       // add(getHorizontalLayout("Наиминование", productNameTextField));
       // add(getHorizontalLayout("Вес", weightTextField));
    }

    private HorizontalLayout getHeader() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        H2 title = new H2("Добавление товара");
        title.setHeight("2.2em");
        title.setWidth("345px");
        horizontalLayout.add(title);
        horizontalLayout.add(new Button("Добавить", event -> {
            ProductDto productDto = new ProductDto();
           // productDto.setName(map.get("Наиминование"));
           // productDto.setWeight(BigDecimal.valueOf(weightTextField.getValue()));
            System.out.println(productDto);
        }));
        horizontalLayout.add(new Button("Закрыть", event -> close()));
        return horizontalLayout;
    }

    private HorizontalLayout getHorizontalLayout(String labelText, TextField textField){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label(labelText);
        label.setWidth(labelWidth);
        textField.setWidth(fieldWidth);
        horizontalLayout.add(label, textField);
        return horizontalLayout;
    }

    private HorizontalLayout getHorizontalLayout(String labelText, NumberField numberField){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label(labelText);
        label.setWidth(labelWidth);
        numberField.setWidth(fieldWidth);
        horizontalLayout.add(label, numberField);
        return horizontalLayout;
    }
}
