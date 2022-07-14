package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.general.ProductSelectModal;
import com.trade_accounting.components.util.GridConfigurer;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.finance.CorrectionDto;
import com.trade_accounting.models.dto.finance.CorrectionProductDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.finance.CorrectionProductService;
import com.trade_accounting.services.interfaces.finance.CorrectionService;
import com.trade_accounting.services.interfaces.util.ColumnsMaskService;
import com.trade_accounting.services.interfaces.warehouse.ProductPriceService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static com.trade_accounting.config.SecurityConstants.*;

@SpringComponent
@PageTitle("Оприходования")
@Route(value = GOODS_POSTING_VIEW, layout = AppView.class)
@UIScope
public class PostingTabView extends VerticalLayout implements AfterNavigationObserver {

    private final ProductService productService;
    private final CorrectionService correctionService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final CorrectionProductService correctionProductService;
    private final ProductPriceService productPriceService;
    private final PostingCreateView postingCreateView;
    private Notifications notifications;
    private final PostingModal modalWindow;
    private final ProductSelectModal productSelectModal;
    private final List<CorrectionDto> data;

    private final Grid<CorrectionDto> grid = new Grid<>(CorrectionDto.class, false);
    private final GridConfigurer<CorrectionDto> gridConfigurer;
    private GridPaginator<CorrectionDto> paginator;
    private final GridFilter<CorrectionDto> filter;

