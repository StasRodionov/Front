package com.trade_accounting.components.purchases;


import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.ReturnToSupplierDto;
import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.ReturnToSupplierService;
import com.trade_accounting.services.interfaces.WarehouseService;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.time.LocalDateTime;
import java.util.List;

@UIScope
@SpringComponent
public class ReturnToSupplierModalView extends Dialog {

    private final ReturnToSupplierService returnToSupplierService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final ContractorService contractorService;
    private final ContractService contractService;
    private ReturnToSupplierDto dto;

    private final ComboBox<CompanyDto> companyDtoComboBox = new ComboBox<>();
    private final ComboBox<ContractorDto> contractorDtoComboBox = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseDtoComboBox = new ComboBox<>();
    private final ComboBox<ContractDto> contractDtoComboBox = new ComboBox<>();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final Checkbox checkboxIsSpend = new Checkbox("Проведено");
    private final Checkbox checkboxIsPrint = new Checkbox("Напечатано");
    private final TextField returnNumber = new TextField();
    private final TextField commentConfig = new TextField();

    private final Notifications notifications;

    public ReturnToSupplierModalView(ReturnToSupplierService returnToSupplierService, CompanyService companyService,
                                     WarehouseService warehouseService, ContractorService contractorService,
                                     ContractService contractService, Notifications notifications) {
        this.returnToSupplierService = returnToSupplierService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.contractorService = contractorService;
        this.contractService = contractService;
        this.notifications = notifications;
        setSizeFull();
        add(headerLayout(), formLayout());
    }

    public void setReturnToSupplierForEdit(ReturnToSupplierDto editDto) {
        this.dto = editDto;
        returnNumber.setValue(editDto.getId().toString());
        dateTimePicker.setValue(LocalDateTime.parse(editDto.getDate()));
        commentConfig.setValue(editDto.getComment());
        companyDtoComboBox.setValue(companyService.getById(editDto.getCompanyId()));
        warehouseDtoComboBox.setValue(warehouseService.getById(editDto.getWarehouseId()));
        contractDtoComboBox.setValue(contractService.getById(editDto.getContractId()));
        contractorDtoComboBox.setValue(contractorService.getById(editDto.getContractorId()));
    }

    private HorizontalLayout headerLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(title(), saveButton(), closeButton(), addGoodButton());
        return horizontalLayout;
    }

    private VerticalLayout formLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(formLayout1(), formLayout2(), formLayout3());
        return verticalLayout;
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


    private HorizontalLayout formLayout3() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(contractorConfigure());
        return horizontalLayout;
    }


    private H2 title() {
        H2 title = new H2("Добавление возврата");
        return title;
    }

    private Button saveButton() {
        return new Button("Сохранить", e -> {
            ReturnToSupplierDto dto = new ReturnToSupplierDto();
            dto.setId(Long.parseLong(returnNumber.getValue()));
            dto.setCompanyId(companyDtoComboBox.getValue().getId());
            dto.setContractId(contractDtoComboBox.getValue().getId());
            dto.setWarehouseId(warehouseDtoComboBox.getValue().getId());
            dto.setContractorId(contractorDtoComboBox.getValue().getId());
            dto.setDate(dateTimePicker.getValue().toString());
            dto.setIsSend(checkboxIsSpend.getValue());
            dto.setIsPrint(checkboxIsPrint.getValue());
            returnToSupplierService.create(dto);

            UI.getCurrent().navigate("returnsToSuppliers");
            close();
            notifications.infoNotification(String.format("Возврат поставщику № %s сохранен", dto.getId()));
        });
    }

    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> {
            close();
        });
        return button;
    }

    private Button addGoodButton() {
        Button button = new Button("Добавить продукт", new Icon(VaadinIcon.PLUS));
        button.addClickListener(e -> {
            // Добавить продукт в таблицу
        });
        return button;
    }

    private HorizontalLayout numberConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Возврат поставщику №");
        label.setWidth("150px");
        returnNumber.setWidth("50px");
        horizontalLayout.add(label, returnNumber);
        return horizontalLayout;
    }

    private HorizontalLayout dateConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("От");
        dateTimePicker.setWidth("350px");
        horizontalLayout.add(label, dateTimePicker);
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
            companyDtoComboBox.setItems(list);
        }
        companyDtoComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyDtoComboBox.setWidth("350px");
        Label label = new Label("Организация");
        label.setWidth("100px");
        horizontalLayout.add(label, companyDtoComboBox);
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
        return horizontalLayout;
    }

    private HorizontalLayout contractorConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractorDto> list = contractorService.getAll();
        if (list != null) {
            contractorDtoComboBox.setItems(list);
        }
        contractorDtoComboBox.setItemLabelGenerator(ContractorDto::getName);
        contractorDtoComboBox.setWidth("350px");
        Label label = new Label("Контрагент");
        label.setWidth("100px");
        horizontalLayout.add(label, contractorDtoComboBox);
        return horizontalLayout;
    }

}
