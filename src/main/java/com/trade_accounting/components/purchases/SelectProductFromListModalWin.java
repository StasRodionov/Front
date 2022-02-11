package com.trade_accounting.components.purchases;

import com.trade_accounting.models.dto.warehouse.AcceptanceProductionDto;
import com.trade_accounting.models.dto.warehouse.ProductDto;
import com.trade_accounting.services.interfaces.warehouse.AcceptanceProductionService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.math.BigDecimal;
import java.util.List;

@SpringComponent
@UIScope
public class SelectProductFromListModalWin extends Dialog {
    private static final String LABEL_WIDTH = "100px";
    private static final String FIELD_WIDTH = "300px";
    private final ProductService productService;
    private List<ProductDto> productDtos;
    public ComboBox<ProductDto> productSelect = new ComboBox<>();
    private TextField number = new TextField();
    AcceptanceProductionDto addProductToList;
    AcceptanceProductionService acceptanceProductionService;

    public SelectProductFromListModalWin(ProductService productService,
                                         AcceptanceProductionService acceptanceProductionService) {
        this.productService = productService;
        this.acceptanceProductionService = acceptanceProductionService;
//        productSelect = new ComboBox<>();
        add(header(), configureProductSelect(), summConfigure());
    }

    private HorizontalLayout header() {
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.add(getSaveButton(), getCloseButton()
        );
        return headerLayout;
    }

    public void setNewAcceptanceProductionDto (AcceptanceProductionDto add) {
        this.addProductToList = add;
    }

    public void clearWin() {
        number.setValue("");
        productSelect.setLabel("");
    }

    private HorizontalLayout summConfigure() {
        HorizontalLayout horizontal1 = new HorizontalLayout();
        number.setWidth("100px");
        number.setRequired(true);
        number.setRequiredIndicatorVisible(true);
        Label label = new Label("количество");
        label.setWidth("100px");
        horizontal1.add(label, number);
        return horizontal1;
    }

    private HorizontalLayout configureProductSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        productDtos = productService.getAll();
        if (productDtos != null) {
            productSelect.setItems(productDtos);
        }
        productSelect.setItemLabelGenerator(ProductDto::getName);
        productSelect.setWidth(FIELD_WIDTH);
        Label label = new Label("Выберите продукт");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, productSelect);
        return horizontalLayout;
    }

    private Button getSaveButton() {
        return new Button("Добавить", event -> {
//            addProductToList.setId(productSelect.getValue().getId());
            addProductToList.setAmount(new BigDecimal(number.getValue()));
            addProductToList.setProductId(productSelect.getValue().getId());
            addProductToList.setPrice(productSelect.getValue().getPurchasePrice());
            acceptanceProductionService.create(addProductToList);
            clearWin();
            close();
        });
    }

    private Button getCloseButton() {
        return new Button("Закрыть", event -> {
            clearWin();
            close();
        });
    }
}
