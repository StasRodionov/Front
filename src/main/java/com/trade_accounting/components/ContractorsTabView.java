package com.trade_accounting.components;

import com.trade_accounting.components.modal_windows.ContractorModalWindow;
import com.trade_accounting.models.dto.ContractorDto;
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

    private final Grid<ContractorDto> grid = new Grid<>(ContractorDto.class);

    public ContractorsTabView(ContractorService contractorService) {
        this.contractorService = contractorService;
        add(upperLayout(), grid, lowerLayout());
        configureGrid();
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
                new ContractorModalWindow(new ContractorDto(), contractorService);
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

    private TextField textField() {
        TextField textField = new TextField("", "1-1 из 1");
        textField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        return textField;
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
        grid.setColumns("name", "inn", "sortNumber", "phone", "fax", "email", "address", "commentToAddress", "comment");

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
                    new ContractorModalWindow(editContractor, contractorService);
            addContractorModalWindow.addDetachListener(e -> updateList());
            addContractorModalWindow.open();
        });
    }

    private void updateList() {
        grid.setItems(contractorService.getAll());
        System.out.println("обновились");
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upperLayout = new HorizontalLayout();
        upperLayout.add(buttonQuestion(), title(), buttonRefresh(), buttonUnit(), buttonFilter(), text(), numberField(),
                valueSelect(), buttonSettings());
        upperLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upperLayout;
    }

    private HorizontalLayout lowerLayout() {
        HorizontalLayout lowerLayout = new HorizontalLayout();
        lowerLayout.add(new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT)),
                new Button(new Icon(VaadinIcon.ANGLE_LEFT)),
                textField(),
                new Button(new Icon(VaadinIcon.ANGLE_RIGHT)),
                new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT)));
        return lowerLayout;
    }
}


