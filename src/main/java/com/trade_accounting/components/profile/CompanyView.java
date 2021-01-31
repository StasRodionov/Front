package com.trade_accounting.components.profile;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Route(value = "company", layout = AppView.class)
@PageTitle("Юр. лица")
public class CompanyView extends VerticalLayout {

    private final CompanyService companyService;
    private List<CompanyDto> finalData;
    private List<CompanyDto> data;
    private final Grid<CompanyDto> grid;
    private final HorizontalLayout filterLayout;
    private final GridPaginator<CompanyDto> paginator;

    private TextField idFilterField;
    private TextField searchTextField;
    private TextField addressFilterField;
    private TextField emailFilterField;
    private TextField leaderFilterField;
    private TextField chiefAccountantFilterField;
    private TextField leaderManagerPositionFilterField;
    private IntegerField innFilterField;
    private IntegerField phoneFilterField;
    private IntegerField faxFilterField;
    private ComboBox<Boolean> payerVatFilterField;

    public CompanyView(CompanyService companyService) {
        this.companyService = companyService;
        this.finalData = companyService.getAll();
        this.data = finalData;

        this.grid = new Grid<>(CompanyDto.class);
        this.paginator = new GridPaginator<>(grid, data, 100);
        this.filterLayout = new HorizontalLayout();

        setHorizontalComponentAlignment(Alignment.CENTER, paginator);

        configureGrid();
        prepareFilterFields();

        filterLayout.setVisible(false);

        add(getToolbar(), filterLayout, grid, paginator);
    }

    private void configureGrid() {
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.setColumns("id", "name", "inn", "address", "commentToAddress",
                "email", "phone", "fax", "leader", "leaderManagerPosition", "leaderSignature",
                "chiefAccountant", "chiefAccountantSignature", "payerVat",
                "stamp", "sortNumber", "legalDetailDto");

        grid.getColumnByKey("name").setHeader("Наименование");
        grid.getColumnByKey("inn").setHeader("ИНН");
        grid.getColumnByKey("address").setHeader("Адрес");
        grid.getColumnByKey("commentToAddress").setHeader("Комментарий к адресу");
        grid.getColumnByKey("email").setHeader("E-mail");
        grid.getColumnByKey("phone").setHeader("Телефон");
        grid.getColumnByKey("fax").setHeader("Факс");
        grid.getColumnByKey("leader").setHeader("Руководитель");
        grid.getColumnByKey("leaderManagerPosition").setHeader("Должность руководителя");
        grid.getColumnByKey("leaderSignature").setHeader("Подпись руководителя");
        grid.getColumnByKey("chiefAccountant").setHeader("Главный бухгалтер");
        grid.getColumnByKey("chiefAccountantSignature").setHeader("Подпись гл. бухгалтера");
        grid.getColumnByKey("payerVat").setHeader("Плательщик НДС");
        grid.getColumnByKey("sortNumber").setHeader("Нумерация");
        grid.getColumnByKey("stamp").setHeader("Печать");
        grid.getColumnByKey("legalDetailDto").setHeader("Юридические детали");

        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.addItemDoubleClickListener(event -> {
            CompanyDto companyDto = event.getItem();
            CompanyModal companyModal = new CompanyModal(companyDto, companyService);
            companyModal.addDetachListener(e -> reloadGrid());
            companyModal.open();
        });
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();

        searchTextField = gridTextFieldFilter();
        searchTextField.setPlaceholder("Наименование");
        searchTextField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        searchTextField.setWidth("300px");

        searchTextField.setValueChangeMode(ValueChangeMode.TIMEOUT);
        searchTextField.addValueChangeListener(e -> onFilterChange());

        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filterLayout.setVisible(!filterLayout.isVisible()));


