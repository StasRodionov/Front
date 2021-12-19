package com.trade_accounting.components.util;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * @author RulleR
 */
public class Modals {

    public static Dialog confirmModal(String text, Button confirmBtn, Button cancelBtn) {
        Dialog dialog = new Dialog();
        confirmBtn.addClickListener(e -> dialog.close());
        cancelBtn.addClickListener(e -> dialog.close());
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        VerticalLayout layout = new VerticalLayout(
                new Text(text),
                new Div(),
                new HorizontalLayout(confirmBtn, new Div(), cancelBtn));
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        dialog.add(layout);
        return dialog;
    }

    public static Dialog infoModal(String text) {
        Dialog dialog = new Dialog();
        VerticalLayout layout = new VerticalLayout(
                new Text(text),
                new Div(),
                new Button("Ок", e -> dialog.close()));
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        dialog.add(layout);
        return dialog;
    }

}
