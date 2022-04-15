package com.trade_accounting.components.util.configure.components.button;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Для создания объекта button через ButtonBuilder
 * @Example:
 *          Button test = new ButtonExt.ButtonBuilder()
 *                 .buttonText("Click me")
 *                 .buttonComponent(new Icon(VaadinIcon.ARROW_RIGHT))
 *                 .buttonAttribute("aria-label", "value")
 *                 .buttonThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR)
 *                 .buttonIconAfterText(true)
 *                 //.buttonEnabled(false)
 *                 .buttonDisableOnClick(true)
 *                 .buttonAutofocus(true)
 *                 .buttonClickListener(event -> {
 *                     new Notification("Hello!").open();
 *                 })
 *                 .build();
 *         add(test);
 */
public class ButtonExt extends Button {

    private ButtonExt(ButtonBuilder buttonBuilder) {
        super();
        //Добавляем текст к кнопке
        if (buttonBuilder.text != null) {
            this.setText(buttonBuilder.text);
        }
        //Добавляем иконку к кнопке
        if (buttonBuilder.icon != null) {
            this.setIcon(buttonBuilder.icon);
        }
        //Добавляем атрибуты к кнопке
        buttonBuilder.attribute.forEach((a, v) -> this.getElement().setAttribute(a, v));
        //Добавляем темы кнопки
        buttonBuilder.variant.forEach(this::addThemeVariants);
        //Задаем расположение текста относительно иконки
        this.setIconAfterText(buttonBuilder.iconAfterText);
        //Добавляем статус кнопки (активна/не активна)
        this.setEnabled(buttonBuilder.enabled);
        //Задаем наличие блокировки множественных кликов по кнопке
        this.setDisableOnClick(buttonBuilder.disableOnClick);
        //Задаем наличие автофокуса на кнопке
        this.setAutofocus(buttonBuilder.autofocus);
        //Задаем событие для кнопки
        if (buttonBuilder.clickListener != null) {
            this.addClickListener(buttonBuilder.clickListener);
        }

    }




    //Класс для сборки кнопки
    public static class ButtonBuilder {
        String text;
        Component icon;
        Map<String, String> attribute;
        Set<ButtonVariant> variant;
        boolean iconAfterText;
        boolean enabled;
        boolean disableOnClick;
        boolean autofocus;
        ComponentEventListener<ClickEvent<Button>> clickListener;


        public ButtonBuilder() {
            attribute = new HashMap<>();
            variant = new HashSet<>();
            iconAfterText = false;
            enabled = true;
            disableOnClick = false;
            autofocus = false;
        }




        /**
         * Добавление текста у кнопки
         * @param text
         * @return - текущий объект ButtonExt
         */
        public ButtonBuilder buttonText(String text) {
            this.text = text;
            return this;
        }

        /**
         * Добавление иконки у кнопки
         * @param icon
         * @return - текущий объект ButtonExt
         */
        public ButtonBuilder buttonComponent(Component icon) {
            this.icon = icon;
            return this;
        }

        /**
         * Добавление атрибутов к кнопке
         * @param attribute - имя атрибута
         * @param value - значение атрибута
         * @return - текущий объект ButtonExt
         */
        public ButtonBuilder buttonAttribute(String attribute, String value) {
            this.attribute.put(attribute, value);
            return this;
        }

        /**
         * Добавление тем для кнопки (может быть несколько)
         * @param buttonVariant
         * @return - текущий объект ButtonExt
         */
        public ButtonBuilder buttonThemeVariants(ButtonVariant... buttonVariant) {
            variant.addAll(Arrays.asList(buttonVariant));
            return this;
        }

        /**
         * Добавление расположения иконки с правой стороны от текста (по дефолту слева)
         * @param enabled
         * @return - текущий объект ButtonExt
         */
        public ButtonBuilder buttonIconAfterText(boolean enabled) {
            iconAfterText = enabled;
            return this;
        }

        /**
         * Добавление неактивного статуса кнопке
         * @param enabled
         * @return - текущий объект ButtonExt
         */
        public ButtonBuilder buttonEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        /**
         * Добавление блокировки множественного клика
         * @param enabled
         * @return - текущий объект ButtonExt
         */
        public ButtonBuilder buttonDisableOnClick(boolean enabled) {
            disableOnClick = enabled;
            return this;
        }

        /**
         * Добавление автофокуса на кнопку
         * @param enabled
         * @return - текущий объект ButtonExt
         */
        public ButtonBuilder buttonAutofocus(boolean enabled) {
            autofocus = enabled;
            return this;
        }

        /**
         * Добавление события по клику на кнопку
         * @param clickListener
         * @return - текущий объект ButtonExt
         */
        public ButtonBuilder buttonClickListener(ComponentEventListener<ClickEvent<Button>> clickListener) {
            this.clickListener = clickListener;
            return this;
        }

        /**
         * Сборка готовой кнопки
         * @param - текущий объект buttonBuilder
         * @return - новый объект ButtonExt
         */
        public ButtonExt build() {
            return new ButtonExt(this);
        }

    }

}
