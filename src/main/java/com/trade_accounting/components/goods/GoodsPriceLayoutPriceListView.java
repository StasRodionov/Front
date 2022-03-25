package com.trade_accounting.components.goods;

import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.models.dto.company.PriceListDto;
import com.trade_accounting.models.dto.warehouse.ProductDto;
import com.trade_accounting.services.interfaces.warehouse.ProductGroupService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
@Slf4j
public class GoodsPriceLayoutPriceListView extends VerticalLayout implements AfterNavigationObserver {

    private PriceListDto data;
    private Grid<ProductDto> grid;
    private final VerticalLayout upperLayout;
    private final GoodsPriceLayout containingView;
    private final ProductGroupService productGroupService;
    private final ProductService productService;

    public GoodsPriceLayoutPriceListView(GoodsPriceLayout containingView, ProductService productService, ProductGroupService productGroupService) {
        this.containingView = containingView;
        this.productService = productService;
        this.productGroupService = productGroupService;
        this.setVisible(false);
        this.upperLayout = new VerticalLayout();
        this.grid = new Grid<>(ProductDto.class, false);
        configureGrid();
        add(upperLayout, grid);
    }


    private void configureUpperLayout() {
        HorizontalLayout line1 = new HorizontalLayout();
        line1.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        line1.add(getSaveButton(),
                getCloseButton(),
                getPriceListChanger(),
                getActionsMenuBar());

        HorizontalLayout line2 = new HorizontalLayout();
        line2.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        Label label1 = new Label("Прайс-лист №");
        TextField listName = new TextField();
        listName.setValue(data.getNumber());
        Label label2 = new Label("от");
        TextField creationDate = new TextField();
        creationDate.setValue(data.getTime());
        MenuBar status = new MenuBar();
        MenuItem menuItem1 = status.addItem("Статус");
        SubMenu menuItem1SubMenu = menuItem1.getSubMenu();
        menuItem1SubMenu.addItem("Настроить");
        Button questionButton = Buttons.buttonQuestion("Непроведенный документ — это черновик. Он не учитывается в отчетах.");
        Checkbox checkbox = new Checkbox("Проведено", data.getSent());
        line2.add(label1, listName, label2, creationDate, status, questionButton, checkbox);

        HorizontalLayout line3 = new HorizontalLayout();
        line3.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        Button addPositionButton = new Button("Добавить позицию", new Icon(VaadinIcon.PLUS));
        Button deleteButton = new Button("Удалить");
        Button filterButton = new Button("Фильтр");
        TextField filterCriteria = new TextField();
        line3.add(addPositionButton, deleteButton, filterButton, filterButton);

        upperLayout.removeAll();
        upperLayout.add(line1, line2, line3);
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn(ProductDto::getId).setKey("productID").setHeader("Код").setSortable(true).setId("Код");
        grid.addColumn(item -> "").setKey("vendorCode").setHeader("Артикул").setId("Артикул");
        grid.addColumn(ProductDto::getName).setKey("productName").setHeader("Наимнование").setId("Наимнование");
        grid.addColumn(productDto ->  (productGroupService.getById(productDto.getProductGroupId())).getName()).setKey("groupName")
                .setHeader("Группа").setId("Группа");
        grid.addColumn(item -> "").setKey("list2").setHeader("Лист 2").setId("Лист2");
        grid.setHeight("66vh");
        grid.setMaxWidth("2500px");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addItemClickListener(event -> {/*show modal*/});

    }

    public void updateList() {
        List<ProductDto> productDtoList = productService.getAll().stream()
                .filter(p -> data.getProductsIds().contains(p.getId()))
                .collect(Collectors.toList());
        System.out.println(productDtoList);
        grid.setItems(productDtoList);
    }

    private Button getSaveButton() {
        return new Button("Сохранить", e -> {/*TODO*/});
    }

    private Button getCloseButton() {
        return new Button("Закрыть", e -> {
            this.setVisible(false);
            containingView.showPriceLists();
        });
    }

    private MenuBar getPriceListChanger() {
        MenuBar menuBar = new MenuBar();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_ICON);
        menuBar.addItem(new Text("x из y"));
        menuBar.addItem(new Button(new Icon(VaadinIcon.CARET_LEFT)));
        menuBar.addItem(new Button(new Icon(VaadinIcon.CARET_RIGHT)));
        return menuBar;
    }

    private MenuBar getActionsMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_ICON);

        MenuItem actions = menuBar.addItem("Действия");
        SubMenu actionsSubMenu = actions.getSubMenu();
        actionsSubMenu.addItem("Дополнить из остатков");
        actionsSubMenu.addItem("Дополнить из номенклатуры");

        MenuItem change = menuBar.addItem("Изменить");
        SubMenu changeSubmenu = change.getSubMenu();
        changeSubmenu.addItem("Удалить");
        changeSubmenu.addItem("Копировать");

        MenuItem print = menuBar.addItem(new Icon(VaadinIcon.PRINT));
        print.add("Печать");
        SubMenu printSubMenu = print.getSubMenu();
        printSubMenu.addItem("Ценники");

        MenuItem send = menuBar.addItem(new Icon(VaadinIcon.ENVELOPE));
        send.add("Отправить");
        SubMenu sendSubMenu = send.getSubMenu();
        sendSubMenu.addItem("Ценники");
        sendSubMenu.addItem("Прайс-лист");

        return menuBar;
    }

    public void fillContent(PriceListDto priceList) {
        this.data = priceList;
        configureUpperLayout();
        updateList();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
