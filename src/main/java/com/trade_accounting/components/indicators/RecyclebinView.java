package com.trade_accounting.components.indicators;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.goods.GoodsSubInventoryModalWindow;
import com.trade_accounting.components.goods.InternalOrderModalView;
import com.trade_accounting.components.goods.LossModalWindow;
import com.trade_accounting.components.goods.MovementViewModalWindow;
import com.trade_accounting.components.goods.PostingModal;
import com.trade_accounting.components.money.CreditOrderModal;
import com.trade_accounting.components.money.ExpenseOrderModal;
import com.trade_accounting.components.money.IncomingPaymentModal;
import com.trade_accounting.components.money.OutgoingPaymentModal;
import com.trade_accounting.components.purchases.AcceptanceModalView;
import com.trade_accounting.components.purchases.SupplierAccountModalView;
import com.trade_accounting.components.sells.SalesEditCreateInvoiceView;
import com.trade_accounting.components.sells.SalesEditShipmentView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.AcceptanceDto;
import com.trade_accounting.models.dto.AcceptanceProductionDto;
import com.trade_accounting.models.dto.CorrectionDto;
import com.trade_accounting.models.dto.CorrectionProductDto;
import com.trade_accounting.models.dto.InternalOrderDto;
import com.trade_accounting.models.dto.InternalOrderProductsDto;
import com.trade_accounting.models.dto.InventarizationDto;
import com.trade_accounting.models.dto.InventarizationProductDto;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.LossDto;
import com.trade_accounting.models.dto.LossProductDto;
import com.trade_accounting.models.dto.MovementDto;
import com.trade_accounting.models.dto.MovementProductDto;
import com.trade_accounting.models.dto.OperationsDto;
import com.trade_accounting.models.dto.PaymentDto;
import com.trade_accounting.models.dto.ShipmentDto;
import com.trade_accounting.models.dto.ShipmentProductDto;
import com.trade_accounting.models.dto.SupplierAccountDto;
import com.trade_accounting.services.interfaces.AcceptanceProductionService;
import com.trade_accounting.services.interfaces.AcceptanceService;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.CorrectionProductService;
import com.trade_accounting.services.interfaces.CorrectionService;
import com.trade_accounting.services.interfaces.InternalOrderProductsDtoService;
import com.trade_accounting.services.interfaces.InternalOrderService;
import com.trade_accounting.services.interfaces.InventarizationProductService;
import com.trade_accounting.services.interfaces.InventarizationService;
import com.trade_accounting.services.interfaces.InvoiceProductService;
import com.trade_accounting.services.interfaces.InvoiceService;
import com.trade_accounting.services.interfaces.LossProductService;
import com.trade_accounting.services.interfaces.LossService;
import com.trade_accounting.services.interfaces.MovementProductService;
import com.trade_accounting.services.interfaces.MovementService;
import com.trade_accounting.services.interfaces.OperationsService;
import com.trade_accounting.services.interfaces.PaymentService;
import com.trade_accounting.services.interfaces.ShipmentProductService;
import com.trade_accounting.services.interfaces.ShipmentService;
import com.trade_accounting.services.interfaces.SupplierAccountService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@SpringComponent
@Route(value = "recyclebinView", layout = AppView.class)
@PageTitle("Корзина")
@UIScope
public class RecyclebinView extends VerticalLayout {

