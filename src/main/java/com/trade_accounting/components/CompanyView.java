package com.trade_accounting.components;

import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "company", layout = AppView.class)
@PageTitle("Юр. лица")
public class CompanyView extends Div {

    private final CompanyService companyService;

    private Grid<CompanyDto> grid = new Grid<>(CompanyDto.class);

    //private final Text text;
    private final Button button;
    private final Button buttonText;
    private final Button buttonQuestion;
    private final Button buttonRefresh;
    private final Button buttonFilter;
    private final Button buttonCog;
    private final Button angleDoubleLeft;
    private final Button angleDoubleRight;
    private final Button angleLeft;
    private final Button angleRight;
    private final TextField textField;
    private final Select<String> selector;
    private final HorizontalLayout toolbar = new HorizontalLayout();
    private final HorizontalLayout toolbarLow = new HorizontalLayout();


    @Autowired
    public CompanyView(CompanyService companyService) {

        this.companyService = companyService;

        buttonQuestion = new Button();
        Icon question = new Icon(VaadinIcon.QUESTION_CIRCLE_O);
        question.setColor("Blue");
        question.setSize("1");
        buttonQuestion.setIcon(question);
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        buttonText = new Button("Юр. лица");
        buttonText.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Icon circle = new Icon(VaadinIcon.REFRESH);
        circle.setColor("Grey");
        circle.setSize("1");
        buttonRefresh = new Button();
        buttonRefresh.setIcon(circle);
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);

        button = new Button("Юр. лицо");
        button.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));

        buttonFilter = new Button("Фильтр");

        textField = new TextField();
        textField.setPlaceholder("Наименование или код");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);

        selector = new Select<>();
        selector.setItems("Изменить");
        selector.setValue("Изменить");

        buttonCog = new Button();
        buttonCog.setIcon(new Icon(VaadinIcon.COG));

        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        toolbar.add(buttonQuestion, buttonText, buttonRefresh, button, buttonFilter, textField, selector, buttonCog);

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
