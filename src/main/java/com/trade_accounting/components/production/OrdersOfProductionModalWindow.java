package com.trade_accounting.components.production;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.InternalOrderDto;
import com.trade_accounting.models.dto.OrdersOfProductionDto;
import com.trade_accounting.models.dto.TechnicalCardDto;
import com.trade_accounting.models.dto.TechnicalOperationsDto;
import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.OrdersOfProductionService;
import com.trade_accounting.services.interfaces.TechnicalCardService;
import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@UIScope
@SpringComponent
public class OrdersOfProductionModalWindow  extends Dialog {

    private final TechnicalCardService technicalCardService;
    private final CompanyService companyService;
    private final OrdersOfProductionService ordersOfProductionService;
    private final Notifications notifications;
    private OrdersOfProductionDto ordersOfProductionDto;

    private final ComboBox<TechnicalCardDto> technicalCardComboBox = new ComboBox<>();
    private final ComboBox<CompanyDto> companyComboBox = new ComboBox<>();
    private final DateTimePicker datePicker = new DateTimePicker();
    private final DateTimePicker plannedProductionDatePicker = new DateTimePicker();
    private final Checkbox checkboxIsSpend = new Checkbox("Проведено");
    private final Checkbox checkboxIsPrint = new Checkbox("Напечатано");
    private final TextField numberField = new TextField();
    private final TextField volumeField = new TextField();
    private final TextField produceField = new TextField();
    private final TextArea textArea = new TextArea();

