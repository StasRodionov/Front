package com.trade_accounting.components.goods;

import com.trade_accounting.models.dto.client.DepartmentDto;
import com.trade_accounting.models.dto.client.EmployeeDto;
import com.trade_accounting.models.dto.company.TaxSystemDto;
import com.trade_accounting.models.dto.warehouse.ProductGroupDto;
import com.trade_accounting.services.interfaces.client.DepartmentService;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.company.TaxSystemService;
import com.trade_accounting.services.interfaces.warehouse.ProductGroupService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ErrorLevel;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;


@SpringComponent
@UIScope
@Slf4j
public class ProductGroupModalWindow extends Dialog {
    private final ProductGroupService productGroupService;
    private final TaxSystemService taxSystemService;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    private final TextField nameField = new TextField();
    private final TextField descriptionField = new TextField();
    private final ComboBox<ProductGroupDto> productGroupDtoComboBox = new ComboBox<>();
    private final TextField saleTax = new TextField();
    private final ComboBox<TaxSystemDto> taxSystemDtoComboBox = new ComboBox<>();
    private final ComboBox<EmployeeDto> employeeDtoComboBox = new ComboBox<>();
    private final ComboBox<DepartmentDto> departmentDtoComboBox = new ComboBox<>();

    private final Binder<ProductGroupDto> productGroupDtoBinder = new Binder<>(ProductGroupDto.class);

    private final HorizontalLayout footer = new HorizontalLayout();

    private ProductGroupDto productGroupDto;


    public ProductGroupModalWindow(ProductGroupService productGroupService,
                                   TaxSystemService taxSystemService,
                                   EmployeeService employeeService,
                                   DepartmentService departmentService) {
        this.productGroupService = productGroupService;
        this.taxSystemService = taxSystemService;
        this.employeeService = employeeService;
        this.departmentService = departmentService;

        setCloseOnEsc(true);
        add(getHeader());

        nameField.setPlaceholder("Введите наименование группы");
        productGroupDtoBinder.forField(nameField)
                .withValidator(text -> text.length() >= 1, "Не менее 1 символа", ErrorLevel.ERROR)
                .bind(ProductGroupDto::getName, ProductGroupDto::setName);
        nameField.setValueChangeMode(ValueChangeMode.EAGER);
        add(getHorizontalLayout("Наименование группы", nameField));

        descriptionField.setPlaceholder("Введите описание группы");
        productGroupDtoBinder.forField(descriptionField)
                .withValidator(text -> text.length() >= 5, "Не менее 5 символов", ErrorLevel.ERROR)
                .bind(ProductGroupDto::getDescription, ProductGroupDto::setDescription);
        descriptionField.setValueChangeMode(ValueChangeMode.EAGER);
        add(getHorizontalLayout("Описание", descriptionField));

        productGroupDtoComboBox.setPlaceholder("Выберите родительскую группу продуктов");
        productGroupDtoComboBox.setItems(productGroupService.getAll());
        productGroupDtoComboBox.setItemLabelGenerator(ProductGroupDto::getName);
        add(getHorizontalLayout("Родительская группа", productGroupDtoComboBox));

        saleTax.setPlaceholder("Введите размер НДС");
        productGroupDtoBinder.forField(saleTax)
                .withValidator(text -> text.length() >= 2, "Введите число сооствестующее облагаемой ставке НДС", ErrorLevel.ERROR)
                .bind(ProductGroupDto::getSaleTax, ProductGroupDto::setSaleTax);
        saleTax.setValueChangeMode(ValueChangeMode.EAGER);
        add(getHorizontalLayout("НДС", saleTax));

        taxSystemDtoComboBox.setPlaceholder("Выберите систему налогообложения");
        taxSystemDtoComboBox.setItems(taxSystemService.getAll());
        taxSystemDtoComboBox.setItemLabelGenerator(TaxSystemDto::getName);
        add(getHorizontalLayout("Система налогообложения", taxSystemDtoComboBox));

        employeeDtoComboBox.setPlaceholder("Выберите сотрудника");
        employeeDtoComboBox.setItems(employeeService.getAll());
        employeeDtoComboBox.setItemLabelGenerator(EmployeeDto::getFirstName);
        add(getHorizontalLayout("Сотрудник", employeeDtoComboBox));

        departmentDtoComboBox.setPlaceholder("Выберите отдел");
        departmentDtoComboBox.setItems(departmentService.getAll());
        departmentDtoComboBox.setItemLabelGenerator(DepartmentDto::getName);
        add(getHorizontalLayout("Отдел", departmentDtoComboBox));

        footer.getStyle().set("padding-bottom", "30px");
        footer.getStyle().set("padding-top", "30px");
        footer.add(getFooter(getAddButton()));
        add(footer);
    }

    @Override
    public void open() {
        init();
        super.open();
    }

    public void open(ProductGroupDto productGroupDto) {
        init();
        productGroupDto = productGroupService.getById(productGroupDto.getId());
    }

    private void init() {
        nameField.clear();
        descriptionField.clear();
        productGroupDtoComboBox.setItems(productGroupService.getAll());
        saleTax.clear();
        taxSystemDtoComboBox.setItems(taxSystemService.getAll());
        employeeDtoComboBox.setItems(employeeService.getAll());
        departmentDtoComboBox.setItems(departmentService.getAll());
        productGroupDto = new ProductGroupDto();
    }

    private Component getHeader() {
        HorizontalLayout header = new HorizontalLayout();
        H2 title = new H2("Добавление группы");
        title.setHeight("2.2em");
        title.setWidth("345px");
        header.add(title);
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setPadding(false);
        header.add(title, verticalLayout);
        return header;
    }

    private Button getAddButton() {
        return new Button("Сохранить", buttonClickEvent -> {
            ProductGroupDto productGroupDto = new ProductGroupDto();
            if (!productGroupDtoBinder.validate().isOk()) {
                productGroupDtoBinder.validate().notifyBindingValidationStatusHandlers();
            } else {
                updateProductGroupDto(productGroupDto);
                productGroupService.create(productGroupDto);
                Notification.show(String.format("Группа %s добавлена", productGroupDto.getName()));
            }
        });
    }

    private void updateProductGroupDto(ProductGroupDto productGroupDto) {
        productGroupDto.setName(nameField.getValue());
        productGroupDto.setDescription(descriptionField.getValue());
        productGroupDto.setSaleTax(saleTax.getValue());
        productGroupDto.setTaxSystemId(taxSystemDtoComboBox.getValue().getId());
        productGroupDto.setEmployeeId(employeeDtoComboBox.getValue().getId());
        productGroupDto.setDepartmentId(departmentDtoComboBox.getValue().getId());

        try {
            productGroupDto.setParentId(productGroupDtoComboBox.getValue().getId());
        } catch (NullPointerException ignored) {}
    }

    private HorizontalLayout getFooter(Button addOrUpdateButton) {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        footer.add(addOrUpdateButton);
        footer.add(new Button("Закрыть", buttonClickEvent -> close()));
        footer.setMargin(false);
        footer.setWidth("100%");

        return footer;
    }

    private <T extends Component & HasSize> HorizontalLayout getHorizontalLayout(String labelText, T field) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label(labelText);
        field.setWidth("400px");
        label.setWidth("200px");
        horizontalLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, label);
        horizontalLayout.add(label, field);
        return horizontalLayout;
    }

}
