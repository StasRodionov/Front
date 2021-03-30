package com.trade_accounting.components.contractors;

import com.trade_accounting.components.util.ValidTextField;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.ContractorGroupDto;
import com.trade_accounting.services.interfaces.ContractorGroupService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.validator.RegexpValidator;

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

    private final Select<ContractorGroupDto> contractorGroupDtoSelect = new Select<>();

    private final String labelWidth = "100px";

    private final String fieldWidth = "400px";

    private final ContractorService contractorService;
    private final ContractorGroupService contractorGroupService;

    public ContractorModalWindow(ContractorDto contractorDto,
                                 ContractorService contractorService,
                                 ContractorGroupService contractorGroupService) {
        this.contractorService = contractorService;
        this.contractorGroupService = contractorGroupService;

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
                configureCommentField());
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
        Label label = new Label("Группы");
        contractorGroupDtoSelect.setItems(contractorGroupService.getAll());
        contractorGroupDtoSelect.setItemLabelGenerator(ContractorGroupDto::getName);
        contractorGroupDtoSelect.setWidth(fieldWidth);
        label.setWidth(labelWidth);
        horizontalLayout.add(label, contractorGroupDtoSelect);
        return horizontalLayout;
    }

    private HorizontalLayout configureInnField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Инн");
        label.setWidth(labelWidth);
        innField.setWidth(fieldWidth);
        innField.addInputListener(inputEvent ->
                innField.addValidator(new RegexpValidator("Only 10 or 12 digits.",
                        "^([0-9]{10}|[0-9]{12})$")));
        horizontalLayout.add(label, innField);
        return horizontalLayout;
    }

    private HorizontalLayout configureSortNumberField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Код");
        label.setWidth(labelWidth);
        sortNumberField.setWidth(fieldWidth);
        horizontalLayout.add(label, sortNumberField);
        return horizontalLayout;
    }

    private HorizontalLayout configurePhoneField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Телефон");
        label.setWidth(labelWidth);
        phoneField.setWidth(fieldWidth);
        horizontalLayout.add(label, phoneField);
        return horizontalLayout;
    }

    private HorizontalLayout configureFaxField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Факс");
        label.setWidth(labelWidth);
        faxField.setWidth(fieldWidth);
        horizontalLayout.add(label, faxField);
        return horizontalLayout;
    }

    private HorizontalLayout configureEmailField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Емейл");
        label.setWidth(labelWidth);
        emailField.setWidth("345px");
        Button emailButton = new Button();
        emailButton.setIcon(new Icon(VaadinIcon.ENVELOPE));
        horizontalLayout.add(label, emailField, emailButton);
        return horizontalLayout;
    }

    private HorizontalLayout configureAddressField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Фактический адресс");
        label.setWidth(labelWidth);
        addressField.setWidth("345px");
        Button emailButton = new Button();
        emailButton.setIcon(new Icon(VaadinIcon.CARET_DOWN));
        horizontalLayout.add(label, addressField, emailButton);
        return horizontalLayout;
    }

    private HorizontalLayout configureCommentToAddressField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Комментарий к адресу");
        label.setWidth(labelWidth);
        commentToAddressField.setWidth(fieldWidth);
        commentToAddressField.getStyle().set("minHeight", "120px");
        horizontalLayout.add(label, commentToAddressField);
        return horizontalLayout;
    }

    private HorizontalLayout configureCommentField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Комментарий");
        label.setWidth(labelWidth);
        commentField.setWidth(fieldWidth);
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
        //добавил
        contractorDto.setContractorGroupId(contractorGroupDtoSelect.getValue().getId());

    }

    private Button getCancelButton() {
//        Button cancelButton = new Button("Закрыть", event -> { close(); });
//        return cancelButton;
        return new Button("Закрыть", event -> close());
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }

}
