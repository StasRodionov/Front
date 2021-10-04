package com.trade_accounting.components.goods;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.InternalOrderProductsDto;
import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.InternalOrderProductsDtoService;
import com.trade_accounting.services.interfaces.InternalOrderService;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.WarehouseService;
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
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.List;

@UIScope
@SpringComponent
public class InternalOrderProductsModalView extends Dialog {
    private final InternalOrderProductsDtoService internalOrderProductsDtoService;
    private final ProductService productService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final InternalOrderService internalOrderService;

    private final ComboBox<ProductDto> productDtoComboBox = new ComboBox<>();
    private final BigDecimalField priceField = new BigDecimalField();
    private final BigDecimalField amountField = new BigDecimalField();

    private final Binder<InternalOrderProductsDto> internalOrderProductsDtoBinder =
            new Binder<>(InternalOrderProductsDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";
    private final Notifications notifications;
    private final TitleForModal titleForСreate;

    public InternalOrderProductsModalView(InternalOrderProductsDtoService internalOrderProductsDtoService,
                                          Notifications notifications, ProductService productService,
                                          CompanyService companyService, WarehouseService warehouseService,
                                          InternalOrderService internalOrderService) {
        this.internalOrderProductsDtoService = internalOrderProductsDtoService;
        this.productService = productService;
        this.notifications = notifications;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.internalOrderService = internalOrderService;
        titleForСreate = new TitleForModal("Добавление внутреннего заказа");
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

    private H2 title() {
        return new H2("Добавление продукта внутреннего заказа");
    }

    private Button saveButton() {

        return new Button("Сохранить", e -> {
            if (!internalOrderProductsDtoBinder.validate().isOk()) {
                internalOrderProductsDtoBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
                InternalOrderProductsDto dto = new InternalOrderProductsDto();

                dto.setProductId(productDtoComboBox.getValue().getId());
                dto.setPrice(priceField.getValue());
                dto.setAmount(amountField.getValue());

                internalOrderProductsDtoService.create(dto);

                UI.getCurrent().navigate("internalorder");
                close();
                clearAllFieldsModalView();
                notifications.infoNotification("Продукт внутреннего заказа сохранен");

                InternalOrderModalView modalView = new InternalOrderModalView(
                        companyService,
                        warehouseService,
                        internalOrderService,
                        notifications,
                        internalOrderProductsDtoService,
                        productService,
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
        internalOrderProductsDtoBinder.forField(productDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(internalOrderProductsDto -> productService.getById(internalOrderProductsDto.getProductId()),
                     (internalOrderProductsDto2, internalOrderProductsDto) -> internalOrderProductsDto2.setProductId(internalOrderProductsDto.getId()));
        return horizontalLayout;
    }

    private HorizontalLayout priceConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Цена");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("100px");
        horizontalLayout.add(label, priceField);
        internalOrderProductsDtoBinder.forField(priceField)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(InternalOrderProductsDto::getPriceValid, InternalOrderProductsDto::setPriceValid);
        return horizontalLayout;
    }

    private HorizontalLayout amountConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Количество");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("100px");
        horizontalLayout.add(label, amountField);
        internalOrderProductsDtoBinder.forField(amountField)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(InternalOrderProductsDto::getAmountValid, InternalOrderProductsDto::setAmountValid);
        return horizontalLayout;
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
