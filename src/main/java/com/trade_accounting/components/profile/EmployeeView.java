package com.trade_accounting.components.profile;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.client.EmployeeDto;
import com.trade_accounting.models.dto.util.ImageDto;
import com.trade_accounting.services.interfaces.client.DepartmentService;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.client.PositionService;
import com.trade_accounting.services.interfaces.client.RoleService;
import com.trade_accounting.services.interfaces.util.ImageService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Route(value = "employee", layout = AppView.class)
@PageTitle("Сотрудники")
public class EmployeeView extends VerticalLayout {

    private final Grid<EmployeeDto> grid = new Grid<>(EmployeeDto.class, false);
    private final EmployeeService employeeService;
    private final RoleService roleService;
    private final ImageService imageService;
    private final DepartmentService departmentService;
    private final PositionService positionService;
    private final GridFilter<EmployeeDto> filter;
    private final GridPaginator<EmployeeDto> paginator;
    private final Notifications notifications;
    private String addTitle = "Добавление сотрудника";
    private String editTitle = "Изменение сотрудника";

    public EmployeeView(EmployeeService employeeService, RoleService roleService, ImageService imageService, Notifications notifications,
                        DepartmentService departmentService, PositionService positionService) {
        this.employeeService = employeeService;
        this.roleService = roleService;
        this.imageService = imageService;
        this.departmentService = departmentService;
        this.positionService = positionService;
        this.notifications = notifications;
        List<EmployeeDto> data = getData();
        configureGrid();
        this.filter = new GridFilter<>(grid);
        this.paginator = new GridPaginator<>(grid, data, 50);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        configureFilter();
        add(upperLayout(), filter, grid, paginator);
    }

    private List<EmployeeDto> getData() {
        return employeeService.getAll();
    }

    private void updateGrid() {
        paginator.setData(getData(), false);
        log.info("Таблица обновилась");
    }

    private void configureFilter() {
        filter.onSearchClick(e -> paginator.setData(employeeService.search(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(employeeService.getAll()));
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.removeAllColumns();
        Grid.Column<EmployeeDto> photoColumn = grid.addColumn(new ComponentRenderer<>() {
            @Override
            public Component createComponent(EmployeeDto item) {
                ImageDto imageDto = imageService.getById(item.getImageDtoId());
                if (imageDto != null) {
                    Image image = new Image(new StreamResource("image", () ->
                            new ByteArrayInputStream(imageDto.getContent())), "");
                    image.setHeight("48px");

                    return image;
                }
                return new Label();
            }
        }).setHeader("Фото");
        photoColumn.setKey("imageDto").setId("Фото");
        grid.addColumn(EmployeeDto::getLastName).setKey("lastName").setHeader("Фамилия").setId("Фамилия");
        grid.addColumn(EmployeeDto::getFirstName).setKey("firstName").setHeader("Имя").setId("Имя");
        grid.addColumn(EmployeeDto::getMiddleName).setKey("middleName").setHeader("Отчество").setId("Отчество");
        grid.addColumn(EmployeeDto::getEmail).setKey("email").setFlexGrow(2).setHeader("E-mail").setId("E-mail");
        grid.addColumn(EmployeeDto::getPhone).setKey("phone").setFlexGrow(2).setHeader("Телефон").setId("Телефон");
        grid.addColumn(EmployeeDto::getDescription).setKey("description").setHeader("Описание").setId("Описание");
        grid.addColumn(employeeDto -> (departmentService.getById(employeeDto.getDepartmentDtoId()).getName()))
                .setKey("department").setHeader("Отдел").setId("Отдел");
        grid.addColumn(employeeDto -> (positionService.getById(employeeDto.getPositionDtoId()).getName()))
                .setKey("position").setHeader("Должность").setId("Должность");
        grid.addColumn(employeeDto -> (employeeDto.getRoleDtoIds().stream()
                .map(map -> roleService.getById(map).getName())
                .collect(Collectors.toSet()).toString().replace("[", "").replace("]", "")))
                .setHeader("Роль").setId("Роль");
        grid.setHeight("64vh");
        grid.addItemDoubleClickListener(event -> {
            EmployeeDto employeeDto = event.getItem();
            ImageDto imageDto = imageService.getById(employeeDto.getImageDtoId());
            AddEmployeeModalWindowView addEmployeeModalWindowView =
                    new AddEmployeeModalWindowView(
                            employeeDto,
                            employeeService,
                            roleService,
                            imageService,
                            imageDto,
                            notifications,
                            departmentService,
                            positionService,
                            editTitle);
            addEmployeeModalWindowView.addDetachListener(e -> updateGrid());
            addEmployeeModalWindowView.open();
        });
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion("Здесь предоставлена информация о вашем персонале. Вы можете добавить нового члена команды, нажав кнопку \"+ Сотрудник\"." +
                "Также можно редактировать данные сотрудников, нажав двойным кликом на данные в таблице");
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        buttonRefresh.addClickListener(ev -> updateGrid());
        paginator.reloadGrid();
        return buttonRefresh;
    }

    private Button buttonUnit() {
        Button buttonUnit = new Button("Сотрудник", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickShortcut(Key.ENTER);
        buttonUnit.addClickListener(click -> {
            log.info("Вы нажали кнопку для добавление сотрудника!");
            AddEmployeeModalWindowView addEmployeeModalWindowView =
                    new AddEmployeeModalWindowView(
                            null,
                            employeeService,
                            roleService,
                            imageService,
                            imageService.getById(1L),
                            notifications,
                            departmentService,
                            positionService,
                            addTitle);
            addEmployeeModalWindowView.addDetachListener(event -> updateGrid());
            addEmployeeModalWindowView.isModal();
            addEmployeeModalWindowView.open();
        });
        return buttonUnit;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private TextField text() {
        TextField text = new TextField();
        text.setPlaceholder("Поиск");
        text.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        text.setWidth("300px");
        text.setValueChangeMode(ValueChangeMode.EAGER);
        text.addValueChangeListener(event -> updateList(text.getValue()));
        return text;
    }

    private void updateList(String text) {
        grid.setItems(employeeService.findBySearch(text));
    }

    private H2 title() {
        H2 title = new H2("Сотрудники");
        title.setHeight("2.2em");
        return title;
    }

    private NumberField numberField() {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect() {
        return SelectConfigurer.configureDeleteSelect(() -> {
            deleteSelectedEmployees();
            grid.deselectAll();
            paginator.setData(getData());
        });
    }

    private void deleteSelectedEmployees() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (EmployeeDto employeeDto : grid.getSelectedItems()) {
                employeeService.deleteById(employeeDto.getId());
            }
            if (grid.getSelectedItems().size() == 1) {
                notifications.infoNotification("Выбранный сотрудник успешно удален");
            } else if (grid.getSelectedItems().size() > 1) {
                notifications.infoNotification("Выбранные сотрудники успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужных сотрудников");
        }
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upperLayout = new HorizontalLayout();
        upperLayout.add(buttonQuestion(), title(), buttonRefresh(), buttonUnit(), buttonFilter(), text(), numberField(),
                valueSelect(), buttonSettings());
        upperLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upperLayout;
    }

}
