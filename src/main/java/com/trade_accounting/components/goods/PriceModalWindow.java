package com.trade_accounting.components.goods;

import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.PriceListDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.PriceListService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@SpringComponent
public class PriceModalWindow extends Dialog {

    private final String fieldWidth = "400px";
    private final String labelWidth = "200px";

    private final CompanyService companyService;
    private final PriceListService priceListService;
    PriceListDto priceListDto;

    private final ComboBox<CompanyDto> companyComboBox = new ComboBox<>();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final Checkbox checkboxIsSent = new Checkbox();
    private final Checkbox checkboxIsPrint = new Checkbox();
    private final TextField comment = new TextField();

    private final Binder<PriceListDto> priceListDtoBinder =
            new Binder<>(PriceListDto.class);
    private PriceListDto priceListDtoToEdit = new PriceListDto();
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";

    public PriceModalWindow(PriceListService priceListService,
                            CompanyService companyService) {
        this.companyService = companyService;
        this.priceListService = priceListService;
        add(header());
        add(lowerLayout());
        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);

    }
    public void setPostingEdit(PriceListDto editDto) {
        this.priceListDto = editDto;
        dateTimePicker.setValue(editDto.getTime());
        companyComboBox.setValue(companyService.getById(editDto.getCompanyDtoId()));
        checkboxIsSent.setValue(editDto.getSent());
        checkboxIsPrint.setValue(editDto.getPrinted());
    }


    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        H2 title = new H2("Прайс-лист");
        header.add(getSaveButton(), getCloseButton(), title);
        return header;
    }

    private HorizontalLayout lowerLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        Div div = new Div();
        div.add(
                dateConfigure(),
                companyConfigure(),
                checkboxSentLayout(),
                checkboxPrintLayout(),
                comment()
        );
        layout.add(div);
        return layout;
    }

    private Button getSaveButton() {
        Button saveButton = new Button("Сохранить", event -> {
            if (priceListDto.getId() != null) {
                priceListDto.setCompanyDtoId(companyComboBox.getValue().getId());
                priceListDto.setTime(dateTimePicker.getValue());
                priceListDto.setSent(checkboxIsSent.getValue());
                priceListDto.setPrinted(checkboxIsPrint.getValue());
                priceListDto.setCommentary(comment.getValue());
                if (priceListDtoBinder.validate().isOk()) {
                    priceListService.update(priceListDtoToEdit);
                    clearAll();
                    close();
                } else {
                    priceListDtoBinder.validate().notifyBindingValidationStatusHandlers();
                }
            } else {
                PriceListDto priceListDto = new PriceListDto();
                priceListDto.setCompanyDtoId(companyComboBox.getValue().getId());
                priceListDto.setTime(dateTimePicker.getValue());
                priceListDto.setSent(checkboxIsSent.getValue());
                priceListDto.setPrinted(checkboxIsPrint.getValue());
                priceListDto.setCommentary(comment.getValue());
                if (priceListDtoBinder.validate().isOk()) {
                    priceListService.create(priceListDto);
                    clearAll();
                    close();
                } else {
                    priceListDtoBinder.validate().notifyBindingValidationStatusHandlers();
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


    private Component dateConfigure() {
        Label label = new Label("От");
        label.setWidth(labelWidth);
        dateTimePicker.setWidth(fieldWidth);
        priceListDtoBinder.forField(dateTimePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        return new HorizontalLayout(label, dateTimePicker);
    }

    private Component comment() {
        Label label = new Label("Комментарий");
        comment.setWidth(fieldWidth);
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, comment);
    }

    private Component checkboxSentLayout() {
        Label label = new Label("Отправлено");
        checkboxIsSent.setWidth(fieldWidth);
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, checkboxIsSent);
    }

    private Component checkboxPrintLayout() {
        Label label = new Label("Напечатано");
        checkboxIsPrint.setWidth(fieldWidth);
        label.setWidth(labelWidth);
        return new HorizontalLayout(label, checkboxIsPrint);
    }

    private HorizontalLayout companyConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        java.util.List<CompanyDto> list = companyService.getAll();
        if (list != null) {
            companyComboBox.setItems(list);
        }
        companyComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyComboBox.setWidth(fieldWidth);
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("Организация");
        companyComboBox.setWidth(fieldWidth);
        label.setWidth(labelWidth);
        horizontalLayout.add(label, companyComboBox);
        priceListDtoBinder.forField(companyComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        //
        return horizontalLayout;
    }


    private void clearAll() {
        companyComboBox.clear();
        dateTimePicker.clear();
        checkboxIsSent.clear();
        checkboxIsPrint.clear();
        comment.clear();
    }
}
