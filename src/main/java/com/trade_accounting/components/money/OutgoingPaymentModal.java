package com.trade_accounting.components.money;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.ContractDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.finance.PaymentDto;
import com.trade_accounting.models.dto.util.ProjectDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.finance.PaymentService;
import com.trade_accounting.services.interfaces.util.ProjectService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringComponent
@UIScope
@Slf4j
public class OutgoingPaymentModal extends Dialog {
    private final transient CompanyService companyService;
    private final transient ContractService contractService;
    private final transient ContractorService contractorService;
    private final transient ProjectService projectService;
    private final transient PaymentService paymentService;
    private final transient Notifications notifications;

    private final DateTimePicker dateField = new DateTimePicker();
    private final ComboBox<String> paymentMethods = new ComboBox<>();
    private final ComboBox<CompanyDto> companyDtoComboBox = new ComboBox<>();
    private final ComboBox<ContractorDto> contractorDtoComboBox = new ComboBox<>();
    private final ComboBox<ContractDto> contractDtoComboBox = new ComboBox<>();
    private final ComboBox<ProjectDto> projectDtoComboBox = new ComboBox<>();
    private final TextField payNumber = new TextField();
    private final ComboBox<String> expenseItem = new ComboBox<>();
    private final BigDecimalField sum = new BigDecimalField();
    private final Checkbox сonducted = new Checkbox();
    private transient PaymentDto paymentDto;

    public OutgoingPaymentModal(
            PaymentService paymentService,
            CompanyService companyService,
            ContractorService contractorService,
            ProjectService projectService,
            ContractService contractService,
            Notifications notifications
    ) {
        this.paymentService = paymentService;
        this.notifications = notifications;
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.contractService = contractService;
        this.projectService = projectService;
        paymentMethods.setItems("Наличные", "Безнал");
        expenseItem.setItems("RETURN",
                "PURCHACE",
                "TAXESANDFEES",
                "MOVEMENT",
                "RENTAL",
                "SALARY",
                "MARKETING");
        companyDtoComboBox.setItems(companyService.getAll());
        companyDtoComboBox.setItemLabelGenerator(CompanyDto::getName);
        contractDtoComboBox.setItems(contractService.getAll());
        contractDtoComboBox.setItemLabelGenerator(ContractDto::getNumber);
        contractorDtoComboBox.setItems(contractorService.getAll());
        contractorDtoComboBox.setItemLabelGenerator(ContractorDto::getName);
        projectDtoComboBox.setItems(projectService.getAll());
        projectDtoComboBox.setItemLabelGenerator(ProjectDto::getName);
        payNumber.setPattern("\\d*");
        payNumber.setErrorMessage("только арабские цифры");
        sum.setPlaceholder("введите число меньше 10^17");

        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);
        add(getHeader());
        add(getHorizontalLayout("Дата", dateField));
        add(getHorizontalLayout("Компания", companyDtoComboBox));
        add(getHorizontalLayout("Номер платежа", payNumber));
        add(getHorizontalLayout("Способ оплаты", paymentMethods));
        add(getHorizontalLayout("Статья расходов", expenseItem));
        add(getHorizontalLayout("Контрагент", contractorDtoComboBox));
        add(getHorizontalLayout("Договор", contractDtoComboBox));
        add(getHorizontalLayout("Проект", projectDtoComboBox));
        add(getHorizontalLayout("Сумма", sum));
        add(getHorizontalLayout("Проведен", сonducted));
        add(getFooter());
    }

    private Component getHeader() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        H2 title = new H2("Исходящий платеж");
        title.setHeight("1.5em");
        title.setWidth("345px");
        horizontalLayout.add(title);
        horizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        return horizontalLayout;
    }

    private <T extends Component & HasSize> HorizontalLayout getHorizontalLayout(String labelText, T field) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label(labelText);
        field.setWidth("400px");
        label.setWidth("200px");
        horizontalLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, label);
        horizontalLayout.add(label, field);
        return horizontalLayout;
    }

    private PaymentDto updatePaymentDto() {
        PaymentDto payment = new PaymentDto();
        payment.setTime(dateField.getValue().toString());
        payment.setCompanyId(companyDtoComboBox.getValue().getId());
        payment.setContractorId(contractorDtoComboBox.getValue().getId());
        payment.setContractId(contractDtoComboBox.getValue().getId());
        payment.setProjectId(projectDtoComboBox.getValue().getId());
        payment.setNumber(payNumber.getValue());
        payment.setExpenseItem(expenseItem.getValue());
        payment.setSum(sum.getValue());
        payment.setTypeOfPayment("OUTGOING");
        payment.setTypeOfDocument("Исходящий платеж");
        payment.setConducted(сonducted.getValue());
        if (this.paymentDto != null && this.paymentDto.getId() != null) {
            payment.setId(this.paymentDto.getId());
        }
        if (paymentMethods.getValue().equals("Наличные")) {
            payment.setPaymentMethods("CASH");
        } else {
            payment.setPaymentMethods("BANK");
        }
        return payment;
    }

    private void reset() {
        dateField.clear();
        companyDtoComboBox.clear();
        contractorDtoComboBox.clear();
        contractDtoComboBox.clear();
        projectDtoComboBox.clear();
        expenseItem.clear();
        payNumber.clear();
        paymentMethods.clear();
        sum.clear();
    }

    private Button getSaveButton() {
        Button button = new Button("Сохранить", new Icon(VaadinIcon.PLUS_CIRCLE));
        button.addClickListener(event -> {
            if (sum.getValue().compareTo(BigDecimal.ZERO) > 0) {
                PaymentDto payment = updatePaymentDto();
                if (payment.getId() == null) {
                    paymentService.create(payment);
                } else {
                    paymentService.update(payment);
                }
                reset();
                UI.getCurrent().navigate("money");
                close();
            } else {
                sum.clear();
                notifications.errorNotification("Ошибка сохранения. Число в поле \"Сумма\" превышает допустимый диапазон");
            }
        });
        return button;
    }

    private Button getCancelButton() {
        Button button = new Button("Отмена", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(event -> {
            reset();
            close();
        });
        return button;
    }

    private Component getFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        VerticalLayout saveVerticalLayout = new VerticalLayout();
        VerticalLayout cancelVerticalLayout = new VerticalLayout();
        saveVerticalLayout.add(getSaveButton());
        saveVerticalLayout.setWidth("550px");
        saveVerticalLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.END);
        cancelVerticalLayout.add(getCancelButton());
        cancelVerticalLayout.setWidth("100px");
        cancelVerticalLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.END);
        footer.add(saveVerticalLayout, cancelVerticalLayout);
        footer.setHeight("100px");
        footer.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        return footer;
    }

    public void setPaymentDataForEdit(PaymentDto editPaymentDto) {
        this.paymentDto = editPaymentDto;
        companyDtoComboBox.setValue(companyService.getById(paymentDto.getCompanyId()));
        paymentMethods.setValue(paymentDto.getPaymentMethods());
        contractDtoComboBox.setValue(contractService.getById(paymentDto.getContractId()));
        contractorDtoComboBox.setValue(contractorService.getById(paymentDto.getContractorId()));
        projectDtoComboBox.setValue(projectService.getById(paymentDto.getProjectId()));
        payNumber.setValue(paymentDto.getNumber());
        sum.setValue(paymentDto.getSum());
        expenseItem.setValue(paymentDto.getExpenseItem());
        dateField.setValue(LocalDateTime.now());
        сonducted.setValue(paymentDto.getConducted());
    }
}
