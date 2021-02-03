package com.trade_accounting.components.util;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.List;

/**
 * Server-side component for pagination grid.
 */
public class GridPaginator<T> extends HorizontalLayout {
    private final int itemsPerPage;
    private int numberOfPages;
    private int currentPage;

    private final Grid<T> grid;
    private List<T> data;

    private IntegerField pageItemsTextField;
    private final Button firstPageButton = new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT));
    private final Button prevPageButton = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    private final Button nextPageButton = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    private final Button lastPageButton = new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT));

    /**
     * Creates a Paginator with specific number of items per page.
     *
     * @param grid         grid for pagination
     * @param data         data for grid
     * @param itemsPerPage number items per page
     */
    public GridPaginator(Grid<T> grid, List<T> data, int itemsPerPage) {
        this.grid = grid;
        this.data = data;
        this.itemsPerPage = itemsPerPage;
        this.currentPage = 1;

        calculateNumberOfPages();
        configureButton();
        configureTextField();

        reloadGrid();

        add(firstPageButton, prevPageButton, pageItemsTextField, nextPageButton, lastPageButton);
    }

    /**
     * Creates a Paginator with 10 items per page.
     *
     * @param grid grid for pagination
     * @param data data for grid
     */
    public GridPaginator(Grid<T> grid, List<T> data) {
        this(grid, data, 10);
    }

    private void configureButton() {
        if (currentPage == 1) {
            firstPageButton.setEnabled(false);
            prevPageButton.setEnabled(false);
        }

        if (data.isEmpty() || data.size() <= itemsPerPage) {
            nextPageButton.setEnabled(false);
            lastPageButton.setEnabled(false);
        }

        nextPageButton.addClickListener(e -> {
            if (numberOfPages == currentPage + 1) {
                setCurrentPage(currentPage + 1);

                nextPageButton.setEnabled(false);
                lastPageButton.setEnabled(false);
            } else {
                setCurrentPage(currentPage + 1);
            }

            prevPageButton.setEnabled(true);
            firstPageButton.setEnabled(true);

            reloadGrid();
        });

        lastPageButton.addClickListener(e -> {
            setCurrentPage(getNumberOfPages());

            nextPageButton.setEnabled(false);
            lastPageButton.setEnabled(false);

            prevPageButton.setEnabled(true);
            firstPageButton.setEnabled(true);

            reloadGrid();
        });

        prevPageButton.addClickListener(e -> {
            if (getCurrentPage() - 1 == 1) {
                setCurrentPage(getCurrentPage() - 1);

                prevPageButton.setEnabled(false);
                firstPageButton.setEnabled(false);
            } else {
                setCurrentPage(getCurrentPage() - 1);
            }

            nextPageButton.setEnabled(true);
            lastPageButton.setEnabled(true);

            reloadGrid();
        });

        firstPageButton.addClickListener(e -> {
            setCurrentPage(1);

            prevPageButton.setEnabled(false);
            firstPageButton.setEnabled(false);

            nextPageButton.setEnabled(true);
            lastPageButton.setEnabled(true);

            reloadGrid();
        });
    }

    private void configureTextField() {
        pageItemsTextField = new IntegerField("");
        pageItemsTextField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        pageItemsTextField.setValueChangeMode(ValueChangeMode.TIMEOUT);
        pageItemsTextField.setWidth("200px");

        pageItemsTextField.setPlaceholder(getCurrentGridPageItems());
    }

    private List<T> getPageData() {
        int from = (currentPage - 1) * itemsPerPage;

        int to = (from + itemsPerPage);
        to = Math.min(to, data.size());

        return data.subList(from, to);
    }

    private String getCurrentGridPageItems() {
        int start = currentPage;
        int end = itemsPerPage * currentPage;

        if (start != 1) {
            start = ((itemsPerPage * currentPage) - itemsPerPage) + 1;
        }

        end = Math.min(end, data.size());

        return start + "-" + end + " из " + data.size();
    }

    private void calculateNumberOfPages() {
        this.numberOfPages = (int) Math.ceil((float) data.size() / itemsPerPage);
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
    public void setCurrentPage(int currentPage) {
        if (currentPage > numberOfPages) {
            throw new IllegalArgumentException("The current page: ["+ currentPage +"] greater than maximum number of page.");
        }

        if (currentPage == 1) {
            firstPageButton.setEnabled(false);
            prevPageButton.setEnabled(false);
        }
        this.currentPage = currentPage;
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
     * Sets the number of pages of the paginator. Is has to be greater than 0.
     *
     * @param numberOfPages number of pages
     */
    public void setNumberOfPages(int numberOfPages) {
        if (numberOfPages < 1) {
            throw new IllegalArgumentException("The number of pages has to be greater than 0");
        }
        this.numberOfPages = numberOfPages;
    }

    /**
     * Sets new data for paginator.
     * Also set current page to 1 and reload grid.
     *
     * @param data data for grid
     */
    public void setData(List<T> data) {
        this.data = data;

        if (data.size() <= itemsPerPage) {
            setNumberOfPages(1);

            nextPageButton.setEnabled(false);
            lastPageButton.setEnabled(false);
        } else {
            calculateNumberOfPages();

            nextPageButton.setEnabled(true);
            lastPageButton.setEnabled(true);
        }

        setCurrentPage(1);

        reloadGrid();
    }
}
