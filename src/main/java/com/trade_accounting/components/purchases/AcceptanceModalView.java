package com.trade_accounting.components.purchases;

import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.util.ProjectDto;
import com.trade_accounting.models.dto.warehouse.AcceptanceDto;
import com.trade_accounting.models.dto.warehouse.AcceptanceProductionDto;
import com.trade_accounting.models.dto.company.CompanyDto;
import com.trade_accounting.models.dto.company.ContractDto;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.warehouse.ProductDto;
import com.trade_accounting.models.dto.warehouse.WarehouseDto;
import com.trade_accounting.services.interfaces.util.ProjectService;
import com.trade_accounting.services.interfaces.warehouse.AcceptanceProductionService;
import com.trade_accounting.services.interfaces.warehouse.AcceptanceService;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.trade_accounting.services.interfaces.warehouse.WarehouseService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.trade_accounting.config.SecurityConstants.*;

@UIScope
@SpringComponent
//Если на страницу не ссылаются по URL или она не является отдельной страницей, а подгружается родительским классом, то URL и Title не нужен
//@Route(value = PURCHASES_ACCEPTS_VIEW, layout = AppView.class)
public class AcceptanceModalView extends Dialog {
    transient private final CompanyService companyService;
    transient private final AcceptanceService acceptanceService;
    transient private final ContractService contractService;
    transient private final WarehouseService warehouseService;
    transient private final ContractorService contractorService;
    transient private final ProjectService projectService;
    transient private AcceptanceDto dto = new AcceptanceDto();
    private final ComboBox<ContractDto> contractDtoComboBox = new ComboBox<>();
    private final ComboBox<WarehouseDto> warehouseDtoComboBox = new ComboBox<>();
    private final ComboBox<ContractorDto> contractorDtoComboBox = new ComboBox<>();
    private final ComboBox<CompanyDto> companyDtoComboBox = new ComboBox<>();
    private final ComboBox<ProductDto> productDtoComboBox = new ComboBox<>();
    private final ComboBox<ProjectDto> projectDtoComboBox = new ComboBox<>();
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final Checkbox checkboxIsSent = new Checkbox("Отправлено");
    private final Checkbox checkboxIsPrint = new Checkbox("Напечатано");
    private final TextField returnNumber = new TextField();
    private final TextArea textArea = new TextArea();
    private final Binder<AcceptanceDto> acceptanceDtoBinder = new Binder<>(AcceptanceDto.class);
    private final String TEXT_FOR_REQUEST_FIELD = "Обязательное поле";
    transient private final Notifications notifications;
    transient private final ProductService productService;
    private final Grid<AcceptanceProductionDto> grid = new Grid<>(AcceptanceProductionDto.class, false);
    private GridPaginator<AcceptanceProductionDto> paginator;
    transient private List<AcceptanceProductionDto> data;
    transient private final AcceptanceProductionService acceptanceProductionService;
    private final Editor<AcceptanceProductionDto> editor = grid.getEditor();
    private final Binder<AcceptanceProductionDto> binderInvoiceProductDto = new Binder<>(AcceptanceProductionDto.class);//Rename!!!!
    private final TextField amountField = new TextField();
    private final TextField summ = new TextField();
    private boolean isNew;
    private boolean isNewProd;
    private static final String FIELD_WIDTH = "350px";
    private static final String LABEL_WIDTH = "100px";

    @Autowired
    public AcceptanceModalView(CompanyService companyService,
                               AcceptanceService acceptanceService,
                               ContractService contractService,
                               WarehouseService warehouseService,
                               ContractorService contractorService,
                               Notifications notifications,
                               ProductService productService,
                               AcceptanceProductionService acceptanceProductionService,
                               ProjectService projectService) {
        this.companyService = companyService;
        this.acceptanceService = acceptanceService;
        this.contractService = contractService;
        this.warehouseService = warehouseService;
        this.contractorService = contractorService;
        this.notifications = notifications;
        this.productService = productService;
        this.acceptanceProductionService = acceptanceProductionService;
        this.projectService = projectService;
        isNew = true;
        isNewProd = true;
        data = getData();
        paginator = new GridPaginator<>(grid, data, 40);
        setSizeFull();
        add(headerLayout(), formLayout(), grid, paginator);
    }

