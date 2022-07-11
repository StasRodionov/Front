package com.trade_accounting.components.contractors;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.Buttons;
import com.trade_accounting.components.util.GridConfigurer;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.components.util.configure.components.select.Action;
import com.trade_accounting.components.util.configure.components.select.SelectConfigurer;
import com.trade_accounting.models.dto.company.ContractDto;
import com.trade_accounting.services.interfaces.company.BankAccountService;
import com.trade_accounting.services.interfaces.company.CompanyService;
import com.trade_accounting.services.interfaces.company.ContractService;
import com.trade_accounting.services.interfaces.company.ContractorService;
import com.trade_accounting.services.interfaces.company.LegalDetailService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static com.trade_accounting.config.SecurityConstants.CONTRACTORS_CONTRACTS_VIEW;

@Slf4j
@SpringComponent
@UIScope
//Если на страницу не ссылаются по URL или она не является отдельной страницей, а подгружается родительским классом, то URL и Title не нужен
/*@Route(value = CONTRACTORS_CONTRACTS_VIEW, layout = AppView.class)
@PageTitle("Договоры")*/
public class ContractsView extends VerticalLayout implements AfterNavigationObserver {

    private final LegalDetailService legalDetailService;
    private final BankAccountService bankAccountService;
    private final CompanyService companyService;
    private final ContractService contractService;
    private final ContractorService contractorService;
    private final ContractModalWindow contractModalWindow;
    private final GridFilter<ContractDto> filter;
    private final GridPaginator<ContractDto> paginator;
    private final TextField textField = new TextField();
    private final Grid<ContractDto> grid;
    private final GridConfigurer<ContractDto> gridConfigurer;
    private final Notifications notifications;
    private final GridVariant[] GRID_STYLE = {GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COLUMN_BORDERS};

    @Autowired
    ContractsView(LegalDetailService legalDetailService, BankAccountService bankAccountService, CompanyService companyService, ContractService contractService,
                  ContractorService contractorService,
                  ContractModalWindow contractModalWindow,
                  Notifications notifications) {
        this.legalDetailService = legalDetailService;
        this.bankAccountService = bankAccountService;
        this.companyService = companyService;
        this.contractService = contractService;
        this.contractorService = contractorService;
        this.contractModalWindow = contractModalWindow;
        this.notifications = notifications;
        grid = new Grid<>(ContractDto.class);
        gridConfigurer = new GridConfigurer<>(grid);
        paginator = new GridPaginator<>(grid, contractService.getAll(), 100);
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        configureGrid();
        filter = new GridFilter<>(grid);
        configureFilter();
        add(getToolbar(), filter, grid, paginator);
        contractModalWindow.addDetachListener(detachEvent -> reloadGrid());
    }