    private final TextField textField = new TextField();
    private final MenuBar selectXlsTemplateButton = new MenuBar();
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES,
            GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};

    @Autowired
    public PostingTabView(CorrectionService correctionService,
                          WarehouseService warehouseService,
                          CompanyService companyService,
                          CorrectionProductService correctionProductService,
                          PostingCreateView postingCreateView,
                          Notifications notifications,
                          ProductService productService,
                          ProductSelectModal productSelectModal,
                          ProductPriceService productPriceService,
                          ColumnsMaskService columnsMaskService,
                          PostingModal modalWindow) {
        this.correctionService = correctionService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.correctionProductService = correctionProductService;
        this.postingCreateView = postingCreateView;
        this.notifications = notifications;
        this.modalWindow = modalWindow;
        this.productService = productService;
        this.productPriceService = productPriceService;
        this.productSelectModal = productSelectModal;
        this.data = getData();
        this.gridConfigurer = new GridConfigurer<>(grid, columnsMaskService, GRID_GOODS_MAIN_POSTING);
        paginator = new GridPaginator<>(grid, data, 50);
        setSizeFull();
        configureGrid();
        filter = new GridFilter<>(grid);
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(configureActions(), filter, grid, paginator);
    }

    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(CorrectionDto::getDate).setHeader("Дата").setKey("date")
                .setId("Дата");
        grid.addColumn(correctionDto -> warehouseService.getById(correctionDto.getWarehouseId())
                        .getName()).setHeader("Склад").setKey("warehouseDto")
                .setId("Склад");
        grid.addColumn(correctionDto -> companyService.getById(correctionDto.getCompanyId())
                        .getName()).setHeader("Компания").setKey("companyDto")
                .setId("Компания");
        grid.addColumn(this::getTotalPrice).setHeader("Сумма").setTextAlign(ColumnTextAlign.END)
                .setId("Сумма");
        grid.addColumn("comment").setHeader("Комментарий")
                .setId("Комментарий");
        grid.addColumn(new ComponentRenderer<>(this::getIsSentIcon)).setHeader("Отправлено").setKey("sent")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsPrintIcon)).setHeader("Напечатано").setKey("print")
                .setId("Напечатано");
        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        gridConfigurer.addConfigColumnToGrid();

        grid.setHeight("66vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addItemDoubleClickListener(event -> {
            CorrectionDto correctionDto = event.getItem();
            PostingModal postingModal = new PostingModal(
                    productService,
                    correctionService,
                    warehouseService,
                    companyService,
                    notifications,
                    correctionProductService,
                    productSelectModal,
                    productPriceService);
            postingModal.setPostingEdit(correctionDto);
            postingModal.open();
        });
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.setFieldToComboBox("companyDto", CompanyDto::getName, companyService.getAll());
        filter.setFieldToComboBox("warehouseDto", WarehouseDto::getName, warehouseService.getAll());
        filter.onSearchClick(e -> paginator.setData(correctionService.search(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(correctionService.getAll()));
    }

    private List<CorrectionDto> getData() {
        return correctionService.getAll();
    }

    private void deleteSelectedCorrections() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (CorrectionDto correctionDto : grid.getSelectedItems()) {
                correctionService.deleteById(correctionDto.getId());
                notifications.infoNotification("Выбранные заказы успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные заказы");
        }
    }

    private HorizontalLayout configureActions() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), getTextOrder(), buttonRefresh(), buttonUnit(), buttonFilter(), text(),
                numberField(), valueSelect(), valueStatus(), valuePrint(), buttonSettings());
        upper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return upper;
    }

    private Component getIsSentIcon(CorrectionDto correctionDto) {
        if (correctionDto.getIsSent()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component getIsPrintIcon(CorrectionDto correctionDto) {
        if (correctionDto.getIsPrint()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private HorizontalLayout getTollBar() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(buttonQuestion(), getTextOrder(), buttonRefresh(), buttonUnit(),
                buttonFilter(), text(), numberField(), valueSelect(), valueStatus(),
                valuePrint(), buttonSettings(), selectXlsTemplateButton);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

    protected String getTotalPrice(CorrectionDto correctionDto) {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        for (Long id : correctionDto.getCorrectionProductIds()) {
            CorrectionProductDto correctionProductDto = correctionProductService.getById(id);
            totalPrice = totalPrice.add(correctionProductDto.getAmount()
                    .multiply(correctionProductDto.getPrice()));
        }
        return String.format("%.2f", totalPrice);
    }

    private H2 getTextOrder() {
        final H2 textOrder = new H2("Оприходования");
        textOrder.setHeight("2.2em");
        return textOrder;
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Dialog dialog = new Dialog();
        Button cancelButton = new Button("Закрыть", event -> {
            dialog.close();
        });
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponentAsFirst(cancelButton);
        dialog.add(new Text("Оприходование позволяет учитывать на складе излишки товаров, обнаруженные при " +
                "инвентаризации, и пересортицу, а также вносить на склад начальные остатки. " +
                "Для каждого склада создается отдельный документ оприходования.\n" +
                "Оприходование можно создать вручную или из документа инвентаризации."));
        dialog.setWidth("450px");
        dialog.setHeight("250px");
        buttonQuestion.addClickListener(event -> dialog.open());
        Shortcuts.addShortcutListener(dialog, () -> {
            dialog.close();
        }, Key.ESCAPE);
        dialog.add(new Div(cancelButton));
        return buttonQuestion;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(ev -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    public void updateList() {
        grid.setItems(correctionService.getAll());
    }

    private Button buttonUnit() {

        Button button = new Button("Оприходование", new Icon(VaadinIcon.PLUS_CIRCLE));
        button.addClickListener(e -> {
            postingCreateView.clearForm();
            postingCreateView.setParentLocation(GOODS_POSTING_VIEW);
            button.getUI().ifPresent(ui -> ui.navigate(GOODS_GOODS__POSTING_CREATE));
        });

        return button;
    }

    private Button buttonFilter() {
        return new Button("Фильтр", e -> filter.setVisible(!filter.isVisible()));
    }

    private TextField text() {
        textField.setWidth("300px");
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setClearButtonVisible(true);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        setSizeFull();
        return textField;
    }

    private NumberField numberField() {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect() {
        return SelectConfigurer.configureDeleteSelect(() -> {
            deleteSelectedCorrections();
            grid.deselectAll();
            paginator.setData(getData());
        });
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private Select<String> valueStatus() {
        return SelectConfigurer.configureStatusSelect();
    }

    private Select<String> valuePrint() {
        return SelectConfigurer.configurePrintSelect();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
