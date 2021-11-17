package com.trade_accounting.components.sells;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.InvoiceProductDto;
import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.services.interfaces.InvoiceProductService;
import com.trade_accounting.services.interfaces.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringComponent
@UIScope
public class ReturnBuyersGoodModalWin extends Dialog {
    private ProductDto productDto = new ProductDto();
    private static final String LABEL_WIDTH = "100px";
    private static final String FIELD_WIDTH = "300px";
    private final ProductService productService;
    private List<ProductDto> productDtos;
    public final ComboBox<ProductDto> productSelect = new ComboBox<>();
    private final TextField number = new TextField();
    private final Notifications notifications;
    private static Double summ = 0.00;

    public ReturnBuyersGoodModalWin(ProductService productService,
                                    Notifications notifications) {
        this.productService = productService;
        this.notifications = notifications;
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
//            productService.update(productDto);
//            notifications.infoNotification(String.format("Продукт %s добавлен для возврата", productSelect.getValue()));
//            updateProductList();
            close();

//            open();
        });
    }

    private Button getCloseButton() {
        return new Button("Закрыть", event -> {
            productSelect.setValue(null);
            close();
        });
    }
}
