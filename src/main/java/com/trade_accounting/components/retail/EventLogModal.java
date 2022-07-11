package com.trade_accounting.components.retail;

import com.trade_accounting.models.dto.retail.EventLogDto;
import com.trade_accounting.services.interfaces.retail.RetailEventLogService;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@SpringComponent
@UIScope
@Slf4j
public class EventLogModal extends Dialog {

    private final RetailEventLogService retailEventLogService;
    private final Grid<EventLogDto> grid = new Grid<>(EventLogDto.class, false);
    private List<EventLogDto> eventLogDtoList;

    public EventLogModal(RetailEventLogService retailEventLogService) {
        this.retailEventLogService = retailEventLogService;

        grid.addColumn(EventLogDto::getId).setHeader("ID").setAutoWidth(true);
        grid.addColumn(EventLogDto::getDocType).setHeader("Тип документа").setAutoWidth(true);
        grid.addColumn(EventLogDto::getOperationId).setHeader("ID операции").setAutoWidth(true);
        grid.addColumn(EventLogDto::getActionType).setHeader("Тип действия").setAutoWidth(true);
        grid.addColumn(EventLogDto::getSellPoint).setHeader("Точка продаж").setAutoWidth(true);
        grid.addColumn(EventLogDto::getInitiator).setHeader("Инициатор").setAutoWidth(true);
        grid.addColumn(EventLogDto::getDetails).setHeader("Детали").setAutoWidth(true);
        grid.addColumn(EventLogDto::getApi).setHeader("Через API").setAutoWidth(true);

        grid.setHeightByRows(true);
        add(grid);
    }

    @Override
    public void open() {
        init();
        super.open();
    }

    private void init() {
        eventLogDtoList = retailEventLogService.getAll();
        grid.setItems(eventLogDtoList);
    }
}
