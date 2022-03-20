package com.trade_accounting.components.goods;

import com.trade_accounting.components.general.ProductSelectModal;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Modals;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.finance.CorrectionDto;
import com.trade_accounting.models.dto.finance.CorrectionProductDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.finance.CorrectionProductService;
import com.trade_accounting.services.interfaces.finance.CorrectionService;
import com.trade_accounting.services.interfaces.warehouse.ProductPriceService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UIScope
@SpringComponent
public class PostingModal  extends Dialog {
    CorrectionDto correctionDto;
    private final ProductService productService;
    private final CorrectionService correctionService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final Notifications notifications;
    private final CorrectionProductService correctionProductService;
    private final ProductPriceService productPriceService;
    private List<CorrectionProductDto> tempCorrectionProductDtoList = new ArrayList<>();
    private final ComboBox<CompanyDto> companyComboBox = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseComboBox = new ComboBox<>();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final Checkbox checkboxIsSent = new Checkbox("Отправленно");
    private final Checkbox checkboxIsPrint = new Checkbox("Напечатано");
    private final TextField returnNumber = new com.vaadin.flow.component.textfield.TextField();
    private final TextArea textArea = new TextArea();
    private CorrectionDto dto = new CorrectionDto();
    private final Grid<CorrectionProductDto> grid = new Grid<>(CorrectionProductDto.class, false);
    private final GridPaginator<CorrectionProductDto> paginator;
    private final ProductSelectModal productSelectModal;
    private final Binder<CorrectionDto> postingBinder =
            new Binder<>(CorrectionDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";
    private String parentLocation = "positingTab";

    public PostingModal(ProductService productService, CorrectionService correctionService,
                        WarehouseService warehouseService,
                        CompanyService companyService,
                        Notifications notifications,
                        CorrectionProductService correctionProductService,
                        ProductSelectModal productSelectModal,
                        ProductPriceService productPriceService) {
        this.productService = productService;
        this.correctionService = correctionService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.productSelectModal = productSelectModal;
        this.notifications = notifications;
        this.productPriceService = productPriceService;
        this.correctionProductService = correctionProductService;

        paginator = new GridPaginator<>(grid);

        setSizeFull();
        add(headerLayout(), formLayout(),  grid, paginator);

        productSelectModal.addDetachListener(detachEvent -> {
            if (productSelectModal.isFormValid()) {
                addProduct(productSelectModal.getCorrectionProductDto());
            }
        });
    }

    public void setPostingEdit(CorrectionDto editDto) {
        //this.correctionDto = editDto;
        tempCorrectionProductDtoList = new ArrayList<>();
        if (!editDto.getCorrectionProductIds().isEmpty()){
            for (Long correctionProductsId : editDto.getCorrectionProductIds()) {
                CorrectionProductDto correctionProductDto = correctionProductService.getById(correctionProductsId);
                tempCorrectionProductDtoList.add(correctionProductDto);
            }
        }

        returnNumber.setValue(editDto.getId().toString());
        dateTimePicker.setValue(LocalDateTime.parse(editDto.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        textArea.setValue(editDto.getComment());
        warehouseComboBox.setValue(warehouseService.getById(editDto.getWarehouseId()));
        companyComboBox.setValue(companyService.getById(editDto.getCompanyId()));
        checkboxIsSent.setValue(editDto.getIsSent());
        checkboxIsPrint.setValue(editDto.getIsPrint());
        configureGrid();
    }

    private void addProduct(CorrectionProductDto correctionProductDto) {
        if (!paginator.contains(correctionProductDto)) {
            paginator.addData(correctionProductDto);
        }
    }

    private void configureGrid() {
        grid.removeAllColumns();
        grid.setItems(tempCorrectionProductDtoList);
        grid.addColumn(inPrDto -> tempCorrectionProductDtoList.indexOf(inPrDto) + 1).setHeader("№").setId("№");
        grid.addColumn(inPrDto -> productService.getById(inPrDto.getProductId()).getName()).setHeader("Название")
                .setKey("productDtoName").setId("Название");
        grid.addColumn(inPrDto -> productService.getById(inPrDto.getProductId()).getDescription()).setHeader("Описание")
                .setKey("productDtoDescr").setId("Описание");
        grid.addColumn(CorrectionProductDto::getAmount).setHeader("Количество");
        grid.addColumn(CorrectionProductDto::getPrice).setHeader("Цена").setSortable(true).setId("Цена");
        grid.setHeight("36vh");
        grid.setColumnReorderingAllowed(true);

        grid.setHeight("36vh");
        grid.setColumnReorderingAllowed(true);

        grid.addItemDoubleClickListener(event -> {
            CorrectionProductDto correctionProductDto = event.getItem();
            ProductSelectModal productSelectModal = new ProductSelectModal(
                    productService, productPriceService
            );
            productSelectModal.setPostingEdit(correctionProductDto);
            productSelectModal.open();
        });
    }


    private HorizontalLayout headerLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(title(), saveButton(), closeButton(), getAddProductButton());
        return horizontalLayout;
    }

    private VerticalLayout formLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(formLayout1(), formLayout2(), formLayout4());
        return verticalLayout;
    }

    private H2 title() {
        return new H2("Оприходование");
    }


    private com.vaadin.flow.component.button.Button saveButton() {
        return new Button("Сохранить", e -> {
            //save
            if (!postingBinder.validate().isOk()) {
                postingBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
                CorrectionDto dto = new CorrectionDto();
                dto.setId(Long.parseLong(returnNumber.getValue()));
                dto.setCompanyId(companyComboBox.getValue().getId());
                dto.setWarehouseId(warehouseComboBox.getValue().getId());
                dto.setDate(dateTimePicker.getValue().toString());
                dto.setIsPrint(checkboxIsPrint.getValue());
                dto.setComment(textArea.getValue());
                dto.setIsSent(checkboxIsSent.getValue());
                dto.setCorrectionProductIds(tempCorrectionProductDtoList.stream().map(p->p.getId()).collect(Collectors.toList()));
                correctionService.update(dto);

                close();
                clearAllFieldsModalView();
                UI.getCurrent().navigate("positingTab");

                notifications.infoNotification(String.format("оприходование c ID=%s обновлен", dto.getId()));
            }
        });
    }

    private Button getAddProductButton() {
        return new Button("Добавить продукт", new Icon(VaadinIcon.PLUS_CIRCLE), clickEvent -> {
            productSelectModal.clearForm();
            productSelectModal.open();
        });
    }
    private Button closeButton() {
        return new Button("Закрыть", new Icon(VaadinIcon.CLOSE), clickEvent -> {
            Modals.confirmModal("Вы уверены? Несохраненные данные будут утеряны",
                    new Button("Продолжить", e -> UI.getCurrent().navigate(parentLocation)),
                    new Button("Отменить")
            ).open();
        });
    }

    private HorizontalLayout formLayout1() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(numberConfigure(), dateConfigure(), checkboxLayout(), checkboxPrintLayout());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout2() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(companyConfigure(), warehouseConfigure());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout4() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(commentConfig());
        return horizontalLayout;
    }

