package com.trade_accounting.components.goods;


import com.trade_accounting.components.AppView;
import com.trade_accounting.components.general.ProductSelectModal;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Modals;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.finance.LossDto;
import com.trade_accounting.models.dto.finance.LossProductDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.finance.LossProductService;
import com.trade_accounting.services.interfaces.finance.LossService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.trade_accounting.services.interfaces.units.UnitService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.trade_accounting.config.SecurityConstants.GOODS_GOODS__LOSS_CREATE;

@Slf4j
@Route(value = GOODS_GOODS__LOSS_CREATE, layout = AppView.class)
@PageTitle("Добавить списание")
@PreserveOnRefresh
@SpringComponent
@UIScope
public class LossCreateView extends VerticalLayout {

    private final CompanyService companyService;
    private final LossProductService lossProductService;
    private final LossService lossService;
    private final WarehouseService warehouseService;
    private final ProductService productService;
    private final UnitService unitService;

    private final ProductSelectModal productSelectModal;

    private final H2 title = new H2("Добавление списания");
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final ComboBox<CompanyDto> companySelect = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseSelect = new ComboBox<>();
    private final TextArea commentField = new TextArea();
    private final Checkbox isSent = new Checkbox();
    private final Checkbox isPrint = new Checkbox();
    private final Grid<LossProductDto> grid = new Grid<>(LossProductDto.class, false);
    private final GridPaginator<LossProductDto> paginator;

    private static final String LABEL_WIDTH = "100px";
    private static final String FIELD_WIDTH = "350px";
    private String parentLocation = "";

    @Autowired
    public LossCreateView(CompanyService companyService, LossProductService lossProductService,
                          LossService lossService,
                          WarehouseService warehouseService, ProductSelectModal productSelectModal,
                          ProductService productService, UnitService unitService) {
        this.companyService = companyService;
        this.lossProductService = lossProductService;
        this.lossService = lossService;
        this.warehouseService = warehouseService;
        this.productSelectModal = productSelectModal;
        this.productService = productService;
        this.unitService = unitService;

        configureGrid();
        paginator = new GridPaginator<>(grid);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);

        add(getUpperLayout(), getFormLayout(), grid, paginator);

