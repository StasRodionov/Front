package com.trade_accounting.components.util;

import com.trade_accounting.models.dto.util.ColumnsMaskDto;
import com.trade_accounting.services.interfaces.util.ColumnsMaskService;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Component for generation of context menu and button above the grid as the additional column header
 * to allow columns hiding/showing from that menu selections.
 */
@Slf4j
public class GridConfigurer<T> {

    private final Grid<T> grid;
    private final ColumnsMaskService columnsMaskService;
    private final int gridId;

    /**
     * Creates a GridConfigurer.
     *
     * @param grid               grid
     * @param columnsMaskService columnsMaskService
     * @param gridId             gridId
     */
    @Autowired
    public GridConfigurer(Grid<T> grid, ColumnsMaskService columnsMaskService, int gridId) {
        this.grid = grid;
        this.columnsMaskService = columnsMaskService;
        this.gridId = gridId;
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

        ColumnToggleContextMenu<T> columnToggleContextMenu = new ColumnToggleContextMenu<>(menuButton, columnsMaskService, gridId);
        columnToggleContextMenu.getElement().setProperty("closeOn", "vaadin-overlay-outside-click");

        for (int i = 0; i < columns.size(); i++) {
            Grid.Column<T> column = columns.get(i);

            if (column.getId().isPresent()) {
                columnToggleContextMenu.addColumnToggleItem(column.getId().orElse("ERROR"), column, i);
            }

        }

        columnToggleContextMenu.add(new Hr());
        columnToggleContextMenu.addColumnShowAllItem(columns);

        return menuButton;
    }


    private static class ColumnToggleContextMenu<T> extends ContextMenu {

        private final ColumnsMaskService columnsMaskService;
        private final ColumnsMaskDto columnsMaskDto;

        public ColumnToggleContextMenu(Component target, ColumnsMaskService columnsMaskService, int gridId) {
            super(target);
            setOpenOnClick(true);
            this.columnsMaskService = columnsMaskService;
            this.columnsMaskDto = columnsMaskService.getByGridId(gridId);
            log.info(String.format("Mask value on construct: %d", columnsMaskDto.getMask()));
        }

        private void addColumnToggleItem(String label, Grid.Column<T> column, int i) {
            MenuItem menuItem = this.addItem(label, e -> {
                column.setVisible(e.getSource().isChecked());
                columnsMaskDto.setMask(columnsMaskDto.getMask() ^ (1 << i));
                columnsMaskService.update(columnsMaskDto);
                log.info(String.format("Конфигурация отображения столбцов изменена и сохранена в БД: %d", columnsMaskDto.getMask()));
            });

            menuItem.setCheckable(true);
            menuItem.setChecked((columnsMaskDto.getMask() & (1 << i)) > 0);
            column.setVisible(menuItem.isChecked());
        }

        private void addColumnShowAllItem(List<Grid.Column<T>> columns) {
            this.addItem("Отобразить все столбцы", e -> {
                this.getItems().forEach(s -> {
                    if (s.isCheckable()) {
                        s.setChecked(true);
                    }
                });
                columns.forEach(c -> c.setVisible(true));
                columnsMaskDto.setMask(Integer.MAX_VALUE);
                columnsMaskService.update(columnsMaskDto);
                log.info(String.format("Конфигурация отображения столбцов изменена и сохранена в БД: %d", columnsMaskDto.getMask()));
            });
        }

    }

}
