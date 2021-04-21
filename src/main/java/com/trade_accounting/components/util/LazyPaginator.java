package com.trade_accounting.components.util;

import com.trade_accounting.services.interfaces.PageableService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.event.SortEvent;
import com.vaadin.flow.data.value.ValueChangeMode;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Server-side component for pagination grid.
 */
public class LazyPaginator<T> extends HorizontalLayout {
    private int itemsPerPage;
    private int numberOfPages;
    private int currentPage;

    private final Grid<T> grid;
    private List<T> data;
    private PageableService<T> pageableService;
    private IntegerField pageItemsTextField;
    private final Button firstPageButton = new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT));
    private final Button prevPageButton = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    private final Button nextPageButton = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    private final Button lastPageButton = new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT));
    private Long rowCount;
    private boolean filterIsActive = false;
    private GridFilter<T> gridFilter;
    private Map<String, String> sortParams = new HashMap<>();

    /**
     * Creates a Paginator with specific number of items per page.
     *
     * @param grid         grid for pagination
     * @param pageableService         data for grid
     * @param itemsPerPage number items per page
     */
    public LazyPaginator(Grid<T> grid, PageableService<T> pageableService,
                         int itemsPerPage, GridFilter<T> gridFilter) {
        this.gridFilter = gridFilter;
        this.grid = grid;
        this.pageableService = pageableService;
        this.itemsPerPage = itemsPerPage;
        this.data = pageableService.getPage(gridFilter.getFilterData(), this.sortParams, currentPage, itemsPerPage);
        this.rowCount = pageableService.getRowsCount(gridFilter.getFilterData());
        this.setSortParams("id", SortOrder.ASCENDING.toString());
        grid.setPageSize(itemsPerPage);
        grid.addSortListener(this::sort);
        calculateNumberOfPages();
        configureButton();
        configureTextField();
        setCurrentPageAndReloadGrid(1);
        reloadGrid();
        configureFilter();
        add(firstPageButton, prevPageButton, pageItemsTextField, nextPageButton, lastPageButton);
    }

    private void configureFilter() {
        gridFilter.onSearchClick(e -> {
            this.updateData(false);
        });
        gridFilter.onClearClick(e -> {
            this.updateData(false);
        });
    }
    private void configureButton() {
        nextPageButton.addClickListener(e -> setCurrentPageAndReloadGrid(currentPage + 1));
        lastPageButton.addClickListener(e -> setCurrentPageAndReloadGrid(getNumberOfPages()));
        prevPageButton.addClickListener(e -> setCurrentPageAndReloadGrid(getCurrentPage() - 1));
        firstPageButton.addClickListener(e -> setCurrentPageAndReloadGrid(1));
    }

    private void configureTextField() {
        pageItemsTextField = new IntegerField("");
        pageItemsTextField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        pageItemsTextField.setValueChangeMode(ValueChangeMode.TIMEOUT);
        pageItemsTextField.setWidth("200px");
        pageItemsTextField.setPlaceholder(getCurrentGridPageItems());
    }

    private List<T> getPageData() {
        rowCount = pageableService.getRowsCount(gridFilter.getFilterData());
        data = pageableService.getPage(gridFilter.getFilterData(), this.sortParams, currentPage, itemsPerPage);
        return data;
    }

    private String getCurrentGridPageItems() {
        int start = currentPage;
        int end = itemsPerPage * currentPage;

        if (start != 1) {
            start = ((itemsPerPage * currentPage) - itemsPerPage) + 1;
        }

        end = (int) Math.min(end, rowCount);

        return start + "-" + end + " из " + rowCount;
    }

    private void calculateNumberOfPages() {
        if (rowCount == 0) {
            this.numberOfPages = 1;
        }else {
            this.numberOfPages = (int) Math.ceil((float) rowCount / itemsPerPage);
        }
    }

    /**
     * Reload grid and sets data to grid from the value of current page.
     */
    public void reloadGrid() {
        grid.setItems(getPageData());
        pageItemsTextField.setPlaceholder(getCurrentGridPageItems());
        grid.getDataProvider().refreshAll();
    }

    /**
     * Gets the current page of paginator.
     *
     * @return currentPage current page
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Sets the current page of paginator. The page cannot be greater than number of pages of the paginator.
     * Also reloaded a grid to the setted page.
     *
     * @param currentPage new current page
     */
    public void setCurrentPageAndReloadGrid(int currentPage) {
        if (currentPage > numberOfPages) {
            throw new IllegalArgumentException("The current page: ["+ currentPage +"] greater than maximum number of page.");
        }
        this.currentPage = currentPage;
        firstPageButton.setEnabled(true);
        nextPageButton.setEnabled(true);
        lastPageButton.setEnabled(true);
        prevPageButton.setEnabled(true);

        if (currentPage == 1) {
            firstPageButton.setEnabled(false);
            prevPageButton.setEnabled(false);
        }
        if (currentPage == getNumberOfPages()) {
            lastPageButton.setEnabled(false);
            nextPageButton.setEnabled(false);
        }
        reloadGrid();
    }

    /**
     * Gets the number of pages of the paginator.
     *
     * @return numberOfPages number of pages
     */
    public int getNumberOfPages() {
        return numberOfPages;
    }

    /**
     * Updates all necessary data for grid update.
     *
     * @param saveCurrentPage save current page flag
     */
    public void updateData(boolean saveCurrentPage){
        rowCount = pageableService.getRowsCount(gridFilter.getFilterData());
        calculateNumberOfPages();
        if (saveCurrentPage && numberOfPages >= currentPage) {
            setCurrentPageAndReloadGrid(getCurrentPage());
        } else {
            setCurrentPageAndReloadGrid(1);
        }
    }

    private void setSortParams(String sortColumn, String sortDirection) {
        this.sortParams.put("column", sortColumn);
        this.sortParams.put("direction", sortDirection);
    }

    //FIXME двойной вызов при переходе сортировки между столбцами (сначала сбрасывается сортировка на одном, а потом ставится на другом)
    private void sort(SortEvent<Grid<T>, GridSortOrder<T>> gridGridSortOrderSortEvent) {
        if (!gridGridSortOrderSortEvent.getSortOrder().isEmpty()) {
            gridGridSortOrderSortEvent.getSortOrder().forEach(tGridSortOrder -> {
                this.setSortParams(tGridSortOrder.getSorted().getKey(), tGridSortOrder.getDirection().toString());
            });
        } else {
            this.setSortParams("id", SortOrder.ASCENDING.toString());
        }
        updateData(true);
    }
}
