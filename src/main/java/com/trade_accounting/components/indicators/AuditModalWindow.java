package com.trade_accounting.components.indicators;


import com.trade_accounting.models.dto.indicators.AuditDto;
import com.trade_accounting.services.interfaces.indicators.AuditService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import org.apache.poi.hssf.record.Margin;

public class AuditModalWindow extends Dialog {

    private final String labelWidth = "200px";
    private final TextField timeField = new TextField();
    private final TextField employeeField = new TextField();
    private final TextField eventField = new TextField();
    private Long id;
    private final AuditService auditService;

    private AuditDto auditDtoEdit = new AuditDto();
    private Binder<AuditDto> auditDtoBinder = new Binder<>(AuditDto.class);

    public AuditModalWindow(AuditDto auditDto,
                            AuditService auditService) {
        this.auditService = auditService;

        id = auditDto.getId();
        timeField.setValue(getFieldValueNotNull(auditDto.getDate()));
        employeeField.setValue(getFieldValueNotNull(auditDto.getEmployeeId().toString()));
        eventField.setValue(getFieldValueNotNull(auditDto.getDescription()));
        timeField.setReadOnly(true);
        employeeField.setReadOnly(true);
        eventField.setReadOnly(true);

        add(
            header(),
            lowerLayout()
        );

        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);

    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();

        VerticalLayout titleLayout = new VerticalLayout(new H2("Аудит"));
        VerticalLayout button = new VerticalLayout(new Button("Закрыть", event -> {
            clearAll();
            close();
        }));
        titleLayout.setSizeUndefined();
        titleLayout.setPadding(false);
        button.setSizeUndefined();
        button.setPadding(false);

        header.add(titleLayout, button);
        header.setFlexGrow(1.0, titleLayout);
        header.setFlexGrow(0, button);
        header.getStyle().set("margin-bottom", "20px");

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
        HorizontalLayout time = new HorizontalLayout();
        Label label = new Label("Время");
        label.setWidth(labelWidth);
        time.add(label, timeField);
        return time;
    }

    private Component addEmployee() {
        HorizontalLayout employee = new HorizontalLayout();
        Label label = new Label("Сотрудник");
        label.setWidth(labelWidth);
        employee.add(label, employeeField);
        return employee;
    }

    private Component addEvent() {
        HorizontalLayout event = new HorizontalLayout();
        Label label = new Label("Событие");
        label.setWidth(labelWidth);
        event.add(label, eventField);
        return event;
    }

    public void clearAll() {
        timeField.clear();
        employeeField.clear();
        eventField.clear();
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }

    // Закомментирована кнопка добавления аудита, потому что пользователь не может самостоятельно добавлять данные.
    // Они добавляются автоматически при выполнении операций.

//    private Button getSaveButton() {
//        Button saveButton = new Button("Сохранить", event -> {
//            if (auditDtoEdit.getId() != null) {
//                auditDtoEdit.setDate(String.valueOf(timePickerField.getValue()));
//                auditDtoEdit.setEmployeeId(Long.valueOf(employeeColumn.getValue()));
//                auditDtoEdit.setDescription(eventColumn.getValue());
//                if (auditDtoBinder.validate().isOk()) {
//                    auditService.update(auditDtoEdit);
//                    clearAll();
//                    close();
//                } else {
//                    auditDtoBinder.validate().notifyBindingValidationStatusHandlers();
//                }
//            } else {
//                AuditDto auditDto = new AuditDto();
//                auditDtoEdit.setDate(String.valueOf(timePickerField.getValue()));
//                auditDtoEdit.setEmployeeId(Long.valueOf(employeeColumn.getValue()));
//                auditDtoEdit.setDescription(eventColumn.getValue());
//                if (auditDtoBinder.validate().isOk()) {
//                    auditService.create(auditDto);
//                    clearAll();
//                    close();
//                } else {
//                    auditDtoBinder.validate().notifyBindingValidationStatusHandlers();
//                }
//            }
//        });
//        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        return saveButton;
//    }
}