    private final CreditOrderModal creditOrderModal;
    private final SalesEditCreateInvoiceView salesEditCreateInvoiceView;
    private final GoodsSubInventoryModalWindow goodsSubInventoryModalWindow;
    private final InternalOrderModalView internalOrderModalView;
    private final LossModalWindow lossModalWindow;
    private final MovementViewModalWindow movementViewModalWindow;
    private final PostingModal postingModal;
    private final IncomingPaymentModal incomingPaymentModal;
    private final OutgoingPaymentModal outgoingPaymentModal;
    private final ExpenseOrderModal expenseOrderModal;
    private final SupplierAccountModalView supplierAccountModalView;
    private final AcceptanceModalView acceptanceModalView;
    private final SalesEditShipmentView salesEditShipmentView;
    private final OperationsService operationsService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final MovementService movementService;
    private final MovementProductService movementProductService;
    private final LossService lossService;
    private final InternalOrderService internalOrderService;
    private final LossProductService lossProductService;
    private final InternalOrderProductsDtoService internalOrderProductsDtoService;
    private final PaymentService paymentService;
    private final ContractorService contractorService;
    private final CorrectionService correctionService;
    private final CorrectionProductService correctionProductService;
    private final InventarizationService inventarizationService;
    private final InventarizationProductService inventarizationProductService;
    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final AcceptanceService acceptanceService;
    private final AcceptanceProductionService acceptanceProductionService;
    private final SupplierAccountService supplierAccountService;
    private final ShipmentService shipmentService;
    private final ShipmentProductService shipmentProductService;
    private final Notifications notifications;
    private final Grid<OperationsDto> grid = new Grid<>(OperationsDto.class, false);
    private final GridPaginator<OperationsDto> paginator;

    private List<Long> movementsIds;
    private List<Long> lossIds;
    private List<Long> internalOrdersIds;
    private List<Long> paymentsIds;
    private List<Long> correctionIds;
    private List<Long> inventarizationsIds;
    private List<Long> invoiceIds;
    private List<Long> acceptanceIds;
    private List<Long> supplierAccountIds;
    private List<Long> shipmentIds;

    public RecyclebinView(CreditOrderModal creditOrderModal,
                          SalesEditCreateInvoiceView salesEditCreateInvoiceView,
                          GoodsSubInventoryModalWindow goodsSubInventoryModalWindow,
                          InternalOrderModalView internalOrderModalView,
                          LossModalWindow lossModalWindow, MovementViewModalWindow movementViewModalWindow,
                          PostingModal postingModal, IncomingPaymentModal incomingPaymentModal,
                          OutgoingPaymentModal outgoingPaymentModal, ExpenseOrderModal expenseOrderModal,
                          SupplierAccountModalView supplierAccountModalView, AcceptanceModalView acceptanceModalView,
                          SalesEditShipmentView salesEditShipmentView, OperationsService operationsService,
                          CompanyService companyService,
                          WarehouseService warehouseService, MovementService movementService,
                          MovementProductService movementProductService,
                          LossService lossService, InternalOrderService internalOrderService,
                          LossProductService lossProductService,
                          InternalOrderProductsDtoService internalOrderProductsDtoService,
                          PaymentService paymentService, ContractorService contractorService,
                          CorrectionService correctionService,
                          CorrectionProductService correctionProductService,
                          InventarizationService inventarizationService,
                          InventarizationProductService inventarizationProductService,
                          InvoiceService invoiceService, InvoiceProductService invoiceProductService,
                          AcceptanceService acceptanceService, AcceptanceProductionService acceptanceProductionService,
                          SupplierAccountService supplierAccountService, ShipmentService shipmentService,
                          ShipmentProductService shipmentProductService, Notifications notifications) {
        this.creditOrderModal = creditOrderModal;
        this.salesEditCreateInvoiceView = salesEditCreateInvoiceView;
        this.goodsSubInventoryModalWindow = goodsSubInventoryModalWindow;
        this.internalOrderModalView = internalOrderModalView;
        this.lossModalWindow = lossModalWindow;
        this.movementViewModalWindow = movementViewModalWindow;
        this.postingModal = postingModal;
        this.incomingPaymentModal = incomingPaymentModal;
        this.outgoingPaymentModal = outgoingPaymentModal;
        this.expenseOrderModal = expenseOrderModal;
        this.supplierAccountModalView = supplierAccountModalView;
        this.acceptanceModalView = acceptanceModalView;
        this.salesEditShipmentView = salesEditShipmentView;
        this.operationsService = operationsService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.movementService = movementService;
        this.movementProductService = movementProductService;
        this.lossService = lossService;
        this.internalOrderService = internalOrderService;
        this.lossProductService = lossProductService;
        this.internalOrderProductsDtoService = internalOrderProductsDtoService;
        this.paymentService = paymentService;
        this.contractorService = contractorService;
        this.correctionService = correctionService;
        this.correctionProductService = correctionProductService;
        this.inventarizationService = inventarizationService;
        this.inventarizationProductService = inventarizationProductService;
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.acceptanceService = acceptanceService;
        this.acceptanceProductionService = acceptanceProductionService;
        this.supplierAccountService = supplierAccountService;
        this.shipmentService = shipmentService;
        this.shipmentProductService = shipmentProductService;
        this.notifications = notifications;
        List<OperationsDto> data = getData();
        paginator = new GridPaginator<>(grid, data, 50);
        setSizeFull();
        configureGrid();
        setHorizontalComponentAlignment(Alignment.CENTER);
        add(getUpperLayout(), grid, paginator);
        movementsIds = getMovementsIds();
        lossIds = getLossIds();
        internalOrdersIds = getInternalOrdersIds();
        paymentsIds = getPaymentsIds();
        correctionIds = getCorrectionIds();
        inventarizationsIds = getInventarizationsIds();
        invoiceIds = getInvoiceIds();
        acceptanceIds = getAcceptanceIds();
        supplierAccountIds = getSupplierAccountIds();
        shipmentIds = getShipmentIds();

    }

