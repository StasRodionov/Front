package com.trade_accounting.components.contractors;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.services.interfaces.ContractorGroupService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
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
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Route(value = "contractorsTabView", layout = AppView.class)
@PageTitle("Контрагенты")
public class ContractorsTabView extends VerticalLayout {

    private final ContractorService contractorService;

    private final ContractorGroupService contractorGroupService;
    private Grid<ContractorDto> grid;


    private final String pathForSaveXlsTemplate = "src/main/java/com/trade_accounting/components/contractors/";

    public ContractorsTabView(ContractorService contractorService, ContractorGroupService contractorGroupService) {
        this.contractorService = contractorService;
        this.contractorGroupService = contractorGroupService;
        updateList();
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

    private MenuItem UploadXlsMenuItem(SubMenu subMenu) {
        MenuItem menuItem = subMenu.addItem("добавить шаблон");
        Dialog dialog = new Dialog();
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);

        upload.addFinishedListener(event -> {
            File exelTemplate = new File(pathForSaveXlsTemplate + event.getFileName());
            try (InputStream inputStream = buffer.getInputStream();
                 FileOutputStream fos = new FileOutputStream(exelTemplate)) {
                exelTemplate.createNewFile();
                fos.write(inputStream.readAllBytes());
                fos.flush();
                log.info("xls шаблон успешно загружен");
            } catch (IOException e) {
                log.error("при загрузке xls файла шаблона произошла ошибка");
            }
            dialog.close();
        });
        dialog.add(upload);
        menuItem.addClickListener(x -> dialog.open());
        return menuItem;
    }

    private MenuBar getSelectXlsTemplateButton() {
        MenuBar menuBar = new MenuBar();
        MenuItem print = menuBar.addItem("печать");
        SubMenu printSubMenu = print.getSubMenu();
        MenuItemsWithXls(printSubMenu);
        UploadXlsMenuItem(printSubMenu);
        return menuBar;
    }

    private HorizontalLayout upperLayout() {
        HorizontalLayout upperLayout = new HorizontalLayout();
        upperLayout.add(buttonQuestion(), title(), buttonRefresh(), buttonUnit(), buttonFilter(), text(), numberField(),
                valueSelect(), buttonSettings(), getSelectXlsTemplateButton());
        upperLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return upperLayout;
    }

    private void MenuItemsWithXls(SubMenu subMenu) {
        List<File> list = getPathsToExelFiles();
        for (File file : list) {
            subMenu.addItem(getLinkToTemplate(file));
        }
    }

    private List<File> getPathsToExelFiles() {
        List<File> listFiles = new ArrayList<>();
        File dir = new File(pathForSaveXlsTemplate);
        for ( File file : Objects.requireNonNull(dir.listFiles())){
            if ( file.isFile() && file.getName().contains(".xls")){
                listFiles.add(file);
                log.error(file.getPath());
            }
        }
        return listFiles;
    }

    private Anchor getLinkToTemplate(File file) {
        String filePath = file.getPath();
        String fileName = file.getName();
        PrintContractorsXls printContractorsXls = new PrintContractorsXls(filePath, contractorService.getAll());
        InputStreamFactory inputStreamFactory = (InputStreamFactory) printContractorsXls::createReport;
        return new Anchor(new StreamResource(fileName, inputStreamFactory), fileName);
    }
}


