package com.trade_accounting.components.goods;

import com.trade_accounting.components.goods.print.PrintPriceListProductTagsXls;
import com.trade_accounting.components.goods.print.PrintPriceListProductXls;
import com.trade_accounting.components.goods.print.PrintPriceListXls;
import com.trade_accounting.models.dto.company.PriceListDto;
import com.trade_accounting.models.dto.company.PriceListProductDto;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.PriceListProductPercentsService;
import com.trade_accounting.services.interfaces.company.PriceListProductService;
import com.trade_accounting.services.interfaces.company.PriceListService;
import com.trade_accounting.services.interfaces.units.CountryService;
import com.trade_accounting.services.interfaces.units.UnitService;
import com.trade_accounting.services.interfaces.warehouse.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@SpringComponent
@UIScope
public class PrintPriceListProductModalView extends Dialog {
    private final MenuItem print;
    private final MenuBar selectXlsTemplateButton = new MenuBar();
    private final String pathForSaveXlsTemplate = "src/main/resources/xls_templates/priceList_templates/";
    private final PriceListService priceListService;
    private final PriceListProductService priceListProductService;
    private final PriceListProductPercentsService priceListProductPercentsService;
    private final ProductService productService;
    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final UnitService unitService;
    private final CountryService countryService;

    private PriceListDto priceListDto = new PriceListDto();
    private String addTemplate = "";

    public PrintPriceListProductModalView(PriceListService priceListService,
                                          PriceListProductService priceListProductService,
                                          PriceListProductPercentsService priceListProductPercentsService,
                                          ProductService productService,
                                          CompanyService companyService,
                                          EmployeeService employeeService,
                                          UnitService unitService,
                                          CountryService countryService) {
        this.priceListService = priceListService;
        this.priceListProductService = priceListProductService;
        this.priceListProductPercentsService = priceListProductPercentsService;
        this.productService = productService;
        this.companyService = companyService;
        this.employeeService = employeeService;
        this.unitService = unitService;
        this.countryService = countryService;

        print = selectXlsTemplateButton.addItem("Выберите шаблон");
        add(header(), selectXlsTemplateButton);
        selectXlsTemplateButton.addAttachListener((event -> configureSelectXlsTemplateButton()));
        add(layout());
        setCloseOnOutsideClick(true);
        setCloseOnEsc(true);
    }

    public void setPriceListDto(PriceListDto priceListDto) {
        this.priceListDto = priceListDto;
    }

    public void setPriceListTemplate(String addTemplate) {
        this.addTemplate = addTemplate;
    }

    private VerticalLayout header() {
        VerticalLayout header = new VerticalLayout();
        H2 title = new H2("Создание печатной формы");
        H6 question = new H6("Создать печатную форму по шаблону?");
        header.add(title, question);
        return header;
    }

