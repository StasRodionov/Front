package com.trade_accounting.components.profile;

import com.trade_accounting.models.dto.AddressDto;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.LegalDetailDto;
import com.trade_accounting.models.dto.TypeOfContractorDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.DetailsVariant;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.dom.Style;

import java.time.LocalDate;

public class CompanyModal extends Dialog {

    private static final String FIELD_WIDTH = "400px";

    private Long companyId;
    private CompanyDto companyDto;
    private final TextField name = new TextField();
    private final TextField inn = new TextField();
    private final TextArea address = new TextArea();
    private final TextArea commentToAddress = new TextArea();
    private final TextField email = new TextField();
    private final TextField phone = new TextField();
    private final TextField fax = new TextField();
    private final TextField leader = new TextField();
    private final TextField leaderManagerPosition = new TextField();
    private final TextField leaderSignature = new TextField();
    private final TextField chiefAccountant = new TextField();
    private final TextField chiefAccountantSignature = new TextField();
    private final Select<String> payerVat = new Select<>();
    private final TextField sortNumber = new TextField();
    private final TextField stamp = new TextField();

    private Long legalDetailId;
    private LegalDetailDto legalDetailDto;
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

    private final TextField addressIndex = new TextField();
    private final TextField addressCountry = new TextField();
    public final ComboBox<String> addressRegion = new ComboBox<>();
    private final ComboBox<String> addressCity = new ComboBox<>();
    private final ComboBox<String> addressStreet = new ComboBox<>();
    private final TextField addressHouse = new TextField();
    private final TextField addressApartment = new TextField();
   //private final Binder<AddressDto> addressDtoBinder = new Binder<>(AddressDto.class);

    private final TextArea addressLegalDetailField = new TextArea();
    private final TextField addressIndexLegalDetail = new TextField();
    private final TextField addressCountryLegalDetail = new TextField();
    private final TextField addressRegionLegalDetail = new TextField();
    private final TextField addressCityLegalDetail = new TextField();
    private final TextField addressStreetLegalDetail = new TextField();
    private final TextField addressHouseLegalDetail = new TextField();
    private final TextField addressApartmentLegalDetail = new TextField();
    //private final HorizontalLayout blockLegalDetail;
    private HorizontalLayout block;

    private Long typeOfContractorId;
    private final TextField typeOfContractorName = new TextField();
    private final TextField typeOfContractorSortNumber = new TextField();

    private final CompanyService companyService;

    public CompanyModal(CompanyService companyService) {
        this.companyService = companyService;
        this.companyDto = new CompanyDto();
        block = configureLegalDetailAddressBlock();
        configureModal("Добавление");
    }

    public CompanyModal(CompanyDto companyDto, CompanyService companyService) {
        this.companyService = companyService;
        this.companyDto = companyDto;
        block = configureLegalDetailAddressBlock();
        configureModal("Редактирование");
        setFields();
    }

    private void configureModal(String title) {
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        setHeightFull();
        add(header(title), accordionCompany());
    }

