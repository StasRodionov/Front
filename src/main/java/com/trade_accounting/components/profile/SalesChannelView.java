package com.trade_accounting.components.profile;


import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.units.SalesChannelDto;
import com.trade_accounting.services.interfaces.units.SalesChannelService;
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

import java.util.List;
import java.util.stream.Collectors;

import static com.trade_accounting.components.util.Buttons.buttonQuestion;

public class SalesChannelView extends VerticalLayout {
    private final NumberField selectedNumberField;
    private final List<SalesChannelDto> data;
    private final SalesChannelService salesChannelService;
    private Grid<SalesChannelDto> grid = new Grid<>(SalesChannelDto.class);
    private GridPaginator<SalesChannelDto> paginator;
    private final GridFilter<SalesChannelDto> filter;
    private final Notifications notifications;

    public SalesChannelView(SalesChannelService salesChannelService, Notifications notifications) {
        this.salesChannelService = salesChannelService;
        this.data = salesChannelService.getAll();
        this.notifications = notifications;
        this.paginator = new GridPaginator<>(grid, salesChannelService.getAll(), 50);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        grid();
        this.selectedNumberField = getSelectedNumberField();
        this.filter = new GridFilter<>(grid);
        updateList();
        configureFilter();
        add(getToolbar(), filter, grid, paginator);
    }

    private List<SalesChannelDto> getData() {
        return salesChannelService.getAll();
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonSalesChannel() {
        Button salesChannelButton = new Button("Канал продаж", new Icon(VaadinIcon.PLUS_CIRCLE));
        SalesChannelModalWindow salesChannelModalWindow = new SalesChannelModalWindow(new SalesChannelDto(), salesChannelService);
        salesChannelButton.addClickListener(event -> salesChannelModalWindow.open());
        salesChannelModalWindow.addDetachListener(event -> updateList());
        return salesChannelButton;
    }

    private void updateList() {
        grid.setItems(salesChannelService.getAll());
    }

    public void grid() {
        grid.setItems(salesChannelService.getAll());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setColumns("name", "type", "description", "generalAccess", "departmentOwner", "employeeOwner", "dateOfChange", "employeeChange");
        grid.getColumnByKey("name").setHeader("Наименование").setId("Наименование");
        grid.getColumnByKey("type").setHeader("Тип").setId("Тип");
        grid.getColumnByKey("description").setHeader("Описание").setId("Описание");
        grid.getColumnByKey("generalAccess").setHeader("Общий доступ").setId("Общий доступ");
        grid.getColumnByKey("departmentOwner").setHeader("Владелец-отдел").setId("Владелец-отдел");
        grid.getColumnByKey("employeeOwner").setHeader("Владелец-сотрудник").setId("Владелец-сотрудник");
        grid.getColumnByKey("dateOfChange").setHeader("Когда изменил").setId("Когда изменил");
        grid.getColumnByKey("employeeChange").setHeader("Кто изменил").setId("Кто изменил");
        getGridContextMenu();

        grid.setHeight("64vh");

        grid.addItemDoubleClickListener(event -> {
            SalesChannelDto editSalesChannel = event.getItem();
            SalesChannelModalWindow salesChannelModalWindow = new SalesChannelModalWindow(editSalesChannel, salesChannelService);
            salesChannelModalWindow.addDetachListener(e -> updateList());
            salesChannelModalWindow.open();
        });
        grid.addSelectionListener(e -> selectedNumberField.setValue((double) e.getAllSelectedItems().size()));
    }


    private NumberField numberField() {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }


//    private Select<String> valueSelect() {
//        return new SelectExt.SelectBuilder<String>()
//                .item(SelectConstants.CHANGE_SELECT_ITEM)
//                .defaultValue(SelectConstants.CHANGE_SELECT_ITEM)
//                .width(SelectConstants.SELECT_WIDTH_130PX)
//                .build();
//    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        TextField searchTextField = new TextField();
        searchTextField.setPlaceholder("Наименование");
        searchTextField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        searchTextField.setValueChangeMode(ValueChangeMode.EAGER);
        //searchTextField.addValueChangeListener(event -> updateList(searchTextField.getValue()));
        searchTextField.setWidth("300px");

        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));


        toolbar.add(buttonQuestion(), title(), buttonRefresh(), buttonSalesChannel(), filterButton,
                searchTextField, numberField(), valueSelect(), buttonSettings());
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolbar;
    }


    private H2 title() {
        H2 title = new H2("Каналы продаж");
        title.setHeight("2.2em");
        return title;
    }

    private ContextMenu getGridContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setTarget(grid);

        CheckboxGroup<Grid.Column<SalesChannelDto>> checkboxGroup = new CheckboxGroup<>();
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

    private NumberField getSelectedNumberField() {
        final NumberField numberField = new NumberField();
        numberField.setWidth("50px");
        numberField.setValue(0D);
        return numberField;
    }

    private void configureFilter() {
        filter.onSearchClick(e -> paginator.setData(salesChannelService.search(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(salesChannelService.getAll()));
    }

    private Select<String> valueSelect() {
        return SelectConfigurer.configureDeleteSelect(() -> {
            deleteSelectedSalesChannel();
            grid.deselectAll();
            paginator.setData(getData());
        });
    }


    private void deleteSelectedSalesChannel() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (SalesChannelDto salesChannelDto : grid.getSelectedItems()) {
                salesChannelService.deleteById(salesChannelDto.getId());
            }
            if (grid.getSelectedItems().size() == 1) {
                notifications.infoNotification("Выбранный канал продаж успешно удален");
            } else if (grid.getSelectedItems().size() > 1) {
                notifications.infoNotification("Выбранные каналы продаж успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные каналы продаж");
        }
    }

}

