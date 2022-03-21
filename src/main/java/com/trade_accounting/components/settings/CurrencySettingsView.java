package com.trade_accounting.components.settings;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.profile.CurrencyModalWindow;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.units.CurrencyDto;
import com.trade_accounting.services.interfaces.units.CurrencyService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Route(value = "profile/settings/currency_settings", layout = SettingsView.class)
@PageTitle("Учетная запись")
@Slf4j
public class CurrencySettingsView extends VerticalLayout {

    private final NumberField selectedNumberField;
    private final List<CurrencyDto> data;
    private final CurrencyService currencyService;
    private Grid<CurrencyDto> grid = new Grid<>(CurrencyDto.class);
    private GridPaginator<CurrencyDto> paginator;
    private final GridFilter<CurrencyDto> filter;

    public CurrencySettingsView(CurrencyService currencyService){
        this.selectedNumberField = getSelectedField();
        this.data = currencyService.getAll();
        this.currencyService = currencyService;
        this.paginator = paginator = new GridPaginator<>(grid, data, 30);
        this.filter = new GridFilter<>(grid);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        configureGrid();
//        configureFilter();
        add(getAppView(), filter, grid, paginator);
    }
    private AppView getAppView(){
        return new AppView();
    }

    private NumberField getSelectedField() {
        final NumberField numberField = new NumberField();
        numberField.setWidth("50px");
        numberField.setValue(0D);
        return numberField;
    }

    private void updateList() {
        grid.setItems(currencyService.getAll());
    }

    private void configureGrid() {
        grid.setItems(currencyService.getAll());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setColumns("id", "shortName", "fullName", "digitalCode", "letterCode", "sortNumber");
        grid.getColumnByKey("id").setHeader("ID").setId("ID");
        grid.getColumnByKey("shortName").setHeader("Краткое наименование").setId("Краткое наименование");
        grid.getColumnByKey("fullName").setHeader("Полное наименование").setId("Полное наименование");
        grid.getColumnByKey("digitalCode").setHeader("Цифровой код").setId("Цифровой код");
        grid.getColumnByKey("letterCode").setHeader("Буквенный код").setId("Буквенный код");
        grid.getColumnByKey("sortNumber").setHeader("Сортировочный номер").setId("Сортировочный номер");

        grid.setHeight("64vh");
        grid.addItemDoubleClickListener(event -> {
            CurrencyDto editCurrency = event.getItem();
            CurrencyModalWindow currencyModalWindow =
                    new CurrencyModalWindow(editCurrency, currencyService);
            currencyModalWindow.addDetachListener(e -> updateList());
            currencyModalWindow.open();
        });
        grid.addSelectionListener(e -> selectedNumberField.setValue((double) e.getAllSelectedItems().size()));
    }

}
