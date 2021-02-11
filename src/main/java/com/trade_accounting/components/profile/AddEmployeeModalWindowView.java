package com.trade_accounting.components.profile;

import com.trade_accounting.components.util.ValidTextField;
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
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class AddEmployeeModalWindowView extends Dialog {

    private Long id;

    private ValidTextField firstNameAdd = new ValidTextField(true, "Имя");

    private ValidTextField middleNameAdd = new ValidTextField();

    private ValidTextField lastNameAdd = new ValidTextField(true, "Фамилия");

    private ValidTextField phoneAdd = new ValidTextField(true, "Телефон");

    private ValidTextField emailAdd = new ValidTextField(true, "Адрес электронной почты");

    private TextArea descriptionAdd = new TextArea();

    private ValidTextField innAdd = new ValidTextField(true, "ИНН");

    private PasswordField passwordAdd = new PasswordField();

    private Select<RoleDto> rolesSelect = new Select<>();

    private final String labelWidth = "100px";

    private final String fieldWidth = "400px";

    private Div div;

    private final EmployeeService employeeService;

    private RoleService roleService;

    private EmployeeDto employeeDto;

    private Set<RoleDto> roles;

    public AddEmployeeModalWindowView(EmployeeDto employeeDto, EmployeeService employeeService, RoleService roleService) {
        this.employeeService = employeeService;
        this.roleService = roleService;
        div = new Div();
        if (employeeDto != null) {
            this.employeeDto = employeeDto;
            id = employeeDto.getId();
            firstNameAdd.setValue(getFieldValueNotNull(employeeDto.getFirstName()));
            middleNameAdd.setValue(getFieldValueNotNull(employeeDto.getMiddleName()));
            lastNameAdd.setValue(getFieldValueNotNull(employeeDto.getLastName()));
            phoneAdd.setValue(getFieldValueNotNull(employeeDto.getPhone()));
            emailAdd.setValue(getFieldValueNotNull(employeeDto.getEmail()));
            innAdd.setValue(getFieldValueNotNull(employeeDto.getInn()));
            descriptionAdd.setValue(getFieldValueNotNull(employeeDto.getDescription()));
            passwordAdd.setValue(getFieldValueNotNull(employeeDto.getPassword()));
            roles = employeeDto.getRoleDto();
        }
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        add(title(), header(), lowerLayout());
        add(upperLayout(), lowerLayout());
    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        header.add(addButtonShow(), getCancelButton(), getDeleteButton());
        return header;
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upperLayout = new HorizontalLayout();
        upperLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upperLayout;
    }

    private HorizontalLayout lowerLayout() {
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
        passwordAdd.setRequired(true);
        HorizontalLayout addEmployeePasswordAddLayout = new HorizontalLayout(label, passwordAdd);
        return addEmployeePasswordAddLayout;
    }

    private Component addEmployeeEmail() {
        Label label = new Label("Email");
        label.setWidth(labelWidth);
        emailAdd.setWidth(fieldWidth);
        emailAdd.setPlaceholder("Введите Email");
        emailAdd.addValidator(new EmailValidator("Введите правильно адрес электронной почты!"));
        emailAdd.setRequired(true);
        if(employeeDto!=null) {
            emailAdd.setValue(employeeDto.getEmail());
        }
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
        phoneAdd.addValidator(new RegexpValidator("Введите правильно номер телефона", "^[\\d+]{11}$"));
        phoneAdd.setRequired(true);
        if(employeeDto!=null) {
            phoneAdd.setValue(employeeDto.getPhone());
        }
        HorizontalLayout addEmployeeInnAddLayout = new HorizontalLayout(label, phoneAdd);
        return addEmployeeInnAddLayout;
    }

    private Component addEmployeeInn() {
        Label label = new Label("ИНН");
        label.setWidth(labelWidth);
        innAdd.setWidth(fieldWidth);
        innAdd.setPlaceholder("Введите ИНН");
        innAdd.setPattern("^[\\d+]{12}$");
        innAdd.addValidator(new RegexpValidator("Введите правильно ИНН", "^[\\d+]{12}$"));
        innAdd.setRequired(true);
        if(employeeDto!=null) {
            innAdd.setValue(employeeDto.getInn());
        }
        HorizontalLayout addEmployeeInnAddLayout = new HorizontalLayout(label, innAdd);
        return addEmployeeInnAddLayout;
    }

    private Component addEmployeeLastName() {
        Label label = new Label("Фамилия");
        label.setWidth(labelWidth);
        lastNameAdd.addValidator(new StringLengthValidator("Введите правильно фамилию", 1, 50));
        lastNameAdd.setWidth(fieldWidth);
        lastNameAdd.setPlaceholder("Введите фамилию");
        lastNameAdd.setRequired(true);
        if(employeeDto!=null) {
            lastNameAdd.setValue(employeeDto.getLastName());
        }
        HorizontalLayout lastNameLayout = new HorizontalLayout(label, lastNameAdd);
        return lastNameLayout;
    }

    private Component addEmployeeFirstName() {
        Label label = new Label("Имя");
        label.setWidth(labelWidth);
        firstNameAdd.setWidth(fieldWidth);
        firstNameAdd.setPlaceholder("Введите Имя");
        firstNameAdd.setRequired(true);
        firstNameAdd.addValidator(new StringLengthValidator("Введите правильно имя", 1, 50));
        if(employeeDto!=null) {
            firstNameAdd.setValue(employeeDto.getFirstName());
        }
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

        List<RoleDto> rolesDto = roleService.getAll();

        rolesSelect.setItemLabelGenerator(RoleDto::getName);
        rolesSelect.setItems(rolesDto);
        if (roles != null) {
            for (RoleDto role : roles)
                rolesSelect.setValue(role);
        }
        rolesSelect.setWidth(fieldWidth);
        Label label = new Label("Роль");
        label.setWidth(labelWidth);
        horizontalLayout.add(label, rolesSelect);
        return horizontalLayout;
    }

    private Component addButtonShow() {
        Button addButtonShow = new Button("Сохранить");
        addButtonShow.addClickListener(click -> {
            if (id == null) {
                System.out.println("Вы нажали кнопку для сохранения нового сотрудника!");

                if (!lastNameAdd.isRequiredVerify()||!firstNameAdd.isRequiredVerify()||!emailAdd.isRequiredVerify()
                        ||!phoneAdd.isRequiredVerify()||!innAdd.isRequiredVerify()){
                   return;
                }

                if (emailAdd.isInvalid() || innAdd.isInvalid() || phoneAdd.isInvalid() || lastNameAdd.isInvalid() || firstNameAdd.isInvalid()) {
                    System.out.println("ошибка в введенных данных на форме!");
                } else {
                    EmployeeDto newEmployeeDto = setEmployeeDto(null);
                    employeeService.create(newEmployeeDto);
                    div.removeAll();
                    close();
                }
            } else {
                log.info("Вы нажали кнопку для обновления сотрудника!");
                EmployeeDto updateEmployeeDto = setEmployeeDto(id);
                employeeService.update(updateEmployeeDto);
                div.removeAll();
                close();
            }
        });
        return addButtonShow;
    }

    private EmployeeDto setEmployeeDto(Long id) {
        EmployeeDto updateEmployeeDto = new EmployeeDto();
        if (id != null) {
            updateEmployeeDto.setId(id);
        }
        updateEmployeeDto.setFirstName(firstNameAdd.getValue());
        updateEmployeeDto.setLastName(lastNameAdd.getValue());
        updateEmployeeDto.setMiddleName(middleNameAdd.getValue());
        updateEmployeeDto.setEmail(emailAdd.getValue());
        updateEmployeeDto.setInn(innAdd.getValue());
        updateEmployeeDto.setPhone(phoneAdd.getValue());
        updateEmployeeDto.setDescription(descriptionAdd.getValue());
        updateEmployeeDto.setPassword(passwordAdd.getValue());
        updateEmployeeDto.setRoleDto(getRoles());
        return updateEmployeeDto;
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

    private Component getDeleteButton() {
        Button deleteButton = new Button("Удалить", event -> {
            employeeService.deleteById(id);
            close();
        });
        return deleteButton;
    }

    private H2 title() {
        H2 title = new H2("Добавление сотрудника");
        title.setHeight("2.2em");
        return title;
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }
}