package com.trade_accounting.components.production;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.Action;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.production.TechnicalProcessDto;
import com.trade_accounting.services.interfaces.client.DepartmentService;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.production.StagesProductionService;
import com.trade_accounting.services.interfaces.production.TechnicalProcessService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.ArrayList;
import java.util.List;

import static com.trade_accounting.config.SecurityConstants.PRODUCTION_TECHNICAL_PROCESS_VIEW;

@SpringComponent
@UIScope
@PageTitle("Тех. процессы")
@Route(value = PRODUCTION_TECHNICAL_PROCESS_VIEW, layout = AppView.class)
public class TechnicalProcessViewTab extends VerticalLayout implements AfterNavigationObserver {

    private final TextField textField = new TextField();
    private final MenuBar selectXlsTemplateButton = new MenuBar();
    private final GridFilter<TechnicalProcessDto> filter;
    private final List<TechnicalProcessDto> data;
    private final GridPaginator<TechnicalProcessDto> paginator;
    private final Grid<TechnicalProcessDto> grid = new Grid<>(TechnicalProcessDto.class, false);
    private final TechnicalProcessService technicalProcessService;
    private final Notifications notifications;
    private final TechnicalProcessModalView view;

    private final DepartmentService departmentService;
    private final EmployeeService employeeService;
    private final StagesProductionService stagesProductionService;

    public TechnicalProcessViewTab(TechnicalProcessService technicalProcessService,
                                   Notifications notifications,
                                   TechnicalProcessModalView view,
                                   DepartmentService departmentService,
                                   EmployeeService employeeService,
                                   StagesProductionService stagesProductionService) {
        this.technicalProcessService = technicalProcessService;
        this.notifications = notifications;
        this.view = view;
        this.departmentService = departmentService;
        this.employeeService = employeeService;
        this.stagesProductionService = stagesProductionService;
        this.data = getData();
        this.paginator = new GridPaginator<>(grid, this.technicalProcessService.getAll(), 100);
        setSizeFull();
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(getTollBar(), filter, grid, paginator);
    }

    private List<TechnicalProcessDto> getData() {
        return technicalProcessService.getAll();
    }

    private void configureGrid() {
        grid.addColumn("name").setHeader("Наименование").setId("Наименование");
        grid.addColumn("description").setHeader("Описание").setId("Описание");
        grid.addColumn(new ComponentRenderer<>(this::getIsShared)).setKey("shared").setHeader("Общий доступ")
                .setId("Общий доступ");
        grid.addColumn(e -> departmentService.getById(e.getDepartmentOwnerId()).getName())
                .setHeader("Владелец-отдел").setId("Владелец-отдел");
        grid.addColumn(e -> employeeService.getById(e.getEmployeeOwnerId()).getFirstName())
                .setHeader("Владелец-сотрудник").setId("Владелец-сотрудник");
        grid.addColumn(TechnicalProcessDto::getDateOfChanged).setKey("date")
                .setHeader("Когда изменен").setSortable(true).setId("Когда изменен");
        grid.addColumn(e -> employeeService.getById(e.getEmployeeWhoLastChangedId()))
                .setHeader("Кто изменил").setId("Кто изменил");

        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addItemDoubleClickListener(e -> {
            view.setTechnicalProcessEdit(technicalProcessService.getById(e.getItem().getId()));
            view.open();
        });
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new VerticalLayout(
                        new Text("Технологический процесс — последовательность этапов, " +
                                "которые необходимо выполнить для получения готовой продукции."),
                        new Div(
                                new Text("Читать инструкцию: "),
                                new Anchor("#", "Расширенный учет производственных операций"))
                )
        );
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(ev -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private void configureFilter() {
        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("shared", Boolean.TRUE, Boolean.FALSE);
        filter.onSearchClick(e ->
                paginator.setData(technicalProcessService.searchTechnicalProcess(filter.getFilterData())));
        filter.onClearClick(e ->
                paginator.setData(technicalProcessService.getAll()));
    }

    public void updateListTextField() {
        if (!(textField.getValue().equals(""))) {
            grid.setItems(technicalProcessService.search(textField.getValue()));
        } else {
            grid.setItems(technicalProcessService.search("null"));
        }
    }

    private TextField textField() {
        textField.setWidth("300px");
        textField.setPlaceholder("Наименование или описание");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setClearButtonVisible(true);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(e -> updateListTextField());
        setSizeFull();
        return textField;
    }

    private NumberField numberField() {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    public void deleteSelectedTechnicalProcess() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (TechnicalProcessDto technicalProcessDto : grid.getSelectedItems()) {
                technicalProcessService.deleteById(technicalProcessDto.getId());
                notifications.infoNotification("Выбранные технические процессы успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные ");
        }
    }

    private Select<String> valueSelect() {
        return SelectConfigurer.configureDeleteSelect(() -> {
            deleteSelectedTechnicalProcess();
            grid.deselectAll();
            paginator.setData(getData());
        });
    }

    private H2 getTextOrder() {
        final H2 textOrder = new H2("Техпроцессы");
        textOrder.setHeight("2.2em");
        return textOrder;
    }

    private Button buttonSettings() {
        return new Button(new Icon((VaadinIcon.COG_O)));
    }

    private Button buttonPlusTechnicalProcess() {
        Button addTechnicalProcess = new Button("Техпроцесс", new Icon(VaadinIcon.PLUS_CIRCLE));
        addTechnicalProcess.addClickListener(e -> view.open());
        updateList();
        return addTechnicalProcess;
    }

    private HorizontalLayout getTollBar() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(buttonQuestion(), getTextOrder(), buttonRefresh(),
                buttonPlusTechnicalProcess(), buttonFilter(),
                textField(), numberField(), valueSelect(),
                buttonSettings(), selectXlsTemplateButton);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    private Component getIsShared(TechnicalProcessDto technicalProcessDto) {
        if (technicalProcessDto.getIsShared()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private void updateList() {
        grid.setItems(technicalProcessService.getAll());
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
