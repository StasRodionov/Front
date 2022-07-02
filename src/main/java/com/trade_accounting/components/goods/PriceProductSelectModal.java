package com.trade_accounting.components.goods;

import com.trade_accounting.models.dto.company.PriceListDto;
import com.trade_accounting.models.dto.company.PriceListProductDto;
import com.trade_accounting.models.dto.company.PriceListProductPercentsDto;
import com.trade_accounting.models.dto.warehouse.ProductDto;
import com.trade_accounting.models.dto.warehouse.ProductPriceDto;
import com.trade_accounting.services.interfaces.warehouse.ProductPriceService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@SpringComponent
@UIScope
public class PriceProductSelectModal extends Dialog {
    private static final String LABEL_WIDTH = "100px";
    private static final String FIELD_WIDTH = "300px";
    private final ProductService productService;
    private final ProductPriceService productPriceService;
    private final ComboBox<ProductDto> productSelect = new ComboBox<>();
    private final BigDecimalField priceSelect = new BigDecimalField();
    private Button saveButton;
    private PriceListDto priceListData;
    private PriceListProductPercentsDto priceListProductPercent;
    private PriceListProductDto priceListProductData = new PriceListProductDto();
    ;
    private Label label = new Label();


    public PriceProductSelectModal(ProductService productService, ProductPriceService productPriceService) {
        this.productService = productService;
        this.productPriceService = productPriceService;
        add(configureProductSelect(), configurePriceSelect(), footer());
    }

    private HorizontalLayout footer() {
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
        productSelect.setWidth(FIELD_WIDTH);
        Label labelProduct = new Label("Выберите продукт");
        labelProduct.setWidth(LABEL_WIDTH);
        horizontalLayout.add(labelProduct, productSelect);
        productSelect.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                percentSum(e.getValue());
                getPriceListProductDto();
                updateSaveButtonEnable();
            }
        });
        return horizontalLayout;
    }

    public void setProductSelect(PriceListProductDto priceListProductData, PriceListDto priceListData,
                                 PriceListProductPercentsDto priceListProductPercentsDto) {
        this.priceListData = priceListData;
        this.priceListProductPercent = priceListProductPercentsDto;
        this.priceListProductData = priceListProductData;
        productSelect.setValue(productService.getById(priceListProductData.getProductId()));
    }

    private void percentSum(ProductDto product) {
        ProductPriceDto productPriceDto = null;
        for (Long id : product.getProductPriceIds()) {
            productPriceDto = productPriceService.getById(id);
            if (Objects.equals(priceListData.getTypeOfPriceId(), productPriceDto.getTypeOfPriceId())) {
                priceSelect.setValue(totalSum(productPriceDto.getValue().doubleValue()));
            }
        }
    }

    private BigDecimal totalSum(Double price) {
        double percent = (priceListProductPercent.getPercent()).doubleValue();
        if (percent == 0) {
            return BigDecimal.valueOf(price);
        } else {
            return BigDecimal.valueOf(price + price / 100.0 * percent);
        }
    }

    private HorizontalLayout configurePriceSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, priceSelect);
        return horizontalLayout;
    }

    private Button getSaveButton() {
        saveButton = new Button("Добавить", event -> close());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        saveButton.setEnabled(false);
        return saveButton;
    }

    private Button getCloseButton() {
        Button closeButton = new Button("Закрыть");
        closeButton.addClickListener(event -> {
            priceSelect.clear();
            productSelect.clear();

            close();
        });
        return closeButton;
    }

    public PriceListProductDto getPriceListProductDto() {
            PriceListProductDto priceListProductData = new PriceListProductDto();
            priceListProductData.setId(this.priceListProductData.getId());
            priceListProductData.setProductId(productSelect.getValue().getId());
            priceListProductData.setPrice(priceSelect.getValue());
            priceListProductData.setPriceListId(priceListData.getId());
            return priceListProductData;
    }

    private void updateSaveButtonEnable() {
        saveButton.setEnabled(isFormValid());
    }

    public boolean isFormValid() {
        return productSelect.getValue() != null;
    }

    public void clearForm() {
        productSelect.setValue(null);
        priceSelect.setValue(null);
    }

    public void setPriceList(PriceListDto priceListData, PriceListProductPercentsDto priceListProductPercentsDto) {
        this.priceListData = priceListData;
        this.priceListProductPercent = priceListProductPercentsDto;
        label.setText(priceListProductPercent.getName());
    }
}
