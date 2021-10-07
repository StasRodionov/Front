package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.InventarizationDto;
import com.trade_accounting.models.dto.MovementDto;
import com.trade_accounting.models.dto.MovementProductDto;
import com.trade_accounting.models.dto.TechnicalOperationsDto;
import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.InventarizationService;
import com.trade_accounting.services.interfaces.MovementService;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.UnitService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Route(value = "goods/add_moving", layout = AppView.class)
@PageTitle("Добавить перемещение")
@PreserveOnRefresh
@SpringComponent
@UIScope
public class MovementViewModalWindow extends VerticalLayout {

    private final ProductService productService;
    private final MovementService movementService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final Notifications notifications;
    private final UnitService unitService;

    private final List<MovementDto> data;

    private final ComboBox<CompanyDto> companyComboBox = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseComboBox = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseComboBoxOne = new ComboBox<>();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final Checkbox checkboxIsSent = new Checkbox("Отправленно");
    private final Checkbox checkboxIsPrint = new Checkbox("Напечатанно");
    private final TextField returnNumber = new TextField();
    private final TextArea textArea = new TextArea();
    private final TextArea textCom = new TextArea();
    private List<MovementProductDto> tempMovementProductDtoList = new ArrayList<>();

    private final H4 totalPrice = new H4();
    private final H2 title = new H2("Добавление перемещения");
    private final Button buttonDelete = new Button("Удалить", new Icon(VaadinIcon.TRASH));
    private final Button buttonPrint = new Button("Печать", new Icon(VaadinIcon.PRINT));
    private final Button buttonSend = new Button("Отправить", new Icon(VaadinIcon.ENVELOPE_OPEN_O));

    private final Dialog dialogOnCloseView = new Dialog();
    private String location = null;
    private final Grid<MovementProductDto> grid = new Grid<>(MovementProductDto.class, false);
    private final GridPaginator<MovementProductDto> paginator;

    private final Binder<MovementDto> movementDtoBinder =
            new Binder<>(MovementDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";


    public MovementViewModalWindow(InventarizationService inventarizationService,
                                   ProductService productService, MovementService movementService, WarehouseService warehouseService,
                                   CompanyService companyService,
                                   Notifications notifications, UnitService unitService) {
        this.productService = productService;
        this.movementService = movementService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.notifications = notifications;
        this.unitService = unitService;
        this.data = getData();

        configureGrid();
        paginator = new GridPaginator<>(grid, tempMovementProductDtoList, 50);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);

        setSizeFull();
        add(headerLayout(), formLayout(), grid, paginator);

    }

    private void configureGrid() {
       grid.setItems(tempMovementProductDtoList);
        grid.addColumn(inPrDto -> productService.getById(inPrDto.getProductId()).getName()).setHeader("Название")
                .setKey("productDtoName").setId("Название");
        grid.addColumn(inPrDto -> productService.getById(inPrDto.getProductId()).getDescription()).setHeader("Описание")
                .setKey("productDtoDescr").setId("Описание");
        Grid.Column<MovementProductDto> firstNameColumn = grid.addColumn("amount").setHeader("Количество");
        grid.addColumn(inPrDto -> unitService.getById(productService.getById(inPrDto.getProductId()).getUnitId()).getFullName()).setHeader("Единицы")
                .setKey("productDtoUnit").setId("Единицы");
        grid.addColumn("price").setHeader("Цена").setSortable(true).setId("Цена");
        grid.setHeight("66vh");
        grid.setMaxWidth("100%");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.getColumns().forEach(column -> column.setAutoWidth(true));

    }

    public void setInventarizationEdit(InventarizationDto editDto) {
        //update
//        //this.
//        returnNumber.setValue(editDto.getId().toString());
//        dateTimePicker.setValue(LocalDateTime.parse(editDto.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
//        textArea.setValue(editDto.getComment());
//        warehouseComboBox.setValue(warehouseService.getById(editDto.getWarehouseId()));
//        companyComboBox.setValue(companyService.getById(editDto.getCompanyId()));

    }



    private HorizontalLayout headerLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(title(), saveButton(), closeButton(), buttonUnit(), configureDeleteButton(), buttonPrint, buttonSend);
        horizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return horizontalLayout;
    }

    private VerticalLayout formLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(formLayout1(), formLayout2(), formLayout4());
        return verticalLayout;
    }

    private H2 title() {
        title.setHeight("2.0em");
        return title;
    }




    private Button saveButton() {
        return new Button("Сохранить", e -> {
            //save
            if (!movementDtoBinder.validate().isOk()) {
                movementDtoBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
//                InventarizationDto dto = new InventarizationDto();
//                dto.setId(Long.parseLong(returnNumber.getValue()));
//                dto.setCompanyId(companyComboBox.getValue().getId());
//                dto.setWarehouseId(warehouseComboBox.getValue().getId());
//                dto.setDate(dateTimePicker.getValue().toString());
//                dto.setStatus(checkboxIsSent.getValue());
//                dto.setComment(textArea.getValue());
//                inventarizationService.create(dto);
//
//                UI.getCurrent().navigate("inventory");
//                close();
//                clearAllFieldsModalView();
//                notifications.infoNotification(String.format("инвентаризация c ID=%s сохранен", dto.getId()));
            }
        });
    }

    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> button.getUI().ifPresent(ui -> ui.navigate("movementView")));

        return button;
    }

    private void closeView() {
        clearAllFieldsModalView();
        UI.getCurrent().navigate(location);
    }

    private HorizontalLayout formLayout1() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(numberConfigure(), dateConfigure(), checkboxIsSent, checkboxIsPrint);
        return horizontalLayout;
    }

    private HorizontalLayout formLayout2() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(companyConfigure(), warehouseConfigure());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout4() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(warehouseConfigureOne(), commentConfig(), totalPriceTitle());
        return horizontalLayout;
    }

