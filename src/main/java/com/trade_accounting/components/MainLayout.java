package com.trade_accounting.components;

import com.trade_accounting.components.authentication.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;

import static com.trade_accounting.config.SecurityConstants.TOKEN_ATTRIBUTE_NAME;

@Route(value = "main", layout = AppView.class)
@RouteAlias(value = "", layout = AppView.class)
@PageTitle("Главная | CRM")
public class MainLayout extends VerticalLayout{

    public MainLayout() {

        WrappedSession wrappedSession = VaadinSession.getCurrent().getSession();

        if (wrappedSession.getAttribute(TOKEN_ATTRIBUTE_NAME) == null) {
            UI.getCurrent().navigate(LoginView.class);
        }

    }

}
