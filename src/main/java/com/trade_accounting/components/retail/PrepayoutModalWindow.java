package com.trade_accounting.components.retail;

import com.trade_accounting.models.dto.*;
import com.trade_accounting.services.interfaces.PrepayoutService;
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

public class PrepayoutModalWindow extends Dialog {

    private final String fieldWidth = "400px";
    private final String labelWidth = "200px";

    private final DateTimePicker date = new DateTimePicker();
    private final Select<RetailStoreDto> retailStoreId = new Select<>();
    private final Select<ContractorDto> contractorId = new Select<>();
    private final Select<CompanyDto> companyId = new Select<>();
    private final NumberField cash = new NumberField();
    private final NumberField cashless = new NumberField();
    private final NumberField sum = new NumberField();
    private final Checkbox isSent = new Checkbox();
    private final Checkbox isPrint = new Checkbox();
    private final TextField comment = new TextField();

    private final PrepayoutService prepayoutService;
    PrepayoutDto prepayoutDto;
    private PrepayoutDto prepayoutDtoToEdit = new PrepayoutDto();
    private Binder<PrepayoutDto> prepayoutDtoBinder = new Binder<>(PrepayoutDto.class);

    public PrepayoutModalWindow(PrepayoutService prepayoutService) {
        this.prepayoutService = prepayoutService;
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
                addSum(),
                addIsSent(),
                addIsPrint(),
                addComment()
        );
        layout.add(div);
        return layout;
    }


    private Component addDate() {
        Label label = new Label("Дата");
        label.setWidth(labelWidth);
        date.setWidth(fieldWidth);
        return new HorizontalLayout(label, date);
    }

    private Component addRetailStoreId() {
        Label label = new Label("Контрагент");
        label.setWidth(labelWidth);
        retailStoreId.setItemLabelGenerator(RetailStoreDto::getName);
        prepayoutDtoBinder.forField(retailStoreId).
                withValidator(Objects::nonNull, "Поле должно быть заполнено", ErrorLevel.ERROR)
                .bind("retailStoreId");
        retailStoreId.setWidth(fieldWidth);
        return new HorizontalLayout(label, retailStoreId);
    }

    private Component addСontractorId() {
        Label label = new Label("Контрагент");
        label.setWidth(labelWidth);
        contractorId.setItemLabelGenerator(ContractorDto::getName);
        prepayoutDtoBinder.forField(contractorId).
                withValidator(Objects::nonNull, "Поле должно быть заполнено", ErrorLevel.ERROR)
                .bind("contractorId");
        contractorId.setWidth(fieldWidth);
        return new HorizontalLayout(label, contractorId);
    }

    private Component addСompanyId() {
        Label label = new Label("Организация");
        label.setWidth(labelWidth);
        companyId.setItemLabelGenerator(CompanyDto::getName);
        prepayoutDtoBinder.forField(companyId).
                withValidator(Objects::nonNull, "Поле должно быть заполнено", ErrorLevel.ERROR)
                .bind("companyId");
        companyId.setWidth(fieldWidth);
        return new HorizontalLayout(label, companyId);
    }

    private Component addCash() {
        Label label = new Label("Оплата наличными");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, cash);
    }

    private Component addCashless() {
        Label label = new Label("Безналичная оплата");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, cashless);
    }

    private Component addSum() {
        Label label = new Label("Итого");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, sum);
    }

    private Component addIsSent() {
        Label label = new Label("Отправлено");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, isSent);
    }

    private Component addIsPrint() {
        Label label = new Label("Напечатано");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, isPrint);
    }

    private Component addComment() {
        Label label = new Label("Коментарий");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, comment);
    }

    public void setPrepayoutForEdit(PrepayoutDto editDto) {
        prepayoutDto = editDto;
        prepayoutDtoToEdit.setDate(editDto.getDate());
        prepayoutDtoToEdit.setRetailStoreId(editDto.getRetailStoreId());
        prepayoutDtoToEdit.setContractorId(editDto.getContractorId());
        prepayoutDtoToEdit.setCompanyId(editDto.getCompanyId());
        prepayoutDtoToEdit.setCash(editDto.getCash());
        prepayoutDtoToEdit.setCashless(editDto.getCashless());
        prepayoutDtoToEdit.setSum(editDto.getSum());
        prepayoutDtoToEdit.setIsSent(editDto.getIsSent());
        prepayoutDtoToEdit.setIsPrint(editDto.getIsPrint());
        prepayoutDtoToEdit.setComment(editDto.getComment());
    }


    private Button getSaveButton() {
        Button saveButton = new Button("Сохранить", event -> {
            if (prepayoutDtoToEdit.getId() != null) {
                prepayoutDtoToEdit.setDate(date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                prepayoutDtoToEdit.setRetailStoreId(retailStoreId.getValue().getId());
                prepayoutDtoToEdit.setContractorId(contractorId.getValue().getId());
                prepayoutDtoToEdit.setCompanyId(companyId.getValue().getId());
                prepayoutDtoToEdit.setCash(BigDecimal.valueOf(cash.getValue()));
                prepayoutDtoToEdit.setCashless(BigDecimal.valueOf(cashless.getValue()));
                prepayoutDtoToEdit.setSum(BigDecimal.valueOf(sum.getValue()));
                prepayoutDtoToEdit.setIsSent(isSent.getValue());
                prepayoutDtoToEdit.setIsPrint(isPrint.getValue());
                prepayoutDtoToEdit.setComment(comment.getValue());
                if (prepayoutDtoBinder.validate().isOk()) {
                    prepayoutService.update(prepayoutDtoToEdit);
                    clearAll();
                    close();
                } else {
                    prepayoutDtoBinder.validate().notifyBindingValidationStatusHandlers();
                }
            } else {
                PrepayoutDto prepayoutDto = new PrepayoutDto();
                prepayoutDtoToEdit.setDate(date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                prepayoutDtoToEdit.setRetailStoreId(retailStoreId.getValue().getId());
                prepayoutDtoToEdit.setContractorId(contractorId.getValue().getId());
                prepayoutDtoToEdit.setCompanyId(companyId.getValue().getId());
                prepayoutDtoToEdit.setCash(BigDecimal.valueOf(cash.getValue()));
                prepayoutDtoToEdit.setCashless(BigDecimal.valueOf(cashless.getValue()));
                prepayoutDtoToEdit.setSum(BigDecimal.valueOf(sum.getValue()));
                prepayoutDtoToEdit.setIsSent(isSent.getValue());
                prepayoutDtoToEdit.setIsPrint(isPrint.getValue());
                if (prepayoutDtoBinder.validate().isOk()) {
                    prepayoutService.create(prepayoutDto);
                    clearAll();
                    close();
                } else {
                    prepayoutDtoBinder.validate().notifyBindingValidationStatusHandlers();
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
        date.clear();
        retailStoreId.clear();
        contractorId.clear();
        companyId.clear();
        cash.clear();
        cashless.clear();
        sum.clear();
        isSent.clear();
        isPrint.clear();
        comment.clear();
    }
}
