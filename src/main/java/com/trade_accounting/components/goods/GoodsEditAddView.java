package com.trade_accounting.components.goods;

import com.trade_accounting.components.AppView;
import com.trade_accounting.models.dto.company.ContractorDto;
import com.trade_accounting.models.dto.company.TaxSystemDto;
import com.trade_accounting.models.dto.units.UnitDto;
import com.trade_accounting.models.dto.util.ImageDto;
import com.trade_accounting.models.dto.warehouse.AttributeOfCalculationObjectDto;
import com.trade_accounting.models.dto.warehouse.ProductDto;
import com.trade_accounting.models.dto.warehouse.ProductGroupDto;
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
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ErrorLevel;
import com.vaadin.flow.data.validator.BigDecimalRangeValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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

    private List<ImageDto> imageDtoList;
    private ProductDto productDto;

    private final Binder<ProductDto> productDtoBinder = new Binder<>(ProductDto.class);

    private final HorizontalLayout imageHorizontalLayout = new HorizontalLayout();
    private final VerticalLayout tabsContent = new VerticalLayout();
    private final TextField productNameField = new TextField();
    private final TextArea descriptionField = new TextArea();
    private final TextField countryOriginField = new TextField();
    private final TextField saleTax = new TextField();
    private final ComboBox<ProductGroupDto> productGroupDtoComboBox = new ComboBox<>();
    private final ComboBox<UnitDto> unitDtoComboBox = new ComboBox<>();
    private final ComboBox<ContractorDto> contractorDtoComboBox = new ComboBox<>();
    private final ComboBox<TaxSystemDto> taxSystemDtoComboBox = new ComboBox<>();
    private final ComboBox<AttributeOfCalculationObjectDto> attributeOfCalculationObjectComboBox = new ComboBox<>();
    private final BigDecimalField itemNumber = new BigDecimalField();
    private final BigDecimalField minimumBalance = new BigDecimalField();
    private final BigDecimalField weightNumberField = new BigDecimalField();
    private final BigDecimalField volumeNumberField = new BigDecimalField();
    private final BigDecimalField purchasePriceNumberField = new BigDecimalField();

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


        productNameField.setLabel("Наименование товара");
        productNameField.setWidthFull();
        productDtoBinder.forField(productNameField)
                .withValidator(text -> text.length() >= 3, "Не менее трёх символов", ErrorLevel.ERROR)
                .bind(ProductDto::getName, ProductDto::setName);

        add(getHeader(), productNameField, getMainLayout());
    }

    private HorizontalLayout getHeader() {
        HorizontalLayout header = new HorizontalLayout();

        header.add(getLeftButtons(), getRightButtons());
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        return header;
    }

    private HorizontalLayout getLeftButtons() {
        HorizontalLayout leftButtons = new HorizontalLayout();

        Button saveButton = new Button("Сохранить");
        Button closeButton = new Button("Закрыть");

        saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);

        leftButtons.add(saveButton, closeButton);
        leftButtons.setWidth("50%");
        leftButtons.setJustifyContentMode(JustifyContentMode.START);
        return leftButtons;
    }

    private HorizontalLayout getRightButtons() {
        HorizontalLayout rightButtons = new HorizontalLayout();

        Label changeInfo = new Label("Изменения: ");

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
        rightButtons.setWidth("50%");
        rightButtons.setJustifyContentMode(JustifyContentMode.END);
        return rightButtons;
    }

    private HorizontalLayout getMainLayout() {
        HorizontalLayout mainLayout = new HorizontalLayout();


        mainLayout.add(getLeftColumnLayout(), getRightColumnLayout());
        return mainLayout;
    }

    private VerticalLayout getLeftColumnLayout() {
        VerticalLayout leftColumnLayout = new VerticalLayout();

        leftColumnLayout.add(getImagesLayout(),
                getCommonDataLayout(),
                getRemainingLayout(),
                getSpecialAccountingLayout(),
                getBarcodesLayout(),
                getReceiptLayout(),
                getAccessLayout());
        return leftColumnLayout;
    }

    private VerticalLayout getRightColumnLayout() {
        VerticalLayout rightColumnLayout = new VerticalLayout();


        return rightColumnLayout;
    }

    private Tabs getTabs() {
        Tab prices = new Tab("Цены");
        Tab modifications = new Tab("Модификации");
        Tab packing = new Tab("Упаковка");
        Tab remains = new Tab("Остатки");
        Tab history = new Tab("История");
        Tab files = new Tab("Файлы");

        Tabs tabs = new Tabs(prices, modifications, packing, remains, history, files);
        tabs.addSelectedChangeListener(event ->
                setTabsContent(event.getSelectedTab()));
        return tabs;
    }

    private void setTabsContent(Tab selectedTab) {
        tabsContent.removeAll();

    }

    private VerticalLayout getPricesTabContent() {
        VerticalLayout content = new VerticalLayout();

        return content;
    }

    private VerticalLayout getModificationsTabContent() {
        VerticalLayout content = new VerticalLayout();

        return content;
    }

    private VerticalLayout getPackingTabContent() {
        VerticalLayout content = new VerticalLayout();

        return content;
    }

    private VerticalLayout getRemainsTabContent() {
        VerticalLayout content = new VerticalLayout();

        return content;
    }

    private VerticalLayout getHistoryTabContent() {
        VerticalLayout content = new VerticalLayout();

        return content;
    }

    private VerticalLayout getFilesTabContent() {
        VerticalLayout content = new VerticalLayout();

        return content;
    }


    private Details getImagesLayout() {
        VerticalLayout content = new VerticalLayout();

        content.add(getImageButton(), imageHorizontalLayout);
        Details imagesLayout = new Details("Изображения", content);
        imagesLayout.setOpened(true);

        return imagesLayout;
    }

    private Details getCommonDataLayout() {
        VerticalLayout content = new VerticalLayout();

        descriptionField.setPlaceholder("Введите описание");
        productDtoBinder.forField(descriptionField)
                .withValidator(text -> text.length() >= 3, "Не менее трёх символов", ErrorLevel.ERROR)
                .bind(ProductDto::getName, ProductDto::setName);
        descriptionField.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(getHorizontalLayout("Описание", descriptionField));

        productGroupDtoComboBox.setPlaceholder("Выберите группу продуктов");
        productGroupDtoComboBox.setItems(productGroupService.getAll());
        productGroupDtoComboBox.setItemLabelGenerator(ProductGroupDto::getName);
        content.add(getHorizontalLayout("Группа продуктов", productGroupDtoComboBox));

        countryOriginField.setPlaceholder("Введите страну происхождения");
        productDtoBinder.forField(countryOriginField)
                .withValidator(text-> text.length()>=3,"Не менее 3 Символов", ErrorLevel.ERROR)
                .bind(ProductDto::getCountryOrigin, ProductDto::setCountryOrigin);
        countryOriginField.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(getHorizontalLayout("Страна происхождения", countryOriginField));

        contractorDtoComboBox.setPlaceholder("Выберите поставщика");
        contractorDtoComboBox.setItems(contractorService.getAll());
        contractorDtoComboBox.setItemLabelGenerator(ContractorDto::getName);
        content.add(getHorizontalLayout("Поставщик", contractorDtoComboBox));

        content.add(new Hr());

        itemNumber.setPlaceholder("Введите Артикул");
        productDtoBinder.forField(itemNumber)
                .withValidator(Objects::nonNull, "Введите артикул")
                .withValidator(new BigDecimalRangeValidator("Не верное значение", BigDecimal.ZERO, new BigDecimal("99999999999")))
                .bind(productDto -> new BigDecimal(productDto.getItemNumber()), (productDto1, itemNumber1) -> productDto1.setItemNumber(itemNumber1.intValue()));
        itemNumber.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(getHorizontalLayout("Артикул", itemNumber));

        content.add(new Hr());

        unitDtoComboBox.setPlaceholder("Выберите единицу измерения");
        unitDtoComboBox.setItems(unitService.getAll());
        unitDtoComboBox.setItemLabelGenerator(UnitDto::getFullName);
        content.add(getHorizontalLayout("Единицы измерения", unitDtoComboBox));

        weightNumberField.setPlaceholder("Введите вес");
        productDtoBinder.forField(weightNumberField)
                .withValidator(Objects::nonNull, "Введите вес")
                .withValidator(new BigDecimalRangeValidator("Не верное значение", BigDecimal.ZERO, new BigDecimal("99999999999999999999")))//                .withValidator(value -> value < 0, "Не может быть меньше 0")
                .bind(ProductDto::getWeight, ProductDto::setWeight);
        weightNumberField.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(getHorizontalLayout("Вес", weightNumberField));

        volumeNumberField.setPlaceholder("Введите объём");
        productDtoBinder.forField(volumeNumberField)
                .withValidator(Objects::nonNull, "Введите объём")
                .withValidator(new BigDecimalRangeValidator("Не верное значение", BigDecimal.ZERO, new BigDecimal("99999999999999999999")))//                .withValidator(value -> value < 0, "Не может быть меньше 0")
                .bind(ProductDto::getVolume, ProductDto::setVolume);
        volumeNumberField.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(getHorizontalLayout("Объем", volumeNumberField));

        content.add(new Hr());

        saleTax.setPlaceholder("Введите размер НДС");
        productDtoBinder.forField(saleTax)
                .withValidator(text -> text.length() >= 2, "Введите число сооствестующее облагаемой ставке НДС", ErrorLevel.ERROR)
                .bind(ProductDto::getName, ProductDto::setName);
        saleTax.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(getHorizontalLayout("НДС", saleTax));



        Details commonDataLayout = new Details("Общие данные", content);
        commonDataLayout.setOpened(true);
        return commonDataLayout;
    }

    private Details getRemainingLayout() {
        VerticalLayout content = new VerticalLayout();
//TODO
//реализовать добавление складов и остатков
        minimumBalance.setPlaceholder("Введите минимальный баланс");
        productDtoBinder.forField(minimumBalance)
                .withValidator(Objects::nonNull, "Введите минимальный баланс")
                .withValidator(new BigDecimalRangeValidator("Не верное значение", BigDecimal.ZERO, new BigDecimal("99999999999")))
                .bind(productDto -> new BigDecimal(productDto.getMinimumBalance()), (productDto1, minimumBalance1) -> productDto1.setMinimumBalance(minimumBalance1.intValue()));
        minimumBalance.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(getHorizontalLayout("Минимальный баланс", minimumBalance));

        Details remainingLayout = new Details("Неснижаемый остаток", content);
        remainingLayout.setOpened(true);
        return remainingLayout;
    }

    private Details getSpecialAccountingLayout() {
        VerticalLayout content = new VerticalLayout();


        Details specialAccountingLayout = new Details("Особенности учета", content);
        specialAccountingLayout.setOpened(true);
        return specialAccountingLayout;
    }

    private Details getBarcodesLayout() {
        VerticalLayout content = new VerticalLayout();


        Details barcodesLayout = new Details("Штрихкоды товара", content);
        barcodesLayout.setOpened(true);
        return barcodesLayout;
    }

    private Details getReceiptLayout() {
        VerticalLayout content = new VerticalLayout();


        Details receiptLayout = new Details("Кассовый чек", content);
        receiptLayout.setOpened(true);
        return receiptLayout;
    }

    private Details getAccessLayout() {
        VerticalLayout content = new VerticalLayout();


        Details accessLayout = new Details("Доступ", content);
        accessLayout.setOpened(true);
        return accessLayout;
    }


    private Component getImageButton() {
        Button imageButton = new Button("Добавить фото");
        Dialog dialog = new Dialog();
        MultiFileMemoryBuffer memoryBuffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(memoryBuffer);

        upload.addFinishedListener(event -> {
            try {
                ImageDto imageDto = new ImageDto();
                final String fileName = event.getFileName();
                String fileExtension = fileName.substring(fileName.indexOf("."));
                imageDto.setFileExtension(fileExtension);
                imageDto.setContent(memoryBuffer.getInputStream(event.getFileName()).readAllBytes());
                imageDtoList.add(imageDto);
                StreamResource resource = new StreamResource("image", () -> new ByteArrayInputStream(imageDto.getContent()));
                Image image = new Image(resource, "image");
                image.setHeight("100px");
                imageHorizontalLayout.add(image);
            } catch (IOException e) {
                e.printStackTrace();
            }

            dialog.close();
        });

        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("500px");
        layout.getStyle().set("overflow", "auto");
        dialog.add(upload);
        imageButton.addClickListener(x -> dialog.open());
        return imageButton;
    }

    private Button getRemoveImageButton(ProductDto productDto, Image image, ImageDto imageDto) {
        Button deleteButton = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE_O), buttonClickEvent -> {
            imageHorizontalLayout.remove(image);
            productDto.getImageDtos().remove(imageDto);
        });

        deleteButton.setMinWidth("10px");
        return deleteButton;
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
