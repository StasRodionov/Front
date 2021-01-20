package com.trade_accounting.components;

import com.trade_accounting.models.dto.EmployeeDto;
import com.trade_accounting.services.interfaces.EmployeeService;
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


@Route(value = "employee", layout = AppView.class)
@PageTitle("Сотрудники")
public class EmployeeView extends VerticalLayout {

    private final EmployeeService employeeService;

    public EmployeeView(EmployeeService employeeService){
        this.employeeService = employeeService;
        add(upperLayout(), grid(employeeService), lowerLayout());
    }

    private Button buttonQuestion(){
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private Button buttonRefresh(){
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonUnit(){
        Button buttonUnit = new Button("Сотрудник", new Icon(VaadinIcon.PLUS_CIRCLE));
        return buttonUnit;
    }

    private Button buttonFilter(){
        Button buttonFilter = new Button("Фильтр");
        return buttonFilter;
    }

    private Button buttonSettings(){
        Button buttonSettings = new Button(new Icon(VaadinIcon.COG_O));
        return buttonSettings;
    }

    private TextField text(){
        TextField text = new TextField();
        text.setPlaceholder("Поиск");
        text.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        text.setWidth("300px");
        return text;
    }

    private TextField textField(){
        TextField textField = new TextField("", "1-1 из 1");
        textField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        return textField;
    }

    private H2 title(){
        H2 title = new H2("Сотрудники");
        title.setHeight("2.2em");
        return title;
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

    private Grid<EmployeeDto> grid(EmployeeService employeeService){
        Grid<EmployeeDto> grid = new Grid<>(EmployeeDto.class);
        grid.setItems(employeeService.getAll());

        grid.setColumns("lastName", "imageDto", "firstName",
                "middleName", "email", "phone", "description", "roleDto");

        grid.getColumnByKey("lastName").setHeader("Фамилия");
        grid.getColumnByKey("imageDto").setHeader("");
        grid.getColumnByKey("firstName").setHeader("Имя");
        grid.getColumnByKey("middleName").setHeader("Отчество");
        grid.getColumnByKey("email").setHeader("E-mail");
        grid.getColumnByKey("phone").setHeader("Телефон");
        grid.getColumnByKey("description").setHeader("Описание");
        grid.getColumnByKey("roleDto").setHeader("Роль");

        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setHeight("66vh");
        return grid;
    }

    private HorizontalLayout upperLayout(){
        HorizontalLayout upperLayout = new HorizontalLayout();
        upperLayout.add(buttonQuestion(),title(), buttonRefresh(), buttonUnit(), buttonFilter(), text(), numberField(),
                valueSelect(), buttonSettings());
        upperLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upperLayout;
    }

    private HorizontalLayout lowerLayout(){
        HorizontalLayout lowerLayout = new HorizontalLayout();
        lowerLayout.add(new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT)),
                new Button(new Icon(VaadinIcon.ANGLE_LEFT)),
                textField(),
                new Button(new Icon(VaadinIcon.ANGLE_RIGHT)),
                new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT)));
        return lowerLayout;
    }
}
