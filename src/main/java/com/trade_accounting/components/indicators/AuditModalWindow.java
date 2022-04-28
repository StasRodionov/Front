package com.trade_accounting.components.indicators;


import com.trade_accounting.models.dto.indicators.AuditDto;
import com.trade_accounting.services.interfaces.indicators.AuditService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;

public class AuditModalWindow extends Dialog {
    private final String labelWidth = "200px";

    private final TimePicker timePickerField = new TimePicker();

    private final TextField employeeColumn = new TextField();

    private final TextField eventColumn = new TextField();
    private final AuditService auditService;

    private AuditDto auditDtoEdit = new AuditDto();

    private Binder<AuditDto> auditDtoBinder = new Binder<>(AuditDto.class);

    public AuditModalWindow(AuditService auditService) {
        this.auditService = auditService;
        add(header());
        add(lowerLayout());
        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);
    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        H2 title = new H2("Аудит");
        header.add(getSaveButton(), getCloseButton(), title);
        return header;
    }


    private HorizontalLayout lowerLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        Div div = new Div();
        div.add(
                addTime(),
                addEmployee(),
                addEvent()
        );
        layout.add(div);
        return layout;
    }


    private Component addTime() {
        Label label = new Label("Время");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, timePickerField);
    }

    private Component addEmployee() {
        Label label = new Label("Сотрудник");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, employeeColumn);
    }

    private Component addEvent() {
        Label label = new Label("Событие");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, eventColumn);
    }

    private Button getSaveButton() {
        Button saveButton = new Button("Сохранить", event -> {
            if (auditDtoEdit.getId() != null) {
                auditDtoEdit.setDate(String.valueOf(timePickerField.getValue()));
                auditDtoEdit.setEmployeeId(Long.valueOf(employeeColumn.getValue()));
                auditDtoEdit.setDescription(eventColumn.getValue());
                if (auditDtoBinder.validate().isOk()) {
                    auditService.update(auditDtoEdit);
                    clearAll();
                    close();
                } else {
                    auditDtoBinder.validate().notifyBindingValidationStatusHandlers();
                }
            } else {
                AuditDto auditDto = new AuditDto();
                auditDtoEdit.setDate(String.valueOf(timePickerField.getValue()));
                auditDtoEdit.setEmployeeId(Long.valueOf(employeeColumn.getValue()));
                auditDtoEdit.setDescription(eventColumn.getValue());
                if (auditDtoBinder.validate().isOk()) {
                    auditService.create(auditDto);
                    clearAll();
                    close();
                } else {
                    auditDtoBinder.validate().notifyBindingValidationStatusHandlers();
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
        timePickerField.clear();
        employeeColumn.clear();
        eventColumn.clear();
    }
}
