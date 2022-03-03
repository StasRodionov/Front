package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.goods.print.PrintMovementTorg13Xls;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.company.BankAccountDto;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.warehouse.MovementDto;
import com.trade_accounting.models.dto.warehouse.MovementProductDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.company.BankAccountService;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.LegalDetailService;
import com.trade_accounting.services.interfaces.units.UnitService;
import com.trade_accounting.services.interfaces.warehouse.MovementProductService;
import com.trade_accounting.services.interfaces.warehouse.MovementService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
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
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.io.File;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Route(value = "goods/add_moving", layout = AppView.class)
@PageTitle("Добавить перемещение")
@PreserveOnRefresh
@SpringComponent
@UIScope
public class MovementViewModalWindow extends Dialog {

    private final ProductService productService;
    private final MovementService movementService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final Notifications notifications;
    private final UnitService unitService;
    private MovementDto movementDto;
    private final MovementProductService movementProductService;
    private final LegalDetailService legalDetailService;
    private final BankAccountService bankAccountService;

    private final ComboBox<CompanyDto> companyComboBox = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseComboBox = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseComboBoxOne = new ComboBox<>();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final Checkbox checkboxIsSent = new Checkbox("Отправлено");
    private final Checkbox checkboxIsPrint = new Checkbox("Напечатано");
    private final TextField returnNumber = new TextField();
    private final TextArea textArea = new TextArea();
    private final TextArea textCom = new TextArea();
    private final Label labelSum = new Label();
    private List<MovementProductDto> tempMovementProductDtoList;
    private final MultiselectComboBox<Long> movementProductsIdComboBox = new MultiselectComboBox();

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
    private final String pathForSaveXlsTemplate = "src/main/resources/xls_templates/goods_templates/movement";

