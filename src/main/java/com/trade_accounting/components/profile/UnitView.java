package com.trade_accounting.components.profile;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.configure.components.select.SelectConstants;
import com.trade_accounting.components.util.configure.components.select.SelectExt;
import com.trade_accounting.models.dto.units.UnitDto;
import com.trade_accounting.services.interfaces.units.UnitService;
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

import java.util.ArrayList;
import java.util.List;

import static com.trade_accounting.config.SecurityConstants.PROFILE_UNIT_VIEW;

//Если на страницу не ссылаются по URL или она не является отдельной страницей, а подгружается родительским классом, то URL и Title не нужен
/*@Route(value = PROFILE_UNIT_VIEW, layout = AppView.class)
@PageTitle("Единицы измерения")*/
public class UnitView extends VerticalLayout {

    private final UnitService unitService;
    private Grid<UnitDto> grid;
    private GridPaginator<UnitDto> paginator;
    private GridFilter<UnitDto> filter;

    public UnitView(UnitService unitService) {
        this.unitService = unitService;
        updateList();
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), getTextCompany(), getButtonRefresh(), getButton(),
                getButtonFilter(), getTextField(), getNumberField(), getSelect(), getButtonCog());
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolbar;
    }

    private void getGrid() {
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setColumns("id", "shortName", "fullName", "sortNumber");
        grid.getColumnByKey("id").setHeader("id").setId("id");
        grid.getColumnByKey("shortName").setHeader("Краткое наименование").setId("Краткое наименование");
        grid.getColumnByKey("fullName").setHeader("Полное наименование").setId("Полное наименование");
        grid.getColumnByKey("sortNumber").setHeader("Сортировочный номер").setId("Сортировочный номер");
        grid.setHeight("64vh");
        grid.addItemDoubleClickListener(event -> {
            UnitDto editUnit = event.getItem();
            UnitModalWindow unitModalWindow =
                    new UnitModalWindow(editUnit, unitService);
            unitModalWindow.addDetachListener(e -> updateList());
            unitModalWindow.open();
        });
    }

    private Button getButtonCog() {
        final Button buttonCog = new Button();
        buttonCog.setIcon(new Icon(VaadinIcon.COG_O));
        return buttonCog;
    }

    private NumberField getNumberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private TextField getTextField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("Наименование или код");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> updateListAfterSearch(textField.getValue()));
        textField.setWidth("300px");
        return textField;
    }

    private void updateListAfterSearch(String text) {
        grid.setItems(unitService.findBySearch(text));
    }

    private Button getButtonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private Button getButton() {
        final Button button = new Button("Единица измерения");
        button.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
        UnitModalWindow unitModalWindow =
                new UnitModalWindow(new UnitDto(), unitService);
        button.addClickListener(event -> unitModalWindow.open());
        unitModalWindow.addDetachListener(event -> updateList());
        return button;
    }

    private Button getButtonRefresh() {
        final Button buttonRefresh;
        Icon circle = new Icon(VaadinIcon.REFRESH);
        buttonRefresh = new Button();
        buttonRefresh.setIcon(circle);
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private H2 getTextCompany() {
        final H2 textCompany = new H2("Единицы измерения");
        textCompany.setHeight("2.2em");
        return textCompany;
    }

    private Button getButtonQuestion() {
        return Buttons.buttonQuestion("Добавьте описание");
    }

    private Select<String> getSelect() {
        return new SelectExt.SelectBuilder<String>()
                .item(SelectConstants.CHANGE_SELECT_ITEM)
                .defaultValue(SelectConstants.CHANGE_SELECT_ITEM)
                .width(SelectConstants.SELECT_WIDTH_130PX)
                .build();
    }

    private void configureFilter() {
        filter.onSearchClick(e -> paginator.setData(unitService.search(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(unitService.getAll()));
    }

    private void updateList() {
        grid = new Grid<>(UnitDto.class);
        paginator = new GridPaginator<>(grid, unitService.getAll(), 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        getGrid();
        removeAll();
        filter = new GridFilter<>(grid);
        configureFilter();
        add(getToolbar(),filter, grid, paginator);
        GridSortOrder<UnitDto> gridSortOrder = new GridSortOrder(grid.getColumnByKey("sortNumber"), SortDirection.ASCENDING);
        List<GridSortOrder<UnitDto>> gridSortOrderList = new ArrayList<>();
        gridSortOrderList.add(gridSortOrder);
        grid.sort(gridSortOrderList);
    }
}