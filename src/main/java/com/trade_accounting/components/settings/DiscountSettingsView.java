package com.trade_accounting.components.settings;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.MenuBarIcon;
import com.trade_accounting.models.dto.util.DiscountDto;
import com.trade_accounting.services.interfaces.util.DiscountService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

import static com.trade_accounting.config.SecurityConstants.PROFILE_PROFILE__SETTINGS__DISCOUNT_SETTINGS;

@Route(value = PROFILE_PROFILE__SETTINGS__DISCOUNT_SETTINGS, layout = SettingsView.class)
@PageTitle("Скидки")
@Slf4j
public class DiscountSettingsView extends VerticalLayout {

    private final DiscountService discountService;

    private final Grid<DiscountDto> grid = new Grid<>();


    public DiscountSettingsView(DiscountService discountService) {
        this.discountService = discountService;
        GridPaginator<DiscountDto> paginator = new GridPaginator<>(grid);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        configureGrid();
        add(getAppView(), header(), grid, paginator);
    }

    private HorizontalLayout header() {
        HorizontalLayout header = new HorizontalLayout();
        header.add(buttonQuestion(), title(), buttonRefresh(), addButton(), filterMenu(), settingsButton());
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return header;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        buttonRefresh.addClickListener(e -> grid.setItems(discountService.getAll()));
        return buttonRefresh;
    }

    private MenuBar addButton() {
        MenuBar addButton = new MenuBar();
        addButton.addThemeVariants(MenuBarVariant.LUMO_ICON);
        MenuItem add = MenuBarIcon.createIconItem(addButton, VaadinIcon.PLUS, "Скидка", null);
        SubMenu addSubMenu = add.getSubMenu();
        addSubMenu.addItem("Специальная цена");
        addSubMenu.addItem("Персональная скидка");
        addSubMenu.addItem("Накопительная скидка");
        addSubMenu.addItem("Бонусная программа");

        return addButton;
    }

    private MenuBar filterMenu() {
        MenuBar filter = new MenuBar();
        filter.addItem("Все скидки", menuItemClickEvent -> grid.setItems(discountService.getAll()));
        filter.addItem("Только активные", menuItemClickEvent -> grid.setItems(discountService.getAll().stream()
                .filter(DiscountDto::getIsActive)
                .collect(Collectors.toList())
        ));
        return filter;
    }

    private Button settingsButton() {

        return new Button(new Icon(VaadinIcon.COG));
    }

    private AppView getAppView() {
        return new AppView();
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion("\t\n" +
                "Вы можете устанавливать для контрагентов и на отдельные товары разные типы  скидок: специальную цену," +
                " персональную или накопительную скидку, округление копеек, бонусные программы.\n" +
                "\n" +
                "Чтобы скидка действовала, необходимо поставить флажок в поле Действует. Чтобы выключить скидку, " +
                "флажок нужно убрать. При этом сама скидка останется в списке, ее можно будет включить позже.\n" +
                "\n" +
                "В разделе Розница → Точки продаж можно ограничить скидки, которые кассир может установить в приложении");
    }

    private H2 title() {
        H2 title = new H2("Скидки");
        title.setHeight("2.2em");
        return title;
    }



    private void configureGrid() {
        grid.setItems(discountService.getAll());
        grid.addColumn(DiscountDto::getIsActive).setHeader("Активность");
        grid.addColumn(DiscountDto::getName).setHeader("Наименование");
        grid.addColumn(DiscountDto::getType).setHeader("Тип");
    }
}
