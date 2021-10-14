package com.trade_accounting.components.profile;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.EmployeeDto;
import com.trade_accounting.models.dto.ImageDto;
import com.trade_accounting.models.dto.InternalOrderDto;
import com.trade_accounting.services.interfaces.DepartmentService;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.ImageService;
import com.trade_accounting.services.interfaces.PositionService;
import com.trade_accounting.services.interfaces.RoleService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
        add(upperLayout(), filter, grid, paginator);
    }

    private List<EmployeeDto> getData() {
        return employeeService.getAll();
    }

    private void updateGrid() {
        paginator.setData(getData(), false);
        log.info("Таблица обновилась");
    }

    private void configureGrid() {
        grid.removeAllColumns();
        grid.addColumn(EmployeeDto::getLastName).setHeader("Фамилия").setId("Фамилия");

        // Нужно добавить фотографию при инициализации сотрудника

//        Grid.Column<EmployeeDto> photoColumn = grid.addColumn(new ComponentRenderer<>() {
//            @Override
//            public Component createComponent(EmployeeDto item) {
//                ImageDto imageDto = imageService.getById(item.getImageDtoId());
//                if (imageDto != null) {
//                    Image image = new Image(new StreamResource("image", () ->
//                            new ByteArrayInputStream(imageDto.getContent())), "");
//                    image.setHeight("48px");
//
//                    return image;
//                }
//                return new Label();
//            }
//        }).setHeader("Фото");
//        photoColumn.setKey("imageDto").setId("Фото");
        grid.addColumn(EmployeeDto::getFirstName).setHeader("Имя").setId("Имя");
        grid.addColumn(EmployeeDto::getMiddleName).setHeader("Отчество").setId("Отчество");
        grid.addColumn(EmployeeDto::getEmail).setHeader("E-mail").setId("E-mail");
        grid.addColumn(EmployeeDto::getPhone).setHeader("Телефон").setId("Телефон");
        grid.addColumn(EmployeeDto::getDescription).setHeader("Описание").setId("Описание");
        grid.addColumn(employeeDto -> (departmentService.getById(employeeDto.getDepartmentDtoId()).getName()))
                .setHeader("Отдел").setId("Отдел");
        grid.addColumn(employeeDto -> (employeeDto.getRoleDtoIds().stream()
                .map(map -> roleService.getById(map).getName())
                .collect(Collectors.toSet())))
                .setHeader("Роль").setId("Роль");
        grid.addColumn(employeeDto -> (positionService.getById(employeeDto.getPositionDtoId()).getName()))
                .setHeader("Должность").setId("Должность");
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
                            positionService);
            addEmployeeModalWindowView.addDetachListener(e -> updateGrid());
            addEmployeeModalWindowView.open();
        });
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
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
                            new ImageDto(),
                            notifications,
                            departmentService,
                            positionService);
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
        return text;
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
        Select<String> valueSelect = new Select<>();
        valueSelect.setItems("Выберите действие", "Удалить");
        valueSelect.setValue("Выберите действие");
        valueSelect.addValueChangeListener(event -> {

        if (valueSelect.getValue().equals("Удалить")) {
            deleteSelectedInternalOrders();
            grid.deselectAll();
            valueSelect.setValue("Выберите действие");
            paginator.setData(getData());
            valueSelect.setPlaceholder("");
        }
        });

        valueSelect.setWidth("130px");
        return valueSelect;
    }

    private void deleteSelectedInternalOrders() {
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
