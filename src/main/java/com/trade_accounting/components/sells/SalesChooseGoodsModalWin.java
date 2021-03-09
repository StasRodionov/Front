package com.trade_accounting.components.sells;

import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.services.interfaces.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@SpringComponent
@UIScope
public class SalesChooseGoodsModalWin extends Dialog {

    private String labelWidth = "100px";
    private String fieldWidth = "300px";

    private final ProductService productService;

    private Select<ProductDto> productSelect = new Select<>();

    private final SalesEditCreateInvoiceView salesEditCreateInvoiceView;

    @Autowired
    public SalesChooseGoodsModalWin(ProductService productService,
                                    @Lazy SalesEditCreateInvoiceView salesEditCreateInvoiceView
    ) {
        this.productService = productService;
        this.salesEditCreateInvoiceView = salesEditCreateInvoiceView;

        add(header(), configureProductSelect());
    }

    private HorizontalLayout header() {
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.add(getSaveButton(), getCloseButton()
        );
        return headerLayout;
    }

    private HorizontalLayout configureProductSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ProductDto> productDtos = productService.getAll();
        if (productDtos != null) {
            productSelect.setItems(productDtos);
        }
        productSelect.setItemLabelGenerator(ProductDto::getName);
        productSelect.setWidth(fieldWidth);
        Label label = new Label("Выберите продукт");
        label.setWidth(labelWidth);
        horizontalLayout.add(label, productSelect);
        return horizontalLayout;
    }

    private Button getSaveButton() {
        return new Button("Сохранить", event -> {
            if (productSelect.getValue() != null){
            salesEditCreateInvoiceView.addProduct(productSelect.getValue());
            }
            close();
        });
    }

    private Button getCloseButton() {
        return new Button("Закрыть", event -> {
            close();
        });
    }
}
