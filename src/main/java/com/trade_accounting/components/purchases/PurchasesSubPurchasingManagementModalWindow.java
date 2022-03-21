package com.trade_accounting.components.purchases;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.invoice.InvoicesStatusDto;
import com.trade_accounting.models.dto.warehouse.InventarizationDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.warehouse.InventarizationService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@UIScope
@SpringComponent
//@Route(value = "inventory", layout = AppView.class)
public class PurchasesSubPurchasingManagementModalWindow extends Dialog {

    private final InventarizationService inventarizationService;
    private InventarizationDto inventarizationDto = new InventarizationDto();
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final Notifications notifications;

    private final ComboBox<CompanyDto> companyComboBox = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseComboBox = new ComboBox<>();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final Checkbox checkboxIsSent = new Checkbox("Отправленно");
    private final TextField returnNumber = new TextField();
    private final TextArea textArea = new TextArea();
    private final H2 title = new H2("Добавление заказа");
    private final Button buttonDelete = new Button("Удалить", new Icon(VaadinIcon.TRASH));
    private final Grid<InventarizationDto> grid = new Grid<>(InventarizationDto.class, false);

    private final Binder<InventarizationDto> inventarizationBinder =
            new Binder<>(InventarizationDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";
    private String location;
    private String type;

    public PurchasesSubPurchasingManagementModalWindow(InventarizationService inventarizationService,
                                                       WarehouseService warehouseService,
                                                       CompanyService companyService,
                                                       Notifications notifications) {
        this.inventarizationService = inventarizationService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.notifications = notifications;

        setSizeFull();
        add(headerLayout(), formLayout());

    }
    public void setInventarizationEdit(InventarizationDto editDto) {
        //update
        this.inventarizationDto = editDto;
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
        return new H2("Инвентаризация");
    }


    private Button saveButton() {
        return new Button("Сохранить", e -> {
        //save
            if (!inventarizationBinder.validate().isOk()) {
                inventarizationBinder.validate().notifyBindingValidationStatusHandlers();
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

    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> {
            close();
            clearAllFieldsModalView();
        });
        return button;
    }

    private HorizontalLayout formLayout1() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(numberConfigure(), dateConfigure(), checkboxLayout());
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
        Label label = new Label("Инвентаризация №");
        label.setWidth("150px");
        returnNumber.setWidth("50px");
        horizontalLayout.add(label, returnNumber);
        inventarizationBinder.forField(returnNumber)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
            //
        return horizontalLayout;
    }

    private HorizontalLayout dateConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("От");
        dateTimePicker.setWidth("350px");
        horizontalLayout.add(label, dateTimePicker);
        inventarizationBinder.forField(dateTimePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        //
        return horizontalLayout;
    }

    private VerticalLayout checkboxLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(checkboxIsSent);
        return verticalLayout;
    }

    private HorizontalLayout companyConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<CompanyDto> list = companyService.getAll();
        if (list != null) {
            companyComboBox.setItems(list);
        }
        companyComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyComboBox.setWidth("350px");
        Label label = new Label("Организация");
        label.setWidth("100");
        horizontalLayout.add(label, companyComboBox);
        inventarizationBinder.forField(companyComboBox)
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
        Label label = new Label("Склад");
        label.setWidth("50");
        horizontalLayout.add(label, warehouseComboBox);
        inventarizationBinder.forField(warehouseComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        //
        return horizontalLayout;
    }

    private HorizontalLayout commentConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Комментарий");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("100px");
        horizontalLayout.add(label, textArea);
        inventarizationBinder.forField(textArea);
        return horizontalLayout;
    }
    private void clearAllFieldsModalView() {
        companyComboBox.setValue(null);
        warehouseComboBox.setValue(null);
        dateTimePicker.setValue(null);
        textArea.setValue("");
        returnNumber.setValue("");
        checkboxIsSent.setValue(false);
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setType(String type) {
        this.type = type;
    }

    public void setUpdateState(boolean isUpdate) {
        title.setText(isUpdate ? "Редактирование заказа" : "Добавление заказа");
        buttonDelete.setVisible(isUpdate);
    }

    public void resetView() {

    }
}
