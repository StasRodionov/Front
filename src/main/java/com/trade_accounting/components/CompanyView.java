package com.trade_accounting.components;

import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.services.interfaces.CompanyService;
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

@Route(value = "company", layout = AppView.class)
@PageTitle("Юр. лица")
public class CompanyView extends VerticalLayout {

    private final CompanyService companyService;

    @Autowired
    public CompanyView(CompanyService companyService) {

        this.companyService = companyService;
        add(getToolbar(), getGrid(), getToolbarLow());
    }

    private Grid<CompanyDto> getGrid() {
        Grid<CompanyDto> grid = new Grid<>(CompanyDto.class);
        grid.setItems(companyService.getAll());
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
        return grid;
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), getTextCompany(), getButtonRefresh(), getButton(),
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
        TextField text = new TextField("", "1-1 из 1");
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
        final Button button = new Button("Юр. лицо");
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
}
