package com.trade_accounting.components.contractors;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.ContractDto;
import com.trade_accounting.services.interfaces.ContractService;
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


@Route(value = "contracts", layout = AppView.class)
@PageTitle("Договоры")
public class ContractsView extends VerticalLayout {

    private final ContractService contractService;
    private Grid<ContractDto> grid = new Grid<>(ContractDto.class);
    private GridPaginator<ContractDto> paginator;


    ContractsView(ContractService contractService) {

        this.contractService = contractService;
        paginator = new GridPaginator<>(grid, contractService.getAll(), 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(getToolbar(), grid, paginator);
        getGrid();


    }


    private void getGrid() {

        grid.setItems(contractService.getAll());
        grid.setColumns("id", "contractDate", "companyDto", "contractorDto", "amount",
                "bankAccountDto", "archive","comment", "number");
        grid.getColumnByKey("id").setAutoWidth(true).setHeader("ID");
        grid.getColumnByKey("contractDate").setAutoWidth(true).setHeader("Дата заключения");
        grid.getColumnByKey("companyDto").setHeader("Компания");
        grid.getColumnByKey("contractorDto").setHeader("Контрагент");
        grid.getColumnByKey("amount").setAutoWidth(true).setHeader("Сумма");
        grid.getColumnByKey("bankAccountDto").setHeader("Банковский Аккаунт");
        grid.getColumnByKey("archive").setAutoWidth(true).setHeader("Архив");
        grid.getColumnByKey("comment").setAutoWidth(true).setHeader("Комментарий");
        grid.getColumnByKey("number").setAutoWidth(true).setHeader("Сортировочный номер");
        grid.setHeight("66vh");
        grid.addItemDoubleClickListener(event -> {
            ContractDto editContract = event.getItem();
            ContractModalWindow contractModalWindow =
                    new ContractModalWindow(editContract, contractService);
            //contractModalWindow.addDetachListener(e -> updateList());
            contractModalWindow.open();
        });
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), getTextContract(), getButtonRefresh(), getButton(),
                getButtonFilter(), getTextField(), getNumberField(), getSelect(), getButtonCog());
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolbar;
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
        textField.setWidth("300px");
        return textField;
    }

    private Button getButtonFilter() {
        return new Button("Фильтр");
    }

    private Button getButton() {
        final Button button = new Button("Договор");
        button.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
        ContractModalWindow contractModalWindow = new ContractModalWindow(new ContractDto(), contractService);
        button.addClickListener(event -> contractModalWindow.open());
        //button.addDetachListener(event -> u)
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

    private H2 getTextContract() {
        final H2 textCompany = new H2("Договоры");
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
}
