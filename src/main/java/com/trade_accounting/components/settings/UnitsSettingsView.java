package com.trade_accounting.components.settings;

import com.trade_accounting.components.profile.UnitModalWindow;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.units.UnitDto;
import com.trade_accounting.services.interfaces.units.UnitService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "profile/settings/units_settings", layout = SettingsView.class)
@PageTitle("Единицы измерения")
@Slf4j
@SpringComponent
@UIScope
public class UnitsSettingsView extends VerticalLayout implements AfterNavigationObserver {
    private final UnitService unitService;
    private final List<UnitDto> data;
    private final Grid<UnitDto> grid = new Grid<>(UnitDto.class, false);
    private final GridPaginator<UnitDto> paginator;
    private final GridFilter<UnitDto> filter;
    private final TextField textField = new TextField();

    @Autowired
    public UnitsSettingsView(UnitService unitService) {
        this.unitService = unitService;
        this.data = unitService.getAll();
        configureGrid();
        paginator = new GridPaginator<>(grid, data, 50);
        this.filter = new GridFilter<>(grid);
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(upperLayout(), filter, grid, paginator);
    }

    private void configureFilter() {

    }

    private Grid<UnitDto> configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setItems(data);
        grid.addColumn("unitType").setHeader("Тип").setId("Тип");
        grid.addColumn("shortName").setHeader("Краткое наименование").setId("Краткое наименование");
        grid.addColumn("fullName").setHeader("Полное наименование").setId("Полное наименование");
        grid.addColumn("sortNumber").setHeader("Цифровой код").setId("Цифровой код");
        grid.addColumn("generalAccess").setHeader("Общий доступ").setId("Общий доступ");
        grid.addColumn("departmentOwner").setHeader("Владелец-отдел").setId("Владелец-отдел");
        grid.addColumn("employeeOwner").setHeader("Владелец-сотрудник").setId("Владелец-сотрудник");
        grid.addColumn("dateOfChange").setHeader("Когда изменен").setId("Когда изменен");
        grid.addColumn("employeeChange").setHeader("Кто изменил").setId("Кто изменил");
        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addItemDoubleClickListener(event -> {
        });
        return grid;
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(),buttonUnit(), buttonFilter(), filterByTextField());
        upper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upper;
    }

    private Button buttonUnit() {
        final Button button = new Button("Единица измерения");
        button.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
        UnitModalWindow unitModalWindow =
                new UnitModalWindow(new UnitDto(), unitService);
        button.addClickListener(event -> unitModalWindow.open());
        unitModalWindow.addDetachListener(event -> updateList());
        return button;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion(
                new Text("С справочнике собраны единицы измерения, которые можно указать в карточке товара для учета остатков, " +
                        "а также варианты упаковки товаров. При необходимости вы можете добавлять в справочник новые единицы измерения."));
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        buttonRefresh.addClickListener(e -> updateList());
        return buttonRefresh;
    }


    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private H4 title() {
        H4 title = new H4("Единицы измерения");
        title.setHeight("2.2em");
        title.setWidth("80px");
        return title;
    }

    private void updateList() {
     grid.setItems(unitService.getAll());
    }

    private void updateListAfterSearch(String text) {
        grid.setItems(unitService.findBySearch(text));
    }

    private TextField filterByTextField() {
        textField.setPlaceholder("Наименование или код");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> updateListAfterSearch(textField.getValue()));
        textField.setWidth("300px");
        return textField;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {updateList();}
}
