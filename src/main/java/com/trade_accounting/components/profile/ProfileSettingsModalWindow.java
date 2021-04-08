package com.trade_accounting.components.profile;

import com.trade_accounting.models.dto.EmployeeDto;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import javax.security.auth.message.callback.PasswordValidationCallback;

public class ProfileSettingsModalWindow extends Dialog {

    private PasswordField currentPassword = new PasswordField();
    private PasswordField updatePassword = new PasswordField();
    private PasswordField updatePasswordCheck = new PasswordField();

    private final String labelWidth = "200px";
    private final String fieldWidth = "300px";

    private final EmployeeService employeeService;
    private Div div;

    public ProfileSettingsModalWindow(EmployeeService employeeService) {
        this.employeeService = employeeService;
        div = new Div();

        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);
        add(mainLayout());
    }

    private HorizontalLayout mainLayout() {
        HorizontalLayout main = new HorizontalLayout();
        div.removeAll();
        div.add(currentEmployeePassword(),
                updateEmployeePassword(),
                updateEmployeePasswordCheck(),
                getButtonSave(),
                getCancelButton()
        );
        add(div);
        return main;
    }

    private Component currentEmployeePassword() {
        Label label = new Label("Текущий пароль:");
        label.setWidth(labelWidth);
        Binder<EmployeeDto> binder = new Binder<>();
        EmployeeDto employee = employeeService.getPrincipal();
        currentPassword.setWidth(fieldWidth);
        currentPassword.setRequired(true);
        binder.forField(currentPassword).
                withValidator(pw -> pw.equals(employee.getPassword()), "Invalid password");
        HorizontalLayout checkPassword = new HorizontalLayout(label, currentPassword);
        return checkPassword;
    }

    private Component updateEmployeePassword() {
        Label label = new Label("Новый пароль:");
        label.setWidth(labelWidth);
//        Binder<EmployeeDto> binder = new Binder<>();
//        EmployeeDto employee = employeeService.getPrincipal();
        updatePassword.setWidth(fieldWidth);
        updatePassword.setRequired(true);
//        binder.forField(updatePassword).
//                withValidator(pw -> pw.equals(employee.getPassword()), "Invalid password");
        HorizontalLayout newPasswordLayout = new HorizontalLayout(label, updatePassword);
        return newPasswordLayout;
    }

    private Component updateEmployeePasswordCheck() {
        Label label = new Label("Проверка пароля:");
        label.setWidth(labelWidth);
        Binder<EmployeeDto> binder = new Binder<>();
//        EmployeeDto employee = employeeService.getPrincipal();
        updatePasswordCheck.setWidth(fieldWidth);
        updatePasswordCheck.setRequired(true);
        binder.forField(updatePasswordCheck).
                withValidator(pw -> pw.equals(updatePassword.getValue()), "Don't match");
        HorizontalLayout doubleCheck = new HorizontalLayout(label, updatePasswordCheck);
        return doubleCheck;
    }

    private Component getButtonSave() {
        Button buttonSave = new Button("Сохранить");
        buttonSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        EmployeeDto employee = employeeService.getPrincipal();
        buttonSave.addClickListener(click -> {
            if (currentPassword.getValue() == null || currentPassword.getValue() != employee.getPassword()) {
                return;
            } else {
                employee.setPassword(updatePassword.getValue());
            }
            // UI.getCurrent().navigate("logout");
            close();
        });
        return buttonSave;
    }

    private Button getCancelButton() {
        return new Button("Отменить", event -> close());
    }
}
