package com.trade_accounting.components.indicators;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.client.EmployeeDto;
import com.trade_accounting.models.dto.indicators.AuditDto;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.indicators.AuditService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@SpringComponent
@Route(value = "auditView", layout = AppView.class)
@PageTitle("Аудит")
@UIScope
public class AuditView extends VerticalLayout {
    private final Grid<AuditDto> grid = new Grid<>(AuditDto.class, false);
    private final GridFilter<AuditDto> filter;
    private final GridPaginator<AuditDto> paginator;
    private final EmployeeService employeeService;
    private final AuditService auditService;
    private final Notifications notifications;

    public AuditView(EmployeeService employeeService, AuditService auditService, Notifications notifications) {
        this.employeeService = employeeService;
        this.auditService = auditService;
        this.notifications = notifications;
        List<AuditDto> data = getData();
        paginator = new GridPaginator<>(grid, data, 50);
        setSizeFull();
        configureGrid();
        filter = new GridFilter<>(grid);
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER);
        add(getUpperLayout(), filter, grid, paginator);

    }

    private List<AuditDto> getData() {
//        return auditService.getAll();
        return List.of(
          new AuditDto().builder().id(1L).date(LocalDateTime.now()).employeeDto(employeeService.getPrincipal()).description("Oga").build(),
          new AuditDto().builder().id(2L).date(LocalDateTime.now()).employeeDto(employeeService.getPrincipal()).description("Oga").build(),
          new AuditDto().builder().id(3L).date(LocalDateTime.now()).employeeDto(employeeService.getPrincipal()).description("Oga").build(),
          new AuditDto().builder().id(4L).date(LocalDateTime.now()).employeeDto(employeeService.getPrincipal()).description("Oga").build(),
          new AuditDto().builder().id(5L).date(LocalDateTime.now()).employeeDto(employeeService.getPrincipal()).description("Oga").build(),
          new AuditDto().builder().id(6L).date(LocalDateTime.now()).employeeDto(employeeService.getPrincipal()).description("Oga").build(),
          new AuditDto().builder().id(7L).date(LocalDateTime.now()).employeeDto(employeeService.getPrincipal()).description("Oga").build(),
          new AuditDto().builder().id(8L).date(LocalDateTime.now()).employeeDto(employeeService.getPrincipal()).description("Oga").build()
        );
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn(AuditDto::getDate).setKey("date").setHeader("Время").setSortable(true).setId("Дата");
        grid.addColumn(AuditDto::getEmployeeDto).setKey("employee").setHeader("Сотрудник").setSortable(true).setId("Сотрудник");
        grid.addColumn(AuditDto::getDescription).setKey("description").setHeader("Событие").setSortable(true).setId("Событие");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
    }



    private Component getUpperLayout(){
        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.add(buttonQuestion(), title(), buttonRefresh(), buttonFilter(),
                textField(), numberField(), valueSelect(), valuePrint());
        mainLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return mainLayout;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion("В разделе фиксируются все события с момента появления аккаунта. Удалять события нельзя." +
                " По умолчанию доступ имеют только администраторы, при необходимости можно настроить соответствующие права и для других сотрудников\n");
    }
    private H2 title() {
        H2 textCompany = new H2("Аудит");
        textCompany.setHeight("2.2em");
        return textCompany;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(ev -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonFilter() {
        return new Button("Фильтр", clickEvent -> {
            filter.setVisible(!filter.isVisible());
        });
    }

    private void configureFilter() {
        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("employee", EmployeeDto::toString, employeeService.getAll());
        filter.onSearchClick(e -> paginator.setData(auditService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(auditService.getAll()));
    }


    private TextField textField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("Сотрудник или время");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> updateListQsearch(textField.getValue()));
        return textField;
    }

    private void updateListQsearch(String text) {
        grid.setItems(auditService.quickSearch(text));
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect() {
        Select<String> select = new Select<>();
        List<String> listItems = new ArrayList<>();
        listItems.add("Изменить");
        listItems.add("Удалить");
        select.setItems(listItems);
        select.setValue("Изменить");
        select.setWidth("130px");
        select.addValueChangeListener(e -> {
            if (select.getValue().equals("Удалить")) {
                deleteSelectedAudits();
                grid.deselectAll();
                select.setValue("Изменить");
                updateList();
            }
        });
        return select;
    }


    private void deleteSelectedAudits() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (AuditDto auditDto : grid.getSelectedItems()) {
                auditService.deleteById(auditDto.getId());
                notifications.infoNotification("Выбранные события успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные заказы");
        }
    }


    private Select<String> valuePrint() {
        Select<String> print = new Select<>();
        print.setItems("Печать", "Добавить шаблон");
        print.setValue("Печать");
        print.setWidth("130px");
        return print;
    }

    private void updateList() {
        grid.setItems(getData());
    }
}
