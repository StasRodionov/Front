package com.trade_accounting.components.money;

import com.helger.commons.thirdparty.IThirdPartyModuleProviderSPI;
import com.trade_accounting.components.AppView;
import com.trade_accounting.components.util.GridPaginator;
import com.trade_accounting.models.dto.ContractDto;
import com.trade_accounting.models.dto.PaymentDto;
import com.trade_accounting.services.interfaces.PaymentService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "MoneySubPaymentsView", layout = AppView.class)
@PageTitle("Платежи")
public class MoneySubPaymentsView extends VerticalLayout {

    private final PaymentService paymentService;

    private final List<PaymentDto> data;
    private final Grid<PaymentDto> grid;
    private final GridPaginator<PaymentDto> paginator;

    MoneySubPaymentsView(PaymentService paymentService){
        this.paymentService = paymentService;
        this.data = paymentService.getAll();
        this.grid = new Grid<>(PaymentDto.class);
        this.paginator = new GridPaginator<>(grid,data,100);
        getGrid();


    }

    private void getGrid(){
        Grid<PaymentDto> grid = new Grid<>(PaymentDto.class);
        grid.setItems(paymentService.getAll());
        grid.setColumns("id", "typeOfPayment", "number", "time", "companyDto", "contractorDto", "contractDto", "projectDto", "sum");
        grid.getColumnByKey("id").setAutoWidth(true).setHeader("ID");
        grid.getColumnByKey("typeOfPayment").setAutoWidth(true).setHeader("Тип платежа");
        grid.getColumnByKey("number").setAutoWidth(true).setHeader("Номер платежа");
        grid.getColumnByKey("time").setAutoWidth(true).setHeader("Дата");
        grid.getColumnByKey("companyDto").setHeader("Компания");
        grid.getColumnByKey("contractorDto").setHeader("Контрагент");
        grid.getColumnByKey("contractDto").setAutoWidth(true).setHeader("Договор");
        grid.getColumnByKey("projectDto").setAutoWidth(true).setHeader("Проект");
        grid.getColumnByKey("sum").setAutoWidth(true).setHeader("Сумма");
        grid.setHeight("66vh");
    }


//    private H2 title(){
//        H2 title = new H2("Платежи");
//        title.setHeight("2.2em");
//        return title;
//    }

}
