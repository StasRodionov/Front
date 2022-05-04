package com.trade_accounting.components.tasks;


import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.client.EmployeeDto;
import com.trade_accounting.models.dto.util.TaskCommentDto;
import com.trade_accounting.models.dto.util.TaskDto;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.util.TaskCommentService;
import com.trade_accounting.services.interfaces.util.TaskService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.trade_accounting.config.SecurityConstants.TASKS;

public class TaskModalWin extends Dialog {

    private final TaskService taskService;
    private final TaskDto taskDto;
    private final EmployeeService employeeService;
    private final ContractorService contractorService;
    private final TaskCommentService taskCommentService;

    private final TextArea description = new TextArea("Описание задачи");
    private final Checkbox completed = new Checkbox("Выполнена");
    private final TextArea idField = new TextArea();
    private final DateTimePicker creationDataTime = new DateTimePicker();
    private final DateTimePicker deadlineDateTime = new DateTimePicker();
    private final ComboBox<EmployeeDto> employeeDtoSelect = new ComboBox<>();
    private final ComboBox<ContractorDto> contractorDtoSelect = new ComboBox<>();
    private List<Label> commentsFields = new ArrayList<>();
    private TextField addCommentField = new TextField();
    private Component commentsBlock;

    private boolean flagUpdateTask = false;

    private final Binder<TaskDto> taskDtoBinder = new Binder<>(TaskDto.class);

    private List<EmployeeDto> employeeDtoList;
    private List<ContractorDto> contractorDtoList;

    public TaskModalWin(TaskService taskService, TaskDto taskDto,
                        EmployeeService employeeService, ContractorService contractorService, TaskCommentService taskCommentService) {
        this.taskService = taskService;
        this.taskDto = taskDto;
        this.employeeService = employeeService;
        this.contractorService = contractorService;
        this.taskCommentService = taskCommentService;
        this.commentsFields = fillCommentsList();

        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);
        setWidth("1200px");
        setHeight("750px");

        add(getHeader());
        add(getContent());
        commentsBlock = getComments(taskDto);
        add(commentsBlock);