    private void configureGrid() {
        grid.addThemeVariants(GRID_STYLE);
        grid.setColumns("id", "contractDate", "amount", "comment", "number");
        grid.getColumnByKey("id").setHeader("ID").setId("ID");
        grid.getColumnByKey("contractDate").setHeader("Дата заключения").setId("Дата заключения");
        grid.getColumnByKey("amount").setHeader("Сумма").setTextAlign(ColumnTextAlign.END).setId("Сумма");
        grid.getColumnByKey("comment").setHeader("Комментарий").setId("Комментарий");
        grid.getColumnByKey("number").setHeader("Сортировочный номер").setId("Сортировочный номер");

        grid.addColumn(contractDto -> companyService.getById(contractDto.getCompanyId()).getName())
                .setHeader("Компания").setKey("company").setId("Компания");
        grid.addColumn(contractDto -> contractorService.getById(contractDto.getContractorId()).getName())
                .setHeader("Контрагент").setKey("contractor").setId("Контрактор");
        grid.addColumn(contractDto -> bankAccountService.getById(contractDto.getBankAccountId()).getBank() + " " +
                bankAccountService.getById(contractDto.getBankAccountId()).getAccount())
                .setHeader("Банковский аккаунт").setKey("bankAccount").setId("Банковский аккаунт");
        grid.addColumn(new ComponentRenderer<>(contractDto -> {
            if (contractDto.getArchive()) {
                return new Icon(VaadinIcon.CHECK_CIRCLE);
            } else {
                Icon noIcon = new Icon(VaadinIcon.CIRCLE_THIN);
                noIcon.setColor("white");
                return noIcon;
            }
        })).setHeader("Архив").setKey("archive").setId("Архив");

        grid.addColumn(contractDto -> legalDetailService.getById(contractDto.getLegalDetailId()).getLastName() + " " +
                legalDetailService.getById(contractDto.getLegalDetailId()).getFirstName() + " " +
                legalDetailService.getById(contractDto.getLegalDetailId()).getMiddleName())
                .setHeader("Юридические детали").setKey("legalDetails").setId("Юридические детали");

        grid.setColumnOrder(
                grid.getColumnByKey("id"),
                grid.getColumnByKey("contractDate"),
                grid.getColumnByKey("amount"),
                grid.getColumnByKey("company"),
                grid.getColumnByKey("legalDetails"),
                grid.getColumnByKey("contractor"),
                grid.getColumnByKey("bankAccount"),
                grid.getColumnByKey("archive"),
                grid.getColumnByKey("comment"),
                grid.getColumnByKey("number"));

        grid.getColumns().forEach(column -> column.setResizable(true).setAutoWidth(true).setSortable(true));
        gridConfigurer.addConfigColumnToGrid();

        grid.setHeight("64vh");
        grid.setColumnReorderingAllowed(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addItemDoubleClickListener(event -> {
            ContractDto сontractDto = event.getItem();
            ContractModalWindow сontractModalWindow = new ContractModalWindow(
                    contractService,
                    contractorService,
                    companyService,
                    legalDetailService,
                    bankAccountService);
            сontractModalWindow.setContractDataForEdit(сontractDto);
            сontractModalWindow.open();
        });
    }

    private void reloadGrid() {
        grid.setItems(contractService.getAll());
    }

    private void configureFilter() {
        filter.setFieldToIntegerField("id");
        filter.setFieldToIntegerField("amount");
        filter.setFieldToDatePicker("contractDate");
        filter.setFieldToCheckBox("archive");
        filter.onSearchClick(e -> paginator.setData(contractService.search(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(contractService.getAll()));
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), getTextContract(), getButtonRefresh(), getButton(),
                getButtonFilter(), getTextField(), getNumberField(), getSelect(), getButtonCog());
        toolbar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        return toolbar;
    }

    private Button getButtonCog() {
        final Button buttonCog = new Button();
        buttonCog.setIcon(new Icon(VaadinIcon.COG_O));
        return buttonCog;
    }

    private NumberField getNumberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }

    // WHERE!!!
    private TextField getTextField() {
        textField.setPlaceholder("Наименование или код");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        textField.setClearButtonVisible(true);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(e -> updateTextField());
        setSizeFull();
        return textField;
    }

    public void updateTextField() {
        if (!(textField.getValue().equals(""))) {
            grid.setItems(contractService.searchByTerm(textField.getValue()));
        } else {
            grid.setItems(contractService.searchByTerm("null"));
        }
    }

    private Button getButtonFilter() {
        Button buttonFilter = new Button("Фильтр");
        buttonFilter.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return buttonFilter;
    }

    private Button getButton() {
        final Button button = new Button("Договор");
        button.setIcon(new Icon(VaadinIcon.PLUS_CIRCLE));
        button.addClickListener(event -> {
            contractModalWindow.configure();
            contractModalWindow.open();
        });
        return button;
    }

    private Button getButtonRefresh() {
        final Button buttonRefresh;
        Icon circle = new Icon(VaadinIcon.REFRESH);
        buttonRefresh = new Button();
        buttonRefresh.setIcon(circle);
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        buttonRefresh.addClickListener(click -> reloadGrid());
        return buttonRefresh;
    }

    private H2 getTextContract() {
        final H2 textCompany = new H2("Договоры");
        textCompany.setHeight("2.2em");
        return textCompany;
    }

    private Button getButtonQuestion() {
        return Buttons.buttonQuestion("Добавьте описание");
    }

    private Select<String> getSelect() {
        return SelectConfigurer.configureDeleteSelect(() -> {
                    deleteSelectedInvoices();
                    grid.deselectAll();
                }
        );
    }

    private void deleteSelectedInvoices() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (ContractDto contractDto : grid.getSelectedItems()) {
                contractService.deleteById(contractDto.getId());
                notifications.infoNotification("Выбранные контракты успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные контракты");
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        reloadGrid();
    }
}
