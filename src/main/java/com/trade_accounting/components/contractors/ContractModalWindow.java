package com.trade_accounting.components.contractors;

import com.trade_accounting.components.util.ValidTextField;
import com.trade_accounting.models.dto.BankAccountDto;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.LegalDetailDto;
import com.trade_accounting.services.interfaces.ContractService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.RegexpValidator;

public class ContractModalWindow extends Dialog {

    private Long contractId;
    private TextArea dateField = new TextArea();
    private TextArea amountField = new TextArea();
    private TextArea archiveField = new TextArea();
    private TextArea commentField = new TextArea();
    private ValidTextField numberField = new ValidTextField();

    private Long companyId;
    private final TextField companyName = new TextField();
    private final TextField companyInn = new TextField();
    private final TextArea companyAddress = new TextArea();
    private final TextArea companyCommentToAddress = new TextArea();
    private final TextField companyEmail = new TextField();
    private final TextField companyPhone = new TextField();
    private final TextField companyFax = new TextField();
    private final TextField companyLeader = new TextField();
    private final TextField companyLeaderManagerPosition = new TextField();
    private final TextField companyLeaderSignature = new TextField();
    private final TextField companyChiefAccountant = new TextField();
    private final TextField companyChiefAccountantSignature = new TextField();
    private final Select<String> companyPayerVat = new Select<>();
    private final TextField companySortNumber = new TextField();
    private final TextField companyStamp = new TextField();

    private Long legalDetailId;
    private final TextField legalDetailLastName = new TextField();
    private final TextField legalDetailFirstName = new TextField();
    private final TextField legalDetailMiddleName = new TextField();
    private final TextArea legalDetailAddress = new TextArea();
    private final TextArea legalDetailCommentToAddress = new TextArea();
    private final TextField legalDetailInn = new TextField();
    private final TextField legalDetailOkpo = new TextField();
    private final TextField legalDetailOgrnip = new TextField();
    private final TextField legalDetailNumberOfTheCertificate = new TextField();
    private final DatePicker legalDetailDateOfTheCertificate = new DatePicker();

    private Long typeOfContractorId;
    private final TextField typeOfContractorName = new TextField();
    private final TextField typeOfContractorSortNumber = new TextField();

    private Long bankAccountId;
    private final TextField bankAccountRcbic = new TextField();
    private final TextField bankAccountBank = new TextField();
    private final TextField bankAccountAddress = new TextField();
    private final TextField bankAccountCorrespondentAccount = new TextField();
    private final TextField bankAccountAccount = new TextField();
    private final TextField bankAccountMainAccount = new TextField();
    private final TextField bankAccountSortNumber = new TextField();

    private Long contractorId;
    private TextField contractorName = new TextField();
    private TextField contractorInn = new TextField();
    private TextField contractorSortNumber = new TextField();
    private TextField contractorPhone = new TextField();
    private TextField contractorFax = new TextField();
    private TextField contractorEmail = new TextField();
    private TextField contractorAddress = new TextField();
    private TextField contractorCommentToAddress = new TextField();
    private TextField contractorComment = new TextField();

    private Long contractorGroupId;
    private TextField contractorGroupName = new TextField();
    private TextField contractorGroupSortNumber = new TextField();

    private Long typeOfPriceId;
    private TextField typeOfPriceName = new TextField();
    private TextField typeOfPriceSortNumber = new TextField();


    private final String labelWidth = "100px";

    private final String fieldWidth = "400px";

    private final ContractService contractService;

    public ContractModalWindow(ContractDto contractDto,
                               ContractService contractService) {
        this.contractService = contractService;

        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);

    }


    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        // fullNameField.setWidth("345px");
        header.add(getSaveButton(), getCancelButton(), getDeleteButton());
        return header;
    }


    private HorizontalLayout configureNumberField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Номер");
        label.setWidth(labelWidth);
        numberField.setWidth(fieldWidth);
        horizontalLayout.add(label, numberField);
        numberField.addInputListener(inputEvent ->
                numberField.addValidator(new RegexpValidator("Максимум 5 цифр",
                        "^([0-9]{0,5})$")));
        return horizontalLayout;
    }

    private HorizontalLayout configureCompanyDtoField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Компания");
        label.setWidth(labelWidth);
      //  horizontalLayout.add(label, companyDtoField);
        return horizontalLayout;
    }

    private HorizontalLayout configureDateField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Дата");
        label.setWidth(labelWidth);
        horizontalLayout.add(label, dateField);
        return horizontalLayout;
    }

    private HorizontalLayout configureBankAccountDtoField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Банковский Аккаунт");
        label.setWidth(labelWidth);
       // horizontalLayout.add(label, bankAccountDtoField);
        return horizontalLayout;
    }

    private HorizontalLayout configureContractorDtoField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Контрагент");
        label.setWidth(labelWidth);
      //  horizontalLayout.add(label, contractorDtoField);
        return horizontalLayout;
    }

    private HorizontalLayout configureAmountField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Сумма");
        label.setWidth(labelWidth);
        horizontalLayout.add(label, amountField);
        return horizontalLayout;
    }

    private HorizontalLayout configureArchiveField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Архив");
        label.setWidth(labelWidth);
        horizontalLayout.add(label, archiveField);
        return horizontalLayout;
    }

    private HorizontalLayout configureCommentField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Комментарий");
        label.setWidth(labelWidth);
        horizontalLayout.add(label, commentField);
        return horizontalLayout;
    }


    private Button getSaveButton() {
        return new Button("Сохранить", event -> {/*
            ContractDto contractDto = new ContractDto();
            contractDto.setId(id);
            contractDto.setContractDate(contractDateField.getValue());
            contractDto.setContractorDto();
            //contractDto.setContractorDto(new ContractorDto());
            contractDto.setDate(dateField.getValue());
           // contractDto.setCompanyDto(companyDtoField.getValue());
            contractDto.setCompanyDto(new CompanyDto());
           // contractDto.setBankAccountDto(bankAccountDtoField.getValue());
            contractDto.setBankAccountDto(new BankAccountDto());
           // contractDto.setArchive(archiveField.getValue());
            contractDto.setArchive(false);
            contractDto.setComment(commentField.getValue());
            if (!numberField.isEmpty() && numberField.getValue()
                    .matches("^([0-9]{0,5})$")) {
                contractService.update(contractDto);
                close();
            }
        */
        });
    }

    private Button getCancelButton() {
        Button cancelButton = new Button("Закрыть", event -> {
            close();
        });
        return cancelButton;
    }

    private Button getDeleteButton() {
        Button deleteButton = new Button("Удалить", event -> {
            contractService.deleteById(contractId);
            close();
        });
        return deleteButton;
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }

}
