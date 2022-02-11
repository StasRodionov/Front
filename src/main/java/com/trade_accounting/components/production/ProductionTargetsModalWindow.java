package com.trade_accounting.components.production;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.production.ProductionTargetsDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.production.ProductionTargetsService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
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
import java.util.List;

@UIScope
@SpringComponent
public class ProductionTargetsModalWindow extends Dialog {

    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final ProductionTargetsService productionTargetsService;
    private  final Notifications notifications;

    private final ComboBox<CompanyDto> companyDtoComboBox = new ComboBox<>();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final DateTimePicker plannedProductionDatePicker = new DateTimePicker();
    private final DateTimePicker productionStartDate = new DateTimePicker();
    private final DateTimePicker productionEndDate = new DateTimePicker();
    private final Checkbox checkboxIsSpend = new Checkbox("Отправлено");
    private final Checkbox checkboxIsPrint = new Checkbox("Напечатано");
    private final TextField returnNumber = new TextField();
    private final TextArea comment = new TextArea();
    private final TextArea owner = new TextArea();
    private final TextArea employeeOwner = new TextArea();
    private final TextArea updatedByName = new TextArea();
    private final ComboBox<WarehouseDto> materialWarehouseDtoComboBox = new ComboBox<>();
    private final ComboBox<WarehouseDto> productionWarehouseDtoComboBox = new ComboBox<>();
    private final Binder<ProductionTargetsDto> productionTargetsDtoBinder = new Binder<>(ProductionTargetsDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";

    public ProductionTargetsModalWindow(CompanyService companyService,
                                        WarehouseService warehouseService,
                                        ProductionTargetsService productionTargetsService,
                                        Notifications notifications) {
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.productionTargetsService = productionTargetsService;
        this.notifications = notifications;
        setSizeFull();
        add(headerLayout(), formLayout());
    }

    private HorizontalLayout headerLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(title(), saveButton(), closeButton());

        return horizontalLayout;
    }

    private VerticalLayout formLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(formLayout1(), formLayout2(), formLayout3(), formLayout4(), formLayout5(), formLayout6());

