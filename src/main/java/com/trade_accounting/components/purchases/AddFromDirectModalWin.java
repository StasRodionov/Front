package com.trade_accounting.components.purchases;


import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.warehouse.AcceptanceDto;
import com.trade_accounting.models.dto.warehouse.AcceptanceProductionDto;
import com.trade_accounting.models.dto.warehouse.ProductDto;
import com.trade_accounting.services.interfaces.warehouse.AcceptanceProductionService;
import com.trade_accounting.services.interfaces.warehouse.AcceptanceService;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@SpringComponent
@PageTitle("Выбор товара из списка")
@UIScope
@Component
public class AddFromDirectModalWin extends Dialog {
    private final ProductService productService;
    private final Grid<AcceptanceProductionDto> grid = new Grid<>(AcceptanceProductionDto.class, false);
    private final Grid<AcceptanceProductionDto> gridAdd = new Grid<>(AcceptanceProductionDto.class, false);
    private List<AcceptanceProductionDto> data;
    private List<AcceptanceProductionDto> acceptanceProduction = new ArrayList<>();
    private TextField countTextField = new TextField();
    private final Editor<AcceptanceProductionDto> editor = grid.getEditor();
    private final TextField amountField = new TextField();
    private final AcceptanceProductionService acceptanceProductionService;
    private final AcceptanceService acceptanceService;
    private AcceptanceDto dto;
    private final ContractService contractService;
    private final WarehouseService warehouseService;
    private final ContractorService contractorService;
    private final Notifications notifications;
    private final CompanyService companyService;

    public AddFromDirectModalWin (ProductService productService,
                                  AcceptanceService acceptanceService,
                                  AcceptanceProductionService acceptanceProductionService,
                                  ContractService contractService,
                                  WarehouseService warehouseService,
                                  ContractorService contractorService,
                                  Notifications notifications,
                                  CompanyService companyService) {
        this.productService = productService;
        this.acceptanceProductionService = acceptanceProductionService;
        this.acceptanceService = acceptanceService;
        this.contractService = contractService;
        this.warehouseService = warehouseService;
        this.contractorService = contractorService;
        this.notifications = notifications;
        this.companyService = companyService;
        countTextField.setValue("0");
        data = getData();
        paginator = new GridPaginator<>(grid, data, 7);
        gridAdd.setHeight("30vh");
        grid.setHeight("38vh");
        setSizeFull();
        configureGrid();
        add(configureActions(), countTextFieldConfig(), gridAdd, grid,paginator, getBottomBar());
    }

    public void setAcceptance (AcceptanceDto acceptanceDto) {
        this.dto = acceptanceDto;
        dto.setAcceptanceProduction(acceptanceProductionService.getAll().stream().filter(el -> el.getAcceptanceId() == dto.getId()).collect(Collectors.toList()));
    }

    private void configureGridAdd() {
        gridAdd.removeAllColumns();
        gridAdd.setItems(acceptanceProduction);
        gridAdd.addColumn(inPrDto -> productService.getById(inPrDto.getProductId()).getDescription()).setHeader("Наименование");
        gridAdd.addColumn(inPrDto -> inPrDto.getAmount()).setHeader("Количество");
        gridAdd.addColumn(inPrDto -> inPrDto.getPrice()).setHeader("Цена").setId("Цена");
        gridAdd.addComponentColumn(column -> {
            Button del = new Button("Очистить");
            del.addClassName("delete");
            del.addClickListener(e -> {
                acceptanceProduction.remove(column);
                BigDecimal totalPrice = BigDecimal.valueOf(0.0);
                System.err.println(data);
                for (AcceptanceProductionDto acceptanceProductionDto : acceptanceProduction) {
                    totalPrice = totalPrice.add(acceptanceProductionDto.getPrice()
                            .multiply(acceptanceProductionDto.getAmount()));
                }
                countTextField.setValue(totalPrice.toString());
                configureGridAdd();
            });
            return del;
        });
    }

    private GridPaginator<AcceptanceProductionDto> paginator;
    private void configureGrid() {
        grid.setItems(data);
        grid.addColumn(inPrDto -> productService.getById(inPrDto.getProductId()).getDescription()).setHeader("Наименование");
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
                    if (!amo.isEmpty()) {
                        AcceptanceProductionDto tmp = new AcceptanceProductionDto();
                        tmp.setProductId(column.getProductId());
                        tmp.setAmount(new BigDecimal(amo.getValue()));
                        tmp.setPrice(column.getPrice());
                        tmp.setAcceptanceId(dto.getId());
                        acceptanceProduction.add(tmp);
                        addAmount.remove(amo);
                    }
                }
                BigDecimal totalPrice = BigDecimal.valueOf(0.0);
                for (AcceptanceProductionDto acceptanceProductionDto : acceptanceProduction) {
                    totalPrice = totalPrice.add(acceptanceProductionDto.getPrice()
                            .multiply(acceptanceProductionDto.getAmount()));
                }
                countTextField.setValue(totalPrice.toString());
                configureGridAdd();
            });
            return edit;

        });
        grid.addColumn(inPrDto -> inPrDto.getPrice()).setHeader("Цена").setId("Цена");
    }

    private List<AcceptanceProductionDto> getData() {
        List<AcceptanceProductionDto> productList = new ArrayList<>();
        for(ProductDto productDto : productService.getAll()){
            AcceptanceProductionDto tmp = new AcceptanceProductionDto();
            tmp.setProductId(productDto.getId());
            tmp.setPrice(productDto.getPurchasePrice());
            productList.add(tmp);
        }
        return productList;
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
            close();
        });
        return button;
    }

    private Button saveButton() {

        return new Button("Сохранить", e -> {
            AcceptanceModalView wind = new AcceptanceModalView(companyService,
                    acceptanceService,
                    contractService,
                    warehouseService,
                    contractorService,
                    notifications,
                    productService,
                    acceptanceProductionService);
            for (AcceptanceProductionDto newAcceptanceProductionDto : acceptanceProduction) {
                newAcceptanceProductionDto.setId(acceptanceProductionService.create(newAcceptanceProductionDto).body().getId());
            }
            dto.setAcceptanceProduction(acceptanceProductionService.getAll().stream().filter(el -> el.getAcceptanceId() == dto.getId()).collect(Collectors.toList()));
            wind.setAcceptanceForEdit(dto);
            close();
            wind.open();
        });
    }
}
