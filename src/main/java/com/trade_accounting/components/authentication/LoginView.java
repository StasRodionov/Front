package com.trade_accounting.components.authentication;

import com.trade_accounting.components.AppView;
import com.trade_accounting.services.interfaces.AuthenticationService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import org.springframework.beans.factory.annotation.Autowired;

import static com.trade_accounting.config.SecurityConstants.EXPIRATION_TIME;
import static com.trade_accounting.config.SecurityConstants.TOKEN_ATTRIBUTE_NAME;

@Route(value = "login", layout = AppView.class)
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
            UI.getCurrent().navigate("registration");
        });
        registration.getStyle().set("margin", "auto");

        layout.setAlignItems(Alignment.CENTER);
        layout.add(login, registration);
        add(layout);
    }

    private LoginI18n createTradeAccountUniqueI18n() {
        final LoginI18n i18n = LoginI18n.createDefault();

        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Trade Accounting");
        i18n.getHeader().setDescription("Номер 101 в мире");
        i18n.getForm().setUsername("E-mail");
        i18n.getForm().setTitle("");
        i18n.getForm().setSubmit("Войти");
        i18n.getForm().setPassword("Пароль");
        i18n.getForm().setForgotPassword("Забыли пароль?");
        i18n.getErrorMessage().setTitle("Неверный имейл или пароль");
        i18n.getErrorMessage()
                .setMessage("Проверьте их еще раз");
        i18n.setAdditionalInformation("");
        return i18n;
    }
}
