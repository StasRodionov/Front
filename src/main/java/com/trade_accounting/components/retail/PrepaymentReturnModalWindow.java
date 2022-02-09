package com.trade_accounting.components.retail;

import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.finance.PrepaymentReturnDto;
import com.trade_accounting.models.dto.retail.RetailStoreDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.finance.PrepaymentReturnService;
import com.trade_accounting.services.interfaces.retail.RetailStoreService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ErrorLevel;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PrepaymentReturnModalWindow extends Dialog {

    private final String fieldWidth = "400px";
    private final String labelWidth = "200px";

    private final DateTimePicker time = new DateTimePicker();
    private final Select<RetailStoreDto> retailStoreId = new Select<>();
    private final Select<ContractorDto> contractorId = new Select<>();
    private final Select<CompanyDto> companyId = new Select<>();
    private final NumberField sumCash = new NumberField();
    private final NumberField sumNonCash = new NumberField();
    private final Checkbox sent = new Checkbox();
    private final Checkbox printed = new Checkbox();
    private final TextField comment = new TextField();

    private final PrepaymentReturnService prepaymentReturnService;
    private final CompanyService companyService;
    private final RetailStoreService retailStoreService;
    private final ContractorService contractorService;
    private PrepaymentReturnDto prepaymentReturnDtotoEtid = new PrepaymentReturnDto();
    private Binder<PrepaymentReturnDto> prepaymentReturnDtoBinder = new Binder<>(PrepaymentReturnDto.class);

    public PrepaymentReturnModalWindow(PrepaymentReturnService prepaymentReturnService, CompanyService companyService, RetailStoreService retailStoreService, ContractorService contractorService) {
        this.prepaymentReturnService = prepaymentReturnService;
        this.companyService = companyService;
        this.retailStoreService = retailStoreService;
        this.contractorService = contractorService;
        add(header());
        add(lowerLayout());
        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);
    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        H2 title = new H2("Предоплата");
        header.add(getSaveButton(), getCloseButton(), title);
        return header;
    }

    private HorizontalLayout lowerLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        Div div = new Div();
        div.add(
                addDate(),
                addRetailStoreId(),
                addСontractorId(),
                addСompanyId(),
                addCash(),
                addCashless(),
                addIsSent(),
                addIsPrint(),
                addComment()
        );
        layout.add(div);
        return layout;
    }


    private Component addDate() {
        Label label = new Label("Время");
        label.setWidth(labelWidth);
        time.setWidth(fieldWidth);
        return new HorizontalLayout(label, time);
    }

    private Component addRetailStoreId() {
        Label label = new Label("Контрагент");
        label.setWidth(labelWidth);
        retailStoreId.setItems(retailStoreService.getAll());
        retailStoreId.setItemLabelGenerator(RetailStoreDto::getName);
        prepaymentReturnDtoBinder.forField(retailStoreId).
                withValidator(Objects::nonNull, "Поле должно быть заполнено", ErrorLevel.ERROR)
                .bind("retailStoreId");
        retailStoreId.setWidth(fieldWidth);
        return new HorizontalLayout(label, retailStoreId);
    }

    private Component addСontractorId() {
        Label label = new Label("Контрагент");
        label.setWidth(labelWidth);
        contractorId.setItems(contractorService.getAll());
        contractorId.setItemLabelGenerator(ContractorDto::getName);
        prepaymentReturnDtoBinder.forField(contractorId).
                withValidator(Objects::nonNull, "Поле должно быть заполнено", ErrorLevel.ERROR)
                .bind("contractorId");
        contractorId.setWidth(fieldWidth);
        return new HorizontalLayout(label, contractorId);
    }

    private Component addСompanyId() {
        Label label = new Label("Организация");
        label.setWidth(labelWidth);
        companyId.setItems(companyService.getAll());
        companyId.setItemLabelGenerator(CompanyDto::getName);
        prepaymentReturnDtoBinder.forField(companyId).
                withValidator(Objects::nonNull, "Поле должно быть заполнено", ErrorLevel.ERROR)
                .bind("companyId");
        companyId.setWidth(fieldWidth);
        return new HorizontalLayout(label, companyId);
    }

    private Component addCash() {
        Label label = new Label("Оплата наличными");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, sumCash);
    }

    private Component addCashless() {
        Label label = new Label("Безналичная оплата");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, sumNonCash);
    }

    private Component addIsSent() {
        Label label = new Label("Отправлено");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, sent);
    }

    private Component addIsPrint() {
        Label label = new Label("Напечатано");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, printed);
    }

    private Component addComment() {
        Label label = new Label("Коментарий");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, comment);
    }


    private Button getSaveButton() {
        Button saveButton = new Button("Сохранить", event -> {
            if (prepaymentReturnDtotoEtid.getId() != null) {
                prepaymentReturnDtotoEtid.setTime(time.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                prepaymentReturnDtotoEtid.setRetailStoreId(retailStoreId.getValue().getId());
                prepaymentReturnDtotoEtid.setContractorId(contractorId.getValue().getId());
                prepaymentReturnDtotoEtid.setCompanyId(companyId.getValue().getId());
                prepaymentReturnDtotoEtid.setSumCash(BigDecimal.valueOf(sumCash.getValue()));
                prepaymentReturnDtotoEtid.setSumNonСash(BigDecimal.valueOf(sumNonCash.getValue()));
                prepaymentReturnDtotoEtid.setSent(sent.getValue());
                prepaymentReturnDtotoEtid.setPrinted(printed.getValue());
                prepaymentReturnDtotoEtid.setComment(comment.getValue());
                if (prepaymentReturnDtoBinder.validate().isOk()) {
                    prepaymentReturnService.update(prepaymentReturnDtotoEtid);
                    clearAll();
                    close();
                } else {
                    prepaymentReturnDtoBinder.validate().notifyBindingValidationStatusHandlers();
                }
            } else {
                PrepaymentReturnDto prepaymentReturnDto = new PrepaymentReturnDto();
                prepaymentReturnDtotoEtid.setTime(time.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                prepaymentReturnDtotoEtid.setRetailStoreId(retailStoreId.getValue().getId());
                prepaymentReturnDtotoEtid.setContractorId(contractorId.getValue().getId());
                prepaymentReturnDtotoEtid.setCompanyId(companyId.getValue().getId());
                prepaymentReturnDtotoEtid.setSumCash(BigDecimal.valueOf(sumCash.getValue()));
                prepaymentReturnDtotoEtid.setSumNonСash(BigDecimal.valueOf(sumNonCash.getValue()));
                prepaymentReturnDtotoEtid.setSent(sent.getValue());
                prepaymentReturnDtotoEtid.setPrinted(printed.getValue());
                if (prepaymentReturnDtoBinder.validate().isOk()) {
                    prepaymentReturnService.create(prepaymentReturnDto);
                    clearAll();
                    close();
                } else {
                    prepaymentReturnDtoBinder.validate().notifyBindingValidationStatusHandlers();
                }
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return saveButton;
    }

    private Button getCloseButton() {
        return new Button("Закрыть", event -> {
            clearAll();
            close();
        });
    }

    public void clearAll() {
        time.clear();
        retailStoreId.clear();
        contractorId.clear();
        companyId.clear();
        sumCash.clear();
        sumNonCash.clear();
        sent.clear();
        printed.clear();
        comment.clear();
    }
}