    private final Binder<OrdersOfProductionDto> ordersOfProductionBinder =
            new Binder<>(OrdersOfProductionDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";

    public OrdersOfProductionModalWindow(TechnicalCardService technicalCardService,
                                         CompanyService companyService,
                                         OrdersOfProductionService ordersOfProductionService,
                                         Notifications notifications) {

        this.technicalCardService = technicalCardService;
        this.companyService = companyService;
        this.ordersOfProductionService = ordersOfProductionService;
        this.notifications = notifications;


        setSizeFull();
        add(headerLayout(), formLayout());
    }

    public void setOrdersOfProductionEdit(OrdersOfProductionDto editDto) {
        this.ordersOfProductionDto = editDto;
        numberField.setValue(editDto.getId().toString());
        datePicker.setValue(LocalDateTime.parse(editDto.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        plannedProductionDatePicker.setValue(LocalDateTime.parse(editDto.getPlannedProductionDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        volumeField.setValue(editDto.getVolume().toString());
        produceField.setValue(editDto.getProduce().toString());
        textArea.setValue(editDto.getComment());
        technicalCardComboBox.setValue(technicalCardService.getById(editDto.getTechnicalCardId()));
        companyComboBox.setValue(companyService.getById(editDto.getCompanyId()));
    }

    private HorizontalLayout headerLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(title(), saveButton(), closeButton());
        return horizontalLayout;
    }

    private VerticalLayout formLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(formLayout1(), formLayout2(), formLayout3(), formLayout4(), formLayout5());
        return verticalLayout;
    }

    private HorizontalLayout formLayout1() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(numberConfigure(), dateConfigure(), checkboxLayout());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout2() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(companyConfigure(), technicalCardConfigure());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout3() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(plannedProductionDateConfigure());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout4() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(produceConfig(), volumeConfig());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout5() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(commentConfig());
        return horizontalLayout;
    }


    private H2 title() {
        return new H2("Добавление заказа на производство");
    }

    private Button saveButton() {
        return new Button("Сохранить", e -> {
            if (!ordersOfProductionBinder.validate().isOk()) {
                ordersOfProductionBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
                OrdersOfProductionDto dto = new OrdersOfProductionDto();
                dto.setId(Long.parseLong(numberField.getValue()));
                dto.setCompanyId(companyComboBox.getValue().getId());
                dto.setTechnicalCardId(technicalCardComboBox.getValue().getId());
                dto.setComment(textArea.getValue());
                dto.setDate(datePicker.getValue().toString());
                dto.setPlannedProductionDate(plannedProductionDatePicker.getValue().toString());
                dto.setIsPrint(checkboxIsPrint.getValue());
                dto.setIsSent(checkboxIsSpend.getValue());
                dto.setProduce(Integer.parseInt(produceField.getValue()));
                dto.setVolume(Integer.parseInt(volumeField.getValue()));
                ordersOfProductionService.create(dto);

                UI.getCurrent().navigate("ordersOfProductionViewTab");
                close();
                clearAllFieldsModalView();
                notifications.infoNotification(String.format("Заказ на производство c ID=%s сохранен", dto.getId()));
            }
        });
    }

    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE_BIG));
        button.addClickListener(e -> {
            close();
            clearAllFieldsModalView();
        });
        return button;
    }

    private HorizontalLayout numberConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Заказ на производство №");
        label.setWidth("100px");
        numberField.setWidth("50px");
        horizontalLayout.add(label, numberField);
        ordersOfProductionBinder.forField(numberField)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(OrdersOfProductionDto::getIdValid, OrdersOfProductionDto::setIdValid);
        return horizontalLayout;
    }

    private HorizontalLayout dateConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("от");
        datePicker.setWidth("350px");
        horizontalLayout.add(label, datePicker);
        ordersOfProductionBinder.forField(datePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(OrdersOfProductionDto::getDateValid, OrdersOfProductionDto::setDateValid);
        return horizontalLayout;
    }
    private HorizontalLayout plannedProductionDateConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("План. дата производства");
        label.setWidth("100px");
        plannedProductionDatePicker.setWidth("350px");
        horizontalLayout.add(label, plannedProductionDatePicker);
        ordersOfProductionBinder.forField(plannedProductionDatePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(OrdersOfProductionDto::getPlannedProductionDateValid, OrdersOfProductionDto::setPlannedProductionDateValid);
        return horizontalLayout;
    }

    private VerticalLayout checkboxLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(checkboxIsSpend, checkboxIsPrint);
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
        label.setWidth("100px");
        horizontalLayout.add(label, companyComboBox);
        ordersOfProductionBinder.forField(companyComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(OrdersOfProductionDto::getCompanyDtoValid, OrdersOfProductionDto::setCompanyDtoValid);
        return horizontalLayout;
    }

    private HorizontalLayout technicalCardConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<TechnicalCardDto> list = technicalCardService.getAll();
        if (list != null) {
            technicalCardComboBox.setItems(list);
        }
        technicalCardComboBox.setItemLabelGenerator(TechnicalCardDto::getName);
        technicalCardComboBox.setWidth("350px");
        Label label = new Label("Склад");
        label.setWidth("50px");
        horizontalLayout.add(label, technicalCardComboBox);
        ordersOfProductionBinder.forField(technicalCardComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(OrdersOfProductionDto::getTechnicalCardDtoValid, OrdersOfProductionDto::setTechnicalCardDtoValid);
        return horizontalLayout;
    }
    private HorizontalLayout commentConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Комментарий");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("100px");
        textArea.setWidth("350px");
        horizontalLayout.add(label, textArea);
        return horizontalLayout;
    }

    private HorizontalLayout volumeConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Объем производства");
        label.setWidth("100px");
        volumeField.setWidth("50px");
        horizontalLayout.add(label, volumeField);
        ordersOfProductionBinder.forField(volumeField);
        return horizontalLayout;
    }

    private HorizontalLayout produceConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Произведенно");
        label.setWidth("100px");
        horizontalLayout.setWidth("280px");
        produceField.setWidth("50px");
        horizontalLayout.add(label, produceField);
        ordersOfProductionBinder.forField(produceField);
        return horizontalLayout;
    }

    private void clearAllFieldsModalView() {
        companyComboBox.setValue(null);
        technicalCardComboBox.setValue(null);
        datePicker.setValue(null);
        plannedProductionDatePicker.setValue(null);
        textArea.setValue("");
        numberField.setValue("");
        volumeField.setValue("");
        produceField.setValue("");
        checkboxIsPrint.setValue(false);
        checkboxIsSpend.setValue(false);
    }
}