    private void setFields() {
        companyId = companyDto.getId();
        setField(name, companyDto.getName());
        setField(inn, companyDto.getInn());
        setField(address, companyDto.getAddress());
        setField(commentToAddress, companyDto.getCommentToAddress());
        setField(email, companyDto.getEmail());
        setField(phone, companyDto.getPhone());
        setField(fax, companyDto.getFax());
        setField(leader, companyDto.getLeader());
        setField(leaderManagerPosition, companyDto.getLeaderManagerPosition());
        setField(leaderSignature, companyDto.getLeaderSignature());
        setField(chiefAccountant, companyDto.getChiefAccountant());
        setField(chiefAccountantSignature, companyDto.getChiefAccountantSignature());
        setField(payerVat, Boolean.TRUE.equals(companyDto.getPayerVat()) ? "Да" : "Нет");
        setField(sortNumber, companyDto.getSortNumber());
        setField(stamp, companyDto.getStamp());

        if (companyDto.getLegalDetailDto() != null) {
            legalDetailId = companyDto.getLegalDetailDto().getId();
            setField(legalDetailLastName, companyDto.getLegalDetailDto().getLastName());
            setField(legalDetailFirstName, companyDto.getLegalDetailDto().getFirstName());
            setField(legalDetailMiddleName, companyDto.getLegalDetailDto().getMiddleName());
            if(companyDto.getLegalDetailDto().getAddressDto() != null){
                setField(legalDetailAddress, companyDto.getLegalDetailDto().getAddressDto().getAnother());
            }
            setField(legalDetailCommentToAddress, companyDto.getLegalDetailDto().getCommentToAddress());
            setField(legalDetailInn, companyDto.getLegalDetailDto().getInn());
            setField(legalDetailOkpo, companyDto.getLegalDetailDto().getOkpo());
            setField(legalDetailOgrnip, companyDto.getLegalDetailDto().getOgrn());
            setField(legalDetailNumberOfTheCertificate, companyDto.getLegalDetailDto().getNumberOfTheCertificate());
            setDate(legalDetailDateOfTheCertificate, companyDto.getLegalDetailDto().getDate());

            if (companyDto.getLegalDetailDto().getTypeOfContractorDto() != null) {
                typeOfContractorId = companyDto.getLegalDetailDto().getTypeOfContractorDto().getId();
                setField(typeOfContractorName, companyDto.getLegalDetailDto().getTypeOfContractorDto().getName());
                setField(typeOfContractorSortNumber, companyDto.getLegalDetailDto().getTypeOfContractorDto().getSortNumber());
            }
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

    private Button buttonSave() {
        return new Button("Сохранить", event -> {
            TypeOfContractorDto typeOfContractorDto = new TypeOfContractorDto();
            typeOfContractorDto.setId(typeOfContractorId);
            typeOfContractorDto.setName(typeOfContractorName.getValue());
            typeOfContractorDto.setSortNumber(typeOfContractorSortNumber.getValue());

            LegalDetailDto legalDetailDto = new LegalDetailDto();
            legalDetailDto.setId(legalDetailId);
            legalDetailDto.setLastName(legalDetailLastName.getValue());
            legalDetailDto.setFirstName(legalDetailFirstName.getValue());
            legalDetailDto.setMiddleName(legalDetailMiddleName.getValue());
            legalDetailDto.setAddressDto(getAddressDto());
            legalDetailDto.setCommentToAddress(legalDetailCommentToAddress.getValue());
            legalDetailDto.setInn(legalDetailInn.getValue());
            legalDetailDto.setOkpo(legalDetailOkpo.getValue());
            legalDetailDto.setOgrn(legalDetailOgrnip.getValue());
            legalDetailDto.setNumberOfTheCertificate(legalDetailNumberOfTheCertificate.getValue());
            legalDetailDto.setDate(legalDetailDateOfTheCertificate.getValue() != null
                    ? legalDetailDateOfTheCertificate.getValue().toString() : null);
            legalDetailDto.setTypeOfContractorDto(typeOfContractorDto);

            companyDto.setName(name.getValue());
            companyDto.setInn(inn.getValue());
            companyDto.setAddress(address.getValue());
            companyDto.setCommentToAddress(commentToAddress.getValue());
            companyDto.setEmail(email.getValue());
            companyDto.setPhone(phone.getValue());
            companyDto.setFax(fax.getValue());
            companyDto.setSortNumber(sortNumber.getValue());
            if (payerVat.getValue() != null) {
                companyDto.setPayerVat(payerVat.getValue().equals("Да"));
            }
            companyDto.setStamp(stamp.getValue());
            companyDto.setLeader(leader.getValue());
            companyDto.setLeaderManagerPosition(leaderManagerPosition.getValue());
            companyDto.setLeaderSignature(leaderSignature.getValue());
            companyDto.setChiefAccountant(chiefAccountant.getValue());
            companyDto.setChiefAccountantSignature(chiefAccountantSignature.getValue());
            companyDto.setLegalDetailDto(legalDetailDto);

            if (companyId == null) {
                companyService.create(companyDto);
            } else {
                companyService.update(companyDto);
            }
            close();
        });
    }

    private Button buttonCancel() {
        return new Button("Закрыть", event -> close());
    }

    private Accordion accordionCompany() {
        Accordion accordion = new Accordion();
        accordion.setWidth("575px");

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
                block,
                configureLegalDetailCommentToAddress(),
                configureLegalDetailInn(),
                configureLegalDetailOkpo(),
                configureLegalDetailOgrnip(),
                configureLegalDetailNumberOfTheCertificate(),
                configureLegalDetailDateOfTheCertificate(),
                configureTypeOfContractorName(),
                configureTypeOfContractorSortNumber());
        accordion.add("Юридические детали", layoutDetails).addThemeVariants(DetailsVariant.FILLED);

        return accordion;
    }

    private HorizontalLayout configureName() {
        name.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Наименование", name);
    }

    private HorizontalLayout configureInn() {
        inn.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("ИНН", inn);
    }

    private HorizontalLayout configureAddress() {
        address.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Адрес", address);
    }

    private HorizontalLayout configureLegalDetailAddressBlock() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        FormLayout addressForm = new FormLayout();
        Style addressFormStyle = addressForm.getStyle();
        addressFormStyle.set("width", "385px");
        addressFormStyle.set("margin-left", "132px");
        addressFormStyle.set("padding-left", "10px");

        addressForm.addFormItem(addressIndexLegalDetail, "Индекс");

        addressForm.addFormItem(addressCountryLegalDetail, "Страна");

        addressForm.addFormItem(addressRegionLegalDetail, "Область");

        addressForm.addFormItem(addressCityLegalDetail, "Город");

        addressForm.addFormItem(addressStreetLegalDetail, "Улица");

        addressForm.addFormItem(addressHouseLegalDetail, "Дом");

        addressForm.addFormItem(addressApartmentLegalDetail, "Квартира");

        if (companyDto.getId() != null && companyDto.getLegalDetailDto().getAddressDto() != null) {
            addressIndexLegalDetail.setPlaceholder("Индекс");
            addressIndexLegalDetail.setValue(companyDto.getLegalDetailDto().getAddressDto().getIndex());
            addressCountryLegalDetail.setPlaceholder("Страна");
            addressCountryLegalDetail.setValue(companyDto.getLegalDetailDto().getAddressDto().getCountry());
            addressRegionLegalDetail.setPlaceholder("Область");
            addressRegionLegalDetail.setValue(companyDto.getLegalDetailDto().getAddressDto().getRegion());
            addressCityLegalDetail.setPlaceholder("Город");
            addressCityLegalDetail.setValue(companyDto.getLegalDetailDto().getAddressDto().getCity());
            addressStreetLegalDetail.setPlaceholder("Улица");
            addressStreetLegalDetail.setValue(companyDto.getLegalDetailDto().getAddressDto().getStreet());
            addressHouseLegalDetail.setPlaceholder("Номер дома");
            addressHouseLegalDetail.setValue(companyDto.getLegalDetailDto().getAddressDto().getHouse());
            addressApartmentLegalDetail.setPlaceholder("Номер квартиры");
            addressApartmentLegalDetail.setValue(companyDto.getLegalDetailDto().getAddressDto().getApartment());
        }
        horizontalLayout.add(addressForm);
        horizontalLayout.setVisible(false);
        return horizontalLayout;
    }

