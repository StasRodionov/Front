package com.trade_accounting.components.authentication;

import com.trade_accounting.components.AppView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import static com.trade_accounting.config.SecurityConstants.LOGOUT;

@Route(value = LOGOUT, layout = AppView.class)
public class LogoutView extends VerticalLayout {
    public LogoutView() {
        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().getPage().setLocation("/");
    }
}
