package com.trade_accounting.components.sells;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.invoice.InvoiceDto;
import com.trade_accounting.models.dto.invoice.InvoiceProductDto;
import com.trade_accounting.models.dto.warehouse.ProductDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.invoice.InvoiceProductService;
import com.trade_accounting.services.interfaces.invoice.InvoiceService;
import com.trade_accounting.services.interfaces.warehouse.ProductPriceService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.trade_accounting.services.interfaces.company.TypeOfPriceService;
import com.trade_accounting.services.interfaces.units.UnitService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
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
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.ArrayList;
import java.util.List;

@UIScope
@SpringComponent
public class InvoiceProductDtoAddModalView extends Dialog {
    private final ProductService productService;
    private final InvoiceProductService invoiceProductService;
    private final ContractorService contractorService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final InvoiceService invoiceService;
    private final Notifications notifications;
    private final TypeOfPriceService typeOfPriceService;
    private final UnitService unitService;
    private final ProductPriceService productPriceService;

    private List<InvoiceProductDto> tempInvoiceProductDtoList;
    private final ComboBox<Long> productDtoComboBox = new ComboBox<>();
    private final ComboBox<Long> invoiceDtoComboBox = new ComboBox<>();
    private final BigDecimalField priceField = new BigDecimalField();
    private final BigDecimalField amountField = new BigDecimalField();

    public InvoiceProductDtoAddModalView( List<InvoiceProductDto> tempInvoiceProductDtoList,
                                          ProductService productService, InvoiceProductService invoiceProductService,
                                          ContractorService contractorService, CompanyService companyService,
                                          WarehouseService warehouseService, InvoiceService invoiceService,
                                          Notifications notifications,
                                          TypeOfPriceService typeOfPriceService, UnitService unitService,
                                          ProductPriceService productPriceService) {
        this.tempInvoiceProductDtoList = tempInvoiceProductDtoList;
        this.invoiceProductService = invoiceProductService;
        this.productService = productService;
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.invoiceService = invoiceService;
        this.notifications = notifications;
        this.typeOfPriceService = typeOfPriceService;
        this.unitService = unitService;
        this.productPriceService = productPriceService;
        add(headerLayout(), formLayout());
    }

    private HorizontalLayout productConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ProductDto> list = productService.getAll();
        List<Long> listItems = new ArrayList<>();
        for(ProductDto id : list) {
            listItems.add(id.getId());
        }
        if (listItems != null) {
            productDtoComboBox.setItems(listItems);
        }
        productDtoComboBox.setItemLabelGenerator(item -> productService.getById(item).getName());
        productDtoComboBox.setWidth("350px");
        Label label = new Label("Продукт");
        label.setWidth("100px");
        horizontalLayout.add(label, productDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout invoiceConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<InvoiceDto> list = invoiceService.getAll();
        List<Long> listItems = new ArrayList<>();
        for(InvoiceDto id : list) {
            listItems.add(id.getId());
        }
        if (listItems != null) {
            invoiceDtoComboBox.setItems(listItems);
        }
        invoiceDtoComboBox.setItemLabelGenerator(item -> invoiceService.getById(item).toString());
        invoiceDtoComboBox.setWidth("350px");
        Label label = new Label("Заказ №");
        label.setWidth("100px");
        horizontalLayout.add(label, invoiceDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout headerLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(title(), saveButton(), closeButton());
        return horizontalLayout;
    }

    private VerticalLayout formLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(priceConfig(), amountConfig(), productConfigure(), invoiceConfigure());
        return verticalLayout;
    }

    private Button saveButton() {

        return new Button("Сохранить", e -> {

                InvoiceProductDto invoiceProductDto = new InvoiceProductDto();

                invoiceProductDto.setInvoiceId(invoiceDtoComboBox.getValue());
                invoiceProductDto.setProductId(productDtoComboBox.getValue());
                invoiceProductDto.setPrice(priceField.getValue());
                invoiceProductDto.setAmount(amountField.getValue());

                invoiceProductService.create(invoiceProductDto);

                close();
        });
    }

    private HorizontalLayout priceConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Цена");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("100px");
        horizontalLayout.add(label, priceField);
        return horizontalLayout;
    }

    private HorizontalLayout amountConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Количество");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("100px");
        horizontalLayout.add(label, amountField);
        return horizontalLayout;
    }

    private H2 title() {
        return new H2("Добавление продукта");
    }

    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> {
            close();
        });
        return button;
    }
}
