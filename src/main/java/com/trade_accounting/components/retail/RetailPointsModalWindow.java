package com.trade_accounting.components.retail;

import com.trade_accounting.controllers.dto.ContractorDto;
import com.trade_accounting.controllers.dto.RetailPointsDto;
import com.trade_accounting.services.interfaces.RetailPointsService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ErrorLevel;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class RetailPointsModalWindow extends Dialog {

    private final String fieldWidth = "400px";
    private final String labelWidth = "200px";

    private final NumberField number = new NumberField();
    private final DateTimePicker currentTime = new DateTimePicker();
    private final DateTimePicker accrualDate = new DateTimePicker();
    private final Select<ContractorDto> contractorId = new Select<>();
    private final Select<String> typeOperation = new Select<>();
    private final Select<Integer> bonusProgramId = new Select<>();
    private final NumberField numberOfPoints = new NumberField();

    private final RetailPointsService retailPointsService;
    private RetailPointsDto retailPointsDtoToEdit = new RetailPointsDto();
    private Binder<RetailPointsDto> retailPointsDtoBinder = new Binder<>(RetailPointsDto.class);

    public RetailPointsModalWindow(RetailPointsService retailPointsService) {
        this.retailPointsService = retailPointsService;
        add(header());
        add(lowerLayout());
        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);
    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        H2 title = new H2("Операция с баллами");
        header.add(getSaveButton(), getCloseButton(), title);
        return header;
    }

    private HorizontalLayout lowerLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        Div div = new Div();
        div.add(
                addNumber(),
                addСurrentTime(),
                typeOperation(),
                addNumberOfPoints(),
                addAccrualDate(),
                bonusProgramId(),
                addСontractorId()
        );
        layout.add(div);
        return layout;
    }

    private Component addNumber() {
        Label label = new Label("Номер");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, number);
    }

    private Component addСurrentTime() {
        Label label = new Label("Дата создания");
        label.setWidth(labelWidth);
        currentTime.setWidth(fieldWidth);
        return new HorizontalLayout(label, currentTime);
    }

    private Component addСontractorId() {
        Label label = new Label("Контрагент");
        label.setWidth(labelWidth);
        contractorId.setItemLabelGenerator(ContractorDto::getName);
        retailPointsDtoBinder.forField(contractorId).
                withValidator(Objects::nonNull, "Поле должно быть заполнено", ErrorLevel.ERROR)
                .bind("contractorId");
        contractorId.setWidth(fieldWidth);
        return new HorizontalLayout(label, contractorId);
    }

    private Component addNumberOfPoints() {
        Label label = new Label("Бонусные баллы");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, numberOfPoints);
    }

    private Component typeOperation() {
        Label label = new Label("Тип операции");
        label.setWidth(labelWidth);
        typeOperation.setWidth(fieldWidth);
        typeOperation.setItems("Начисление", "Списание");
        return new HorizontalLayout(label, typeOperation);
    }

    private Component bonusProgramId() {
        Label label = new Label("Бонусная программа");
        label.setWidth(labelWidth);
        retailPointsDtoBinder.forField(bonusProgramId).
                withValidator(Objects::nonNull, "Поле должно быть заполнено", ErrorLevel.ERROR)
                .bind("bonusProgramId");
        bonusProgramId.setWidth(fieldWidth);
        return new HorizontalLayout(label, bonusProgramId);
    }

    private Component addAccrualDate() {
        Label label = new Label("Дата начисления");
        label.setWidth(labelWidth);
        accrualDate.setWidth(fieldWidth);
        return new HorizontalLayout(label, accrualDate);
    }

    private Button getSaveButton() {
        Button saveButton = new Button("Сохранить", event -> {
            if (retailPointsDtoToEdit.getId() != null) {
                retailPointsDtoToEdit.setNumber(number.getValue().longValue());
                retailPointsDtoToEdit.setCurrentTime(currentTime.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                retailPointsDtoToEdit.setAccrualDate(accrualDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//                retailPointsDtoToEdit.setContractorId(Long.parseLong(String.valueOf(contractorId.getId())));
                retailPointsDtoToEdit.setNumberOfPoints(numberOfPoints.getValue().longValue());
                retailPointsDtoToEdit.setTypeOperation(typeOperation.getValue());
                retailPointsDtoToEdit.setBonusProgramId(Long.valueOf(bonusProgramId.getValue()));
                if (retailPointsDtoBinder.validate().isOk()) {
                    retailPointsService.update(retailPointsDtoToEdit);
                    clearAll();
                    close();
                } else {
                    retailPointsDtoBinder.validate().notifyBindingValidationStatusHandlers();
                }
            } else {
                RetailPointsDto retailPointsDto = new RetailPointsDto();
                retailPointsDtoToEdit.setNumber(number.getValue().longValue());
                retailPointsDtoToEdit.setCurrentTime(currentTime.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                retailPointsDtoToEdit.setAccrualDate(accrualDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//                retailPointsDtoToEdit.setContractorId( contractorId.getValue().getId());
                retailPointsDtoToEdit.setNumberOfPoints(numberOfPoints.getValue().longValue());
                retailPointsDtoToEdit.setTypeOperation(typeOperation.getValue());
                retailPointsDtoToEdit.setBonusProgramId(Long.valueOf(bonusProgramId.getValue()));
                if (retailPointsDtoBinder.validate().isOk()) {
                    retailPointsService.create(retailPointsDto);
                    clearAll();
                    close();
                } else {
                    retailPointsDtoBinder.validate().notifyBindingValidationStatusHandlers();
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
        accrualDate.clear();
        number.clear();
        numberOfPoints.clear();
        contractorId.clear();
        typeOperation.clear();
        bonusProgramId.clear();
    }
}