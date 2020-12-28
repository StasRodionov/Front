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
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "company", layout = AppView.class)
@PageTitle("Юр. лица")
public class CompanyView extends Div {

    private final CompanyService companyService;

    private Grid<CompanyDto> grid = new Grid<>(CompanyDto.class);

    private final H2 textCompany;
    private final Button button;
    private final Button buttonQuestion;
    private final Button buttonRefresh;
    private final Button buttonFilter;
    private final Button buttonCog;
    private final Button angleDoubleLeft;
    private final Button angleDoubleRight;
    private final Button angleLeft;
    private final Button angleRight;
    private final TextField textField;
    private final NumberField numberField;
    private final Select<String> selector;
    private final HorizontalLayout toolbar = new HorizontalLayout();
    private final HorizontalLayout toolbarLow = new HorizontalLayout();


    @Autowired
    public CompanyView(CompanyService companyService) {

        this.companyService = companyService;

        buttonQuestion = new Button();
        Icon question = new Icon(VaadinIcon.QUESTION_CIRCLE_O);
        buttonQuestion.setIcon(question);
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        textCompany = new H2("Юр. лицо");
        textCompany.setHeight("2.2em");

        Icon circle = new Icon(VaadinIcon.REFRESH);

        buttonRefresh = new Button();
        buttonRefresh.setIcon(circle);
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);

        button = new Button("Юр. лицо");
        button.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));

        buttonFilter = new Button("Фильтр");

        textField = new TextField();
        textField.setPlaceholder("Наименование или код");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");

        numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");

        selector = new Select<>();
        selector.setItems("Изменить");
        selector.setValue("Изменить");
        selector.setWidth("130px");

        buttonCog = new Button();
        buttonCog.setIcon(new Icon(VaadinIcon.COG));

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setColumns("id", "name", "address", "commentToAddress", "inn",
                "email", "fax", "leader", "leaderManagerPosition", "leaderSignature",
                "chiefAccountant", "chiefAccountantSignature", "payerVat",
                "phone", "stamp", "sortNumber", "legalDetailDto");
        grid.getColumnByKey("address").setHeader("Адрес");
        grid.getColumnByKey("chiefAccountant").setHeader("Главный бухгалтер");
        grid.getColumnByKey("chiefAccountantSignature").setHeader("Подпись гл. бухгалтера");
        grid.getColumnByKey("commentToAddress").setHeader("Комментарий к адресу");
        grid.getColumnByKey("email").setHeader("E-mail");
        grid.getColumnByKey("fax").setHeader("Факс");
        grid.getColumnByKey("inn").setHeader("ИНН");
        grid.getColumnByKey("leader").setHeader("Руководитель");
        grid.getColumnByKey("leaderManagerPosition").setHeader("Должность руководителя");
        grid.getColumnByKey("leaderSignature").setHeader("Подпись руководителя");
        grid.getColumnByKey("name").setHeader("Наименование");
        grid.getColumnByKey("payerVat").setHeader("Плательщик НДС");
        grid.getColumnByKey("phone").setHeader("Телефон");
        grid.getColumnByKey("sortNumber").setHeader("Нумерация");
        grid.getColumnByKey("stamp").setHeader("Печать");
        grid.getColumnByKey("legalDetailDto").setHeader("Юридические детали");

        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        toolbar.add(buttonQuestion, textCompany, buttonRefresh, button, buttonFilter, textField, numberField, selector, buttonCog);


        angleDoubleLeft = new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT));
        angleDoubleRight = new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT));
        angleLeft = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
        angleRight = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));

        TextField text = new TextField("", "1-1 из 1");
        text.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);

        toolbarLow.add(angleDoubleLeft, angleLeft, text, angleRight, angleDoubleRight);

        add(toolbar, grid, toolbarLow);

        grid.setItems(companyService.getAll());

    }
}
