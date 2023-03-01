package com.gutsche.myFinances.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class LaunchDTO {

    private Long id;
    private String description;
    private Integer month;
    private Integer year;
    private BigDecimal value;
    private Long user;
    private String type;
    private String status;
}
