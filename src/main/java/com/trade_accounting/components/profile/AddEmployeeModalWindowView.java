package com.trade_accounting.components.profile;

import com.trade_accounting.models.dto.EmployeeDto;
import com.trade_accounting.models.dto.ImageDto;
import com.trade_accounting.models.dto.RoleDto;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.ImageService;
import com.trade_accounting.services.interfaces.RoleService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class AddEmployeeModalWindowView extends Dialog {

    private Long id;

    private TextField firstNameAdd = new TextField();

    private TextField middleNameAdd = new TextField();

    private TextField lastNameAdd = new TextField();

    private TextField phoneAdd = new TextField();

    private TextField emailAdd = new TextField();

    private TextArea descriptionAdd = new TextArea();

    private TextField innAdd = new TextField();

    private PasswordField passwordAdd = new PasswordField();

    private Select<RoleDto> rolesSelect = new Select<>();

    private TextField imageAdd = new TextField();

    private final String labelWidth = "100px";

    private final String fieldWidth = "400px";

    private Div div;

    private final EmployeeService employeeService;

    private RoleService roleService;

    private ImageService imageService;

    private EmployeeDto employeeDto;

    private ImageDto imageDto;

    private Set<RoleDto> roles;

    private final String pathImageSave = "src/main/resources/images/employee/";

    private String pathImageResult;

    public AddEmployeeModalWindowView(EmployeeDto employeeDto,
                                      EmployeeService employeeService,
                                      RoleService roleService,
                                      ImageService imageService,
                                      ImageDto imageDto) {
        this.employeeService = employeeService;
        this.roleService = roleService;
        this.imageService = imageService;
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

            if (imageDto != null) {
                imageAdd.setValue(getFieldValueNotNull(employeeDto.getImageDto().getImageUrl()));
            }
        }

        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        add(title(), header(), lowerLayout());
        add(upperLayout(), lowerLayout());
    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        header.add(addButtonShow(), getCancelButton(), getDeleteButton(), getImageButton());
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
                rolesSelect(),
                addEmployeeImage()

        );
        add(div);
        return lowerLayout;
    }

    private Component addEmployeeImage() {
        Label label = new Label("Фото профиля");
        label.setWidth(labelWidth);
        imageAdd.setWidth(fieldWidth);
        imageAdd.setPlaceholder("Добавьте фото профиля");
        HorizontalLayout addEmployeeImageAddLayout = new HorizontalLayout(label, imageAdd);
        return addEmployeeImageAddLayout;
    }

//    private Component addEmployeeImage() {
//        Label label = new Label("Фото профиля");
//        label.setWidth(labelWidth);
//        Image image = new Image();
//        if (pathImageResult != null) {
//            image.setSrc(pathImageResult);
//        }
//        imageAdd.setWidth(fieldWidth);
//        imageAdd.add(image);
//        HorizontalLayout addEmployeeImageAddLayout = new HorizontalLayout(label, image);
//        return addEmployeeImageAddLayout;
//    }

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
                EmployeeDto newEmployeeDto = setEmployeeDto(null);
                employeeService.create(newEmployeeDto);
            } else {
                System.out.println("Вы нажали кнопку для обновления сотрудника!");
                EmployeeDto updateEmployeeDto = setEmployeeDto(id);
                employeeService.update(updateEmployeeDto);
            }
            div.removeAll();
            close();
        });
        HorizontalLayout addButtonShowLayout = new HorizontalLayout(addButtonShow);
        return addButtonShowLayout;
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
        updateEmployeeDto.setImageDto(getImages());
        return updateEmployeeDto;
    }

    private Set<RoleDto> getRoles() {
        Set<RoleDto> roleDtos = new HashSet<RoleDto>();
        roleDtos.add(rolesSelect.getValue());
        return roleDtos;
    }

    private ImageDto getImages() {
        ImageDto updateImageDto = new ImageDto();
        updateImageDto.setImageUrl(pathImageResult);
        return updateImageDto;
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

    private Component getImageButton() {
        Button imageButton = new Button("Добавить фото");
        Dialog dialog = new Dialog();
        MemoryBuffer memoryBuffer = new MemoryBuffer();
        Upload upload = new Upload(memoryBuffer);

        upload.addFinishedListener(event -> {
            pathImageResult = pathImageSave + event.getFileName();
            File newImage = new File(pathImageResult);
            ImageDto imageResult = new ImageDto();
            imageResult.setImageUrl(pathImageResult);

            try (InputStream inputStream = memoryBuffer.getInputStream();
                FileOutputStream fos = new FileOutputStream(newImage)) {
                    newImage.createNewFile();
                    fos.write(inputStream.readAllBytes());
                    fos.flush();
                    imageService.create(imageResult);
                    log.info("Фото профиля успешно загружено");
            } catch (IOException e) {
                log.error("При загрузке фото профиля произошла ошибка");
            }
            dialog.close();
        });

        dialog.add(upload);
        imageButton.addClickListener(x -> dialog.open());
        return imageButton;
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