package com.trade_accounting.components.tasks;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.EmployeeDto;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.TaskDto;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.trade_accounting.services.interfaces.TaskCommentService;
import com.trade_accounting.services.interfaces.TaskService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

@SpringComponent
@UIScope
@Slf4j
@Route(value = "tasks", layout = AppView.class)
@PageTitle("Задачи")
public class TasksView extends VerticalLayout implements AfterNavigationObserver {

    private final TaskService taskService;
    private final InvoiceService invoiceService;
    private final Grid<TaskDto> grid = new Grid<>(TaskDto.class, false);
    private final GridFilter<TaskDto> filter;
    private final Notifications notifications;
    private final EmployeeService employeeService;
    private final ContractorService contractorService;
    private final TaskCommentService taskCommentService;
    private GridPaginator<TaskDto> paginator;

    private final TaskDto taskDto;

    @Autowired
    public TasksView(TaskService taskService, InvoiceService invoiceService, EmployeeService employeeService,
                     ContractorService contractorService, @Lazy Notifications notifications, TaskCommentService taskCommentService) {
        this.taskService = taskService;
        this.employeeService = employeeService;
        this.contractorService = contractorService;
        this.taskCommentService = taskCommentService;
        this.notifications = notifications;
        this.invoiceService = invoiceService;
        this.taskDto = new TaskDto();
        paginator = getPaginator();
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        add(getToolBar(), filter, grid, paginator);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.removeAllColumns();
        grid.addColumn(TaskDto::getId).setHeader("ID")
                .setKey("id").setId("ID");
        grid.addColumn(TaskDto::getDescription).setHeader("Описание")
                .setKey("description").setId("Описание");
        grid.addColumn(new ComponentRenderer<>(this::getIsCompleteIcon)).setHeader("Завершена")
                .setKey("completed").setId("Завершена");
        grid.addColumn(e -> contractorService.getById(e.getContractorId()).getName())
                .setKey("contractorId").setHeader("Контрагент").setId("Контрагент");
        grid.addColumn(e -> employeeService.getById(e.getEmployeeId()).getLastName())
                .setKey("employeeId").setHeader("Ответственный").setId("Ответственный");
        grid.addColumn(TaskDto::getDeadlineDateTime).setHeader("Срок")
                .setKey("deadLineDateTime").setId("Срок");
        grid.addColumn(TaskDto::getCreationDateTime).setHeader("Создано")
                .setKey("creationDateTime").setId("Создано");

        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addItemDoubleClickListener(event -> {
            TaskDto taskDto = event.getItem();
            TaskModalWin addTaskModalWin =
                    new TaskModalWin(taskService, taskDto, employeeService, contractorService, taskCommentService);
            addTaskModalWin.addDetachListener(e -> updateList());
            addTaskModalWin.getSaveButton();
            addTaskModalWin.open();
        });

        grid.setSortableColumns("id", "description", "completed", "contractorId", "employeeId", "deadLineDateTime", "creationDateTime");
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToComboBox("contractorId", ContractorDto::getName, contractorService.getAll());
        filter.setFieldToComboBox("employeeId", EmployeeDto::getLastName, employeeService.getAll());
        filter.onSearchClick(e -> paginator.setData(taskService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(taskService.getAll()));
    }

    private HorizontalLayout getToolBar() {
        var toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), getTextTask(), getButtonRefresh(),
                getButtonCreateTask(), getButtonFilter(), getTextField(),valueSelect());

        toolbar.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return toolbar;
    }

    private Select<String> valueSelect() {
        Select<String> select = new Select<>();
        List<String> listItems = new ArrayList<>();
        listItems.add("Изменить");
        listItems.add("Удалить");
        select.setItems(listItems);
        select.setValue("Изменить");
        select.setWidth("130px");
        select.addValueChangeListener(event -> {
            if (select.getValue().equals("Удалить")) {
                deleteSelectedInvoices();
                grid.deselectAll();
                select.setValue("Изменить");
                paginator.setData(getData());
            }
        });
        return select;
    }

    private void deleteSelectedInvoices() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (TaskDto taskDto : grid.getSelectedItems()) {
                taskService.deleteById(taskDto.getId());
                notifications.infoNotification("Выбранные задачи успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные задачи");
        }
    }

    private Button getButtonQuestion() {
        return Buttons.buttonQuestion(
                new VerticalLayout(
                        new Text("Задачи помогают организовать работу. Их можно " +
                                "ставить себе или другим сотрудникам, выполнение отслеживается по уведомлениям. " +
                                "Задачу можно создать из любого документа. Также можно настроить " +
                                "автоматическое создание задач в рамках сценариев. Например, если " +
                                "покупатель в течение недели не оплачивает счет, можно поставить " +
                                "менеджеру задачу связаться с ним."),
                        new Div(
                                new Text("Читать инструкцию: "),
                                new Anchor("#", "Задачи"))
                )
        );
    }

    private H2 getTextTask() {
        final var textTask = new H2("Задачи");
        textTask.setHeight("2.2em");
        return textTask;
    }

    private Button getButtonRefresh() {
        final var buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        buttonRefresh.addClickListener(e -> updateList());
        return buttonRefresh;
    }

    private Button getButtonCreateTask() {
        var buttonUnit = new Button("Задача", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickListener(click -> {
            TaskModalWin taskModalWin = new TaskModalWin(taskService, new TaskDto(), employeeService, contractorService, taskCommentService);
            taskModalWin.open();
        });
        return buttonUnit;
    }

    private Button getButtonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
    }

    private TextField getTextField() {
        var textField = new TextField();
        textField.setPlaceholder("Задача, комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");

        return textField;
    }

    private void updateList() {
        GridPaginator<TaskDto> paginatorUpdateList
                = new GridPaginator<>(grid, taskService.getAll(), 15);
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorUpdateList);
        paginator = getPaginator();
        removeAll();
        add(getToolBar(), filter, grid, paginator);
    }

    private static String formatDate(String date) {
        return LocalDateTime.parse(date).format(DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.SHORT));
    }

    private List<TaskDto> getData() {
        return taskService.getAll();
    }

    private Component getIsCompleteIcon(TaskDto taskDto) {
        if (taskDto.isCompleted()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private GridPaginator<TaskDto> getPaginator(){
        return new GridPaginator<>(grid, taskService.getAll(), 15);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
