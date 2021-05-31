package com.trade_accounting.components.production;


import com.trade_accounting.components.AppView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
@PageTitle("Тех. карты")
@Route(value = "flowcharts", layout = AppView.class)
public class FlowchartsViewTab extends VerticalLayout {

    private final TextField text = new TextField();

    public FlowchartsViewTab() {
        setSizeFull();
        add(getToolBar(), getLabelFlowcharts());
        setHorizontalComponentAlignment(Alignment.CENTER);
    }

    private HorizontalLayout getToolBar() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(buttonQuestion(), title(), buttonRefresh(), buttonPlusFlowcharts(), buttonPlusGroup(),
                buttonFilter(), text(), bigDecimalField(), valueSelect());
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private H2 title() {
        H2 title = new H2("Тех. карты");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonPlusFlowcharts() {
        Button addFlowchartsButton = new Button("Тех. карта", new Icon(VaadinIcon.PLUS_CIRCLE));
        addFlowchartsButton.getStyle().set("cursor", "pointer");
        return addFlowchartsButton;
    }

    private Button buttonPlusGroup() {
        return new Button("Группа", new Icon(VaadinIcon.PLUS_CIRCLE));
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        return buttonFilter;
    }

    private TextField text() {
        text.setPlaceholder("Наименование или комментарий");
        text.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        text.setClearButtonVisible(true);
        text.setValueChangeMode(ValueChangeMode.EAGER);
        text.setWidth("300px");
        return text;
    }

    private BigDecimalField bigDecimalField() {
        BigDecimalField numberField = new BigDecimalField();
        numberField.setPlaceholder("0");
        numberField.setWidth("35px");
        return numberField;
    }

    private Select<String> valueSelect() {
        Select<String> valueSelect = new Select<>();
        valueSelect.setWidth("120px");
        valueSelect.setItems("Изменить");
        valueSelect.setValue("Изменить");
        return valueSelect;
    }

    private VerticalLayout getLabelFlowcharts() {
        VerticalLayout verticalLayout = new VerticalLayout();
        Label label = new Label("Тех. карты");
        label.setWidth("300px");
        label.setHeight("30px");
        label.getElement().getStyle().set("background-color", "#e4f1fa");
        verticalLayout.add(label);
        return verticalLayout;
    }


}
