package com.trade_accounting.components.contractors;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.ContractDto;
import com.trade_accounting.services.interfaces.ContractService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "contracts", layout = AppView.class)
@PageTitle("Договоры")
public class ContractsView extends VerticalLayout {

    private final ContractService contractService;

    private final List<ContractDto> finalData;
    private List<ContractDto> data;
    private final Grid<ContractDto> grid;
    private final GridPaginator<ContractDto> paginator;

    @Autowired
    ContractsView(ContractService contractService) {

        this.contractService = contractService;
        this.finalData = contractService.getAll();
        this.data = finalData;

        this.grid = new Grid<>(ContractDto.class);
        this.paginator = new GridPaginator<>(grid,data,100);
        getGrid();

        add(getToolbar(),grid,paginator);

    }


    private void getGrid() {

        Grid<ContractDto> grid = new Grid<>(ContractDto.class);
        grid.setItems(contractService.getAll());
        grid.setColumns("id", "number", "contractDate", "date", "companyDto",
                "bankAccountDto", "contractorDto", "amount", "archive", "comment", "legalDetailDto");
        grid.getColumnByKey("id").setAutoWidth(true).setHeader("ID");
        grid.getColumnByKey("number").setAutoWidth(true).setHeader("Код");
        grid.getColumnByKey("contractDate").setAutoWidth(true).setHeader("Дата заключения");
        grid.getColumnByKey("date").setAutoWidth(true).setHeader("Дата");
        grid.getColumnByKey("companyDto").setHeader("Компания");
        grid.getColumnByKey("bankAccountDto").setHeader("Банковский Аккаунт");
        grid.getColumnByKey("contractorDto").setHeader("Контрагент");
        grid.getColumnByKey("amount").setAutoWidth(true).setHeader("Сумма");
        grid.getColumnByKey("archive").setAutoWidth(true).setHeader("Архив");
        grid.getColumnByKey("comment").setAutoWidth(true).setHeader("Комментарий");
        grid.getColumnByKey("legalDetailDto").setHeader("Реквизиты");
        grid.setHeight("66vh");

    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), getTextContract(), getButtonRefresh(), getButton(),
                getButtonFilter(), getTextField(), getNumberField(), getSelect(), getButtonCog());
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolbar;
    }

    private HorizontalLayout getToolbarLow() {
        HorizontalLayout toolbarLow = new HorizontalLayout();
        toolbarLow.add(getAngleDoubleLeft(), getAngleLeft(), getTextFieldLow(), getAngleRight(), getAngleDoubleRight());
        return toolbarLow;
    }

    private TextField getTextFieldLow() {
        TextField text = new TextField("", "");
        text.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        return text;
    }

    private Button getAngleRight() {
        return new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    }

    private Button getAngleLeft() {
        return new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    }

    private Button getAngleDoubleRight() {
        return new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT));
    }

    private Button getAngleDoubleLeft() {
        return new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT));
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
        final H2 textCompany = new H2("Договора");
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
