package com.trade_accounting.components.settings;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.profile.SalesChannelModalWindow;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;

import com.trade_accounting.models.dto.units.SalesChannelDto;
import com.trade_accounting.services.interfaces.units.SalesChannelService;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.trade_accounting.config.SecurityConstants.PROFILE_PROFILE__SETTINGS__SALES_CHANNEL_SETTINGS;

@Route(value = PROFILE_PROFILE__SETTINGS__SALES_CHANNEL_SETTINGS, layout = SettingsView.class)
@PageTitle("Учетная запись")
@Slf4j
public class SalesChannelSettingsView extends VerticalLayout {

    private final NumberField selectedNumberField;
    private final List<SalesChannelDto> data;
    SalesChannelService salesChannelService;
    private Grid<SalesChannelDto> grid = new Grid<>(SalesChannelDto.class);
    private GridPaginator<SalesChannelDto> paginator;
    private final GridFilter<SalesChannelDto> filter;

    public SalesChannelSettingsView(SalesChannelService salesChannelService) {
        this.selectedNumberField = getSelectedField();
        this.salesChannelService = salesChannelService;
        this.data = salesChannelService.getAll();
        paginator = new GridPaginator<SalesChannelDto>(grid, salesChannelService.getAll(), 50);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        configureGrid();
        this.filter = new GridFilter<>(grid);
        add(getAppView(), horizontalLayout(), filter, horizontalLayout2(), horizontalLayout3(), grid, paginator);
    }


    private AppView getAppView() {
        return new AppView();
    }

    private H2 title() {
        H2 title = new H2("Каналы продаж");
        title.setHeight("2.2em");
        return title;
    }

    private HorizontalLayout horizontalLayout() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.add(title(), buttonRefresh(), buttonSalesChannel(), getButtonFilter(), textField());
      hl.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return hl;
    }

    private HorizontalLayout horizontalLayout2() {
        HorizontalLayout hl2 = new HorizontalLayout();
        hl2.add("При подключении интернет-магазинов каналы продаж создаются автоматически. Добавьте дополнительные каналы, например для мессенджеров и соцсетей, и анализируйте продажи в отчете \"Прибыльность\".\n" +
                "\n" +
                "В Заказах покупателя каналы проставляются несколькими способами:\n" +
                "\n" +
                "1) Автоматически при выгрузке из интернет-магазина;\n" +
                "2) Вручную в каждом документе;\n" +
                "3) С помощью массового редактирования;\n" +
                "4) В рамках сценария.\n" +
                "Далее канал автоматически отмечается в Отгрузках и Возвратах, связанных с Заказом покупателя.\n");
        return hl2;
    }

    private HorizontalLayout horizontalLayout3() {
        HorizontalLayout hl3 = new HorizontalLayout();
        hl3.add(new Anchor("https://support.moysklad.ru/hc/ru/articles/4403265549585", "Каналы продаж"));
        return hl3;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonSalesChannel() {
        Button salesChannelButton = new Button("Канал продаж", new Icon(VaadinIcon.PLUS_CIRCLE));
        SalesChannelModalWindow salesChannelModalWindow = new SalesChannelModalWindow(new SalesChannelDto(), salesChannelService);
        salesChannelButton.addClickListener(event -> salesChannelModalWindow.open());
        salesChannelButton.addDetachListener(event -> updateList());
        return salesChannelButton;
    }

    private Button getButtonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
    }

    private TextField textField() {
        TextField text = new TextField();
        text.setPlaceholder("Наименование");
        text.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        text.setValueChangeMode(ValueChangeMode.EAGER);
        //text.addValueChangeListener(event -> updateList(text.getValue()));
        text.setWidth("300px");
        return text;
    }

    private void updateList() {
        grid.setItems(salesChannelService.getAll());
    }


    public void configureGrid() {
        grid.setItems(salesChannelService.getAll());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setColumns("name", "type", "description");
        grid.getColumnByKey("name").setHeader("Наименование").setId("Наименование");
        grid.getColumnByKey("type").setHeader("Тип").setId("Тип");
        grid.getColumnByKey("description").setHeader("Описание").setId("Описание");
        grid.setHeight("64vh");

        grid.addItemDoubleClickListener(event -> {
            SalesChannelDto editSalesChabbel = event.getItem();
            SalesChannelModalWindow salesChannelModalWindow =
                    new SalesChannelModalWindow(editSalesChabbel, salesChannelService);
            salesChannelModalWindow.addDetachListener(e -> updateList());
            salesChannelModalWindow.open();
        });
        grid.addSelectionListener(e -> selectedNumberField.setValue((double) e.getAllSelectedItems().size()));
    }


    private NumberField getSelectedField() {
        final NumberField numberField = new NumberField();
        numberField.setWidth("50px");
        numberField.setValue(0D);
        return numberField;
    }
}
