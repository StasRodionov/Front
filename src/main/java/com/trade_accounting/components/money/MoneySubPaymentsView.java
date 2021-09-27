package com.trade_accounting.components.money;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridFilter;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.PaymentDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.ContractorService;
import com.trade_accounting.services.interfaces.PaymentService;
import com.trade_accounting.services.interfaces.ProjectService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Route(value = "MoneySubPaymentsView", layout = AppView.class)
@PageTitle("Платежи")
public class MoneySubPaymentsView extends VerticalLayout {
    private final transient PaymentService paymentService;
    private final transient CompanyService companyService;
    private final transient ContractorService contractorService;
    private final transient ProjectService projectService;
    private final transient ContractService contractService;
    private final transient Notifications notifications;

    private final transient List<PaymentDto> data;
    private final Grid<PaymentDto> grid = new Grid<>(PaymentDto.class, false);
    private final GridPaginator<PaymentDto> paginator;
    private final GridFilter<PaymentDto> filter;
    private final CreditOrderModal creditOrderModal;
    private final IncomingPaymentModal incomingPaymentModal;
    private final OutgoingPaymentModal outgoingPaymentModal;
    private final ExpenseOrderModal expenseOrderModal;
    private final Scroller scroller = new Scroller();

    MoneySubPaymentsView(PaymentService paymentService,
                         CompanyService companyService,
                         ContractorService contractorService,
                         ProjectService projectService,
                         ContractService contractService,
                         Notifications notifications,
                         CreditOrderModal creditOrderModal,
                         IncomingPaymentModal incomingPaymentModal,
                         ExpenseOrderModal expenseOrderModal,
                         OutgoingPaymentModal outgoingPaymentModal) {
        this.paymentService = paymentService;
        this.data = getDate();
        this.companyService = companyService;
        this.contractorService = contractorService;
        this.projectService = projectService;
        this.contractService = contractService;
        this.notifications = notifications;
        this.creditOrderModal = creditOrderModal;
        this.incomingPaymentModal = incomingPaymentModal;
        this.expenseOrderModal = expenseOrderModal;
        this.outgoingPaymentModal = outgoingPaymentModal;


        getGrid();
        configureScroller();
        this.paginator = new GridPaginator<>(grid, data, 10);
        this.filter = new GridFilter<>(grid);
        configureScroller();
        configureFilter();
        setHorizontalComponentAlignment(Alignment.CENTER, paginator);
        add(getToolbar(), filter, grid, paginator);
    }

    private void configureScroller() {
        scroller.setScrollDirection(Scroller.ScrollDirection.HORIZONTAL);
        scroller.setContent(grid);
        add(scroller);
    }

    private void configureFilter() {
        filter.setFieldToDatePicker("time");
        filter.setFieldToIntegerField("sum");
        filter.setFieldToIntegerField("number");
        filter.setFieldToComboBox("typeOfPayment", "Входящий платеж", "Приходный ордер");
        filter.setFieldToIntegerField("contractDto");
        filter.onSearchClick(e -> paginator.setData(paymentService.filter(filter.getFilterData())));
        filter.onClearClick(e -> paginator.setData(paymentService.getAll()));
    }

    private Grid getGrid() {
        grid.setItems(data);
        grid.addColumn("typeOfDocument").setWidth("11em").setFlexGrow(0).setHeader("Тип документа").setId("Тип документа");
        grid.addColumn("number").setWidth("6em").setFlexGrow(0).setHeader("№").setId("Номер платежа");
        grid.addColumn("time").setWidth("12em").setFlexGrow(0).setHeader("Дата").setId("Дата");
        grid.addColumn(pDto -> companyService.getById(pDto.getCompanyId()).getName()).setWidth("14em").setFlexGrow(0).setSortable(true)
                .setKey("companyDto").setHeader("Компания").setId("Компания");
        grid.addColumn(pDto -> contractorService.getById(pDto.getContractorId()).getName()).setWidth("19em").setFlexGrow(0).setSortable(true)
                .setKey("contractorDto").setHeader("Контрагент").setId("Контрагент");
        grid.addColumn("sum").setFlexGrow(4).setHeader("Сумма").setId("Сумма");
        grid.addColumn("paymentMethods").setFlexGrow(4).setHeader("Способ Оплаты").setId("Способ оплаты");
        grid.addColumn("expenseItem").setFlexGrow(4).setHeader("Статья расходов").setId("Статья расходов");
        grid.addColumn(pDto -> contractService.getById(pDto.getContractId()).getNumber()).setFlexGrow(3).setSortable(true)
                .setKey("contractDto").setHeader("Договор").setId("Договор");
        grid.addColumn(pDto -> projectService.getById(pDto.getProjectId()).getName()).setFlexGrow(7).setSortable(true)
                .setKey("projectDto").setHeader("Проект").setId("Проект");
        grid.addColumn("typeOfPayment").setFlexGrow(4).setHeader("Тип платежа").setId("Тип платежа");
        grid.setHeight("73vh");
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addItemDoubleClickListener(event -> {
            PaymentDto editPaymentDto = event.getItem();
            if (editPaymentDto.getTypeOfDocument().equals("входящий платеж")) {
                IncomingPaymentModal incomingPaymentModal = new IncomingPaymentModal(
                        paymentService,
                        companyService,
                        contractorService,
                        projectService,
                        contractService,
                        notifications);
                incomingPaymentModal.addDetachListener(e -> updateList());
                incomingPaymentModal.setPaymentDataForEdit(editPaymentDto);
                incomingPaymentModal.open();
            } else {
                CreditOrderModal addCreditOrderModal = new CreditOrderModal(
                        paymentService,
                        companyService,
                        contractorService,
                        projectService,
                        contractService,
                        notifications);
                addCreditOrderModal.addDetachListener(e -> updateList());
                addCreditOrderModal.setPaymentDataForEdit(editPaymentDto);
                addCreditOrderModal.open();
            }

        });
        return grid;
    }

