package com.trade_accounting.components.goods;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.warehouse.MovementProductDto;
import com.trade_accounting.models.dto.warehouse.ProductDto;
import com.trade_accounting.services.interfaces.company.BankAccountService;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.LegalDetailService;
import com.trade_accounting.services.interfaces.warehouse.MovementProductService;
import com.trade_accounting.services.interfaces.warehouse.MovementService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.trade_accounting.services.interfaces.units.UnitService;
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
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.List;

@UIScope
@SpringComponent
public class MovementProductsModalView extends Dialog {
    private final MovementProductService movementProductService;
    private final ProductService productService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final MovementService movementService;
    private final UnitService unitService;
    private final LegalDetailService legalDetailService;
    private final BankAccountService bankAccountService;

    private final ComboBox<ProductDto> productDtoComboBox = new ComboBox<>();
    private final BigDecimalField priceField = new BigDecimalField();
    private final BigDecimalField amountField = new BigDecimalField();

    private final Binder<MovementProductDto> movementProductDtoBinder =
            new Binder<>(MovementProductDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";
    private final Notifications notifications;
    private final TitleForModal titleForСreate;


    public MovementProductsModalView(MovementProductService movementProductService,
                                     ProductService productService,
                                     CompanyService companyService,
                                     WarehouseService warehouseService,
                                     MovementService movementService,
                                     UnitService unitService,
                                     LegalDetailService legalDetailService,
                                     BankAccountService bankAccountService,
                                     Notifications notifications) {
        this.movementProductService = movementProductService;
        this.productService = productService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.movementService = movementService;
        this.unitService = unitService;
        this.notifications = notifications;
        this.legalDetailService = legalDetailService;
        this.bankAccountService = bankAccountService;

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
            if (!movementProductDtoBinder.validate().isOk()) {
                movementProductDtoBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
                MovementProductDto dto = new MovementProductDto();

                dto.setProductId(productDtoComboBox.getValue().getId());
                dto.setPrice(priceField.getValue());
                dto.setAmount(amountField.getValue());

                movementProductService.create(dto);

                UI.getCurrent().navigate("movementView");
                close();
                clearAllFieldsModalView();
                notifications.infoNotification("Продукт перемещения сохранен");

                MovementViewModalWindow modalView = new MovementViewModalWindow(
                        productService,
                        movementService,
                        warehouseService,
                        companyService,
                        notifications,
                        unitService,
                        movementProductService,
                        legalDetailService,
                        bankAccountService);
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
        movementProductDtoBinder.forField(productDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(movementProductDto -> productService.getById(movementProductDto.getProductId()),
                        (movementProductDto1, movementProductDto) -> movementProductDto1.setProductId(movementProductDto.getId()));
        return horizontalLayout;
    }

    private HorizontalLayout priceConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Цена");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("100px");
        horizontalLayout.add(label, priceField);
        movementProductDtoBinder.forField(priceField)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(MovementProductDto::getPrice, MovementProductDto::setPrice);
        return horizontalLayout;
    }

    private HorizontalLayout amountConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Количество");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("100px");
        horizontalLayout.add(label, amountField);
        movementProductDtoBinder.forField(amountField)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(MovementProductDto::getAmount, MovementProductDto::setAmount);
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
