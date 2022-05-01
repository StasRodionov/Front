package com.trade_accounting.components.util;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@SpringComponent
public class BookmarkSaveModalView extends Dialog{

    private final TextField titleConfig1 = new TextField();
    private final Notifications notifications;

    public BookmarkSaveModalView(Notifications notifications) {
        this.notifications = notifications;
        setSizeFull();
        add(headerLayout(), formLayout());
    }
    private VerticalLayout formLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(formLayout1());
        return verticalLayout;
    }
    private HorizontalLayout headerLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(title(), save(), closeButton());
        return horizontalLayout;
    }
    private H2 title() {
        H2 title = new H2("Закладки");
        return title;
    }
    private Button save() {
        return new Button("Сохранить закладку", e -> {
            if (titleConfig1.getValue() != null) {
                notifications.infoNotification(String.format("Добавлено в закладки"));
            }
        });
    }

    private Button closeButton() {
        Button button = new Button("Отменить", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> close());
        return button;
    }
    private HorizontalLayout formLayout1() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(titleConfig());
        return horizontalLayout;
    }

    private HorizontalLayout titleConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Комментарий");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("100px");
        horizontalLayout.add(label, titleConfig1);
        return horizontalLayout;
    }
}
