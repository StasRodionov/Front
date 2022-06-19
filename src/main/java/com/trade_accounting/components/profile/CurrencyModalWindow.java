package com.trade_accounting.components.profile;

import com.trade_accounting.models.dto.units.CurrencyDto;
import com.trade_accounting.models.dto.warehouse.ProductDto;
import com.trade_accounting.services.interfaces.units.CurrencyService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ErrorLevel;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import static com.vaadin.flow.component.Tag.H2;

@UIScope
@Slf4j
public class CurrencyModalWindow extends Dialog {

    private TextField shortNameField = new TextField();
    private TextArea fullNameField = new TextArea();
    private TextArea digitalCodeField = new TextArea();
    private TextArea letterCodeField = new TextArea();
    private TextArea sortNumberField = new TextArea();
    private Long id;
    private final CurrencyService currencyService;

    public CurrencyModalWindow(CurrencyDto currencyDto,
                               CurrencyService currencyService) {
        this.currencyService = currencyService;

        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);

        id = currencyDto.getId();
        shortNameField.setValue(getFieldValueNotNull(currencyDto.getShortName()));
        fullNameField.setValue(getFieldValueNotNull(currencyDto.getFullName()));
        digitalCodeField.setValue(getFieldValueNotNull(currencyDto.getDigitalCode()));
        letterCodeField.setValue(getFieldValueNotNull(currencyDto.getLetterCode()));
        sortNumberField.setValue(getFieldValueNotNull(currencyDto.getSortNumber()));

        fullNameField.setPlaceholder("Введите полное наименование");
        fullNameField.setValueChangeMode(ValueChangeMode.EAGER);
        shortNameField.setPlaceholder("Введите короткое наименование");
        shortNameField.setValueChangeMode(ValueChangeMode.EAGER);
        digitalCodeField.setPlaceholder("Введите цифровой код");
        digitalCodeField.setValueChangeMode(ValueChangeMode.EAGER);
        letterCodeField.setPlaceholder("Введите буквенный код");
        letterCodeField.setValueChangeMode(ValueChangeMode.EAGER);
        sortNumberField.setPlaceholder("Введите сортировочный номер");
        sortNumberField.setValueChangeMode(ValueChangeMode.EAGER);

        add(
                header(),
                getHorizontalLayout("Наименование", fullNameField),
                getHorizontalLayout("Краткое наименование", shortNameField),
                getHorizontalLayout("Цифровой код", digitalCodeField),
                getHorizontalLayout("Буквенный код", letterCodeField),
                getHorizontalLayout("Сортировочный номер", sortNumberField),
                footer()
        );

    }

    private Component header() {
        HorizontalLayout header = new HorizontalLayout();
        H2 title = new H2("Валюта");
        title.setWidth("345px");
        header.add(title);
        return header;
    }

    private <T extends Component & HasSize> HorizontalLayout getHorizontalLayout(String labelText, T field) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label(labelText);
        field.setWidth("400px");
        label.setWidth("200px");
        horizontalLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, label);
        horizontalLayout.add(label, field);
        return horizontalLayout;
    }

    private HorizontalLayout footer() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.add(getSaveButton(), getCancelButton(), getDeleteButton());
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        footer.setPadding(true);
        return footer;
    }

    private Button getSaveButton() {
        Button saveButton = new Button("Сохранить", event -> {
            CurrencyDto currencyDto = new CurrencyDto();
            currencyDto.setId(id);
            currencyDto.setShortName(shortNameField.getValue());
            currencyDto.setFullName(fullNameField.getValue());
            currencyDto.setDigitalCode(digitalCodeField.getValue());
            currencyDto.setLetterCode(letterCodeField.getValue());
            currencyDto.setSortNumber(sortNumberField.getValue());
            if (currencyDto.getId() == null) {
                currencyService.create(currencyDto);
            } else {
                currencyService.update(currencyDto);
            }

            close();
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        return saveButton;
    }

    private Button getCancelButton() {
        Button cancelButton = new Button("Закрыть", event -> {
            close();
        });
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return cancelButton;
    }

    private Button getDeleteButton() {
        Button deleteButton = new Button("Удалить", event -> {
            currencyService.deleteById(id);
            close();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        return deleteButton;
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }
}
