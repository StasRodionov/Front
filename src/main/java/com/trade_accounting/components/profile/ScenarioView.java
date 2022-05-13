package com.trade_accounting.components.profile;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.configure.components.select.SelectConstants;
import com.trade_accounting.components.util.configure.components.select.SelectExt;
import com.trade_accounting.models.dto.units.ScenarioDto;
import com.trade_accounting.services.interfaces.units.ScenarioService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;


import java.util.ArrayList;
import java.util.List;

import static com.trade_accounting.config.SecurityConstants.PROFILE_SCENARIO_VIEW;


public class ScenarioView extends VerticalLayout {
    private final ScenarioService scenarioService;
    private final List<ScenarioDto> data;
    private Grid<ScenarioDto> grid = new Grid<>(ScenarioDto.class);
    private GridPaginator<ScenarioDto> paginator;
    private final GridFilter<ScenarioDto> filter;

    public ScenarioView(ScenarioService scenarioService){
        this.scenarioService = scenarioService;
        this.data = scenarioService.getAll();
        paginator = new GridPaginator<>(grid, data,100);
        setHorizontalComponentAlignment(Alignment.CENTER,paginator);
        grid();
        this.filter = new GridFilter<>(grid);
        updateList();
        configureFilter();
        add(toolsUp(), filter, grid, paginator);
    }
    private void configureFilter(){
        filter.onSearchClick(e-> paginator.setData(scenarioService.search(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(scenarioService.getAll()));
    }
    private Button buttonQuestion(){
        return Buttons.buttonQuestion("\t\n" +
                "Сценарии — это цепочки действий, которые при включении выполняются автоматически," +
                " без участия пользователя. Чтобы создать сценарий, нужно указать, какое событие его запускает," +
                " что именно выполняется по сценарию и при каких условиях.");
    }
    private H2 title(){
        H2 title = new H2("Сценарии");
        title.setHeight("2.2em");
        return title;
    }
    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }
    private Button buttonScenario(){
        Button buttonScenario = new Button("Сценарий",new Icon(VaadinIcon.PLUS_CIRCLE));
        ScenarioModalWindow addScenarioModalWindow = new ScenarioModalWindow(new ScenarioDto(),scenarioService);
        buttonScenario.addClickListener(e -> addScenarioModalWindow.open());
        addScenarioModalWindow.addDetachListener(e-> updateList());
        return buttonScenario;
    }
    private void updateList(){
        grid.setItems(scenarioService.getAll());
    }
    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }
    private TextField textField() {
        TextField textField = new TextField("", "1-1 из 1");
        textField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        return textField;
    }
    private void updateListAfterSearch(String text) {
        grid.setItems(scenarioService.findBySearch(text));
    }

    private NumberField numberField() {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }
    private Select<String> valueSelect() {
        return new SelectExt.SelectBuilder<String>()
                .item(SelectConstants.CHANGE_SELECT_ITEM)
                .defaultValue(SelectConstants.CHANGE_SELECT_ITEM)
                .width(SelectConstants.SELECT_WIDTH_130PX)
                .build();
    }
    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private HorizontalLayout toolsUp() {
        HorizontalLayout toolsUp = new HorizontalLayout();
        toolsUp.add(buttonQuestion(), title(), buttonRefresh(), buttonScenario(), buttonFilter(), textField(), numberField(),
                valueSelect(), buttonSettings());
        toolsUp.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return toolsUp;
    }

    private Button doubleLeft() {
        return new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT));
    }

    private Button left() {
        return new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    }

    private Button doubleRight() {
        return new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT));
    }

    private Button right() {
        return new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    }

    private void grid() {
        grid.setItems(scenarioService.getAll());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setColumns("id", "name","comment","activeStatus");
        grid.getColumnByKey("id").setHeader("id").setId("id");
        grid.getColumnByKey("name").setHeader("Имя").setId("Имя");
        grid.getColumnByKey("comment").setHeader("Комментарий").setId("Комментарий");
        grid.getColumnByKey("activeStatus").setHeader("Действует").setId("Действует");
        grid.setHeight("64vh");
        grid.addItemDoubleClickListener(event -> {
            ScenarioDto editScenario = event.getItem();
            ScenarioModalWindow scenarioModalWindow =
                    new ScenarioModalWindow(editScenario, scenarioService);
            scenarioModalWindow.addDetachListener(e -> updateList());
            scenarioModalWindow.open();
        });
    }
}