    public MovementViewModalWindow(ProductService productService, MovementService movementService, WarehouseService warehouseService,
                                   CompanyService companyService,
                                   Notifications notifications,
                                   UnitService unitService,
                                   MovementProductService movementProductService,
                                   LegalDetailService legalDetailService,
                                   BankAccountService bankAccountService) {
        this.productService = productService;
        this.movementService = movementService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.notifications = notifications;
        this.unitService = unitService;
        this.movementProductService = movementProductService;
        this.legalDetailService = legalDetailService;
        this.bankAccountService = bankAccountService;

        this.tempMovementProductDtoList = new ArrayList<>();
        paginator = new GridPaginator<>(grid, tempMovementProductDtoList, 50);
//        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, paginator);

        setSizeFull();
        add(headerLayout(), formLayout(), grid, paginator);
        configureGrid();

    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
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

//    private Button buttonRefresh() {
//        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
//        buttonRefresh.addClickListener(ev -> getData());
//        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
//        return buttonRefresh;
//    }

    public void setMovementForEdit(MovementDto editDto) {
        this.movementDto = editDto;
        returnNumber.setValue(editDto.getId().toString());
        dateTimePicker.setValue(LocalDateTime.parse(editDto.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        textArea.setValue(editDto.getComment());
        labelSum.setText(String.valueOf(editDto.getSum()));
        checkboxIsSent.setValue(movementDto.getIsSent());
        checkboxIsPrint.setValue(movementDto.getIsPrint());
        warehouseComboBox.setValue(warehouseService.getById(editDto.getWarehouseId()));
        companyComboBox.setValue(companyService.getById(editDto.getCompanyId()));
        warehouseComboBoxOne.setValue(warehouseService.getById(editDto.getWarehouseToId()));
        movementProductsIdComboBox.setValue(new HashSet<>(movementDto.getMovementProductsIds()));


        configureDeleteButton(editDto.getId());
        configurePrintButtonContextMenu(editDto);
        configureSendButton(editDto);

        getData();

    }

    private void configurePrintButtonContextMenu(MovementDto movementDto) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setTarget(buttonPrint);
        contextMenu.setOpenOnClick(true);
        SubMenu subMenuTorg13 = contextMenu.addItem(new Div(new Text("ТОРГ-13"))).getSubMenu();
        subMenuTorg13.addItem("Открыть в браузере");
        getXlsFiles().forEach(x -> subMenuTorg13.addItem(getLinkToXlsTemplate(x)));
        subMenuTorg13.addItem("Скачать в формате PDF");
        subMenuTorg13.addItem("Скачать в формате Open Office Calc");

        contextMenu.addItem("...");
    }

    private Anchor getLinkToXlsTemplate(File file) {
        String templateName = file.getName();
        List<MovementProductDto> products = movementDto.getMovementProductsIds().stream()
                .map(movementProductService::getById)
                .collect(Collectors.toList());
        PrintMovementTorg13Xls printMovementTorg13Xls = new PrintMovementTorg13Xls(file.getPath(), products, getParamsForTemplate(products), productService, unitService);
        return new Anchor(new StreamResource(templateName, printMovementTorg13Xls::createReport), "Скачать в формате Excel");
    }

    private Map<String, String> getParamsForTemplate(List<MovementProductDto> productDtos) {
        HashMap params = new HashMap();
        CompanyDto companyDto = companyComboBox.getValue();
        WarehouseDto warehouseDtoFrom = warehouseComboBox.getValue();
        WarehouseDto warehouseDtoTo = warehouseComboBoxOne.getValue();
        List<Long> bankAccountDtoIds = companyDto.getBankAccountDtoIds();  //todo сделать поле bankAccountDtoIds Non Null
        List<BankAccountDto> bankAccountDtos = bankAccountDtoIds != null ?
                bankAccountDtoIds.stream().map(bankAccountService::getById).collect(Collectors.toList()) :
                null;
        params.put("organization", companyDto.getName());
        params.put("okpo", legalDetailService.getById(companyDto.getLegalDetailDtoId()).getOkpo());
        params.put("id", companyDto.getId().toString());
        params.put("receiver", warehouseDtoFrom.getName());
        params.put("recipient", warehouseDtoTo.getName());
        params.put("correspondentAccount", bankAccountDtos != null ? bankAccountDtos.get(0) : "");
        params.put("resultAmount", productDtos.stream().map(MovementProductDto::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).toPlainString());
        params.put("resultSum", labelSum.getText());
        return params;
    }

    private List<File> getXlsFiles() {
        File dir = new File(pathForSaveXlsTemplate);
        return Arrays.stream(Objects.requireNonNull(dir.listFiles())).filter(File::isFile).filter(x -> x.getName()
                .contains(".xls")).collect(Collectors.toList());
    }

    private void configureSendButton(MovementDto movementDto) {

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setTarget(buttonSend);
        contextMenu.setOpenOnClick(true);

        Div container = new Div();
        container.setWidth("160px");
        container.setId("containerTorg13");
        container.add(new Span("TОРГ-13"));

        if (Boolean.TRUE.equals(movementDto.getIsSent())) {
            container.add(new Span(" отправлено"));
        }

        contextMenu.addItem(container, event -> {
            if (Boolean.FALSE.equals(movementDto.getIsSent())) {
                //TODO movementDto should be updated before call updateTorg13
                movementService.updateTorg13(movementDto,
                        companyService.getById(movementDto.getCompanyId()),
                        warehouseService.getById(movementDto.getWarehouseToId()));

                movementService.update(movementDto);
                HttpClient httpClient = HttpClient.newHttpClient();
                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:4445/api/movements/files/send/torg13/xls"))
                        .build();
                httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::statusCode)
                        .thenAccept(integer -> {
                            System.out.println("200 ОК : Документ успешно отправлен на почту");
                            movementDto.setIsSent(true);

                        }).join();
                if (Boolean.TRUE.equals(movementDto.getIsSent())) {
                    container.add(new Span(" отправлено"));
                    movementService.update(movementDto);
                    checkboxIsSent.setValue(true);
                    //TODO Add new modal window with message info to.. subject.. text.. etc
                }
            }
        });
        contextMenu.addItem("Комплект . . .");
    }

    private HorizontalLayout headerLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(
                title(), saveButton(), closeButton(), buttonUnit(),
                buttonDelete, buttonPrint, buttonSend);
        horizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return horizontalLayout;
    }

