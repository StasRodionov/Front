package com.trade_accounting.components.settings;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.units.CountryDto;
import com.trade_accounting.services.interfaces.units.CountryService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.trade_accounting.config.SecurityConstants.PROFILE_PROFILE__SETTINGS__COUNTRIES_SETTINGS;

@Route(value = PROFILE_PROFILE__SETTINGS__COUNTRIES_SETTINGS, layout = SettingsView.class)
@PageTitle("Страны")
@Slf4j
public class CountrySettingView extends VerticalLayout {

    private final CountryService countryService;
    private final Notifications notifications;
    private Grid<CountryDto> grid = new Grid<>(CountryDto.class);
    private GridPaginator<CountryDto> paginator;

    public CountrySettingView(CountryService countryService, Notifications notifications) {
        this.countryService = countryService;
        this.notifications = notifications;

        paginator = new GridPaginator<>(grid, countryService.getAll(), 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        grid();
        updateList();
        add (
                getAppView(),
                toolsUp(),
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
    }

    private void updateList() {
        grid.setItems(countryService.getAll());
        GridSortOrder<CountryDto> gridSortOrder = new GridSortOrder(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        List<GridSortOrder<CountryDto>> gridSortOrderList = new ArrayList<>();
        gridSortOrderList.add(gridSortOrder);
        grid.sort(gridSortOrderList);
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

    private Button buttonRefresh(){
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonCountry(){
        Button buttonCountry = new Button("Страны",new Icon(VaadinIcon.PLUS_CIRCLE));
//        CountryModalWindow addCountryModalWindow =
//                new CountryModalWindow(new CountryDto(), countryService);
//        buttonCountry.addClickListener(event -> addCountryModalWindow.open());
//        addCountryModalWindow.addDetachListener(event -> updateList());
        return buttonCountry;
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

    private Component toolsUp() {
        HorizontalLayout toolsUp = new HorizontalLayout();
        toolsUp.add(buttonQuestion(), title(), buttonRefresh(), buttonCountry(), valueSelect());
        toolsUp.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return toolsUp;
    }

}
