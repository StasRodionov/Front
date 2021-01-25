package com.trade_accounting.components;

import com.trade_accounting.components.modal_windows.CompanyModal;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.vaadin.componentfactory.Paginator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
    private final List<CompanyDto> data;
    private final Grid<CompanyDto> grid;

    private static final int ITEMS_PER_PAGE = 100;
    private final Paginator paginator;
    private final TextField pageNumberTextField;

    public CompanyView(CompanyService companyService) {

        this.companyService = companyService;
        this.data = getData();
        this.paginator = getPaginator();
        this.grid = getGrid();
        this.pageNumberTextField = getPageNumberTextField();

        add(getToolbar(), grid, getToolbarLow());
    }

    private Grid<CompanyDto> getGrid() {
        Grid<CompanyDto> dtoGrid = new Grid<>(CompanyDto.class);
        dtoGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        dtoGrid.setColumns("id", "name", "inn", "address", "commentToAddress",
                "email", "phone", "fax", "leader", "leaderManagerPosition", "leaderSignature",
                "chiefAccountant", "chiefAccountantSignature", "payerVat",
                "stamp", "sortNumber", "legalDetailDto");

        dtoGrid.getColumnByKey("name").setHeader("Наименование");
        dtoGrid.getColumnByKey("inn").setHeader("ИНН");
        dtoGrid.getColumnByKey("address").setHeader("Адрес");
        dtoGrid.getColumnByKey("commentToAddress").setHeader("Комментарий к адресу");
        dtoGrid.getColumnByKey("email").setHeader("E-mail");
        dtoGrid.getColumnByKey("phone").setHeader("Телефон");
        dtoGrid.getColumnByKey("fax").setHeader("Факс");
        dtoGrid.getColumnByKey("leader").setHeader("Руководитель");
        dtoGrid.getColumnByKey("leaderManagerPosition").setHeader("Должность руководителя");
        dtoGrid.getColumnByKey("leaderSignature").setHeader("Подпись руководителя");
        dtoGrid.getColumnByKey("chiefAccountant").setHeader("Главный бухгалтер");
        dtoGrid.getColumnByKey("chiefAccountantSignature").setHeader("Подпись гл. бухгалтера");
        dtoGrid.getColumnByKey("payerVat").setHeader("Плательщик НДС");
        dtoGrid.getColumnByKey("sortNumber").setHeader("Нумерация");
        dtoGrid.getColumnByKey("stamp").setHeader("Печать");
        dtoGrid.getColumnByKey("legalDetailDto").setHeader("Юридические детали");

        dtoGrid.setHeight("66vh");
        loadItemsToGrid(dtoGrid, 1);

        return dtoGrid;
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();

        TextField searchTextField = getSearchTextField();
        searchTextField.setValueChangeMode(ValueChangeMode.LAZY);
        searchTextField.addValueChangeListener(e -> {
            List<CompanyDto> searchList = data.stream()
                    .filter(item -> item.getName().toLowerCase().contains(e.getValue().toLowerCase()))
                    .collect(Collectors.toList());

            if (!searchTextField.isEmpty()) {
                grid.setItems(searchList);
                this.pageNumberTextField.setPlaceholder("Найдено: " + searchList.size());
            } else {
                loadItemsToGrid(grid, paginator.getCurrentPage());
                this.pageNumberTextField.setPlaceholder(getCurrentGridNumber());
            }

            remove(getComponentAt(1));
            addComponentAtIndex(1, grid);
        });

        toolbar.add(getButtonQuestion(), getTextCompany(), getButtonRefresh(), getButtonAdd(),
                getButtonFilter(), searchTextField, getNumberField(), getSelect(), getButtonCog());
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolbar;
    }

    private HorizontalLayout getToolbarLow() {
        HorizontalLayout toolbarLow = new HorizontalLayout();

        Button firstPageButton = new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT));
        Button prevPageButton = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
        Button nextPageButton = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
        Button lastPageButton = new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT));

        if (paginator.getCurrentPage() == 1) {
            firstPageButton.setEnabled(false);
            prevPageButton.setEnabled(false);
        }

        nextPageButton.addClickListener(e -> {
            if (paginator.getNumberOfPages() == paginator.getCurrentPage() + 1) {
                paginator.setCurrentPage(paginator.getCurrentPage() + 1);

                nextPageButton.setEnabled(false);
                lastPageButton.setEnabled(false);
            } else {
                paginator.setCurrentPage(paginator.getCurrentPage() + 1);
            }

            prevPageButton.setEnabled(true);
            firstPageButton.setEnabled(true);

            reloadGrid();

            pageNumberTextField.setPlaceholder(getCurrentGridNumber());
        });

        lastPageButton.addClickListener(e -> {
            paginator.setCurrentPage(paginator.getNumberOfPages());

            nextPageButton.setEnabled(false);
            lastPageButton.setEnabled(false);

            prevPageButton.setEnabled(true);
            firstPageButton.setEnabled(true);

            reloadGrid();

            pageNumberTextField.setPlaceholder(getCurrentGridNumber());
        });

        prevPageButton.addClickListener(e -> {
            if (paginator.getCurrentPage() - 1 == 1) {
                paginator.setCurrentPage(paginator.getCurrentPage() - 1);

                prevPageButton.setEnabled(false);
                firstPageButton.setEnabled(false);
            } else {
                paginator.setCurrentPage(paginator.getCurrentPage() - 1);
            }

            nextPageButton.setEnabled(true);
            lastPageButton.setEnabled(true);

            reloadGrid();

            pageNumberTextField.setPlaceholder(getCurrentGridNumber());
        });

        firstPageButton.addClickListener(e -> {
            paginator.setCurrentPage(1);

            prevPageButton.setEnabled(false);
            firstPageButton.setEnabled(false);

            nextPageButton.setEnabled(true);
            lastPageButton.setEnabled(true);

            reloadGrid();

            pageNumberTextField.setPlaceholder(getCurrentGridNumber());
        });

        toolbarLow.add(firstPageButton,
                prevPageButton,
                pageNumberTextField,
                nextPageButton,
                lastPageButton);

        return toolbarLow;
    }

    private Paginator getPaginator() {
        int numberPages = (int) Math.ceil((float) data.size() / ITEMS_PER_PAGE);
        return new Paginator(numberPages);
    }

    private List<CompanyDto> getData() {
        return companyService.getAll();
    }

    private TextField getPageNumberTextField() {
        TextField textField = new TextField("", getCurrentGridNumber());
        textField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);

        return textField;
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

    private TextField getSearchTextField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("Наименование");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        return textField;
    }

    private Button getButtonFilter() {
        return new Button("Фильтр");
    }

    private Button getButtonAdd() {
        Button button = new Button("Юр. лицо");
        button.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
        CompanyModal companyModal = new CompanyModal(companyService);
        companyModal.addDetachListener(e -> reloadGrid());
        button.addClickListener(e -> companyModal.open());
        return button;
    }

    private Button getButtonRefresh() {
        final Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private H2 getTextCompany() {
        final H2 textCompany = new H2("Юр. лицо");
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

    private String getCurrentGridNumber() {
        List<CompanyDto> currentDataOfGrid = grid.getDataProvider().fetch(new Query<>()).collect(Collectors.toList());

        return currentDataOfGrid.get(0).getId() + "-" + currentDataOfGrid.get(currentDataOfGrid.size()-1).getId() + " из " + data.size();
    }

    private void reloadGrid() {
        loadItemsToGrid(grid, paginator.getCurrentPage());

        remove(getComponentAt(1));
        addComponentAtIndex(1, grid);
    }

    private void loadItemsToGrid(Grid<CompanyDto> grid, int page) {
        int from = (page - 1) * ITEMS_PER_PAGE;

        int to = (from + ITEMS_PER_PAGE);
        to = Math.min(to, data.size());

        grid.setItems(data.subList(from, to));
    }
}
