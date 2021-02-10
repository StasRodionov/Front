package com.trade_accounting.components.profile;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.EmployeeDto;
import com.trade_accounting.models.dto.ImageDto;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.ImageService;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;


@Route(value = "employee", layout = AppView.class)
@PageTitle("Сотрудники")
public class EmployeeView extends VerticalLayout {

    private final Grid<EmployeeDto> grid;
    private final EmployeeService employeeService;
    private final RoleService roleService;
    private final ImageService imageService;
    private List<EmployeeDto> data;
    private final List<EmployeeDto> finalData;
    private final GridPaginator<EmployeeDto> paginator;

    public EmployeeView(EmployeeService employeeService, RoleService roleService, ImageService imageService) {
        this.employeeService = employeeService;
        this.roleService = roleService;
        this.finalData = employeeService.getAll();
        this.imageService = imageService;
        this.data = finalData;
        this.grid = new Grid<>(EmployeeDto.class);
        this.paginator = new GridPaginator<>(grid, data, 100);

        setHorizontalComponentAlignment(Alignment.CENTER, paginator);

        configureGrid();
        updateGrid();

        add(upperLayout(), grid, paginator);
    }

    private void updateGrid() {
        grid.setItems(employeeService.getAll());
        System.out.println("updateList");
    }

    private void configureGrid() {
        grid.setColumns("lastName", "imageDto", "firstName",
                "middleName", "email", "phone", "description", "roleDto");
        grid.getColumnByKey("lastName").setHeader("Фамилия");
        grid.getColumnByKey("imageDto").setHeader("Фото профиля");
        grid.getColumnByKey("firstName").setHeader("Имя");
        grid.getColumnByKey("middleName").setHeader("Отчество");
        grid.getColumnByKey("email").setHeader("E-mail");
        grid.getColumnByKey("phone").setHeader("Телефон");
        grid.getColumnByKey("description").setHeader("Описание");
        grid.getColumnByKey("roleDto").setHeader("Роль");
        grid.addItemDoubleClickListener(event -> {
            EmployeeDto employeeDto = event.getItem();
            ImageDto imageDto = employeeDto.getImageDto();
            AddEmployeeModalWindowView addEmployeeModalWindowView =
                    new AddEmployeeModalWindowView(
                            employeeDto,
                            employeeService,
                            roleService,
                            imageService,
                            imageDto);
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
        return buttonRefresh;
    }

    private Button buttonUnit() {
        Button buttonUnit = new Button("Сотрудник", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickShortcut(Key.ENTER);
        buttonUnit.addClickListener(click -> {
            System.out.println("Вы нажали кнопку для добавление сотрудника!");
            AddEmployeeModalWindowView addEmployeeModalWindowView =
                    new AddEmployeeModalWindowView(
                            null,
                            employeeService,
                            roleService,
                            imageService,
                            null);
            addEmployeeModalWindowView.addDetachListener(event -> updateGrid());
            addEmployeeModalWindowView.isModal();
            addEmployeeModalWindowView.open();
        });
        return buttonUnit;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        return buttonFilter;
    }

    private Button buttonSettings() {
        Button buttonSettings = new Button(new Icon(VaadinIcon.COG_O));
        return buttonSettings;
    }

    private TextField text() {
        TextField text = new TextField();
        text.setPlaceholder("Поиск");
        text.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        text.setWidth("300px");
        return text;
    }

    private TextField textField() {
        TextField textField = new TextField("", "1-1 из 1");
        textField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        return textField;
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
        valueSelect.setItems("Изменить");
        valueSelect.setValue("Изменить");
        valueSelect.setWidth("130px");
        return valueSelect;
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upperLayout = new HorizontalLayout();
        upperLayout.add(buttonQuestion(), title(), buttonRefresh(), buttonUnit(), buttonFilter(), text(), numberField(),
                valueSelect(), buttonSettings());
        upperLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upperLayout;
    }

}