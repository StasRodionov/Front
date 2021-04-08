package com.trade_accounting.components.profile;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.ValidTextField;
import com.trade_accounting.models.dto.EmployeeDto;
import com.trade_accounting.models.dto.ImageDto;
import com.trade_accounting.models.dto.PositionDto;
import com.trade_accounting.models.dto.RoleDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.CurrencyService;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.ImageService;
import com.trade_accounting.services.interfaces.PositionService;
import com.trade_accounting.services.interfaces.RoleService;
import com.trade_accounting.services.interfaces.UnitService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.board.Row;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Route(value = "profile/settings", layout = AppView.class)
@PageTitle("Учетная запись")
@Slf4j
public class ProfileSettingsView extends VerticalLayout {

    private Long id;

    private ValidTextField firstName = new ValidTextField(true, "Имя");

    private ValidTextField middleName = new ValidTextField(false, "Отчество");

    private ValidTextField lastName = new ValidTextField(true, "Фамилия");

    private ValidTextField phone = new ValidTextField(true, "Телефон");

    private ValidTextField email = new ValidTextField(true, "E-mail");

    private ValidTextField position = new ValidTextField(false, "Должность");

    private ValidTextField inn = new ValidTextField(true, "ИНН");

    private PasswordField password = new PasswordField();

    private final String labelWidth = "100px";

    private final String fieldWidth = "400px";

    private Div div;

    private final EmployeeService employeeService;

    private final PositionService positionService;

    private RoleService roleService;

    private ImageService imageService;

    private EmployeeDto employeeDto;

    private ImageDto imageDto;

    private Set<RoleDto> roles;

    private Component avatarContainer;

    private Image avatar = new Image();

    public ProfileSettingsView( EmployeeService employeeService,
                                PositionService positionService){
        this.employeeService = employeeService;
        this.positionService = positionService;
        div = new Div();
        add(header());
        add(upperLayout(), lowerLayout());
        setAlignItems(Alignment.CENTER);
    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        header.add(addButtonSave(), getCancelButton());
        return header;
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upperLayout = new HorizontalLayout();
        upperLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upperLayout;
    }

    private HorizontalLayout lowerLayout() {
        HorizontalLayout lowerLayout = new HorizontalLayout();
//        div.removeAll();

        add(addLoginInfo(), addPasswordButton());
        div.add(
                addEmployeeFirstName(),
                addEmployeeMiddleName(),
                addEmployeeLastName(),
                addEmployeeEmail(),
                addEmployeePhone(),
                addEmployeePosition(),
                addEmployeeInn()
        );
        add(div);
        return lowerLayout;
    }

    private Component addLoginInfo() {
        TextField login = new TextField();
        Label label = new Label("Логин");
        label.setWidth(labelWidth);
        login.setWidth(fieldWidth);
        login.setValue(employeeService.getPrincipal().getEmail());
        login.setReadOnly(true);
        HorizontalLayout addEmployeePasswordAddLayout = new HorizontalLayout(label, login);
        return addEmployeePasswordAddLayout;
    }

    private Component addPasswordButton() {
        Button changePassword = new Button("Изменить пароль");
        changePassword.setHeightFull();
        changePassword.setWidth("230px");
        changePassword.addClickListener(event -> {
            ProfileSettingsModalWindow passwordChangeModal = new ProfileSettingsModalWindow(employeeService);
            passwordChangeModal.open();
                });
        return changePassword;
    }

    private Component addEmployeeEmail() {
        Label label = new Label("E-mail");
        label.setWidth(labelWidth);
        email.addValidator(new EmailValidator("Введите правильно адрес электронной почты!"));
        email.setWidth(fieldWidth);
        email.setValue(employeeService.getPrincipal().getEmail());
        email.setPlaceholder("Введите е-mail");
        email.setRequired(true);
        email.setHelperText("При изменении email требуется повторный логин");
        HorizontalLayout addEmployeeEmailAddLayout = new HorizontalLayout(label, email);
        return addEmployeeEmailAddLayout;
    }

