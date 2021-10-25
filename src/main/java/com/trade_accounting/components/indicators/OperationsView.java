package com.trade_accounting.components.indicators;

import com.trade_accounting.components.AppView;
import com.trade_accounting.components.goods.GoodsSubInventoryModalWindow;
import com.trade_accounting.components.goods.InternalOrderModalView;
import com.trade_accounting.components.goods.LossModalWindow;
import com.trade_accounting.components.goods.MovementViewModalWindow;
import com.trade_accounting.components.goods.PostingModal;
import com.trade_accounting.components.money.CreditOrderModal;
import com.trade_accounting.components.money.ExpenseOrderModal;
import com.trade_accounting.components.money.IncomingPaymentModal;
import com.trade_accounting.components.money.OutgoingPaymentModal;
import com.trade_accounting.components.purchases.AcceptanceModalView;
import com.trade_accounting.components.purchases.SupplierAccountModalView;
import com.trade_accounting.components.sells.SalesEditCreateInvoiceView;
import com.trade_accounting.components.sells.SalesEditShipmentView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.components.util.Notifications;
import com.trade_accounting.models.dto.OperationsDto;
import com.trade_accounting.services.interfaces.OperationsService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@SpringComponent
@Route(value = "operationsView", layout = AppView.class)
@PageTitle("Документы")
@UIScope
public class OperationsView extends VerticalLayout {
    private final OperationsService operationsService;
    private final CreditOrderModal creditOrderModal;
    private final SalesEditCreateInvoiceView salesEditCreateInvoiceView;
    private final GoodsSubInventoryModalWindow goodsSubInventoryModalWindow;
    private final InternalOrderModalView internalOrderModalView;
    private final LossModalWindow lossModalWindow;
    private final MovementViewModalWindow movementViewModalWindow;
    private final PostingModal postingModal;
    private final IncomingPaymentModal incomingPaymentModal;
    private final OutgoingPaymentModal outgoingPaymentModal;
    private final ExpenseOrderModal expenseOrderModal;
    private final SupplierAccountModalView supplierAccountModalView;
    private final AcceptanceModalView acceptanceModalView;
    private final SalesEditShipmentView salesEditShipmentView;


//    private final Grid<OperationsDto> grid = new Grid<>(OperationsDto.class, false);
//    private final GridPaginator<OperationsDto> paginator;
    private final Notifications notifications;
    public OperationsView(OperationsService operationsService, CreditOrderModal creditOrderModal,
                          SalesEditCreateInvoiceView salesEditCreateInvoiceView,
                          GoodsSubInventoryModalWindow goodsSubInventoryModalWindow,
                          InternalOrderModalView internalOrderModalView,
                          LossModalWindow lossModalWindow, MovementViewModalWindow movementViewModalWindow,
                          PostingModal postingModal, IncomingPaymentModal incomingPaymentModal,
                          OutgoingPaymentModal outgoingPaymentModal, ExpenseOrderModal expenseOrderModal,
                          SupplierAccountModalView supplierAccountModalView, AcceptanceModalView acceptanceModalView, SalesEditShipmentView salesEditShipmentView, Notifications notifications) {
        this.operationsService = operationsService;
        this.creditOrderModal = creditOrderModal;
        this.salesEditCreateInvoiceView = salesEditCreateInvoiceView;
        this.goodsSubInventoryModalWindow = goodsSubInventoryModalWindow;
        this.internalOrderModalView = internalOrderModalView;
        this.lossModalWindow = lossModalWindow;
        this.movementViewModalWindow = movementViewModalWindow;
        this.postingModal = postingModal;
        this.incomingPaymentModal = incomingPaymentModal;
        this.outgoingPaymentModal = outgoingPaymentModal;
        this.expenseOrderModal = expenseOrderModal;
        this.supplierAccountModalView = supplierAccountModalView;
        this.acceptanceModalView = acceptanceModalView;
        this.salesEditShipmentView = salesEditShipmentView;
        this.notifications = notifications;
//        List<OperationsDto> data = getData();
//        paginator = new GridPaginator<>(grid, data, 50);
        setHorizontalComponentAlignment(Alignment.CENTER);
        add(getUpperLayout());
//        configureGrid();
    }

//    private Grid<OperationsDto> configureGrid() {
//        grid.addColumn(OperationsDto::getDate).setHeader("Дата документа").setId("Дата документа");
//        grid.addColumn(OperationsDto::getId).setHeader("№").setId("№");
//        grid.addColumn(OperationsDto::getComment).setHeader("Комментарий").setId("Комментарий");
//        return grid;
//    }

//    private List<OperationsDto> getData() {
//        return operationsService.getAll();
//    }

    private Component getUpperLayout(){
        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.add(buttonQuestion(),title(), buttonRefresh(), configSubMenu(), buttonFilter(),
                textField(), numberField(), valueSelect(), valuePrint());
        mainLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return mainLayout;
    }

    private Button buttonQuestion() {
        Button buttonQuestion = new Button(new Icon(VaadinIcon.QUESTION_CIRCLE_O));
        buttonQuestion.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Dialog dialog = new Dialog();
        Button cancelButton = new Button("Закрыть", event -> dialog.close());
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponentAsFirst(cancelButton);
        dialog.add(new Text("В разделе представлены файлы, добавленные за всё время." +
                " Здесь удобно искать конкретные файлы с помощью фильтров и удалять файлы, " +
                "чтобы освободить место в хранилище."));
        dialog.setWidth("500px");
        dialog.setHeight("300px");
        buttonQuestion.addClickListener(event -> dialog.open());
        Shortcuts.addShortcutListener(dialog, dialog::close, Key.ESCAPE);
        dialog.add(new Div(cancelButton));
        return buttonQuestion;
    }
    private H2 title() {
        H2 textCompany = new H2("Документы");
        textCompany.setHeight("2.2em");
        return textCompany;
    }