    public void setAcceptanceForEdit(AcceptanceDto editDto) {
        this.dto = editDto;
        returnNumber.setValue(editDto.getId().toString());
        dateTimePicker.setValue(LocalDateTime.parse(editDto.getDate()));
        textArea.setValue(editDto.getComment());
        companyDtoComboBox.setValue(companyService.getById(editDto.getCompanyId()));
        warehouseDtoComboBox.setValue(warehouseService.getById(editDto.getWarehouseId()));
        contractDtoComboBox.setValue(contractService.getById(editDto.getContractId()));
        if (editDto.getProjectId() != null) {
            projectDtoComboBox.setValue(projectService.getById(editDto.getProjectId()));
        }
        contractorDtoComboBox.setValue(contractorService.getById(editDto.getContractorId()));
        checkboxIsSent.setValue(editDto.getIsSent());
        checkboxIsPrint.setValue(editDto.getIsPrint());
    }

    private List<AcceptanceProductionDto> getData() {
        if (dto.getAcceptanceProduction() == null) {
            dto.setAcceptanceProduction(new ArrayList<>());
        }
        return dto.getAcceptanceProduction();
    }

//    private void configureGrid() {
//        grid.removeAllColumns();
//        grid.setItems(data);
//        grid.addColumn(inPrDto -> inPrDto.getId()).setHeader("№").setId("№");
//        grid.addColumn(inPrDto -> productService.getById(inPrDto.getProductId()).getDescription()).setHeader("Название");
//        grid.addColumn(inPrDto -> inPrDto.getAmount()).setHeader("Количество");
//        grid.addColumn(inPrDto -> inPrDto.getPrice()).setHeader("Цена").setId("Цена");
//        grid.setHeight("36vh");
//        grid.setColumnReorderingAllowed(true);
//        editor.setBinder(binderInvoiceProductDto);
//    }

    private void updateSupplier() {
        dto.setId(Long.parseLong(returnNumber.getValue()));
        dto.setCompanyId(companyDtoComboBox.getValue().getId());
        dto.setContractId(contractDtoComboBox.getValue().getId());
        dto.setWarehouseId(warehouseDtoComboBox.getValue().getId());
        dto.setContractorId(contractorDtoComboBox.getValue().getId());
        dto.setProjectId(projectDtoComboBox.getValue() == null ?
                null : projectDtoComboBox.getValue().getId());
        dto.setDate(dateTimePicker.getValue().toString());
        dto.setIsSent(checkboxIsSent.getValue());
        dto.setIsPrint(checkboxIsPrint.getValue());
        dto.setComment(textArea.getValue());
//        dto.setAcceptanceProduction(data);
//        if (data.isEmpty()) {
//            dto.setAcceptanceProduction(new ArrayList<>());
//        }
        dto.setAcceptanceProduction(data);
        if (!isNew) {
            acceptanceService.update(dto);
            isNew = true;
        }
        data = null;
        clearAllFieldsModalView();
        UI.getCurrent().navigate(PURCHASES_ADMISSIONS_VIEW);
        close();

    }