        productSelectModal.addDetachListener(detachEvent -> {
            if (productSelectModal.isFormValid()) {
                addProduct(productSelectModal.getLossProductDto());
            }
        });
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn(inPrDto -> paginator.getData().indexOf(inPrDto) + 1).setHeader("№").setId("№");
        grid.addColumn(inPrDto -> productService.getById(inPrDto.getProductId()).getName()).setHeader("Название")
                .setKey("productDtoName").setId("Название");
        grid.addColumn(inPrDto -> productService.getById(inPrDto.getProductId()).getDescription()).setHeader("Описание")
                .setKey("productDtoDescr").setId("Описание");
        grid.addColumn("amount").setHeader("Количество");
        grid.addColumn(inPrDto -> unitService.getById(productService.getById(inPrDto.getProductId()).getUnitId()).getFullName()).setHeader("Единицы")
                .setKey("productDtoUnit").setId("Единицы");
        grid.addColumn("price").setHeader("Цена").setSortable(true).setId("Цена");
        grid.setHeight("36vh");
        grid.setColumnReorderingAllowed(true);
    }

    private HorizontalLayout getUpperLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.add(getQuestionButton(), getTitle(), getSaveButton(), getCloseButton(), getAddProductButton());
        layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return layout;
    }

    private VerticalLayout getFormLayout() {
        return new VerticalLayout(getFormLine1(), getFormLine2());
    }

    private H2 getTitle() {
        title.setHeight("2.0em");
        return title;
    }

    private Button getQuestionButton() {
        Button questionButton = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        questionButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return questionButton;
    }

    private Button getSaveButton() {
        return new Button("Сохранить", clickEvent -> {
            if (isLossFormValid()) {
                if (dateTimePicker.isEmpty()) {
                    dateTimePicker.setValue(LocalDateTime.now());
                }
                saveLoss();
                UI.getCurrent().navigate(parentLocation);
            } else {
                Modals.infoModal("Заполните все поля, чтобы добавить списание!").open();
            }
        });
    }

    private Button getCloseButton() {
        return new Button("Закрыть", new Icon(VaadinIcon.CLOSE), clickEvent -> {
            Modals.confirmModal("Вы уверены? Несохраненные данные будут утеряны",
                    new Button("Продолжить", e -> UI.getCurrent().navigate(parentLocation)),
                    new Button("Отменить")
            ).open();
        });
    }

    private Button getAddProductButton() {
        return new Button("Добавить продукт", new Icon(VaadinIcon.PLUS_CIRCLE), clickEvent -> {
            productSelectModal.clearForm();
            productSelectModal.open();
        });
    }

    private HorizontalLayout getFormLine1() {
        return new HorizontalLayout(getDateField(), getCompanyField(), getWarehouseField());
    }

    private HorizontalLayout getFormLine2() {
        return new HorizontalLayout(getCommentField(), getCheckboxFields());
    }

    private HorizontalLayout getDateField() {
        HorizontalLayout layout = new HorizontalLayout();
        Label label = new Label("Дата");
        label.setWidth(LABEL_WIDTH);
        dateTimePicker.setWidth(FIELD_WIDTH);
        dateTimePicker.setHelperText("По умолчанию текущая дата/время");
        layout.add(label, dateTimePicker);
        return layout;
    }

    private HorizontalLayout getCompanyField() {
        HorizontalLayout layout = new HorizontalLayout();
        Label label = new Label("Организация");
        label.setWidth(LABEL_WIDTH);
        companySelect.setItemLabelGenerator(CompanyDto::getName);
        companySelect.setItems(companyService.getAll());
        layout.add(label, companySelect);
        return layout;
    }

    private HorizontalLayout getWarehouseField() {
        HorizontalLayout layout = new HorizontalLayout();
        Label label = new Label("Склад");
        label.setWidth(LABEL_WIDTH);
        warehouseSelect.setItemLabelGenerator(WarehouseDto::getName);
        warehouseSelect.setItems(warehouseService.getAll());
        layout.add(label, warehouseSelect);
        return layout;
    }

    private HorizontalLayout getCommentField() {
        HorizontalLayout layout = new HorizontalLayout();
        Label label = new Label("Причина списания");
        label.setWidth(LABEL_WIDTH);
        layout.add(label, commentField);
        return layout;
    }

    private HorizontalLayout getCheckboxFields() {
        return new HorizontalLayout(isSent, new Label("Отправлено"), new Div(), isPrint, new Label("Напечатано"));
    }

    private void addProduct(LossProductDto lossProductDto) {
        if (!paginator.contains(lossProductDto)) {
            paginator.addData(lossProductDto);
        }
    }

    private boolean isLossFormValid() {
        return !paginator.getData().isEmpty() && !companySelect.isEmpty()
                && !warehouseSelect.isEmpty();
    }

    private LossDto saveLoss() {
        LossDto lossDto = new LossDto();
        lossDto.setDate(dateTimePicker.getValue().toString());
        lossDto.setLossProductsIds(paginator.getData().stream()
                .map(lossProductDto -> lossProductService.create(lossProductDto).getId())
                .collect(Collectors.toList()));
        lossDto.setComment(commentField.getValue());
        lossDto.setCompanyId(companySelect.getValue().getId());
        lossDto.setWarehouseId(warehouseSelect.getValue().getId());
        lossDto.setIsSent(isSent.getValue());
        lossDto.setIsPrint(isPrint.getValue());
        return lossService.create(lossDto);
    }

    public void clearForm() {
        dateTimePicker.clear();
        companySelect.clear();
        warehouseSelect.clear();
        commentField.clear();
        isSent.clear();
        isPrint.clear();
        paginator.setData(new ArrayList<>());
    }

    public void setParentLocation(String parentLocation) {
        this.parentLocation = parentLocation;
    }
}
