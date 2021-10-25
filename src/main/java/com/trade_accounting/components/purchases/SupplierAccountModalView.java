package com.trade_accounting.components.purchases;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.ReturnToSupplierDto;
import com.trade_accounting.models.dto.SupplierAccountDto;
import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.SupplierAccountService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.time.LocalDateTime;
import java.util.List;

@SpringComponent
@UIScope
public class SupplierAccountModalView extends Dialog {

    private final SupplierAccountService supplierAccountService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final ContractorService contractorService;
    private final ContractService contractService;
    private SupplierAccountDto saveSupplier = new SupplierAccountDto();
    private final ComboBox<CompanyDto> companyDtoComboBox = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseDtoComboBox = new ComboBox<>();
    private final ComboBox<ContractDto> contractDtoComboBox = new ComboBox<>();
    private final ComboBox<ContractorDto> contractorDtoComboBox = new ComboBox<>();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final Checkbox isSpend = new Checkbox("Проведено");
    private final TextField supplierNumber = new TextField();
    private final TextField commentConfig = new TextField();
    private final Notifications notifications;
    private final Binder<SupplierAccountDto> supplierAccountDtoBinder = new Binder<>(SupplierAccountDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";
    private final Dialog dialogOnCloseView = new Dialog();

    public SupplierAccountModalView(SupplierAccountService supplierAccountService,
                                    CompanyService companyService,
                                    WarehouseService warehouseService,
                                    ContractorService contractorService, ContractService contractService, Notifications notifications) {
        this.supplierAccountService = supplierAccountService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.contractorService = contractorService;
        this.contractService = contractService;
        this.notifications = notifications;
        configureCloseViewDialog();
        setSizeFull();
        configureDateTimePickerField();
        add(upperButtonInModalView(), formToAddSupplerAccount());
    }


    public void setSupplierAccountsForEdit(SupplierAccountDto editSupplierAccounts) {
        this.saveSupplier = editSupplierAccounts;
        supplierNumber.setValue(saveSupplier.getId().toString());
        dateTimePicker.setValue(LocalDateTime.parse(saveSupplier.getDate()));
        commentConfig.setValue(saveSupplier.getComment());
        companyDtoComboBox.setValue(companyService.getById(saveSupplier.getCompanyId()));
        warehouseDtoComboBox.setValue(warehouseService.getById(saveSupplier.getWarehouseId()));
        contractDtoComboBox.setValue(contractService.getById(saveSupplier.getContractId()));
        contractorDtoComboBox.setValue(contractorService.getById(saveSupplier.getContractorId()));
    }

    private HorizontalLayout upperButtonInModalView() {
        HorizontalLayout upperButton = new HorizontalLayout();
        upperButton.add(title(), saveButton(), closeButton(), addProductButton());
        upperButton.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upperButton;
    }

    private H2 title() {
        final H2 label = new H2("Добавление счета");
        label.setHeight("2.2em");
        return label;
    }

    private Button saveButton() {
        return new Button("Сохранить", e -> {
            if (!supplierAccountDtoBinder.validate().isOk()) {
                supplierAccountDtoBinder.validate().notifyBindingValidationStatusHandlers();
            } else if (supplierAccountService.getById(Long.parseLong(supplierNumber.getValue())) != null) {
                e.getSource().setText("Обновить");
                dialogOnCloseView.open();
            } else {
                e.getSource().setText("Сохранить");
                updateSupplier();
                notifications.infoNotification(String.format("Счет поставщика № %s сохранен", saveSupplier.getId()));
            }
        });
    }

    private void updateSupplier() {
        saveSupplier.setId(Long.parseLong(supplierNumber.getValue()));
        saveSupplier.setDate(dateTimePicker.getValue().toString());
        saveSupplier.setCompanyId(companyDtoComboBox.getValue().getId());
        saveSupplier.setWarehouseId(warehouseDtoComboBox.getValue().getId());
        saveSupplier.setContractId(contractDtoComboBox.getValue().getId());
        saveSupplier.setContractorId(contractorDtoComboBox.getValue().getId());
        saveSupplier.setIsSpend(isSpend.getValue());
        saveSupplier.setComment(commentConfig.getValue());
        supplierAccountService.update(saveSupplier);
        UI.getCurrent().navigate("suppliersInvoices");
        close();
    }

    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> {
            close();
        });
        return button;
    }

    private Button addProductButton() {
        return new Button("Добавить из справочника", new Icon(VaadinIcon.PLUS_CIRCLE), e -> {
        });
    }

    private VerticalLayout formToAddSupplerAccount() {
        VerticalLayout form = new VerticalLayout();
        form.add(horizontalLayout1(), horizontalLayout2(),
                horizontalLayout3(), dataPlaneConfigure(), incomingConfigure(), commentConfigure());
        return form;
    }

    private HorizontalLayout horizontalLayout1() {
        HorizontalLayout hLay1 = new HorizontalLayout();
        hLay1.add(dataConfigure(), isSpend);
        return hLay1;
    }

    private HorizontalLayout horizontalLayout2() {
        HorizontalLayout hLay2 = new HorizontalLayout();
        hLay2.add(companyConfigure(), warehouseConfigure());
        return hLay2;
    }

    private HorizontalLayout horizontalLayout3() {
        HorizontalLayout hLay3 = new HorizontalLayout();
        hLay3.add(contractorConfigure(), contractConfigure());
        return hLay3;
    }

    private HorizontalLayout dataConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Счет поставщика №");
        label.setWidth("150px");

        supplierNumber.setAutofocus(true);
        supplierNumber.setWidth("50px");
        supplierNumber.setRequired(true);
        supplierNumber.setRequiredIndicatorVisible(true);
        supplierAccountDtoBinder.forField(supplierNumber)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(SupplierAccountDto::getIdValid, SupplierAccountDto::setIdValid);
        Label label2 = new Label("от");
        dateTimePicker.setWidth("350px");
        dateTimePicker.setRequiredIndicatorVisible(true);
        supplierAccountDtoBinder.forField(dateTimePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(SupplierAccountDto::getDateValid, SupplierAccountDto::setDateValid);
        horizontalLayout.add(label, supplierNumber, label2, dateTimePicker);
        return horizontalLayout;
    }

    private HorizontalLayout contractorConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractorDto> contractorDtos = contractorService.getAll();
        if (contractorDtos != null) {
            contractorDtoComboBox.setItems(contractorDtos);
        }
        contractorDtoComboBox.setItemLabelGenerator(ContractorDto::getName);
        contractorDtoComboBox.setWidth("350px");
        contractorDtoComboBox.setRequired(true);
        contractorDtoComboBox.setRequiredIndicatorVisible(true);
        supplierAccountDtoBinder.forField(contractorDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(SupplierAccountDto::getContractorDtoValid, SupplierAccountDto::setContractorDtoValid);
        Label label = new Label("Контрагент");
        label.setWidth("100px");
        horizontalLayout.add(label, contractorDtoComboBox);
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
        supplierAccountDtoBinder.forField(companyDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(SupplierAccountDto::getCompanyDtoValid, SupplierAccountDto::setCompanyDtoValid);
        Label label = new Label("Компания");
        label.setWidth("100px");
        horizontalLayout.add(label, companyDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout warehouseConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<WarehouseDto> warehouseDtos = warehouseService.getAll();
        if (warehouseDtos != null) {
            warehouseDtoComboBox.setItems(warehouseDtos);
        }
        warehouseDtoComboBox.setItemLabelGenerator(WarehouseDto::getName);
        warehouseDtoComboBox.setWidth("350px");
        warehouseDtoComboBox.setRequired(true);
        warehouseDtoComboBox.setRequiredIndicatorVisible(true);
        supplierAccountDtoBinder.forField(warehouseDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(SupplierAccountDto::getWarehouseDtoValid, SupplierAccountDto::setWarehouseDtoValid);
        Label label = new Label("Склад");
        label.setWidth("100px");
        horizontalLayout.add(label, warehouseDtoComboBox);
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
        supplierAccountDtoBinder.forField(contractDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(SupplierAccountDto::getContractDtoValid, SupplierAccountDto::setContractDtoValid);
        Label label = new Label("Договор");
        label.setWidth("100px");
        horizontalLayout.add(label, contractDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout dataPlaneConfigure() {
        HorizontalLayout horizontal = new HorizontalLayout();
        DatePicker dt = new DatePicker();
        Label label = new Label("План. дата оплаты");
        label.setWidth("150px");
        dt.setWidth("150px");
        horizontal.add(label, dt);
        return horizontal;
    }

    private HorizontalLayout incomingConfigure() {
        HorizontalLayout horizontal1 = new HorizontalLayout();
        DatePicker dt1 = new DatePicker();
        Label label = new Label("Входящий номер");
        label.setWidth("150px");
        TextField text = new TextField();
        text.setWidth("70px");
        Label label2 = new Label("от");
        dt1.setWidth("150px");
        horizontal1.add(label, text, label2, dt1);
        return horizontal1;
    }

    private HorizontalLayout commentConfigure() {
        HorizontalLayout horizontal3 = new HorizontalLayout();
        commentConfig.setWidth("500px");
        commentConfig.setHeight("300px");
        commentConfig.setPlaceholder("Комментарий");
        horizontal3.add(commentConfig);
        return horizontal3;
    }

    private void configureCloseViewDialog() {
        dialogOnCloseView.add(new Text(String.format("Документ с таким номером уже существует и будет изменен! Вы уверены?", supplierNumber.getValue())));
        dialogOnCloseView.setCloseOnEsc(false);
        dialogOnCloseView.setCloseOnOutsideClick(false);
        Button confirmButton = new Button("Продолжить", event -> {
            updateSupplier();
            notifications.infoNotification(String.format("Документ №%s изменен", saveSupplier.getId()));
            close();
            dialogOnCloseView.close();
        });
        Button cancelButton = new Button("Отменить", event -> {
            dialogOnCloseView.close();
        });
        Shortcuts.addShortcutListener(dialogOnCloseView, () -> {
            dialogOnCloseView.close();
        }, Key.ESCAPE);
        cancelButton.setAutofocus(true);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        HorizontalLayout buttonLayout = new HorizontalLayout(confirmButton, cancelButton);
        buttonLayout.getStyle().set("flex-wrap", "wrap");
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        dialogOnCloseView.add(buttonLayout);
    }

    private void configureDateTimePickerField() {
        dateTimePicker.setValue(LocalDateTime.now());
    }
}
