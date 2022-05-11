package com.trade_accounting.components.authentication;


import com.trade_accounting.models.dto.client.AccountDto;
import com.trade_accounting.models.dto.client.EmployeeDto;
import com.trade_accounting.services.interfaces.client.AccountService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.trade_accounting.config.SecurityConstants.REGISTRATION;

@Slf4j
@Route(value = REGISTRATION)
public class RegistrationView extends VerticalLayout {

    private PasswordField passwordField1;
    private PasswordField passwordField2;


    public RegistrationView(@Autowired AccountService accountService) {

        // Картинка на фоне
        StreamResource bodyPicture = new StreamResource("lord.png",
                () -> getImageInputStream("lord.png"));
        Image bodyImage = new Image(bodyPicture, "");
        bodyImage.setId("lord.png");
        bodyImage.setWidth("300px");
        bodyImage.setHeight("300px");
        bodyImage.getStyle()
                .set("position", "relative")
                .set("bottom", "-60px")
                .set("left", "-20px");

        H1 title = new H1("Signup form");

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

        add(formLayout, bodyImage);

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

    // Метод, достающий картинку
    private static InputStream getImageInputStream(String svgIconName) {
        InputStream imageInputStream = null;
        try {
            imageInputStream = new DataInputStream(new FileInputStream("src/main/resources/static/icons/" + svgIconName));
        } catch (IOException ex) {
            log.error("При чтении icon {} произошла ошибка", svgIconName);
        }
        return imageInputStream;
    }

}
