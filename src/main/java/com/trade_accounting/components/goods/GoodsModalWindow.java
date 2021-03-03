package com.trade_accounting.components.goods;

import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.models.dto.UnitDto;
import com.trade_accounting.services.interfaces.UnitService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@SpringComponent
@UIScope
public class GoodsModalWindow extends Dialog {


    private final UnitService unitService;

    private final TextField nameTextField = new TextField();
    private final TextField descriptionField = new TextField();

    private final NumberField weightNumberField = new NumberField();
    private final NumberField volumeNumberField = new NumberField();
    private final NumberField purchasePriceNumberField = new NumberField();

    private final Select<UnitDto> unitDtoSelect = new Select<>();

    private final String labelWidth = "100px";
    private final String fieldWidth = "400px";

    @Autowired
    public GoodsModalWindow(UnitService unitService) {
        this.unitService = unitService;

        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        add(getHeader());


        add(getHorizontalLayout("Наиминование", nameTextField));
        add(getHorizontalLayout("Вес", weightNumberField));
        add(getHorizontalLayout("Объем", volumeNumberField));
        add(getHorizontalLayout("Закупочная цена", purchasePriceNumberField));
        add(getHorizontalLayout("Описание", descriptionField));

        unitDtoSelect.setItemLabelGenerator(UnitDto::getFullName);
        add(getHorizontalLayout("Единицы измерения", unitDtoSelect));

    }

    @Override
    public void open() {
        unitDtoSelect.setItems(unitService.getAll());
        super.open();
    }

    private HorizontalLayout getHeader() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        H2 title = new H2("Добавление товара");
        title.setHeight("2.2em");
        title.setWidth("345px");
        horizontalLayout.add(title);
        horizontalLayout.add(new Button("Добавить", event -> {
            ProductDto productDto = new ProductDto();
            productDto.setName(nameTextField.getValue());
            productDto.setWeight(BigDecimal.valueOf(weightNumberField.getValue()));
            productDto.setVolume(BigDecimal.valueOf(volumeNumberField.getValue()));
            productDto.setPurchasePrice(BigDecimal.valueOf(purchasePriceNumberField.getValue()));
            productDto.setDescription(descriptionField.getValue());
            productDto.setUnitDto(unitDtoSelect.getValue());
            System.out.println(productDto);
        }));
        horizontalLayout.add(new Button("Закрыть", event -> close()));
        return horizontalLayout;
    }

    private <T extends Component & HasSize> HorizontalLayout getHorizontalLayout(String labelText, T field) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        field.setWidth(fieldWidth);
        Label label = new Label(labelText);
        label.setWidth(labelWidth);
        horizontalLayout.add(label, field);
        return horizontalLayout;
    }



}