    private HorizontalLayout numberConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("Инвентаризация №");
        label.setWidth("150px");
        returnNumber.setWidth("50px");
        horizontalLayout.add(label, returnNumber);
        postingBinder.forField(returnNumber)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        //
        return horizontalLayout;
    }

    private HorizontalLayout dateConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("От");
        dateTimePicker.setWidth("350px");
        horizontalLayout.add(label, dateTimePicker);
        postingBinder.forField(dateTimePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        //
        return horizontalLayout;
    }

    private VerticalLayout checkboxLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(checkboxIsSent);
        return verticalLayout;
    }

    private VerticalLayout checkboxPrintLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(checkboxIsPrint);
        return verticalLayout;
    }

    private HorizontalLayout companyConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        java.util.List<CompanyDto> list = companyService.getAll();
        if (list != null) {
            companyComboBox.setItems(list);
        }
        companyComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyComboBox.setWidth("350px");
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("Организация");
        label.setWidth("100");
        horizontalLayout.add(label, companyComboBox);
        postingBinder.forField(companyComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        //
        return horizontalLayout;
    }

    private HorizontalLayout warehouseConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<WarehouseDto> list = warehouseService.getAll();
        if (list != null) {
            warehouseComboBox.setItems(list);
        }
        warehouseComboBox.setItemLabelGenerator(WarehouseDto::getName);
        warehouseComboBox.setWidth("350px");
        com.vaadin.flow.component.html.Label label = new com.vaadin.flow.component.html.Label("Склад");
        label.setWidth("50");
        horizontalLayout.add(label, warehouseComboBox);
        postingBinder.forField(warehouseComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        //
        return horizontalLayout;
    }

    private HorizontalLayout commentConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        com.vaadin.flow.component.html.Label label = new Label("Комментарий");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("100px");
        horizontalLayout.add(label, textArea);
        postingBinder.forField(textArea);
        return horizontalLayout;
    }
    private void clearAllFieldsModalView() {
        companyComboBox.setValue(null);
        warehouseComboBox.setValue(null);
        dateTimePicker.setValue(null);
        textArea.setValue("");
        returnNumber.setValue("");
        checkboxIsSent.setValue(false);
        checkboxIsPrint.setValue(false);
    }
}
