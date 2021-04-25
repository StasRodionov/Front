package com.trade_accounting.components.contractors;

import com.trade_accounting.components.util.ValidTextField;
import com.trade_accounting.models.dto.AddressDto;
import com.trade_accounting.models.dto.ContactDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.ContractorGroupDto;
import com.trade_accounting.models.dto.FiasModelDto;
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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.dom.Style;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ContractorModalWindow extends Dialog {
    private final TextField idField = new TextField();
    private final TextArea nameField = new TextArea();
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
    private final Map<Long, List<TextField>> existContactTextFields = new HashMap<>();
    private final List<List<TextField>> newContactTextFields = new ArrayList<>();
    private final Binder<ContractorDto> contractorDtoBinder = new Binder<>(ContractorDto.class);
    private final Binder<LegalDetailDto> legalDetailDtoBinder = new Binder<>(LegalDetailDto.class);

    private static final String LABEL_WIDTH = "100px";
    private static final String FIELD_WIDTH = "400px";
    private static final String MODAL_WINDOW_WIDTH = "650px";

    private final ContractorService contractorService;
    private final ContractorGroupService contractorGroupService;
    private final TypeOfContractorService typeOfContractorService;
    private final TypeOfPriceService typeOfPriceService;
    private final LegalDetailService legalDetailService;

    private ContractorDto contractorDto;
    private LegalDetailDto legalDetailDto;
    private final Binder<ContactDto> contactDtoBinder = new Binder<>(ContactDto.class);
    private List<TypeOfContractorDto> typeOfContractorDtoList;
    private final TextField lastNameLegalDetailField = new TextField(); //"Фамилия"
    private final TextField firstNameLegalDetailField = new TextField(); //"Имя"
    private final TextField middleNameLegalDetailField = new TextField(); //"Отчество"
    private final TextArea addressLegalDetailField = new TextArea(); //"Юридический адрес"
    private final TextArea commentToAddressLegalDetailField = new TextArea(); //"Комментарий к адресу"
    private final ValidTextField inn = new ValidTextField(); //"ИНН"
    private final ValidTextField innLegalDetailField = new ValidTextField(); //"ИНН"
    private final TextArea kppLegalDetailField = new TextArea(); //"КПП"
    private final ValidTextField okpoLegalDetailField = new ValidTextField(); //"ОКПО"
    private final ValidTextField ogrnipLegalDetailField = new ValidTextField(); //"ОГРН"
    private final ValidTextField numberOfTheCertificateLegalDetailField = new ValidTextField(); //"Номер сертефиката"
    private final ValidTextField dateOfTheCertificateLegalDetailField = new ValidTextField(); //"Дата сертефиката"

    private final ComboBox<TypeOfContractorDto> typeOfContractorDtoLegalDetailField = new ComboBox<>("Тип контракта.");
//    private final TextField  typeOfContractorDtoLegalDetailTextField = new TextField("Тип контракта.");

    // блок адреса
    private final TextField addressIndex = new TextField();
    private final TextField addressCountry = new TextField();
    public final ComboBox<String> addressRegion = new ComboBox<>();
    private final ComboBox<String> addressCity = new ComboBox<>();
    private final ComboBox<String> addressStreet = new ComboBox<>();
    private final TextField addressHouse = new TextField();
    private final TextField addressApartment = new TextField();
    private final Binder<AddressDto> addressDtoBinder = new Binder<>(AddressDto.class);

    // блок юр адреса
    private final TextField addressIndexLegalDetail = new TextField();
    private final TextField addressCountryLegalDetail = new TextField();
    private final TextField addressRegionLegalDetail = new TextField();
    private final TextField addressCityLegalDetail = new TextField();
    private final TextField addressStreetLegalDetail = new TextField();
    private final TextField addressHouseLegalDetail = new TextField();
    private final TextField addressApartmentLegalDetail = new TextField();
    private final HorizontalLayout blockLegalDetail;

    private final HorizontalLayout block;

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

        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);

        idField.setValue(getFieldValueNotNull(String.valueOf(contractorDto.getId())));
        nameField.setValue(getFieldValueNotNull(contractorDto.getName()));
        innField.setValue(getFieldValueNotNull(contractorDto.getInn()));
        sortNumberField.setValue(getFieldValueNotNull(contractorDto.getSortNumber()));
        phoneField.setValue(getFieldValueNotNull(contractorDto.getPhone()));
        faxField.setValue(getFieldValueNotNull(contractorDto.getFax()));
        emailField.setValue(getFieldValueNotNull(contractorDto.getEmail()));
        addressField.setValue(getFieldValueNotNull(
                getAddress(contractorDto)
        ));
        commentToAddressField.setValue(getFieldValueNotNull(contractorDto.getCommentToAddress()));
        commentField.setValue(getFieldValueNotNull(contractorDto.getComment()));
        block = configureAddressBlock();
        blockLegalDetail = configureAddressBlockLegalDetail();
        add(new Text("Наименование"), header(), contractorsAccordion());
        setWidth(MODAL_WINDOW_WIDTH);
    }

    private String getAddress(ContractorDto contractorDto) {
        if (contractorDto.getId() != null) {
            return contractorService.getById(contractorDto.getId()).getAddressDto().getAnother();
        } else {
            return "";
        }
    }

    private String getAddressFromLegalDetail(LegalDetailDto legalDetailDto) {
        if (legalDetailDto.getId() != null) {
            return legalDetailService.getById(legalDetailDto.getId()).getAddressDto().getAnother();
        } else {
            return "";
        }
    }

    public void setContractorDataForEdit(ContractorDto contractorDto) {
        if (contractorDto.getContractorGroupDto().getName() != null) {
            contractorGroupDtoSelect.setValue(contractorService
                    .getById(contractorDto.getId()).getContractorGroupDto());
        }

        if (contractorDto.getTypeOfContractorDto().getName() != null) {
            typeOfContractorDtoSelect.setValue(contractorService
                    .getById(contractorDto.getId()).getTypeOfContractorDto());
        }

        if (contractorDto.getTypeOfPriceDto().getName() != null) {
            typeOfPriceDtoSelect.setValue(contractorService
                    .getById(contractorDto.getId()).getTypeOfPriceDto());
        }

        if (contractorDto.getLegalDetailDto().getInn() != null) {
            // idLegalDetailField.setValue(String.valueOf(legalDetailDto.getId()));
            lastNameLegalDetailField.setValue(legalDetailDto.getLastName());
            firstNameLegalDetailField.setValue(legalDetailDto.getFirstName());
            middleNameLegalDetailField.setValue(legalDetailDto.getMiddleName());
            addressLegalDetailField.setValue(getFieldValueNotNull(getAddressFromLegalDetail(legalDetailDto)));
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

        Details componentFormContractDto = new Details("О контрагенте", new Text(" "));
        componentFormContractDto.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
        componentFormContractDto.setOpened(true);
        componentFormContractDto.addContent(
                contractorGroupSelect(),
                configureInnField(),
                configurePhoneField(),
                configureFaxField(),
                configureEmailField(),
                configureAddressField(),
                block,
                configureCommentToAddressField(),
                configureCommentField(),
                configureSortNumberField());
        add(componentFormContractDto);


        Details componentContactFaces = new Details("Контактные лица", new Text(" "));
        componentContactFaces.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
        componentContactFaces.addContent(contactDetailSelect());
        add(componentContactFaces);

        Details componentDetails = new Details("Реквизиты", new Text(" "));
        componentDetails.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
        componentDetails.addContent(legalDetailSelect());
        componentDetails.setOpened(true);
        add(componentDetails);

        Details componentLayoutTypeOfPrice = new Details("Скидки и цены", new Text(" "));
        componentLayoutTypeOfPrice.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
        componentLayoutTypeOfPrice.addContent(typeOfPriceSelect());
        componentLayoutTypeOfPrice.setOpened(true);
        add(componentLayoutTypeOfPrice);

        Details componentAccesses = new Details("Доступ", new Text(" Добавить компоненты."));
        componentAccesses.addThemeVariants(DetailsVariant.REVERSE, DetailsVariant.FILLED);
        //"Expandable Details",new Text("Toggle using mouse, Enter and Space keys."));
        add(componentAccesses);

        componentAll.addContent(componentFormContractDto, componentContactFaces,
                componentDetails, componentLayoutTypeOfPrice, componentAccesses);

        return componentAll;

    }

    private HorizontalLayout configureInnField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Инн");
        label.setWidth(LABEL_WIDTH);
        innField.setWidth(FIELD_WIDTH);
        contractorDtoBinder.forField(innField)
                .asRequired("Не заполнено!")
                .bind("inn");
        innField.addInputListener(inputEvent ->
                innField.addValidator(new RegexpValidator("Only 10 or 12 digits.",
                        "^([0-9]{10}|[0-9]{12})$")));

        horizontalLayout.add(label, innField);
        return horizontalLayout;
    }

    private VerticalLayout contactDetailSelect() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(getAddContactButton(verticalLayout));
        if (contractorDto.getId() != null) {
            List<ContactDto> contactDtos = contractorService
                    .getById(contractorDto.getId()).getContactDto();
            if (contactDtos != null) {
                contactDtos.forEach(contactDto -> showContact(contactDto, verticalLayout));
            }
        }

        return verticalLayout;
    }

    private void showContact(ContactDto contact, VerticalLayout verticalLayout) {
        FormLayout contactForm = new FormLayout();
        Style addressFormStyle = contactForm.getStyle();
        addressFormStyle.set("width", "385px");
        //Блок контактных лиц
        TextField contactFullName = new TextField();
        TextField contactPosition = new TextField();
        TextField contactPhone = new TextField();
        TextField contactEmail = new TextField();
        TextField contactComment = new TextField();

        contactForm.add("Контактное лицо");
        contactForm.addFormItem(contactFullName, "ФИО");
        contactDtoBinder.forField(contactFullName).asRequired("Обязательное поле").bind(ContactDto::getFullName, ContactDto::setFullName);

        contactForm.addFormItem(contactPosition, "Должность");
        contactDtoBinder.forField(contactPosition).bind(ContactDto::getPosition, ContactDto::setPosition);

        contactForm.addFormItem(contactPhone, "Телефон");
        contactDtoBinder.forField(contactPhone).bind(ContactDto::getPhone, ContactDto::setPhone);

        contactForm.addFormItem(contactEmail, "Электронный адрес");
        contactDtoBinder.forField(contactEmail).bind(ContactDto::getEmail, ContactDto::setEmail);


        contactForm.addFormItem(contactComment, "Комментарий");
        contactDtoBinder.forField(contactComment).bind(ContactDto::getComment, ContactDto::setComment);

        contactFullName.setPlaceholder("ФИО");
        contactFullName.setValue(contact.getFullName());
        contactFullName.setRequired(true);
        contactFullName.setRequiredIndicatorVisible(true);
        contactPosition.setPlaceholder("Должность");
        contactPosition.setValue(contact.getPosition());
        contactPhone.setPlaceholder("Телефон");
        contactPhone.setValue(contact.getPhone());
        contactEmail.setPlaceholder("Электронный адрес");
        contactEmail.setValue(contact.getEmail());
        contactComment.setPlaceholder("Комментарий");
        contactComment.setValue(contact.getComment());

        if (contact.getId() == null) {
            newContactTextFields.add(List.of(contactFullName, contactPosition, contactPhone, contactEmail, contactComment));
        } else {
            existContactTextFields.put(contact.getId(), List.of(contactFullName, contactPosition, contactPhone, contactEmail, contactComment));
        }

        verticalLayout.addComponentAtIndex(1, contactForm);

    }

    private Button getAddContactButton(VerticalLayout verticalLayout) {
        ContactDto newContactDto = ContactDto.builder().fullName("").position("")
                .phone("").email("").comment("").build();
        Button addContactButton = new Button("", event -> {
            showContact(newContactDto, verticalLayout);
        });
        addContactButton.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE_O));

        return addContactButton;

    }

    private FormLayout contractorsAccordionCreate() {
        //Получение select-ора "Тип контрагента"
        HorizontalLayout typeOfContractor = typeOfContractorSelect();

        HorizontalLayout inn = getNumberField(legalDetailDtoBinder, innLegalDetailField, "inn", "ИНН");
        HorizontalLayout lastName = getField(legalDetailDtoBinder, lastNameLegalDetailField, "lastName", "Фамилия");
        HorizontalLayout firstName = getField(legalDetailDtoBinder, firstNameLegalDetailField, "firstName", "Имя");
        HorizontalLayout middleName = getField(legalDetailDtoBinder, middleNameLegalDetailField, "middleName", "Отчество");
        HorizontalLayout name = getArea(contractorDtoBinder, nameField, "name", "Полное наименование");
        HorizontalLayout commentToAddress = getArea(legalDetailDtoBinder, commentToAddressLegalDetailField, "commentToAddress", "Комментарий к адресу");
//        HorizontalLayout kpp = getNumberField(legalDetailDtoBinder, kppLegalDetailField, "kpp", "КПП");
        kppLegalDetailField.setValue("ждём появлени КПП в сущности");
        HorizontalLayout kpp = getSmallField(kppLegalDetailField, "КПП"); // - временная заглушка
        HorizontalLayout okpo = getNumberField(legalDetailDtoBinder, okpoLegalDetailField, "okpo", "ОКПО");
        HorizontalLayout ogrnip = getNumberField(legalDetailDtoBinder, ogrnipLegalDetailField, "ogrnip", "ОГРН");
        HorizontalLayout numberOfTheCertificate = getNumberField(legalDetailDtoBinder,
                numberOfTheCertificateLegalDetailField, "numberOfTheCertificate", "Номер сертефиката");
        dateOfTheCertificateLegalDetailField.addValidator(
                Objects::isNull, "Not Vaadin!");
        HorizontalLayout dateOfTheCertificate = getField(legalDetailDtoBinder, dateOfTheCertificateLegalDetailField,
                "dateOfTheCertificate", "Дата сертефиката");

        FormLayout accountForm = new FormLayout();
        AtomicBoolean legalEntity = new AtomicBoolean(false);
        AtomicBoolean individual = new AtomicBoolean(false);
        AtomicBoolean person = new AtomicBoolean(false);

        //Реализация выбора "Тип контрагента"
        //Юр. лицо - 1, ИП - 2, ФЛ - 3
        typeOfContractorDtoSelect.addValueChangeListener(event -> {
            String sortNumber = event.getValue().getSortNumber();

            switch (sortNumber) {
                case "1":
                    legalEntity.set(true);
                    individual.set(false);
                    person.set(false);
                    break;
                case "2":
                    legalEntity.set(false);
                    individual.set(true);
                    person.set(false);
                    break;
                case "3":
                    legalEntity.set(false);
                    individual.set(false);
                    person.set(true);
                    break;
            }

//Тип контрагента - Физическое лицо
//ИНН, Фамилия, Имя, Отчество, Адрес регистрации, Комментарий к адресу
//Тип контрагента - Юридическое лицо
//ИНН, Полное наименование, Юридический адрес, Комментарий к адресу, КПП, ОГРН, ОКПО
//Тип контрагента - Индивидуальный предприниматель
//ИНН, Фамилия, Имя, Отчество, Адрес регистрации, Комментарий к адресу, ОКПО, ОГРНИП, Номер свидетельства, Дата свидетельства
            lastName.setVisible(person.get() || individual.get());
            firstName.setVisible(person.get() || individual.get());
            middleName.setVisible(person.get() || individual.get());
            name.setVisible(legalEntity.get());
            kpp.setVisible(legalEntity.get());
            okpo.setVisible(legalEntity.get() || individual.get());
            ogrnip.setVisible(legalEntity.get() || individual.get());
            numberOfTheCertificate.setVisible(individual.get());
            dateOfTheCertificate.setVisible(individual.get());
        });

        accountForm.add(typeOfContractor);
        accountForm.add(inn);
        accountForm.add(lastName);
        accountForm.add(firstName);
        accountForm.add(middleName);
        accountForm.add(name);
        accountForm.add(configureAddressFieldLegalDetail());
        accountForm.add(blockLegalDetail);
        accountForm.add(commentToAddress);
        accountForm.add(kpp);
        accountForm.add(okpo);
        accountForm.add(ogrnip);
        accountForm.add(numberOfTheCertificate);
        accountForm.add(dateOfTheCertificate);

        accountForm.setWidth("575px");
        return accountForm;
    }

    //Получение универсального поля
    private HorizontalLayout getField(Binder<?> binder, TextField textField, String bind, String label) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        textField.setWidth(FIELD_WIDTH);
        binder.forField(textField)
                .asRequired("Не заполнено!")
                .bind(bind);
        Label labelInside = new Label(label);
        labelInside.setWidth(LABEL_WIDTH);
        horizontalLayout.add(labelInside, textField);
        horizontalLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, labelInside);
        return horizontalLayout;
    }

    //Получение вложеного универсального поля
    private HorizontalLayout getSmallField(TextArea textArea, String label) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        textArea.setWidth(FIELD_WIDTH);

        Label labelInside = new Label(label);
        labelInside.setWidth(LABEL_WIDTH);
        horizontalLayout.add(labelInside, textArea);
        horizontalLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, labelInside);
        horizontalLayout.setHeight("30%");
        horizontalLayout.setSpacing(false);
        horizontalLayout.setMargin(false);
        return horizontalLayout;
    }

    //Получение универсальной textArea
    private HorizontalLayout getArea(Binder<?> binder, TextArea textArea, String bind, String labelFrom) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        textArea.setWidth(FIELD_WIDTH);
        binder.forField(textArea)
                .asRequired("Не заполнено!")
                .bind(bind);
        Label label = new Label(labelFrom);
        label.setWidth(LABEL_WIDTH);
        if ("address".equals(bind)) {
            horizontalLayout.add(label, textArea, dropDownAddressButton(block));
        } else {
            horizontalLayout.add(label, textArea);
        }
        horizontalLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, label);
        return horizontalLayout;
    }

    //Получение универсального цифрового поля
    private HorizontalLayout getNumberField(Binder<?> binder, ValidTextField validTextField, String bind, String labelFrom) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label(labelFrom);
        label.setWidth(LABEL_WIDTH);
        validTextField.setWidth(FIELD_WIDTH);
        binder.forField(validTextField)
                .asRequired("Не заполнено!")
                .bind(bind);
        validTextField.addInputListener(inputEvent ->
                validTextField.addValidator(new RegexpValidator("Only 10 or 12 digits.",
                        "^([0-9]{10}|[0-9]{12})$")));
        horizontalLayout.add(label, validTextField);
        horizontalLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, label);
        return horizontalLayout;
    }

    //Получени select-ора "Тип контрагента" в "Реквизитах"
    private HorizontalLayout typeOfContractorSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        typeOfContractorDtoList = typeOfContractorService.getAll();
        if (typeOfContractorDtoList != null) {
            List<TypeOfContractorDto> list = typeOfContractorDtoList;
            typeOfContractorDtoSelect.setItems(list);
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

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        nameField.setWidth("445px");
        contractorDtoBinder.forField(nameField)
                .asRequired("Не заполнено!")
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
        horizontalLayout.add(label, typeOfPriceDtoSelect);
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

        horizontalLayout.add(contractorsAccordionCreate());

        return horizontalLayout;
    }

    private HorizontalLayout configurePhoneField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        phoneField.setWidth(FIELD_WIDTH);
        contractorDtoBinder.forField(phoneField)
                .asRequired("Не заполнено!")
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
                .asRequired("Не заполнено!")
                .bind("fax");
        horizontalLayout.add(label, faxField);
        return horizontalLayout;
    }

    private HorizontalLayout configureEmailField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Емейл");
        label.setWidth(LABEL_WIDTH);
        contractorDtoBinder.forField(emailField)
                .asRequired("Не заполнено!")
                .bind("email");
        emailField.setWidth("345px");
        Button emailButton = new Button();
        emailButton.setIcon(new Icon(VaadinIcon.ENVELOPE));
        horizontalLayout.add(label, emailField, emailButton);
        return horizontalLayout;
    }

    private HorizontalLayout configureAddressField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Фактический адрес");
        label.setWidth(LABEL_WIDTH);
        addressField.setWidth("345px");
        horizontalLayout.add(label, addressField, dropDownAddressButton(block));
        return horizontalLayout;
    }

    private HorizontalLayout configureAddressFieldLegalDetail() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Юридический адрес");
        label.setWidth(LABEL_WIDTH);
        addressLegalDetailField.setWidth("345px");
        horizontalLayout.add(label, addressLegalDetailField, dropDownAddressButton(blockLegalDetail));
        horizontalLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, label);
        return horizontalLayout;
    }

    private HorizontalLayout configureAddressBlock() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<FiasModelDto> addressByLevel1 = contractorService.getAllAddressByLevel("1");
        var listRegion = addressByLevel1.stream()
                .map(x -> (x.getFormalname() + " " + x.getShortname() + "."))
                .collect(Collectors.toList());
        FormLayout addressForm = new FormLayout();
        Style addressFormStyle = addressForm.getStyle();
        addressFormStyle.set("width", "385px");
        addressFormStyle.set("margin-left", "132px");
        addressFormStyle.set("padding-left", "10px");

        addressForm.addFormItem(addressIndex, "Индекс");
        addressDtoBinder.forField(addressIndex).bind(AddressDto::getIndex, AddressDto::setIndex);

        addressForm.addFormItem(addressCountry, "Страна");
        addressDtoBinder.forField(addressCountry).bind(AddressDto::getCountry, AddressDto::setCountry);

        addressForm.addFormItem(addressRegion, "Область");
        addressRegion.setItems(listRegion);
        addressDtoBinder.forField(addressRegion).bind(AddressDto::getRegion, AddressDto::setRegion);

        addressForm.addFormItem(addressCity, "Город");
        addressDtoBinder.forField(addressCity).bind(AddressDto::getCity, AddressDto::setCity);
        addressCity.setItems(List.of(""));

        addressForm.addFormItem(addressStreet, "Улица");
        addressDtoBinder.forField(addressStreet).bind(AddressDto::getStreet, AddressDto::setStreet);
        addressStreet.setItems(List.of(""));

        addressForm.addFormItem(addressHouse, "Дом");
        addressDtoBinder.forField(addressHouse).bind(AddressDto::getHouse, AddressDto::setHouse);

        addressForm.addFormItem(addressApartment, "Квартира");
        addressDtoBinder.forField(addressApartment).bind(AddressDto::getApartment, AddressDto::setApartment);

        if (contractorDto.getId() != null) {
            addressIndex.setValue(contractorDto.getAddressDto().getIndex());
            addressCountry.setValue(contractorDto.getAddressDto().getCountry());
            addressRegion.setPlaceholder(contractorDto.getAddressDto().getRegion());
            addressCity.setPlaceholder(contractorDto.getAddressDto().getCity());
            addressStreet.setPlaceholder(contractorDto.getAddressDto().getStreet());
            addressHouse.setValue(contractorDto.getAddressDto().getHouse());
            addressApartment.setValue(contractorDto.getAddressDto().getApartment());
        }
        AtomicReference<List<FiasModelDto>> citiesByRegion = new AtomicReference<>();
        addressRegion.addValueChangeListener(event -> {
            String regionValue = addressRegion.getValue();
            if (regionValue != null) {
                String aoguid = addressByLevel1.stream()
                        .filter(x -> (x.getFormalname() + " " + x.getShortname() + ".").equals(regionValue))
                        .findFirst()
                        .get()
                        .getAoguid();
                citiesByRegion.set(contractorService.getAddressesByGuid(aoguid));
                addressCity.setItems(contractorService.getAddressesByGuid(aoguid).stream()
                        .map(x -> (x.getShortname() + " " + x.getFormalname())));
                System.out.println(aoguid);
            }
        });
        addressCity.addValueChangeListener(event -> {
            String cityValue = addressCity.getValue();
            if (cityValue != null) {
                String aoguid = citiesByRegion.get().get(0).getAoguid();
                addressStreet.setItems(contractorService.getAddressesByGuid(aoguid).stream().map(x -> (x.getShortname() + " " + x.getFormalname())));
                System.out.println(aoguid);
            }
        });
        horizontalLayout.add(addressForm);
        horizontalLayout.setVisible(false);
        return horizontalLayout;
    }

    private HorizontalLayout configureAddressBlockLegalDetail() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        FormLayout addressForm = new FormLayout();
        Style addressFormStyle = addressForm.getStyle();
        addressFormStyle.set("width", "385px");
        addressFormStyle.set("margin-left", "132px");
        addressFormStyle.set("padding-left", "10px");

        addressForm.addFormItem(addressIndexLegalDetail, "Индекс");
        addressDtoBinder.forField(addressIndexLegalDetail).bind(AddressDto::getIndex, AddressDto::setIndex);

        addressForm.addFormItem(addressCountryLegalDetail, "Страна");
        addressDtoBinder.forField(addressCountry).bind(AddressDto::getCountry, AddressDto::setCountry);

        addressForm.addFormItem(addressRegionLegalDetail, "Область");
        addressDtoBinder.forField(addressRegionLegalDetail).bind(AddressDto::getRegion, AddressDto::setRegion);

        addressForm.addFormItem(addressCityLegalDetail, "Город");
        addressDtoBinder.forField(addressCityLegalDetail).bind(AddressDto::getCity, AddressDto::setCity);

        addressForm.addFormItem(addressStreetLegalDetail, "Улица");
        addressDtoBinder.forField(addressStreetLegalDetail).bind(AddressDto::getStreet, AddressDto::setStreet);

        addressForm.addFormItem(addressHouseLegalDetail, "Дом");
        addressDtoBinder.forField(addressHouseLegalDetail).bind(AddressDto::getHouse, AddressDto::setHouse);

        addressForm.addFormItem(addressApartmentLegalDetail, "Квартира");
        addressDtoBinder.forField(addressApartmentLegalDetail).bind(AddressDto::getApartment, AddressDto::setApartment);
        if (contractorDto.getId() != null) {
            addressIndexLegalDetail.setPlaceholder("Индекс");
            addressIndexLegalDetail.setValue(contractorDto.getLegalDetailDto().getAddressDto().getIndex());
            addressCountryLegalDetail.setPlaceholder("Страна");
            addressCountryLegalDetail.setValue(contractorDto.getLegalDetailDto().getAddressDto().getCountry());
            addressRegionLegalDetail.setPlaceholder("Область");
            addressRegionLegalDetail.setValue(contractorDto.getLegalDetailDto().getAddressDto().getRegion());
            addressCityLegalDetail.setPlaceholder("Город");
            addressCityLegalDetail.setValue(contractorDto.getLegalDetailDto().getAddressDto().getCity());
            addressStreetLegalDetail.setPlaceholder("Улица");
            addressStreetLegalDetail.setValue(contractorDto.getLegalDetailDto().getAddressDto().getStreet());
            addressHouseLegalDetail.setPlaceholder("Номер дома");
            addressHouseLegalDetail.setValue(contractorDto.getLegalDetailDto().getAddressDto().getHouse());
            addressApartmentLegalDetail.setPlaceholder("Номер квартиры");
            addressApartmentLegalDetail.setValue(contractorDto.getLegalDetailDto().getAddressDto().getApartment());
        }
        horizontalLayout.add(addressForm);
        horizontalLayout.setVisible(false);
        return horizontalLayout;
    }

    private Button dropDownAddressButton(HorizontalLayout layout) {
        Button button = new Button();
        button.setIcon(new Icon(VaadinIcon.CARET_DOWN));
        button.addClickListener(e -> layout.setVisible(!layout.isVisible()));
        return button;
    }

    private HorizontalLayout configureCommentToAddressField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Комментарий к адресу");
        label.setWidth(LABEL_WIDTH);
        commentToAddressField.setWidth(FIELD_WIDTH);
        contractorDtoBinder.forField(commentToAddressField)
                .asRequired("Не заполнено!")
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
                .asRequired("Не заполнено!")
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
                .asRequired("Не заполнено!")
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
                saveFieldsCreate(legalDetailDto);
