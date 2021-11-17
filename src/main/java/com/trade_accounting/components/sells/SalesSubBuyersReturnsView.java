package com.trade_accounting.components.sells;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.BuyersReturnDto;
import com.trade_accounting.services.interfaces.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Route(value = "buyersReturns", layout = AppView.class)
@PageTitle("Возвраты покупателей")
@SpringComponent
@UIScope
public class SalesSubBuyersReturnsView extends VerticalLayout {
    private final TextField textField = new TextField();
    private final BuyersReturnService buyersReturnService;
    private final ContractorService contractorService;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final Notifications notifications;
    private final ReturnBuyersReturnModalView returnBuyersReturnModalView;
    private final List<BuyersReturnDto> data;
    private final Grid<BuyersReturnDto> grid = new Grid<>(BuyersReturnDto.class, false);
    private final GridPaginator<BuyersReturnDto> paginator;
    private final GridFilter<BuyersReturnDto> filter;
    private ContractService contractService;
    private final MenuBar selectXlsTemplateButton = new MenuBar();
    private final MenuItem print;
    private final SelectProductFromListWithQuantityModalWin selectProductFromListWithQuantityModalWin;
    private final String pathForSaveXlsTemplate = "src/main/resources/xls_templates/salesSubBuyersReturns_templates/";