    private Button buttonRefresh() {
        Button buttonRefresh = new Button(new Icon(VaadinIcon.REFRESH));
        buttonRefresh.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        return buttonRefresh;
    }

    private Button buttonFilter() {
        Button buttonFilter = new Button("Фильтр");
       //реализовать
        return buttonFilter;
    }

    private TextField textField() {
        final TextField textField = new TextField();
        textField.setPlaceholder("Номер, компания или комментарий");
        textField.addThemeVariants(TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        textField.setWidth("300px");
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> updateList(textField.getValue()));
        return textField;
    }

    private NumberField numberField() {
        final NumberField numberField = new NumberField();
        numberField.setPlaceholder("0");
        numberField.setWidth("45px");
        return numberField;
    }


    private Select<String> valueSelect() {
        Select<String> select = new Select<>();
        List<String> listItems = new ArrayList<>();
        listItems.add("Изменить");
        listItems.add("Удалить");
        select.setItems(listItems);
        select.setValue("Изменить");
        select.setWidth("130px");
        return select;
    }

    private Select<String> valuePrint() {
        Select<String> print = new Select<>();
        print.setItems("Печать", "Добавить шаблон");
        print.setValue("Печать");
        print.setWidth("130px");
        return print;
    }


    private void updateList(String text) {
       //реализовать
    }

    //меню добавления документов
    private HorizontalLayout configSubMenu() {
        MenuBar menuBar = new MenuBar();
        menuBar.setOpenOnHover(true);
        HorizontalLayout operations = new HorizontalLayout(menuBar);
        MenuItem operationsMenuItem = menuBar.addItem("Добавить документ");
        SubMenu operationsSubMenu = operationsMenuItem.getSubMenu();

        //Продажи sells/shipment-edit
        MenuItem salesSubCustomersOrders = operationsSubMenu.addItem("Заказ покупателя");
        salesSubCustomersOrders.addClickListener(e -> operations.getUI()
                .ifPresent(ui -> ui.navigate("sells/customer-order-edit")));

        MenuItem salesSubInvoicesToBuyers = operationsSubMenu.addItem("Счета покупателей");
        salesSubInvoicesToBuyers.addClickListener(e -> operations.getUI()
                .ifPresent(ui -> ui.navigate("sells/add-new-invoices-to-buyers")));
        MenuItem salesSubShipment = operationsSubMenu.addItem("Отгрузки");
        salesSubShipment.addClickListener(e -> operations.getUI()
                .ifPresent(ui -> ui.navigate("sells/shipment-edit")));
        //Закупки
        MenuItem supplierOrders = operationsSubMenu.addItem("Заказы поставщикам");
        supplierOrders.addClickListener(e -> operations.getUI()
                .ifPresent(ui -> ui.navigate("sells/customer-order-edit")));

        MenuItem vendorAccounts = operationsSubMenu.addItem("Счета поставщиков", menuItemClickEvent -> supplierAccountModalView.open());
        MenuItem admissions = operationsSubMenu.addItem("Приемка", menuItemClickEvent -> acceptanceModalView.open());

        //Деньги
        MenuItem incomingPayment = operationsSubMenu.addItem("Входящие платежи", menuItemClickEvent -> incomingPaymentModal.open());
        MenuItem сreditOrder = operationsSubMenu.addItem("Приходные ордеры", menuItemClickEvent -> creditOrderModal.open());
        MenuItem outComingPayment = operationsSubMenu.addItem("Исходящие платежи", menuItemClickEvent -> outgoingPaymentModal.open());
        MenuItem expenseOrder = operationsSubMenu.addItem("Расходные ордеры", menuItemClickEvent -> expenseOrderModal.open());

        //Склад
        MenuItem internalOrder = operationsSubMenu.addItem("Внутренний заказ", menuItemClickEvent -> internalOrderModalView.open());
        MenuItem movement = operationsSubMenu.addItem("Перемещение");
        movement.addClickListener(e -> operations.getUI().ifPresent(ui -> ui.navigate("goods/add_moving")));
        MenuItem inventory = operationsSubMenu.addItem("Инвентаризация", menuItemClickEvent -> goodsSubInventoryModalWindow.open());
        MenuItem posting = operationsSubMenu.addItem("Оприходование", menuItemClickEvent -> postingModal.open());
        MenuItem loss = operationsSubMenu.addItem("Списание", menuItemClickEvent -> lossModalWindow.open());

        //Производство
        MenuItem technologicalOperations = operationsSubMenu.addItem("Технологическая операция");
        technologicalOperations.addClickListener(e -> operations.getUI()
                .ifPresent(ui -> ui.navigate("technologicalOperationsModal")));

        MenuItem ordersOfProduction = operationsSubMenu.addItem("Заказ на производство");
        ordersOfProduction.addClickListener(e -> operations.getUI()
                .ifPresent(ui -> ui.navigate("ordersOfProductionModal")));
        //  "Производственное задание"
        return operations;



    }

}
