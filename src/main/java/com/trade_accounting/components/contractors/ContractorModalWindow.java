package com.trade_accounting.components.contractors;

import com.trade_accounting.components.util.ValidTextField;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.ContractorGroupDto;
import com.trade_accounting.models.dto.LegalDetailDto;
import com.trade_accounting.models.dto.TypeOfContractorDto;
import com.trade_accounting.models.dto.TypeOfPriceDto;
import com.trade_accounting.services.interfaces.ContractorGroupService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.LegalDetailService;
import com.trade_accounting.services.interfaces.TypeOfContractorService;
import com.trade_accounting.services.interfaces.TypeOfPriceService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;

import java.util.List;
import java.util.Objects;

public class ContractorModalWindow extends Dialog {

    private final TextField idField = new TextField();
    private final TextField nameField = new TextField();
    private final ValidTextField innField = new ValidTextField();
    private final TextField sortNumberField = new TextField();
    private final TextField phoneField = new TextField();
    private final TextField faxField = new TextField();
    private final TextField emailField = new TextField();
    private final TextArea addressField = new TextArea();
    private final TextArea commentToAddressField = new TextArea();
    private final TextArea commentField = new TextArea();

    private final ComboBox<ContractorGroupDto> contractorGroupDtoSelect = new ComboBox<>();
    private final ComboBox<TypeOfContractorDto> typeOfContractorDtoSelect = new ComboBox<>();
    private final ComboBox<TypeOfPriceDto> typeOfPriceDtoSelect = new ComboBox<>();
    private final ComboBox<LegalDetailDto> legalDetailDtoSelect = new ComboBox<>();
    private final Binder<ContractorDto> contractorDtoBinder = new Binder<>(ContractorDto.class);
    private final Binder<LegalDetailDto> legalDetailDtoBinder = new Binder<>(LegalDetailDto.class);

    private static final String LABEL_WIDTH = "100px";
    private static final String FIELD_WIDTH = "400px";

    private final ContractorService contractorService;
    private final ContractorGroupService contractorGroupService;
    private final TypeOfContractorService typeOfContractorService;
    private final TypeOfPriceService typeOfPriceService;
    private final LegalDetailService legalDetailService;

    private ContractorDto contractorDto ;
    private LegalDetailDto legalDetailDto ;
    List<TypeOfContractorDto> typeOfContractorDtoList;
   // private final TextField idLegalDetailField = new TextField("ID");
    private final TextField lastNameLegalDetailField = new TextField("Фамилия");
    private final TextField firstNameLegalDetailField = new TextField("Имя");
    private final TextField middleNameLegalDetailField = new TextField("Отчество");
    private final TextField addressLegalDetailField = new TextField("Юридический адрес");
    private final TextField commentToAddressLegalDetailField = new TextField("Комментарий к адресу");
    private final ValidTextField innLegalDetailField = new ValidTextField("Инн");;
    private final TextField okpoLegalDetailField = new TextField("ОКПО");
    private final TextField ogrnipLegalDetailField = new TextField("ОГРН");
    private final TextField numberOfTheCertificateLegalDetailField = new TextField("Номер сертефиката");
    private final TextField  dateOfTheCertificateLegalDetailField = new TextField("Дата сертефиката");

    private final ComboBox<TypeOfContractorDto> typeOfContractorDtoLegalDetailField = new ComboBox<>("Тип контракта.");
//    private final TextField  typeOfContractorDtoLegalDetailTextField = new TextField("Тип контракта.");

    public ContractorModalWindow(ContractorDto contractorDto,
                                 ContractorService contractorService,
                                 ContractorGroupService contractorGroupService,
                                 TypeOfContractorService typeOfContractorService,
                                 TypeOfPriceService typeOfPriceService,
                                 LegalDetailService legalDetailService) {
        this.contractorService = contractorService;
        this.contractorGroupService = contractorGroupService;
        this.typeOfContractorService = typeOfContractorService;
        this.typeOfPriceService = typeOfPriceService;
        this.legalDetailService = legalDetailService;
        this.contractorDto = contractorDto;

        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);

