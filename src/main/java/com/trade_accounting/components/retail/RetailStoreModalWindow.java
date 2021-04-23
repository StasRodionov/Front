package com.trade_accounting.components.retail;

import com.trade_accounting.models.dto.RetailStoreDto;
import com.trade_accounting.services.interfaces.RetailStoreService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.checkbox.Checkbox;

import java.math.BigDecimal;

public class RetailStoreModalWindow extends Dialog {

    private final String fieldWidth = "400px";
    private final String labelWidth = "100px";

    private final Checkbox isActive = new Checkbox();
    private final TextField name = new TextField();
    private final TextField organization = new TextField();
    private final TextField salesInvoicePrefix = new TextField();
    private final Select<String> defaultTaxationSystem = new Select<>();
    private final Select<String> orderTaxationSystem = new Select<>();

    private final RetailStoreService retailStoreService;

    public RetailStoreModalWindow(RetailStoreService retailStoreService) {
        this.retailStoreService = retailStoreService;
        add(header());
        add(lowerLayout());
        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);
    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        getSaveButton().addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        header.add(getSaveButton(), getCloseButton());
        return header;
    }

    private HorizontalLayout lowerLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        Div div = new Div();
        div.add(
                addActiveCheck(),
                addStoreName(),
                addOrganization(),
                addPrefix(),
                addDefaultTaxation(),
                addOrderTaxation()
        );
        layout.add(div);
        return layout;
    }

    private Component addActiveCheck() {
        Label label = new Label("Включена");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, isActive);
    }

    private Component addStoreName() {
        Label label = new Label("Наименование");
        label.setWidth(labelWidth);
        name.setRequired(true);
        name.setWidth(fieldWidth);
        return new HorizontalLayout(label, name);
    }

    private Component addOrganization() {
        Label label = new Label("Организация");
        label.setWidth(labelWidth);
        organization.setRequired(true);
        organization.setWidth(fieldWidth);
        return new HorizontalLayout(label, organization);
    }

    private Component addPrefix() {
        Label label = new Label("Префикс номера продаж");
        label.setWidth(labelWidth);
        salesInvoicePrefix.setWidth(fieldWidth);
        return new HorizontalLayout(label, salesInvoicePrefix);
    }

    private Component addDefaultTaxation() {
        Label label = new Label("Система налогообложения по умолчанию");
        label.setWidth(labelWidth);
        defaultTaxationSystem.setWidth(fieldWidth);
        defaultTaxationSystem.setItems("ОСН", "УСН. Доход", "УСН. Доход-Расход", "ЕСХН", "ЕНВД", "Патент");
        return new HorizontalLayout(label, defaultTaxationSystem);
    }

    private Component addOrderTaxation() {
        Label label = new Label("Система налогообложения для заказов");
        label.setWidth(labelWidth);
        orderTaxationSystem.setWidth(fieldWidth);
        if (defaultTaxationSystem.getValue() != null) {
            orderTaxationSystem.setValue(defaultTaxationSystem.getValue());
        }
        orderTaxationSystem.setItems("ОСН", "УСН. Доход", "УСН. Доход-Расход", "ЕСХН", "ЕНВД", "Патент");
        return new HorizontalLayout(label, orderTaxationSystem);
    }

    private Button getSaveButton() {
        return new Button("Сохранить", event -> {
            RetailStoreDto retailStoreDto = new RetailStoreDto();
            retailStoreDto.setActive(isActive.getValue());
            retailStoreDto.setName(name.getValue());
            retailStoreDto.setActivityStatus("Онлайн");
            retailStoreDto.setRevenue(new BigDecimal(10_000));
            retailStoreDto.setOrganization(organization.getValue());
            retailStoreDto.setSalesInvoicePrefix(salesInvoicePrefix.getValue());
            retailStoreDto.setDefaultTaxationSystem(defaultTaxationSystem.getValue());
            retailStoreDto.setOrderTaxationSystem(orderTaxationSystem.getValue());
            retailStoreService.create(retailStoreDto);
            close();
        });
    }

    private Button getCloseButton() {
        return new Button("Закрыть", event -> close());
    }
}
