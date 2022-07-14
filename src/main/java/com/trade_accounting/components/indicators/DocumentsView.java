package com.trade_accounting.components.indicators;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridConfigurer;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.client.EmployeeDto;
import com.trade_accounting.models.dto.util.FileDto;
import com.trade_accounting.services.interfaces.client.EmployeeService;
import com.trade_accounting.services.interfaces.util.ColumnsMaskService;
import com.trade_accounting.services.interfaces.util.FileService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.trade_accounting.config.SecurityConstants.*;

@Slf4j
@SpringComponent
//Если на страницу не ссылаются по URL или она не является отдельной страницей, а подгружается родительским классом, то URL и Title не нужен
/*@Route(value = INDICATORS_DOCUMENTS_VIEW, layout = AppView.class)
@PageTitle("Файлы")*/
@UIScope
public class DocumentsView extends VerticalLayout {

    private final Grid<FileDto> grid = new Grid<>(FileDto.class, false);
    private final GridConfigurer<FileDto> gridConfigurer;
    private final GridPaginator<FileDto> paginator;
    private final GridFilter<FileDto> filter;
    private final FileService fileService;
    private final EmployeeService employeeService;
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES,
            GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};

    public DocumentsView(FileService fileService, EmployeeService employeeService,
                         ColumnsMaskService columnsMaskService) {
        this.employeeService = employeeService;
        this.fileService = fileService;
        this.gridConfigurer = new GridConfigurer<>(grid, columnsMaskService, GRID_INDICATORS_MAIN_FILES);
        List<FileDto> files = fileService.getAll();
        configureGrid();
        this.filter = new GridFilter<>(grid);
        this.paginator = new GridPaginator<>(grid, files, 50);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(getUpperLayout(), grid, paginator);
    }


    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.removeAllColumns();
        Grid.Column<FileDto> photoColumn = grid.addColumn(new ComponentRenderer<>() {
            @Override
            public Component createComponent(FileDto item) {
                if (item.getExtension().equals(".jpg") || item.getExtension().equals(".jpeg") || item.getExtension().equals(".png")) {
                    Image image = new Image(new StreamResource("image", () ->
                            new ByteArrayInputStream(item.getContent())), "");
                    image.setHeight("48px");

                    image.addClickListener((ComponentEventListener<ClickEvent<Image>>) imageClickEvent -> {
                        Dialog dialog = new Dialog();
                        dialog.add(new Image(new StreamResource("image", () ->
                                new ByteArrayInputStream(item.getContent())), ""));
                        dialog.open();
                    });
                    return image;
                }
                return new Label();
            }
        }).setHeader("");
        photoColumn.setWidth("10px").setKey("content").setId("Изображение");
        grid.addColumn(FileDto::getName).setKey("name").setHeader("Наименование").setSortable(true).setId("Наименование");
        Grid.Column<FileDto> buttonColumn = grid.addColumn(new ComponentRenderer<>() {
            @Override
            public Anchor createComponent(FileDto item) {
                Anchor download = new Anchor(new StreamResource(item.getName(), () -> new ByteArrayInputStream(item.getContent())), "");
                download.getElement().setAttribute("download", true);
                download.removeAll();
                download.add(new Button(new Icon(VaadinIcon.DOWNLOAD_ALT)));
                return download;
            }
        });
        buttonColumn.setKey("button").setId("Кнопки");
        grid.addColumn(fileDto -> (FileUtils.byteCountToDisplaySize(fileDto.getContent().length))).setWidth("50px").setKey("size")
                .setHeader("Размер").setSortable(true).setId("Размер");
        grid.addColumn(FileDto::getPlacement).setKey("placement").setHeader("Расположение").setSortable(true).setId("Расположение");
        grid.addColumn(fileDto -> (fileDto.getUploadDateTime().toString().substring(0, 16).replace("T", " ")))
                .setKey("uploadDateTime").setHeader("Добавлен").setSortable(true).setId("Добавлен");
        grid.addColumn(FileDto::getEmployee).setKey("employee").setHeader("Сотрудник").setSortable(true).setId("Сотрудник");
        grid.addComponentColumn(this::createRemoveButton).setHeader("Удалить");
        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        grid.getColumnByKey("content").setSortable(false);
        grid.getColumnByKey("button").setSortable(false);

        gridConfigurer.addConfigColumnToGrid();
        grid.setHeight("64vh");
        grid.setWidth("80%");
        grid.setColumnReorderingAllowed(true);
    }


    private void configureFilter() {
    }


    private Button createRemoveButton(FileDto item) {
        Icon icon = new Icon(VaadinIcon.CLOSE);
        icon.setColor("red");
        return new Button(icon, buttonClickEvent -> {
            fileService.deleteById(item.getId());
            grid.setItems(fileService.getAll());
            paginator.reloadGrid();
        });
    }


    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        buttonRefresh.addClickListener(ev -> grid.setItems(fileService.getAll()));
        paginator.reloadGrid();
        return buttonRefresh;
    }


    private Button buttonQuestion() {
        return Buttons.buttonQuestion("В разделе представлены файлы, добавленные за всё время. " +
                "Здесь удобно искать конкретные файлы с помощью фильтров и удалять файлы, " +
                "чтобы освободить место в хранилище.");
    }


    private Button buttonAdd() {
        Button buttonAdd = new Button("Загрузить файл", new Icon(VaadinIcon.PLUS_CIRCLE));
        Dialog dialog = new Dialog();
        MemoryBuffer memoryBuffer = new MemoryBuffer();
        Upload upload = new Upload(memoryBuffer);
        upload.setMaxFileSize(20971520);
        upload.setDropLabel(new Label("Загрузите файл не больше 20мб"));
        upload.addFinishedListener(event -> {
            byte[] content = new byte[0];
            try {
                content = memoryBuffer.getInputStream().readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDto fileDto = new FileDto();
            fileDto.setUploadDateTime(LocalDateTime.now());
            final String fileName = event.getFileName();
            fileDto.setName(fileName);
            String fileExtension = fileName.substring(fileName.indexOf("."));
            fileDto.setExtension(fileExtension);
            fileDto.setContent(content);
            fileDto.setKey(UUID.randomUUID().toString());
            fileDto.setPlacement("Файлы");
            EmployeeDto employee = employeeService.getPrincipal();
            fileDto.setEmployee(employee.getFirstName() + " " + employee.getLastName());
            fileService.create(fileDto);
            dialog.close();
            buttonRefresh().click();
        });
        dialog.add(upload);
        buttonAdd.addClickListener(x -> dialog.open());
        return buttonAdd;
    }


    private Component getUpperLayout(){
        HorizontalLayout mainLayout = new HorizontalLayout();
        TextField searchTextField = new TextField();
        searchTextField.setPlaceholder("Наименование");
        searchTextField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        searchTextField.setWidth("300px");
        searchTextField.setValueChangeMode(ValueChangeMode.EAGER);
        searchTextField.addValueChangeListener(field -> fillList(field.getValue()));
        Button filterButton = new Button("Фильтр");
        mainLayout.add(buttonQuestion(), title(), buttonRefresh(), buttonAdd(), filterButton, searchTextField);
        mainLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return mainLayout;
    }


    private void fillList(String searchData) {
        Map<String, String> fileName = new HashMap<>();

        if (searchData.isEmpty()) {
            grid.setItems(fileService.getAll());
        } else {
            fileName.put("name", searchData);
            grid.setItems(fileService.search(fileName));
        }
    }


    private H2 title() {
        H2 textCompany = new H2("Файлы");
        textCompany.setHeight("2.2em");
        return textCompany;
    }
}
