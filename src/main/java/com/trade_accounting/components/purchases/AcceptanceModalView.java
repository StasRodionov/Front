package com.trade_accounting.components.purchases;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.AcceptanceDto;
import com.trade_accounting.models.dto.BuyersReturnDto;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.SupplierAccountDto;
import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.AcceptanceService;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
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
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@UIScope
@SpringComponent
public class AcceptanceModalView extends Dialog {

    private final CompanyService companyService;
    private final AcceptanceService acceptanceService;
    private final ContractService contractService;
    private final WarehouseService warehouseService;
    private final ContractorService contractorService;
    private AcceptanceDto dto = new AcceptanceDto();
    private final ComboBox<ContractDto> contractDtoComboBox = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseDtoComboBox = new ComboBox<>();
    private final ComboBox<ContractorDto> contractorDtoComboBox = new ComboBox<>();
    private final ComboBox<CompanyDto> companyDtoComboBox = new ComboBox<>();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final Checkbox checkboxIsSent = new Checkbox("Отправлено");
    private final Checkbox checkboxIsPrint = new Checkbox("Напечатано");
    private final TextField returnNumber = new TextField();
    private final TextArea textArea = new TextArea();

    private final Binder<AcceptanceDto> acceptanceDtoBinder =
            new Binder<>(AcceptanceDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";
    private final Notifications notifications;

    public AcceptanceModalView(CompanyService companyService,
                               AcceptanceService acceptanceService,
                               ContractService contractService,
                               WarehouseService warehouseService,
                               ContractorService contractorService,
                               Notifications notifications) {
        this.companyService = companyService;
        this.acceptanceService = acceptanceService;
        this.contractService = contractService;
        this.warehouseService = warehouseService;
        this.contractorService = contractorService;
        this.notifications = notifications;
        setSizeFull();
        add(headerLayout(), formLayout());
    }

    private void updateSupplier() {
        dto.setId(Long.parseLong(returnNumber.getValue()));
        dto.setWarehouseId(warehouseDtoComboBox.getValue().getId());
        dto.setDate(dateTimePicker.getValue().toString());
        dto.setContractId(contractDtoComboBox.getValue().getId());
        dto.setCompanyId(contractDtoComboBox.getValue().getId());
        dto.setContractorId(contractorDtoComboBox.getValue().getId());
        dto.setComment(textArea.getValue());
        dto.setProjectId((long) 1); //Это не правлильно. Должен быть чекбокс с выбором проекта. Пока списка проектов нет, будет так
//        buyersReturnDto.setSum(new BigDecimal(summConfig.getValue()));
        dto.setIsSent(checkboxIsSent.getValue());
        dto.setIsPrint(checkboxIsPrint.getValue());
        dto.setAcceptanceProductIds(new ArrayList<>());
        dto.setAcceptanceProductIds(new ArrayList<>());
        dto.setIncomingNumber("1");
        acceptanceService.create(dto);
        UI.getCurrent().navigate("admissions");
        close();
        clearAllFieldsModalView();
    }

    private Button saveButton() {
        return new Button("Сохранить", e -> {
//                dto.setAcceptanceProduction(acceptanceDto.getAcceptanceProduction());
            updateSupplier();
            notifications.infoNotification(String.format("Приемка c ID=%s сохранена", dto.getId()));
        });
    }

    public void setAcceptanceForEdit(AcceptanceDto editDto) {
        this.dto = editDto;
        returnNumber.setValue(dto.getId().toString());
        dateTimePicker.setValue(LocalDateTime.parse(dto.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        textArea.setValue(dto.getComment());
        contractDtoComboBox.setValue(contractService.getById(dto.getContractId()));
        warehouseDtoComboBox.setValue(warehouseService.getById(editDto.getWarehouseId()));
        contractorDtoComboBox.setValue(contractorService.getById(editDto.getContractorId()));
    }

    private HorizontalLayout headerLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(title(), saveButton(), closeButton(), addAcceptanceButton());
        return horizontalLayout;
    }

    private VerticalLayout formLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(formLayout1(), formLayout2(), formLayout3(), formLayout4());
        return verticalLayout;
    }

    private HorizontalLayout formLayout1() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(numberConfigure(), dateConfigure(), checkboxLayout());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout2() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(contractorConfigure(), contractConfigure());
        return horizontalLayout;
    }
    private HorizontalLayout formLayout3() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(companyConfigure(), warehouseConfigure());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout4() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(commentConfig());
        return horizontalLayout;
    }

    private H2 title() {
        H2 title = new H2("Добавление приемки");
        return title;
    }



    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> {
            close();
            clearAllFieldsModalView();
        });
        return button;
    }

    private Button addAcceptanceButton() {
        Button button = new Button("Добавить из справочника", new Icon(VaadinIcon.PLUS));
        button.addClickListener(e -> {
            // Добавить приемку в таблицу
        });
        return button;
    }

    private HorizontalLayout numberConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Приемка №");
        label.setWidth("150px");
        returnNumber.setWidth("50px");
        horizontalLayout.add(label, returnNumber);
        acceptanceDtoBinder.forField(returnNumber)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(AcceptanceDto::getIdValid, AcceptanceDto::setIdValid);
        return horizontalLayout;
    }

