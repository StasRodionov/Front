package com.trade_accounting.components.authentication;

import com.trade_accounting.services.interfaces.AuthenticationService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import org.springframework.beans.factory.annotation.Autowired;

import static com.trade_accounting.config.SecurityConstants.*;

@Route(value = LOGIN)
public class LoginView extends VerticalLayout {

    private final LoginForm login = new LoginForm();
    private VerticalLayout layout = new VerticalLayout();

    public LoginView(@Autowired AuthenticationService authenticationService) {

        layout.add();
        WrappedSession wrappedSession = VaadinSession.getCurrent().getSession();
        login.setI18n(createTradeAccountUniqueI18n());
        login.addLoginListener(e -> {
            LoginRequest loginRequest = new LoginRequest();

            loginRequest.setEmail(e.getUsername());
            loginRequest.setPassword(e.getPassword());

            LoginResponse loginResponse = authenticationService.userLogin(loginRequest);

            if (loginResponse != null) {
                wrappedSession.setAttribute(TOKEN_ATTRIBUTE_NAME, loginResponse.getToken());
                wrappedSession.setMaxInactiveInterval((int) (EXPIRATION_TIME / 1000));
                UI.getCurrent().getPage().setLocation("/");
            } else {
                login.setError(true);
            }
        });
        Button registration = new Button("Зарегистрировться",e ->{
            UI.getCurrent().navigate(REGISTRATION);
        });
        registration.getStyle().set("margin", "auto");

        layout.setAlignItems(Alignment.CENTER);
        layout.add(login, registration);
        add(layout);
    }

    private LoginI18n createTradeAccountUniqueI18n() {

        final LoginI18n i18n = LoginI18n.createDefault();

        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("Trade Accounting");
        i18nForm.setUsername("E-mail");
        i18nForm.setPassword("Пароль");
        i18nForm.setSubmit("Войти");
        i18nForm.setForgotPassword("Забыли пароль?");
        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Неверный имейл или пароль");
        i18nErrorMessage.setMessage("Проверьте их еще раз");
        i18n.setErrorMessage(i18nErrorMessage);

        LoginForm loginForm = new LoginForm();
        loginForm.setI18n(i18n);
        return i18n;
    }
}
