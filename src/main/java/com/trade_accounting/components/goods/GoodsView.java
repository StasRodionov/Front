package com.trade_accounting.components.goods;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.models.dto.ProductGroupDto;
import com.trade_accounting.services.interfaces.ProductGroupService;
import com.trade_accounting.services.interfaces.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.klaudeta.PaginatedGrid;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Route(value = "good", layout = AppView.class)
@PageTitle("Товары и услуги")
public class GoodsView extends VerticalLayout {

    private final String VALUE_SELECT_WIDTH = "120px";

    private final ProductService productService;
    private final ProductGroupService productGroupService;

    private List<ProductDto> products;
    private List<ProductDto> filteredData = new LinkedList<>();
    private List<ProductGroupDto> productGroupData;//данные для древовидного layout

    private Grid<ProductDto> grid;
    private TreeGrid<ProductGroupDto> treeGrid;//древовидный layout
    private HorizontalLayout upperLayout;
    private HorizontalLayout printLayout;
    private SplitLayout middleLayout;//двухоконный layout в котором размещаются грид и древовидный грид
    private GridPaginator<ProductDto> paginator;


    public GoodsView(ProductService productService, ProductGroupService productGroupService) {
        this.productService = productService;
        this.productGroupService = productGroupService;

        loadProducts();
        setGrid();
        setTreeGrid();
        setUpperLayout();
        setPrintLayout();
        setMiddleLayout();
        setPaginator();

        add(upperLayout, printLayout, middleLayout, paginator);
    }

    private void loadProducts() {
//        products = productService.getAll();
        products = new ArrayList<>();
        List<ProductDto> p = productService.getAll();
    }

    private Button buttonQuestion() {
        Button button = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return button;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonPlusGoods() {
        return new Button("Товар", new Icon(VaadinIcon.PLUS_CIRCLE));
    }

    private Button buttonPlusService() {
        return new Button("Услуга", new Icon(VaadinIcon.PLUS_CIRCLE));
    }

    private Button buttonPlusSet() {
        return new Button("Комплект", new Icon(VaadinIcon.PLUS_CIRCLE));
    }

    private Button buttonPlusGroup() {
        return new Button("Группа", new Icon(VaadinIcon.PLUS_CIRCLE));
    }

    private Button buttonFilter() {
        return new Button("Фильтр");
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private TextField text() {
        TextField text = new TextField();
        text.setPlaceholder("Наименование, код или артикул");
        text.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        text.setWidth("300px");
        return text;
    }

    private H2 title() {
        H2 textCompany = new H2("Товары и услуги");
        textCompany.setHeight("2.2em");
        return textCompany;
    }

    private NumberField numberField() {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("35px");
        return numberField;
    }

    private Select<String> valueSelect() {
        Select<String> valueSelect = new Select<>();
        valueSelect.setWidth(VALUE_SELECT_WIDTH);
        valueSelect.setItems("Изменить");
        valueSelect.setValue("Изменить");
        return valueSelect;
    }

    private Select<String> valueSelectPrint() {
        Select<String> valueSelect = new Select<>();
        valueSelect.setWidth(VALUE_SELECT_WIDTH);
        valueSelect.setItems("Печать");
        valueSelect.setValue("Печать");
        return valueSelect;
    }

    private Select<String> valueSelectImport() {
        Select<String> valueSelect = new Select<>();
        valueSelect.setWidth(VALUE_SELECT_WIDTH);
        valueSelect.setItems("Импорт");
        valueSelect.setValue("Импорт");
        return valueSelect;
    }

    private Select<String> valueSelectExport() {
        Select<String> valueSelect = new Select<>();
        valueSelect.setWidth(VALUE_SELECT_WIDTH);
        valueSelect.setItems("Экспорт");
        valueSelect.setValue("Экспорт");
        return valueSelect;
    }

    private void setUpperLayout() {
        upperLayout = new HorizontalLayout();
        upperLayout.add(buttonQuestion(), title(), buttonRefresh(), buttonPlusGoods(),
                buttonPlusService(), buttonPlusSet(), buttonPlusGroup(),
                buttonFilter(), text(), numberField(), valueSelect(), valueSelectPrint(),
                valueSelectImport(), valueSelectExport(), buttonSettings());
        upperLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
    }

    private void setPrintLayout() {
        printLayout = new HorizontalLayout();
        printLayout.add(numberField(), valueSelect(), valueSelectPrint());
        printLayout.setSpacing(false);
    }

    private void setMiddleLayout() {
        middleLayout = new SplitLayout();
        middleLayout.setWidth("100%");
        middleLayout.setHeight("66vh");
        middleLayout.addToPrimary(treeGrid);
        middleLayout.addToSecondary(grid);
    }

    private void setPaginator() {
        paginator = new GridPaginator<>(grid, products, 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
    }

    private void setGrid() {
        grid = new PaginatedGrid<>(ProductDto.class);
        grid.setWidth("80%");
        grid.setHeight("100%");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.setColumns("name", "description", "weight", "volume", "purchasePrice");
        grid.getColumnByKey("name").setHeader("Наименование");
        grid.getColumnByKey("description").setHeader("Артикул");
        grid.getColumnByKey("weight").setHeader("Вес");
        grid.getColumnByKey("volume").setHeader("Объем");
        grid.getColumnByKey("purchasePrice").setHeader("Закупочная цена");
        grid.setItems(products);
    }

    /**
     * Метод возвращает подготовленный древовидный грид
    **/
    public void setTreeGrid() {
        treeGrid = new TreeGrid<>();
        treeGrid.setHeight("100%");
        treeGrid.setWidth("20%");
        treeGrid.setThemeName("dense", true);

        treeGrid.addHierarchyColumn(ProductGroupDto::getName)
                .setSortProperty("sortNumber")
                .setComparator(Comparator.comparing(ProductGroupDto::getSortNumber))
                .setHeader("Товарная группа");
        HeaderRow header = treeGrid.appendHeaderRow();
        treeGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        SingleSelect<Grid<ProductGroupDto>, ProductGroupDto> singleSelect = treeGrid.asSingleSelect();
        singleSelect.addValueChangeListener(event -> {
            ProductGroupDto selected = event.getValue();
            filteredData.clear();
            filteredData = productService.getAllByProductGroupId(selected.getId());
            paginator.setData(filteredData);
            header.getCells().forEach(x -> x.setText(selected.getName()));
        });
        updateTreeGrid();
    }

    /**
     * Метод обновляет содержимое древовидного грида
     */
    private void updateTreeGrid() {
        List<ProductGroupDto> buffer = new LinkedList<>();

        ProductGroupDto element;
        ProductGroupDto parent;
        productGroupData = productGroupService.getAll();
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
}