    private HorizontalLayout configureCommentToAddress() {
        commentToAddress.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Комментарий к адресу", commentToAddress);
    }

    private HorizontalLayout configureEmail() {
        email.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("E-mail", email);
    }

    private HorizontalLayout configurePhone() {
        phone.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Телефон", phone);
    }

    private HorizontalLayout configureFax() {
        fax.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Факс", fax);
    }

    private HorizontalLayout configureSortNumber() {
        sortNumber.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Нумерация", sortNumber);
    }

    private HorizontalLayout configurePayerVat() {
        payerVat.setWidth(FIELD_WIDTH);
        payerVat.setItems("Да", "Нет");
        return getHorizontalLayout("Плательщик НДС", payerVat);
    }

    private HorizontalLayout configureStamp() {
        stamp.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Печать", stamp);
    }

    private HorizontalLayout configureLeader() {
        leader.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Руководитель", leader);
    }

    private HorizontalLayout configureLeaderManagerPosition() {
        leaderManagerPosition.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Должность руководителя", leaderManagerPosition);
    }

    private HorizontalLayout configureLeaderSignature() {
        leaderSignature.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Подпись руководителя", leaderSignature);
    }

    private HorizontalLayout configureChiefAccountant() {
        chiefAccountant.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Главный бухгалтер", chiefAccountant);
    }

    private HorizontalLayout configureChiefAccountantSignature() {
        chiefAccountantSignature.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Подпись гл. бухгалтера", chiefAccountantSignature);
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
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Юридический адрес");
        addressLegalDetailField.setWidth("345px");
        horizontalLayout.add(label, addressLegalDetailField, dropDownAddressButton(block));
        horizontalLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, label);
        return horizontalLayout;
    }

    private Button dropDownAddressButton(HorizontalLayout layout) {
        Button button = new Button();
        button.setIcon(new Icon(VaadinIcon.CARET_DOWN));
        button.addClickListener(e -> layout.setVisible(!layout.isVisible()));
        return button;
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

    private void setField(AbstractField field, String value) {
        if (value != null) {
            field.setValue(value);
        }
    }

    private void setDate(AbstractField field, LocalDate date) {
        if (date != null) {
            field.setValue(date);
        }
    }

    private AddressDto getAddressDto(){
        AddressDto addressDto = new AddressDto();
        addressDto.setCountry(addressCountry.getValue());
        addressDto.setRegion(addressRegion.getValue());
        addressDto.setCity(addressCity.getValue());
        addressDto.setStreet(addressStreet.getValue());
        addressDto.setHouse(addressHouse.getValue());
        addressDto.setApartment(addressApartment.getValue());
        return addressDto;
    }
}
