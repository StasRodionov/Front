package com.trade_accounting.components.modal_windows;

import com.trade_accounting.models.dto.EmployeeDto;
import com.trade_accounting.models.dto.RoleDto;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.RoleService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddEmployeeModalWindowView extends Dialog {

    private TextField firstNameAdd = new TextField();

    private TextField middleNameAdd = new TextField();

    private TextField lastNameAdd = new TextField();

    private TextField phoneAdd = new TextField();

    private TextField emailAdd = new TextField();

    private TextArea descriptionAdd = new TextArea();

    private TextField innAdd = new TextField();

    private PasswordField passwordAdd = new PasswordField();

    private Select<RoleDto> rolesSelect = new Select<>();

    private final String labelWidth = "100px";

    private final String fieldWidth = "400px";

    private Div div;

    private final EmployeeService employeeService;

    private RoleService roleService;

    public AddEmployeeModalWindowView(EmployeeService employeeService, RoleService roleService){
        this.employeeService = employeeService;
        this.roleService = roleService;
        div = new Div();
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        add(title(), header(), lowerLayout());
        add(upperLayout(), lowerLayout());
    }

    private HorizontalLayout header(){
        HorizontalLayout header = new HorizontalLayout();
        header.add(addButtonShow(), getCancelButton());
        return header;
    }

    private HorizontalLayout upperLayout(){
        HorizontalLayout upperLayout = new HorizontalLayout();
        upperLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upperLayout;
    }

    private HorizontalLayout lowerLayout(){
        HorizontalLayout lowerLayout = new HorizontalLayout();
        div.removeAll();
        div.add(addEmployeeLastName(),
                addEmployeeFirstName(),
                addEmployeeMiddleName(),
                addEmployeeEmail(),
                addEmployeePhone(),
                addEmployeeInn(),
                addEmployeeDescription(),
                addEmployeePassword(),
                rolesSelect()

        );
        add(div);
        return lowerLayout;
    }

    private Component addEmployeePassword() {
        Label label = new Label("Пароль");
        label.setWidth(labelWidth);
        passwordAdd.setWidth(fieldWidth);
        passwordAdd.setPlaceholder("Введите пароль");
        HorizontalLayout addEmployeePasswordAddLayout = new HorizontalLayout(label, passwordAdd);
        return addEmployeePasswordAddLayout;
    }

    private Component addEmployeeEmail() {
        Label label = new Label("Email");
        label.setWidth(labelWidth);
        emailAdd.setWidth(fieldWidth);
        emailAdd.setPlaceholder("Введите Email");
        HorizontalLayout addEmployeeEmailAddLayout = new HorizontalLayout(label, emailAdd);
        return addEmployeeEmailAddLayout;
    }

    private Component addEmployeeDescription() {
        Label label = new Label("Описание");
        label.setWidth(labelWidth);
        descriptionAdd.setWidth(fieldWidth);
        descriptionAdd.setPlaceholder("Введите описания сотрудника");
        HorizontalLayout addEmployeeInnAddLayout = new HorizontalLayout(label, descriptionAdd);
        return addEmployeeInnAddLayout;
    }

    private Component addEmployeePhone() {
        Label label = new Label("Телефон");
        label.setWidth(labelWidth);
        phoneAdd.setWidth(fieldWidth);
        phoneAdd.setPlaceholder("Введите номер телефона");
        HorizontalLayout addEmployeeInnAddLayout = new HorizontalLayout(label, phoneAdd);
        return addEmployeeInnAddLayout;
    }

    private Component addEmployeeInn() {
        Label label = new Label("ИНН");
        label.setWidth(labelWidth);
        innAdd.setWidth(fieldWidth);
        innAdd.setPlaceholder("Введите ИНН");
        HorizontalLayout addEmployeeInnAddLayout = new HorizontalLayout(label, innAdd);
        return addEmployeeInnAddLayout;
    }

    private Component addEmployeeLastName() {
        Label label = new Label("Фамилия");
        label.setWidth(labelWidth);
        lastNameAdd.setWidth(fieldWidth);
        lastNameAdd.setPlaceholder("Введите фамилию");
        HorizontalLayout lastNameLayout = new HorizontalLayout(label, lastNameAdd);
        return lastNameLayout;
    }

    private Component addEmployeeFirstName() {
        Label label = new Label("Имя");
        label.setWidth(labelWidth);
        firstNameAdd.setWidth(fieldWidth);
        firstNameAdd.setPlaceholder("Введите Имя");
        HorizontalLayout firstNameLayout = new HorizontalLayout(label, firstNameAdd);
        return firstNameLayout;
    }

    private Component addEmployeeMiddleName() {
        Label label = new Label("Отчество");
        label.setWidth(labelWidth);
        middleNameAdd.setWidth(fieldWidth);
        middleNameAdd.setPlaceholder("Введите Отчество");
        HorizontalLayout middleNameLayout = new HorizontalLayout(label, middleNameAdd);
        return middleNameLayout;
    }

    private HorizontalLayout rolesSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        List<RoleDto> rolesDto =  roleService.getAll();

        rolesSelect.setItemLabelGenerator(RoleDto::getName);
        rolesSelect.setItems(rolesDto);

        rolesSelect.setWidth(fieldWidth);
        Label label = new Label("Роль");
        label.setWidth(labelWidth);
        horizontalLayout.add(label, rolesSelect);
        return horizontalLayout;
    }

    private Component addButtonShow() {
        Button addButtonShow = new Button("Добавить");
        addButtonShow.addClickListener(click -> {
            System.out.println("Вы нажали кнопку для сохранения нового  сотрудника!");

            EmployeeDto newEmployeeDto = new EmployeeDto();
            newEmployeeDto.setFirstName(firstNameAdd.getValue());
            newEmployeeDto.setLastName(lastNameAdd.getValue());
            newEmployeeDto.setMiddleName(middleNameAdd.getValue());
            newEmployeeDto.setEmail(emailAdd.getValue());
            newEmployeeDto.setInn(innAdd.getValue());
            newEmployeeDto.setPhone(phoneAdd.getValue());
            newEmployeeDto.setDescription(descriptionAdd.getValue());
            newEmployeeDto.setPassword(passwordAdd.getValue());
            newEmployeeDto.setRoleDto(getRoles());
            employeeService.create(newEmployeeDto);
            div.removeAll();
            close();
        });
        HorizontalLayout addButtonShowLayout = new HorizontalLayout(addButtonShow);
        return addButtonShowLayout;
    }

    private Set<RoleDto> getRoles() {
        Set<RoleDto> roleDtos = new HashSet<RoleDto>();
        roleDtos.add(rolesSelect.getValue());
        return roleDtos;
    }

    private Button getCancelButton() {
        Button cancelButton = new Button("Закрыть", event -> {
            close();
        });
        return cancelButton;
    }

    private H2 title(){
        H2 title = new H2("Добавление сотрудника");
        title.setHeight("2.2em");
        return title;
    }
}
