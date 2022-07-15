package com.trade_accounting.components.settings;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.units.OnlineStoreDto;
import com.trade_accounting.services.interfaces.units.OnlineStoreService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
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
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import static com.trade_accounting.config.SecurityConstants.PROFILE_PROFILE__SETTINGS__ONLINE_STORES_SETTINGS;

@Route(value = PROFILE_PROFILE__SETTINGS__ONLINE_STORES_SETTINGS, layout = SettingsView.class)
@PageTitle("Интернет-магазины")
@Slf4j
public class OnlineStoreSettingsView extends VerticalLayout {

    private final OnlineStoreService onlineStoreService;
    private final Notifications notifications;
    private Grid<OnlineStoreDto> grid = new Grid<>(OnlineStoreDto.class);
    private GridPaginator<OnlineStoreDto> paginator;
    private final NumberField selectedNumberField;
    private final GridFilter<OnlineStoreDto> filter;

    public OnlineStoreSettingsView(OnlineStoreService onlineStoreService, Notifications notifications) {
        this.onlineStoreService = onlineStoreService;
        this.notifications = notifications;
        this.selectedNumberField = getSelectedNumberField();
        this.filter = new GridFilter<>(grid);

        paginator = new GridPaginator<>(grid, onlineStoreService.getAll(), 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);

        grid();
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
        grid.setItems(onlineStoreService.getAll());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setColumns("id", "name", "type", "orders", "remains");
        grid.getColumnByKey("id").setHeader("№").setId("id");
        grid.getColumnByKey("name").setHeader("Магазин").setId("Магазин");
        grid.getColumnByKey("type").setHeader("Тип").setId("Тип");
        grid.getColumnByKey("orders").setHeader("Заказы").setId("Заказы");
        grid.getColumnByKey("remains").setHeader("Остатки").setId("Остатки");
        getGridContextMenu();

        grid.addItemDoubleClickListener(event -> {
            OnlineStoreDto editStore = event.getItem();
//            OnlineStoreModalWindow onlineStoreModalWindow =
//                    new OnlineStoreModalWindow(editStore, onlineStoreService);
//            onlineStoreModalWindow.addDetachListener(e -> updateList());
//            onlineStoreModalWindow.open();
        });
        grid.addSelectionListener(e -> selectedNumberField.setValue((double) e.getAllSelectedItems().size()));
    }

    private void updateList() {
        grid.setItems(onlineStoreService.getAll());
    }

    private Button buttonQuestion(){
        return  Buttons.buttonQuestion(
                "К МоемуСкладу можно подключить интернет-магазин и настроить автоматическую синхронизацию каталога товаров, списка заказов и остатков" +
                "Читать инструкцию: Подключение интернет-магазинов");
    }

    private H2 title(){
        H2 title = new H2("Синхронизация");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonAdd() {
        Button buttonAdd = new Button("Магазин",new Icon(VaadinIcon.PLUS_CIRCLE));
//        OnlineStoreModalWindow addOnlineStoreModalWindow =
//                new OnlineStoreModalWindow(new OnlineStoreDto(), onlineStoreService);
//        buttonAdd.addClickListener(event -> addOnlineStoreModalWindow.open());
//        addOnlineStoreModalWindow.addDetachListener(event -> updateList());
        return buttonAdd;
    }

    private void deleteSelectedItems() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (OnlineStoreDto onlineStoreDto : grid.getSelectedItems()) {
                onlineStoreService.deleteById(onlineStoreDto.getId());
                notifications.infoNotification("Выбранные магазины успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте магазины для удаления");
        }
    }

    private List<OnlineStoreDto> getData() {
        return onlineStoreService.getAll();
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
        grid.setItems(onlineStoreService.searchByString(text));
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
        toolsUp.add(buttonQuestion(), title(), buttonRefresh(), buttonAdd(), textFieldTop(), numberField(), valueSelect());
        toolsUp.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolsUp;
    }

    private ContextMenu getGridContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setTarget(grid);

        CheckboxGroup<Grid.Column<OnlineStoreDto>> checkboxGroup = new CheckboxGroup<>();
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
