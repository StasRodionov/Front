package com.trade_accounting.components.production;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.OrdersOfProductionDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.OrdersOfProductionService;
import com.trade_accounting.services.interfaces.TechnicalCardService;
import com.vaadin.flow.component.Component;
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
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@SpringComponent
@UIScope
@PageTitle("Заказы на производство")
@Route(value = "ordersOfProductionViewTab", layout = AppView.class)
public class OrdersOfProductionViewTab extends VerticalLayout implements AfterNavigationObserver {


        private final TextField textField = new TextField();
        private final MenuBar selectXlsTemplateButton = new MenuBar();

    private final GridPaginator<OrdersOfProductionDto> paginator;
    private final Grid<OrdersOfProductionDto> grid = new Grid<>(OrdersOfProductionDto.class, false);
    private final OrdersOfProductionService ordersOfProductionService;
    private final CompanyService companyService;
    private final TechnicalCardService technicalCardService;
    private final List<OrdersOfProductionDto> data;
    private GridFilter<OrdersOfProductionDto> filter;
    private final Notifications notifications;
    private final OrdersOfProductionModalWindow modalWindow;
    private final MenuItem print;
    private final String pathForSaveXlsTemplate = "src/main/resources/xls_templates/ordersOfProduction_templates/";

    OrdersOfProductionViewTab(OrdersOfProductionService ordersOfProductionService, CompanyService companyService, TechnicalCardService technicalCardService, Notifications notifications, OrdersOfProductionModalWindow modalWindow) {
            this.ordersOfProductionService = ordersOfProductionService;
            this.companyService = companyService;
        this.technicalCardService = technicalCardService;
        this.notifications = notifications;
        this.modalWindow = modalWindow;
        setSizeFull();
        print = selectXlsTemplateButton.addItem("Печать");
        this.data = getData();
        paginator = new GridPaginator<>(grid, this.ordersOfProductionService.getAll(), 100);
        configureGrid();
        this.filter = new GridFilter<>(grid);
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(getTollBar(), filter, grid, paginator);
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
        PrintOrdersOfProductionXls printOrdersOfProductionXls = new PrintOrdersOfProductionXls(file.getPath(), ordersOfProductionService.getAll(),
                                                                                               companyService, technicalCardService);
        return new Anchor(new StreamResource(templateName, printOrdersOfProductionXls::createReport), templateName);
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


    private void configureGrid () {
        grid.setItems(data);
        grid.addColumn("id").setHeader("№").setId("№");
        grid.addColumn(OrdersOfProductionDto::getDate).setKey("date").setHeader("время").setSortable(true).setId("Дата");
        grid.addColumn("comment").setHeader("Комментарий").setId("Комментарий");
        grid.addColumn(e -> companyService.getById(e.getCompanyId()).getName()).setHeader("организация").setId("организация");
        grid.addColumn(t -> technicalCardService.getById(t.getTechnicalCardId()).getName()).setHeader("Технологическая карта").setId("Технологическая карта");
        grid.addColumn("volume").setHeader("Объем производства").setId("Объем производства");
        grid.addColumn("produce").setHeader("Произведено").setId("Произведено");
        grid.addColumn(OrdersOfProductionDto::getPlannedProductionDate)
                .setHeader("План. дата производства").setSortable(true).setId("План. дата производства");
        grid.addColumn(new ComponentRenderer<>(this::getIsSentIcon)).setHeader("Отправлено")
                .setId("Отправлено");
        grid.addColumn(new ComponentRenderer<>(this::getIsPrintIcon)).setHeader("Напечатано")
                .setId("Напечатано");

        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addItemDoubleClickListener(e -> {
            OrdersOfProductionDto dto = e.getItem();
            OrdersOfProductionModalWindow modalWindow = new OrdersOfProductionModalWindow(
            technicalCardService,
            companyService,
            ordersOfProductionService,
            notifications
            );
           modalWindow.setOrdersOfProductionEdit(dto);
           modalWindow.open();
        });


    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToDatePicker("date");
        filter.onSearchClick(e ->
                paginator.setData(ordersOfProductionService.searchOrdersOfProduction(filter.getFilterData())));
        filter.onClearClick(e ->
                paginator.setData(ordersOfProductionService.getAll()));
    }


    private Component getIsSentIcon(OrdersOfProductionDto ordersOfProductionDto) {
        if (ordersOfProductionDto.getIsSent()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private Component getIsPrintIcon(OrdersOfProductionDto ordersOfProductionDto) {
        if (ordersOfProductionDto.getIsPrint()) {
            Icon icon = new Icon(VaadinIcon.CHECK);
            icon.setColor("green");
            return icon;
        } else {
            return new Span("");
        }
    }

    private List<OrdersOfProductionDto> getData() {
        return ordersOfProductionService.getAll();
    }

        private HorizontalLayout getTollBar () {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(buttonQuestion(), getTextOrder(), buttonRefresh(), buttonUnit(),
                buttonFilter(), text(), numberField(), valueSelect(), valueStatus(),
                valuePrint(), buttonSettings(), selectXlsTemplateButton);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return horizontalLayout;
    }

        private Button buttonQuestion () {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

        private Button buttonRefresh () {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(ev -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

        private void updateList () {
            grid.setItems(ordersOfProductionService.getAll());
    }

        private Button buttonUnit () {
        Button buttonUnit = new Button("Заказ", new Icon(VaadinIcon.PLUS_CIRCLE));
            buttonUnit.addClickListener(e -> modalWindow.open());
            updateList();
        return buttonUnit;
    }

        private Button buttonFilter () {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }


        private TextField text () {
        textField.setWidth("300px");
        textField.setPlaceholder("Номер или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setClearButtonVisible(true);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
            textField.addValueChangeListener(e -> updateListTextField());
            setSizeFull();
        return textField;
    }

    public void updateListTextField() {
        if (!(textField.getValue().equals(""))) {
            grid.setItems(ordersOfProductionService.search(textField.getValue()));
        } else {
            grid.setItems(ordersOfProductionService.search("null"));
        }
    }

        private NumberField numberField () {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

        private Select<String> valueSelect () {
            Select<String> valueSelect = new Select<>();
            List<String> list = new ArrayList<>();
            list.add("Изменить");
            list.add("Удалить");
            valueSelect.setItems(list);
            valueSelect.setValue("Изменить");
            valueSelect.setWidth("120px");
            valueSelect.addValueChangeListener(event -> {
                if (valueSelect.getValue().equals("Удалить")) {
                    deleteSelectedOrdersOfProduction();
                    grid.deselectAll();
                    valueSelect.setValue("Изменить");
                    paginator.setData(getData());
                }
            });
            return valueSelect;
    }

    private void deleteSelectedOrdersOfProduction() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (OrdersOfProductionDto ordersOfProductionDto : grid.getSelectedItems()) {
                ordersOfProductionService.deleteById(ordersOfProductionDto.getId());
                notifications.infoNotification("Выбранные заказы успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные заказы");
        }
    }

        private Button buttonSettings () {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

        private Select<String> valueStatus () {
        Select<String> status = new Select<>();
        status.setItems("Статус");
        status.setValue("Статус");
        status.setWidth("110px");
        return status;
    }

        private Select<String> valuePrint () {
        Select<String> print = new Select<>();
        print.setItems("Печать", "Добавить шаблон");
        print.setValue("Печать");
        print.setWidth("110px");
        return print;
    }

        private H2 getTextOrder () {
        final H2 textOrder = new H2("Заказы на производство");
        textOrder.setHeight("2.2em");
        return textOrder;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        updateList();
    }
}