    private Button saveButton() {
        return new Button("Сохранить", e -> {
            if (!acceptanceDtoBinder.validate().isOk()) {
                acceptanceDtoBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
                AcceptanceDto dto = new AcceptanceDto();
                dto.setId(Long.parseLong(returnNumber.getValue()));
                dto.setCompanyId(companyDtoComboBox.getValue().getId());
                dto.setContractId(contractDtoComboBox.getValue().getId());
                dto.setWarehouseId(warehouseDtoComboBox.getValue().getId());
                dto.setContractorId(contractorDtoComboBox.getValue().getId());
                dto.setProjectId(projectDtoComboBox.getValue() == null ?
                        null : projectDtoComboBox.getValue().getId());
                dto.setDate(dateTimePicker.getValue().toString());
                dto.setIsSent(checkboxIsSent.getValue());
                dto.setIsPrint(checkboxIsPrint.getValue());
                dto.setComment(textArea.getValue());
                acceptanceService.create(dto);

                UI.getCurrent().navigate(PURCHASES_ADMISSIONS_VIEW);
                updateSupplier();
                close();
                clearAllFieldsModalView();
                notifications.infoNotification(String.format("Приемка № %s сохранена", dto.getId()));
            }
        });
    }

//    private Button saveButton() {
//        return new Button("Сохранить", e -> {
//            if (returnNumber.getValue() != null && warehouseDtoComboBox.getValue() != null && dateTimePicker.getValue() != null &&
//                    contractDtoComboBox.getValue() != null && companyDtoComboBox.getValue() != null && contractorDtoComboBox.getValue() != null) {
//                updateSupplier();
//                clearAllFieldsModalView();
//                notifications.infoNotification(String.format("Приемка №=%s сохранена", dto.getIncomingNumber()));
//            }
//        });
//    }

    private Button addProduct() {
        Button button = new Button("Добавить продукт", new Icon(VaadinIcon.ADD_DOCK));
        button.addClickListener(e -> {
            AcceptanceProductionDto acceptanceProductionDto = new AcceptanceProductionDto();
            acceptanceProductionDto.setId(productDtoComboBox.getValue().getId());
            acceptanceProductionDto.setProductId(productDtoComboBox.getValue().getId());
            acceptanceProductionDto.setAmount(new BigDecimal(amountField.getValue()));
            acceptanceProductionDto.setPrice(productDtoComboBox.getValue().getPurchasePrice());
            Long dtoId;
            if (isNewProd) {
                dto.setAcceptanceProduction(new ArrayList<>());
                dto.setId(Long.parseLong(returnNumber.getValue()));
                dto.setWarehouseId(warehouseDtoComboBox.getValue().getId());
                dto.setDate(dateTimePicker.getValue().toString());
                dto.setContractId(contractDtoComboBox.getValue().getId());
                dto.setCompanyId(companyDtoComboBox.getValue().getId());
                dto.setContractorId(contractorDtoComboBox.getValue().getId());
                dto.setComment(textArea.getValue());
                dto.setProjectId(projectDtoComboBox.getValue() == null ?
                        null : projectDtoComboBox.getValue().getId());
                dto.setIsSent(checkboxIsSent.getValue());
                dto.setIsPrint(checkboxIsPrint.getValue());
                dtoId = acceptanceService.create(dto).body().getId();
                acceptanceProductionDto.setAcceptanceId(dtoId);
                dto.setId(dtoId);
                isNewProd = false;
            } else {
                acceptanceProductionDto.setAcceptanceId(dto.getId());
            }
            acceptanceProductionDto.setId(acceptanceProductionService.create(acceptanceProductionDto).body().getId());
            data.add(acceptanceProductionDto);
            summ.setValue(getTotalPrice().toString());
            //setAcceptanceForEdit();
        });
        return button;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = BigDecimal.valueOf(0.0);
        for (AcceptanceProductionDto acceptanceProductionDto : data) {
            totalPrice = totalPrice.add(acceptanceProductionDto.getPrice()
                    .multiply(acceptanceProductionDto.getAmount()));
        }
        return totalPrice;
    }

