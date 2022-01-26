package com.trade_accounting.components.production;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.StagesProductionDto;
import com.trade_accounting.services.interfaces.DepartmentService;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.StagesProductionService;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.ArrayList;
import java.util.List;

@SpringComponent
@UIScope
@PageTitle("Этапы")
@Route(value = "stages", layout = AppView.class)
public class StageProductionViewTab extends VerticalLayout implements AfterNavigationObserver {

    private final TextField textField = new TextField();
    private final MenuBar selectXlsTemplateButton = new MenuBar();
    private final GridFilter<StagesProductionDto> filter;
    private final List<StagesProductionDto> data;
    private final GridPaginator<StagesProductionDto> paginator;
    private final Grid<StagesProductionDto> grid = new Grid<>(StagesProductionDto.class, false);
    private final StagesProductionService stagesProductionService;
    private final Notifications notifications;
    private final StageProductionModalView view;

    private final DepartmentService departmentService;
    private final EmployeeService employeeService;

    public StageProductionViewTab(StagesProductionService stagesProductionService,
                                  Notifications notifications,
                                  StageProductionModalView view,
                                  DepartmentService departmentService,
                                  EmployeeService employeeService) {

        this.stagesProductionService = stagesProductionService;
        this.notifications = notifications;
        this.view = view;
        this.data = getData();
        this.paginator = new GridPaginator<>(grid, this.stagesProductionService.getALl(), 100);
        setSizeFull();
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(getTollBar(), filter, grid, paginator);
        this.departmentService = departmentService;
        this.employeeService = employeeService;

    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn("name").setHeader("Наименование").setId("Наименование");
        grid.addColumn("description").setHeader("Описание").setId("Описание");
        grid.addColumn(e -> departmentService.getById(e.getDepartmentId()).getName()).setHeader("Владелец-отдел").setId("Владелец-отдел");
        grid.addColumn(e -> employeeService.getById(e.getEmployeeId()).getFirstName()).setHeader("Владелец-сотрудник").setId("Владелец-сотрудник");

        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addItemDoubleClickListener(e -> {
            StagesProductionDto dto = e.getItem();
            StageProductionModalView view = new StageProductionModalView(stagesProductionService, employeeService, departmentService, notifications);
            view.setStagesProductionEdit(dto);
            view.open();
        });
    }

    private void updateList() {
        grid.setItems(stagesProductionService.getALl());
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new VerticalLayout(
                        new Text("Этап — часть техпроцесса. В технологической карте по этапам четко прописываются " +
                                "требования к материалам и затраты на производство. " +
                                "Одни и те же этапы могут быть включены в разные техпроцессы."),
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

        filter.onSearchClick(e ->
                paginator.setData(stagesProductionService.searchStagesProduction(filter.getFilterData())));
        filter.onClearClick(e ->
                paginator.setData(stagesProductionService.getALl()));
    }


    public void updateListTextField() {
        if (!(textField.getValue().equals(""))) {
            grid.setItems(stagesProductionService.search(textField.getValue()));
        } else {
            grid.setItems(stagesProductionService.search("null"));
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

    public void deleteSelectedStagesProduction() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (StagesProductionDto stagesProductionDto : grid.getSelectedItems()) {
                stagesProductionService.deleteById(stagesProductionDto.getId());
                notifications.infoNotification("Выбранные этапы успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные ");
        }
    }

    private Select<String> valueSelect() {
        Select<String> valueSelect = new Select<>();
        List<String> list = new ArrayList<>();
        list.add("Изменить");
        list.add("Удалить");
        valueSelect.setItems(list);
        valueSelect.setValue("Изменить");
        valueSelect.setWidth("120px");
        valueSelect.addValueChangeListener(e -> {
            if (valueSelect.getValue().equals("Удалить")) {
                deleteSelectedStagesProduction();
                grid.deselectAll();
                valueSelect.setValue("Изменить");
                paginator.setData(getData());
            }
        });
        return valueSelect;
    }

    private HorizontalLayout getTollBar() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(buttonQuestion(), getTextOrder(), buttonRefresh(),
                buttonPlusStageProduction(), buttonFilter(),
                textField(), numberField(), valueSelect(),
                buttonSettings(), selectXlsTemplateButton);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    private List<StagesProductionDto> getData() {
        return stagesProductionService.getALl();
    }

    private Button buttonSettings() {
        return new Button(new Icon((VaadinIcon.COG_O)));
    }

    private Button buttonPlusStageProduction() {
        Button addStageProduction = new Button("Этап", new Icon(VaadinIcon.PLUS_CIRCLE));
        addStageProduction.addClickListener(e -> view.open());
        updateList();
        return addStageProduction;
    }


    private H2 getTextOrder() {
        final H2 textOrder = new H2("Этапы");
        textOrder.setHeight("2.2em");
        return textOrder;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }

}