    private Component addEmployeePhone() {
        Label label = new Label("Телефон");
        label.setWidth(labelWidth);
        phone.addValidator(new RegexpValidator("Введите правильно номер телефона!", "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$"));
        phone.setWidth(fieldWidth);
        phone.setValue(employeeService.getPrincipal().getPhone());
        phone.setPlaceholder("Введите номер телефона");
        phone.setRequired(true);
        HorizontalLayout addEmployeeInnAddLayout = new HorizontalLayout(label, phone);
        return addEmployeeInnAddLayout;
    }

    private Component addEmployeeInn() {
        Label label = new Label("ИНН");
        label.setWidth(labelWidth);
        inn.setPattern("^[\\d+]{12}$");
        inn.addValidator(new RegexpValidator("Введите правильно ИНН!", "^[\\d+]{12}$"));
        inn.setWidth(fieldWidth);
        inn.setValue(employeeService.getPrincipal().getInn());
        inn.setPlaceholder("Введите ИНН");
        inn.setRequired(true);
        HorizontalLayout addEmployeeInnAddLayout = new HorizontalLayout(label, inn);
        return addEmployeeInnAddLayout;
    }

    private Component addEmployeeLastName() {
        Label label = new Label("Фамилия");
        label.setWidth(labelWidth);
        lastName.addValidator(new StringLengthValidator("Введите правильно фамилию!", 1, 50));
        lastName.setWidth(fieldWidth);
        lastName.setValue(employeeService.getPrincipal().getLastName());
        lastName.setPlaceholder("Введите фамилию");
        lastName.setRequired(true);
        HorizontalLayout lastNameLayout = new HorizontalLayout(label, lastName);
        return lastNameLayout;
    }

    private Component addEmployeeFirstName() {
        Label label = new Label("Имя");
        label.setWidth(labelWidth);
        firstName.addValidator(new StringLengthValidator("Введите правильно имя!", 1, 50));
        firstName.setWidth(fieldWidth);
        firstName.setValue(employeeService.getPrincipal().getFirstName());
        firstName.setPlaceholder("Введите имя");
        firstName.setRequired(true);
        HorizontalLayout firstNameLayout = new HorizontalLayout(label, firstName);
        return firstNameLayout;
    }

    private Component addEmployeeMiddleName() {
        Label label = new Label("Отчество");
        label.setWidth(labelWidth);
        middleName.setWidth(fieldWidth);
        middleName.setPlaceholder("Введите отчество");
        middleName.setValue(employeeService.getPrincipal().getMiddleName());
        HorizontalLayout middleNameLayout = new HorizontalLayout(label, middleName);
        return middleNameLayout;
    }

    private Component addEmployeePosition() {
        Label label = new Label("Должность");
        label.setWidth(labelWidth);
        position.setWidth(fieldWidth);
        position.setPlaceholder("Введите должность");
        position.setValue(employeeService.getPrincipal().getPositionDto().getName());
        HorizontalLayout positionLayout = new HorizontalLayout(label, position);
        return positionLayout;
    }

    private Component addButtonSave() {
        Button buttonSave = new Button("Сохранить");
        buttonSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonSave.addClickListener(click -> {
                log.info("Вы нажали кнопку для обновления сотрудника!");
                EmployeeDto updateEmployeeDto = setEmployeeDto();
                employeeService.update(updateEmployeeDto);
        });
        return buttonSave;
    }

    private EmployeeDto setEmployeeDto() {
        EmployeeDto updateEmployeeDto = employeeService.getPrincipal();
        updateEmployeeDto.setFirstName(firstName.getValue());
        updateEmployeeDto.setLastName(lastName.getValue());
        updateEmployeeDto.setMiddleName(middleName.getValue());
        updateEmployeeDto.setEmail(email.getValue());
        PositionDto updatePosition = positionService.getByName(position.getValue());
        if (updatePosition.getName() != null && updatePosition.getName().equals(position.getValue())) {
            updateEmployeeDto.setPositionDto(updatePosition);
        } else {
            PositionDto newPosition = new PositionDto();
            newPosition.setName(position.getValue());
            positionService.create(newPosition);
            updateEmployeeDto.setPositionDto(positionService.getByName(position.getValue()));
        }
        updateEmployeeDto.setInn(inn.getValue());
        updateEmployeeDto.setPhone(phone.getValue());
        return updateEmployeeDto;
    }

    private Button getCancelButton() {
        return new Button("Закрыть", event -> UI.getCurrent().navigate("profile"));
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }

}
