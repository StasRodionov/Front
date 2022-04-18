package com.trade_accounting.components.authentication;

import com.trade_accounting.components.AppView;
import com.trade_accounting.models.dto.client.AccountDto;
import com.trade_accounting.models.dto.client.EmployeeDto;
import com.trade_accounting.services.interfaces.client.AccountService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "registration")
public class RegistrationView extends VerticalLayout {

    private PasswordField passwordField1;
    private PasswordField passwordField2;


    public RegistrationView(@Autowired AccountService accountService) {
        H2 title = new H2("Signup form");

        TextField firstNameField = new TextField("First name");
        TextField lastNameField = new TextField("Last name");


        EmailField emailField = new EmailField("Email");
        emailField.setVisible(false);

        passwordField1 = new PasswordField("Wanted password");
        passwordField2 = new PasswordField("Password again");

        Span errorMessage = new Span();

        Button submitButton = new Button("Join the community");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        FormLayout formLayout = new FormLayout(title, firstNameField, lastNameField, passwordField1, passwordField2,
                emailField, errorMessage, submitButton);

        formLayout.setMaxWidth("500px");
        formLayout.getStyle().set("margin", "0 auto");

        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("490px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP));

        formLayout.setColspan(title, 2);
        formLayout.setColspan(errorMessage, 2);
        formLayout.setColspan(submitButton, 2);

        errorMessage.getStyle().set("color", "var(--lumo-error-text-color)");
        errorMessage.getStyle().set("padding", "15px 0");

        add(formLayout);

        submitButton.addClickListener(e -> {
            AccountDto accountDto = new AccountDto();
            EmployeeDto employeeDto = new EmployeeDto();
            employeeDto.setFirstName(firstNameField.getValue());
            employeeDto.setLastName(lastNameField.getValue());
            employeeDto.setEmail(emailField.getValue());
            employeeDto.setPassword(passwordField1.getValue());
            accountService.create(accountDto, employeeDto);
        });
    }

}
