package com.trade_accounting.components.util;

import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.models.dto.ProductGroupDto;
import com.trade_accounting.services.interfaces.ProductGroupService;
import com.trade_accounting.services.interfaces.ProductService;
import com.vaadin.flow.component.board.Row;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.function.ValueProvider;

import java.util.LinkedList;
import java.util.List;

public class ProductGroupGridFiller {

    private ProductGroupService productGroupService;
    private ProductService productService;
    private Grid<ProductDto> grid;
    private GridPaginator<ProductDto> paginator;
    private List<ProductGroupDto> allProductGroups;
    private List<ProductGroupDto> buffer;
    private List<ProductDto> filteredData = new LinkedList<>();
    private TreeGrid<ProductGroupDto> productGroupTree;
    private ProductGroupDto p;
    private ProductGroupDto pg;
    private Class dtoClass = ProductGroupDto.class.getClass();
    private ProductGroupDto productGroupDto = null;


    public ProductGroupGridFiller(ProductGroupService productGroupService, ProductService productService, Grid<ProductDto> grid, GridPaginator<ProductDto> paginator) {
        //this.dtoClass = dtoClass;
        this.productGroupService = productGroupService;
        this.productService = productService;
        this.grid = grid;
        allProductGroups = productGroupService.getAll();
        buffer = new LinkedList<>();
        this.paginator = paginator;
        productGroupTree = new TreeGrid<>();
        productGroupTree.setSelectionMode(TreeGrid.SelectionMode.SINGLE);
    }

    public SplitLayout middleLayout() {
        SplitLayout layout = new SplitLayout();
        TreeGrid<ProductGroupDto> productGroupTree = new TreeGrid<>();
        productGroupTree.addHierarchyColumn(ProductGroupDto::getName).setHeader("Товарная группа");
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

        while (!allProductGroups.isEmpty()) {
            for (int i = 0; i < allProductGroups.size(); i++) {
                p = allProductGroups.get(i);
                if (p.getParentId() == null) {
                    productGroupTree.getTreeData().addItem(null, p);

                    buffer.add(p);
                    allProductGroups.remove(p);
                } else if (!buffer.isEmpty()) {
                    for (int j = 0; j < buffer.size(); j++) {
                        pg = buffer.get(j);
                        if (pg.getId() == p.getParentId()) {
                            productGroupTree.getTreeData().addItem(pg, p);
                            buffer.add(p);
                            allProductGroups.remove(p);
                            break;
                        }
                    }
                }
            }
        }
        buffer.clear();
        layout.setWidth("100%");
        layout.setHeight("66vh");
        grid.setWidth("80%");
        grid.setHeight("100%");
        productGroupTree.setHeight("100%");
        productGroupTree.setWidth("20%");
        productGroupTree.setThemeName("dense", true);
        layout.addToPrimary(productGroupTree);
        layout.addToSecondary(grid);
        return layout;
    }
}
