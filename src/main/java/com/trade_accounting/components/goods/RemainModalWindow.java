package com.trade_accounting.components.goods;

import com.trade_accounting.models.dto.warehouse.RemainDto;
import com.trade_accounting.services.interfaces.warehouse.RemainService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class RemainModalWindow  extends Dialog {

    private final String fieldWidth = "400px";
    private final String labelWidth = "200px";

    private static final TextField name = new TextField();
    private static final TextField vendorCode = new TextField();
    private final DateTimePicker currentTime = new DateTimePicker();
    private final NumberField balance = new NumberField();
    private final NumberField irreducibleBalance = new NumberField();
    private final NumberField reserve = new NumberField();
    private final NumberField expectation = new NumberField();
    private final NumberField available = new NumberField();
    private final NumberField unitId = new NumberField();
    private final NumberField daysOnWarehouse = new NumberField();
    private final NumberField costPrice = new NumberField();
    private final NumberField sumOfCostPrice = new NumberField();
    private final NumberField salesCost = new NumberField();
    private final NumberField salesSum = new NumberField();
    private final RemainService remainService;
    private RemainDto remainDtoToEdit = new RemainDto();
    private Binder<RemainDto> remainDtoBinder = new Binder<>(RemainDto.class);

    public RemainModalWindow(RemainService remainService) {
        this.remainService = remainService;
        add(header());
        add(lowerLayout());
        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);
    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        H2 title = new H2("Остатки");
        header.add(getSaveButton(), getCloseButton(), title);
        return header;
    }

    private HorizontalLayout lowerLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        Div div = new Div();
        div.add(
                addСurrentTime(),
                addName(),
                addVendorCode(),
                addBalance(),
                addIrreducibleBalance(),
                addReserve(),
                addExpectation(),
                addAvailable(),
                addUnitId(),
                addDaysOnWarehouse(),
                addCostPrice(),
                addSumOfCostPrice(),
                addSalesCost() ,
                addSalesSum()
        );
        layout.add(div);
        return layout;
    }

    private Component addName() {
        Label label = new Label("Наименование");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, name);
    }

    private Component addVendorCode() {
        Label label = new Label("Код продавца");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, vendorCode);
    }

    private Component addBalance() {
        Label label = new Label("Баланс");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, balance);
    }

    private Component addIrreducibleBalance() {
        Label label = new Label("Неснижаемый остаток");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, irreducibleBalance);
    }

    private Component addReserve() {
        Label label = new Label("Резерв");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, reserve);
    }

    private Component addExpectation() {
        Label label = new Label("Ожидание");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, expectation);
    }

    private Component addAvailable() {
        Label label = new Label("Доступно");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, available);
    }

    private Component addUnitId() {
        Label label = new Label("Единицы измерения");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, unitId);
    }

    private Component addDaysOnWarehouse() {
        Label label = new Label("Дней на складе");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, daysOnWarehouse);
    }

    private Component addCostPrice() {
        Label label = new Label("Закупочная цена");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, costPrice);
    }

    private Component addSumOfCostPrice() {
        Label label = new Label("Сумма закупки");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, sumOfCostPrice);
    }

    private Component addSalesCost() {
        Label label = new Label("Цена на продажу");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, salesCost);
    }

    private Component addSalesSum() {
        Label label = new Label("Сумма продаж");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, salesSum);
    }

    private Component addСurrentTime() {
        Label label = new Label("Дата создания");
        label.setWidth(labelWidth);
        currentTime.setWidth(fieldWidth);
        return new HorizontalLayout(label, currentTime);
    }


    private Button getSaveButton() {
        Button saveButton = new Button("Сохранить", event -> {
            if (remainDtoToEdit.getId() != null) {
                remainDtoToEdit.setName(name.getValue());
                remainDtoToEdit.setVendorCode(vendorCode.getValue());
                remainDtoToEdit.setBalance(balance.getValue().intValue());
                remainDtoToEdit.setIrreducibleBalance(irreducibleBalance.getValue().intValue());
                remainDtoToEdit.setReserve(reserve.getValue().intValue());
                remainDtoToEdit.setExpectation(expectation.getValue().intValue());
                remainDtoToEdit.setAvailable(available.getValue().intValue());
                remainDtoToEdit.setUnitId(unitId.getValue().longValue());
                remainDtoToEdit.setDaysOnWarehouse(daysOnWarehouse.getValue().intValue());
                remainDtoToEdit.setCostPrice(costPrice.getValue().intValue());
                remainDtoToEdit.setSumOfCostPrice(sumOfCostPrice.getValue().intValue());
                remainDtoToEdit.setSalesCost(salesCost.getValue().intValue());
                remainDtoToEdit.setSalesSum(salesSum.getValue().intValue());
                if (remainDtoBinder.validate().isOk()) {
                    remainService.update(remainDtoToEdit);
                    clearAll();
                    close();
                } else {
                    remainDtoBinder.validate().notifyBindingValidationStatusHandlers();
                }
            } else {
                RemainDto remainDto = new RemainDto();
                remainDtoToEdit.setName(name.getValue());
                remainDtoToEdit.setVendorCode(vendorCode.getValue());
                remainDtoToEdit.setBalance(balance.getValue().intValue());
                remainDtoToEdit.setIrreducibleBalance(irreducibleBalance.getValue().intValue());
                remainDtoToEdit.setReserve(reserve.getValue().intValue());
                remainDtoToEdit.setExpectation(expectation.getValue().intValue());
                remainDtoToEdit.setAvailable(available.getValue().intValue());
                remainDtoToEdit.setUnitId(unitId.getValue().longValue());
                remainDtoToEdit.setDaysOnWarehouse(daysOnWarehouse.getValue().intValue());
                remainDtoToEdit.setCostPrice(costPrice.getValue().intValue());
                remainDtoToEdit.setSumOfCostPrice(sumOfCostPrice.getValue().intValue());
                remainDtoToEdit.setSalesCost(salesCost.getValue().intValue());
                remainDtoToEdit.setSalesSum(salesSum.getValue().intValue());
                if (remainDtoBinder.validate().isOk()) {
                    remainService.create(remainDto);
                    clearAll();
                    close();
                } else {
                    remainDtoBinder.validate().notifyBindingValidationStatusHandlers();
                }
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return saveButton;
    }

    private Button getCloseButton() {
        return new Button("Закрыть", event -> {
            clearAll();
            close();
        });
    }

    public void clearAll() {
        currentTime.clear();
        name.clear();
        vendorCode.clear();
        balance.clear();
        irreducibleBalance.clear();
        reserve.clear();
        expectation.clear();
        available.clear();
        unitId.clear();
        daysOnWarehouse.clear();
        costPrice.clear();
        sumOfCostPrice.clear();
        salesCost.clear();
        salesSum.clear();
    }
}
