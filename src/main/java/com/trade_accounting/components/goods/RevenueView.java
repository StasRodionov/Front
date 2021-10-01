package com.trade_accounting.components.goods;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.models.dto.RevenueDto;
import com.trade_accounting.services.interfaces.RevenueService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@PageTitle("Обороты")
@Route(value = "revenueView", layout = AppView.class)
@UIScope
public class RevenueView extends VerticalLayout {

    private final RevenueService revenueService;
    private final RevenueDto revenueDto;

    private final Grid<RevenueDto> grid = new Grid<>(RevenueDto.class, false);
    private final GridFilter<RevenueDto> filter;

    public RevenueView(RevenueService revenueService) {
        this.revenueService = revenueService;
        this.filter = new GridFilter<>(grid);
        this.revenueDto = new RevenueDto();

        add(upperLayout(), filter);
    }

    private void configureGrid() {

    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), title(), buttonRefresh(), buttonFilter(), numberField(), getPrint(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upper;
    }

    private H2 title() {
        H2 title = new H2("Обороты");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        String notificationText = "В разделе представлены приход и расход товаров за определенный "+
                "временной промежуток. Можно посмотреть статистику по складу, поставщику, проекту и так далее.\n" +
                "\n" +
                "Нажмите на строку с товаром — откроется список всех документов, " +
                "которые повлияли на оборот по данному товару.";


        var notification = new Notification(
                notificationText, 3000, Notification.Position.BOTTOM_START);

        buttonQuestion.addClickListener(event -> notification.open());

        return buttonQuestion;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(e -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }


    private Button buttonFilter() {
        Button filterButton = new Button("Фильтр");
        return filterButton;
    }

    private void updateList() {

    }


    private Button buttonSettings() {
        Button buttonSettings = new Button(new Icon(VaadinIcon.COG_O));
        return buttonSettings;
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }


    private Select<String> getPrint() {
        Select<String> print = new Select<>();
        print.setItems("Печать");
        print.setValue("Печать");
        print.setWidth("130px");
        return print;
    }

}