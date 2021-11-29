package com.trade_accounting.components.sells;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;

//@SpringComponent
//@UIScope
public class InformationView extends Dialog {

    private static final String LABEL_WIDTH = "300px";
    private static final String WIN_WIDTH = "320px";

    public Button saveButton = new Button();

    public InformationView(String text) {
        Label labelText = new Label(text);
        labelText.setWidth(LABEL_WIDTH);
        Button buttonOK = new Button("OK");

        add(labelText);
        add(buttonOK);

        setWidth(WIN_WIDTH);

        buttonOK.addClickListener(e -> this.close());
    }

}
