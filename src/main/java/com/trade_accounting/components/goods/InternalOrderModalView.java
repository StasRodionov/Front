package com.trade_accounting.components.goods;

import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.finance.LossDto;
import com.trade_accounting.models.dto.invoice.InternalOrderDto;
import com.trade_accounting.models.dto.invoice.InternalOrderProductsDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.invoice.InternalOrderProductsDtoService;
import com.trade_accounting.services.interfaces.invoice.InternalOrderService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
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
import org.vaadin.gatanaso.MultiselectComboBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.trade_accounting.config.SecurityConstants.*;

@UIScope
@SpringComponent
public class InternalOrderModalView extends Dialog {
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final InternalOrderService internalOrderService;
    private final InternalOrderProductsDtoService internalOrderProductsDtoService;
    private final ProductService productService;
    private final TitleForModal title;
    private InternalOrderDto internalOrderDto;

    private final ComboBox<CompanyDto> companyDtoComboBox = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseDtoComboBox = new ComboBox<>();
    private final MultiselectComboBox<Long> internalOrderProductsIdComboBox = new MultiselectComboBox();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final Checkbox checkboxIsSpend = new Checkbox("Проведено");
    private final Checkbox checkboxIsPrint = new Checkbox("Напечатано");
    private final TextField returnNumber = new TextField();
    private final TextArea textArea = new TextArea();

    private final Binder<InternalOrderDto> internalOrderDtoBinder =
            new Binder<>(InternalOrderDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";
    private final Notifications notifications;

    public InternalOrderModalView(CompanyService companyService, WarehouseService warehouseService,
                                  InternalOrderService internalOrderService, Notifications notifications,
                                  InternalOrderProductsDtoService internalOrderProductsDtoService,
                                  ProductService productService, TitleForModal title) {
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.internalOrderService = internalOrderService;
        this.notifications = notifications;
        this.internalOrderProductsDtoService = internalOrderProductsDtoService;
        this.productService = productService;
        this.title = title;
        setSizeFull();
        add(headerLayout(), formLayout());
    }

    public void setInternalOrderForEdit(InternalOrderDto editDto) {
        this.internalOrderDto = editDto;
        returnNumber.setValue(editDto.getId().toString());
        dateTimePicker.setValue(LocalDateTime.parse(editDto.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        textArea.setValue(editDto.getComment());
        companyDtoComboBox.setValue(companyService.getById(editDto.getCompanyId()));
        warehouseDtoComboBox.setValue(warehouseService.getById(editDto.getWarehouseId()));
        Set<Long> idset = new HashSet<>(internalOrderDto.getInternalOrderProductsIds());
        internalOrderProductsIdComboBox.setValue(idset);
        checkboxIsSpend.setValue(internalOrderDto.getIsSent());
        checkboxIsPrint.setValue(internalOrderDto.getIsPrint());

    }

    private HorizontalLayout headerLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(title(title.toString()), saveButton(), closeButton(), addGoodButton());
        return horizontalLayout;
    }

    private VerticalLayout formLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(formLayout1(), formLayout2(), formLayout4());
        return verticalLayout;
    }

    private HorizontalLayout formLayout1() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(dateConfigure(), checkboxLayout());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout2() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(companyConfigure(), warehouseConfigure());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout4() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(internalOrderProductsConfigure(), commentConfig());
        return horizontalLayout;
    }

    private H2 title(String title) {
        return new H2(title);
    }

