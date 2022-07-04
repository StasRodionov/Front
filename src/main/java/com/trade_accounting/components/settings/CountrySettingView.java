package com.trade_accounting.components.settings;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.units.CountryDto;
import com.trade_accounting.models.dto.units.CurrencyDto;
import com.trade_accounting.services.interfaces.units.CountryService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
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
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import static com.trade_accounting.config.SecurityConstants.PROFILE_PROFILE__SETTINGS__COUNTRIES_SETTINGS;

@Route(value = PROFILE_PROFILE__SETTINGS__COUNTRIES_SETTINGS, layout = SettingsView.class)
@PageTitle("Страны")
@Slf4j
public class CountrySettingView extends VerticalLayout {

    private final CountryService countryService;
    private final Notifications notifications;
    private Grid<CountryDto> grid = new Grid<>(CountryDto.class);
    private GridPaginator<CountryDto> paginator;
    private final NumberField selectedNumberField;
    private final GridFilter<CountryDto> filter;

    public CountrySettingView(CountryService countryService, Notifications notifications) {
        this.countryService = countryService;
        this.notifications = notifications;
        this.selectedNumberField = getSelectedNumberField();
        this.filter = new GridFilter<>(grid);

        paginator = new GridPaginator<>(grid, countryService.getAll(), 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);

        grid();
        configureFilter();
        add (
                getAppView(),
                toolsUp(),
                filter,
                grid,
                paginator
        );
    }

    private Component getAppView() {
        return new AppView();
    }

    private void grid() {
        grid.setItems(countryService.getAll());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setColumns("id", "type", "shortName", "fullName", "digitCode", "twoLetterCode", "threeLetterCode");
        grid.getColumnByKey("id").setHeader("№").setId("id");
        grid.getColumnByKey("type").setHeader("Тип").setId("Тип");
        grid.getColumnByKey("shortName").setHeader("Краткое наименование").setId("Краткое наименование");
        grid.getColumnByKey("fullName").setHeader("Полное наименование").setId("Полное наименование");
        grid.getColumnByKey("digitCode").setHeader("Цифровой код").setId("Цифровой код");
        grid.getColumnByKey("twoLetterCode").setHeader("Буквенный код(2)").setId("Буквенный код(2)");
        grid.getColumnByKey("threeLetterCode").setHeader("Буквенный код(3)").setId("Буквенный код(3)");
        getGridContextMenu();

        grid.addItemDoubleClickListener(event -> {
            CountryDto editCountry = event.getItem();
            CountryModalWindow countryModalWindow =
                    new CountryModalWindow(editCountry, countryService);
            countryModalWindow.addDetachListener(e -> updateList());
            countryModalWindow.open();
        });
        grid.addSelectionListener(e -> selectedNumberField.setValue((double) e.getAllSelectedItems().size()));
    }

    private void updateList() {
        grid.setItems(countryService.getAll());
    }

    private Button buttonQuestion(){
        return  Buttons.buttonQuestion(
                "Список стран, которые вы можете указать в качестве страны происхождения товара.");
    }

    private H2 title(){
        H2 title = new H2("Страны");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonCountry() {
        Button buttonCountry = new Button("Страны",new Icon(VaadinIcon.PLUS_CIRCLE));
        CountryModalWindow addCountryModalWindow =
                new CountryModalWindow(new CountryDto(), countryService);
        buttonCountry.addClickListener(event -> addCountryModalWindow.open());
        addCountryModalWindow.addDetachListener(event -> updateList());
        return buttonCountry;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private void configureFilter() {
        filter.onSearchClick(e -> paginator.setData(countryService.search(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(countryService.getAll()));
    }

    private void deleteSelectedItems() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (CountryDto countryDto : grid.getSelectedItems()) {
                countryService.deleteById(countryDto.getId());
                notifications.infoNotification("Выбранные страны успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте страны для удаления");
        }
    }

    private List<CountryDto> getData() {
        return countryService.getAll();
    }

    private Select<String> valueSelect() {
        return SelectConfigurer.configureDeleteSelect(() -> {
            deleteSelectedItems();
            grid.deselectAll();
            paginator.setData(getData());
        });
    }

    private NumberField getSelectedNumberField() {
        final NumberField numberField = new NumberField();
        numberField.setWidth("50px");
        numberField.setValue(0D);
        return numberField;
    }

    private NumberField numberField() {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private void updateList(String text) {
        grid.setItems(countryService.searchByString(text));
    }

    private TextField textFieldTop() {
        TextField quickSearch = new TextField();
        quickSearch.setPlaceholder("Наименование");
        quickSearch.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        quickSearch.setValueChangeMode(ValueChangeMode.EAGER);
        quickSearch.addValueChangeListener(e -> updateList(quickSearch.getValue()));
        quickSearch.setWidth("300px");

        return quickSearch;
    }

    private Component toolsUp() {
        HorizontalLayout toolsUp = new HorizontalLayout();
        toolsUp.add(buttonQuestion(), title(), buttonRefresh(), buttonCountry(), buttonFilter(), textFieldTop(), numberField(), valueSelect());
        toolsUp.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolsUp;
    }

    private ContextMenu getGridContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setTarget(grid);

        CheckboxGroup<Grid.Column<CountryDto>> checkboxGroup = new CheckboxGroup<>();
        contextMenu.addItem(checkboxGroup);

        checkboxGroup.setItems(grid.getColumns().stream());
        checkboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        checkboxGroup.setItemLabelGenerator(i -> i.getId().orElse(i.getKey()));
        checkboxGroup.setValue(grid.getColumns().stream().filter(Component::isVisible).collect(Collectors.toSet()));
        checkboxGroup.addSelectionListener(e -> {
            e.getAddedSelection().forEach(i -> i.setVisible(true));
            e.getRemovedSelection().forEach(i -> i.setVisible(false));
        });

        MenuItem menuItem = contextMenu.addItem("Количество строк");
        SubMenu subMenu = menuItem.getSubMenu();

        subMenu.addItem("25", e -> paginator.setItemsPerPage(25));

        subMenu.addItem("50", e -> paginator.setItemsPerPage(50));

        subMenu.addItem("100", e -> paginator.setItemsPerPage(100));

        return contextMenu;
    }

}
