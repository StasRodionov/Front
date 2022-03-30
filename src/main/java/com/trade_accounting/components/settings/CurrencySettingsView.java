package com.trade_accounting.components.settings;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.profile.CurrencyModalWindow;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.units.CurrencyDto;
import com.trade_accounting.services.interfaces.units.CurrencyService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Route(value = "profile/settings/currency_settings", layout = SettingsView.class)
@PageTitle("Учетная запись")
@Slf4j
public class CurrencySettingsView extends VerticalLayout {

    private final NumberField selectedNumberField;
    private final List<CurrencyDto> data;
    private final CurrencyService currencyService;
    private Grid<CurrencyDto> grid = new Grid<>(CurrencyDto.class);
    private GridPaginator<CurrencyDto> paginator;
    private final GridFilter<CurrencyDto> filter;
    private final String CURRENCY = "currency.png";

    public CurrencySettingsView(CurrencyService currencyService) {
        this.selectedNumberField = getSelectedField();
        this.currencyService = currencyService;
        this.data = currencyService.getAll();
        paginator = new GridPaginator<>(grid, data, 30);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        add(getAppView(), horizontalLayout(), filter, horizontalLayout2(), grid, paginator);
    }

    private AppView getAppView() {
        return new AppView();
    }

    private HorizontalLayout horizontalLayout() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.add(buttonQuestion(), title(), buttonRefresh(), buttonCurrency(), getButtonFilter(), textField(), getSelectedField());
        hl.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return hl;
    }

    private HorizontalLayout horizontalLayout2() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.add(image());
        return hl;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new VerticalLayout(
                        new Text(
                                "Валюта учета — это обычно валюта страны, где зарегистрирована ваша компания. В валюте учета рассчитывается себестоимость товаров, прибыль и взаиморасчеты с контрагентами. По умолчанию в МоемСкладе за валюту учета принят российский рубль. Вы можете изменить валюту учета в справочнике валют.\n" +
                                        "\n" +
                                        "Если вы хотите использовать несколько валют, то решите, какая валюта будет у вас валютой учета. Откройте настройки валюты учета и выберите нужную валюту. После этого добавьте другие валюты.\n"
                        ),
                        new Div(
                                new Text("Читать инструкцию: "),
                                new Anchor("#", "Валюты"))
                ));
    }

    private H2 title() {
        H2 title = new H2("Валюты");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonCurrency() {
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

    private TextField textField() {
        TextField text = new TextField();
        text.setPlaceholder("Краткое наименование");
        text.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        text.setValueChangeMode(ValueChangeMode.EAGER);
        text.addValueChangeListener(event -> updateList(text.getValue()));
        text.setWidth("300px");
        return text;
    }

    private void updateList(String text) {
        grid.setItems(currencyService.findBySearch(text));
    }

    private NumberField getSelectedField() {
        final NumberField numberField = new NumberField();
        numberField.setWidth("50px");
        numberField.setValue(0D);
        return numberField;
    }

    private void configureGrid() {
        grid.setItems(currencyService.getAll());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setColumns("id", "shortName", "fullName", "digitalCode", "letterCode", "sortNumber");
        grid.getColumnByKey("id").setHeader("ID").setId("ID");
        grid.getColumnByKey("shortName").setFlexGrow(2).setHeader("Краткое наименование").setId("Краткое наименование");
        grid.getColumnByKey("fullName").setFlexGrow(2).setHeader("Полное наименование").setId("Полное наименование");
        grid.getColumnByKey("digitalCode").setHeader("Цифровой код").setId("Цифровой код");
        grid.getColumnByKey("letterCode").setHeader("Буквенный код").setId("Буквенный код");
        grid.getColumnByKey("sortNumber").setHeader("Код").setId("Код");

        grid.setHeight("64vh");
        grid.addItemDoubleClickListener(event -> {
            CurrencyDto editCurrency = event.getItem();
            CurrencyModalWindow currencyModalWindow =
                    new CurrencyModalWindow(editCurrency, currencyService);
            currencyModalWindow.addDetachListener(e -> updateList());
            currencyModalWindow.open();
        });
        grid.addSelectionListener(e -> selectedNumberField.setValue((double) e.getAllSelectedItems().size()));
    }

    private Button getButtonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
    }

    private void configureFilter() {
        filter.setVisibleFields(false, "shortName");
        filter.onSearchClick(e -> paginator.setData(currencyService.search(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(currencyService.getAll()));
    }

    private Image image() {
        StreamResource resource = new StreamResource("currency.png", () -> getImageInputStream(CURRENCY));
        Image logo = new Image(resource, "currency");
        logo.setId("currency");
        logo.setHeight("235px");
        logo.setWidth("1154px");
        return logo;
    }

    public static InputStream getImageInputStream(String svgIconName) {
        InputStream imageInputStream = null;
        try {
            imageInputStream = new DataInputStream(new FileInputStream("src/main/resources/static/icons/" + svgIconName));
        } catch (IOException ex) {
            log.error("При чтении icon {} произошла ошибка", svgIconName);
        }
        return imageInputStream;
    }
}
