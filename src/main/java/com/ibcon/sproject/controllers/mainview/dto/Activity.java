package com.ibcon.sproject.controllers.mainview.dto;

import lombok.Data;

import java.math.BigDecimal;

public @Data class Activity {
    private Integer actId;
    private Integer wbsId;
    private String name;
    private BigDecimal cost;
}
