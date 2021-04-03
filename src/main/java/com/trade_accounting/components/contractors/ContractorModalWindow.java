package com.trade_accounting.components.contractors;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.trade_accounting.components.util.ValidTextField;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.ContractorGroupDto;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.LegalDetailDto;
import com.trade_accounting.models.dto.TypeOfContractorDto;
import com.trade_accounting.models.dto.TypeOfPriceDto;
import com.trade_accounting.services.interfaces.ContractorGroupService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.LegalDetailService;
import com.trade_accounting.services.interfaces.TypeOfContractorService;
import com.trade_accounting.services.interfaces.TypeOfPriceService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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


    private final String LABEL_WIDTH = "100px";

    private final String FIELD_WIDTH = "400px";

    private final ContractorService contractorService;
    private final ContractorGroupService contractorGroupService;
    private final TypeOfContractorService typeOfContractorService;
    private final TypeOfPriceService typeOfPriceService;
    private final LegalDetailService legalDetailService;



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
            legalDetailDtoSelect.setValue(contractorService
                    .getById(contractorDto.getId()).getLegalDetailDto());
        }

    }

    private Accordion contractorsAccordion() {
        Accordion accordion = new Accordion();
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(contractorGroupSelect(),
                configureInnField(),
                configureSortNumberField(),
                configurePhoneField(),
                configureFaxField(),
                configureEmailField(),
                configureAddressField(),
                configureCommentToAddressField(),
                configureCommentField(),
                typeOfContractorSelect(),
                typeOfPriceSelect(),
                LegalDetailSelect()
        );

        accordion.add("О контрагенте", verticalLayout).addThemeVariants(DetailsVariant.FILLED);
        accordion.add("Контактные лица", new VerticalLayout()).addThemeVariants(DetailsVariant.FILLED);
        accordion.add("Реквизиты", new VerticalLayout()).addThemeVariants(DetailsVariant.FILLED);
        accordion.add("Скидки", new VerticalLayout()).addThemeVariants(DetailsVariant.FILLED);
        accordion.add("Доступы", new VerticalLayout()).addThemeVariants(DetailsVariant.FILLED);
        accordion.setWidth("575px");
        return accordion;
    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        nameField.setWidth("345px");
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
        Label label = new Label("Группа контракта");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, contractorGroupDtoSelect);
        return horizontalLayout;

    }

    private HorizontalLayout typeOfContractorSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        List<TypeOfContractorDto> typeOfContractorDtoList = typeOfContractorService.getAll();
        if (typeOfContractorDtoList != null) {
            typeOfContractorDtoSelect.setItems(typeOfContractorDtoList);
        }
        typeOfContractorDtoSelect.setItemLabelGenerator(TypeOfContractorDto::getName);
        typeOfContractorDtoSelect.setWidth(FIELD_WIDTH);
        contractorDtoBinder.forField(typeOfContractorDtoSelect)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("typeOfContractorDto");
        Label label = new Label("Тип контракта");
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
        Label label = new Label("Тип прайса");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, typeOfPriceDtoSelect);
        return horizontalLayout;
    }

    private HorizontalLayout LegalDetailSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<LegalDetailDto> legalDetailDtoList = legalDetailService.getAll();
        if (legalDetailDtoList != null) {
            legalDetailDtoSelect.setItems(legalDetailDtoList);
        }
        legalDetailDtoSelect.setItemLabelGenerator(LegalDetailDto::getInn);
        legalDetailDtoSelect.setWidth(FIELD_WIDTH);
        contractorDtoBinder.forField(legalDetailDtoSelect)
                .withValidator(Objects::nonNull, "Не заполнено!")
                .bind("legalDetailDto");
        Label label = new Label("Юр. детали");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, legalDetailDtoSelect);
        return horizontalLayout;
    }

    private HorizontalLayout configureInnField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Инн");
        label.setWidth(LABEL_WIDTH);
        innField.setWidth(FIELD_WIDTH);
        innField.addInputListener(inputEvent ->
                innField.addValidator(new RegexpValidator("Only 10 or 12 digits.",
                        "^([0-9]{10}|[0-9]{12})$")));
        horizontalLayout.add(label, innField);
        return horizontalLayout;
    }

    private HorizontalLayout configureSortNumberField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Код");
        label.setWidth(LABEL_WIDTH);
        sortNumberField.setWidth(FIELD_WIDTH);
        horizontalLayout.add(label, sortNumberField);
        return horizontalLayout;
    }

    private HorizontalLayout configurePhoneField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Телефон");
        label.setWidth(LABEL_WIDTH);
        phoneField.setWidth(FIELD_WIDTH);
        horizontalLayout.add(label, phoneField);
        return horizontalLayout;
    }

    private HorizontalLayout configureFaxField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Факс");
        label.setWidth(LABEL_WIDTH);
        faxField.setWidth(FIELD_WIDTH);
        horizontalLayout.add(label, faxField);
        return horizontalLayout;
    }

    private HorizontalLayout configureEmailField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Емейл");
        label.setWidth(LABEL_WIDTH);
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
        commentToAddressField.getStyle().set("minHeight", "120px");
        horizontalLayout.add(label, commentToAddressField);
        return horizontalLayout;
    }

    private HorizontalLayout configureCommentField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Комментарий");
        label.setWidth(LABEL_WIDTH);
        commentField.setWidth(FIELD_WIDTH);
        commentField.getStyle().set("minHeight", "120px");
        horizontalLayout.add(label, commentField);
        return horizontalLayout;
    }

    private Button getSaveButton() {
        if (nameField.isEmpty()) {
            ContractorDto contractorDto = new ContractorDto();
            return new Button("Добавить", event -> {
                saveFields(contractorDto);
                contractorService.create(contractorDto);
                if (!innField.isEmpty() && innField.getValue()
                            .matches("^([0-9]{10}|[0-9]{12})$"))
                    close();
            });
        } else {
            ContractorDto contractorDto = contractorService.getById(Long.valueOf(idField.getValue()));
            return new Button("Изменить", event -> {
                saveFields(contractorDto);
                contractorService.update(contractorDto);
                if (!innField.isEmpty() && innField.getValue()
                        .matches("^([0-9]{10}|[0-9]{12})$")) {
                    close();
                }
            });
        }
    }

    private void saveFields(ContractorDto contractorDto) {
        contractorDto.setName(nameField.getValue());
        contractorDto.setInn(innField.getValue());
        contractorDto.setSortNumber(sortNumberField.getValue());
        contractorDto.setPhone(phoneField.getValue());
        contractorDto.setFax(faxField.getValue());
        contractorDto.setEmail(emailField.getValue());
        contractorDto.setAddress(addressField.getValue());
        contractorDto.setCommentToAddress(commentToAddressField.getValue());
        contractorDto.setComment(commentField.getValue());

        contractorDto.setContractorGroupId(contractorGroupDtoSelect.getValue().getId());
        contractorDto.setTypeOfContractorId(typeOfContractorDtoSelect.getValue().getId());
        contractorDto.setTypeOfPriceId(typeOfPriceDtoSelect.getValue().getId());
        contractorDto.setLegalDetailId(legalDetailDtoSelect.getValue().getId());
    }

    private Button getCancelButton() {
        return new Button("Закрыть", event -> close());
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }

}
