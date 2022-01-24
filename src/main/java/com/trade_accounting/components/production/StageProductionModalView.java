package com.trade_accounting.components.production;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.controllers.dto.DepartmentDto;
import com.trade_accounting.controllers.dto.EmployeeDto;
import com.trade_accounting.controllers.dto.StagesProductionDto;
import com.trade_accounting.services.interfaces.DepartmentService;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.StagesProductionService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.List;

@UIScope
@SpringComponent
public class StageProductionModalView extends Dialog {

    private final StagesProductionService stagesProductionService;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final Notifications notifications;
    private Long id;
    private final TextArea textName = new TextArea();
    private final TextArea textDescription = new TextArea();
    private final ComboBox<EmployeeDto> employeeDtoComboBox = new ComboBox<>();
    private final ComboBox<DepartmentDto> departmentDtoComboBox = new ComboBox<>();

    private final Binder<StagesProductionDto> stagesProductionDtoBinder =
                                    new Binder<>(StagesProductionDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";


    public StageProductionModalView(StagesProductionService stagesProductionService,
                                    EmployeeService employeeService,
                                    DepartmentService departmentService,
                                    Notifications notifications) {

        this.stagesProductionService = stagesProductionService;
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.notifications = notifications;
        setSizeFull();
        add(headerLayout(), formLayout());
    }


    public void setStagesProductionEdit(StagesProductionDto editDto) {
        id = editDto.getId();
        textName.setValue(editDto.getName());
        textDescription.setValue(editDto.getDescription());
        employeeDtoComboBox.setValue(employeeService.getById(editDto.getEmployeeId()));
        departmentDtoComboBox.setValue(departmentService.getById(editDto.getDepartmentId()));
    }

    private HorizontalLayout headerLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(title(), saveButton(), closeButton());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(nameAndDescriptionLayout(), formLayoutDepartmentAndEmployee());
        return horizontalLayout;
    }

    private H2 title() {
        return new H2("Этап");
    }

    private void clearAllFieldsModalView() {
        employeeDtoComboBox.setValue(null);
        departmentDtoComboBox.setValue(null);
        textName.setValue("");
        textDescription.setValue("");
    }

    private Button saveButton() {
        return new Button("Сохранить", e -> {
            if (!stagesProductionDtoBinder.validate().isOk()) {
                stagesProductionDtoBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
                StagesProductionDto dto = new StagesProductionDto();
                dto.setId(id);
                dto.setDepartmentDto(departmentService.getById(departmentDtoComboBox.getValue().getId()));
                dto.setEmployeeDto(employeeService.getById(employeeDtoComboBox.getValue().getId()));
                dto.setName(textName.getValue());
                dto.setDescription(textDescription.getValue());
                dto.setDepartmentId(dto.getDepartmentDto().getId());
                dto.setEmployeeId(dto.getEmployeeDto().getId());
                if (id != null) {
                    stagesProductionService.update(dto);
                } else {
                    stagesProductionService.create(dto);
                }
                UI.getCurrent().navigate("stages");
                close();
                clearAllFieldsModalView();
                notifications.infoNotification(String.format("Этап %s сохранен", dto.getName()));
            }
        });
    }

    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> {
            close();
            clearAllFieldsModalView();
        });
        return button;
    }

    private HorizontalLayout departmentConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<DepartmentDto> list = departmentService.getAll();
        if (list != null) {
            departmentDtoComboBox.setItems(list);
        }
        departmentDtoComboBox.setItemLabelGenerator(DepartmentDto::getName);
        departmentDtoComboBox.setWidth("350px");
        Label label = new Label("Отдел");
        label.setWidth("150px");
        horizontalLayout.add(label, departmentDtoComboBox);
        stagesProductionDtoBinder.forField(departmentDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(StagesProductionDto::getDepartmentDto, StagesProductionDto::setDepartmentDto);
        return horizontalLayout;
    }

    private HorizontalLayout employeeConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<EmployeeDto> list = employeeService.getAll();
        if (list != null) {
            employeeDtoComboBox.setItems(list);
        }
        employeeDtoComboBox.setItemLabelGenerator(EmployeeDto::getFirstName);
        employeeDtoComboBox.setWidth("350px");
        Label label = new Label("Сотрудник");
        label.setWidth("150px");
        horizontalLayout.add(label, employeeDtoComboBox);
        stagesProductionDtoBinder.forField(employeeDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(StagesProductionDto::getEmployeeDto, StagesProductionDto::setEmployeeDto);
        return horizontalLayout;
    }

    private VerticalLayout formLayoutDepartmentAndEmployee() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(departmentConfigure(), employeeConfigure());
        return verticalLayout;
    }

    private HorizontalLayout nameConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Наименование");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("75px");
        horizontalLayout.add(label, textName);
        stagesProductionDtoBinder.forField(textName);
        return horizontalLayout;
    }

    private HorizontalLayout descriptionConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Описание");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("200px");
        horizontalLayout.add(label, textDescription);
        stagesProductionDtoBinder.forField(textName);
        return horizontalLayout;
    }

    private VerticalLayout nameAndDescriptionLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(nameConfigure(), descriptionConfigure());
        return verticalLayout;
    }
}
