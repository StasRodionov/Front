package com.trade_accounting.components.profile;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
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
    private Grid<CurrencyDto> grid = new Grid<>(CurrencyDto.class);
    private GridPaginator<CurrencyDto> paginator;
    private final GridFilter<CurrencyDto> filter;

    public CurrencyView(CurrencyService currencyService) {
        this.currencyService = currencyService;
        paginator = new GridPaginator<>(grid,currencyService.getAll(), 100 );
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        grid();

        this.filter = new GridFilter<>(grid);
        configureFilter();

        add(getToolbar(), filter, grid, paginator);
//        updateList();
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
        Button currencyButton = new Button("Валюта", new Icon(VaadinIcon.PLUS_CIRCLE));
        CurrencyModalWindow addCurrencyModalWindow =
                new CurrencyModalWindow(new CurrencyDto(), currencyService);
        currencyButton.addClickListener(event -> addCurrencyModalWindow.open());
        addCurrencyModalWindow.addDetachListener(event -> updateList());
        return currencyButton;
    }

    private void updateList() {
        grid.setItems(currencyService.getAll());
    }

    private void configureFilter() {
        filter.setVisibleFields(false, "name", "legalDetailDto");

        filter.setFieldToComboBox("payerVat", Boolean.TRUE, Boolean.FALSE);
        filter.setFieldToIntegerField("inn");

        filter.onSearchClick(e -> paginator.setData(currencyService.search(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(currencyService.getAll()));
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

//    private HorizontalLayout toolsTop(){
//        HorizontalLayout tools = new HorizontalLayout();
//        tools.add(buttonQuestion(),title(), buttonRefresh(), buttonCurrency(), buttonFilter(),
//                textFieldTop(), numberField(), valueSelect(), buttonSettings());
//        tools.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
//        return tools;
//    }
    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        TextField searchTextField = new TextField();
        searchTextField.setPlaceholder("Наименование");
        searchTextField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        searchTextField.setWidth("300px");

        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));


        toolbar.add(buttonQuestion(), title(), buttonRefresh(), buttonCurrency(), filterButton,
                searchTextField, numberField(), valueSelect(), buttonSettings());
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolbar;
    }

    private void grid(){
        grid.setItems(currencyService.getAll());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setColumns("id", "shortName","fullName","digitalCode","letterCode", "sortNumber");
        grid.getColumnByKey("id").setHeader("ID");
        grid.getColumnByKey("shortName").setHeader("Краткое наименование");
        grid.getColumnByKey("fullName").setHeader("Полное наименование");
        grid.getColumnByKey("digitalCode").setHeader("Цифровой код");
        grid.getColumnByKey("letterCode").setHeader("Буквенный код");
        grid.getColumnByKey("sortNumber").setHeader("Сортировочный номер");
        grid.setHeight("64vh");
        grid.addItemDoubleClickListener(event -> {
            CurrencyDto editCurrency = event.getItem();
            CurrencyModalWindow currencyModalWindow =
                    new CurrencyModalWindow(editCurrency, currencyService);
            currencyModalWindow.addDetachListener(e -> updateList());
            currencyModalWindow.open();
        });
    }
}
