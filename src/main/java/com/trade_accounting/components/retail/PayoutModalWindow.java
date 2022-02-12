package com.trade_accounting.components.retail;

import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.finance.PayoutDto;
import com.trade_accounting.models.dto.retail.RetailStoreDto;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.trade_accounting.services.interfaces.finance.PayoutService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ErrorLevel;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PayoutModalWindow extends Dialog {

    private final String fieldWidth = "400px";
    private final String labelWidth = "200px";

    private final DateTimePicker date = new DateTimePicker();
    private final NumberField retailStoreId = new NumberField();
    private final TextField whoWasPaid = new TextField();
    private final NumberField companyId = new NumberField();
    private final Checkbox isSent = new Checkbox();
    private final Checkbox isPrint = new Checkbox();
    private final TextField comment = new TextField();
    private final Select<CompanyDto> companyDto = new Select<>();
    private final Select<RetailStoreDto> retailStoreDto = new Select<>();


    private final PayoutService payoutService;
    private PayoutDto payoutDtoToEdit = new PayoutDto();
    private Binder<PayoutDto> payoutDtoBinder = new Binder<>(PayoutDto.class);

    public PayoutModalWindow(PayoutService payoutService) {
        this.payoutService = payoutService;
        add(header());
        add(lowerLayout());
        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);
    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        H2 title = new H2("Выплата");
        header.add(getSaveButton(), getCloseButton(), title);
        return header;
    }

    private HorizontalLayout lowerLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        Div div = new Div();
        div.add(
                addСurrentTime(),
                addRetailStoreId(),
                addWhoWasPaid(),
                addCompanyId(),
                addIsSent(),
                addIsPrint(),
                addComment(),
                addСompanyDto(),
                addRetailStoreDto()
        );
        layout.add(div);
        return layout;
    }

    private com.vaadin.flow.component.Component addСurrentTime() {
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("Время");
        label.setWidth(labelWidth);
        date.setWidth(fieldWidth);
        return new HorizontalLayout(label, date);
    }


    private com.vaadin.flow.component.Component addRetailStoreId() {
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("Точка продаж");
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, retailStoreId);
    }

    private com.vaadin.flow.component.Component addWhoWasPaid() {
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("Кому");
        label.setWidth(labelWidth);
        whoWasPaid.setWidth(fieldWidth);
        return new HorizontalLayout(label, whoWasPaid);
    }

    private com.vaadin.flow.component.Component addCompanyId() {
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("Номер организации");
        label.setWidth(labelWidth);
        companyId.setWidth(fieldWidth);
        return new HorizontalLayout(label, companyId);
    }

    private com.vaadin.flow.component.Component addIsSent() {
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("Отправлено");
        label.setWidth(labelWidth);
        isSent.setWidth(fieldWidth);
        return new HorizontalLayout(label, isSent);
    }

    private com.vaadin.flow.component.Component addIsPrint() {
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("Напечатано");
        label.setWidth(labelWidth);
        isPrint.setWidth(fieldWidth);
        return new HorizontalLayout(label, isPrint);
    }

    private com.vaadin.flow.component.Component addComment() {
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("Коментарий");
        label.setWidth(labelWidth);
        comment.setWidth(fieldWidth);
        return new HorizontalLayout(label, comment);
    }


    private com.vaadin.flow.component.Component addСompanyDto() {
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("Организация");
        label.setWidth(labelWidth);
        companyDto.setItemLabelGenerator(CompanyDto::getName);
        payoutDtoBinder.forField(companyDto).
                withValidator(Objects::nonNull, "Поле должно быть заполнено", ErrorLevel.ERROR)
                .bind("companyDto");
        companyDto.setWidth(fieldWidth);
        return new HorizontalLayout(label, companyDto);
    }

    private com.vaadin.flow.component.Component addRetailStoreDto() {
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("Точка продаж");
        label.setWidth(labelWidth);
        retailStoreDto.setItemLabelGenerator(RetailStoreDto::getName);
        payoutDtoBinder.forField(retailStoreDto).
                withValidator(Objects::nonNull, "Поле должно быть заполнено", ErrorLevel.ERROR)
                .bind("companyDto");
        retailStoreDto.setWidth(fieldWidth);
        return new HorizontalLayout(label, retailStoreDto);
    }

    private com.vaadin.flow.component.button.Button getSaveButton() {
        com.vaadin.flow.component.button.Button saveButton = new com.vaadin.flow.component.button.Button("Сохранить", event -> {
            if (payoutDtoToEdit.getId() != null) {
                payoutDtoToEdit.setDate(date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                payoutDtoToEdit.setRetailStoreId(retailStoreId.getValue().longValue());
                payoutDtoToEdit.setWhoWasPaid(whoWasPaid.getValue());
                payoutDtoToEdit.setCompanyId(companyId.getValue().longValue());
                payoutDtoToEdit.setIsSent(isSent.getValue());
                payoutDtoToEdit.setIsPrint(isPrint.getValue());
                payoutDtoToEdit.setComment(comment.getValue());
                payoutDtoToEdit.setCompanyDto(companyDto.getValue());
                payoutDtoToEdit.setRetailStoreDto(retailStoreDto.getValue());
                if (payoutDtoBinder.validate().isOk()) {
                    payoutService.update(payoutDtoToEdit);
                    clearAll();
                    close();
                } else {
                    payoutDtoBinder.validate().notifyBindingValidationStatusHandlers();
                }
            } else {
                PayoutDto payoutDto = new PayoutDto();
                payoutDtoToEdit.setDate(date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                payoutDtoToEdit.setRetailStoreId(retailStoreId.getValue().longValue());
                payoutDtoToEdit.setWhoWasPaid(whoWasPaid.getValue());
                payoutDtoToEdit.setCompanyId(companyId.getValue().longValue());
                payoutDtoToEdit.setIsSent(isSent.getValue());
                payoutDtoToEdit.setIsPrint(isPrint.getValue());
                payoutDtoToEdit.setComment(comment.getValue());
                payoutDtoToEdit.setCompanyDto(companyDto.getValue());
                payoutDtoToEdit.setRetailStoreDto(retailStoreDto.getValue());
                if (payoutDtoBinder.validate().isOk()) {
                    payoutService.create(payoutDto);
                    clearAll();
                    close();
                } else {
                    payoutDtoBinder.validate().notifyBindingValidationStatusHandlers();
                }
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return saveButton;
    }

    private com.vaadin.flow.component.button.Button getCloseButton() {
        return new Button("Закрыть", event -> {
            clearAll();
            close();
        });
    }

    public void clearAll() {
        date.clear();
        companyDto.clear();
        retailStoreDto.clear();
        comment.clear();
        isSent.clear();
        isPrint.clear();
        retailStoreId.clear();
        whoWasPaid.clear();
        companyId.clear();
    }

}
