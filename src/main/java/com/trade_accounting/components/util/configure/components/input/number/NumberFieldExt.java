package com.trade_accounting.components.util.configure.components.input.number;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Для создания объекта NumField через NumberField
 * @Example:
 *          NumberField test = new NumberFieldExt.NumberFieldBuilder()
 *                 .numberFieldLabel("Верхнее поле с описанием")
 *                 .numberFieldValue(2)
 *                 .numberFieldPlaceholder("Ввведи цифру")
 *                 .numberFieldHelperText("Текст для помощи")
 *                 .numberFieldErrorMessage("Ошибка в поле ввода!")
 *                 .numberFieldMin(-10)
 *                 .numberFieldMax(10)
 *                 .numberFieldStep(2.5)
 *                 .numberFieldAttribute("my-attribute", "value")
 *                 .numberFieldThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT)
 *                 .numberFieldPrefixComponent(VaadinIcon.MAP_MARKER.create())
 *                 .numberFieldSuffixComponent(VaadinIcon.CHECK.create())
 *                 .numberFieldHasControls(true)
 *                 .numberFieldAutofocus(false)
 *                 .numberFieldClearButtonVisible(true)
 *                 .numberFieldRequiredIndicatorVisible(true)
 *                 .numberFieldReadOnly(false)
 *                 .numberFieldEnabled(true)
 *                 .build()
 *                 .setNumberFieldStyle("width", "300px");
 *         add(test);
 */
public class NumberFieldExt extends NumberField {

    private NumberFieldExt(NumberFieldBuilder numberFieldBuilder) {
        super();

        if (numberFieldBuilder.label != null) {
            this.setLabel(numberFieldBuilder.label);
        }

        if (numberFieldBuilder.value != 0) {
            this.setValue(numberFieldBuilder.value);
        }

        if (numberFieldBuilder.placeholder != null) {
            this.setPlaceholder(numberFieldBuilder.placeholder);
        }

        if (numberFieldBuilder.helperText != null) {
            this.setHelperText(numberFieldBuilder.helperText);
        }

        if (numberFieldBuilder.errorMessage != null) {
            this.setErrorMessage(numberFieldBuilder.errorMessage);
        }

        this.setMin(numberFieldBuilder.min);

        if (numberFieldBuilder.max > 0) {
            this.setMax(numberFieldBuilder.max);
        }

        if (numberFieldBuilder.step > 0) {
            this.setStep(numberFieldBuilder.step);
        }

        numberFieldBuilder.attribute.forEach((a, v) -> this.getElement().setAttribute(a, v));

        numberFieldBuilder.variant.forEach(this::addThemeVariants);

        if (numberFieldBuilder.prefixComponent != null) {
            this.setPrefixComponent(numberFieldBuilder.prefixComponent);
        }

        if (numberFieldBuilder.suffixComponent != null) {
            this.setPrefixComponent(numberFieldBuilder.suffixComponent);
        }

        this.setHasControls(numberFieldBuilder.hasControls);

        this.setAutofocus(numberFieldBuilder.autofocus);

        this.setClearButtonVisible(numberFieldBuilder.clearButtonVisible);

        this.setRequiredIndicatorVisible(numberFieldBuilder.requiredIndicatorVisible);

        this.setReadOnly(numberFieldBuilder.readOnly);

        this.setEnabled(numberFieldBuilder.enabled);

    }


    public NumberFieldExt setNumberFieldStyle(String var1, String var2) {
        this.getStyle().set(var1, var2);
        return this;
    }

    public NumberFieldExt removeNumberFieldStyle(String var1) {
        this.getStyle().remove(var1);
        return this;
    }

    public NumberFieldExt clearNumberFieldStyle() {
        this.getStyle().clear();
        return this;
    }




    public static class NumberFieldBuilder {
        String label;
        double value;
        String placeholder;
        String helperText;
        String errorMessage;
        double min;
        double max;
        double step;
        Map<String, String> attribute;
        Set<TextFieldVariant> variant;
        Component prefixComponent;
        Component suffixComponent;
        boolean hasControls;
        boolean autofocus;
        boolean clearButtonVisible;
        boolean requiredIndicatorVisible;
        boolean readOnly;
        boolean enabled;


        public NumberFieldBuilder() {
            attribute = new HashMap<>();
            variant = new HashSet<>();
            hasControls = false;
            autofocus = false;
            clearButtonVisible = false;
            requiredIndicatorVisible = false;
            readOnly = false;
            enabled = true;
        }


        /**
         * Добавление текста над полем ввода
         * @param label
         * @return - текущий объект NumberFieldBuilder
         */
        public NumberFieldBuilder numberFieldLabel(String label) {
            this.label = label;
            return this;
        }