    private Button saveButton() {

        return new Button("Сохранить", e -> {
            if (!internalOrderDtoBinder.validate().isOk()) {
                internalOrderDtoBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
                InternalOrderDto dto = new InternalOrderDto();

                if(returnNumber.getValue() != "") {
                    dto.setId(Long.parseLong(returnNumber.getValue()));
                    ((InternalOrderDto) dto).setInternalOrderProductsIds(internalOrderDto.getInternalOrderProductsIds());
                }

                dto.setCompanyId(companyDtoComboBox.getValue().getId());
                dto.setWarehouseId(warehouseDtoComboBox.getValue().getId());
                dto.setDate(dateTimePicker.getValue().toString());
                dto.setIsSent(checkboxIsSpend.getValue());
                dto.setIsPrint(checkboxIsPrint.getValue());
                dto.setComment(textArea.getValue());
                List<Long> idList = new ArrayList<>(internalOrderProductsIdComboBox.getValue());
                dto.setInternalOrderProductsIds(idList);
                internalOrderService.create(dto);

                close();
                clearAllFieldsModalView();
                notifications.infoNotification("Внутренний заказ сохранен");
                UI.getCurrent().navigate(GOODS_INTERNAL_ORDER_VIEW);
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

    private Button addGoodButton() {
        Button button = new Button("Добавить продукт", new Icon(VaadinIcon.PLUS));
        button.addClickListener(e -> {
            close();
            InternalOrderProductsModalView modalView = new InternalOrderProductsModalView(
                    internalOrderProductsDtoService,
                    notifications,
                    productService,
                    companyService,
                    warehouseService,
                    internalOrderService
            );
            modalView.open();
        });
        return button;
    }

    private HorizontalLayout dateConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("От");
        dateTimePicker.setWidth("350px");
        horizontalLayout.add(label, dateTimePicker);
        internalOrderDtoBinder.forField(dateTimePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(InternalOrderDto::getDateValid, InternalOrderDto::setDateValid);
        return horizontalLayout;
    }

    private VerticalLayout checkboxLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(checkboxIsSpend, checkboxIsPrint);
        return verticalLayout;
    }


    private HorizontalLayout  internalOrderProductsConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        List<InternalOrderDto> iod = internalOrderService.getAll();
        List<Long> checkInIod = new ArrayList<>();
        for(InternalOrderDto id : iod) {
            List<Long> list = id.getInternalOrderProductsIds();
            checkInIod.addAll(list);
        }

        List<InternalOrderProductsDto> labels = internalOrderProductsDtoService.getAll();
        List<Long> items = new ArrayList<>();
        for(InternalOrderProductsDto id : labels) {
            Long check = id.getId();
            if(!(checkInIod.contains(check))) {
                items.add(id.getId());
            }
        }

        internalOrderProductsIdComboBox.setItems(items);
        internalOrderProductsIdComboBox.setItemLabelGenerator(item -> productService.getById(internalOrderProductsDtoService
                .getById(item).getProductId()).getName());
        internalOrderProductsIdComboBox.setWidth("350px");
        Label label = new Label("Список товаров");
        label.setWidth("100px");
        horizontalLayout.add(label, internalOrderProductsIdComboBox);

        internalOrderDtoBinder.forField(internalOrderProductsIdComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(internalOrderDto -> new HashSet<>(internalOrderDto.getInternalOrderProductsIdsValid()),
                        (internalOrderDto, internalOrderDto2) -> internalOrderDto.setInternalOrderProductsIdsValid(internalOrderDto
                                .getInternalOrderProductsIdsValid()));
        UI.getCurrent().navigate(GOODS);
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
        internalOrderDtoBinder.forField(companyDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(InternalOrderDto::getCompanyDtoValid, InternalOrderDto::setCompanyDtoValid);
        return horizontalLayout;
    }

    private HorizontalLayout warehouseConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<WarehouseDto> list = warehouseService.getAll();
        if (list != null) {
            warehouseDtoComboBox.setItems(list);
        }
        warehouseDtoComboBox.setItemLabelGenerator(WarehouseDto::getName);
        warehouseDtoComboBox.setWidth("350px");
        Label label = new Label("Склад");
        label.setWidth("100px");
        horizontalLayout.add(label, warehouseDtoComboBox);
        internalOrderDtoBinder.forField(warehouseDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(InternalOrderDto::getWarehouseDtoValid, InternalOrderDto::setWarehouseDtoValid);
        return horizontalLayout;
    }

    private HorizontalLayout commentConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Комментарий");
        label.setWidth("100px");
        horizontalLayout.setWidth("750px");
        horizontalLayout.setHeight("100px");
        horizontalLayout.add(label, textArea);
        return horizontalLayout;
    }

    private void clearAllFieldsModalView() {
        companyDtoComboBox.setValue(null);
        warehouseDtoComboBox.setValue(null);
        dateTimePicker.setValue(null);
        textArea.setValue("");
        returnNumber.setValue("");
        checkboxIsPrint.setValue(false);
        checkboxIsSpend.setValue(false);
        internalOrderProductsIdComboBox.setValue(null);
    }
}
