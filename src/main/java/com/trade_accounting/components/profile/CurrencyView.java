package com.trade_accounting.components.profile;

import com.trade_accounting.components.AppView;
import com.trade_accounting.models.dto.CurrencyDto;
import com.trade_accounting.services.interfaces.CurrencyService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "currency", layout = AppView.class)
@PageTitle("Валюты")
public class CurrencyView extends VerticalLayout {

    private final CurrencyService currencyService;

    public CurrencyView(CurrencyService currencyService) {
        this.currencyService = currencyService;
        add(toolsTop(), grid(), toolsBottom());
    }

    private Button buttonQuestion(){
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private H2 title(){
        H2 title = new H2("Валюты");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonRefresh(){
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private  Button buttonCurrency(){
        return new Button("Валюта", new Icon(VaadinIcon.PLUS_CIRCLE));
    }

    private Button buttonFilter(){
        return new Button("Фильтр");
    }

    private TextField textFieldTop(){
        TextField text = new TextField();
        text.setPlaceholder("Краткое наименование");
        text.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        text.setWidth("300px");
        return text;
    }

    private NumberField numberField(){
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect(){
        Select<String> valueSelect = new Select<>();
        valueSelect.setItems("Изменить");
        valueSelect.setValue("Изменить");
        valueSelect.setWidth("130px");
        return valueSelect;
    }

    private Button buttonSettings(){
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private HorizontalLayout toolsTop(){
        HorizontalLayout tools = new HorizontalLayout();
        tools.add(buttonQuestion(),title(), buttonRefresh(), buttonCurrency(), buttonFilter(),
                textFieldTop(), numberField(), valueSelect(), buttonSettings());
        tools.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return tools;
    }

    private Grid<CurrencyDto> grid(){
        Grid<CurrencyDto> grid = new Grid<>(CurrencyDto.class);
        grid.setItems(currencyService.getAll());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.setColumns("id", "shortName","fullName","digitalCode","letterCode");
        grid.getColumnByKey("id").setHeader("ID");
        grid.getColumnByKey("shortName").setHeader("Краткое наименование");
        grid.getColumnByKey("fullName").setHeader("Полное наименование");
        grid.getColumnByKey("digitalCode").setHeader("Цифровой код");
        grid.getColumnByKey("letterCode").setHeader("Буквенный код");
        grid.setHeight("66vh");
        return grid;
    }

    private Button doubleLeft(){
        return new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT));
    }

    private Button left(){
        return new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    }

    private TextField textFieldBottom(){
        TextField textField = new TextField("", "1-1 из 1");
        textField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        return textField;
    }

    private Button right(){
        return new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    }

    private Button doubleRight(){
        return new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT));
    }

    private HorizontalLayout toolsBottom(){
        HorizontalLayout tools = new HorizontalLayout();
        tools.add(doubleLeft(),left(),textFieldBottom(),right(),doubleRight());
        return tools;
    }
}
