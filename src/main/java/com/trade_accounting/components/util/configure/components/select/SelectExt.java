package com.trade_accounting.components.util.configure.components.select;

import com.vaadin.flow.component.select.Select;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * Для создания объекта select через builder
 * @Example:
 *          new SelectExt.SelectBuilder<String>().itemWithAction("Удалить", ()->{
 *                  *действия при нажатии кнопки*
 *                  }).build();
 * @param <T>
 */
public class SelectExt<T> extends Select<T> {

    Map<T, Action> itemsWithActions;
    Set<T> items;
    T defaultValue;

    private SelectExt(SelectBuilder<T> selectBuilder) {
        super();
        this.itemsWithActions = selectBuilder.itemsWithActions;
        this.items = selectBuilder.items;
        this.items.addAll(itemsWithActions.keySet());
        this.setWidth(selectBuilder.width);
        this.setItems(items);
        this.defaultValue = selectBuilder.defaultValue;
        if (items.contains(defaultValue)){
            this.setValue(defaultValue);
        }
        this.addValueChangeListener(event -> {
            if (Objects.nonNull(itemsWithActions.get(this.getValue()))) {
                itemsWithActions.get(this.getValue()).onChange();
                if (items.contains(defaultValue)){
                    this.setValue(defaultValue);
                }
            }
        });
    }

    public void setActionForItem(T item, Action action){
        itemsWithActions.put(item, action);
    }


    public static class SelectBuilder<T> {

        Map<T, Action> itemsWithActions;
        Set<T> items;
        String width;
        T defaultValue;

        public SelectBuilder() {
            itemsWithActions = new HashMap<>();
            items = new HashSet<>();
        }

        /**
         * Добавление элемента без действия
         * @param item - элемент
         * @return
         */
        public SelectBuilder<T> item(T item) {
            items.add(item);
            return this;
        }

        /**
         * Дефолтное значение select
         * @param value
         * @return
         */
        public SelectBuilder<T> defaultValue(T value){
            this.defaultValue = value;
            return this;
        }
        /**
         * Добавление элемента с действием
         * @param item - элемент
         * @param execute - действие (через функциональный интерфейс Action)
         * @return
         */
        public SelectBuilder<T> itemWithAction(T item, Action execute) {
            itemsWithActions.put(item, execute);
            return this;
        }

        public SelectBuilder<T> width(String width) {
            this.width = width;
            return this;
        }

        public SelectExt<T> build() {
            return new SelectExt<>(this);
        }

    }
}
