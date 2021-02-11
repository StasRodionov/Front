package com.trade_accounting.components.goods;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.authentication.LoginView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.models.dto.ProductGroupDto;
import com.trade_accounting.services.interfaces.ProductGroupService;
import com.trade_accounting.services.interfaces.ProductService;
import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.klaudeta.PaginatedGrid;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Route(value = "good", layout = AppView.class)
@PageTitle("Товары и услуги")
public class GoodsView extends VerticalLayout {

    private final ProductService productService;
    private final ProductGroupService productGroupService;
    private List<ProductDto> data;
    private GridPaginator<ProductDto> paginator;
    private Grid<ProductDto> grid;
    private TreeGrid<ProductGroupDto> productGroupTree = new TreeGrid<>();//древовидный layout
    private List<ProductDto> filteredData = new LinkedList<>();
    private List<ProductGroupDto> productGroupData;//данные для древовидного layout
    private SplitLayout middleLayout;//двухоконный layout в котором размещаются грид и древовидный грид

    public GoodsView(ProductService productService, ProductGroupService productGroupService) {
        this.productService = productService;
        this.productGroupService = productGroupService;
        data = productService.getAll();
        middleLayout = new SplitLayout();

        middleLayout.setWidth("100%");
        middleLayout.setHeight("64vh");

        try {
            grid = grid();
            productGroupTree = treeGrid();
            grid.setWidth("80%");
            grid.setHeight("100%");
            productGroupTree.setHeight("100%");
            productGroupTree.setWidth("20%");
            productGroupTree.setThemeName("dense", true);
            middleLayout.addToPrimary(productGroupTree);
            middleLayout.addToSecondary(grid);
            paginator = new GridPaginator<>(grid, data, 100);
            setHorizontalComponentAlignment(Alignment.CENTER, paginator);
            add(upperLayout(), middleLayout, paginator);
        } catch (NullPointerException e) {
            WrappedSession wrappedSession = VaadinSession.getCurrent().getSession();
            wrappedSession.setAttribute("redirectDestination", "/good");
            UI.getCurrent().navigate(LoginView.class);
        }
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
        Button button = new Button("Товар", new Icon(VaadinIcon.PLUS_CIRCLE));
        return button;
    }

    private Button buttonPlusService() {
        Button button = new Button("Услуга", new Icon(VaadinIcon.PLUS_CIRCLE));
        return button;
    }

    private Button buttonPlusSet() {
        Button button = new Button("Комплект", new Icon(VaadinIcon.PLUS_CIRCLE));
        return button;
    }

    private Button buttonPlusGroup() {
        Button button = new Button("Группа", new Icon(VaadinIcon.PLUS_CIRCLE));
        return button;
    }

    private Button buttonFilter() {
        Button button = new Button("Фильтр");
        return button;
    }

    private Button buttonSettings() {
        Button button = new Button(new Icon(VaadinIcon.COG_O));
        return button;
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
        valueSelect.setWidth("120px");
        valueSelect.setItems("Изменить");
        valueSelect.setValue("Изменить");
        return valueSelect;
    }

    private Select<String> valueSelectPrint() {
        Select<String> valueSelect = new Select<>();
        valueSelect.setWidth("120px");
        valueSelect.setItems("Печать");
        valueSelect.setValue("Печать");
        return valueSelect;
    }

    private Select<String> valueSelectImport() {
        Select<String> valueSelect = new Select<>();
        valueSelect.setWidth("120px");
        valueSelect.setItems("Импорт");
        valueSelect.setValue("Импорт");
        return valueSelect;
    }

    private Select<String> valueSelectExport() {
        Select<String> valueSelect = new Select<>();
        valueSelect.setWidth("120px");
        valueSelect.setItems("Экспорт");
        valueSelect.setValue("Экспорт");
        return valueSelect;
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upperLayout = new HorizontalLayout();
        HorizontalLayout printLayout = new HorizontalLayout();
        printLayout.add(
                numberField(), valueSelect(), valueSelectPrint());
        printLayout.setSpacing(false);
        upperLayout.add(buttonQuestion(), title(), buttonRefresh(), buttonPlusGoods(), buttonPlusService(),
                buttonPlusSet(), buttonPlusGroup(),
                buttonFilter(), text(), numberField(), valueSelect(), valueSelectPrint(),
                valueSelectImport(),
                valueSelectExport(), buttonSettings());
        upperLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upperLayout;
    }

    private Grid<ProductDto> grid() {
        PaginatedGrid<ProductDto> grid = new PaginatedGrid<>(ProductDto.class);
//        try {
            grid.setItems(productService.getAll());
//        } catch (NullPointerException e) {
//            WrappedSession wrappedSession = VaadinSession.getCurrent().getSession();
//            wrappedSession.setAttribute("redirectDestination", "/good");
//            UI.getCurrent().navigate(LoginView.class);
//        }

        grid.setColumns("name", "description", "weight", "volume",
                "purchasePrice");
        grid.getColumnByKey("name").setHeader("Наименование");
        grid.getColumnByKey("description").setHeader("Артикул");
        grid.getColumnByKey("weight").setHeader("Вес");
        grid.getColumnByKey("volume").setHeader("Объем");
        grid.getColumnByKey("purchasePrice").setHeader("Закупочная цена");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        return grid;
    }

    /**
     * Метод возвращает подготовленный древовидный грид
    **/
    public TreeGrid<ProductGroupDto> treeGrid() {

        //productGroupTree.addHierarchyColumn(ProductGroupDto::getName).setHeader("");

        productGroupTree.addHierarchyColumn(ProductGroupDto::getName)
                .setSortProperty("sortNumber")
                .setComparator(Comparator.comparing(ProductGroupDto::getSortNumber))
                .setHeader("Товарная группа");
        HeaderRow header = productGroupTree.appendHeaderRow();
        productGroupTree.setSelectionMode(Grid.SelectionMode.SINGLE);
        SingleSelect<Grid<ProductGroupDto>, ProductGroupDto> singleSelect = productGroupTree.asSingleSelect();
        singleSelect.addValueChangeListener(event -> {
            ProductGroupDto selected = event.getValue();
            filteredData.clear();
            filteredData = productService.getAllByProductGroupId(selected.getId());
            paginator.setData(filteredData);
            header.getCells().forEach(x -> x.setText(selected.getName()));
        });

        updateTreeGrid();
        return productGroupTree;
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
                    productGroupTree.getTreeData().addItem(null, element);
                    buffer.add(element);
                    productGroupData.remove(element);
                } else if (!buffer.isEmpty()) {
                    for (int j = 0; j < buffer.size(); j++) {
                        parent = buffer.get(j);
                        if (parent.getId() == element.getParentId()) {
                            productGroupTree.getTreeData().addItem(parent, element);
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
