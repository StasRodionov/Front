package com.trade_accounting.components.modal_windows;

import com.trade_accounting.models.dto.EmployeeDto;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import javax.validation.constraints.Pattern;

public class AddEmployeeModalWindowView extends Dialog {

    private TextField firstNameAdd = new TextField();

    private TextField middleNameAdd = new TextField();

    private TextField lastNameAdd = new TextField();

    private TextField phoneAdd = new TextField();

    private TextField emailAdd = new TextField();

    private TextArea descriptionAdd = new TextArea();

    private TextField innAdd = new TextField();

    private Div div;

    private final EmployeeService employeeService;

    public AddEmployeeModalWindowView(EmployeeService employeeService){
        this.employeeService = employeeService;
        div = new Div();
        add(upperLayout(), lowerLayout());
    }

    private HorizontalLayout upperLayout(){
        HorizontalLayout upperLayout = new HorizontalLayout();
//        upperLayout.add(buttonQuestion(),title(), buttonRefresh(), buttonUnit(), buttonFilter(), text(), numberField(),
//                valueSelect(), buttonSettings());
        upperLayout.add(new Label("Добавление сотрудника"));
        upperLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upperLayout;
    }

    private HorizontalLayout lowerLayout(){
        HorizontalLayout lowerLayout = new HorizontalLayout();
//        lowerLayout.add(new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT)),
//                new Button(new Icon(VaadinIcon.ANGLE_LEFT)),
//                textField(),
//                new Button(new Icon(VaadinIcon.ANGLE_RIGHT)),
//                new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT)));
        div.removeAll();
        div.add(addEmployeeLastName(),
                addEmployeeFirstName(),
                addEmployeeMiddleName(),
//                addEmployeeSortNumber(),
                addEmployeeEmail(),
                addEmployeePhone(),
                addEmployeeInn(),
                addEmployeeDescription(),
                addButtonShow()
//                addEmployeePassword()
        );

        add(div);
        return lowerLayout;
    }

    private Component addEmployeeEmail() {
        HorizontalLayout addEmployeeEmailAddLayout = new HorizontalLayout(new Label("E-mail"), emailAdd);
        return addEmployeeEmailAddLayout;
    }

    private Component addEmployeeDescription() {
        HorizontalLayout addEmployeeInnAddLayout = new HorizontalLayout(new Label("Описание"), descriptionAdd);
        return addEmployeeInnAddLayout;
    }

    private Component addEmployeePhone() {
        HorizontalLayout addEmployeeInnAddLayout = new HorizontalLayout(new Label("Телефон"), phoneAdd);
        return addEmployeeInnAddLayout;
    }

    private Component addEmployeeInn() {
        HorizontalLayout addEmployeeInnAddLayout = new HorizontalLayout(new Label("ИНН"), innAdd);
        return addEmployeeInnAddLayout;
    }

//    private Component addEmployeeSortNumber() {
//        TextField sortNumberAdd = new TextField();
//        HorizontalLayout addEmployeeSortNumberLayout = new HorizontalLayout(new Label("SortNumber сотрудника"), sortNumberAdd);
//        return addEmployeeSortNumberLayout;
//    }
    private Component addEmployeeLastName() {

        HorizontalLayout lastNameLayout = new HorizontalLayout(new Label("Фамилия сотрудника"), lastNameAdd);
        return lastNameLayout;
    }

    private Component addEmployeeFirstName() {
        HorizontalLayout firstNameLayout = new HorizontalLayout(new Label("Имя сотрудника"), firstNameAdd);
        return firstNameLayout;
    }

    private Component addEmployeeMiddleName() {
        HorizontalLayout middleNameLayout = new HorizontalLayout(new Label("Отчество сотрудника"), middleNameAdd);
        return middleNameLayout;
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

            employeeService.create(newEmployeeDto);
            div.removeAll();
            close();
        });
        HorizontalLayout addButtonShowLayout = new HorizontalLayout(addButtonShow);
        return addButtonShowLayout;
    }
}
