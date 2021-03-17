package com.trade_accounting.components.sells;

import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.InvoiceService;
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

    private DateTimePicker dateField = new DateTimePicker();
//    private TextField dateField = new TextField();
    private TextField typeOfInvoiceField = new TextField();
//    private TextField companyField = new TextField();
//    private TextField contractorField = new TextField();

    private String labelWidth = "100px";
    private String fieldWidth = "300px";
    private final InvoiceService invoiceService;
    private final ContractorService contractorService;
    private final CompanyService companyService;

    public SalesModalWinCustomersOrders(InvoiceDto invoiceDto,
                                        InvoiceService invoiceService,
                                        ContractorService contractorService,
                                        CompanyService companyService) {
        this.invoiceService = invoiceService;
        this.contractorService = contractorService;
        this.companyService = companyService;
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        if (invoiceDto.getDate() != null) {
            dateField.setValue(LocalDateTime.parse(invoiceDto.getDate()));
        }
        typeOfInvoiceField.setValue(getFieldValueNotNull(invoiceDto.getTypeOfInvoice()));
//        companyField.setValue(getFieldValueNotNull(invoiceDto.getCompany().toString()));
//        contractorField.setValue(getFieldValueNotNull(invoiceDto.getContractor().toString()));
        add(headerOne(), headerTwo());
    }

    private HorizontalLayout headerOne() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(getSaveButton(), getCloseButton(), getChangeButton(), getCreateButton(), getPrintButton());
        return horizontalLayout;
    }

    private Button getSaveButton() {
        return new Button("Сохранить", event -> {
            InvoiceDto newInvoiceDto = new InvoiceDto();
            newInvoiceDto.setDate(dateField.getValue().toString());
            newInvoiceDto.setTypeOfInvoice(typeOfInvoiceField.getValue());
//            newInvoiceDto.setCompany(companyField.getValue());
//            invoiceService.create(newInvoiceDto);
            invoiceService.create(newInvoiceDto);
            close();
        });
    }

    private Button getCloseButton() {
        return new Button("Закрыть", event -> {
            close();
        });
    }

    private Button getChangeButton() {
        return new Button("Изменить");
    }

    private Button getCreateButton() {
        return new Button("Сохранить док-т");
    }

    private Button getPrintButton() {
        return new Button("Печать");
    }

    private VerticalLayout headerTwo() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(configureDateField(),
                configureTypeOfInvoiceField(),
                configureCompanySelect(),
                configureContractorSelect());
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
        Select<CompanyDto> labelSelect = new Select<>();
        List<CompanyDto> companies = companyService.getAll();
        if (companies != null) {
            labelSelect.setItems(companies);
        }
        labelSelect.setItemLabelGenerator(CompanyDto::getName);
        labelSelect.setWidth(fieldWidth);
        Label label = new Label("Компания");
        label.setWidth(labelWidth);
        horizontalLayout.add(label, labelSelect);
        return horizontalLayout;
    }

    private HorizontalLayout configureContractorSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Select<ContractorDto> labelSelect = new Select<>();
        //labelSelect.setItems(contractorService.getAll());//для теста
        labelSelect.setItems(contractorService.getAllString());//для теста
        labelSelect.setItemLabelGenerator(ContractorDto::getName);
        labelSelect.setWidth(fieldWidth);
        Label label = new Label("Контрагент");
        label.setWidth(labelWidth);
        horizontalLayout.add(label, labelSelect);
        return horizontalLayout;
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }
}
