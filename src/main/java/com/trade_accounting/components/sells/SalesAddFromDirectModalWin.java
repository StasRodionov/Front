package com.trade_accounting.components.sells;

import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.BuyersReturnDto;
import com.trade_accounting.models.dto.ProductDto;
import com.trade_accounting.models.dto.ShipmentDto;
import com.trade_accounting.models.dto.ShipmentProductDto;
import com.trade_accounting.services.interfaces.BuyersReturnService;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.ProductService;
import com.trade_accounting.services.interfaces.ShipmentProductService;
import com.trade_accounting.services.interfaces.ShipmentService;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@SpringComponent
@PageTitle("Выбор товара из списка")
@UIScope
@Component
public class SalesAddFromDirectModalWin  extends Dialog {
    private final ProductService productService;
    private final Grid<ShipmentProductDto> grid = new Grid<>(ShipmentProductDto.class, false);
    private final Grid<ShipmentProductDto> gridAdd = new Grid<>(ShipmentProductDto.class, false);
    private List<ShipmentProductDto> data;
    private List<ShipmentProductDto> shipmentProductDtoList = new ArrayList<>();
    private TextField countTextField = new TextField();
    private final Editor<ShipmentProductDto> editor = grid.getEditor();
    private final TextField amountField = new TextField();
    private GridPaginator<ShipmentProductDto> paginator;
    private final BuyersReturnService buyersReturnService;
    private final ShipmentProductService shipmentProductService;
    private final ShipmentService shipmentService;
    private ShipmentDto dto = new ShipmentDto();
    private final ContractService contractService;
    private final WarehouseService warehouseService;
    private final ContractorService contractorService;
    private final Notifications notifications;
    private final CompanyService companyService;
    private List<ShipmentProductDto> shipmentDtoList;

    public SalesAddFromDirectModalWin (ProductService productService,
                                       ShipmentService shipmentService,
                                       ShipmentProductService shipmentProductService,
                                       ContractService contractService,
                                       WarehouseService warehouseService,
                                       ContractorService contractorService,
                                       Notifications notifications,
                                       CompanyService companyService,
                                       BuyersReturnService buyersReturnService) {
        this.productService = productService;
        this.shipmentProductService = shipmentProductService;
        this.shipmentService = shipmentService;
        this.contractService = contractService;
        this.warehouseService = warehouseService;
        this.contractorService = contractorService;
        this.notifications = notifications;
        this.companyService = companyService;
        this.buyersReturnService = buyersReturnService;
        countTextField.setValue("0");
        shipmentDtoList = new ArrayList<>();
//        data = getData();
//        paginator = new GridPaginator<>(grid, shipmentDtoList, 7);
        gridAdd.setHeight("30vh");
        grid.setHeight("38vh");
        setSizeFull();
        configureGrid();
        add(configureActions(), countTextFieldConfig(), gridAdd, grid, getBottomBar());
    }

    public void setShipment (ShipmentDto shipmentDto) {
        this.dto = shipmentDto;
        List<ShipmentDto> tmp = shipmentService.getAll().stream().filter(el -> el.getCompanyId().equals(dto.getCompanyId()) && el.getContractorId().equals(dto.getContractorId())).collect(Collectors.toList());
        for (ShipmentDto sdto : tmp) {
            List<Long> qwer = sdto.getShipmentProductsIds();
            qwer.stream().forEach(e-> shipmentDtoList.add(shipmentProductService.getById(e)));
        }
    }

    private void configureGridAdd() {
        gridAdd.removeAllColumns();
        gridAdd.setItems(shipmentProductDtoList);
        gridAdd.addColumn(inPrDto -> productService.getById(inPrDto.getProductId()).getDescription()).setHeader("Наименование");
        gridAdd.addColumn(inPrDto -> inPrDto.getAmount()).setHeader("Количество");
        gridAdd.addColumn(inPrDto -> inPrDto.getPrice()).setHeader("Цена").setId("Цена");
        gridAdd.addComponentColumn(column -> {
            Button del = new Button("Очистить");
            del.addClassName("delete");
            del.addClickListener(e -> {
                shipmentProductDtoList.remove(column);
                BigDecimal totalPrice = BigDecimal.valueOf(0.0);
                System.err.println(data);
                for (ShipmentProductDto shipmentProductDto : shipmentProductDtoList) {
                    totalPrice = totalPrice.add(shipmentProductDto.getPrice()
                            .multiply(shipmentProductDto.getAmount()));
                }
                countTextField.setValue(totalPrice.toString());
                configureGridAdd();
            });
            return del;
        });
    }

