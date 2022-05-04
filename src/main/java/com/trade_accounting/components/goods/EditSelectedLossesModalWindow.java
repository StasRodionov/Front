package com.trade_accounting.components.goods;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.finance.LossDto;
import com.trade_accounting.models.dto.invoice.InternalOrderDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.finance.LossService;
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
import com.vaadin.flow.data.binder.Binder;

import java.util.List;
import java.util.Set;

import static com.trade_accounting.config.SecurityConstants.GOODS_LOSS_VIEW;

public class EditSelectedLossesModalWindow extends Dialog {

    private final LossService lossService;
    private final Notifications notifications;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final Set<LossDto> listOfLosses;

    private final ComboBox<CompanyDto> companyDtoComboBox = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseDtoComboBox = new ComboBox<>();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final Binder<LossDto> lossDtoBinder =
            new Binder<>(LossDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";


    public EditSelectedLossesModalWindow(LossService lossService, Notifications notifications, CompanyService companyService, WarehouseService warehouseService, Set<LossDto> listOfLosses) {
        this.lossService = lossService;
        this.notifications = notifications;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.listOfLosses = listOfLosses;

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
        return new H2("Массовое редактирование списаний");
    }

    private Button saveButton() {
        return new Button("Сохранить", e -> {

            for (LossDto dto : listOfLosses) {

                dto.setDate(dateTimePicker.getValue().toString());
                dto.setCompanyId(companyDtoComboBox.getValue().getId());

                if (warehouseDtoComboBox.getValue() != null) {
                    dto.setWarehouseId(warehouseDtoComboBox.getValue().getId());
                }

                if (companyDtoComboBox.getValue() != null) {
                    dto.setCompanyId(companyDtoComboBox.getValue().getId());
                }

                if (dateTimePicker.getValue() != null) {
                    dto.setDate(dateTimePicker.getValue().toString());
                }

                lossService.create(dto);
            }
            notifications.infoNotification("Выбранные списания успешно отредактированы");
            close();
            UI.getCurrent().navigate(GOODS_LOSS_VIEW);
        });
    }

    private HorizontalLayout dateConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("От");
        dateTimePicker.setWidth("350px");
        horizontalLayout.add(label, dateTimePicker);
        lossDtoBinder.forField(dateTimePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(LossDto::getDateValid, LossDto::setDateValid);
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
        lossDtoBinder.forField(companyDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(LossDto::getCompanyDtoValid, LossDto::setCompanyDtoValid);
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
        lossDtoBinder.forField(warehouseDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(LossDto::getWarehouseDtoValid, LossDto::setWarehouseDtoValid);
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