    private HorizontalLayout headerLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(title(), saveButton(), closeButton(), addAcceptanceButton());
        return horizontalLayout;
    }

    private VerticalLayout formLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(formLayout1(), formLayout2(), formLayout3(), formLayout4());
        return verticalLayout;
    }

    private HorizontalLayout formLayout1() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(numberConfigure(), dateConfigure(), checkboxLayout());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout2() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(companyConfigure(), warehouseConfigure(), contractorConfigure());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout3() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(contractConfigure(), productConfigure(), projectConfigure());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout4() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(amountFieldConfig(), addProduct());
        return horizontalLayout;
    }

    private HorizontalLayout formLayout5() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(commentSumm(), commentConfig());
        return horizontalLayout;
    }

    private H2 title() {
        H2 title = new H2("Добавление приемки");
        return title;
    }

    private Button closeButton() {
        Button button = new Button("Закрыть", new Icon(VaadinIcon.CLOSE));
        button.addClickListener(e -> {
            close();
            clearAllFieldsModalView();
        });
        return button;
    }

    private Button addAcceptanceButton() {
        Button button = new Button("Добавить из справочника", new Icon(VaadinIcon.PLUS));
        button.addClickListener(e -> {
            Long dtoId;
            AddFromDirectModalWin modalAddFromDirectModalWin = new AddFromDirectModalWin(productService,
                    acceptanceService,
                    acceptanceProductionService,
                    contractService,
                    warehouseService,
                    contractorService,
                    notifications,
                    companyService,
                    projectService);

            if (isNew) {
                dto.setAcceptanceProduction(new ArrayList<>());
                dto.setId(Long.parseLong(returnNumber.getValue()));
                dto.setWarehouseId(warehouseDtoComboBox.getValue().getId());
                dto.setDate(dateTimePicker.getValue().toString());
                dto.setContractId(contractDtoComboBox.getValue().getId());
                dto.setCompanyId(companyDtoComboBox.getValue().getId());
                dto.setContractorId(contractorDtoComboBox.getValue().getId());
                dto.setComment(textArea.getValue());
                dto.setProjectId(projectDtoComboBox.getValue() == null ?
                        null : projectDtoComboBox.getValue().getId());
                dto.setIsSent(checkboxIsSent.getValue());
                dto.setIsPrint(checkboxIsPrint.getValue());
                dtoId = acceptanceService.create(dto).body().getId();
                dto.setId(dtoId);
                isNewProd = false;
            } else {
                dto.setAcceptanceProduction(data);
            }
            modalAddFromDirectModalWin.setAcceptance(dto);
            modalAddFromDirectModalWin.open();
            close();
        });
        return button;
    }

    private HorizontalLayout numberConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Приемка №");
        label.setWidth("150px");
        returnNumber.setWidth("50px");
        horizontalLayout.add(label, returnNumber);
        acceptanceDtoBinder.forField(returnNumber)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(AcceptanceDto::getIdValid, AcceptanceDto::setIdValid);
        return horizontalLayout;
    }

    private HorizontalLayout dateConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("От");
        dateTimePicker.setWidth(FIELD_WIDTH);
        dateTimePicker.setRequiredIndicatorVisible(true);
        horizontalLayout.add(label, dateTimePicker);
        acceptanceDtoBinder.forField(dateTimePicker)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(AcceptanceDto::getDateValid, AcceptanceDto::setDateValid);
        return horizontalLayout;
    }

    private VerticalLayout checkboxLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(checkboxIsSent, checkboxIsPrint);
        return verticalLayout;
    }

    private HorizontalLayout warehouseConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<WarehouseDto> list = warehouseService.getAll();
        if (list != null) {
            warehouseDtoComboBox.setItems(list);
        }
        warehouseDtoComboBox.setItemLabelGenerator(WarehouseDto::getName);
        warehouseDtoComboBox.setWidth(FIELD_WIDTH);
        Label label = new Label("Склад");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, warehouseDtoComboBox);
        acceptanceDtoBinder.forField(warehouseDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(AcceptanceDto::getWarehouseDtoValid, AcceptanceDto::setWarehouseDtoValid);
        return horizontalLayout;
    }

    private HorizontalLayout contractorConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractorDto> list = contractorService.getAll();
        if (list != null) {
            contractorDtoComboBox.setItems(list);
        }
        contractorDtoComboBox.setItemLabelGenerator(ContractorDto::getName);
        contractorDtoComboBox.setWidth(FIELD_WIDTH);
        Label label = new Label("Контрагент");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, contractorDtoComboBox);
        acceptanceDtoBinder.forField(contractorDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(AcceptanceDto::getContractorDtoValid, AcceptanceDto::setContractorDtoValid);
        return horizontalLayout;
    }

    private HorizontalLayout contractConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ContractDto> list = contractService.getAll();
        if (list != null) {
            contractDtoComboBox.setItems(list);
        }
        contractDtoComboBox.setItemLabelGenerator(ContractDto::getNumber);
        contractDtoComboBox.setWidth(FIELD_WIDTH);
        contractDtoComboBox.setRequired(true);
        contractDtoComboBox.setRequiredIndicatorVisible(true);
        acceptanceDtoBinder.forField(contractDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(AcceptanceDto::getContractDtoValid, AcceptanceDto::setContractDtoValid);
        Label label = new Label("Договор");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, contractDtoComboBox);
        acceptanceDtoBinder.forField(contractDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(AcceptanceDto::getContractDtoValid, AcceptanceDto::setContractDtoValid);
        return horizontalLayout;
    }

    private HorizontalLayout companyConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<CompanyDto> list = companyService.getAll();
        if (list != null) {
            companyDtoComboBox.setItems(list);
        }
        companyDtoComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyDtoComboBox.setWidth(FIELD_WIDTH);
        companyDtoComboBox.setRequired(true);
        companyDtoComboBox.setRequiredIndicatorVisible(true);
        acceptanceDtoBinder.forField(companyDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD)
                .bind(AcceptanceDto::getCompanyDtoValid, AcceptanceDto::setCompanyDtoValid);
        Label label = new Label("Компания");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, companyDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout projectConfigure() {
        HorizontalLayout projectLayout = new HorizontalLayout();
        List<ProjectDto> projects = projectService.getAll();
        if (projects != null) {
            projectDtoComboBox.setItems(projects);
        }
        projectDtoComboBox.setItemLabelGenerator(ProjectDto::getName);
        projectDtoComboBox.setWidth(FIELD_WIDTH);
        Label label = new Label("Проект");
        label.setWidth(LABEL_WIDTH);
        projectLayout.add(label, projectDtoComboBox);
        return projectLayout;
    }

    private HorizontalLayout productConfigure() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<ProductDto> productDto = productService.getAll();
        if (productDto != null) {
            productDtoComboBox.setItems(productDto);
        }
        productDtoComboBox.setItemLabelGenerator(ProductDto::getDescription);
        productDtoComboBox.setWidth(FIELD_WIDTH);
        productDtoComboBox.setRequired(true);
        productDtoComboBox.setRequiredIndicatorVisible(true);
        acceptanceDtoBinder.forField(productDtoComboBox)
                .asRequired(TEXT_FOR_REQUEST_FIELD);
        Label label = new Label("Продукты");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, productDtoComboBox);
        return horizontalLayout;
    }

    private HorizontalLayout amountFieldConfig() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Количество");
        label.setWidth(LABEL_WIDTH);
        horizontalLayout.add(label, amountField);
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

    private HorizontalLayout commentSumm() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Сумма");
        label.setWidth("300px");
        horizontalLayout.add(label, summ);
        return horizontalLayout;
    }

    private void clearAllFieldsModalView() {
        companyDtoComboBox.setValue(null);
        contractDtoComboBox.setValue(null);
        contractorDtoComboBox.setValue(null);
        warehouseDtoComboBox.setValue(null);
        projectDtoComboBox.setValue(null);
        dateTimePicker.setValue(null);
        textArea.setValue("");
        returnNumber.setValue("");
        checkboxIsPrint.setValue(false);
        checkboxIsSent.setValue(false);
        grid.removeAllColumns();
    }
}