        idField.setValue(getFieldValueNotNull(String.valueOf(contractorDto.getId())));
        nameField.setValue(getFieldValueNotNull(contractorDto.getName()));
        innField.setValue(getFieldValueNotNull(contractorDto.getInn()));
        sortNumberField.setValue(getFieldValueNotNull(contractorDto.getSortNumber()));
        phoneField.setValue(getFieldValueNotNull(contractorDto.getPhone()));
        faxField.setValue(getFieldValueNotNull(contractorDto.getFax()));
        emailField.setValue(getFieldValueNotNull(contractorDto.getEmail()));
        addressField.setValue(getFieldValueNotNull(contractorDto.getAddress()));
        commentToAddressField.setValue(getFieldValueNotNull(contractorDto.getCommentToAddress()));
        commentField.setValue(getFieldValueNotNull(contractorDto.getComment()));
        add(new Text("Наименование"), header(), contractorsAccordion());
    }

    public void setContractorDataForEdit(ContractorDto contractorDto) {
        if (contractorDto.getContractorGroupName() != null) {
            contractorGroupDtoSelect.setValue(contractorService
                    .getById(contractorDto.getId()).getContractorGroupDto());
        }

        if (contractorDto.getTypeOfContractorName() != null) {
            typeOfContractorDtoSelect.setValue(contractorService
                    .getById(contractorDto.getId()).getTypeOfContractorDto());
        }

        if (contractorDto.getTypeOfPriceName() != null) {
            typeOfPriceDtoSelect.setValue(contractorService
                    .getById(contractorDto.getId()).getTypeOfPriceDto());
        }

        if (contractorDto.getLegalDetailInn() != null) {

           // idLegalDetailField.setValue(String.valueOf(legalDetailDto.getId()));
            lastNameLegalDetailField.setValue(legalDetailDto.getLastName());
            firstNameLegalDetailField.setValue(legalDetailDto.getFirstName());
            middleNameLegalDetailField.setValue(legalDetailDto.getMiddleName());
            addressLegalDetailField.setValue(legalDetailDto.getAddress());
            commentToAddressLegalDetailField.setValue(legalDetailDto.getCommentToAddress());
            innLegalDetailField.setValue(legalDetailDto.getInn());
            okpoLegalDetailField.setValue(legalDetailDto.getOkpo());
            ogrnipLegalDetailField.setValue(legalDetailDto.getOgrnip());
            numberOfTheCertificateLegalDetailField.setValue(legalDetailDto.getNumberOfTheCertificate());
            dateOfTheCertificateLegalDetailField.setValue(legalDetailDto.getDateOfTheCertificate());

            if (typeOfContractorDtoList != null) {
                typeOfContractorDtoLegalDetailField.setItems(typeOfContractorDtoList);
                typeOfContractorDtoLegalDetailField.setValue(contractorService
                        .getById(contractorDto.getId()).getTypeOfContractorDto());
                typeOfContractorDtoLegalDetailField.setItemLabelGenerator(TypeOfContractorDto::getName);
            }

        }

    }

    private Details contractorsAccordion() {
        Details componentAll = new Details();
        componentAll.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
        componentAll.setOpened(true);

        Details componentFormContractDto = new Details("О контрагенте",new Text(" " ));
        componentFormContractDto.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
        componentFormContractDto.setOpened(true);
        componentFormContractDto.addContent(
                contractorGroupSelect(),
                configureInnField(),
                configurePhoneField(),
                configureFaxField(),
                configureEmailField(),
                configureAddressField(),
                configureCommentToAddressField(),
                configureCommentField(),
                configureSortNumberField());
        add(componentFormContractDto);


        Details componentContactFaces = new Details("Контактные лица",new Text("Добавить компоненты." ));
        componentContactFaces.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
        componentContactFaces.addOpenedChangeListener(e ->
                Notification.show(e.isOpened() ? "Opened" : "Closed"));
        add(componentContactFaces);

        Details componentDetails = new Details("Реквизиты",new Text(" " ) );
        componentDetails.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
        componentDetails.addContent(typeOfContractorSelect(),legalDetailSelect());
        add(componentDetails);

        Details componentLayoutTypeOfPrice = new Details("Скидки и цены",new Text(" " ) );
        componentLayoutTypeOfPrice.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
        componentLayoutTypeOfPrice.addContent(typeOfPriceSelect());
        add(componentLayoutTypeOfPrice );

        Details componentAccesses = new Details("Доступ",new Text(" Добавить компоненты." ));
        componentAccesses.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
        //"Expandable Details",new Text("Toggle using mouse, Enter and Space keys."));
        add(componentAccesses);

        componentAll.addContent(componentFormContractDto,componentContactFaces,
                componentDetails,componentLayoutTypeOfPrice, componentAccesses );

        return componentAll;

    }

    private  FormLayout contractorsAccordionCreate() {
        //Accordion accordion = new Accordion();
        FormLayout  accountForm = new FormLayout();
        // VerticalLayout verticalLayout = new VerticalLayout();
        // verticalLayout.add(
        accountForm.add(lastNameLegalDetailField);
        accountForm.add(firstNameLegalDetailField);
        accountForm.add(middleNameLegalDetailField);
        accountForm.add(addressLegalDetailField);
        accountForm.add(commentToAddressLegalDetailField);
        accountForm.add(innLegalDetailField);
        accountForm.add(okpoLegalDetailField);
        accountForm.add(ogrnipLegalDetailField);
        accountForm.add(numberOfTheCertificateLegalDetailField);
        accountForm.add(dateOfTheCertificateLegalDetailField);

//            accountForm.add(typeOfContractorDtoLegalDetailField);

//        accordion.setWidth("275px");
//        accordion.add("Реквизиты", accountForm);
//        return accordion;
        accountForm.setWidth("275px");
        return accountForm;
    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        nameField.setWidth("445px");
        contractorDtoBinder.forField(nameField)
                .asRequired( "Не заполнено!")
                .bind("name");
        header.add(nameField, getSaveButton(), getCancelButton());
        return header;
    }

    private HorizontalLayout contractorGroupSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractorGroupDto> contractorGroupDtoList = contractorGroupService.getAll();

        if (contractorGroupDtoList != null) {
            contractorGroupDtoSelect.setItems(contractorGroupDtoList);
        }
        contractorGroupDtoSelect.setItemLabelGenerator(ContractorGroupDto::getName);
        contractorGroupDtoSelect.setWidth(FIELD_WIDTH);
        contractorDtoBinder.forField(contractorGroupDtoSelect)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("contractorGroupDto");
        Label label = new Label("Группы");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, contractorGroupDtoSelect);
        return horizontalLayout;

    }

    private HorizontalLayout typeOfContractorSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

         typeOfContractorDtoList = typeOfContractorService.getAll();
        if (typeOfContractorDtoList != null) {
            typeOfContractorDtoSelect.setItems(typeOfContractorDtoList);
        }
        typeOfContractorDtoSelect.setItemLabelGenerator(TypeOfContractorDto::getName);
        typeOfContractorDtoSelect.setWidth(FIELD_WIDTH);
        contractorDtoBinder.forField(typeOfContractorDtoSelect)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("typeOfContractorDto");
        Label label = new Label("Тип контрагента");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, typeOfContractorDtoSelect);
        return horizontalLayout;
    }

    private HorizontalLayout typeOfPriceSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        List<TypeOfPriceDto> typeOfPriceDtoList = typeOfPriceService.getAll();
        if (typeOfPriceDtoList != null) {
            typeOfPriceDtoSelect.setItems(typeOfPriceDtoList);
        }
        typeOfPriceDtoSelect.setItemLabelGenerator(TypeOfPriceDto::getName);
        typeOfPriceDtoSelect.setWidth(FIELD_WIDTH);
        contractorDtoBinder.forField(typeOfPriceDtoSelect)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("typeOfPriceDto");
        Label label = new Label("Цены");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add( label,typeOfPriceDtoSelect);
        return horizontalLayout;
    }

    private HorizontalLayout legalDetailSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        if (contractorDto.getId() != null) {
            legalDetailDto = contractorService
                    .getById(contractorDto.getId()).getLegalDetailDto();
            if (legalDetailDto != null) {
                legalDetailDtoSelect.setItems(legalDetailDto);
                legalDetailDtoSelect.setValue(contractorService
                        .getById(contractorDto.getId()).getLegalDetailDto());
            }
//            legalDetailDtoSelect.setWidth(FIELD_WIDTH);
        }

        legalDetailDtoBinder.forField(lastNameLegalDetailField)
                .asRequired( "Не заполнено!").bind("lastName");
        legalDetailDtoBinder.forField(firstNameLegalDetailField)
                .asRequired( "Не заполнено!").bind("firstName");
        legalDetailDtoBinder.forField(middleNameLegalDetailField)
                .asRequired( "Не заполнено!").bind("middleName");
        legalDetailDtoBinder.forField(addressLegalDetailField)
                .asRequired( "Не заполнено!").bind("address");
        legalDetailDtoBinder.forField(commentToAddressLegalDetailField)
                .asRequired( "Не заполнено!").bind("commentToAddress");
        legalDetailDtoBinder.forField(innLegalDetailField)
                .asRequired( "Не заполнено!").bind("inn");
        legalDetailDtoBinder.forField(okpoLegalDetailField)
                .asRequired( "Не заполнено!").bind("okpo");
        legalDetailDtoBinder.forField(ogrnipLegalDetailField)
                .asRequired( "Не заполнено!").bind("ogrnip");
        legalDetailDtoBinder.forField(numberOfTheCertificateLegalDetailField)
                .asRequired( "Не заполнено!").bind("numberOfTheCertificate");
        legalDetailDtoBinder.forField(dateOfTheCertificateLegalDetailField)
                .asRequired( "Не заполнено!").bind("dateOfTheCertificate");
        horizontalLayout.add(contractorsAccordionCreate());

        return horizontalLayout;
    }

    private HorizontalLayout configureInnField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Инн");
        label.setWidth(LABEL_WIDTH);
        innField.setWidth(FIELD_WIDTH);
        contractorDtoBinder.forField(innField)
                .asRequired( "Не заполнено!")
                .bind("inn");
        innField.addInputListener(inputEvent ->
                innField.addValidator(new RegexpValidator("Only 10 or 12 digits.",
                        "^([0-9]{10}|[0-9]{12})$")));

        horizontalLayout.add(label, innField);
        return horizontalLayout;
    }

    private HorizontalLayout configurePhoneField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        phoneField.setWidth(FIELD_WIDTH);
        contractorDtoBinder.forField(phoneField)
                .asRequired( "Не заполнено!")
                .bind("phone");
        Label label = new Label("Телефон");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, phoneField);
        return horizontalLayout;
    }

    private HorizontalLayout configureFaxField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Факс");
        label.setWidth(LABEL_WIDTH);
        faxField.setWidth(FIELD_WIDTH);
        contractorDtoBinder.forField(faxField)
                .asRequired( "Не заполнено!")
                .bind("fax");
        horizontalLayout.add(label, faxField);
        return horizontalLayout;
    }

    private HorizontalLayout configureEmailField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Емейл");
        label.setWidth(LABEL_WIDTH);
        contractorDtoBinder.forField(emailField)
                .asRequired( "Не заполнено!")
                .bind("email");
        emailField.setWidth("345px");
        Button emailButton = new Button();
        emailButton.setIcon(new Icon(VaadinIcon.ENVELOPE));
        horizontalLayout.add(label, emailField, emailButton);
        return horizontalLayout;
    }

    private HorizontalLayout configureAddressField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Фактический адресс");
        label.setWidth(LABEL_WIDTH);
        contractorDtoBinder.forField(addressField)
                .asRequired( "Не заполнено!")
                .bind("address");
        addressField.setWidth("345px");
        Button emailButton = new Button();
        emailButton.setIcon(new Icon(VaadinIcon.CARET_DOWN));
        horizontalLayout.add(label, addressField, emailButton);
        return horizontalLayout;
    }

    private HorizontalLayout configureCommentToAddressField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Комментарий к адресу");
        label.setWidth(LABEL_WIDTH);
        commentToAddressField.setWidth(FIELD_WIDTH);
        contractorDtoBinder.forField(commentToAddressField)
                .asRequired( "Не заполнено!")
                .bind("commentToAddress");
        commentToAddressField.getStyle().set("minHeight", "120px");
        horizontalLayout.add(label, commentToAddressField);
        return horizontalLayout;
    }

    private HorizontalLayout configureCommentField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Комментарий");
        label.setWidth(LABEL_WIDTH);
        commentField.setWidth(FIELD_WIDTH);
        contractorDtoBinder.forField(commentField)
                .asRequired( "Не заполнено!")
                .bind("comment");
        commentField.getStyle().set("minHeight", "120px");
        horizontalLayout.add(label, commentField);
        return horizontalLayout;
    }

    private HorizontalLayout configureSortNumberField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Код");
        label.setWidth(LABEL_WIDTH);
        contractorDtoBinder.forField(sortNumberField)
                .asRequired( "Не заполнено!")
                .bind("sortNumber");
        sortNumberField.setWidth(FIELD_WIDTH);
        horizontalLayout.add(label, sortNumberField);
        return horizontalLayout;
    }

    private Button getSaveButton() {
        if (nameField.isEmpty()) {
            //ContractorDto contractorDtoNew = new ContractorDto();
                         legalDetailDto = new LegalDetailDto();
            return new Button("Добавить", event -> {
                saveFieldsCreate(legalDetailDto );
                legalDetailService.create(legalDetailDto);
                saveFields(contractorDto );
                contractorService.create(contractorDto );
                if (!innField.isEmpty() && innField.getValue()
                            .matches("^([0-9]{10}|[0-9]{12})$"))
                    close();
            });
        } else {
             contractorDto = contractorService.getById(Long.valueOf(idField.getValue()));
            return new Button("Изменить", event -> {
                saveFieldsCreate(legalDetailDto);//,contractorDto
                legalDetailService.create(legalDetailDto);
                saveFields(contractorDto);
                contractorService.update(contractorDto);
                if (!innField.isEmpty() && innField.getValue()
                        .matches("^([0-9]{10}|[0-9]{12})$")) {
                    close();
                }
            });
        }
    }

    private LegalDetailDto saveFieldsCreate(LegalDetailDto legalDetailDto) {

        legalDetailDto.setLastName(lastNameLegalDetailField.getValue());
        legalDetailDto.setFirstName(firstNameLegalDetailField.getValue());
        legalDetailDto.setMiddleName(middleNameLegalDetailField.getValue());
        legalDetailDto.setAddress(addressLegalDetailField.getValue());
        legalDetailDto.setCommentToAddress(commentToAddressLegalDetailField.getValue());
        legalDetailDto.setInn(innLegalDetailField.getValue());
        legalDetailDto.setOkpo(okpoLegalDetailField.getValue());
        legalDetailDto.setOgrnip(ogrnipLegalDetailField.getValue());
        legalDetailDto.setNumberOfTheCertificate(numberOfTheCertificateLegalDetailField.getValue());
        legalDetailDto.setDateOfTheCertificate(dateOfTheCertificateLegalDetailField.getValue());

        legalDetailDto.setTypeOfContractorDto(typeOfContractorDtoLegalDetailField.getValue());

        return legalDetailDto;
    }

    private void saveFields(ContractorDto contractorDto) {
        contractorDto.setName(nameField.getValue());
        contractorDto.setInn(innField.getValue());
        contractorDto.setPhone(phoneField.getValue());
        contractorDto.setFax(faxField.getValue());
        contractorDto.setEmail(emailField.getValue());
        contractorDto.setAddress(addressField.getValue());
        contractorDto.setCommentToAddress(commentToAddressField.getValue());
        contractorDto.setComment(commentField.getValue());
        contractorDto.setSortNumber(sortNumberField.getValue());

        if (contractorDto.getId()!= null) {
            contractorDto.setLegalDetailId(legalDetailDtoSelect.getValue().getId());
            contractorDto.setContractorGroupId(contractorGroupDtoSelect.getValue().getId());
            contractorDto.setTypeOfContractorId(typeOfContractorDtoSelect.getValue().getId());
            contractorDto.setTypeOfPriceId(typeOfPriceDtoSelect.getValue().getId());
        }else {
            contractorDto.setLegalDetailDto(legalDetailDto);
            contractorDto.setContractorGroupDto(contractorGroupDtoSelect.getValue());
            contractorDto.setTypeOfContractorDto(typeOfContractorDtoSelect.getValue());
            contractorDto.setTypeOfPriceDto(typeOfPriceDtoSelect.getValue());
        }
    }

    private Button getCancelButton() {
        return new Button("Закрыть", event -> close());
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }

}
