package com.trade_accounting.models.dto.retail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventLogDto {

    private Long id;
    private String docType;
    private Long operationId;
    private String actionType;
    private String sellPoint;
    private String initiator;
    private String details;
    private String api;
}
