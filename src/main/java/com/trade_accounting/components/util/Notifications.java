package com.trade_accounting.components.util;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class Notifications {

    private final Button ok = new Button("OK");

    private final VerticalLayout verticalLayout = new VerticalLayout(ok);

    private final Notification notification = new Notification();

    public Notifications () {
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        ok.addClickListener(event -> notification.close());
        notification.setPosition(Notification.Position.BOTTOM_END);
    }

    public void infoNotification(String message) {
        notification.removeAll();
        notification.add(new H5(message));
        notification.add(verticalLayout);
        notification.open();
    }

    public void errorNotification(String message) {
        H5 text = new H5(message);
        text.addClassName("my-style");
        notification.removeAll();
        notification.add(text);
        notification.add(verticalLayout);

        String styles = ".my-style { color: red; }";

        StreamRegistration resource = UI.getCurrent().getSession().getResourceRegistry()
                .registerResource(new StreamResource("styles.css", () ->
                        new ByteArrayInputStream(styles.getBytes(StandardCharsets.UTF_8))));

        UI.getCurrent().getPage().addStyleSheet(
                "base://" + resource.getResourceUri().toString());
        notification.open();
    }
}
