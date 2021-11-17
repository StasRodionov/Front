package com.trade_accounting.components.sells;

import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.services.interfaces.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.util.List;

@SpringComponent
@UIScope
public class SelectProductFromListWithQuantityModalWin extends Dialog {
    private static final String LABEL_WIDTH = "100px";
    private static final String FIELD_WIDTH = "300px";
    private final ProductService productService;
    private List<ProductDto> productDtos;
    public ComboBox<ProductDto> productSelect;
    private TextField number;
    private static Double summ;

    public SelectProductFromListWithQuantityModalWin(ProductService productService) {
        this.productService = productService;
        productSelect = new ComboBox<>();
        number = new TextField();
        summ = 0.00;
        add(header(), configureProductSelect(), summConfigure());
    }

    private HorizontalLayout header() {
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.add(getSaveButton(), getCloseButton()
        );
        return headerLayout;
    }

    public void updateProductList() {
        productSelect.setItems(productService.getAll());
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
            productSelect.setLabel(number.getValue());
            summ += Integer.parseInt(number.getValue()) * productSelect.getValue().getPurchasePrice().longValue();
            productSelect.setHelperText(summ.toString());
            close();
        });
    }

    private Button getCloseButton() {
        return new Button("Закрыть", event -> {
            productSelect.setValue(null);
            close();
        });
    }
}