//    private HorizontalLayout dataConfigure() {
//        HorizontalLayout horizontalLayout = new HorizontalLayout();
//        Label label = new Label("Возврат покупателя №");
//        label.setWidth("150px");
//        buyersNumber.setAutofocus(true);
//        buyersNumber.setWidth("50px");
//        buyersNumber.setRequired(true);
//        buyersNumber.setRequiredIndicatorVisible(true);
//        buyersReturnDtoBinder.forField(buyersNumber)
//                .asRequired(TEXT_FOR_REQUEST_FIELD)
//                .bind(BuyersReturnDto::getIdValid, BuyersReturnDto::setIdValid);
//        Label label2 = new Label("от");
//        dateTimePicker.setWidth("350px");
//        dateTimePicker.setRequiredIndicatorVisible(true);
//        buyersReturnDtoBinder.forField(dateTimePicker)
//                .asRequired(TEXT_FOR_REQUEST_FIELD)
//                .bind(BuyersReturnDto::getDateValid, BuyersReturnDto::setDateValid);
//        horizontalLayout.add(label, buyersNumber, label2, dateTimePicker);
//        return horizontalLayout;
//    }

    private HorizontalLayout dateConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("От");
        dateTimePicker.setWidth("350px");
        dateTimePicker.setRequiredIndicatorVisible(true);
        horizontalLayout.add(label, dateTimePicker);
        acceptanceDtoBinder.forField(dateTimePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(AcceptanceDto::getDateValid, AcceptanceDto::setDateValid);
        return horizontalLayout;
    }


//    private HorizontalLayout dateConfigure() {
//        HorizontalLayout horizontalLayout = new HorizontalLayout();
//        Label label = new Label("От");
//        dateTimePicker.setWidth("350px");
//        horizontalLayout.add(label, dateTimePicker);
//        acceptanceDtoBinder.forField(dateTimePicker)
//                .asRequired(TEXT_FOR_REQUEST_FIELD)
//                .bind(AcceptanceDto::getDateValid, AcceptanceDto::setDateValid);
//        return horizontalLayout;
//    }

    private VerticalLayout checkboxLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(checkboxIsSent, checkboxIsPrint);
        return verticalLayout;
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
        acceptanceDtoBinder.forField(warehouseDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(AcceptanceDto::getWarehouseDtoValid, AcceptanceDto::setWarehouseDtoValid);
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
        acceptanceDtoBinder.forField(contractorDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(AcceptanceDto::getContractorDtoValid, AcceptanceDto::setContractorDtoValid);
        return horizontalLayout;
    }

    private HorizontalLayout contractConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractDto> contractDtos = contractService.getAll();
        if (contractDtos != null) {
            contractDtoComboBox.setItems(contractDtos);
        }
        contractDtoComboBox.setItemLabelGenerator(ContractDto::getNumber);
        contractDtoComboBox.setWidth("350px");
        contractDtoComboBox.setRequired(true);
        contractDtoComboBox.setRequiredIndicatorVisible(true);
        acceptanceDtoBinder.forField(contractDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(AcceptanceDto::getContractDtoValid, AcceptanceDto::setContractDtoValid);
        Label label = new Label("Договор");
        label.setWidth("100px");
        horizontalLayout.add(label, contractDtoComboBox);
        return horizontalLayout;
    }
    private HorizontalLayout companyConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<CompanyDto> companyDtos = companyService.getAll();
        if (companyDtos != null) {
            companyDtoComboBox.setItems(companyDtos);
        }
        companyDtoComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyDtoComboBox.setWidth("350px");
        companyDtoComboBox.setRequired(true);
        companyDtoComboBox.setRequiredIndicatorVisible(true);
        acceptanceDtoBinder.forField(companyDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
//                .bind(AcceptanceDto::ge
//                        getCompanyDtoValid, AcceptanceDto::setCompanyDtoValid);
        Label label = new Label("Компания");
        label.setWidth("100px");
        horizontalLayout.add(label, companyDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout commentConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Комментарий");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("100px");
        horizontalLayout.add(label, textArea);
        return horizontalLayout;
    }

    private void clearAllFieldsModalView() {
        contractDtoComboBox.setValue(null);
        warehouseDtoComboBox.setValue(null);
        contractorDtoComboBox.setValue(null);
        dateTimePicker.setValue(null);
        textArea.setValue("");
        returnNumber.setValue("");
        checkboxIsPrint.setValue(false);
        checkboxIsSent.setValue(false);
    }
}
