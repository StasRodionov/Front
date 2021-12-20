package com.trade_accounting.components.util;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.apache.poi.sl.usermodel.Placeholder;

import java.util.function.Consumer;

public class SearchTextField extends TextField {

    public SearchTextField(String placeholder, String width) {
        this.setPlaceholder(placeholder);
        this.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        this.setValueChangeMode(ValueChangeMode.EAGER);
        this.setClearButtonVisible(true);
        this.setWidth(width);
    }

    public SearchTextField(String placeholder, String width, Consumer<String> updateList) {
        this(placeholder, width);
        this.addValueChangeListener(e -> updateList.accept(this.getValue()));
    }
}
