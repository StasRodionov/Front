package com.trade_accounting.components.settings;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.profile.UnitModalWindow;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.units.UnitDto;
import com.trade_accounting.services.interfaces.client.DepartmentService;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.units.UnitService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.trade_accounting.config.SecurityConstants.PROFILE_PROFILE__SETTINGS__UNITS_SETTINGS;

@Route(value = PROFILE_PROFILE__SETTINGS__UNITS_SETTINGS, layout = AppView.class)
@PageTitle("Единицы измерения")
@Slf4j
@SpringComponent
@UIScope
public class UnitsSettingsView extends VerticalLayout implements AfterNavigationObserver {
    private final UnitService unitService;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final Notifications notifications;
    private final List<UnitDto> data;
    private final Grid<UnitDto> grid = new Grid<>(UnitDto.class, false);
    private final GridPaginator<UnitDto> paginator;
    private final GridFilter<UnitDto> filter;
    private final TextField textField = new TextField();


    @Autowired
    public UnitsSettingsView(UnitService unitService, EmployeeService employeeService, DepartmentService departmentService, @Lazy Notifications notifications) {
        this.unitService = unitService;
        this.data = unitService.getAll();
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.notifications = notifications;
        configureGrid();
        paginator = new GridPaginator<>(grid, data, 50);
        this.filter = new GridFilter<>(grid);
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(getAppView(), upperLayout(), filter, grid, paginator);
    }

    private void configureFilter() {
        filter.setFieldToComboBox("unitType", UnitDto::getUnitType, unitService.getAll()
                .stream()
                .filter(distinctByKey(UnitDto::getUnitType))
                .filter(e -> !Objects.equals(e.getUnitType(), ""))
                .collect(Collectors.toList()));
        filter.setFieldToIntegerField("shortName");
        filter.setFieldToIntegerField("fullName");
        filter.setFieldToComboBox("departmentOwner", UnitDto::getDepartmentOwner, unitService.getAll()
                .stream()
                .filter(distinctByKey(UnitDto::getDepartmentOwner))
                .filter(e -> !Objects.equals(e.getDepartmentOwner(), ""))
                .collect(Collectors.toList()));
        filter.setFieldToComboBox("employeeOwner", UnitDto::getEmployeeOwner, unitService.getAll()
                .stream()
                .filter(distinctByKey(UnitDto::getEmployeeOwner))
                .filter(e -> !Objects.equals(e.getEmployeeOwner(), ""))
                .collect(Collectors.toList()));
        filter.setFieldToDatePicker("dateOfChange");
        filter.setFieldToComboBox("employeeChange", UnitDto::getEmployeeChange, unitService.getAll()
                .stream()
                .filter(distinctByKey(UnitDto::getEmployeeChange))
                .filter(e -> !Objects.equals(e.getEmployeeChange(), ""))
                .collect(Collectors.toList()));
        filter.setFieldToComboBox("generalAccess", true, false);
        filter.onSearchClick(e -> paginator.setData(unitService.search(filter.getFilterData())));
        filter.onClearClick(e -> updateList());
    }

    private Grid<UnitDto> configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setItems(data);
        grid.addColumn("unitType").setHeader("Тип").setId("Тип");
        grid.addColumn("shortName").setHeader("Краткое наименование").setId("Краткое наименование");
        grid.addColumn("fullName").setHeader("Полное наименование").setId("Полное наименование");
        grid.addColumn("sortNumber").setHeader("Цифровой код").setId("Цифровой код");
        grid.addColumn("generalAccess").setHeader("Общий доступ").setId("Общий доступ");
        grid.addColumn("departmentOwner").setHeader("Владелец-отдел").setId("Владелец-отдел");
        grid.addColumn("employeeOwner").setHeader("Владелец-сотрудник").setId("Владелец-сотрудник");
        grid.addColumn("dateOfChange").setHeader("Когда изменен").setId("Когда изменен");
        grid.addColumn("employeeChange").setHeader("Кто изменил").setId("Кто изменил");
        grid.setHeight("80vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addItemDoubleClickListener(event -> {
            UnitDto editUnit = event.getItem();
            UnitModalWindow unitModalWindow =
                    new UnitModalWindow(editUnit, unitService);
            unitModalWindow.addDetachListener(e -> updateList());
            unitModalWindow.open();
        });
        return grid;
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonUnit(), buttonFilter(), filterByTextField(), valueSelect());
        upper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upper;
    }

    private Button buttonUnit() {
        final Button button = new Button("Единица измерения");
        button.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
        UnitModalWindow unitModalWindow =
                new UnitModalWindow(new UnitDto(), unitService);
        button.addClickListener(event -> unitModalWindow.open());
        unitModalWindow.addDetachListener(event -> updateList());
        return button;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("С справочнике собраны единицы измерения, которые можно указать в карточке товара для учета остатков, " +
                        "а также варианты упаковки товаров. При необходимости вы можете добавлять в справочник новые единицы измерения."));
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        buttonRefresh.addClickListener(e -> updateList());
        return buttonRefresh;
    }


    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private H4 title() {
        H4 title = new H4("Единицы измерения");
        title.setHeight("2.2em");
        title.setWidth("80px");
        return title;
    }

    private void updateList() {
        grid.setItems(unitService.getAll());
    }

    private void updateListAfterSearch(String text) {
        grid.setItems(unitService.findBySearch(text));
    }

    private TextField filterByTextField() {
        textField.setPlaceholder("Наименование или код");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> updateListAfterSearch(textField.getValue()));
        textField.setWidth("300px");
        return textField;
    }

    private AppView getAppView() {
        return new AppView();
    }

    private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        final Set<Object> seen = new HashSet<>();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }

    public List<UnitDto> moveToRecycleBin() {
        List<UnitDto> moved = new ArrayList<>();
        if (!grid.getSelectedItems().isEmpty()) {
            for (UnitDto unitDto : grid.getSelectedItems()) {
                moved.add(unitService.getById(unitDto.getId()));
                unitService.moveToIsRecyclebin(unitDto.getId());
                notifications.infoNotification("Выбранные единицы измерения помещены в корзину");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные единицы измерения");
        }

        return moved;
    }

    private Select<String> valueSelect() {

        return SelectConfigurer.configureBulkEditAndDeleteSelect(
                () -> {
                    if (grid.getSelectedItems().stream().anyMatch(e -> Objects.equals(e.getUnitType(), "Системный"))) {
                        notifications.errorNotification("Системные единицы измерения нельзя изменить");
                    } else if (!grid.getSelectedItems().isEmpty()) {
                        UnitsBulkEditModalWindow unitsBulkEditModalWindow =
                                new UnitsBulkEditModalWindow(new ArrayList<>(grid.getSelectedItems()), employeeService, departmentService, unitService);
                        unitsBulkEditModalWindow.open();
                    } else {
                        notifications.errorNotification("Сначала отметьте галочками нужные единицы измерения");
                    }
                    grid.deselectAll();
                    paginator.setData(unitService.getAll());
                }
                , () -> {
                    if (grid.getSelectedItems().stream().anyMatch(e -> Objects.equals(e.getUnitType(), "Системный"))) {
                        notifications.errorNotification("Системные единицы измерения нельзя удалить");
                    } else if (!grid.getSelectedItems().isEmpty()) {
                        moveToRecycleBin();
                    } else {
                        notifications.errorNotification("Сначала отметьте галочками нужные единицы измерения");
                    }
                    grid.deselectAll();
                    paginator.setData(unitService.getAll());
                });
    }
}
