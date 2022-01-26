package com.trade_accounting.components.money;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.MoneySubProfitLossDto;
import com.trade_accounting.services.interfaces.MoneySubProfitLossService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "MoneySubProfitLossView", layout = AppView.class)
@PageTitle("Прибыли и убытки")
public class MoneySubProfitLossView extends VerticalLayout {


    private final MoneySubProfitLossService moneySubProfitLossService;

    private final List<MoneySubProfitLossDto> data;
    private final Grid<MoneySubProfitLossDto> grid = new Grid<>(MoneySubProfitLossDto.class, false);
    private final GridFilter<MoneySubProfitLossDto> filter;
    private final CreditOrderModal creditOrderModal;

    private H2 title() {
        H2 title = new H2("Прибыли и убытки");
        title.setHeight("2.2em");
        return title;
    }

    public MoneySubProfitLossView(MoneySubProfitLossService moneySubProfitLossService,
                                  CreditOrderModal creditOrderModal) {
        this.moneySubProfitLossService = moneySubProfitLossService;
        this.data = moneySubProfitLossService.getAll();
        this.creditOrderModal = creditOrderModal;
        getGrid();
        this.filter = new GridFilter<>(grid);
//        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER);
        add(getToolbar(), filter, grid);
    }

//    private void configureFilter() {
//        filter.setFieldToDatePicker("itemsList");
//    }

    //TODO Rename to configureGrid
    private Grid getGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setItems(data);
        grid.addColumn("itemsList").setFlexGrow(3).setHeader("Статья").setId("Статья");
        grid.addColumn("profitLoss").setFlexGrow(3).setHeader("Прибыль/Убытки").setId("Прибыль/Убытки");
        return grid;
    }

    private void updateList() {
        GridPaginator<MoneySubProfitLossDto> paginatorUpdateList
                = new GridPaginator<>(grid, moneySubProfitLossService.getAll(), 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(getToolbar(), grid);
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), getTextContract(), getButtonRefresh(),
                getButtonFilter(), getPrint());
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolbar;
    }

    private Button getButtonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
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
        final H2 textCompany = new H2("Прибыли и убытки");
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


    private Select<String> getPrint() {
        Select getPrint = new Select();
        getPrint.setWidth("130px");
        getPrint.setItems("Печать", "Прибыли и убытки");
        getPrint.setValue("Печать");
        return getPrint;
    }
}