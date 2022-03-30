package com.trade_accounting.components.util;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class Buttons {

    private Buttons() {
        throw new IllegalStateException("Buttons class");
    }

    public static Button buttonQuestion(Component ... components) {
        HorizontalLayout layout = new HorizontalLayout(new Div(components));
        var notification = new Notification(layout);
        notification.setPosition(Notification.Position.TOP_START);
        notification.setDuration(Integer.MAX_VALUE);

        Button close = new Button(new Icon(VaadinIcon.CLOSE));
        close.setWidth("30px");
        close.addClickListener(e -> notification.close());
        Shortcuts.addShortcutListener(notification, notification::close, Key.ESCAPE);

        layout.add(close);
        notification.add(layout);

        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        buttonQuestion.addClickListener(event -> notification.open());

        return buttonQuestion;
    }

    public static Button buttonQuestion(String message) {
        return buttonQuestion(new Text(message));
    }
}