        return verticalLayout;
    }

    private H2 title() {
        return new H2("Добавление производственного задания");
    }

    private Button saveButton() {
        return new Button("Сохранить", e -> {
            if (!productionTargetsDtoBinder.validate().isOk()) {
                productionTargetsDtoBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
                ProductionTargetsDto dto = new ProductionTargetsDto();
                dto.setId(Long.parseLong(returnNumber.getValue()));
                dto.setDate(dateTimePicker.getValue().toString());
                dto.setCompanyId(companyDtoComboBox.getValue().getId());
                dto.setDeliveryPlannedMoment(plannedProductionDatePicker.getValue().toString());
                dto.setMaterialWarehouse(materialWarehouseDtoComboBox.getValue().getId());
                dto.setProductionWarehouse(productionWarehouseDtoComboBox.getValue().getId());
                dto.setProductionStart(productionStartDate.getValue().toString());
                dto.setProductionEnd(productionEndDate.getValue().toString());
                dto.setOwner(owner.getValue());
                dto.setEmployeeOwner(employeeOwner.getValue());
                dto.setPublished(checkboxIsSpend.getValue());
                dto.setPrinted(checkboxIsPrint.getValue());
                dto.setDescription(comment.getValue());
                dto.setUpdated(LocalDateTime.now().toString().substring(0, 16));
                dto.setUpdatedByName(updatedByName.getValue());
                productionTargetsService.create(dto);

                UI.getCurrent().navigate("productionTargets");
                close();
                clearAllFieldsModalView();
                notifications.infoNotification(String.format("Производственное задание c ID=%s сохранено", dto.getId()));
            }
        });
    }

    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> {
            close();
            clearAllFieldsModalView();
        });

        return button;
    }

    private HorizontalLayout formLayout1() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(numberConfigure(), dateConfigure(), checkboxLayout());

        return horizontalLayout;
    }

    private HorizontalLayout formLayout2() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(companyConfigure(),materialWarehouseConfigure());

        return horizontalLayout;
    }

    private HorizontalLayout formLayout3() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(plannedProductionDateConfigure(), productionWarehouseConfigure());

        return horizontalLayout;
    }

    private HorizontalLayout formLayout4() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(ownerConfig(), employeeOwnerConfig());

        return horizontalLayout;
    }

    private HorizontalLayout formLayout5() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(productionStartDateConfigure(), productionEndDateConfigure());

        return horizontalLayout;
    }

    private HorizontalLayout formLayout6() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(updateByNameConfig(), commentConfig());

        return horizontalLayout;
    }

    private HorizontalLayout numberConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Производственное задание №");
        label.setWidth("150px");
        returnNumber.setWidth("50px");
        horizontalLayout.add(label, returnNumber);
        productionTargetsDtoBinder.forField(returnNumber)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(ProductionTargetsDto::getIdValid, ProductionTargetsDto::setIdValid);

        return horizontalLayout;
    }

    private HorizontalLayout companyConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<CompanyDto> list = companyService.getAll();

        if (list != null) {
            companyDtoComboBox.setItems(list);
        }

        companyDtoComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyDtoComboBox.setWidth("350px");
        Label label = new Label("Организация");
        label.setWidth("100px");
        horizontalLayout.add(label, companyDtoComboBox);
        productionTargetsDtoBinder.forField(companyDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(ProductionTargetsDto::getCompanyDtoValid, ProductionTargetsDto::setCompanyDtoValid);

        return horizontalLayout;
    }

    private HorizontalLayout materialWarehouseConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<WarehouseDto> list = warehouseService.getAll();

        if (list != null) {
            materialWarehouseDtoComboBox.setItems(list);
        }

        materialWarehouseDtoComboBox.setItemLabelGenerator(WarehouseDto::getName);
        materialWarehouseDtoComboBox.setWidth("350px");
        Label label = new Label("Склад материалов");
        label.setWidth("100px");
        horizontalLayout.add(label, materialWarehouseDtoComboBox);
        productionTargetsDtoBinder.forField(materialWarehouseDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(ProductionTargetsDto::getMaterialWarehouseDtoValid, ProductionTargetsDto::setMaterialWarehouseDtoValid);

        return horizontalLayout;
    }

    private HorizontalLayout productionWarehouseConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<WarehouseDto> list = warehouseService.getAll();

        if (list != null) {
            productionWarehouseDtoComboBox.setItems(list);
        }

        productionWarehouseDtoComboBox.setItemLabelGenerator(WarehouseDto::getName);
        productionWarehouseDtoComboBox.setWidth("350px");
        Label label = new Label("Склад продукции");
        label.setWidth("100px");
        horizontalLayout.add(label, productionWarehouseDtoComboBox);
        productionTargetsDtoBinder.forField(productionWarehouseDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(ProductionTargetsDto::getProductionWarehouseDtoValid, ProductionTargetsDto::setProductionWarehouseDtoValid);

        return horizontalLayout;
    }

    private HorizontalLayout dateConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("От");
        dateTimePicker.setWidth("350px");
        horizontalLayout.add(label, dateTimePicker);
        productionTargetsDtoBinder.forField(dateTimePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(ProductionTargetsDto::getDateValid, ProductionTargetsDto::setDateValid);

        return horizontalLayout;
    }

    private HorizontalLayout plannedProductionDateConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("План. дата производства");
        label.setWidth("100px");
        plannedProductionDatePicker.setWidth("350px");
        horizontalLayout.add(label, plannedProductionDatePicker);
        productionTargetsDtoBinder.forField(plannedProductionDatePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(ProductionTargetsDto::getPlannedProductionDateValid, ProductionTargetsDto::setPlannedProductionDateValid);

        return horizontalLayout;
    }

    private HorizontalLayout productionStartDateConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Начало производства");
        label.setWidth("100px");
        productionStartDate.setWidth("350px");
        horizontalLayout.add(label, productionStartDate);
        productionTargetsDtoBinder.forField(productionStartDate)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(ProductionTargetsDto::getProductionStartDateValid, ProductionTargetsDto::setProductionStartDateValid);

        return horizontalLayout;
    }

    private HorizontalLayout productionEndDateConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Завершение производства");
        label.setWidth("100px");
        productionEndDate.setWidth("350px");
        horizontalLayout.add(label, productionEndDate);
        productionTargetsDtoBinder.forField(productionEndDate)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(ProductionTargetsDto::getProductionEndDateValid, ProductionTargetsDto::setProductionEndDateValid);

        return horizontalLayout;
    }

    private HorizontalLayout commentConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Комментарий");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("100px");
        comment.setWidth("350px");
        horizontalLayout.add(label, comment);

        return horizontalLayout;
    }

    private HorizontalLayout ownerConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Отдел");
        label.setWidth("100px");
        owner.setWidth("350px");
        owner.setHeight("50px");
        horizontalLayout.add(label, owner);

        return horizontalLayout;
    }

    private HorizontalLayout employeeOwnerConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Сотрудник");
        label.setWidth("100px");
        employeeOwner.setWidth("350px");
        employeeOwner.setHeight("50px");
        horizontalLayout.add(label, employeeOwner);

        return horizontalLayout;
    }

    private HorizontalLayout updateByNameConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Кем изменено");
        label.setWidth("100px");
        updatedByName.setWidth("350px");
        updatedByName.setHeight("50px");
        horizontalLayout.add(label, updatedByName);

        return horizontalLayout;
    }

    private VerticalLayout checkboxLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(checkboxIsSpend, checkboxIsPrint);

        return verticalLayout;
    }

    private void clearAllFieldsModalView() {
        companyDtoComboBox.setValue(null);
        materialWarehouseDtoComboBox.setValue(null);
        productionWarehouseDtoComboBox.setValue(null);
        dateTimePicker.setValue(null);
        comment.setValue("");
        returnNumber.setValue("");
        checkboxIsPrint.setValue(false);
        checkboxIsSpend.setValue(false);
    }
}
