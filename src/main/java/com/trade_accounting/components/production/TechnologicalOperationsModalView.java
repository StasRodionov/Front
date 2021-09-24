package com.trade_accounting.components.production;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.InternalOrderDto;
import com.trade_accounting.models.dto.TechnicalCardDto;
import com.trade_accounting.models.dto.TechnicalOperationsDto;
import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.TechnicalCardService;
import com.trade_accounting.services.interfaces.TechnicalOperationsService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.Component;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@UIScope
@SpringComponent
public class TechnologicalOperationsModalView extends Dialog {

    private final TechnicalCardService technicalCardService;
    private final TechnicalOperationsService technicalOperationsService;
    private final WarehouseService warehouseService;
    private TechnicalOperationsDto technicalOperationsDto;
    private final Notifications notifications;

    private final ComboBox<TechnicalCardDto> technicalCardComboBox = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseComboBox = new ComboBox<>();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final Checkbox checkboxIsSpend = new Checkbox("Проведено");
    private final Checkbox checkboxIsPrint = new Checkbox("Напечатано");
    private final TextField returnNumber = new TextField();
    private final TextField returnVolume = new TextField();
    private final TextArea textArea = new TextArea();

    private final Binder<TechnicalOperationsDto> technicalOperationsBinder =
            new Binder<>(TechnicalOperationsDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";

    public TechnologicalOperationsModalView(TechnicalCardService technicalCardService, TechnicalOperationsService technicalOperationsService,
                                            WarehouseService warehouseService, Notifications notifications) {
        this.technicalCardService = technicalCardService;
        this.technicalOperationsService = technicalOperationsService;
        this.warehouseService = warehouseService;
        this.notifications = notifications;
        setSizeFull();
        add(headerLayout(), formLayout());
    }

    public void setTechnicalOperationsEdit(TechnicalOperationsDto editDto) {
        this.technicalOperationsDto = editDto;
        returnNumber.setValue(editDto.getId().toString());
        dateTimePicker.setValue(LocalDateTime.parse(editDto.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        textArea.setValue(editDto.getVolume().toString());
        textArea.setValue(editDto.getComment());
        warehouseComboBox.setValue(warehouseService.getById(editDto.getWarehouse()));
        technicalCardComboBox.setValue(technicalCardService.getById(editDto.getTechnicalCard()));

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
        return new H2("Добавление технологической операции");
    }

    private Button saveButton() {
        return new Button("Сохранить", e -> {
            if (!technicalOperationsBinder.validate().isOk()) {
                technicalOperationsBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
                TechnicalOperationsDto dto = new TechnicalOperationsDto();
                dto.setId(Long.parseLong(returnNumber.getValue()));
                dto.setTechnicalCard(technicalCardComboBox.getValue().getId());
                dto.setWarehouse(warehouseComboBox.getValue().getId());
                dto.setDate(dateTimePicker.getValue().toString());
                dto.setIsSent(checkboxIsSpend.getValue());
                dto.setIsPrint(checkboxIsPrint.getValue());
                dto.setComment(textArea.getValue());
                dto.setVolume(Integer.parseInt(returnVolume.getValue()));
                technicalOperationsService.create(dto);

                UI.getCurrent().navigate("technological");
                close();
                clearAllFieldsModalView();
                notifications.infoNotification(String.format("технологическая операция c ID=%s сохранен", dto.getId()));
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
        horizontalLayout.add(technicalCardConfigure(), warehouseConfigure());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout4() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(volumeConfig(), commentConfig());
        return horizontalLayout;
    }

    private HorizontalLayout numberConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Технологическая операция №");
        label.setWidth("150px");
        returnNumber.setWidth("50px");
        horizontalLayout.add(label, returnNumber);
        technicalOperationsBinder.forField(returnNumber)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(TechnicalOperationsDto::getIdValid, TechnicalOperationsDto::setIdValid);
        return horizontalLayout;
    }

    private HorizontalLayout dateConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("От");
        dateTimePicker.setWidth("350px");
        horizontalLayout.add(label, dateTimePicker);
        technicalOperationsBinder.forField(dateTimePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(TechnicalOperationsDto::getDateValid, TechnicalOperationsDto::setDateValid);
        return horizontalLayout;
    }

    private VerticalLayout checkboxLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(checkboxIsSpend, checkboxIsPrint);
        return verticalLayout;
    }

    private HorizontalLayout technicalCardConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<TechnicalCardDto> list = technicalCardService.getAll();
        if (list != null) {
            technicalCardComboBox.setItems(list);
        }
        technicalCardComboBox.setItemLabelGenerator(TechnicalCardDto::getName);
        technicalCardComboBox.setWidth("350px");
        Label label = new Label("Технологическая карта");
        label.setWidth("150px");
        horizontalLayout.add(label, technicalCardComboBox);
        technicalOperationsBinder.forField(technicalCardComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(TechnicalOperationsDto::getTechnicalCardDtoValid, TechnicalOperationsDto::setTechnicalCardDtoValid);
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
        label.setWidth("100px");
        horizontalLayout.add(label, warehouseComboBox);
        technicalOperationsBinder.forField(warehouseComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(TechnicalOperationsDto::getWarehouseDtoValid, TechnicalOperationsDto::setWarehouseDtoValid);
        return horizontalLayout;
    }

    private HorizontalLayout volumeConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Объем производства");
        label.setWidth("100px");
        horizontalLayout.setWidth("550px");
        horizontalLayout.setHeight("50px");
        returnVolume.setWidth("50px");
        horizontalLayout.add(label, returnVolume);
        technicalOperationsBinder.forField(returnVolume);
              //  .asRequired(TEXT_FOR_REQUEST_FIELD)
              //  .bind(TechnicalOperationsDto::getVolumeValid, TechnicalOperationsDto::setVolumeValid);
        return horizontalLayout;
    }

    private HorizontalLayout commentConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Комментарий");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("100px");
        horizontalLayout.add(label, textArea);
        technicalOperationsBinder.forField(textArea);
            //    .asRequired(TEXT_FOR_REQUEST_FIELD)
                //.bind(TechnicalOperationsDto::getCommentValid, TechnicalOperationsDto::setCommentValid);
        return horizontalLayout;
    }

    private void clearAllFieldsModalView() {
        technicalCardComboBox.setValue(null);
        warehouseComboBox.setValue(null);
        dateTimePicker.setValue(null);
        textArea.setValue("");
        returnNumber.setValue("");
        checkboxIsPrint.setValue(false);
        checkboxIsSpend.setValue(false);
    }
}
