package com.trade_accounting.components.settings;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.configure.components.select.SelectConstants;
import com.trade_accounting.components.util.configure.components.select.SelectExt;
import com.trade_accounting.models.dto.units.ExportDto;
import com.trade_accounting.services.interfaces.units.ExportService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.trade_accounting.config.SecurityConstants.PROFILE_PROFILE__SETTINGS__EXPORT_SETTINGS;

@Route(value = PROFILE_PROFILE__SETTINGS__EXPORT_SETTINGS, layout = SettingsView.class)
@PageTitle("Учетная запись")
@Slf4j
public class ExportSettingsView extends VerticalLayout {

    private final ExportService exportService;
    private Grid<ExportDto> grid = new Grid<>(ExportDto.class);
    private GridPaginator<ExportDto> paginator;
    private final GridFilter<ExportDto> filter;

    public ExportSettingsView(ExportService exportService) {
        this.exportService = exportService;
        paginator = new GridPaginator<>(grid, exportService.getAll(), 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        grid();
        this.filter = new GridFilter<>(grid);
        updateList();
        configureFilter();
        add(getAppView(), toolsUp(), filter, grid, paginator);
    }


    private void grid() {
        grid.setItems(exportService.getAll());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setColumns("id", "task", "start", "finish", "status");
        grid.getColumnByKey("id").setHeader("id").setId("id");
        grid.getColumnByKey("task").setHeader("Задача").setId("Задача");
        grid.getColumnByKey("start").setHeader("Создана").setId("Создана");
        grid.getColumnByKey("finish").setHeader("Завершена").setId("Завершена");
        grid.getColumnByKey("status").setHeader("Статус").setId("Статус");
        grid.setHeight("64vh");

    }

    private void updateList() {
        grid.setItems(exportService.getAll());
        GridSortOrder<ExportDto> gridSortOrder = new GridSortOrder(grid.getColumnByKey("task"), SortDirection.ASCENDING);
        List<GridSortOrder<ExportDto>> gridSortOrderList = new ArrayList<>();
        gridSortOrderList.add(gridSortOrder);
        grid.sort(gridSortOrderList);
    }

    private void configureFilter() {
        filter.onSearchClick(e -> paginator.setData(exportService.search(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(exportService.getAll()));
    }

    private AppView getAppView() {
        return new AppView();
    }

    private HorizontalLayout toolsUp() {
        HorizontalLayout toolsUp = new HorizontalLayout();
        toolsUp.add(buttonQuestion(), title(), buttonExport(), valueSelect());
        toolsUp.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return toolsUp;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion("\t\n" +
                "В разделе отображаются результаты всех операций по экспорту товаров, " +
                "контрагентов и дополнительных справочников, а также результаты действий с ЭДО.");
    }

    private H2 title() {
        H2 title = new H2("Экспорт");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonExport() {
        Button exportButton = new Button("Экспортировать");

        return exportButton;
    }

    private Select<String> valueSelect() {
        return new SelectExt.SelectBuilder<String>()
                .item(SelectConstants.CHANGE_SELECT_ITEM)
                .defaultValue(SelectConstants.CHANGE_SELECT_ITEM)
                .width(SelectConstants.SELECT_WIDTH_130PX)
                .build();
    }
}