//    private HorizontalLayout formLayout5() {
//        HorizontalLayout horizontalLayout = new HorizontalLayout();
//        horizontalLayout.add(addCom(), buttonUnit());
//        return horizontalLayout;
//    }

    private Button buttonUnit() {
        Button button = new Button("Добавить из справочника", new Icon(VaadinIcon.PLUS_CIRCLE));
        return button;
    }

//    private HorizontalLayout addCom() {
//        HorizontalLayout horizontalLayout = new HorizontalLayout();
//        textCom.setWidth("850px");
//        textCom.setHeight("50px");
//        textCom.setPlaceholder("Добавить позицию - введите наименование, код, штрихкод или артикул");
//        horizontalLayout.add(textCom);
//        return horizontalLayout;
//    }



    private HorizontalLayout numberConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Перемещение №");
        label.setWidth("150px");
        returnNumber.setWidth("50px");
        horizontalLayout.add(label, returnNumber);
        movementDtoBinder.forField(returnNumber)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        //
        return horizontalLayout;
    }

    private HorizontalLayout dateConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("От");
        dateTimePicker.setWidth("350px");
        horizontalLayout.add(label, dateTimePicker);
        movementDtoBinder.forField(dateTimePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        //
        return horizontalLayout;
    }


    private HorizontalLayout companyConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<CompanyDto> list = companyService.getAll();
        if (list != null) {
            companyComboBox.setItems(list);
        }
        companyComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyComboBox.setWidth("350px");
        Label label = new Label("Организация");
        horizontalLayout.setWidth("500px");
        label.setWidth("100");
        horizontalLayout.add(label, companyComboBox);
        movementDtoBinder.forField(companyComboBox)
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
        Label label = new Label("Со склада");
        label.setWidth("100");
        horizontalLayout.add(label, warehouseComboBox);
        movementDtoBinder.forField(warehouseComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        //
        return horizontalLayout;
    }

    private HorizontalLayout warehouseConfigureOne() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<WarehouseDto> list = warehouseService.getAll();
        if (list != null) {
            warehouseComboBoxOne.setItems(list);
        }
        warehouseComboBoxOne.setItemLabelGenerator(WarehouseDto::getName);
        warehouseComboBoxOne.setWidth("350px");
        Label label = new Label("На склад");
        horizontalLayout.setWidth("590px");
        label.setWidth("95px");
        horizontalLayout.add(label, warehouseComboBoxOne);
        movementDtoBinder.forField(warehouseComboBoxOne)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        //
        return horizontalLayout;
    }

    private HorizontalLayout commentConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        textArea.setWidth("350px");
        textArea.setHeight("50px");
        textArea.setPlaceholder("Комментарий");
        horizontalLayout.add(textArea);
        return horizontalLayout;
    }

    private H4 totalPriceTitle() {
        H4 totalPriceTitle = new H4("Итого:");
        totalPriceTitle.setHeight("2.0em");
        return totalPriceTitle;
    }

    private H4 totalPrice() {
        totalPrice.setText(getTotalPrice().toString());
        totalPrice.setHeight("2.0em");
        return totalPrice;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        for (MovementProductDto movementProductDto : tempMovementProductDtoList) {
            totalPrice = totalPrice.add(movementProductDto.getPrice()
                    .multiply(movementProductDto.getAmount()));
        }
        return totalPrice;
    }

    private void setTotalPrice() {
        totalPrice.setText(
                String.format("%.2f", getTotalPrice())
        );
    }

    private Button configureDeleteButton() {
        buttonDelete.addClickListener(event -> {
            deleteInvoiceById(Long.parseLong(textArea.getValue()));
            clearAllFieldsModalView();
            buttonDelete.getUI().ifPresent(ui -> ui.navigate(location));
        });
        return buttonDelete;
    }

    public void deleteInvoiceById(Long movementDtoId) {
        movementService.deleteById(movementDtoId);
        notifications.infoNotification(String.format("Заказ № %s успешно удален", movementDtoId));
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

    private List<MovementDto> getData() {
        return movementService.getAll();
    }
}