//                legalDetailService.create(legalDetailDto);
                saveFields(contractorDto);
                contractorService.create(contractorDto);
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
        if (contractorDto.getLegalDetailDto() != null) {
            Long addressId = contractorDto.getLegalDetailDto().getAddressDto().getId();
            legalDetailDto.setAddressDto(AddressDto.builder()
                    .id(addressId)
                    .index(addressIndexLegalDetail.getValue())
                    .country(addressCountryLegalDetail.getValue())
                    .region(addressRegionLegalDetail.getValue())
                    .city(addressCityLegalDetail.getValue())
                    .street(addressStreetLegalDetail.getValue())
                    .house(addressHouseLegalDetail.getValue())
                    .apartment(addressApartmentLegalDetail.getValue())
                    .build());
        } else {
            legalDetailDto.setAddressDto(AddressDto.builder()
                    .index(addressIndexLegalDetail.getValue())
                    .country(addressCountryLegalDetail.getValue())
                    .region(addressRegionLegalDetail.getValue())
                    .city(addressCityLegalDetail.getValue())
                    .street(addressStreetLegalDetail.getValue())
                    .house(addressHouseLegalDetail.getValue())
                    .apartment(addressApartmentLegalDetail.getValue())
                    .build());
        }
        legalDetailDto.setCommentToAddress(commentToAddressLegalDetailField.getValue());
        legalDetailDto.setInn(innLegalDetailField.getValue());
        legalDetailDto.setOkpo(okpoLegalDetailField.getValue());
        legalDetailDto.setOgrnip(ogrnipLegalDetailField.getValue());
        legalDetailDto.setNumberOfTheCertificate(numberOfTheCertificateLegalDetailField.getValue());
        legalDetailDto.setDateOfTheCertificate(dateOfTheCertificateLegalDetailField.getValue());

        legalDetailDto.setTypeOfContractorDto(typeOfContractorDtoSelect.getValue());

        return legalDetailDto;
    }

    private void saveFields(ContractorDto contractorDto) {
        contractorDto.setName(nameField.getValue());
        contractorDto.setInn(innField.getValue());
        contractorDto.setPhone(phoneField.getValue());
        contractorDto.setFax(faxField.getValue());
        contractorDto.setEmail(emailField.getValue());
        contractorDto.setAddressDto(AddressDto.builder()
                .another(addressField.getValue())
                .build());
        contractorDto.setCommentToAddress(commentToAddressField.getValue());
        contractorDto.setComment(commentField.getValue());
        contractorDto.setSortNumber(sortNumberField.getValue());
        List<ContactDto> newContactDtoList = new ArrayList<>();
        if (contractorDto.getId() != null) {
            Long addressId = contractorDto.getAddressDto().getId();
            if (addressRegion.getValue() == null) {
                addressRegion.setValue(contractorDto.getAddressDto().getRegion());
            }
            if (addressCity.getValue() == null) {
                addressCity.setValue(contractorDto.getAddressDto().getCity());
            }
            if (addressStreet.getValue() == null) {
                addressStreet.setValue(contractorDto.getAddressDto().getStreet());
            }
            contractorDto.setAddressDto(AddressDto.builder()
                    .id(addressId)
                    .index(addressIndex.getValue())
                    .country(addressCountry.getValue())
                    .region(addressRegion.getValue())
                    .city(addressCity.getValue())
                    .street(addressStreet.getValue())
                    .house(addressHouse.getValue())
                    .apartment(addressApartment.getValue())
                    .build());
            contractorDto.getLegalDetailDto().setId(legalDetailDtoSelect.getValue().getId());

            contractorDto.getContactDto().forEach(contact -> {
                Long contactId = contact.getId();

                newContactDtoList.add(ContactDto.builder().id(contactId)
                        .fullName(existContactTextFields.get(contactId).get(0).getValue())
                        .position(existContactTextFields.get(contactId).get(1).getValue())
                        .phone(existContactTextFields.get(contactId).get(2).getValue())
                        .email(existContactTextFields.get(contactId).get(3).getValue())
                        .comment(existContactTextFields.get(contactId).get(4).getValue())
                        .build());
            });
            newContactTextFields.forEach(contact -> {
                newContactDtoList.add(ContactDto.builder().fullName(contact.get(0).getValue())
                        .position(contact.get(1).getValue())
                        .phone(contact.get(2).getValue())
                        .email(contact.get(3).getValue())
                        .comment(contact.get(4).getValue())
                        .build());
            });
            contractorDto.setContactDto(newContactDtoList);

            contractorDto.getContractorGroupDto().setId(contractorGroupDtoSelect.getValue().getId());
            contractorDto.getTypeOfContractorDto().setId(typeOfContractorDtoSelect.getValue().getId());
            contractorDto.getTypeOfPriceDto().setId(typeOfPriceDtoSelect.getValue().getId());
        } else {
            contractorDto.setAddressDto(AddressDto.builder()
                    .index(addressIndex.getValue())
                    .country(addressCountry.getValue())
                    .region(addressRegion.getValue())
                    .city(addressCity.getValue())
                    .street(addressStreet.getValue())
                    .house(addressHouse.getValue())
                    .apartment(addressApartment.getValue())
                    .build());

            newContactTextFields.forEach(contact -> {
                newContactDtoList.add(ContactDto.builder().fullName(contact.get(0).getValue())
                        .position(contact.get(1).getValue())
                        .phone(contact.get(2).getValue())
                        .email(contact.get(3).getValue())
                        .comment(contact.get(4).getValue())
                        .build());
            });
            contractorDto.setContactDto(newContactDtoList);

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
