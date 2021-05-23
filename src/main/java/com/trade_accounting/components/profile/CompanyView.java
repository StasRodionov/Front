package com.trade_accounting.components.profile;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
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
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Route(value = "company", layout = AppView.class)
@PageTitle("Юр. лица")
public class CompanyView extends VerticalLayout {

    private final CompanyService companyService;
    private final List<CompanyDto> data;
    private final Grid<CompanyDto> grid;
    private final GridPaginator<CompanyDto> paginator;
    private final GridFilter<CompanyDto> filter;

    private final NumberField selectedNumberField;

    public CompanyView(CompanyService companyService) {
        this.companyService = companyService;
        this.data = companyService.getAll();

        this.grid = new Grid<>(CompanyDto.class);
        this.paginator = new GridPaginator<>(grid, data, 100);

        this.selectedNumberField = getSelectedNumberField();

        setHorizontalComponentAlignment(Alignment.CENTER, paginator);

        configureGrid();

        this.filter = new GridFilter<>(grid);
        configureFilter();

        add(getToolbar(), filter, grid, paginator);
    }

    private void configureGrid() {
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.setColumns("id", "name", "inn", "address", "commentToAddress",
                "email", "phone", "fax", "leader", "leaderManagerPosition", "leaderSignature",
                "chiefAccountant", "chiefAccountantSignature", "payerVat",
                "stamp", "sortNumber", "legalDetailDto");

        grid.getColumnByKey("id").setHeader("ID").setId("ID");
        grid.getColumnByKey("name").setHeader("Наименование").setId("Наименование");
        grid.getColumnByKey("inn").setHeader("ИНН").setId("ИНН");
        grid.getColumnByKey("address").setHeader("Адрес").setId("Адрес");
        grid.getColumnByKey("commentToAddress").setHeader("Комментарий к адресу").setId("Комментарий к адресу");
        grid.getColumnByKey("email").setHeader("E-mail").setId("E-mail");
        grid.getColumnByKey("phone").setHeader("Телефон").setId("Телефон");
        grid.getColumnByKey("fax").setHeader("Факс").setId("Факс");
        grid.getColumnByKey("leader").setHeader("Руководитель").setId("Руководитель");
        grid.getColumnByKey("leaderManagerPosition").setHeader("Должность руководителя").setId("Должность руководителя");
        grid.getColumnByKey("leaderSignature").setHeader("Подпись руководителя").setId("Подпись руководителя");
        grid.getColumnByKey("chiefAccountant").setHeader("Главный бухгалтер").setId("Главный бухгалтер");
        grid.getColumnByKey("chiefAccountantSignature").setHeader("Подпись гл. бухгалтера").setId("Подпись гл. бухгалтера");
        grid.getColumnByKey("payerVat").setHeader("Плательщик НДС").setId("Плательщик НДС");
        grid.getColumnByKey("sortNumber").setHeader("Нумерация").setId("Нумерация");
        grid.getColumnByKey("stamp").setHeader("Печать").setId("Печать");
        grid.getColumnByKey("legalDetailDto").setHeader("Юридические детали").setId("Юридические детали");
        getGridContextMenu();

        grid.setHeight("64vh");
        grid.setColumnReorderingAllowed(true);
        grid.addItemDoubleClickListener(event -> {
            CompanyDto companyDto = event.getItem();
            CompanyModal companyModal = new CompanyModal(companyDto, companyService);
            companyModal.addDetachListener(e -> reloadGrid());
            companyModal.open();
        });

        grid.addSelectionListener(e -> selectedNumberField.setValue((double) e.getAllSelectedItems().size()));
    }


    private void reloadGrid() {
        paginator.setData(companyService.getAll());
    }

    private void configureFilter() {
        filter.setVisibleFields(false, "name", "legalDetailDto");

        filter.setFieldToComboBox("payerVat", Boolean.TRUE, Boolean.FALSE);
        filter.setFieldToIntegerField("inn");

        filter.onSearchClick(e -> paginator.setData(companyService.search(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(companyService.getAll()));
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();

        TextField searchTextField = new TextField();
        searchTextField.setPlaceholder("Наименование");
        searchTextField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        searchTextField.setWidth("300px");
        // Добавляем лиснер для пойска
        searchTextField.setValueChangeMode(ValueChangeMode.EAGER);
        searchTextField.addValueChangeListener(field -> fillList(field.getValue()));


        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));


        toolbar.add(getButtonQuestion(), getTextCompany(), getRefreshButton(), getNewCompanyButton(), filterButton,
                searchTextField, selectedNumberField, getSelect(), getSettingButton());
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolbar;
    }

    //Ищем компанию по имени
    private void fillList(String searchData) {
        Map<String, String> companyName = new HashMap<>();

        if (searchData.isEmpty()) {
            grid.setItems(companyService.getAll());
        } else {
            companyName.put("name", searchData);
            grid.setItems(companyService.search(companyName));
        }
    }

    private Button getSettingButton() {
        final Button buttonCog = new Button();
        buttonCog.setIcon(new Icon(VaadinIcon.COG_O));
        return buttonCog;
    }

    private NumberField getSelectedNumberField() {
        final NumberField numberField = new NumberField();
        numberField.setWidth("50px");
        numberField.setValue(0D);
        return numberField;
    }

    private Button getNewCompanyButton() {
        final Button button = new Button("Юр. лицо");
        button.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
        CompanyModal companyModal = new CompanyModal(companyService);
        companyModal.addDetachListener(e -> reloadGrid());
        button.addClickListener(e -> companyModal.open());
        return button;
    }

    private Button getRefreshButton() {
        final Button buttonRefresh;
        Icon circle = new Icon(VaadinIcon.REFRESH);
        buttonRefresh = new Button();
        buttonRefresh.setIcon(circle);
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private H2 getTextCompany() {
        final H2 textCompany = new H2("Юр. лица");
        textCompany.setHeight("2.2em");
        return textCompany;
    }

    private Button getButtonQuestion() {
        final Button buttonQuestion = new Button();
        Icon question = new Icon(VaadinIcon.QUESTION_CIRCLE_O);
        buttonQuestion.setIcon(question);
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private Select<String> getSelect() {
        final Select<String> selector = new Select<>();
        selector.setItems("Изменить");
        selector.setValue("Изменить");
        selector.setWidth("130px");
        return selector;
    }

    private ContextMenu getGridContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setTarget(grid);

        CheckboxGroup<Grid.Column<CompanyDto>> checkboxGroup = new CheckboxGroup<>();
        contextMenu.addItem(checkboxGroup);

        checkboxGroup.setItems(grid.getColumns().stream());
        checkboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        checkboxGroup.setItemLabelGenerator(i -> i.getId().orElse(i.getKey()));
        checkboxGroup.setValue(grid.getColumns().stream().filter(Component::isVisible).collect(Collectors.toSet()));
        checkboxGroup.addSelectionListener(e -> {
            e.getAddedSelection().forEach(i -> i.setVisible(true));
            e.getRemovedSelection().forEach(i -> i.setVisible(false));
        });

        MenuItem menuItem = contextMenu.addItem("Количество строк");
        SubMenu subMenu = menuItem.getSubMenu();

        subMenu.addItem("25", e -> paginator.setItemsPerPage(25));

        subMenu.addItem("50", e -> paginator.setItemsPerPage(50));

        subMenu.addItem("100", e -> paginator.setItemsPerPage(100));

        return contextMenu;
    }
}