    private HorizontalLayout layout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.add(getCloseButton());
        return layout;
    }

    private Button getCloseButton() {
        return new Button("Закрыть", event -> {
            close();
        });
    }

    private SubMenu configureSelectXlsTemplateButton() {
        SubMenu printSubMenu = print.getSubMenu();
        printSubMenu.removeAll();
        templatesXlsMenuItems(printSubMenu);
        return printSubMenu;
    }

    private void templatesXlsMenuItems(SubMenu subMenu) {

        getXlsFiles().forEach(x -> subMenu.addItem(getLinkToXlsTemplate(x))
                .addClickListener(event -> {
                    priceListDto.setIsPrint(true);
                    priceListService.update(priceListDto);
                }));
        getXlsFiles().forEach(x -> subMenu.addItem(getLinkToPdfTemplate(x))
                .addClickListener(event -> {
                    priceListDto.setIsPrint(true);
                    priceListService.update(priceListDto);
                }));
        getXlsFiles().forEach(x -> subMenu.addItem(getLinkToOdsTemplate(x))
                .addClickListener(event -> {
                    priceListDto.setIsPrint(true);
                    priceListService.update(priceListDto);
                }));
    }

    private List<File> getXlsFiles() {
        File dir = new File(pathForSaveXlsTemplate + addTemplate);
        return Arrays.stream(Objects.requireNonNull(dir.listFiles())).filter(File::isFile).filter(x -> x.getName()
                .contains(".xls")).collect(Collectors.toList());
    }

    private Anchor getLinkToXlsTemplate(File file) {
        String templateName = file.getName();

        if(addTemplate.equals("priceListProduct/")) {
            List<PriceListProductDto> priceLists = priceListDto.getProductsIds()
                    .stream().map(priceListProductService::getById).collect(Collectors.toList());
            PrintPriceListProductXls printPriceListXls = new PrintPriceListProductXls(file.getPath(),
                    priceLists,
                    priceListProductPercentsService,
                    productService,
                    priceListDto);
            return new Anchor(new StreamResource(templateName, printPriceListXls::createReport), "Печать в формате Excel: " + templateName);
        } else if (addTemplate.equals("priceList/")){
            List<PriceListDto> priceLists1 = priceListService.getAll();
            PrintPriceListXls printPriceListXls = new PrintPriceListXls(file.getPath(),
                    priceLists1,
                    companyService
            );
            return new Anchor(new StreamResource(templateName, printPriceListXls::createReport), "Печать в формате Excel: " + templateName);
        } else {
            List<PriceListProductDto> priceLists = priceListDto.getProductsIds()
                    .stream().map(priceListProductService::getById).collect(Collectors.toList());
            PrintPriceListProductTagsXls printPriceListXls = new PrintPriceListProductTagsXls(file.getPath(),
                    priceLists,
                    priceListProductPercentsService,
                    productService,
                    companyService,
                    employeeService,
                    unitService,
                    countryService,
                    priceListDto);
            return new Anchor(new StreamResource(templateName, printPriceListXls::createReport), "Печать в формате Excel: " + templateName);
        }
    }

    private Anchor getLinkToPdfTemplate(File file) {
        String templateName = file.getName().substring(0, file.getName().lastIndexOf(".")) + ".pdf";

        if(addTemplate.equals("priceListProduct/")) {
            List<PriceListProductDto> priceLists = priceListDto.getProductsIds()
                    .stream().map(priceListProductService::getById).collect(Collectors.toList());
            PrintPriceListProductXls printPriceListXls = new PrintPriceListProductXls(file.getPath(),
                priceLists,
                priceListProductPercentsService,
                productService,
                priceListDto);
        return new Anchor(new StreamResource(templateName, printPriceListXls::createReportPDF), "Печать в формате PDF: " + templateName);
        } else if (addTemplate.equals("priceList/")){
            List<PriceListDto> priceLists1 = priceListService.getAll();
            PrintPriceListXls printPriceListXls = new PrintPriceListXls(file.getPath(),
                    priceLists1,
                    companyService
            );
            return new Anchor(new StreamResource(templateName, printPriceListXls::createReportPDF), "Печать в формате PDF: " + templateName);
        } else {
            List<PriceListProductDto> priceLists = priceListDto.getProductsIds()
                    .stream().map(priceListProductService::getById).collect(Collectors.toList());
            PrintPriceListProductTagsXls printPriceListXls = new PrintPriceListProductTagsXls(file.getPath(),
                    priceLists,
                    priceListProductPercentsService,
                    productService,
                    companyService,
                    employeeService,
                    unitService,
                    countryService,
                    priceListDto);
            return new Anchor(new StreamResource(templateName, printPriceListXls::createReportPDF), "Печать в формате PDF: " + templateName);
        }
    }

    private Anchor getLinkToOdsTemplate(File file) {
        String templateName = file.getName().substring(0, file.getName().lastIndexOf(".")) + ".ods";

        if(addTemplate.equals("priceListProduct/")) {
            List<PriceListProductDto> priceLists = priceListDto.getProductsIds()
                    .stream().map(priceListProductService::getById).collect(Collectors.toList());
            PrintPriceListProductXls printPriceListXls = new PrintPriceListProductXls(file.getPath(),
                priceLists,
                priceListProductPercentsService,
                productService,
                priceListDto);
        return new Anchor(new StreamResource(templateName, printPriceListXls::createReportODS), "Печать в формате Office Calc: " + templateName);
        } else if (addTemplate.equals("priceList/")){
            List<PriceListDto> priceLists1 = priceListService.getAll();
            PrintPriceListXls printPriceListXls = new PrintPriceListXls(file.getPath(),
                    priceLists1,
                    companyService
            );
            return new Anchor(new StreamResource(templateName, printPriceListXls::createReportODS), "Печать в формате Office Calc: " + templateName);
        } else {
            List<PriceListProductDto> priceLists = priceListDto.getProductsIds()
                    .stream().map(priceListProductService::getById).collect(Collectors.toList());
            PrintPriceListProductTagsXls printPriceListXls = new PrintPriceListProductTagsXls(file.getPath(),
                    priceLists,
                    priceListProductPercentsService,
                    productService,
                    companyService,
                    employeeService,
                    unitService,
                    countryService,
                    priceListDto);
            return new Anchor(new StreamResource(templateName, printPriceListXls::createReportODS), "Печать в формате Office Calc: " + templateName);
        }
    }
}
