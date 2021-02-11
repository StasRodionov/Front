package com.trade_accounting.components.contractors;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.authentication.LoginView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.services.interfaces.ContractorGroupService;
import com.trade_accounting.services.interfaces.ContractorService;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Route(value = "contractorsTabView", layout = AppView.class)
@PageTitle("Контрагенты")
public class ContractorsTabView extends VerticalLayout {

    private final ContractorService contractorService;

    private final ContractorGroupService contractorGroupService;

    private Grid<ContractorDto> grid;

    private MenuBar selectXlsTemplateButton = new MenuBar();

    private MenuItem print;


    private final String pathForSaveXlsTemplate = "src/main/resources/xls_templates/contractors_templates/";

    public ContractorsTabView(ContractorService contractorService, ContractorGroupService contractorGroupService) {
        this.contractorService = contractorService;
        this.contractorGroupService = contractorGroupService;

        print = selectXlsTemplateButton.addItem("печать");
        configureSelectXlsTemplateButton();

        try {
            updateList();
        } catch (NullPointerException e) {
            WrappedSession wrappedSession = VaadinSession.getCurrent().getSession();
            wrappedSession.setAttribute("redirectDestination", "/contractorsTabView");
            UI.getCurrent().navigate(LoginView.class);
        }
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addClickListener(ev -> updateList());
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonUnit() {
        Button buttonUnit = new Button("Контрагент", new Icon(VaadinIcon.PLUS_CIRCLE));
        ContractorModalWindow addContractorModalWindow =
                new ContractorModalWindow(new ContractorDto(), contractorService, contractorGroupService);
        addContractorModalWindow.addDetachListener(event -> updateList());
        buttonUnit.addClickListener(event -> addContractorModalWindow.open());
        return buttonUnit;
    }

    private Button buttonFilter() {
        return new Button("Фильтр");
    }

    private Button buttonSettings() {
        return new Button(new Icon(VaadinIcon.COG_O));
    }

    private TextField text() {
        TextField text = new TextField();
        text.setPlaceholder("Наимен, тел, соб, коммент...");
        text.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        text.setWidth("300px");
        return text;
    }

    private H2 title() {
        H2 title = new H2("Контрагенты");
        title.setHeight("2.2em");
        return title;
    }

    private NumberField numberField() {
        NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    private Select<String> valueSelect() {
        Select<String> valueSelect = new Select<>();
        valueSelect.setItems("Изменить");
        valueSelect.setValue("Изменить");
        valueSelect.setWidth("130px");
        return valueSelect;
    }

    private void configureGrid() {
        grid.setColumns("id", "name", "inn", "sortNumber", "phone", "fax", "email", "address", "commentToAddress", "comment");
        grid.getColumnByKey("id").setHeader("ID");

        grid.getColumnByKey("name").setHeader("Наименование");
        grid.getColumnByKey("inn").setHeader("Инн");
        grid.getColumnByKey("sortNumber").setHeader("номер");
        grid.getColumnByKey("phone").setHeader("телефон");
        grid.getColumnByKey("fax").setHeader("факс");
        grid.getColumnByKey("email").setHeader("емэйл");
        grid.getColumnByKey("address").setHeader("адресс");
        grid.getColumnByKey("commentToAddress").setHeader("комментарий к адресу");
        grid.getColumnByKey("comment").setHeader("комментарий");
        grid.setHeight("64vh");

        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addItemDoubleClickListener(event -> {
            ContractorDto editContractor = event.getItem();
            ContractorModalWindow addContractorModalWindow =
                    new ContractorModalWindow(editContractor, contractorService, contractorGroupService);
            addContractorModalWindow.addDetachListener(e -> updateList());
            addContractorModalWindow.open();
        });
    }

    private void updateList() {
        this.grid = new Grid<>(ContractorDto.class);
        GridPaginator<ContractorDto> paginator
                = new GridPaginator<>(grid, contractorService.getAll(), 9);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        configureGrid();
        removeAll();
        add(upperLayout(), grid, paginator);
    }

    private void UploadXlsMenuItem(SubMenu subMenu) {
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
                getErrorNotification("Файл с таки именем уже существует");
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

    private void configureSelectXlsTemplateButton() {
        SubMenu printSubMenu = print.getSubMenu();
        printSubMenu.removeAll();
        templatesXlsMenuItems(printSubMenu);
        UploadXlsMenuItem(printSubMenu);
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upperLayout = new HorizontalLayout();
        upperLayout.add(buttonQuestion(), title(), buttonRefresh(), buttonUnit(), buttonFilter(), text(), numberField(),
                valueSelect(), buttonSettings(), selectXlsTemplateButton);
        upperLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upperLayout;
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
        PrintContractorsXls printContractorsXls = new PrintContractorsXls(file.getPath(), contractorService.getAll());
        return new Anchor(new StreamResource(templateName, printContractorsXls::createReport), templateName);
    }


    private void getInfoNotification(String message) {
        Notification notification = new Notification(message, 5000);
        notification.open();
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
}


