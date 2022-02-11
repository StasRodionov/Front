package com.trade_accounting.components.sells;

import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.invoice.InvoiceDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.invoice.InvoiceService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;

import java.time.LocalDateTime;
import java.util.List;

public class SalesModalWinCustomersOrders extends Dialog {

    private final DateTimePicker dateField = new DateTimePicker();
    private final TextField typeOfInvoiceField = new TextField();
    private final Select<CompanyDto> companySelect = new Select<>();
    private final Select<ContractorDto> contractorSelect = new Select<>();
    private final Select<WarehouseDto> warehouseSelect = new Select<>();

    private String labelWidth = "100px";
    private String fieldWidth = "300px";
    private final InvoiceService invoiceService;
    private final ContractorService contractorService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;

    public SalesModalWinCustomersOrders(InvoiceDto invoiceDto,
                                        InvoiceService invoiceService,
                                        ContractorService contractorService,
                                        CompanyService companyService,
                                        WarehouseService warehouseService
    ) {
        this.invoiceService = invoiceService;
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        add(headerOne(), headerTwo());
        if (invoiceDto.getDate() != null) {
            dateField.setValue(LocalDateTime.parse(invoiceDto.getDate()));
        }

        typeOfInvoiceField.setValue(getFieldValueNotNull(invoiceDto.getTypeOfInvoice()));

        if (invoiceDto.getCompanyId() != null) {
            companySelect.setPlaceholder(companyService.getById(invoiceDto.getCompanyId()).getName());
            companySelect.setValue(companyService.getById(invoiceDto.getCompanyId()));
        }

        if (invoiceDto.getContractorId() != null) {
            contractorSelect.setValue(contractorService.getById(invoiceDto.getContractorId()));
            contractorSelect.setPlaceholder(contractorService.getById(invoiceDto.getContractorId()).getName());
        }

        if (invoiceDto.getWarehouseId() != null) {
            warehouseSelect.setPlaceholder(warehouseService.getById(invoiceDto.getWarehouseId()).getName());
            warehouseSelect.setValue(warehouseService.getById(invoiceDto.getWarehouseId()));
        }

    }


    private HorizontalLayout headerOne() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(getSaveButton(), getCloseButton()
//                ,getChangeButton(), getCreateButton(), getPrintButton()
        );
        return horizontalLayout;
    }

    private Button getSaveButton() {
        return new Button("Сохранить", event -> {
            InvoiceDto newInvoiceDto = new InvoiceDto();
            System.out.println("**************************************************************");
            System.out.println(dateField.getValue());
            System.out.println(typeOfInvoiceField.getValue());
            System.out.println(companySelect.getValue());
            System.out.println(contractorSelect.getValue());
            System.out.println(warehouseSelect.getValue());
//            System.out.println(companyField.getValue());
//            newInvoiceDto.setDate(dateField.getValue().toString());
//            newInvoiceDto.setTypeOfInvoice(typeOfInvoiceField.getValue());
//            newInvoiceDto.setCompany(companyField.getValue());
//            invoiceService.create(newInvoiceDto);
//            invoiceService.create(newInvoiceDto);
//            close();
        });
    }

    private Button getCloseButton() {
        return new Button("Закрыть", event -> {
            close();
        });
    }

//    private Button getChangeButton() {
//        return new Button("Изменить");
//    }
//
//    private Button getCreateButton() {
//        return new Button("Сохранить док-т");
//    }
//
//    private Button getPrintButton() {
//        return new Button("Печать");
//    }

    private VerticalLayout headerTwo() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(configureDateField(),
                configureTypeOfInvoiceField(),
                configureCompanySelect(),
                configureContractorSelect(),
                configureWarehouseSelect());
        return verticalLayout;
    }

    private HorizontalLayout configureDateField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Дата");
        label.setWidth(labelWidth);
        dateField.setWidth(fieldWidth);
        horizontalLayout.add(label, dateField);
        return horizontalLayout;
    }

    private HorizontalLayout configureTypeOfInvoiceField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Счет-фактура");
        label.setWidth(labelWidth);
        typeOfInvoiceField.setWidth(fieldWidth);
        horizontalLayout.add(label, typeOfInvoiceField);
        return horizontalLayout;
    }

    private HorizontalLayout configureCompanySelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<CompanyDto> companies = companyService.getAll();
        if (companies != null) {
            companySelect.setItems(companies);
        }
        companySelect.setItemLabelGenerator(CompanyDto::getName);
        companySelect.setWidth(fieldWidth);
        Label label = new Label("Компания");
        label.setWidth(labelWidth);
        horizontalLayout.add(label, companySelect);
        return horizontalLayout;
    }

    private HorizontalLayout configureContractorSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractorDto> contractors = contractorService.getAll();
        if (contractors != null) {
            contractorSelect.setItems(contractors);
        }
        contractorSelect.setItemLabelGenerator(ContractorDto::getName);
        contractorSelect.setWidth(fieldWidth);
        Label label = new Label("Контрагент");
        label.setWidth(labelWidth);
        horizontalLayout.add(label, contractorSelect);
        return horizontalLayout;
    }

    private HorizontalLayout configureWarehouseSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<WarehouseDto> warehouses = warehouseService.getAll();
        if (warehouses != null) {
            warehouseSelect.setItems(warehouses);
        }
        warehouseSelect.setItemLabelGenerator(WarehouseDto::getName);
        warehouseSelect.setWidth(fieldWidth);
        Label label = new Label("Склад");
        label.setWidth(labelWidth);
        horizontalLayout.add(label, warehouseSelect);
        return horizontalLayout;
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }
}
