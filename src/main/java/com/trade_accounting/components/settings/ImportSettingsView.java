package com.trade_accounting.components.settings;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.units.ImportDto;
import com.trade_accounting.services.interfaces.units.ImportService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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

import static com.trade_accounting.config.SecurityConstants.PROFILE_PROFILE__SETTINGS__IMPORT_SETTINGS;

@Route(value = PROFILE_PROFILE__SETTINGS__IMPORT_SETTINGS, layout = SettingsView.class)
@PageTitle("Учетная запись")
@Slf4j
public class ImportSettingsView extends VerticalLayout {

    private final ImportService importService;
    private final Notifications notifications;
    private Grid<ImportDto> grid = new Grid<>(ImportDto.class);
    private GridPaginator<ImportDto> paginator;

    public ImportSettingsView(ImportService importService, Notifications notifications) {
        this.importService = importService;
        this.notifications = notifications;
        paginator = new GridPaginator<>(grid, importService.getAll(), 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        grid();
        updateList();
        add (
                getAppView(),
                toolsUp(),
                grid,
                paginator
        );
    }

    private Component getAppView() {
        return new AppView();
    }

    private void grid() {
        grid.setItems(importService.getAll());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setColumns("id", "task", "start", "end", "status");
        grid.getColumnByKey("id").setHeader("№").setId("id");
        grid.getColumnByKey("task").setHeader("Задача").setId("Задача");
        grid.getColumnByKey("start").setHeader("Создана").setId("Создана");
        grid.getColumnByKey("end").setHeader("Завершена").setId("Завершена");
        grid.getColumnByKey("status").setHeader("Статус").setId("Статус");

    }

    private void updateList() {
        grid.setItems(importService.getAll());
        GridSortOrder<ImportDto> gridSortOrder = new GridSortOrder(grid.getColumnByKey("id"), SortDirection.ASCENDING);
        List<GridSortOrder<ImportDto>> gridSortOrderList = new ArrayList<>();
        gridSortOrderList.add(gridSortOrder);
        grid.sort(gridSortOrderList);
    }

    private Button buttonQuestion(){
        return  Buttons.buttonQuestion(
                "В разделе отображаются результаты всех операций по импорту товаров, контрагентов и дополнительных справочников, а также результаты действий с ЭДО." +
                "Импорт запускается из разделов Товары → Товары и услуги, Контрагенты → Контрагенты, а также из документов." +
                "Читать инструкции:" +
                "Импорт и экспорт" +
                "Импорт контрагентов" +
                "Импорт и экспорт справочников");
    }
    private H2 title(){
        H2 title = new H2("Импорт");
        title.setHeight("2.2em");
        return title;
    }
    private Button buttonRefresh(){
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }
    private Button buttonImport(){
        Button buttonImport = new Button("Импорт",new Icon(VaadinIcon.PLUS_CIRCLE));
        ImportModalWindow addImportModalWindow =
                new ImportModalWindow(new ImportDto(), importService);
        buttonImport.addClickListener(event -> addImportModalWindow.open());
        addImportModalWindow.addDetachListener(event -> updateList());
        return buttonImport;
    }

    private void deleteSelectedItems() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (ImportDto importDto : grid.getSelectedItems()) {
                importService.deleteById(importDto.getId());
                notifications.infoNotification("Выбранные задачи успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте задачи для удаления");
        }

    }

    private List<ImportDto> getData() {
        return importService.getAll();
    }

    private Select<String> valueSelect() {
        return SelectConfigurer.configureDeleteSelect(() -> {
            deleteSelectedItems();
            grid.deselectAll();
            paginator.setData(getData());
        });
    }

    private Component toolsUp() {
        HorizontalLayout toolsUp = new HorizontalLayout();
        toolsUp.add(buttonQuestion(), title(), buttonRefresh(), buttonImport(), valueSelect());
        toolsUp.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return toolsUp;
    }
}