    private VerticalLayout formLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(formLayout1(), formLayout2(), formLayout4(), formLayout6());
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
                MovementDto dto = new MovementDto();
                dto.setId(Long.parseLong(returnNumber.getValue()));
                dto.setCompanyId(companyComboBox.getValue().getId());
                dto.setWarehouseToId(warehouseComboBox.getValue().getId());
                dto.setWarehouseId(warehouseComboBox.getValue().getId());
                dto.setDate(dateTimePicker.getValue().toString());
                dto.setIsSent(checkboxIsSent.getValue());
                dto.setIsPrint(checkboxIsPrint.getValue());
                dto.setComment(textArea.getValue());
                List<Long> idList = new ArrayList<>(movementProductsIdComboBox.getValue());
                dto.setMovementProductsIds(idList);
                movementService.create(dto);

                UI.getCurrent().navigate("goods");
                close();
                clearAllFieldsModalView();
                notifications.infoNotification(String.format("Перемещение c ID=%s сохранен", dto.getId()));
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
        horizontalLayout.add(warehouseConfigureOne(), commentConfig());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout6() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(movementProductsConfigure(), totalPriceTitle());
        return horizontalLayout;
    }

//    private HorizontalLayout formLayout5() {
//        HorizontalLayout horizontalLayout = new HorizontalLayout();
//        horizontalLayout.add(addCom(), buttonUnit());
//        return horizontalLayout;
//    }

    private Button buttonUnit() {
        Button button = new Button("Добавить из справочника", new Icon(VaadinIcon.PLUS_CIRCLE));
        button.addClickListener(e -> {
            close();
            MovementProductsModalView modalView = new MovementProductsModalView(movementProductService,
                    productService,
                    companyService,
                    warehouseService,
                    movementService,
                    unitService,
                    legalDetailService,
                    bankAccountService,
                    notifications);
            modalView.open();
        });
        return button;
    }

    private void getData() {
        List<MovementProductDto> productDtosList = new ArrayList<>();

        for (Long id : movementDto.getMovementProductsIds()) {
            MovementProductDto productDto = movementProductService.getById(id);
            productDtosList.add(productDto);
        }
        grid.setItems(productDtosList);
    }


    private HorizontalLayout movementProductsConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        List<MovementDto> iod = movementService.getAll();
        List<Long> checkInIod = new ArrayList<>();
        for (MovementDto id : iod) {
            List<Long> list = id.getMovementProductsIds();
            checkInIod.addAll(list);
        }

        List<MovementProductDto> labels = movementProductService.getAll();
        List<Long> items = new ArrayList<>();
        for (MovementProductDto id : labels) {
            Long check = id.getId();
            if (!(checkInIod.contains(check))) {
                items.add(id.getId());
            }
        }

        movementProductsIdComboBox.setItems(items);
        movementProductsIdComboBox.setItemLabelGenerator(item -> productService.getById(movementProductService
                .getById(item).getProductId()).getName());
        movementProductsIdComboBox.setWidth("350px");
        Label label = new Label("Список товаров");
        label.setWidth("100px");
        horizontalLayout.add(label, movementProductsIdComboBox);

        movementDtoBinder.forField(movementProductsIdComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(movementDto -> new HashSet<>(movementDto.getMovementProductsIds()),
                        (movementDto, movementDto1) -> movementDto.setMovementProductsIds(movementDto
                                .getMovementProductsIds()));
        UI.getCurrent().navigate("goods");
        return horizontalLayout;
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
        horizontalLayout.setWidth("460px");
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
        Label label = new Label("Комментарий");
        label.setWidth("95px");
        horizontalLayout.add(label, textArea);
        return horizontalLayout;
    }

    private HorizontalLayout totalPriceTitle() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Итого: ");
        label.setWidth("60px");
        labelSum.setWidth("60px");
        horizontalLayout.add(label, labelSum);
        return horizontalLayout;
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

    private void configureDeleteButton(Long id) {
        buttonDelete.addClickListener(event -> {
            deleteInvoiceById(id);
            UI.getCurrent().close();
            clearAllFieldsModalView();
        });
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

}