    private void configureGrid() {
        grid.removeAllColumns();
        grid.setItems(shipmentDtoList);

        grid.addColumn(inPrDto -> productService.getById(inPrDto.getProductId()).getDescription()).setHeader("Наименование");
        grid.addColumn(inPrDto -> inPrDto.getPrice()).setHeader("Цена").setId("Цена");
        grid.addColumn(inPrDto -> inPrDto.getAmount()).setHeader("Лимит").setId("Лимит");
        Collection<TextField> addAmount = Collections.newSetFromMap(new LinkedHashMap<>());
        grid.addComponentColumn(column -> {
            TextField amount = new TextField();
            amount.setWidth("100px");
            amount.addClassName("add");
            addAmount.add(amount);
            return amount;
        }).setHeader("Количество");

        grid.addComponentColumn(column -> {
            Button edit = new Button("Задать количество");
            edit.addClassName("edit");
            edit.addClickListener(e -> {
                editor.editItem(column);
                amountField.focus();
                List<TextField> list = addAmount.stream().filter(Objects::nonNull).collect(Collectors.toList());
                for (TextField amo: list){
                    if (!amo.isEmpty() && (new BigDecimal(amo.getValue()).compareTo(column.getAmount())) <= 0) {
                        ShipmentProductDto tmp = new ShipmentProductDto();
                        tmp.setProductId(column.getProductId());
                        tmp.setAmount(new BigDecimal(amo.getValue()));
                        tmp.setPrice(column.getPrice());
                        shipmentProductDtoList.add(tmp);
                        addAmount.remove(amo);
//                        column.setAmount(column.getAmount().subtract(new BigDecimal(amo.getValue())));
                    }
                }
                BigDecimal totalPrice = BigDecimal.valueOf(0.0);
                for (ShipmentProductDto shipmentProductDto : shipmentProductDtoList) {
                    totalPrice = totalPrice.add(shipmentProductDto.getPrice()
                            .multiply(shipmentProductDto.getAmount()));
                }
                countTextField.setValue(totalPrice.toString());
                configureGridAdd();
            });
            return edit;

        });
    }

    private HorizontalLayout configureActions() {
        HorizontalLayout upper = new HorizontalLayout();
        upper.add(buttonQuestion(), getTextOrder());
        return upper;
    }

    private H2 getTextOrder() {
        final H2 textOrder = new H2("Добавление товаров");
        return textOrder;
    }

    private HorizontalLayout getBottomBar() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(saveButton(), closeButton());
        return horizontalLayout;
    }

    private HorizontalLayout countTextFieldConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Общая сумма");
        label.setWidth("100px");
        horizontalLayout.add(countTextField, label);
        return horizontalLayout;
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Dialog dialog = new Dialog();
        Button cancelButton = new Button("Закрыть", event -> dialog.close());
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponentAsFirst(cancelButton);
        dialog.add(new Text("Продекты  - В этом окне Вы можете добавить необходимые продукты из прайс-листа"));
        dialog.setWidth("450px");
        dialog.setHeight("250px");
        buttonQuestion.addClickListener(event -> dialog.open());
        Shortcuts.addShortcutListener(dialog, dialog::close, Key.ESCAPE);
        dialog.add(new Div(cancelButton));
        return buttonQuestion;
    }

    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> {
            shipmentDtoList = new ArrayList<>();
            close();
        });
        return button;
    }

    private Button saveButton() {

        return new Button("Сохранить", e -> {

            ReturnBuyersReturnModalView wind = new ReturnBuyersReturnModalView(buyersReturnService,
                    contractorService,
                    warehouseService,
                    companyService,
                    notifications,
                    productService,
                    shipmentService,
                    shipmentProductService,
                    contractService);
            BuyersReturnDto brdto = new BuyersReturnDto();
            brdto.setSum(new BigDecimal(countTextField.getValue()));
            brdto.setContractorId(dto.getContractorId());
            brdto.setCompanyId(dto.getCompanyId());
            brdto.setWarehouseId(dto.getWarehouseId());
            brdto.setIsPrint(dto.getIsPrint());
            brdto.setIsSent(dto.getIsSend());
            brdto.setDate(dto.getDate());
            wind.setAcceptanceForEdit(brdto);
            close();
            wind.open();
        });
    }
}
