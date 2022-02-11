package com.trade_accounting.components.goods;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.invoice.InternalOrderDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.invoice.InternalOrderService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.util.List;
import java.util.Set;

public class EditSelectedModalWindow extends Dialog {

    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final InternalOrderService internalOrderService;

    private final ComboBox<CompanyDto> companyDtoComboBox = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseDtoComboBox = new ComboBox<>();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final TextField returnNumber = new TextField();
    private final Set<InternalOrderDto> list;

    private final Binder<InternalOrderDto> internalOrderDtoBinder =
            new Binder<>(InternalOrderDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";
    private final Notifications notifications;


    public EditSelectedModalWindow(CompanyService companyService, WarehouseService warehouseService,
                                   InternalOrderService internalOrderService,
                                   Notifications notifications, Set<InternalOrderDto> list) {
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.internalOrderService = internalOrderService;
        this.notifications = notifications;
        this.list = list;
        setSizeFull();
        add(headerLayout(), formLayout());
    }

    private VerticalLayout formLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(formLayout1(), formLayout2());
        return verticalLayout;
    }

    private HorizontalLayout formLayout1() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(dateConfigure());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout2() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(companyConfigure(), warehouseConfigure());
        return horizontalLayout;
    }

    private HorizontalLayout headerLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(title(), saveButton(), closeButton());
        return horizontalLayout;
    }

    private H2 title() {
        return new H2("Массовое редактирование заказов");
    }

    private Button saveButton() {
        return new Button("Сохранить", e -> {

                for(InternalOrderDto dto : list) {

                    if(returnNumber.getValue() != "") {
                        dto.setId(Long.parseLong(returnNumber.getValue()));
                        dto.setInternalOrderProductsIds(dto.getInternalOrderProductsIds());
                        dto.setWarehouseId(warehouseDtoComboBox.getValue().getId());
                        dto.setDate(dateTimePicker.getValue().toString());
                        dto.setCompanyId(companyDtoComboBox.getValue().getId());
                    }

                    if(warehouseDtoComboBox.getValue() == null) {
                        dto.setWarehouseId(dto.getWarehouseId());
                    } else {
                        dto.setWarehouseId(warehouseDtoComboBox.getValue().getId());
                    }

                    if(companyDtoComboBox.getValue() == null) {
                        dto.setCompanyId(dto.getCompanyId());
                    } else {
                        dto.setCompanyId(companyDtoComboBox.getValue().getId());
                    }

                    if(dateTimePicker.getValue() == null) {
                        dto.setDate(dto.getDate());
                    } else {
                        dto.setDate(dateTimePicker.getValue().toString());
                    }

                    internalOrderService.create(dto);
                }
                notifications.infoNotification("Выбранные заказы успешно отредактированы");
                close();
                UI.getCurrent().navigate("internalorder");
        });
    }

    private HorizontalLayout dateConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("От");
        dateTimePicker.setWidth("350px");
        horizontalLayout.add(label, dateTimePicker);
        internalOrderDtoBinder.forField(dateTimePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(InternalOrderDto::getDateValid, InternalOrderDto::setDateValid);
        return horizontalLayout;
    }

    private HorizontalLayout companyConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<CompanyDto> list = companyService.getAll();
        if (list != null) {
            companyDtoComboBox.setItems(list);
        }
        companyDtoComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyDtoComboBox.setWidth("350px");
        Label label = new Label("Организация");
        label.setWidth("100px");
        horizontalLayout.add(label, companyDtoComboBox);
        internalOrderDtoBinder.forField(companyDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(InternalOrderDto::getCompanyDtoValid, InternalOrderDto::setCompanyDtoValid);
        return horizontalLayout;
    }

    private HorizontalLayout warehouseConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<WarehouseDto> list = warehouseService.getAll();
        if (list != null) {
            warehouseDtoComboBox.setItems(list);
        }
        warehouseDtoComboBox.setItemLabelGenerator(WarehouseDto::getName);
        warehouseDtoComboBox.setWidth("350px");
        Label label = new Label("Склад");
        label.setWidth("100px");
        horizontalLayout.add(label, warehouseDtoComboBox);
        internalOrderDtoBinder.forField(warehouseDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(InternalOrderDto::getWarehouseDtoValid, InternalOrderDto::setWarehouseDtoValid);
        return horizontalLayout;
    }

    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> {
            close();
        });
        return button;
    }
}