    private List<OperationsDto> getData() {
        return operationsService.getAll().stream()
                .filter(operationsDto -> operationsDto.getIsRecyclebin())
                .collect(Collectors.toList());
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn("id").setWidth("5px").setHeader("ID").setResizable(true).setId("ID");
        grid.addColumn(this::getType).setHeader("Тип документа").setSortable(true);
        grid.addColumn(OperationsDto::getDate).setKey("date").setHeader("Дата").setSortable(true);
        grid.addColumn(operationsDto->companyService.getById(operationsDto.getCompanyId())
                .getName()).setKey("company").setHeader("Организация").setId("Организация");
//        grid.addColumn(this::getTotalPrice).setHeader("Сумма").setSortable(true);
        grid.addColumn(this::getDWarehouseFrom).setHeader("Со склада").setSortable(true);
        grid.addColumn(this::getDWarehouseTo).setHeader("На склад").setSortable(true);
        grid.addColumn(this::getContractor).setHeader("Контрагент").setSortable(true);
        grid.addColumn(new ComponentRenderer<>(this::getIsSentIcon)).setKey("sent").setHeader("Отправлено")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsPrintIcon)).setKey("print").setHeader("Напечатано")
                .setId("Напечатано");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addItemDoubleClickListener(e-> {
            OperationsDto dto = e.getItem();
            openModalWindow(dto);
        });

    }

    private void openModalWindow(OperationsDto dto) {
        // реализовать
    }

    private String getDWarehouseFrom(OperationsDto operationsDto) {
        String warehouseFrom;
        if (movementsIds.contains(operationsDto.getId())){
            MovementDto movementDto = movementService.getById(operationsDto.getId());
            warehouseFrom = warehouseService.getById(movementDto.getWarehouseId()).getName();
            return warehouseFrom;
        } else if (lossIds.contains(operationsDto.getId())){
            LossDto lossDto = lossService.getById(operationsDto.getId());
            warehouseFrom = warehouseService.getById(lossDto.getWarehouseId()).getName();
            return warehouseFrom;
        } else if (inventarizationsIds.contains(operationsDto.getId())) {
            InventarizationDto invDto = inventarizationService.getById(operationsDto.getId());
            warehouseFrom = warehouseService.getById(invDto.getWarehouseId()).getName();
            return warehouseFrom;
        } else if (shipmentIds.contains(operationsDto.getId())) {
            ShipmentDto shipmentDto = shipmentService.getById(operationsDto.getId());
            warehouseFrom = warehouseService.getById(shipmentDto.getWarehouseId()).getName();
            return warehouseFrom;
        }
        else {
            warehouseFrom = " ";
            return warehouseFrom;

        }
    }

    private String getDWarehouseTo(OperationsDto operationsDto) {
        String warehouseTo;
        if (movementsIds.contains(operationsDto.getId())){
            MovementDto movementDto = movementService.getById(operationsDto.getId());
            warehouseTo = warehouseService.getById(movementDto.getWarehouseToId()).getName();
            return warehouseTo;
        }else if (internalOrdersIds.contains(operationsDto.getId())) {
            InternalOrderDto internalOrderDto = internalOrderService.getById(operationsDto.getId());
            warehouseTo = warehouseService.getById(internalOrderDto.getWarehouseId()).getName();
            return warehouseTo;
        }
        else if (correctionIds.contains(operationsDto.getId())) {
            CorrectionDto correctionDto = correctionService.getById(operationsDto.getId());
            warehouseTo = warehouseService.getById(correctionDto.getWarehouseId()).getName();
            return warehouseTo;
        } else if (acceptanceIds.contains(operationsDto.getId())) {
            AcceptanceDto acceptanceDto = acceptanceService.getById(operationsDto.getId());
            warehouseTo = warehouseService.getById(acceptanceDto.getWarehouseId()).getName();
            return warehouseTo;
        }
        else {
            warehouseTo = " ";
            return warehouseTo;

        }
    }

    private String getContractor(OperationsDto operationsDto) {
        String contractor;
        if (paymentsIds.contains(operationsDto.getId())) {
            PaymentDto paymentDto = paymentService.getById(operationsDto.getId());
            contractor = contractorService.getById(paymentDto.getContractorId()).getName();
            return contractor;
        }  else if (invoiceIds.contains(operationsDto.getId())) {
            InvoiceDto invoiceDto = invoiceService.getById(operationsDto.getId());
            contractor = contractorService.getById(invoiceDto.getContractorId()).getName();
            return contractor;
        } else if (acceptanceIds.contains(operationsDto.getId())) {
            AcceptanceDto acceptanceDto = acceptanceService.getById(operationsDto.getId());
            contractor = contractorService.getById(acceptanceDto.getContractorId()).getName();
            return contractor;
        } else if (supplierAccountIds.contains(operationsDto.getId())) {
            SupplierAccountDto supplierAccountDto = supplierAccountService.getById(operationsDto.getId());
            contractor = contractorService.getById(supplierAccountDto.getContractorId()).getName();
            return contractor;
        } else if (shipmentIds.contains(operationsDto.getId())) {
            ShipmentDto shipmentDto = shipmentService.getById(operationsDto.getId());
            contractor = contractorService.getById(shipmentDto.getContractorId()).getName();
            return contractor;
        }
        else {
            contractor  =" ";
            return contractor;
        }
    }


    private Component getUpperLayout(){
        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.add(buttonQuestion(), title(), buttonRefresh(), deleteAllOperation(), buttonFilter(),
                textField(), numberField(), valueSelect(), valuePrint());
        mainLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return mainLayout;
    }

    private Button buttonQuestion() {
        return Buttons.buttonQuestion("В разделе хранятся удаленные документы — " +
                "их можно восстановить в течение 7 дней после удаления. " +
                "По истечении этого срока документы окончательно стираются.");
    }

    private H2 title() {
        H2 textCompany = new H2("Корзина");
        textCompany.setHeight("2.2em");
        return textCompany;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(ev -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
        //реализовать
        return buttonFilter;
    }

    private TextField textField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("Номер, компания или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> updateList(textField.getValue()));
        return textField;
    }

    private void updateList(String value) {
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }


    private Select<String> valueSelect() {
        Select<String> select = new Select<>();
        List<String> listItems = new ArrayList<>();
        listItems.add("Изменить");
        listItems.add("Восстановить");
        listItems.add("Удалить");
        select.setItems(listItems);
        select.setValue("Изменить");
        select.setWidth("130px");
        select.addValueChangeListener(e -> {
            if (select.getValue().equals("Восстановить")) {
                restoreOperation(grid.getSelectedItems());
                grid.deselectAll();
                select.setValue("Изменить");
                paginator.setData(getData());
                updateList();
            } else if (select.getValue().equals("Удалить")) {
                deleteOperation(grid.getSelectedItems());
                grid.deselectAll();
                select.setValue("Изменить");
                paginator.setData(getData());
                updateList();
            }
        });
        return select;
    }

    private void restoreOperation(Set<OperationsDto> set) {
        if (!set.isEmpty()) {
            for (OperationsDto dto : set) {
                Long id = dto.getId();
                if (movementsIds.contains(id)) {
                    movementService.restoreFromIsRecyclebin(id);
                } else if (lossIds.contains(id)) {
                    lossService.restoreFromIsRecyclebin(id);
                } else if (internalOrdersIds.contains(id)) {
                    internalOrderService.restoreFromIsRecyclebin(id);
                } else if (paymentsIds.contains(id)) {
                    paymentService.restoreFromIsRecyclebin(id);
                } else if (correctionIds.contains(id)) {
                    correctionService.restoreFromIsRecyclebin(id);
                } else if (inventarizationsIds.contains(id)) {
                    inventarizationService.restoreFromIsRecyclebin(id);
                } else if (invoiceIds.contains(id)) {
                    invoiceService.restoreFromIsRecyclebin(id);
                } else if (acceptanceIds.contains(id)) {
                    acceptanceService.restoreFromIsRecyclebin(id);
                } else if (supplierAccountIds.contains(id)) {
                    supplierAccountService.restoreFromIsRecyclebin(id);
                } else if (shipmentIds.contains(id)) {
                    shipmentService.restoreFromIsRecyclebin(id);
                }
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные ");
        }
    }

    private void deleteOperation(Set<OperationsDto> set) {
        if (!set.isEmpty()) {
            for (OperationsDto dto : set) {
                Long id = dto.getId();
                if (movementsIds.contains(id)) {
                    movementService.deleteById(id);
                } else if (lossIds.contains(id)) {
                    lossService.deleteById(id);
                } else if (internalOrdersIds.contains(id)) {
                    internalOrderService.deleteById(id);
                } else if (paymentsIds.contains(id)) {
                    paymentService.deleteById(id);
                } else if (correctionIds.contains(id)) {
                    correctionService.deleteById(id);
                } else if (inventarizationsIds.contains(id)) {
                    inventarizationService.deleteById(id);
                } else if (invoiceIds.contains(id)) {
                    invoiceService.deleteById(id);
                } else if (acceptanceIds.contains(id)) {
                    acceptanceService.deleteById(id);
                } else if (supplierAccountIds.contains(id)) {
                    supplierAccountService.deleteById(id);
                } else if (shipmentIds.contains(id)) {
                    shipmentService.deleteById(id);
                }
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные ");
        }
    }

    private Button deleteAllOperation () {
        Button buttonDeleteAll = new Button("Очистить корзину");
        buttonDeleteAll.addClickListener(e -> {
            deleteOperation(new HashSet<>(getData()));
            updateList();
        });

        return buttonDeleteAll;
    }

    private Select<String> valuePrint() {
        Select<String> print = new Select<>();
        print.setItems("Печать", "Добавить шаблон");
        print.setValue("Печать");
        print.setWidth("130px");
        return print;
    }


    private void updateList() {
        grid.setItems(getData());
        movementsIds = getMovementsIds();
        lossIds = getLossIds();
        internalOrdersIds = getInternalOrdersIds();
        paymentsIds = getPaymentsIds();
        correctionIds = getCorrectionIds();
        inventarizationsIds = getInventarizationsIds();
        invoiceIds = getInvoiceIds();
        acceptanceIds = getAcceptanceIds();
        supplierAccountIds = getSupplierAccountIds();
        shipmentIds = getShipmentIds();
    }

    private Component getIsSentIcon(OperationsDto operationsDto) {
        if (operationsDto.getIsSent()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private List<Long> getMovementsIds(){
        List<Long> movementsIds = new ArrayList<>();
        for (MovementDto movementDto : movementService.getAll()) {
            movementsIds.add(movementDto.getId());
        }
        return movementsIds;
    }

    private List<Long> getLossIds(){
        List<Long> lossList = new ArrayList<>();
        for (LossDto lossDto : lossService.getAll()) {
            lossList.add(lossDto.getId());
        }
        return lossList;
    }

    private List<Long> getInternalOrdersIds(){
        List<Long> internalIds = new ArrayList<>();
        for (InternalOrderDto internalOrderDto : internalOrderService.getAll()) {
            internalIds.add(internalOrderDto.getId());
        }
        return internalIds;
    }

    private List<Long> getPaymentsIds() {
        List<Long> paymentsIds = new ArrayList<>();
        for(PaymentDto paymentDto : paymentService.getAll()) {
            paymentsIds.add(paymentDto.getId());
        }
        return paymentsIds;
    }

    private List<Long> getCorrectionIds() {
        List<Long> corrIds = new ArrayList<>();
        for (CorrectionDto correctionDto : correctionService.getAll()) {
            corrIds.add(correctionDto.getId());
        }
        return corrIds;
    }

    private List<Long> getInventarizationsIds() {
        List<Long> invIds = new ArrayList<>();
        for (InventarizationDto inventarizationDto : inventarizationService.getAll()) {
            invIds.add(inventarizationDto.getId());
        }
        return invIds;
    }

    private List<Long> getInvoiceIds() {
        List<Long> invIds = new ArrayList<>();
        for (InvoiceDto invoiceDto : invoiceService.getAll()) {
            invIds.add(invoiceDto.getId());
        }
        return invIds;
    }

    private List<Long> getAcceptanceIds(){
        List<Long> accIds = new ArrayList<>();
        for (AcceptanceDto acceptanceDto : acceptanceService.getAll()) {
            accIds.add(acceptanceDto.getId());
        }
        return accIds;
    }

    private List<Long> getSupplierAccountIds(){
        List<Long> suppIds = new ArrayList<>();
        for (SupplierAccountDto supplierAccountDto : supplierAccountService.getAll()) {
            suppIds.add(supplierAccountDto.getId());
        }
        return suppIds;
    }

    private List<Long> getShipmentIds() {
        List<Long> shipIds = new ArrayList<>();
        for (ShipmentDto shipmentDto : shipmentService.getAll()){
            shipIds.add(shipmentDto.getId());
        }
        return shipIds;
    }


    private String getTotalPrice(OperationsDto operationsDto){
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        if (movementsIds.contains(operationsDto.getId())) {
            MovementDto movementDto = movementService.getById(operationsDto.getId());
            for (Long id : movementDto.getMovementProductsIds()) {
                MovementProductDto movementProductDto = movementProductService.getById(id);
                totalPrice = totalPrice.add(movementProductDto.getAmount()
                        .multiply(movementProductDto.getPrice()));
            }
            return String.format("%.2f", totalPrice);
        } else if (internalOrdersIds.contains(operationsDto.getId())){
            InternalOrderDto internalDto = internalOrderService.getById(operationsDto.getId());
            for (Long id: internalDto.getInternalOrderProductsIds()) {
                InternalOrderProductsDto internalProducts = internalOrderProductsDtoService.getById(id);
                totalPrice = totalPrice.add(internalProducts.getAmount()).multiply(internalProducts.getPrice());
            }
            return String.format("%.2f", totalPrice);
        } else if (lossIds.contains(operationsDto.getId())){
            LossDto lossDto = lossService.getById(operationsDto.getId());
            for (Long id : lossDto.getLossProductsIds()) {
                LossProductDto lossProductDto = lossProductService.getById(id);
                totalPrice = totalPrice.add(lossProductDto.getAmount().multiply(lossProductDto.getPrice()));
            }
            return String.format("%.2f", totalPrice);
        } else if (paymentsIds.contains(operationsDto.getId())){
            PaymentDto paymentDto = paymentService.getById(operationsDto.getId());
            totalPrice = paymentDto.getSum();
            return String.format("%.2f", totalPrice);
        } else if (inventarizationsIds.contains(operationsDto.getId())) {
            InventarizationDto invDto = inventarizationService.getById(operationsDto.getId());
            for(Long id : invDto.getInventarizationProductIds()) {
                InventarizationProductDto invPrDto = inventarizationProductService.getById(id);
                totalPrice = totalPrice.add(invPrDto.getActualAmount().multiply(invPrDto.getPrice()));
            }
            return String.format("%.2f", totalPrice);
        }
        else if (correctionIds.contains(operationsDto.getId())) {
            CorrectionDto correctionDto = correctionService.getById(operationsDto.getId());
            for (Long id : correctionDto.getCorrectionProductIds()) {
                CorrectionProductDto corrPrDto = correctionProductService.getById(id);
                totalPrice = totalPrice.add(corrPrDto.getAmount().multiply(corrPrDto.getPrice()));
            }
            return String.format("%.2f", totalPrice);
        } else if (shipmentIds.contains(operationsDto.getId())){
            ShipmentDto shipmentDto = shipmentService.getById(operationsDto.getId());
            for (Long id : shipmentDto.getShipmentProductsIds()) {
                ShipmentProductDto shipmentProductDto = shipmentProductService.getById(id);
                totalPrice = totalPrice.add(shipmentProductDto.getAmount().multiply(shipmentProductDto.getPrice()));
            }
            return String.format("%.2f", totalPrice);
        }

        else if (acceptanceIds.contains(operationsDto.getId())) {
            AcceptanceDto acceptanceDto = acceptanceService.getById(operationsDto.getId());
            for (Long id : acceptanceDto.getAcceptanceProductIds()) {
                AcceptanceProductionDto acceptanceProductionDto = acceptanceProductionService.getById(id);
                totalPrice = totalPrice.add(acceptanceProductionDto.getAmount().multiply(acceptanceProductionDto.getPrice()));
            }
            return String.format("%.2f", totalPrice);
        }
        else {
            return String.valueOf(0.00);
        }
    }

    private Component getIsPrintIcon(OperationsDto operationsDto) {
        if (operationsDto.getIsPrint()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private String getType(OperationsDto operationsDto) {
        String typeOfOperation;
        if (movementsIds.contains(operationsDto.getId())) {
            typeOfOperation = "Перемещение";
            return typeOfOperation;
        } else if (lossIds.contains(operationsDto.getId())) {
            typeOfOperation = "Списание";
            return typeOfOperation;
        } else if (internalOrdersIds.contains(operationsDto.getId())) {
            typeOfOperation = "Внутренний заказ";
            return typeOfOperation;
        } else if (paymentsIds.contains(operationsDto.getId())) {
            PaymentDto paymentDto = paymentService.getById(operationsDto.getId());
            typeOfOperation = paymentDto.getTypeOfDocument();
            return typeOfOperation;
        } else if (correctionIds.contains(operationsDto.getId())) {
            typeOfOperation = "Оприходование";
            return typeOfOperation;
        } else if (inventarizationsIds.contains(operationsDto.getId())) {
            typeOfOperation = "Инвентаризация";
            return typeOfOperation;
        } else if (invoiceIds.contains(operationsDto.getId())) {
            InvoiceDto invoiceDto = invoiceService.getById(operationsDto.getId());
            if (invoiceDto.getTypeOfInvoice().equals("EXPENSE")) {
                typeOfOperation = "Заказ поставщику";
            } else {
                typeOfOperation = "Заказ покупателя";
            }
            return typeOfOperation;
        } else if (acceptanceIds.contains(operationsDto.getId())) {
            typeOfOperation = "Приемки";
            return typeOfOperation;
        } else if (supplierAccountIds.contains(operationsDto.getId())){
            typeOfOperation = "Счет поставщика";
            return typeOfOperation;
        } else if (shipmentIds.contains(operationsDto.getId())) {
            typeOfOperation = "Отгрузка";
            return typeOfOperation;
        }
        else {
            typeOfOperation = "неизвестно";
            return typeOfOperation;
        }
    }

}