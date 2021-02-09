package com.trade_accounting.components.contractors;

import com.trade_accounting.components.util.ValidTextField;
import com.trade_accounting.models.dto.BankAccountDto;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.LegalDetailDto;
import com.trade_accounting.services.interfaces.ContractService;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.RegexpValidator;

import java.time.LocalDate;

public class ContractModalWindow extends Dialog {

    private static final String FIELD_WIDTH = "400px";

    private Long contractId;
    private TextField dateField = new TextField();
    private TextField amountField = new TextField();
    private TextField archiveField = new TextField();
    private TextField commentField = new TextField();
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
    private final Select<String> bankAccountMainAccount = new Select<>();
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

        configureModal("Договор");
        setFields(contractDto);

    }

    private void configureModal(String title) {
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        add(header(title), accordionCompany());
    }

    private void setFields(ContractDto dto) {

        /*contractId = dto.getId();
        setDate(dateField, dto.getDate().toString());
        setField(amountField, dto.getAmount().toString());
        setField(archiveField, Boolean.TRUE.equals(dto.getArchive()) ? "Да" : "Нет");
        setField(commentField, dto.getComment());
        setField(numberField, dto.getNumber());

        companyId = dto.getCompanyDto().getId();
        setField(companyName, dto.getCompanyDto().getName());
        setField(companyInn, dto.getCompanyDto().getInn());
        setField(companyAddress, dto.getCompanyDto().getAddress());
        setField(companyCommentToAddress, dto.getCompanyDto().getCommentToAddress());
        setField(companyEmail, dto.getCompanyDto().getEmail());
        setField(companyPhone, dto.getCompanyDto().getPhone());
        setField(companyFax, dto.getCompanyDto().getFax());
        setField(companyLeader, dto.getCompanyDto().getLeader());
        setField(companyLeaderManagerPosition, dto.getCompanyDto().getLeaderManagerPosition());
        setField(companyLeaderSignature, dto.getCompanyDto().getLeaderSignature());
        setField(companyChiefAccountant, dto.getCompanyDto().getChiefAccountant());
        setField(companyChiefAccountantSignature, dto.getCompanyDto().getChiefAccountantSignature());
        setField(companyPayerVat, Boolean.TRUE.equals(dto.getCompanyDto().getPayerVat()) ? "Да" : "Нет");
        setField(companySortNumber, dto.getCompanyDto().getSortNumber());
        setField(companyStamp, dto.getCompanyDto().getStamp());*/

        if (dto.getLegalDetailDto() != null) {
            legalDetailId = dto.getLegalDetailDto().getId();
            setField(legalDetailLastName, dto.getLegalDetailDto().getLastName());
            setField(legalDetailFirstName, dto.getLegalDetailDto().getFirstName());
            setField(legalDetailMiddleName, dto.getLegalDetailDto().getMiddleName());
            setField(legalDetailAddress, dto.getLegalDetailDto().getAddress());
            setField(legalDetailCommentToAddress, dto.getLegalDetailDto().getCommentToAddress());
            setField(legalDetailInn, dto.getLegalDetailDto().getInn());
            setField(legalDetailOkpo, dto.getLegalDetailDto().getOkpo());
            setField(legalDetailOgrnip, dto.getLegalDetailDto().getOgrnip());
            setField(legalDetailNumberOfTheCertificate, dto.getLegalDetailDto().getNumberOfTheCertificate());
            setDate(legalDetailDateOfTheCertificate, dto.getLegalDetailDto().getDateOfTheCertificate());

            if (dto.getLegalDetailDto().getTypeOfContractorDto() != null) {
                typeOfContractorId = dto.getLegalDetailDto().getTypeOfContractorDto().getId();
                setField(typeOfContractorName, dto.getLegalDetailDto().getTypeOfContractorDto().getName());
                setField(typeOfContractorSortNumber, dto.getLegalDetailDto().getTypeOfContractorDto().getSortNumber());
            }
        }

        /*bankAccountId = dto.getBankAccountDto().getId();
        setField(bankAccountRcbic, dto.getBankAccountDto().getRcbic());
        setField(bankAccountBank, dto.getBankAccountDto().getBank());
        setField(bankAccountAddress, dto.getBankAccountDto().getAddress());
        setField(bankAccountCorrespondentAccount, dto.getBankAccountDto().getCorrespondentAccount());
        setField(bankAccountAccount, dto.getBankAccountDto().getAccount());
        setField(bankAccountMainAccount, Boolean.TRUE.equals(dto.getBankAccountDto().getMainAccount()) ? "Да" : "Нет");
        setField(bankAccountSortNumber, dto.getBankAccountDto().getSortNumber());

        contractorId = dto.getContractorDto().getId();
        setField(contractorName, dto.getContractorDto().getName());
        setField(contractorInn, dto.getContractorDto().getInn());
        setField(contractorSortNumber, dto.getContractorDto().getSortNumber());
        setField(contractorPhone, dto.getContractorDto().getPhone());
        setField(contractorFax, dto.getContractorDto().getFax());
        setField(contractorEmail, dto.getContractorDto().getEmail());
        setField(contractorAddress, dto.getContractorDto().getAddress());
        setField(contractorCommentToAddress, dto.getContractorDto().getCommentToAddress());
        setField(contractorComment, dto.getContractorDto().getComment());

        contractorGroupId = dto.getContractorDto().getContractorGroupDto().getId();
        setField(contractorGroupName, dto.getContractorDto().getContractorGroupDto().getName());
        setField(contractorGroupSortNumber, dto.getContractorDto().getContractorGroupDto().getSortNumber());

        typeOfPriceId = dto.getContractorDto().getTypeOfPriceDto().getId();
        setField(typeOfPriceName, dto.getContractorDto().getTypeOfPriceDto().getName());
        setField(typeOfPriceSortNumber, dto.getContractorDto().getTypeOfPriceDto().getSortNumber());*/

    }


    private void setField(AbstractField field, String value) {
        if (value != null) {
            field.setValue(value);
        }
    }

    private void setDate(AbstractField field, String date) {
        if (date != null) {
            field.setValue(LocalDate.parse(date));
        }
    }

    private HorizontalLayout header(String titleText) {
        HorizontalLayout header = new HorizontalLayout();
        H2 title = new H2(titleText);
        title.setHeight("2.2em");
        title.setWidth("345px");
        header.add(title);
        header.add(buttonSave(), buttonCancel());
        return header;
    }

    private Accordion accordionCompany() {
        Accordion accordion = new Accordion();
        accordion.setWidth("575px");

        VerticalLayout layoutContract = new VerticalLayout();
        layoutContract.add(
                configureDateField(),
                configureAmountField(),
                configureArchiveField(),
                configureCommentField(),
                configureNumberField()
        );
        accordion.add("Договор", layoutContract).addThemeVariants(DetailsVariant.FILLED);

        VerticalLayout layoutInfo = new VerticalLayout();
        layoutInfo.add(
                configureName(),
                configureInn(),
                configureAddress(),
                configureCommentToAddress(),
                configureEmail(),
                configurePhone(),
                configureFax(),
                configureSortNumber(),
                configurePayerVat(),
                configureStamp());
        accordion.add("О юр. лице", layoutInfo).addThemeVariants(DetailsVariant.FILLED);

        VerticalLayout layoutPersons = new VerticalLayout();
        layoutPersons.add(
                configureLeader(),
                configureLeaderManagerPosition(),
                configureLeaderSignature(),
                configureChiefAccountant(),
                configureChiefAccountantSignature());
        accordion.add("Главные лица", layoutPersons).addThemeVariants(DetailsVariant.FILLED);

        VerticalLayout layoutDetails = new VerticalLayout();
        layoutDetails.add(
                configureLegalDetailLastName(),
                configureLegalDetailFirstName(),
                configureLegalDetailMiddleName(),
                configureLegalDetailAddress(),
                configureLegalDetailCommentToAddress(),
                configureLegalDetailInn(),
                configureLegalDetailOkpo(),
                configureLegalDetailOgrnip(),
                configureLegalDetailNumberOfTheCertificate(),
                configureLegalDetailDateOfTheCertificate(),
                configureTypeOfContractorName(),
                configureTypeOfContractorSortNumber());
        accordion.add("Юридические детали", layoutDetails).addThemeVariants(DetailsVariant.FILLED);

        VerticalLayout layoutBankAccount = new VerticalLayout();
        layoutBankAccount.add(
                configureBankAccountRcbic(),
                configureBankAccountBank(),
                configureBankAccountAddress(),
                configureBankAccountCorrespondentAccount(),
                configureBankAccountAccount(),
                configureBankAccountMainAccount(),
                configureBankAccountSortNumber()
        );
        accordion.add("Банковские реквизиты", layoutBankAccount).addThemeVariants(DetailsVariant.FILLED);

        return accordion;
    }

    private HorizontalLayout configureBankAccountRcbic() {
        bankAccountRcbic.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("БИК", bankAccountRcbic);
    }

    private HorizontalLayout configureBankAccountBank() {
        bankAccountBank.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Банк", bankAccountBank);
    }

    private HorizontalLayout configureBankAccountAddress() {
        bankAccountAddress.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Адрес", bankAccountAddress);
    }

    private HorizontalLayout configureBankAccountCorrespondentAccount() {
        bankAccountCorrespondentAccount.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Корр.счет", bankAccountCorrespondentAccount);
    }

    private HorizontalLayout configureBankAccountAccount() {
        bankAccountAccount.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Счет", bankAccountAccount);
    }

    private HorizontalLayout configureBankAccountMainAccount() {
        bankAccountMainAccount.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Осн.Счет", bankAccountMainAccount);
    }

    private HorizontalLayout configureBankAccountSortNumber() {
        bankAccountSortNumber.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Сортировочный номер", bankAccountSortNumber);
    }

    private HorizontalLayout configureNumberField() {
        numberField.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Сортировочный номер", numberField);
    }

    private HorizontalLayout configureCommentField() {
        commentField.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Комментарий", commentField);
    }

    private HorizontalLayout configureArchiveField() {
        archiveField.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Архив", archiveField);
    }

    private HorizontalLayout configureAmountField() {
        amountField.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Сумма", amountField);
    }

    private HorizontalLayout configureDateField() {
        dateField.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Дата заключения", dateField);
    }

    private HorizontalLayout configureName() {
        companyName.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Наименование", companyName);
    }

    private HorizontalLayout configureInn() {
        companyInn.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("ИНН", companyInn);
    }

    private HorizontalLayout configureAddress() {
        companyAddress.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Адрес", companyAddress);
    }

    private HorizontalLayout configureCommentToAddress() {
        companyCommentToAddress.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Комментарий к адресу", companyCommentToAddress);
    }

    private HorizontalLayout configureEmail() {
        companyEmail.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("E-mail", companyEmail);
    }

    private HorizontalLayout configurePhone() {
        companyPhone.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Телефон", companyPhone);
    }

    private HorizontalLayout configureFax() {
        companyFax.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Факс", companyFax);
    }

    private HorizontalLayout configureSortNumber() {
        companySortNumber.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Нумерация", companySortNumber);
    }

    private HorizontalLayout configurePayerVat() {
        companyPayerVat.setWidth(FIELD_WIDTH);
        companyPayerVat.setItems("Да", "Нет");
        return getHorizontalLayout("Плательщик НДС", companyPayerVat);
    }

    private HorizontalLayout configureStamp() {
        companyStamp.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Печать", companyStamp);
    }

    private HorizontalLayout configureLeader() {
        companyLeader.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Руководитель", companyLeader);
    }

    private HorizontalLayout configureLeaderManagerPosition() {
        companyLeaderManagerPosition.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Должность руководителя", companyLeaderManagerPosition);
    }

    private HorizontalLayout configureLeaderSignature() {
        companyLeaderSignature.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Подпись руководителя", companyLeaderSignature);
    }

    private HorizontalLayout configureChiefAccountant() {
        companyChiefAccountant.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Главный бухгалтер", companyChiefAccountant);
    }

    private HorizontalLayout configureChiefAccountantSignature() {
        companyChiefAccountantSignature.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Подпись гл. бухгалтера", companyChiefAccountantSignature);
    }

    private HorizontalLayout configureLegalDetailLastName() {
        legalDetailLastName.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Фамилия", legalDetailLastName);
    }

    private HorizontalLayout configureLegalDetailFirstName() {
        legalDetailFirstName.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Имя", legalDetailFirstName);
    }

    private HorizontalLayout configureLegalDetailMiddleName() {
        legalDetailMiddleName.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Отчество", legalDetailMiddleName);
    }

    private HorizontalLayout configureLegalDetailAddress() {
        legalDetailAddress.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Адрес", legalDetailAddress);
    }

    private HorizontalLayout configureLegalDetailCommentToAddress() {
        legalDetailCommentToAddress.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Комментарий к адресу", legalDetailCommentToAddress);
    }

    private HorizontalLayout configureLegalDetailInn() {
        legalDetailInn.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("ИНН", legalDetailInn);
    }

    private HorizontalLayout configureLegalDetailOkpo() {
        legalDetailOkpo.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("ОКПО", legalDetailOkpo);
    }

    private HorizontalLayout configureLegalDetailOgrnip() {
        legalDetailOgrnip.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("ОГРНИП", legalDetailOgrnip);
    }

    private HorizontalLayout configureLegalDetailNumberOfTheCertificate() {
        legalDetailNumberOfTheCertificate.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Номер сертификата", legalDetailNumberOfTheCertificate);
    }

    private HorizontalLayout configureLegalDetailDateOfTheCertificate() {
        legalDetailDateOfTheCertificate.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Дата сертификата", legalDetailDateOfTheCertificate);
    }

    private HorizontalLayout configureTypeOfContractorName() {
        typeOfContractorName.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Имя поставщика", typeOfContractorName);
    }

    private HorizontalLayout configureTypeOfContractorSortNumber() {
        typeOfContractorSortNumber.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Номер поставщика", typeOfContractorSortNumber);
    }

    private HorizontalLayout getHorizontalLayout(String title, Component field) {
        Label label = new Label(title);
        label.setWidth("100px");
        return new HorizontalLayout(label, field);
    }

    /*private HorizontalLayout configureNumberField() {
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
    }*/


    private Button buttonSave() {
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

    private Button buttonCancel() {
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
