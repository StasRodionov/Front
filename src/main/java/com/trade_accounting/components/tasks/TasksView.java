package com.trade_accounting.components.tasks;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.TaskDto;
import com.trade_accounting.services.interfaces.TaskService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@SpringComponent
@UIScope
@Slf4j
@Route(value = "tasks", layout = AppView.class)
@PageTitle("Задачи")
public class TasksView extends VerticalLayout {

    private final TaskService taskService;
    private final List<TaskDto> data;
    private final Grid<TaskDto> grid = new Grid<>(TaskDto.class, false);
    private final GridFilter<TaskDto> filter = new GridFilter<>(grid);
    private final GridPaginator<TaskDto> paginator = new GridPaginator<>(grid, data, 10);



    @Autowired
    public TasksView(TaskService taskService) {
        this.taskService = taskService;
        this.data = getData();

        configureGrid();

        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(getToolBar(), filter, grid, paginator);
    }


    private void configureGrid() {
        grid.addColumn("id").setHeader("ID").setId("ID");
        grid.addColumn("description").setHeader("Описание").setId("Описание");
    }

    private HorizontalLayout getToolBar() {
        var toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), getTextTask(), getButtonRefresh(),
                getButtonCreateTask(), getButtonFilter(), getTextField());

        toolbar.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return toolbar;
    }

    private Button getButtonQuestion() {
        var buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        String notificationText = "Задачи помогают организовать работу. Их можно" +
                " ставить себе или другим сотрудникам, выполнение отслеживается по уведомлениям.\n" +
                "\nЗадачу можно создать из любого документа. Также можно настроить" +
                " автоматическое создание задач в рамках сценариев. Например, если" +
                " покупатель в течение недели не оплачивает счет, можно поставить " +
                "менеджеру задачу связаться с ним.\n\nЧитать инструкцию: Задачи";

        var notification = new Notification(
                notificationText, 3000, Notification.Position.BOTTOM_START);

        buttonQuestion.addClickListener(event -> notification.open());

        return buttonQuestion;
    }

    private H2 getTextTask() {
        final var textTask = new H2("Задачи");
        textTask.setHeight("2.2em");
        return textTask;
    }

    private Button getButtonRefresh() {
        final var buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
//        buttonRefresh.addClickListener(click -> reloadGrid());
        return buttonRefresh;
    }

    private Button getButtonCreateTask() {
        var buttonUnit = new Button("Задача", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickListener(click -> {
            var taskModalWin = new TaskModalWin();
            taskModalWin.open();
        });
        return buttonUnit;
    }

    private Button getButtonFilter() {
        var buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> {
        });
//        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private TextField getTextField() {
        var textField = new TextField();
        textField.setPlaceholder("Задача, комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");

        return textField;
    }

    private List<TaskDto> getData() {
        return taskService.getAll();
    }
}
