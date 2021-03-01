package com.trade_accounting.components.money;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.PaymentDto;
import com.trade_accounting.services.interfaces.PaymentService;
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

import java.util.List;

@Route(value = "MoneySubPaymentsView", layout = AppView.class)
@PageTitle("Платежи")
public class MoneySubPaymentsView extends VerticalLayout {

    private final PaymentService paymentService;

    private final List<PaymentDto> data;
    private final Grid<PaymentDto> grid = new Grid<>(PaymentDto.class, false);
    private final GridPaginator<PaymentDto> paginator;

    MoneySubPaymentsView(PaymentService paymentService) {
        this.paymentService = paymentService;
        this.data = paymentService.getAll();
        getGrid();
        this.paginator = new GridPaginator<>(grid, data, 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(getToolbar(), grid, paginator);

    }

    private Grid getGrid() {
        grid.setItems(data);
        grid.addColumn("id").setHeader("ID");
        grid.addColumn("time").setFlexGrow(10).setHeader("Дата");
        grid.addColumn(pDto -> pDto.getCompanyDto().getName()).setFlexGrow(10).setSortable(true)
                .setHeader("Компания").setId("companyDto");
        grid.addColumn("sum").setFlexGrow(7).setHeader("Сумма");
        grid.addColumn("number").setFlexGrow(4).setHeader("Номер платеж");
        grid.addColumn("typeOfPayment").setFlexGrow(4).setHeader("Тип платежа");
        grid.addColumn(pDto -> pDto.getContractorDto().getName()).setFlexGrow(10).setSortable(true)
                .setHeader("Контрагент").setId("contractorDto");
        grid.addColumn(pDto -> pDto.getContractDto().getNumber()).setFlexGrow(7).setSortable(true)
                .setHeader("Договор").setId("contractDto");
        grid.addColumn(pDto -> pDto.getProjectDto().getName()).setFlexGrow(7).setSortable(true)
                .setHeader("Проект").setId("projectDto");
        grid.setHeight("66vh");
        return grid;
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
        final Button button = new Button("Платеж");
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
        final H2 textCompany = new H2("Платежи");
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
