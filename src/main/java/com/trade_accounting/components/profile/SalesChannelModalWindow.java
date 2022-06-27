package com.trade_accounting.components.profile;

import com.trade_accounting.models.dto.units.SalesChannelDto;
import com.trade_accounting.services.interfaces.units.SalesChannelService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;


public class SalesChannelModalWindow extends Dialog {

    private TextArea nameField = new TextArea();

    private TextArea typeField = new TextArea();

    private TextArea descriptionField = new TextArea();

    private Long id;

    private final SalesChannelService salesChannelService;

    private final String labelWidth = "100px";

    private final String fieldWidth = "400px";

    public SalesChannelModalWindow(SalesChannelDto salesChannelDto, SalesChannelService salesChannelService) {
        this.salesChannelService = salesChannelService;

        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        id = salesChannelDto.getId();

        nameField.setValue(getFieldValueNotNull(salesChannelDto.getName()));
        typeField.setValue(getFieldValueNotNull(salesChannelDto.getType()));
        descriptionField.setValue(getFieldValueNotNull(salesChannelDto.getDescription()));

        add(header(), configureNameField(), configureTypeField(), configureDescriptionField());

    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        header.add(getSaveButton(), getCancelButton(), getDeleteButton());
        return header;
    }

    private HorizontalLayout configureNameField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Наименование");
        label.setWidth(labelWidth);
        nameField.setWidth(fieldWidth);
        nameField.getStyle().set("minHeight", "30px");
        horizontalLayout.add(label, nameField);
        return horizontalLayout;
    }

    private HorizontalLayout configureTypeField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Тип");
        label.setWidth(labelWidth);
        typeField.setWidth(fieldWidth);
        typeField.getStyle().set("minHeight", "30px");
        horizontalLayout.add(label, typeField);
        return horizontalLayout;
    }

    private HorizontalLayout configureDescriptionField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Описание");
        label.setWidth(labelWidth);
        descriptionField.setWidth(fieldWidth);
        descriptionField.getStyle().set("minHeight", "60px");
        horizontalLayout.add(label, descriptionField);
        return horizontalLayout;
    }

    private Button getSaveButton() {
        return new Button("Сохранить", event -> {
            SalesChannelDto salesChannelDto = new SalesChannelDto();

            salesChannelDto.setId(id);
            salesChannelDto.setName(nameField.getValue());
            salesChannelDto.setType(typeField.getValue());
            salesChannelDto.setDescription(descriptionField.getValue());
            if (salesChannelDto.getId() == null) {
                salesChannelService.create(salesChannelDto);
            } else {
                salesChannelService.update(salesChannelDto);
            }
            close();
        });
    }


    private Button getCancelButton() {
        Button cancelButton = new Button("Закрыть", event -> {
            close();
        });
        return cancelButton;
    }

    private Button getDeleteButton() {
        Button deleteButton = new Button("Удалить", event -> {
            salesChannelService.deleteById(id);
            close();
        });
        return deleteButton;
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }

}
