package com.trade_accounting.components.contractors;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.services.interfaces.ContractorGroupService;
import com.trade_accounting.services.interfaces.ContractorService;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "contractorsTabView", layout = AppView.class)
@PageTitle("Контрагенты")
public class ContractorsTabView extends VerticalLayout {

    private final ContractorService contractorService;
    private final ContractorGroupService contractorGroupService;
    private Grid<ContractorDto> grid;


    public ContractorsTabView(ContractorService contractorService, ContractorGroupService contractorGroupService) {
        this.contractorService = contractorService;
        this.contractorGroupService = contractorGroupService;
        updateList();
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(ev -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonUnit() {
        Button buttonUnit = new Button("Контрагент", new Icon(VaadinIcon.PLUS_CIRCLE));
        ContractorModalWindow addContractorModalWindow =
                new ContractorModalWindow(new ContractorDto(), contractorService, contractorGroupService);
        addContractorModalWindow.addDetachListener(event -> updateList());
        buttonUnit.addClickListener(event -> addContractorModalWindow.open());
        return buttonUnit;
    }

    private Button buttonFilter() {
        return new Button("Фильтр");
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private TextField text() {
        TextField text = new TextField();
        text.setPlaceholder("Наимен, тел, соб, коммент...");
        text.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        text.setWidth("300px");
        return text;
    }

    private H2 title() {
        H2 title = new H2("Контрагенты");
        title.setHeight("2.2em");
        return title;
    }

    private NumberField numberField() {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect() {
        Select<String> valueSelect = new Select<>();
        valueSelect.setItems("Изменить");
        valueSelect.setValue("Изменить");
        valueSelect.setWidth("130px");
        return valueSelect;
    }

    private void configureGrid() {
        grid.setColumns("id", "name", "inn", "sortNumber", "phone", "fax", "email", "address", "commentToAddress", "comment");
        grid.getColumnByKey("id").setHeader("ID");

        grid.getColumnByKey("name").setHeader("Наименование");
        grid.getColumnByKey("inn").setHeader("Инн");
        grid.getColumnByKey("sortNumber").setHeader("номер");
        grid.getColumnByKey("phone").setHeader("телефон");
        grid.getColumnByKey("fax").setHeader("факс");
        grid.getColumnByKey("email").setHeader("емэйл");
        grid.getColumnByKey("address").setHeader("адресс");
        grid.getColumnByKey("commentToAddress").setHeader("комментарий к адресу");
        grid.getColumnByKey("comment").setHeader("комментарий");

        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addItemDoubleClickListener(event -> {
            ContractorDto editContractor = event.getItem();
            ContractorModalWindow addContractorModalWindow =
                    new ContractorModalWindow(editContractor, contractorService, contractorGroupService);
            addContractorModalWindow.addDetachListener(e -> updateList());
            addContractorModalWindow.open();
        });
    }

    private void updateList() {
        this.grid = new Grid<>(ContractorDto.class);
        GridPaginator<ContractorDto> paginator
                = new GridPaginator<>(grid, contractorService.getAll(), 9);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        configureGrid();
        removeAll();
        add(upperLayout(), grid, paginator);
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upperLayout = new HorizontalLayout();
        upperLayout.add(buttonQuestion(), title(), buttonRefresh(), buttonUnit(), buttonFilter(), text(), numberField(),
                valueSelect(), buttonSettings());
        upperLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upperLayout;
    }

}


