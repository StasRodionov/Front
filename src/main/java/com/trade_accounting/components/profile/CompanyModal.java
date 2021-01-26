package com.trade_accounting.components.profile;

import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class CompanyModal extends Dialog {

    private TextField name = new TextField();
    private TextField inn = new TextField();
    private TextArea address = new TextArea();
    private TextArea commentToAddress = new TextArea();
    private TextField email = new TextField();
    private TextField phone = new TextField();
    private TextField fax = new TextField();
    private TextField leader = new TextField();
    private TextField leaderManagerPosition = new TextField();
    private TextField leaderSignature = new TextField();
    private TextField chiefAccountant = new TextField();
    private TextField chiefAccountantSignature = new TextField();
    private TextField payerVat = new TextField();
    private TextField sortNumber = new TextField();
    private TextField stamp = new TextField();
    private final String labelWidth = "100px";
    private final String fieldWidth = "400px";
    private final CompanyService companyService;

    public CompanyModal(CompanyService companyService) {
        this.companyService = companyService;
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        add(header("Добавление"), accordionCompany());
    }

    private HorizontalLayout header(String titleText){
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
            CompanyDto companyDto = new CompanyDto();
            companyDto.setName(name.getValue());
            companyDto.setInn(inn.getValue());
            companyDto.setAddress(address.getValue());
            companyDto.setCommentToAddress(commentToAddress.getValue());
            companyDto.setEmail(email.getValue());
            companyDto.setPhone(phone.getValue());
            companyDto.setFax(fax.getValue());
            companyService.create(companyDto);
            // todo: add remaining fields
            close();
        });
    }

    private Button buttonCancel() {
        return new Button("Закрыть", event -> close());
    }

    private Accordion accordionCompany() {
        Accordion accordion = new Accordion();
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(configureName(),
                configureInn(),
                configureAddress());
        accordion.add("О юр. лице", verticalLayout).addThemeVariants(DetailsVariant.FILLED);
        accordion.add("Контактные лица", new VerticalLayout()).addThemeVariants(DetailsVariant.FILLED);
        accordion.add("Реквизиты", new VerticalLayout()).addThemeVariants(DetailsVariant.FILLED);
        accordion.add("Доступы", new VerticalLayout()).addThemeVariants(DetailsVariant.FILLED);
        accordion.setWidth("575px");
        return accordion;
    }

    private HorizontalLayout configureName() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Наименование");
        label.setWidth(labelWidth);
        name.setWidth(fieldWidth);
        horizontalLayout.add(label, name);
        return horizontalLayout;
    }

    private HorizontalLayout configureInn() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("ИНН");
        label.setWidth(labelWidth);
        inn.setWidth(fieldWidth);
        horizontalLayout.add(label, inn);
        return horizontalLayout;
    }

    private HorizontalLayout configureAddress() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Адрес");
        label.setWidth(labelWidth);
        address.setWidth(fieldWidth);
        horizontalLayout.add(label, address);
        return horizontalLayout;
    }
}