        idField.setValue(getFieldValueNotNull(String.valueOf(taskDto.getId())));
        description.setValue(getFieldValueNotNull(taskDto.getDescription()));
        completed.setValue(taskDto.isCompleted());
        contractorDtoSelect.setValue(contractorService.getById(getLongIdNotNull(taskDto.getContractorId())));
        employeeDtoSelect.setValue(employeeService.getById(getLongIdNotNull(taskDto.getEmployeeId())));
        creationDataTime.setValue(LocalDateTime.parse(getFieldValueNotNull
                (taskDto.getCreationDataTimeNotNull().replaceAll(" ", "T"))));
        deadlineDateTime.setValue(LocalDateTime.parse(getFieldValueNotNull
                (taskDto.getDeadLineDataTimeNotNullPlus60min().replaceAll(" ", "T"))));
    }

    private String getFieldValueNotNull(String value) {
        return value == null ? "" : value;
    }
    private Long getLongIdNotNull(Long value) {
        return value == null ? employeeService.getPrincipal().getId() : value;
    }

    private Component getHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.add(getSaveButton(), getCancelButton());
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
        verticalComponents.add(contractorSelect());
        verticalComponents.add(employeeSelect());
        verticalComponents.add(creationDataTime);
        verticalComponents.add(deadlineDateTime);

        verticalComponents.add(div);

        return verticalComponents;
    }

    private Component getComments(TaskDto taskDto) {
        VerticalLayout verticalLayoutMain = new VerticalLayout();
        VerticalLayout verticalLayoutContent = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        if (taskDto.getId() == null){
            // если задача только создается, то taskDto пустой и комменты пока прикреплять не к чему,
            // поэтому возвращаем пустой layout
            return verticalLayoutMain;
        }

        String textForHeaderLabel = taskDto.getTaskCommentsIds().size() > 0 ? "Комментарии к задаче: " : "Комментариев пока что нет";
        Label labelHeader = new Label(textForHeaderLabel);
        verticalLayoutMain.add(labelHeader);

        commentsFields.forEach(label -> {
            verticalLayoutContent.add(label);
        });
        verticalLayoutMain.add(verticalLayoutContent);

        addCommentField.setPlaceholder("Написать комментарий");
        Button buttonAddComment = configureButtonAddComment();
        horizontalLayout.add(addCommentField, buttonAddComment);
        verticalLayoutMain.add(horizontalLayout);

        return verticalLayoutMain;
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

    private HorizontalLayout contractorSelect() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        contractorDtoList = contractorService.getAll();
        if (contractorDtoList != null) {
            List<ContractorDto> list = contractorDtoList;
            contractorDtoSelect.setItems(list);
        }
        contractorDtoSelect.setItemLabelGenerator(ContractorDto::getName);
        contractorDtoSelect.setWidth(String.valueOf(300));
        Label label = new Label("Контрагент");
        label.setWidth(String.valueOf(300));
        horizontalLayout.add(label, contractorDtoSelect);
        return horizontalLayout;
    }

    public Button getSaveButton() {
        if (getFieldValueNotNull(taskDto.getDescription()).isEmpty()){
            return new Button("Добавить", event -> {
                saveFields(taskDto);
                taskService.create(taskDto);
                close();
                UI.getCurrent().navigate(TASKS);
            });
        } else {
            return new Button("Изменить", event -> {
                saveFields(taskDto);
                taskService.update(taskDto);
                close();
                UI.getCurrent().navigate(TASKS);
            });
        }
    }

    private List<Label> fillCommentsList(){
        List<Label> result = new ArrayList<>();
        if (taskDto.getId() != null ){
            taskDto.getTaskCommentsIds().forEach(commentId -> {
                TaskCommentDto commentDto = taskCommentService.getById(commentId);
                EmployeeDto empDto = employeeService.getById(commentDto.getPublisherId());

                Label label = new Label();
                label.setText(commentDto.getPublishedDateTime() + ". " + empDto.getFirstName() + " " + empDto.getLastName()
                        + ": " + commentDto.getCommentContent());

                result.add(label);
            });
        }

        return result;
    }

    private Button configureButtonAddComment(){
        Button buttonAddComment = new Button("Опубликовать");

        buttonAddComment.addClickListener(e -> {
            if (!addCommentField.getValue().equals("")){
                TaskCommentDto taskCommentDto = buildCommentDto();
                taskCommentService.create(taskCommentDto);
                Long id = taskCommentService.getAll().stream()
                        .filter( x ->  x.getCommentContent().equals(taskCommentDto.getCommentContent()))
                        .filter( x ->  x.getPublisherId().equals(taskCommentDto.getPublisherId()))
                        .filter( x ->  x.getPublishedDateTime().equals(taskCommentDto.getPublishedDateTime()))
                        .filter( x ->  x.getTaskId().equals(taskCommentDto.getTaskId()))
                        .findFirst().get().getId();
                taskDto.getTaskCommentsIds().add(id);
                taskService.update(taskDto);
                updateComments();
                addCommentField.setValue("");
                addCommentField.setPlaceholder("Написать комментарий");
            }
        });

        return buttonAddComment;
    }

    private Button getCancelButton() {
        return new Button("Закрыть", event -> close());
    }

    private TaskCommentDto buildCommentDto() {
        TaskCommentDto taskCommentDto = new TaskCommentDto();
        taskCommentDto.setCommentContent(addCommentField.getValue());
        taskCommentDto.setPublisherId(employeeService.getPrincipal().getId());
        taskCommentDto.setPublishedDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));
        taskCommentDto.setTaskId(taskDto.getId());
        return taskCommentDto;
    }

    private void updateComments() {
        commentsFields = fillCommentsList();
        this.remove(commentsBlock);
        commentsBlock = getComments(taskDto);
        this.add(commentsBlock);

    }

    private void saveFields(TaskDto taskDto){
        taskDto.setCompleted(completed.getValue());
        taskDto.setDescription(description.getValue());
        taskDto.setCreationDateTime(creationDataTime.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));
        taskDto.setDeadlineDateTime(deadlineDateTime.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));
        taskDto.setEmployeeId(employeeDtoSelect.getValue().getId());
        taskDto.setContractorId(contractorDtoSelect.getValue().getId());
        taskDto.setTaskAuthorId(employeeService.getPrincipal().getId());
        if (taskDto.getTaskCommentsIds() == null){
            taskDto.setTaskCommentsIds(List.of());
        } else {
            taskDto.setTaskCommentsIds(taskDto.getTaskCommentsIds());
        }
    }
}
