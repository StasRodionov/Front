package com.trade_accounting.components.goods;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.finance.LossProductDto;
import com.trade_accounting.models.dto.warehouse.ProductDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.finance.LossProductService;
import com.trade_accounting.services.interfaces.finance.LossService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.data.binder.Binder;

import java.util.List;

public class LossProductModalWindow extends Dialog {
    private final ProductService productService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final LossService lossService;
    private final LossProductService lossProductService;

    ComboBox<ProductDto> productDtoComboBox = new ComboBox<>();
    private final BigDecimalField priceField = new BigDecimalField();
    private final BigDecimalField amountField = new BigDecimalField();

    private final Binder<LossProductDto> lossProductDtoBinder = new Binder<>(LossProductDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";
    private final Notifications notifications;
    private final TitleForModal titleForСreate;

    public LossProductModalWindow (ProductService productService,
                                   CompanyService companyService,
                                   WarehouseService warehouseService,
                                   LossService lossService,
                                   Notifications notifications,
                                   LossProductService lossProductService) {
        this.productService = productService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.lossService = lossService;
        this.notifications = notifications;
        this.lossProductService = lossProductService;
        titleForСreate = new TitleForModal("Добавление списания");
        setSizeFull();
        add(headerLayout(), formLayout());
    }

    private HorizontalLayout headerLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(title(), saveButton(), closeButton());
        return horizontalLayout;
    }

    private VerticalLayout formLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(priceConfig(), amountConfig(), productConfigure());
        return verticalLayout;
    }

    private Button saveButton() {
        return new Button("Сохранить", e -> {
            if (!lossProductDtoBinder.validate().isOk()) {
                lossProductDtoBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
                LossProductDto dto = new LossProductDto();

                dto.setProductId(productDtoComboBox.getValue().getId());
                dto.setPrice(priceField.getValue());
                dto.setAmount(amountField.getValue());

                lossProductService.create(dto);

                UI.getCurrent().navigate("lossView");
                close();
                clearAllFieldsModalView();
                notifications.infoNotification("Продукт списания сохранен");

                LossModalWindow modalView = new LossModalWindow(
                        warehouseService,
                        lossService,
                        companyService,
                        lossProductService,
                        productService,
                        notifications,
                        titleForСreate
                );
                modalView.open();

            }
        });
    }

    private HorizontalLayout productConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ProductDto> list = productService.getAll();
        if (list != null) {
            productDtoComboBox.setItems(list);
        }
        productDtoComboBox.setItemLabelGenerator(ProductDto::getName);
        productDtoComboBox.setWidth("350px");
        Label label = new Label("Продукт");
        label.setWidth("100px");
        horizontalLayout.add(label, productDtoComboBox);
        lossProductDtoBinder.forField(productDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(lossProductDto -> productService.getById(lossProductDto.getProductId()),
                        (lossProductDto2, lossProductDto) -> lossProductDto2.setProductId(lossProductDto.getId()));
        return horizontalLayout;
    }

    private HorizontalLayout priceConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Цена");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("100px");
        horizontalLayout.add(label, priceField);
        lossProductDtoBinder.forField(priceField)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(LossProductDto::getPrice, LossProductDto::setPrice);
        return horizontalLayout;
    }

    private HorizontalLayout amountConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Количество");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("100px");
        horizontalLayout.add(label, amountField);
        lossProductDtoBinder.forField(amountField)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(LossProductDto::getAmount, LossProductDto::setAmount);
        return horizontalLayout;
    }

    private H2 title() {
        return new H2("Добавление продукта на списание");
    }

    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> {
            close();
            clearAllFieldsModalView();
        });
        return button;
    }

    private void clearAllFieldsModalView() {
        priceField.setValue(null);
        amountField.setValue(null);

    }
}
