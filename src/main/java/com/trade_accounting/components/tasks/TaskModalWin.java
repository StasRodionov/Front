package com.trade_accounting.components.tasks;


import com.trade_accounting.models.dto.EmployeeDto;
import com.trade_accounting.models.dto.TaskDto;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.TaskService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;


import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskModalWin extends Dialog {

    private final TaskService taskService;
    private final TaskDto taskDto;
    private final EmployeeService employeeService;

    private final TextArea description = new TextArea("Описание задачи");
    private final Checkbox completed = new Checkbox("Выполнена");
    private final DateTimePicker creationDataTime = new DateTimePicker();
    private final DateTimePicker deadlineDateTime = new DateTimePicker();
    private final ComboBox<EmployeeDto> employeeDtoSelect = new ComboBox<>();

    private final Binder<TaskDto> taskDtoBinder = new Binder<>(TaskDto.class);

    private List<EmployeeDto> employeeDtoList;

    public TaskModalWin(TaskService taskService, TaskDto taskDto, EmployeeService employeeService) {
        this.taskService = taskService;
        this.taskDto = taskDto;
        this.employeeService = employeeService;

        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);
        setWidth("800px");
        setHeight("450px");

        add(getHeader());
        add(getContent());

        description.setValue(getFieldValueNotNull(taskDto.getDescription()));
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }

    private Component getHeader() {
        HorizontalLayout header = new HorizontalLayout();
        Button saveButton = new Button("Сохранить", event -> {
            saveFields(taskDto);
            taskService.create(taskDto);
            close();
        });
        Button closeButton = new Button("Закрыть", e -> {
            close();
        });
        header.add(saveButton, closeButton);

        return header;
    }

    private Component getContent() {
        HorizontalLayout contentField = new HorizontalLayout();

        description.getStyle().set("maxHeight", "400px");
        description.setHeight("300px");
        description.setWidth("450px");

        taskDtoBinder.forField(description)
                .asRequired("Не заполнено!")
                .bind("description");

        contentField.add(description);
        contentField.add(componentsFields());
        return contentField;
    }

    private Component componentsFields() {
        VerticalLayout verticalComponents = new VerticalLayout();

        creationDataTime.setLabel("Дата постановки задачи");
        verticalComponents.add(creationDataTime);
        deadlineDateTime.setLabel("Срок");
        verticalComponents.add(deadlineDateTime);

        Div div = new Div();
        Anchor contactor = new Anchor("/tasks", "контрагентом");
        Anchor document = new Anchor("/tasks", "документом");
        div.add("Связать с : ");
        div.add(contactor);
        div.add(", ");
        div.add(document);
        div.getElement().getStyle().set("font-size", "14px");

        verticalComponents.add(completed);
        verticalComponents.add(employeeSelect());
        verticalComponents.add(creationDataTime);
        verticalComponents.add(deadlineDateTime);

        verticalComponents.add(div);

        return verticalComponents;
    }

    private HorizontalLayout employeeSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        employeeDtoList = employeeService.getAll();
        if (employeeDtoList != null) {
            List<EmployeeDto> list = employeeDtoList;
            employeeDtoSelect.setItems(list);
        }
        employeeDtoSelect.setItemLabelGenerator(EmployeeDto::getLastName);
        employeeDtoSelect.setWidth(String.valueOf(300));
        Label label = new Label("Исполнитель");
        label.setWidth(String.valueOf(300));
        horizontalLayout.add(label, employeeDtoSelect);
        return horizontalLayout;
    }

    private void saveFields(TaskDto taskDto){
        taskDto.setCompleted(completed.getValue());
        taskDto.setDescription(description.getValue());
        taskDto.setCreationDateTime(creationDataTime.getValue().format(DateTimeFormatter.ofPattern("2021-06-06 09:03:49")));
        taskDto.setDeadlineDateTime(deadlineDateTime.getValue().format(DateTimeFormatter.ofPattern("2021-06-06 09:03:49")));
        taskDto.setEmployeeId(employeeDtoSelect.getValue().getId());
        taskDto.setTaskAuthorId(employeeService.getPrincipal().getId());
        taskDto.setTaskCommentsIds(List.of());
    }
}
