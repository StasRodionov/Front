package com.trade_accounting.components.util;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Component for generation of context menu and button above the grid as the additional column header
 * to allow columns hiding/showing from that menu selections.
 */
@Slf4j
public class GridConfigurer<T> {

    private final Grid<T> grid;

    /**
     * Creates a GridConfigurer.
     *
     * @param grid grid
     */
    public GridConfigurer(Grid<T> grid) {
        this.grid = grid;
    }

    /**
     * Adds configuration button as the additional column header to the bounded grid
     * (already containing the range of column headers so as ones used to build
     * a configuration menu)
     */
    public void addConfigColumnToGrid() {
        grid.addComponentColumn(column -> new Label())
                .setHeader(configureColumnsVisibility())
                .setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true);
    }

    private Component configureColumnsVisibility() {
        List<Grid.Column<T>> columns = grid.getColumns();

        Button menuButton = new Button(new Icon(VaadinIcon.COG));
        menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        ColumnToggleContextMenu<T> columnToggleContextMenu = new ColumnToggleContextMenu<>(menuButton);
        columnToggleContextMenu.getElement().setProperty("closeOn", "vaadin-overlay-outside-click");
        for (Grid.Column<T> column : columns) {
            column.getId().ifPresent(s -> columnToggleContextMenu.addColumnToggleItem(s, column));
        }
        columnToggleContextMenu.add(new Hr());
        columnToggleContextMenu.addColumnShowAllItem(columns);

        return menuButton;
    }

    private static class ColumnToggleContextMenu<T> extends ContextMenu {

        public ColumnToggleContextMenu(Component target) {
            super(target);
            setOpenOnClick(true);
        }

        private void addColumnToggleItem(String label, Grid.Column<T> column) {
            MenuItem menuItem = this.addItem(label, e -> {
                column.setVisible(e.getSource().isChecked());
            });

            menuItem.setCheckable(true);
            menuItem.setChecked(column.isVisible());
        }

        private void addColumnShowAllItem(List<Grid.Column<T>> columns) {
            this.addItem("Отобразить все столбцы", e -> {
                for (Grid.Column<T> column : columns) {
                    column.setVisible(true);
                }

                this.getItems().forEach(f -> {
                    if (f.isCheckable()) {
                        f.setChecked(true);
                    }
                });
            });
        }

    }
}
