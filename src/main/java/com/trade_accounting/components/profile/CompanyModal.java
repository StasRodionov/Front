package com.trade_accounting.components.profile;

import com.trade_accounting.models.dto.AddressDto;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.LegalDetailDto;
import com.trade_accounting.models.dto.TypeOfContractorDto;
import com.trade_accounting.services.interfaces.AddressService;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.LegalDetailService;
import com.trade_accounting.services.interfaces.TypeOfContractorService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompanyModal extends Dialog {

    private static final String FIELD_WIDTH = "400px";

    private Long companyId;
    private final TextField name = new TextField();
    private final TextField inn = new TextField();
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

    private Long addressId;
    private final TextField addressIndex = new TextField();
    private final TextField addressCountry = new TextField();
    private final TextField addressRegion = new TextField();
    private final TextField addressCity = new TextField();
    private final TextField addressStreet = new TextField();
    private final TextField addressHouse = new TextField();
    private final TextField addressApartment = new TextField();
    private final TextField addressAnother = new TextField();

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

    private final CompanyService companyService;
    private final AddressService addressService;
    private final LegalDetailService legalDetailService;
    private final TypeOfContractorService typeOfContractorService;

    public CompanyModal(CompanyService companyService, AddressService addressService, LegalDetailService legalDetailService, TypeOfContractorService typeOfContractorService) {
        this.companyService = companyService;
        this.addressService = addressService;
        this.legalDetailService = legalDetailService;
        this.typeOfContractorService = typeOfContractorService;
        configureModal("Добавление");
    }

    public CompanyModal(CompanyDto companyDto, CompanyService companyService, AddressService addressService, LegalDetailService legalDetailService, TypeOfContractorService typeOfContractorService) {
        this.companyService = companyService;
        this.addressService = addressService;
        this.legalDetailService = legalDetailService;
        this.typeOfContractorService = typeOfContractorService;
        configureModal("Редактирование");
        setFields(companyDto);
    }

    private void configureModal(String title) {
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        add(header(title), accordionCompany());
    }

    private void setFields(CompanyDto dto) {
        companyId = dto.getId();
        setField(name, dto.getName());
        setField(inn, dto.getInn());
        setField(commentToAddress, dto.getCommentToAddress());
        setField(email, dto.getEmail());
        setField(phone, dto.getPhone());
        setField(fax, dto.getFax());
        setField(leader, dto.getLeader());
        setField(leaderManagerPosition, dto.getLeaderManagerPosition());
        setField(leaderSignature, dto.getLeaderSignature());
        setField(chiefAccountant, dto.getChiefAccountant());
        setField(chiefAccountantSignature, dto.getChiefAccountantSignature());
        setField(payerVat, Boolean.TRUE.equals(dto.getPayerVat()) ? "Да" : "Нет");
        setField(sortNumber, dto.getSortNumber());
        setField(stamp, dto.getStamp());

        if (dto.getAddressId() != null) {
            addressId = dto.getAddressId();
            AddressDto addressDto = addressService.getById(addressId);
            setField(addressIndex, addressDto.getIndex());
            setField(addressCountry, addressDto.getCountry());
            setField(addressRegion, addressDto.getRegion());
            setField(addressCity, addressDto.getCity());
            setField(addressStreet, addressDto.getStreet());
            setField(addressHouse, addressDto.getHouse());
            setField(addressApartment, addressDto.getApartment());
            setField(addressAnother, addressDto.getAnother());
        }

        LegalDetailDto legalDetailDto = legalDetailService.getById(dto.getLegalDetailDtoId());
        if (legalDetailDto != null) {
            legalDetailId = legalDetailDto.getId();
            setField(legalDetailLastName, legalDetailDto.getLastName());
            setField(legalDetailFirstName, legalDetailDto.getFirstName());
            setField(legalDetailMiddleName, legalDetailDto.getMiddleName());
            //setField(legalDetailAddress, dto.getLegalDetailDto().getAddressDto().getAnother());
            setField(legalDetailCommentToAddress, legalDetailDto.getCommentToAddress());
            setField(legalDetailInn, legalDetailDto.getInn());
            setField(legalDetailOkpo, legalDetailDto.getOkpo());
            setField(legalDetailOgrnip, legalDetailDto.getOgrn());
            setField(legalDetailNumberOfTheCertificate, legalDetailDto.getNumberOfTheCertificate());
            setDate(legalDetailDateOfTheCertificate, legalDetailDto.getDate());

            typeOfContractorId = legalDetailDto.getTypeOfContractorDtoId();
            if (typeOfContractorId != null) {
                TypeOfContractorDto typeOfContractorDto = typeOfContractorService.getById(typeOfContractorId);
                setField(typeOfContractorName, typeOfContractorDto.getName());
                setField(typeOfContractorSortNumber, typeOfContractorDto.getSortNumber());
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
            if (typeOfContractorId == null) {
                // TODO выпадающий список
                //  typeOfContractorId = typeOfContractorService.create(typeOfContractorDto).getId();
            } else {
                typeOfContractorService.update(typeOfContractorDto);
            }

            AddressDto addressDto = new AddressDto();
            addressDto.setIndex(addressIndex.getValue());
            addressDto.setCountry(addressCountry.getValue());
            addressDto.setRegion(addressRegion.getValue());
            addressDto.setCity(addressCity.getValue());
            addressDto.setStreet(addressStreet.getValue());
            addressDto.setHouse(addressHouse.getValue());
            addressDto.setApartment(addressApartment.getValue());
            addressDto.setAnother(addressAnother.getValue());
            if (addressId == null) {
                addressId = addressService.create(addressDto).getId();
            } else {
                addressService.update(addressDto);
            }


            LegalDetailDto legalDetailDto = new LegalDetailDto();
            legalDetailDto.setId(legalDetailId);
            legalDetailDto.setLastName(legalDetailLastName.getValue());
            legalDetailDto.setFirstName(legalDetailFirstName.getValue());
            legalDetailDto.setMiddleName(legalDetailMiddleName.getValue());
            //TODO сейчас адрес тот же что и у компании, реализовать галочку адрес совпадает, либо ввод нового
            legalDetailDto.setAddressDtoId(addressId);
            legalDetailDto.setCommentToAddress(legalDetailCommentToAddress.getValue());
            legalDetailDto.setInn(legalDetailInn.getValue());
            legalDetailDto.setOkpo(legalDetailOkpo.getValue());
            legalDetailDto.setOgrn(legalDetailOgrnip.getValue());
            legalDetailDto.setNumberOfTheCertificate(legalDetailNumberOfTheCertificate.getValue());
            legalDetailDto.setDate(legalDetailDateOfTheCertificate.getValue() != null
                    ? legalDetailDateOfTheCertificate.getValue().toString() : null);
          //TODO реализовать выбор typeOfContractor, передать в legalDetail
            legalDetailDto.setTypeOfContractorDtoId(1L);
            if (legalDetailId == null) {
                legalDetailId = legalDetailService.create(legalDetailDto).getId();
            } else {
                legalDetailService.update(legalDetailDto);
            }

            CompanyDto companyDto = new CompanyDto();
            companyDto.setId(companyId);
            companyDto.setName(name.getValue());
            companyDto.setInn(inn.getValue());
            companyDto.setAddressId(addressId);
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
            companyDto.setLegalDetailDtoId(legalDetailId);
            companyDto.setBankAccountDtoIds(Stream.of(1L).collect(Collectors.toList()));
            System.out.println(companyDto);
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
                configureCommentToAddress(),
                configureEmail(),
                configurePhone(),
                configureFax(),
                configureSortNumber(),
                configurePayerVat(),
                configureStamp());

        VerticalLayout address = new VerticalLayout();
        address.add(
                configureAddressIndex(),
                configureAddressCountry(),
                configureAddressRegion(),
                configureAddressCity(),
                configureAddressStreet(),
                configureAddressHouse(),
                configureAddressApartment(),
                configureAddressAnother());
        accordion.add("Адрес", address).addThemeVariants(DetailsVariant.FILLED);

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

    private HorizontalLayout configureAddressIndex() {
        addressIndex.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Индекс", addressIndex);
    }

    private HorizontalLayout configureAddressCountry() {
        addressCountry.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Страна", addressCountry);
    }

    private HorizontalLayout configureAddressRegion() {
        addressRegion.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Регион", addressRegion);
    }

    private HorizontalLayout configureAddressCity() {
        addressCity.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Город", addressCity);
    }

    private HorizontalLayout configureAddressStreet() {
        addressStreet.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Улица", addressStreet);
    }

    private HorizontalLayout configureAddressHouse() {
        addressHouse.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Номер дома", addressHouse);
    }

    private HorizontalLayout configureAddressApartment() {
        addressApartment.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Номер квартиры", addressApartment);
    }

    private HorizontalLayout configureAddressAnother() {
        addressAnother.setWidth(FIELD_WIDTH);
        return getHorizontalLayout("Примечание", addressAnother);
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
}
