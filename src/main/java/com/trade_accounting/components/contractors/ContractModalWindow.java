package com.trade_accounting.components.contractors;

import com.trade_accounting.components.util.ValidTextField;
import com.trade_accounting.models.dto.BankAccountDto;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.LegalDetailDto;
import com.trade_accounting.models.dto.TypeOfContractorDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ContractModalWindow extends Dialog {

    private static final String FIELD_WIDTH = "400px";

    private Long contractId;
    private DatePicker dateField = new DatePicker();
    private TextField amountField = new TextField();
    private Checkbox archiveField = new Checkbox();
    private TextField commentField = new TextField();
    private ValidTextField numberField = new ValidTextField();

    private Select<ContractorDto> selectContractor = new Select<>();
    private Select<CompanyDto> selectCompany = new Select<>();
    private Select<BankAccountDto> selectBankAccount = new Select<>();
    private Select<LegalDetailDto> selectLegalDetail = new Select<>();


    private final String labelWidth = "100px";

    private final String fieldWidth = "400px";

    private final ContractService contractService;
    private final ContractorService contractorService;
    private final CompanyService companyService;

    public ContractModalWindow(ContractService contractService, ContractorService contractorService,
                               CompanyService companyService) {
        this.contractService = contractService;
        this.contractorService = contractorService;
        this.companyService = companyService;

        configureModal("Добавление");
    }


    public ContractModalWindow(ContractDto contractDto,
                               ContractService contractService, ContractorService contractorService,
                               CompanyService companyService) {
        this.contractService = contractService;
        this.contractorService = contractorService;
        this.companyService = companyService;

        configureModal("Редактирование");
        setFields(contractDto);
    }

    private void configureModal(String title) {
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        add(header(title), accordionCompany());
    }

    private void setFields(ContractDto dto) {
        if (dto != null) {
            contractId = dto.getId();
            setField(dateField, dto.getDate());
            setField(amountField, dto.getAmount().toString());
            setField(archiveField, dto.getArchive());
            setField(commentField, dto.getComment());
            setField(numberField, dto.getNumber());
        }
        selectContractor.setValue(dto.getContractorDto());
        if (dto.getCompanyDto() != null) {
//            setField(selectCompany, dto.getCompanyDto());
            selectCompany.setValue(dto.getCompanyDto());
//            companyId = dto.getCompanyDto().getId();
//            setField(companyName, dto.getCompanyDto().getName());
//            setField(companyInn, dto.getCompanyDto().getInn());
//            setField(companyAddress, dto.getCompanyDto().getAddress());
//            setField(companyCommentToAddress, dto.getCompanyDto().getCommentToAddress());
//            setField(companyEmail, dto.getCompanyDto().getEmail());
//            setField(companyPhone, dto.getCompanyDto().getPhone());
//            setField(companyFax, dto.getCompanyDto().getFax());
//            setField(companyLeader, dto.getCompanyDto().getLeader());
//            setField(companyLeaderManagerPosition, dto.getCompanyDto().getLeaderManagerPosition());
//            setField(companyLeaderSignature, dto.getCompanyDto().getLeaderSignature());
//            setField(companyChiefAccountant, dto.getCompanyDto().getChiefAccountant());
//            setField(companyChiefAccountantSignature, dto.getCompanyDto().getChiefAccountantSignature());
//            setField(companyPayerVat, Boolean.TRUE.equals(dto.getCompanyDto().getPayerVat()) ? "Да" : "Нет");
//            setField(companySortNumber, dto.getCompanyDto().getSortNumber());
//            setField(companyStamp, dto.getCompanyDto().getStamp());
        }

        if (dto.getLegalDetailDto() != null) {
            setField(selectLegalDetail, dto.getLegalDetailDto());
//            selectLegalDetail.setItems(selectCompany.getValue().getLegalDetailDto());
//            selectLegalDetail.setValue(dto.getLegalDetailDto());


//            legalDetailId = dto.getLegalDetailDto().getId();
//            setField(legalDetailLastName, dto.getLegalDetailDto().getLastName());
//            setField(legalDetailFirstName, dto.getLegalDetailDto().getFirstName());
//            setField(legalDetailMiddleName, dto.getLegalDetailDto().getMiddleName());
//            setField(legalDetailAddress, dto.getLegalDetailDto().getAddress());
//            setField(legalDetailCommentToAddress, dto.getLegalDetailDto().getCommentToAddress());
//            setField(legalDetailInn, dto.getLegalDetailDto().getInn());
//            setField(legalDetailOkpo, dto.getLegalDetailDto().getOkpo());
//            setField(legalDetailOgrnip, dto.getLegalDetailDto().getOgrnip());
//            setField(legalDetailNumberOfTheCertificate, dto.getLegalDetailDto().getNumberOfTheCertificate());
//            setDate(legalDetailDateOfTheCertificate, dto.getLegalDetailDto().getDateOfTheCertificate());
//
//            if (dto.getLegalDetailDto().getTypeOfContractorDto() != null) {
//                typeOfContractorId = dto.getLegalDetailDto().getTypeOfContractorDto().getId();
//                setField(typeOfContractorName, dto.getLegalDetailDto().getTypeOfContractorDto().getName());
//                setField(typeOfContractorSortNumber, dto.getLegalDetailDto().getTypeOfContractorDto().getSortNumber());
//            }
        }

        setField(selectBankAccount, dto.getBankAccountDto());

//        selectBankAccount.setItems(selectCompany.getValue().getBankAccountDto());
//        selectBankAccount.setValue(dto.getBankAccountDto());
//        selectContractor.setValue(dto.getContractorDto());

//        bankAccountId = dto.getBankAccountDto().getId();
//        setField(bankAccountRcbic, dto.getBankAccountDto().getRcbic());
//        setField(bankAccountBank, dto.getBankAccountDto().getBank());
//        setField(bankAccountAddress, dto.getBankAccountDto().getAddress());
//        setField(bankAccountCorrespondentAccount, dto.getBankAccountDto().getCorrespondentAccount());
//        setField(bankAccountAccount, dto.getBankAccountDto().getAccount());
//        setField(bankAccountMainAccount, Boolean.TRUE.equals(dto.getBankAccountDto().getMainAccount()) ? "Да" : "Нет");
//        setField(bankAccountSortNumber, dto.getBankAccountDto().getSortNumber());

//        contractorId = dto.getContractorDto().getId();
//        setField(contractorName, dto.getContractorDto().getName());
//        setField(contractorInn, dto.getContractorDto().getInn());
//        setField(contractorSortNumber, dto.getContractorDto().getSortNumber());
//        setField(contractorPhone, dto.getContractorDto().getPhone());
//        setField(contractorFax, dto.getContractorDto().getFax());
//        setField(contractorEmail, dto.getContractorDto().getEmail());
//        setField(contractorAddress, dto.getContractorDto().getAddress());
//        setField(contractorCommentToAddress, dto.getContractorDto().getCommentToAddress());
//        setField(contractorComment, dto.getContractorDto().getComment());
//
//        if (dto.getContractorDto().getContractorGroupDto() != null) {
//            contractorGroupId = dto.getContractorDto().getContractorGroupDto().getId();
//            setField(contractorGroupName, dto.getContractorDto().getContractorGroupDto().getName());
//            setField(contractorGroupSortNumber, dto.getContractorDto().getContractorGroupDto().getSortNumber());
//        }
//        if (dto.getContractorDto().getTypeOfPriceDto() != null) {
//            typeOfPriceId = dto.getContractorDto().getTypeOfPriceDto().getId();
//            setField(typeOfPriceName, dto.getContractorDto().getTypeOfPriceDto().getName());
//            setField(typeOfPriceSortNumber, dto.getContractorDto().getTypeOfPriceDto().getSortNumber());
//        }

    }


    private void setField(AbstractField field, Object value) {
        if (value != null) {
            field.setValue(value);
        }
    }

    private HorizontalLayout header(String titleText) {
        HorizontalLayout header = new HorizontalLayout();
        H2 title = new H2(titleText);
        title.setHeight("2.2em");
        title.setWidth("345px");
        header.add(title);
        header.add(getDeleteButton(), buttonSave(), buttonCancel());
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
                configureContractor());
        accordion.add("Контрактор", layoutInfo).addThemeVariants(DetailsVariant.FILLED);

        VerticalLayout layoutPersons = new VerticalLayout();
        layoutPersons.add(
                configureCompany());
        accordion.add("Компания", layoutPersons).addThemeVariants(DetailsVariant.FILLED);

        VerticalLayout layoutDetails = new VerticalLayout();
        layoutDetails.add(
                configureLegalDetails());
        accordion.add("Юридические детали", layoutDetails).addThemeVariants(DetailsVariant.FILLED);

        VerticalLayout layoutBankAccount = new VerticalLayout();
        layoutBankAccount.add(
                configureBankAccount()
        );
        accordion.add("Банковские реквизиты", layoutBankAccount).addThemeVariants(DetailsVariant.FILLED);

        return accordion;
    }

    private HorizontalLayout configureContractor(){
        selectContractor.setItems(contractorService.getAll());
        selectContractor.setItemLabelGenerator(contractorDto -> contractorDto.getName());
        selectContractor.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Контрактор", selectContractor);
    }

    private HorizontalLayout configureCompany(){
        selectCompany.setItems(companyService.getAll());
        selectCompany.setItemLabelGenerator(
                companyDto -> companyDto.getName() + ", ИНН: " + companyDto.getInn()
        );
        selectCompany.addValueChangeListener(event -> {
            selectBankAccount.setItems(selectCompany.getValue().getBankAccountDto());
            selectLegalDetail.setItems(selectCompany.getValue().getLegalDetailDto());
        });
        selectCompany.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Компания", selectCompany);
    }

    private HorizontalLayout configureBankAccount(){
        selectBankAccount.setWidth(FIELD_WIDTH);
        selectBankAccount.setItemLabelGenerator(
                bankAccountDto -> bankAccountDto.getBank() + " " + bankAccountDto.getAccount());
        return getHorizontalLayout("Банковский аккаунт", selectBankAccount);
    }

    private HorizontalLayout configureLegalDetails(){
        selectLegalDetail.setWidth(FIELD_WIDTH);
        selectLegalDetail.setItemLabelGenerator(
                legalDetailDto -> legalDetailDto.getLastName() + " "
                        + legalDetailDto.getFirstName() + " "
                        + legalDetailDto.getMiddleName()
        );
        return getHorizontalLayout("Юридические детали", selectLegalDetail);
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

    private HorizontalLayout getHorizontalLayout(String title, Component field) {
        Label label = new Label(title);
        label.setWidth("100px");
        return new HorizontalLayout(label, field);
    }

    private Button buttonSave() {
        return new Button("Сохранить", event -> {

            ContractDto contractDto = new ContractDto();
            contractDto.setDate(dateField.getValue().toString());
            contractDto.setAmount(new BigDecimal(amountField.getValue()));
            contractDto.setArchive(archiveField.getValue());
            contractDto.setComment(commentField.getValue());
            contractDto.setNumber(numberField.getValue());
            contractDto.setContractorDto(selectContractor.getValue());
            contractDto.setCompanyDto(selectCompany.getValue());
            contractDto.setBankAccountDto(selectBankAccount.getValue());
            contractDto.setLegalDetailDto(selectLegalDetail.getValue());
            contractDto.setId(contractId);

            if (contractId == null) {
                contractService.create(contractDto);
            } else {
                contractService.update(contractDto);
            }
            close();

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

}
