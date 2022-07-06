package com.trade_accounting.components.settings;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.configure.components.select.SelectConstants;
import com.trade_accounting.components.util.configure.components.select.SelectExt;
import com.trade_accounting.models.dto.util.ProjectDto;
import com.trade_accounting.services.interfaces.finance.PaymentService;
import com.trade_accounting.services.interfaces.invoice.InvoiceService;
import com.trade_accounting.services.interfaces.util.ProjectService;
import com.trade_accounting.services.interfaces.warehouse.AcceptanceService;
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

import static com.trade_accounting.config.SecurityConstants.*;

@Route(value = PROFILE_PROFILE__SETTINGS__PROJECTS_SETTINGS, layout = SettingsView.class)
@PageTitle("Проекты")
@Slf4j
public class ProjectSettingsView extends VerticalLayout {

    private final ProjectService projectService;
    private final InvoiceService invoiceService;
    private final PaymentService paymentService;
    private final AcceptanceService acceptanceService;

    private Grid<ProjectDto> grid = new Grid<>(ProjectDto.class);
    private GridPaginator<ProjectDto> paginator;
    private final GridFilter<ProjectDto> filter;

    public ProjectSettingsView(ProjectService projectService,
                               InvoiceService invoiceService,
                               PaymentService paymentService,
                               AcceptanceService acceptanceService) {
        this.projectService = projectService;
        this.invoiceService = invoiceService;
        this.paymentService = paymentService;
        this.acceptanceService = acceptanceService;
        paginator = new GridPaginator<>(grid, projectService.getAll(), 100);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);
        configureGrid();
        this.filter = new GridFilter<>(grid);
        updateList();
        configureFilter();
        add(getAppView(), upperLayout(), filter, grid, paginator);
    }

    private AppView getAppView() {
        return  new AppView();
    }

    private void configureFilter() {
        filter.onSearchClick(e -> paginator.setData(projectService.search(filter.getFilterData())));
        filter.onClearClick(e-> paginator.setData(projectService.getAll()));
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion("\t\n" +
                "Проекты позволяют оценить успех акции или, например, нового канала продаж. Это атрибут, " +
                "который присваивается документам и потом становится доступен в отчетах.\n");
    }

    private H2 title() {
        H2 title = new H2("Проекты");
        title.setHeight("2.2em");
        return title;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private Button buttonProject() {
        Button buttonProject = new Button("Проект", new Icon(VaadinIcon.PLUS_CIRCLE));
        AddEditProjectModal addEditProjectModal =
                new AddEditProjectModal(new ProjectDto(), projectService, invoiceService, paymentService, acceptanceService);
        buttonProject.addClickListener(event -> addEditProjectModal.open());
        addEditProjectModal.addDetachListener(event -> updateList());
        return buttonProject;
    }

    private void updateList() {
        grid.setItems(projectService.getAll());
        GridSortOrder<ProjectDto> gridSortOrder = new GridSortOrder(grid.getColumnByKey("name"), SortDirection.ASCENDING);
        List<GridSortOrder<ProjectDto>> gridSortOrderList = new ArrayList<>();
        gridSortOrderList.add(gridSortOrder);
        grid.sort(gridSortOrderList);
    }

    private Select<String> valueSelect() {
        return new SelectExt.SelectBuilder<String>()
                .item(SelectConstants.CHANGE_SELECT_ITEM)
                .defaultValue(SelectConstants.CHANGE_SELECT_ITEM)
                .width(SelectConstants.SELECT_WIDTH_130PX)
                .build();
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upperLayout = new HorizontalLayout();
        upperLayout.add(buttonQuestion(), title(), buttonRefresh(), buttonProject(), buttonFilter(), valueSelect());
        upperLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upperLayout;
    }

    private void configureGrid() {
        grid.setItems(projectService.getAll());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setColumns( "name", "code", "description");
        grid.getColumnByKey("name").setHeader("Наименование").setId("Наименование");
        grid.getColumnByKey("code").setHeader("Код").setId("Код");
        grid.getColumnByKey("description").setHeader("Описание").setId("Описание");
        grid.setHeight("64vh");
        grid.addItemClickListener(event -> {
            ProjectDto editProject = event.getItem();
            AddEditProjectModal addEditProjectModal =
                    new AddEditProjectModal(editProject, projectService, invoiceService, paymentService, acceptanceService);
            addEditProjectModal.addDetachListener(e -> updateList());
            addEditProjectModal.open();
        });
    }
}
