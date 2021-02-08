package com.trade_accounting.components.authentication;

import com.trade_accounting.components.AppView;
import com.trade_accounting.services.interfaces.AuthenticationService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;

import static com.trade_accounting.config.SecurityConstants.TOKEN_ATTRIBUTE_NAME;
import static com.trade_accounting.config.SecurityConstants.EXPIRATION_TIME;

@Route(value = "login", layout = AppView.class)
public class LoginView extends VerticalLayout {

    private LoginOverlay loginForm = new LoginOverlay();

    private final AuthenticationService authenticationService;

    public LoginView(AuthenticationService authenticationService) {

        this.authenticationService = authenticationService;

        WrappedSession wrappedSession = VaadinSession.getCurrent().getSession();

        loginForm.setOpened(true);

        loginForm.addLoginListener(e -> {
            LoginRequest loginRequest = new LoginRequest();

            loginRequest.setEmail(e.getUsername());
            loginRequest.setPassword(e.getPassword());

            LoginResponse loginResponse = new LoginResponse();
            loginResponse = authenticationService.userLogin(loginRequest);

            if (loginResponse != null) {
                wrappedSession.setAttribute(TOKEN_ATTRIBUTE_NAME, loginResponse.getToken());
                wrappedSession.setMaxInactiveInterval((int) (EXPIRATION_TIME / 1000));
                loginForm.setOpened(false);
                UI.getCurrent().getPage().setLocation("/");
            }

        });
    }
}