        toolbar.add(getButtonQuestion(), getTextCompany(), getRefreshButton(), getNewCompanyButton(),
                filterButton, searchTextField, getSelectedNumberField(), getSelect(), getSettingButton());
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolbar;
    }

    private Button getSettingButton() {
        final Button buttonCog = new Button();
        buttonCog.setIcon(new Icon(VaadinIcon.COG_O));
        return buttonCog;
    }

    private NumberField getSelectedNumberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
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

    private void prepareFilterFields() {
        idFilterField = gridTextFieldFilter();
        innFilterField = gridIntegerFieldFilter();
        addressFilterField = gridTextFieldFilter();
        emailFilterField = gridTextFieldFilter();
        phoneFilterField = gridIntegerFieldFilter();
        faxFilterField = gridIntegerFieldFilter();
        leaderFilterField = gridTextFieldFilter();
        leaderManagerPositionFilterField = gridTextFieldFilter();
        chiefAccountantFilterField = gridTextFieldFilter();

        payerVatFilterField = new ComboBox<>();
        payerVatFilterField.setItems(Boolean.TRUE, Boolean.FALSE);
        payerVatFilterField.addValueChangeListener(e -> onFilterChange());

        Button button = new Button("Очистить");
        button.addClickListener(e -> {
            idFilterField.clear();
            innFilterField.clear();
            addressFilterField.clear();
            emailFilterField.clear();
            phoneFilterField.clear();
            faxFilterField.clear();
            leaderFilterField.clear();
            leaderManagerPositionFilterField.clear();
            chiefAccountantFilterField.clear();
            payerVatFilterField.clear();
        });
        button.getStyle().set("align-self", "flex-end");

        idFilterField.setLabel("ID");
        innFilterField.setLabel("ИНН");
        addressFilterField.setLabel("Адрес");
        emailFilterField.setLabel("E-mail");
        phoneFilterField.setLabel("Телефон");
        faxFilterField.setLabel("Факс");
        leaderFilterField.setLabel("Руководитель");
        leaderManagerPositionFilterField.setLabel("Должность руководителя");
        chiefAccountantFilterField.setLabel("Главный бухгалтер");
        payerVatFilterField.setLabel("Плательщик НДС");

        filterLayout.add(idFilterField, innFilterField, addressFilterField, emailFilterField, phoneFilterField, faxFilterField, leaderFilterField, leaderManagerPositionFilterField, chiefAccountantFilterField,
                payerVatFilterField, button);

        filterLayout.getStyle().set("background-color", "#e7eaef")
                .set("border-radius", "4px")
                .set("align-items", "baseline")
                .set("flex-flow", "row wrap");

        filterLayout.setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private TextField gridTextFieldFilter() {
        TextField filter = new TextField();
        filter.setValueChangeMode(ValueChangeMode.TIMEOUT);
        filter.addValueChangeListener(e -> this.onFilterChange());

        return filter;
    }

    private IntegerField gridIntegerFieldFilter() {
        IntegerField filter = new IntegerField();
        filter.setValueChangeMode(ValueChangeMode.TIMEOUT);
        filter.addValueChangeListener(e -> this.onFilterChange());

        return filter;
    }

    private void reloadGrid() {
        finalData = companyService.getAll();
        onFilterChange();
    }

    private void onFilterChange() {
        grid.setItems(finalData);
        ListDataProvider<CompanyDto> listDataProvider = (ListDataProvider<CompanyDto>) grid.getDataProvider();
        listDataProvider.setFilter(item -> {
            boolean idFilterMatch = true;
            boolean searchFilterMatch = true;
            boolean innFilterMatch = true;
            boolean addressFilterMatch = true;
            boolean emailFilterMatch = true;
            boolean phoneFilterMatch = true;
            boolean faxFilterMatch = true;
            boolean leaderFilterMatch = true;
            boolean leaderManagerPositionFilterMatch = true;
            boolean chiefAccountantFilterMatch = true;
            boolean payerVatFilterMatch = true;

            if (!idFilterField.isEmpty()) {
                idFilterMatch = String.valueOf(item.getId()).contains(idFilterField.getValue());
            }

            if (!searchTextField.isEmpty()) {
                searchFilterMatch = item.getName().toLowerCase().contains(searchTextField.getValue().toLowerCase());
            }

            if (!innFilterField.isEmpty()) {
                innFilterMatch = item.getInn().contains(String.valueOf(innFilterField.getValue()));
            }

            if (!addressFilterField.isEmpty()) {
                addressFilterMatch = item.getAddress().toLowerCase().contains(addressFilterField.getValue().toLowerCase());
            }

            if (!emailFilterField.isEmpty()) {
                emailFilterMatch = item.getEmail().toLowerCase().contains(emailFilterField.getValue().toLowerCase());
            }
            if (!phoneFilterField.isEmpty()) {
                phoneFilterMatch = item.getPhone().contains(String.valueOf(phoneFilterField.getValue()));
            }

            if (!faxFilterField.isEmpty()) {
                faxFilterMatch = item.getFax().contains(String.valueOf(faxFilterField.getValue()));
            }

            if (!leaderFilterField.isEmpty()) {
                leaderFilterMatch = item.getLeader().toLowerCase().contains(leaderFilterField.getValue().toLowerCase());
            }

            if (!leaderManagerPositionFilterField.isEmpty()) {
                leaderManagerPositionFilterMatch = item.getLeaderManagerPosition().toLowerCase()
                        .contains(leaderManagerPositionFilterField.getValue().toLowerCase());
            }

            if (!chiefAccountantFilterField.isEmpty()) {
                chiefAccountantFilterMatch = item.getChiefAccountant().toLowerCase()
                        .contains(chiefAccountantFilterField.getValue().toLowerCase());
            }

            if (!payerVatFilterField.isEmpty()) {
                payerVatFilterMatch = item.getPayerVat().equals(payerVatFilterField.getValue());
            }

            return idFilterMatch && searchFilterMatch && innFilterMatch && addressFilterMatch && emailFilterMatch
                    && phoneFilterMatch && faxFilterMatch && leaderFilterMatch && leaderManagerPositionFilterMatch
                    && chiefAccountantFilterMatch && payerVatFilterMatch;
        });

        data = grid.getDataProvider().fetch(new Query<>()).collect(Collectors.toList());

        paginator.setData(data);
    }
}