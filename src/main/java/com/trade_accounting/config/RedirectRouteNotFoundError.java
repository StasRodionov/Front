package com.trade_accounting.config;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.errors.NotFoundErrorView;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.ParentLayout;

import javax.servlet.http.HttpServletResponse;

@ParentLayout(AppView.class)
public class RedirectRouteNotFoundError extends VerticalLayout implements HasErrorParameter<NotFoundException> {

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {

        //Если запрашиваемая страница не найдена, то выводит страницу с 404 ошибкой
        event.rerouteTo(NotFoundErrorView.class);
        return HttpServletResponse.SC_NOT_FOUND;

    }
}

