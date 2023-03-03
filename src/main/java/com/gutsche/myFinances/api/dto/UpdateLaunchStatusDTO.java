package com.gutsche.myFinances.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateLaunchStatusDTO {

    private String status;
}
