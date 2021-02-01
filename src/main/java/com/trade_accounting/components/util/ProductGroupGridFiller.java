package com.trade_accounting.components.util;

import com.trade_accounting.models.dto.ProductGroupDto;
import com.trade_accounting.services.interfaces.ProductGroupService;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.AbstractHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class ProductGroupGridFiller {

    private ProductGroupService productGroupService;
    private List<ProductGroupDto> allProductGroups;
    private List<ProductGroupDto> buffer;
    private TreeGrid<ProductGroupDto> productGroupTree;
    private ProductGroupDto p;
    private ProductGroupDto pg;


    public ProductGroupGridFiller(ProductGroupService productGroupService) {
        this.productGroupService = productGroupService;
        allProductGroups = productGroupService.getAll();
        buffer = new LinkedList<>();
        productGroupTree = new TreeGrid<>();

    }


    public TreeGrid<ProductGroupDto> middleLayout() {
        TreeGrid<ProductGroupDto> productGroupTree = new TreeGrid<>(ProductGroupDto.class);
        productGroupTree.setHierarchyColumn("Группа товаров");

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
        productGroupTree.setHierarchyColumn("name");
        return productGroupTree;

    }
}
