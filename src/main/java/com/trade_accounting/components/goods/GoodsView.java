package com.trade_accounting.components.goods;


import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridConfigurer;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.NaiveXlsTableBuilder;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.components.util.configure.components.select.SelectConstants;
import com.trade_accounting.components.util.configure.components.select.SelectExt;
import com.trade_accounting.models.dto.warehouse.ProductDto;
import com.trade_accounting.models.dto.warehouse.ProductGroupDto;
import com.trade_accounting.services.interfaces.warehouse.ProductGroupService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.trade_accounting.config.SecurityConstants.*;

@Slf4j
@SpringComponent
//Если на страницу не ссылаются по URL или она не является отдельной страницей, а подгружается родительским классом, то URL и Title не нужен
//@Route(value = GOODS_CUSTOMERS_PRODUCTS_VIEW, layout = AppView.class)
@UIScope
public class GoodsView extends VerticalLayout {

    private final Grid<ProductDto> grid;
    private final GridConfigurer<ProductDto> gridConfigurer;
    private final ProductService productService;
    private final ProductGroupService productGroupService;
    private final GoodsModalWindow goodsModalWindow;
    private final ServiceModalWindow serviceModalWindow;
    private final SetModalWindow setModalWindow;
    private final TreeGrid<ProductGroupDto> treeGrid;
    private final GridFilter<ProductDto> filter;
    private final GridPaginator<ProductDto> paginator;
    private final Notifications notifications;
    private final ProductGroupModalWindow productGroupModalWindow;
    private Optional<ProductGroupDto> optional = Optional.empty();
    private final GoodsEditAddView goodsEditAddView;
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};
    private final KitsEditAddView kitsEditAddView;

    @Autowired
    public GoodsView(ProductService productService,
                     ProductGroupService productGroupService,
                     GoodsModalWindow goodsModalWindow,
                     ServiceModalWindow serviceModalWindow,
                     SetModalWindow setModalWindow, Notifications notifications,
                     ProductGroupModalWindow productGroupModalWindow, GoodsEditAddView goodsEditAddView,
                     KitsEditAddView kitsEditAddView) {
        this.kitsEditAddView = kitsEditAddView;
        this.goodsEditAddView = goodsEditAddView;
        this.setModalWindow = setModalWindow;
        this.productGroupModalWindow = productGroupModalWindow;
        this.grid = new Grid<>(ProductDto.class);
        this.gridConfigurer = new GridConfigurer<>(grid);
        this.productService = productService;
        this.productGroupService = productGroupService;
        this.goodsModalWindow = goodsModalWindow;
        this.serviceModalWindow = serviceModalWindow;
        this.notifications = notifications;
        treeGrid = getTreeGrid();
        List<ProductDto> data = getData();
        configureGrid();
        this.filter = new GridFilter<>(this.grid);
        configureFilter();
        this.paginator = new GridPaginator<>(this.grid, data,50);
        setHorizontalComponentAlignment(Alignment.CENTER);
        add(getUpperLayout(), filter, getMiddleLayout(this.grid), paginator);
    }

    private List<ProductDto> getData() {
        return productService.getAll();
    }

    private List<ProductDto> filteredByGroupData(ProductGroupDto productGroupDto) {
        return productService.getAllByProductGroup(productGroupDto);
    }

    public void updateData() {
        paginator.setData(getData(), false);
        updateTreeGrid(productGroupService.getAll());
    }

    private Component getUpperLayout() {
        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.add(buttonQuestion(), title(), buttonRefresh());
        mainLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout upperLayout = new HorizontalLayout();
        upperLayout.add(buttonPlusGoods(),
                buttonPlusService(), buttonPlusSet(), buttonPlusGroup(),
                buttonFilter());

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(text(), BigDecimalField(), valueSelect(), valueSelectPrint(),
                valueSelectImport(), valueSelectExport(), buttonSettings());
        verticalLayout.add(upperLayout, horizontalLayout);
        mainLayout.add(verticalLayout);
        return mainLayout;
    }

    private SplitLayout getMiddleLayout(Grid<ProductDto> grid) {
        SplitLayout middleLayout = new SplitLayout();
        middleLayout.setWidth("100%");
        middleLayout.setHeight("66vh");
        middleLayout.addToPrimary(treeGrid);
        middleLayout.addToSecondary(grid);
        return middleLayout;
    }

    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.removeAllColumns();
        grid.setWidth("75%");
        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.setColumns("id", "name", "itemNumber", "description", "weight", "volume", "purchasePrice");
        grid.getColumnByKey("id").setHeader("id").setId("ID");
        grid.getColumnByKey("name").setHeader("Наименование").setId("Наименование");
        grid.getColumnByKey("weight").setHeader("Вес").setTextAlign(ColumnTextAlign.END).setId("Вес");
        grid.getColumnByKey("volume").setHeader("Объем").setTextAlign(ColumnTextAlign.END).setId("Объем");
        grid.getColumnByKey("itemNumber").setHeader("Артикул").setId("Артикул");
        grid.getColumnByKey("description").setHeader("Описание").setId("Описание");
        grid.getColumnByKey("purchasePrice").setHeader("Закупочная цена").setTextAlign(ColumnTextAlign.END).setId("Закупочная цена");
        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        gridConfigurer.addConfigColumnToGrid();

        grid.addItemDoubleClickListener(event -> {
            ProductDto productDto = event.getItem();
            goodsEditAddView.setProductDataForEdit(productDto);
            goodsEditAddView.setLocation(GOODS);
            UI.getCurrent().navigate(GOODS_GOODS__EDIT_VIEW);
        });
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.onSearchClick(e ->
                paginator.setData(productService.searchByFilter(filter.getFilterData())));
        filter.onClearClick(e -> {
            if(optional.isPresent()){
                paginator.setData(filteredByGroupData(optional.get()), false);
            } else {
                paginator.setData(productService.getAll());
            }
        });
    }

    private TreeGrid<ProductGroupDto> getTreeGrid() {
        TreeGrid<ProductGroupDto> treeGridLocal = new TreeGrid<>();
        treeGridLocal.setHeight("66vh");
        treeGridLocal.setWidth("25%");
        treeGridLocal.setThemeName("dense", true);
        treeGridLocal.addClassName("treeGreed");

        Grid.Column<ProductGroupDto> column = treeGridLocal
                .addHierarchyColumn(x -> "")
                .setHeader("Товарная группа")
                .setFlexGrow(0)
                .setWidth("auto")
                .setSortProperty("sortNumber")
                .setComparator(Comparator.comparing(ProductGroupDto::getSortNumber));

        treeGridLocal.addColumn(ProductGroupDto::getName);
        HeaderRow.HeaderCell cell = treeGridLocal.appendHeaderRow().getCell(column);
        Label label = new Label();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setPadding(false);
        horizontalLayout.setMargin(false);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        Button closeButton = new Button(new Icon(VaadinIcon.CLOSE));
        closeButton.setVisible(false);
        closeButton.setMaxHeight("20px");
        horizontalLayout.add(label, closeButton);
        closeButton.addClickListener(event -> {
            closeButton.setVisible(false);
            label.setText("");
            paginator.setData(getData(), false);
            treeGridLocal.deselectAll();
        });
        cell.setComponent(horizontalLayout);
        treeGridLocal.setSelectionMode(Grid.SelectionMode.SINGLE);
        treeGridLocal.addSelectionListener(event -> {
            optional = event.getFirstSelectedItem();
            if (optional.isPresent()) {
                paginator.setData(filteredByGroupData(optional.get()), false);
                label.setText(optional.get().getName());
                closeButton.setVisible(true);
            }
        });
        return treeGridLocal;
    }

    private void updateTreeGrid(List<ProductGroupDto> productGroupData) {
        treeGrid.getTreeData().clear();
        List<ProductGroupDto> buffer = new ArrayList<>();
        ProductGroupDto element;
        ProductGroupDto parent;
        while (!productGroupData.isEmpty()) {
            for (int i = 0; i < productGroupData.size(); i++) {
                element = productGroupData.get(i);
                if (element.getParentId() == null) {
                    treeGrid.getTreeData().addItem(null, element);
                    buffer.add(element);
                    productGroupData.remove(element);
                } else if (!buffer.isEmpty()) {
                    for (int j = 0; j < buffer.size(); j++) {
                        parent = buffer.get(j);
                        if (parent.getId().equals(element.getParentId())) {
                            treeGrid.getTreeData().addItem(parent, element);
                            buffer.add(element);
                            productGroupData.remove(element);
                            break;
                        }
                    }
                }
            }
        }
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion("В разделе представлены все ваши товары, услуги и комплекты. " +
                "артикулом по характеристикам (например, размеру или цвету) удобно с помощью модификаций. " +
                "Несколько единиц одного товара можно продавать упаковками. А комплекты позволяют продавать " +
                "наборы разных товаров и услуг как единое целое. " +
                "Каталог товаров можно импортировать и экспортировать.");
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonPlusGoods() {
        Button addGoodsButton = new Button("Товар", new Icon(VaadinIcon.PLUS_CIRCLE));
        addGoodsButton.addClickListener(e -> {
            goodsEditAddView.setLocation(GOODS);
            UI.getCurrent().navigate(GOODS_GOODS__EDIT_VIEW);
        });
        addGoodsButton.getStyle().set("cursor", "pointer");
        return addGoodsButton;
    }

    private Button buttonPlusService() {
        Button addServiceButton = new Button("Услуга", new Icon(VaadinIcon.PLUS_CIRCLE));
        addServiceButton.addClickListener(e -> serviceModalWindow.open());
        addServiceButton.getStyle().set("cursor", "pointer");
        return addServiceButton;
    }

    private Button buttonPlusSet() {
        Button addSetButton = new Button("Комплект", new Icon(VaadinIcon.PLUS_CIRCLE));
        addSetButton.addClickListener(e -> {
            kitsEditAddView.setLocation(GOODS);
            UI.getCurrent().navigate(GOODS_KITS__EDIT_VIEW);
        });
        addSetButton.getStyle().set("cursor", "pointer");
        return addSetButton;
    }

    private Button buttonPlusGroup() {
        Button addProductGroupButton = new Button("Группа", new Icon(VaadinIcon.PLUS_CIRCLE));
        addProductGroupButton.addClickListener(e -> productGroupModalWindow.open());
        addProductGroupButton.getStyle().set("cursor", "pointer");
        return addProductGroupButton;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private TextField text() {
        TextField text = new TextField();
        text.setPlaceholder("Наименование, код или артикул");
        text.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        text.setClearButtonVisible(true);
        text.setValueChangeMode(ValueChangeMode.EAGER);
        text.addValueChangeListener(e -> updateList(text));
        text.setWidth("300px");
        return text;
    }

    private void updateList(TextField text) {
        paginator.setData(getData(), false);
    }

    private H2 title() {
        H2 textCompany = new H2("Товары и услуги");
        textCompany.setHeight("2.2em");
        return textCompany;
    }

    private BigDecimalField BigDecimalField() {
        BigDecimalField numberField = new BigDecimalField();
        numberField.setPlaceholder("0");
        numberField.setWidth("35px");
        return numberField;
    }

    private Select<String> valueSelect() {
        return SelectConfigurer.configureDeleteSelect(() -> {
            deleteSelectedGoods();
            grid.deselectAll();
            paginator.setData(getData());
        });
    }

    private void deleteSelectedGoods() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (ProductDto productDto : grid.getSelectedItems()) {
                productService.deleteById(productDto.getId());
                notifications.infoNotification("Выбранные товары успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные товары");
        }
    }

    private Select<String> valueSelectPrint() {
        Select<String> valueSelect = SelectConfigurer.configurePrintSelect();
        Anchor anchor = new Anchor(new StreamResource("goods.xls",
                this::buildXlsTable), "Список товаров");
        valueSelect.add(anchor);
        return valueSelect;
    }

    private Select<String> valueSelectImport() {
        return new SelectExt.SelectBuilder<String>()
                .item("Импорт")
                .defaultValue("Импорт")
                .width(SelectConstants.SELECT_WIDTH_120PX)
                .build();
    }

    private Select<String> valueSelectExport() {
        return new SelectExt.SelectBuilder<String>()
                .item("Экспорт")
                .defaultValue("Экспорт")
                .width(SelectConstants.SELECT_WIDTH_120PX)
                .build();
    }

    private InputStream buildXlsTable() {
        return new NaiveXlsTableBuilder<ProductDto>().header("Товары")
                .metadata("Создал: ")
                .columns("№", "Наименование", "Описание", "Артикул", "Вес", "Объем", "Закупочная цена")
                .mappings(
                        (product, cell) -> cell.setCellValue(product.getId()),
                        (product, cell) -> cell.setCellValue(product.getName()),
                        (product, cell) -> cell.setCellValue(product.getDescription()),
                        (product, cell) -> cell.setCellValue(product.getItemNumber()),
                        (product, cell) -> cell.setCellValue(product.getWeight().doubleValue()),
                        (product, cell) -> cell.setCellValue(product.getVolume().doubleValue()),
                        (product, cell) -> cell.setCellValue(product.getPurchasePrice().doubleValue()))
                .data(productService.getAll())
                .getAsStream();

    }
}
