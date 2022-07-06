package com.trade_accounting.components.settings;

import com.trade_accounting.models.dto.units.CountryDto;
import com.trade_accounting.services.interfaces.units.CountryService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class CountryModalWindow extends Dialog {

    private TextField shortNameField = new TextField();
    private TextField fullNameField = new TextField();
    private TextField digitCodeField = new TextField();
    private TextField twoLetterCodeField = new TextField();
    private TextField threeLetterCodeField = new TextField();
    private Long id;
    private final CountryService countryService;


    public CountryModalWindow(CountryDto countryDto, CountryService countryService) {
        this.countryService = countryService;

        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);

        id = countryDto.getId();
        shortNameField.setValue(getFieldValueNotNull(countryDto.getShortName()));
        fullNameField.setValue((getFieldValueNotNull(countryDto.getFullName())));
        digitCodeField.setValue(getFieldValueNotNull(countryDto.getDigitCode()));
        twoLetterCodeField.setValue(getFieldValueNotNull(countryDto.getTwoLetterCode()));
        threeLetterCodeField.setValue(getFieldValueNotNull(countryDto.getThreeLetterCode()));

        add(
            header(),
            getHorizontalLayout("Краткое наименование", fullNameField),
            getHorizontalLayout("Полное наименование", shortNameField),
            getHorizontalLayout("Цифровой код", digitCodeField),
            getHorizontalLayout("Буквенный код(2)", twoLetterCodeField),
            getHorizontalLayout("Буквенный код(3)", threeLetterCodeField)

        );
    }

    @Override
    public void open() {
        if (id == null) {
            init();
        }
        super.open();
    }

    private void init() {
        shortNameField.clear();
        fullNameField.clear();
        digitCodeField.clear();
        twoLetterCodeField.clear();
        threeLetterCodeField.clear();
    }

    private Button getSaveButton() {
        return new Button("Сохранить", event -> {
            CountryDto countryDto = new CountryDto();
            countryDto.setId(id);
            countryDto.setType("Системный");
            countryDto.setShortName(shortNameField.getValue());
            countryDto.setFullName(fullNameField.getValue());
            countryDto.setDigitCode(digitCodeField.getValue());
            countryDto.setTwoLetterCode(twoLetterCodeField.getValue());
            countryDto.setThreeLetterCode(threeLetterCodeField.getValue());
            if (countryDto.getId() == null) {
                countryService.create(countryDto);
            } else {
                countryService.update(countryDto);
            }

            close();
        });
    }

    private Button getCancelButton() {
        return new Button("Закрыть", event -> {
            close();
        });
    }

    private Button getDeleteButton() {
        return new Button("Удалить", event -> {
            try {
                countryService.deleteById(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            close();
        });
    }

    private Component header() {
        HorizontalLayout header = new HorizontalLayout();
        header.add(getSaveButton(), getCancelButton(), getDeleteButton());
        header.setPadding(true);
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

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }
}