        /**
         * Добавление текста по умолчанию в поле ввода
         * @param value
         * @return - текущий объект NumberFieldBuilder
         */
        public NumberFieldBuilder numberFieldValue(double value) {
            this.value = value;
            return this;
        }

        /**
         * Добавление текста подсказки внутри поля ввода
         * @param placeholder
         * @return - текущий объект NumberFieldBuilder
         */
        public NumberFieldBuilder numberFieldPlaceholder(String placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        /**
         * Добавление текста снизу поля ввода
         * @param helperText
         * @return - текущий объект NumberFieldBuilder
         */
        public NumberFieldBuilder numberFieldHelperText(String helperText) {
            this.helperText = helperText;
            return this;
        }

        /**
         * Добавление текста для описания ошибки поля ввода
         * @param errorMessage
         * @return - текущий объект NumberFieldBuilder
         */
        public NumberFieldBuilder numberFieldErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        /**
         * Добавление минимального значения поля ввода
         * @param min
         * @return - текущий объект NumberFieldBuilder
         */
        public NumberFieldBuilder numberFieldMin(double min) {
            this.min = min;
            return this;
        }

        /**
         * Добавление максимального значения поля ввода
         * @param max
         * @return - текущий объект NumberFieldBuilder
         */
        public NumberFieldBuilder numberFieldMax(double max) {
            this.max = max;
            return this;
        }

        /**
         * Добавление шага значения поля ввода
         * @param step
         * @return - текущий объект NumberFieldBuilder
         */
        public NumberFieldBuilder numberFieldStep(double step) {
            this.step = step;
            return this;
        }

        /**
         * Добавление атрибутов поля ввода
         * @param attribute - имя атрибута
         * @param value - значение атрибута
         * @return - текущий объект NumberFieldBuilder
         */
        public NumberFieldBuilder numberFieldAttribute(String attribute, String value) {
            this.attribute.put(attribute, value);
            return this;
        }

        /**
         * Добавление тем для поля ввода
         * @param textFieldVariants
         * @return - текущий объект NumberFieldBuilder
         */
        public NumberFieldBuilder numberFieldThemeVariants(TextFieldVariant... textFieldVariants) {
            variant.addAll(Arrays.asList(textFieldVariants));
            return this;
        }

        /**
         * Добавление преффикса для поля ввода
         * @param component
         * @return - текущий объект NumberFieldBuilder
         */
        public NumberFieldBuilder numberFieldPrefixComponent(Component component) {
            this.prefixComponent = component;
            return this;
        }

        /**
         * Добавление суффикса для поля ввода
         * @param component
         * @return - текущий объект NumberFieldBuilder
         */
        public NumberFieldBuilder numberFieldSuffixComponent(Component component) {
            this.suffixComponent = component;
            return this;
        }

        /**
         * Добавление кнопок -/+ для поля ввода
         * @param enabled
         * @return - текущий объект NumberFieldBuilder
         */
        public NumberFieldBuilder numberFieldHasControls(boolean enabled) {
            this.hasControls = enabled;
            return this;
        }

        /**
         * Добавление добавления автофокуса на поле ввода
         * @param enabled
         * @return - текущий объект NumberFieldBuilder
         */
        public NumberFieldBuilder numberFieldAutofocus(boolean enabled) {
            this.autofocus = enabled;
            return this;
        }

        /**
         * Добавление кнопки очистки поля ввода
         * @param enabled
         * @return - текущий объект NumberFieldBuilder
         */
        public NumberFieldBuilder numberFieldClearButtonVisible(boolean enabled) {
            this.clearButtonVisible = enabled;
            return this;
        }

        /**
         * Добавление индикатора обязательного поля ввода
         * @param enabled
         * @return - текущий объект NumberFieldBuilder
         */
        public NumberFieldBuilder numberFieldRequiredIndicatorVisible(boolean enabled) {
            this.requiredIndicatorVisible = enabled;
            return this;
        }

        /**
         * Добавление ограничения только на чтение поля ввода
         * @param enabled
         * @return - текущий объект NumberFieldBuilder
         */
        public NumberFieldBuilder numberFieldReadOnly(boolean enabled) {
            this.readOnly = enabled;
            return this;
        }

        //Установка отключения поля ввода
        /**
         * Добавление неактивного статуса для поля ввода
         * @param enabled
         * @return - текущий объект NumberFieldBuilder
         */
        public NumberFieldBuilder numberFieldEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        /**
         * Сборка готового поля ввода
         * @param - текущий объект NumberFieldBuilder
         * @return - текущий объект NumberFieldBuilder
         */
        public NumberFieldExt build() {
            return new NumberFieldExt(this);
        }

    }

}
