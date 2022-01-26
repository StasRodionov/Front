package com.trade_accounting.components.goods;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.CorrectionDto;
import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.CorrectionService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@UIScope
@SpringComponent
public class PostingModal  extends Dialog {
    CorrectionDto correctionDto;
    private final CorrectionService correctionService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final Notifications notifications;

    private final ComboBox<CompanyDto> companyComboBox = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseComboBox = new ComboBox<>();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final com.vaadin.flow.component.checkbox.Checkbox checkboxIsSent = new Checkbox("Отправленно");
    private final com.vaadin.flow.component.checkbox.Checkbox checkboxIsPrint = new Checkbox("Напечатано");
    private final com.vaadin.flow.component.textfield.TextField returnNumber = new com.vaadin.flow.component.textfield.TextField();
    private final com.vaadin.flow.component.textfield.TextArea textArea = new TextArea();

    private final Binder<CorrectionDto> postingBinder =
            new Binder<>(CorrectionDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";

    public PostingModal(CorrectionService correctionService,
                                        WarehouseService warehouseService,
                                        CompanyService companyService,
                                        Notifications notifications) {
        this.correctionService = correctionService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.notifications = notifications;

        setSizeFull();
        add(headerLayout(), formLayout());

    }
    public void setPostingEdit(CorrectionDto editDto) {
        this.correctionDto = editDto;
        returnNumber.setValue(editDto.getId().toString());
        dateTimePicker.setValue(LocalDateTime.parse(editDto.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        textArea.setValue(editDto.getComment());
        warehouseComboBox.setValue(warehouseService.getById(editDto.getWarehouseId()));
        companyComboBox.setValue(companyService.getById(editDto.getCompanyId()));

    }

    private HorizontalLayout headerLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(title(), saveButton(), closeButton());
        return horizontalLayout;
    }

    private VerticalLayout formLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(formLayout1(), formLayout2(), formLayout4());
        return verticalLayout;
    }

    private H2 title() {
        return new H2("Оприходование");
    }


    private com.vaadin.flow.component.button.Button saveButton() {
        return new com.vaadin.flow.component.button.Button("Сохранить", e -> {
            //save
            if (!postingBinder.validate().isOk()) {
                postingBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
//                InventarizationDto dto = new InventarizationDto();
//                dto.setId(Long.parseLong(returnNumber.getValue()));
//                dto.setCompanyId(companyComboBox.getValue().getId());
//                dto.setWarehouseId(warehouseComboBox.getValue().getId());
//                dto.setDate(dateTimePicker.getValue().toString());
//                dto.setStatus(checkboxIsSent.getValue());
//                dto.setComment(textArea.getValue());
//                inventarizationService.create(dto);
//
//                UI.getCurrent().navigate("inventory");
//                close();
//                clearAllFieldsModalView();
//                notifications.infoNotification(String.format("инвентаризация c ID=%s сохранен", dto.getId()));
            }
        });
    }

    private com.vaadin.flow.component.button.Button closeButton() {
        com.vaadin.flow.component.button.Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> {
            close();
            clearAllFieldsModalView();
        });
        return button;
    }

    private HorizontalLayout formLayout1() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(numberConfigure(), dateConfigure(), checkboxLayout(), checkboxPrintLayout());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout2() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(companyConfigure(), warehouseConfigure());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout4() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(commentConfig());
        return horizontalLayout;
    }

    private HorizontalLayout numberConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("Инвентаризация №");
        label.setWidth("150px");
        returnNumber.setWidth("50px");
        horizontalLayout.add(label, returnNumber);
        postingBinder.forField(returnNumber)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        //
        return horizontalLayout;
    }

    private HorizontalLayout dateConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("От");
        dateTimePicker.setWidth("350px");
        horizontalLayout.add(label, dateTimePicker);
        postingBinder.forField(dateTimePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        //
        return horizontalLayout;
    }

    private VerticalLayout checkboxLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(checkboxIsSent);
        return verticalLayout;
    }

    private VerticalLayout checkboxPrintLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(checkboxIsPrint);
        return verticalLayout;
    }

    private HorizontalLayout companyConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        java.util.List<CompanyDto> list = companyService.getAll();
        if (list != null) {
            companyComboBox.setItems(list);
        }
        companyComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyComboBox.setWidth("350px");
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("Организация");
        label.setWidth("100");
        horizontalLayout.add(label, companyComboBox);
        postingBinder.forField(companyComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        //
        return horizontalLayout;
    }

    private HorizontalLayout warehouseConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<WarehouseDto> list = warehouseService.getAll();
        if (list != null) {
            warehouseComboBox.setItems(list);
        }
        warehouseComboBox.setItemLabelGenerator(WarehouseDto::getName);
        warehouseComboBox.setWidth("350px");
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("Склад");
        label.setWidth("50");
        horizontalLayout.add(label, warehouseComboBox);
        postingBinder.forField(warehouseComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        //
        return horizontalLayout;
    }

    private HorizontalLayout commentConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        com.vaadin.flow.component.html.Label label = new Label("Комментарий");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("100px");
        horizontalLayout.add(label, textArea);
        postingBinder.forField(textArea);
        return horizontalLayout;
    }
    private void clearAllFieldsModalView() {
        companyComboBox.setValue(null);
        warehouseComboBox.setValue(null);
        dateTimePicker.setValue(null);
        textArea.setValue("");
        returnNumber.setValue("");
        checkboxIsSent.setValue(false);
        checkboxIsPrint.setValue(false);
    }
}
