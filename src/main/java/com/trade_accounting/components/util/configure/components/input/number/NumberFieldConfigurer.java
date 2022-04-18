package com.trade_accounting.components.util.configure.components.input.number;

import com.vaadin.flow.component.textfield.TextFieldVariant;

public class NumberFieldConfigurer {

    public static NumberFieldExt configureNumberFieldWithPlaceholderAndAlignRight(String placeholder){
        return new NumberFieldExt.NumberFieldBuilder()
                .numberFieldPlaceholder(placeholder)
                .numberFieldThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT)
                .build();
    }

}
