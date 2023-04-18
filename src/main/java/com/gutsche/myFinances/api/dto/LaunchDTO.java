package com.gutsche.myFinances.api.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaunchDTO {

    private Long id;
    private String description;
    private Integer month;
    private Integer year;
    private BigDecimal value;
    private Long userId;
    private String type;
    private String status;
}
