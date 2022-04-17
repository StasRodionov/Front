package com.trade_accounting.components.util.configure.components.button;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class ButtonConfigurer {

    public static ButtonExt configurePrimaryBlueButton(String text){
        return new ButtonExt.ButtonBuilder()
                .buttonText(text)
                .buttonThemeVariants(ButtonVariant.LUMO_PRIMARY)
                .build();
    }

    public static ButtonExt configurePrimaryGreenButton(String text){
        return new ButtonExt.ButtonBuilder()
                .buttonText(text)
                .buttonThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS)
                .build();
    }

    public static ButtonExt configureSecondaryBlueButton(String text){
        return new ButtonExt.ButtonBuilder()
                .buttonText(text)
                .build();
    }

    public static ButtonExt configureSecondaryBlueIconButton(String text, VaadinIcon icon){
        return new ButtonExt.ButtonBuilder()
                .buttonComponent(new Icon(icon))
                .buttonText(text)
                .build();
    }

}