    @Autowired
    public SalesSubBuyersReturnsView(BuyersReturnService buyersReturnService,
                                     ContractorService contractorService,
                                     CompanyService companyService, ReturnBuyersReturnModalView returnBuyersReturnModalView,
                                     WarehouseService warehouseService,
                                     Notifications notifications,
                                     SelectProductFromListWithQuantityModalWin selectProductFromListWithQuantityModalWin) {
        this.buyersReturnService = buyersReturnService;
        this.warehouseService = warehouseService;
        this.contractorService = contractorService;
        this.companyService = companyService;
        this.returnBuyersReturnModalView = returnBuyersReturnModalView;
        this.data = buyersReturnService.getAll();
        this.notifications = notifications;
        this.selectProductFromListWithQuantityModalWin = selectProductFromListWithQuantityModalWin;
        print = selectXlsTemplateButton.addItem("Печать");

        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(dto -> formatDate(dto.getDate())).setFlexGrow(7).setHeader("Время")
                .setKey("date").setId("Дата");
        grid.addColumn(dto -> warehouseService.getById(dto.getWarehouseId()).getName()).setFlexGrow(7).setFlexGrow(7).setHeader("На склад").setId("На склад");
        grid.addColumn(dto -> contractorService.getById(dto.getContractorId()).getName()).setFlexGrow(7).setHeader("Контрагент")
                .setKey("contractorId").setId("Контрагент");
        grid.addColumn(dto -> companyService.getById(dto.getCompanyId()).getName()).setFlexGrow(7).setHeader("Компания")
                .setKey("companyId").setId("Компания");
        grid.addColumn("sum").setFlexGrow(7).setHeader("Сумма").setId("Сумма");
        grid.addColumn("isSent").setFlexGrow(7).setHeader("Отправлено").setId("Отправлено");
        grid.addColumn("isPrint").setFlexGrow(7).setHeader("Напечатано").setId("Напечатано");
        this.filter = new GridFilter<>(grid);
        this.paginator = new GridPaginator<>(grid, data, 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(getToolbar(), filter);
        configureGrid();
        configureSelectXlsTemplateButton();
    }

    private void configureSelectXlsTemplateButton() {
        SubMenu printSubMenu = print.getSubMenu();
        printSubMenu.removeAll();
        templatesXlsMenuItems(printSubMenu);
        uploadXlsMenuItem(printSubMenu);
    }

    private void templatesXlsMenuItems(SubMenu subMenu) {
        getXlsFiles().forEach(x -> subMenu.addItem(getLinkToXlsTemplate(x)));
    }

    private List<File> getXlsFiles() {
        File dir = new File(pathForSaveXlsTemplate);
        return Arrays.stream(Objects.requireNonNull(dir.listFiles())).filter(File::isFile).filter(x -> x.getName()
                .contains(".xls")).collect(Collectors.toList());
    }

    private Anchor getLinkToXlsTemplate(File file) {
        String templateName = file.getName();
        List<String> sumList = new ArrayList<>();
        List<BuyersReturnDto> list1 = buyersReturnService.getAll();
        PrintSalesSubBuyersReturnsXls printSalesSubBuyersReturnsXls = new PrintSalesSubBuyersReturnsXls(file.getPath(), buyersReturnService.getAll(),
                contractorService, companyService, sumList );
        return new Anchor(new StreamResource(templateName,printSalesSubBuyersReturnsXls::createReport), templateName);
    }

    private void uploadXlsMenuItem(SubMenu subMenu) {
        MenuItem menuItem = subMenu.addItem("добавить шаблон");
        Dialog dialog = new Dialog();
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        configureUploadFinishedListener(upload, buffer, dialog);
        dialog.add(upload);
        menuItem.addClickListener(x -> dialog.open());
    }

    private void configureUploadFinishedListener(Upload upload, MemoryBuffer buffer, Dialog dialog) {
        upload.addFinishedListener(event -> {
            if (getXlsFiles().stream().map(File::getName).anyMatch(x -> x.equals(event.getFileName()))) {
                getErrorNotification("Файл с таким именем уже существует");
            } else {
                File exelTemplate = new File(pathForSaveXlsTemplate + event.getFileName());
                try (FileOutputStream fos = new FileOutputStream(exelTemplate)) {
                    fos.write(buffer.getInputStream().readAllBytes());
                    configureSelectXlsTemplateButton();
                    getInfoNotification("Файл успешно загружен");
                    log.info("xls шаблон успешно загружен");
                } catch (IOException e) {
                    getErrorNotification("При загрузке шаблона произошла ошибка");
                    log.error("при загрузке xls шаблона произошла ошибка");
                }
                dialog.close();
            }
        });
    }

    private void getErrorNotification(String message) {
        Div content = new Div();
        content.addClassName("my-style");
        content.setText(message);
        Notification notification = new Notification(content);
        notification.setDuration(5000);
        String styles = ".my-style { color: red; }";
        StreamRegistration resource = UI.getCurrent().getSession()
                .getResourceRegistry()
                .registerResource(new StreamResource("styles.css", () ->
                        new ByteArrayInputStream(styles.getBytes(StandardCharsets.UTF_8))));
        UI.getCurrent().getPage().addStyleSheet(
                "base://" + resource.getResourceUri().toString());
        notification.open();
    }

    private void getInfoNotification(String message) {
        Notification notification = new Notification(message, 5000);
        notification.open();
    }

    private void configureGrid() {
        grid.removeAllColumns();
        grid.setItems(data);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(dto -> formatDate(dto.getDate())).setFlexGrow(7).setHeader("Время")
                .setKey("date").setId("Дата");
        grid.addColumn(dto -> warehouseService.getById(dto.getWarehouseId()).getName()).setFlexGrow(7).setFlexGrow(7).setHeader("На склад").setId("На склад");
        grid.addColumn(dto -> contractorService.getById(dto.getContractorId()).getName()).setFlexGrow(7).setHeader("Контрагент")
                .setKey("contractorId").setId("Контрагент");
        grid.addColumn(dto -> companyService.getById(dto.getCompanyId()).getName()).setFlexGrow(7).setHeader("Компания")
                .setKey("companyId").setId("Компания");
        grid.addColumn("sum").setFlexGrow(7).setHeader("Сумма").setId("Сумма");
        grid.addColumn("isSent").setFlexGrow(7).setHeader("Отправлено").setId("Отправлено");
        grid.addColumn("isPrint").setFlexGrow(7).setHeader("Напечатано").setId("Напечатано");
        grid.addColumn("comment").setFlexGrow(7).setHeader("Комментарий").setId("Комментарий");
        grid.addItemDoubleClickListener(event -> {
            BuyersReturnDto buyersReturnDto = event.getItem();
            ReturnBuyersReturnModalView view = new ReturnBuyersReturnModalView(
                    contractorService,
                    warehouseService,
                    companyService,
                    selectProductFromListWithQuantityModalWin);
            view.setReturnEdit(buyersReturnDto);
            view.open();

        });
        grid.setHeight("66vh");
        grid.setMaxWidth("2500px");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        add(grid, paginator);

    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), title(), getButtonRefresh(), buttonUnit(), getButtonFilter(), selectXlsTemplateButton, textField(),
                numberField(), getSelect(), getStatus(), buttonSettings());
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolbar;
    }

    private H2 title() {
        H2 title = new H2("Возвраты покупателей");
        title.setHeight("2.2em");
        return title;
    }

    public Button buttonUnit() {
        Button buttonUnit = new Button("Возврат покупателя", new Icon(VaadinIcon.PLUS_CIRCLE));
        buttonUnit.addClickListener(event -> returnBuyersReturnModalView.open());
        return buttonUnit;
    }

    private Button getButtonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
    }

    private Button getButtonRefresh() {
        final Button buttonRefresh;
        Icon circle = new Icon(VaadinIcon.REFRESH);
        buttonRefresh = new Button();
        buttonRefresh.setIcon(circle);
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button getButtonQuestion() {
        final Button buttonQuestion = new Button();
        Icon question = new Icon(VaadinIcon.QUESTION_CIRCLE_O);
        buttonQuestion.setIcon(question);
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }



    private static String formatDate(String date) {
        return LocalDateTime.parse(date)
                .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private TextField textField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> updateList(textField.getValue()));
        return textField;
    }

    private void updateList(String text) {
        grid.setItems(buyersReturnService.findBySearch(text));
    }

    private Select<String> getSelect() {
        Select<String> select = new Select<>();
        select.setItems("Изменить", "Удалить", "Массовое редактирование", "Провести", "Снять проведение");
        select.setValue("Изменить");
        select.setWidth("130px");
        select.addValueChangeListener(event -> {
            if (select.getValue().equals("Удалить")) {
                deleteSelectedBuyersReturn();
                grid.deselectAll();
                select.setValue("Изменить");
                paginator.setData(getData());
            }
        });
        return select;
    }

    private Select<String> getStatus() {
        Select<String> status = new Select<>();
        status.setItems("Статус", "Настроить");
        status.setValue("Статус");
        status.setWidth("130px");
        return status;
    }

    private void deleteSelectedBuyersReturn() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (BuyersReturnDto buyersReturnDto : grid.getSelectedItems()) {
                buyersReturnService.deleteById(buyersReturnDto.getId());
                notifications.infoNotification("Выбранные заказы успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные заказы");
        }
    }

    private List<BuyersReturnDto> getData() {
        return buyersReturnService.getAll();
    }
}
