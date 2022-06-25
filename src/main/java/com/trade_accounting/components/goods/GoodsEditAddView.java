package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.trade_accounting.models.dto.warehouse.ProductDto;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.company.TaxSystemService;
import com.trade_accounting.services.interfaces.company.TypeOfPriceService;
import com.trade_accounting.services.interfaces.units.UnitService;
import com.trade_accounting.services.interfaces.util.ImageService;
import com.trade_accounting.services.interfaces.warehouse.AttributeOfCalculationObjectService;
import com.trade_accounting.services.interfaces.warehouse.ProductGroupService;
import com.trade_accounting.services.interfaces.warehouse.ProductPriceService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ErrorLevel;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import static com.trade_accounting.config.SecurityConstants.GOODS_GOODS__EDIT_VIEW;

@Slf4j
@SpringComponent
@UIScope
@Route(value = GOODS_GOODS__EDIT_VIEW, layout = AppView.class)
@PageTitle("Заказы поставщикам")
public class GoodsEditAddView extends VerticalLayout {

    private final ProductPriceService productPriceService;
    private final UnitService unitService;
    private final ContractorService contractorService;
    private final TaxSystemService taxSystemService;
    private final ProductService productService;
    private final ImageService imageService;
    private final ProductGroupService productGroupService;
    private final AttributeOfCalculationObjectService attributeOfCalculationObjectService;
    private final TypeOfPriceService typeOfPriceService;
    private final EmployeeService employeeService;

    private final Binder<ProductDto> productDtoBinder = new Binder<>(ProductDto.class);

    TextField productNameField = new TextField();


    public GoodsEditAddView(ProductPriceService productPriceService,
                            UnitService unitService,
                            ContractorService contractorService,
                            TaxSystemService taxSystemService,
                            ProductService productService,
                            ImageService imageService,
                            ProductGroupService productGroupService,
                            AttributeOfCalculationObjectService attributeOfCalculationObjectService,
                            TypeOfPriceService typeOfPriceService,
                            EmployeeService employeeService) {
        this.productPriceService = productPriceService;
        this.unitService = unitService;
        this.contractorService = contractorService;
        this.taxSystemService = taxSystemService;
        this.productService = productService;
        this.imageService = imageService;
        this.productGroupService = productGroupService;
        this.attributeOfCalculationObjectService = attributeOfCalculationObjectService;
        this.typeOfPriceService = typeOfPriceService;
        this.employeeService = employeeService;


    }

    private HorizontalLayout getHeader() {
        HorizontalLayout header = new HorizontalLayout();

        productNameField.setLabel("Наименование товара");
        productDtoBinder.forField(productNameField)
                .withValidator(text -> text.length() >= 3, "Не менее трёх символов", ErrorLevel.ERROR)
                .bind(ProductDto::getName, ProductDto::setName);

        header.add(getLeftButtons(), getRightButtons(), productNameField);
        return header;
    }

    private HorizontalLayout getLeftButtons() {
        HorizontalLayout leftButtons = new HorizontalLayout();

        Button saveButton = new Button("Сохранить");
        Button closeButton = new Button("Закрыть");

        saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);

        leftButtons.add(saveButton, closeButton);
        leftButtons.setJustifyContentMode(JustifyContentMode.START);
        return leftButtons;
    }

    private HorizontalLayout getRightButtons() {
        HorizontalLayout rightButtons = new HorizontalLayout();

        Label changeInfo = new Label("Изменно: ");

        MenuBar rightMenu = new MenuBar();

        MenuItem print = rightMenu.addItem("Печать");
        SubMenu printSubmenu = print.getSubMenu();
        printSubmenu.addItem("Ценник (70x49,5мм)");
        printSubmenu.addItem("Термоэтикетка (58х40мм)");
        printSubmenu.addItem("Настроить");

        MenuItem settings = rightMenu.addItem(new Icon(VaadinIcon.COG));

        MenuItem additionalActions = rightMenu.addItem(new Icon(VaadinIcon.MENU));
        SubMenu additionalActionsSubMenu = additionalActions.getSubMenu();
        additionalActionsSubMenu.addItem("Переместить в архив");
        additionalActionsSubMenu.addItem("Удалить");

        rightButtons.add(changeInfo, rightMenu);
        rightButtons.setJustifyContentMode(JustifyContentMode.END);
        return rightButtons;
    }


}
