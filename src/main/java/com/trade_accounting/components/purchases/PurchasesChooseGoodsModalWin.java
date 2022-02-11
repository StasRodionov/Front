package com.trade_accounting.components.purchases;

import com.trade_accounting.models.dto.warehouse.ProductDto;
import com.trade_accounting.models.dto.warehouse.ProductPriceDto;
import com.trade_accounting.services.interfaces.warehouse.ProductPriceService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
@Component
public class PurchasesChooseGoodsModalWin extends Dialog {
    private static final String LABEL_WIDTH = "100px";
    private static final String FIELD_WIDTH = "300px";

    private final ProductService productService;
    private final ProductPriceService productPriceService;

    public BigDecimalField amoSelect = new BigDecimalField();

    private List<ProductDto> productDtos;

    public final ComboBox<ProductDto> productSelect = new ComboBox<>();
    public final ComboBox<ProductPriceDto> priceSelect = new ComboBox<>();

    public Button saveButton = new Button();

    public PurchasesChooseGoodsModalWin(ProductService productService, ProductPriceService productPriceService) {
        this.productService = productService;
        this.productPriceService = productPriceService;

        add(header(), configureProductSelect(), configureAmoSelect(), configurePriceSelect());
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

    public void updatePricesList() {
        if (productSelect.getValue() != null){
            priceSelect.setItems(productPriceService.getAll()
                    .stream()
                    .filter(x -> productSelect.getValue().getProductPriceIds().contains(x.getId()))
                    .collect(Collectors.toList())
            );
            priceSelect.setItemLabelGenerator(x -> x.getValue().toString());
            priceSelect.addValueChangeListener(e -> {
                updateSaveButtonEnable();
            });
        }
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

        productSelect.addValueChangeListener(e -> {
            updatePricesList();
            updateSaveButtonEnable();
        });

        return horizontalLayout;
    }

    private HorizontalLayout configurePriceSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Выберите цену");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, priceSelect);
        return horizontalLayout;
    }

    private HorizontalLayout configureAmoSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Укажите количество");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, amoSelect);
        return horizontalLayout;
    }

    private Button getSaveButton() {
        saveButton = new Button("Добавить", event -> {
            close();
        });
        saveButton.setEnabled(false);
        return saveButton;
    }

    private Button getCloseButton() {
        return new Button("Закрыть", event -> {
            productSelect.setValue(null);
            priceSelect.setValue(null);
            amoSelect.setValue(BigDecimal.valueOf(0.0));
            close();
        });
    }

    private void updateSaveButtonEnable() {
        if (productSelect.getValue() != null && priceSelect.getValue() != null && amoSelect.getValue() !=null){
            saveButton.setEnabled(true);
        } else {
            saveButton.setEnabled(false);
        }
    }
}
