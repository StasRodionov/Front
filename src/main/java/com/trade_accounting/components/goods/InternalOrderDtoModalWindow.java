package com.trade_accounting.components.goods;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.InternalOrderDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.InternalOrderService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringComponent
@UIScope
@Slf4j
public class InternalOrderDtoModalWindow extends Dialog {

    private final ContractService contractService;
    private final CompanyService companyService;
    private final InternalOrderService internalOrderService;
    private InternalOrderDto internalOrderDto;

    private final ComboBox<CompanyDto> companyDtoComboBox = new ComboBox<>();
    private final Checkbox checkboxIsSent = new Checkbox("Отправлено");
    private final Checkbox checkboxIsPrint = new Checkbox("Напечатано");
    private final TextField returnNumber = new TextField();
    private final TextArea textArea = new TextArea();

    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final Binder<InternalOrderDto> internalOrderDtoBinder =
            new Binder<>(InternalOrderDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";
    private final Notifications notifications;

    @Autowired
    public InternalOrderDtoModalWindow(ContractService contractService, CompanyService companyService,
                                       InternalOrderService internalOrderService, Notifications notifications) {
        this.contractService = contractService;
        this.companyService = companyService;
        this.internalOrderService = internalOrderService;
        this.notifications = notifications;

        setSizeFull();
        add(headerLayout(), formLayout());
    }

    public void setInternalOrderForEdit(InternalOrderDto internalOrderDto) {
        this.internalOrderDto = internalOrderDto;
        returnNumber.setValue(internalOrderDto.getId().toString());
        dateTimePicker.setValue(LocalDateTime.parse(internalOrderDto.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        companyDtoComboBox.setValue(companyService.getById(internalOrderDto.getCompanyId()));
        textArea.setValue(internalOrderDto.getComment());
    }

    private HorizontalLayout headerLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(title(), saveButton(), closeButton(), addAcceptanceButton());
        return horizontalLayout;
    }

    private VerticalLayout formLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(formLayout1(), formLayout2());
        return verticalLayout;
    }

    private HorizontalLayout formLayout1() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add( dateConfigure(), numberConfigure(), contractConfigure());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout2() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(checkboxLayout(), commentConfig());
        return horizontalLayout;
    }

    private H2 title() {
        H2 title = new H2("Внутренние заказы");
        return title;
    }

    private Button saveButton() {
        return new Button("Сохранить", e -> {
            if (!internalOrderDtoBinder.validate().isOk()) {
                internalOrderDtoBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
                InternalOrderDto dto = new InternalOrderDto();
                dto.setId(Long.parseLong(returnNumber.getValue()));
                dto.setCompanyId(companyDtoComboBox.getValue().getId());
                dto.setDate(dateTimePicker.getValue().toString());
                dto.setIsSent(checkboxIsSent.getValue());
                dto.setIsPrint(checkboxIsPrint.getValue());
                dto.setComment(textArea.getValue());
                internalOrderService.create(dto);

                UI.getCurrent().navigate("admissions");
                close();
                clearAllFieldsModalView();
                notifications.infoNotification(String.format("Внутренний заказ c ID=%s сохранена", dto.getId()));
            }
        });
    }

    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> {
            close();
            clearAllFieldsModalView();
        });
        return button;
    }

    private Button addAcceptanceButton() {
        Button button = new Button("Добавить внутренний заказ", new Icon(VaadinIcon.PLUS));
        button.addClickListener(e -> {
            // Добавить приемку в таблицу

            clearAllFieldsModalView();
        });
        return button;
    }

    private HorizontalLayout numberConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Внутренний заказ №");
        label.setWidth("150px");
        returnNumber.setWidth("50px");
        horizontalLayout.add(label, returnNumber);
        internalOrderDtoBinder.forField(returnNumber)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(InternalOrderDto::getIdValid, InternalOrderDto::setIdValid);
        return horizontalLayout;
    }

    private HorizontalLayout dateConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("От");
        dateTimePicker.setWidth("350px");
        horizontalLayout.add(label, dateTimePicker);
        internalOrderDtoBinder.forField(dateTimePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(InternalOrderDto::getDateValid, InternalOrderDto::setDateValid);
        return horizontalLayout;
    }

    private HorizontalLayout contractConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<CompanyDto> list = companyService.getAll();
        if (list != null) {
            companyDtoComboBox.setItems(list);
        }
        companyDtoComboBox.setItemLabelGenerator(dto -> companyService.getById(contractService.getById(dto.getId()).getCompanyId()).getName());
        companyDtoComboBox.setWidth("350px");
        Label label = new Label("Организация");
        label.setWidth("100px");
        horizontalLayout.add(label, companyDtoComboBox);
        internalOrderDtoBinder.forField(companyDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(InternalOrderDto::getCompanyDtoValid, InternalOrderDto::setCompanyDtoValid);
        return horizontalLayout;
    }

    private HorizontalLayout commentConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Комментарий");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("100px");
        horizontalLayout.add(label, textArea);
        return horizontalLayout;
    }

    private VerticalLayout checkboxLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(checkboxIsSent, checkboxIsPrint);
        return verticalLayout;
    }

    private void clearAllFieldsModalView() {
        dateTimePicker.setValue(null);
        returnNumber.setValue("");
        checkboxIsPrint.setValue(false);
        checkboxIsSent.setValue(false);
        textArea.setValue("");
    }
}