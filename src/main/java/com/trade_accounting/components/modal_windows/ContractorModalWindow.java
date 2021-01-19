package com.trade_accounting.components.modal_windows;

import com.trade_accounting.models.dto.ContractorDto;
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


public class ContractorModalWindow extends Dialog {

    private final TextField nameField = new TextField();

    private final TextField phoneField = new TextField();

    private final TextField faxField = new TextField();

    private final TextField emailField = new TextField();

    private final TextArea addressField = new TextArea();

    private final TextArea commentToAddressField = new TextArea();

    private final TextArea commentField = new TextArea();

    private final TextField innField = new TextField();

    private final TextField sortNumberField = new TextField();

    private final String labelWidth = "100px";

    private final String fieldWidth = "400px";

    private final ContractorService contractorService;

    public ContractorModalWindow (ContractorDto contractorDto, ContractorService contractorService) {
        this.contractorService = contractorService;
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        nameField.setValue(getFieldValueNotNull(contractorDto.getName()));
        phoneField.setValue(getFieldValueNotNull(contractorDto.getPhone()));
        faxField.setValue(getFieldValueNotNull(contractorDto.getFax()));
        emailField.setValue(getFieldValueNotNull(contractorDto.getEmail()));
        addressField.setValue(getFieldValueNotNull(contractorDto.getAddress()));
        commentToAddressField.setValue(getFieldValueNotNull(contractorDto.getCommentToAddress()));
        commentField.setValue(getFieldValueNotNull(contractorDto.getComment()));
        innField.setValue(getFieldValueNotNull(contractorDto.getInn()));
        sortNumberField.setValue(getFieldValueNotNull(contractorDto.getSortNumber()));
        add(new Text("Наименование"), header(), contractorsAccordion());
    }

    private Accordion contractorsAccordion() {
        Accordion accordion = new Accordion();
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(contractorGroupSelect(),
                configurePhoneField(),
                configureFaxField(),
                configureEmailField(),
                configureAddressField(),
                configureCommentToAddressField(),
                configureCommentField(),
                configureInnField(),
                configureSortNumberField());
        accordion.add("О контрагенте", verticalLayout).addThemeVariants(DetailsVariant.FILLED);
        accordion.add("Контактные лица", new VerticalLayout()).addThemeVariants(DetailsVariant.FILLED);
        accordion.add("Реквизиты", new VerticalLayout()).addThemeVariants(DetailsVariant.FILLED);
        accordion.add("Скидки", new VerticalLayout()).addThemeVariants(DetailsVariant.FILLED);
        accordion.add("Доступы", new VerticalLayout()).addThemeVariants(DetailsVariant.FILLED);
        accordion.setWidth("575px");
        return accordion;
    }

    private HorizontalLayout header(){
        HorizontalLayout header = new HorizontalLayout();
        nameField.setWidth("345px");
        header.add(nameField, getSaveButton(), getCancelButton());
        return header;
    }

    private HorizontalLayout contractorGroupSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Select<String> labelSelect = new Select<>();
        labelSelect.setItems("Option one", "Option two");
        labelSelect.setWidth(fieldWidth);
        Label label = new Label("Группы");
        label.setWidth(labelWidth);
        horizontalLayout.add(label, labelSelect);
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
        horizontalLayout.add(label,  commentToAddressField);
        return horizontalLayout;
    }

    private HorizontalLayout configureCommentField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Комментарий");
        label.setWidth(labelWidth);
        commentField.setWidth(fieldWidth);
        commentField.getStyle().set("minHeight", "120px");
        horizontalLayout.add(label,  commentField);
        return horizontalLayout;
    }

    private HorizontalLayout configureInnField() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Инн");
        label.setWidth(labelWidth);
        innField.setWidth(fieldWidth);
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

    private Button getSaveButton() {
        return new Button("Сохранить", event -> {
            ContractorDto newContractorDto = new ContractorDto();
            newContractorDto.setName(nameField.getValue());
            newContractorDto.setPhone(phoneField.getValue());
            newContractorDto.setFax(faxField.getValue());
            newContractorDto.setEmail(emailField.getValue());
            newContractorDto.setAddress(addressField.getValue());
            newContractorDto.setCommentToAddress(commentToAddressField.getValue());
            newContractorDto.setComment(commentField.getValue());
            newContractorDto.setInn(innField.getValue());
            newContractorDto.setSortNumber(sortNumberField.getValue());
            contractorService.create(newContractorDto);
            close();
        });
    }

    private Button getCancelButton() {
        Button cancelButton = new Button("Закрыть", event -> {
            close();
        });
        return cancelButton;
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }
}
