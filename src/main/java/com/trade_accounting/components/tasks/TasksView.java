package com.trade_accounting.components.tasks;

import com.trade_accounting.components.AppView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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


@SpringComponent
@UIScope
@Slf4j
@Route(value = "tasks", layout = AppView.class)
@PageTitle("Задачи")
public class TasksView extends VerticalLayout {


    @Autowired
    public TasksView() {
        add(getToolBar());
    }

    private HorizontalLayout getToolBar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), getTextTask(), getButtonRefresh(),
                getButtonCreateTask(), getButtonFilter(), getTextField());

        toolbar.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return toolbar;
    }

    private Button getButtonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        String notificationText = "Задачи помогают организовать работу. Их можно" +
                " ставить себе или другим сотрудникам, выполнение отслеживается по уведомлениям.\n" +
                "\nЗадачу можно создать из любого документа. Также можно настроить" +
                " автоматическое создание задач в рамках сценариев. Например, если" +
                " покупатель в течение недели не оплачивает счет, можно поставить " +
                "менеджеру задачу связаться с ним.\n\nЧитать инструкцию: Задачи";

        Notification notification = new Notification(
                notificationText, 3000, Notification.Position.BOTTOM_START);

        buttonQuestion.addClickListener(event -> notification.open());

        return buttonQuestion;
    }

    private H2 getTextTask() {
        final H2 textTask = new H2("Задачи");
        textTask.setHeight("2.2em");
        return textTask;
    }

    private Button getButtonRefresh() {
        final Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
//        buttonRefresh.addClickListener(click -> reloadGrid());
        return buttonRefresh;
    }

    private Button getButtonCreateTask() {
        Button buttonUnit = new Button("Задача", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickListener(click -> {
            TaskModalWin taskModalWin = new TaskModalWin();
            taskModalWin.open();
        });
        return buttonUnit;
    }

    private Button getButtonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> {
        });
//        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private TextField getTextField() {
        TextField textField = new TextField();
        textField.setPlaceholder("Задача, комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");

        return textField;
    }

}