    private void updateList() {
        GridPaginator<PaymentDto> paginatorUpdateList
                = new GridPaginator<>(grid, paymentService.getAll(), 10);
        setHorizontalComponentAlignment(Alignment.CENTER, paginatorUpdateList);
        removeAll();
        add(getToolbar(), grid, paginator);
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(getButtonQuestion(), getTextContract(), getButtonRefresh(), getComingMenuBar(), getConsumptionMenuBar(),
                getButtonFilter(), getTextField(), getNumberField(), getSelect(), getPrint(), getButtonCog());
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

    private TextField getTextField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("Наименование или код");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> updateList(textField.getValue()));
        return textField;
    }

    private void updateList(String search) {
        if (search.isEmpty()) {
            paginator.setData(paymentService.getAll());
        } else paginator.setData(paymentService.search(search));
    }

    private Button getButtonFilter() {
        Button filterButton = new Button("Фильтр");
        filterButton.addClickListener(e -> filter.setVisible(!filter.isVisible()));
        return filterButton;
    }

    private MenuBar getComingMenuBar() {
        MenuBar menuBar = new MenuBar();
        MenuItem menuPayment = menuBar.addItem("Приход");
        SubMenu subMenu = menuPayment.getSubMenu();
        subMenu.addItem("Приходный ордер", menuItemClickEvent -> creditOrderModal.open());
        subMenu.addItem("Входящий платеж", menuItemClickEvent -> incomingPaymentModal.open());
        return menuBar;
    }

    private MenuBar getConsumptionMenuBar() {
        MenuBar menuBar = new MenuBar();
        MenuItem menuPayment = menuBar.addItem("Расход");
        SubMenu subMenu = menuPayment.getSubMenu();
        subMenu.addItem("Расходный ордер", menuItemClickEvent -> expenseOrderModal.open());
        subMenu.addItem("Исходящий платеж", menuItemClickEvent -> outgoingPaymentModal.open());
        return menuBar;
    }

    private Button getButtonRefresh() {
        final Button buttonRefresh;
        Icon circle = new Icon(VaadinIcon.REFRESH);
        buttonRefresh = new Button();
        buttonRefresh.setIcon(circle);
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private H2 getTextContract() {
        final H2 textCompany = new H2("Платежи");
        textCompany.setHeight("2.2em");
        return textCompany;
    }

    private Button getButtonQuestion() {
        final Button buttonQuestion = new Button();
        Icon question = new Icon(VaadinIcon.QUESTION_CIRCLE_O);
        buttonQuestion.setIcon(question);
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return buttonQuestion;
    }

    private MenuBar getSelect() {
        MenuBar menuBar = new MenuBar();
        MenuItem menuPayment = menuBar.addItem("Изменить");
        SubMenu subMenu = menuPayment.getSubMenu();
        subMenu.addItem("Удалить", menuItemClickEvent -> {
            deleteSelectedContractors();
            grid.deselectAll();
            paginator.setData(getDate());
        });
        return menuBar;
    }

    private void deleteSelectedContractors() {
        if (!grid.getSelectedItems().isEmpty()) {
            for (PaymentDto paymentDto: grid.getSelectedItems()) {
                paymentService.deleteById(paymentDto.getId());
                notifications.infoNotification("Выбранные контрагенты успешно удалены");
            }
        } else {
            notifications.errorNotification("Сначала отметьте галочками нужные контрагенты");
        }
    }

    private MenuBar getPrint() {
        MenuBar menuBar = new MenuBar();
        MenuItem menuPayment = menuBar.addItem("Печать");
        SubMenu subMenu = menuPayment.getSubMenu();
        subMenu.addItem("Список всех платежей", menuItemClickEvent -> {
            PaymentPrintModal paymentPrintModal = new PaymentPrintModal(companyService, contractService, contractorService, projectService, paymentService);
            paymentPrintModal.open();
        });
        return menuBar;
    }

    private List<PaymentDto> getDate() {
        return paymentService.getAll().stream().sorted((o1, o2) -> o1.getId().compareTo(o2.getId())).collect(Collectors.toList());
    }
}
