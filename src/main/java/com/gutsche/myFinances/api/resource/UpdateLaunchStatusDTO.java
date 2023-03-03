package com.gutsche.myFinances.api.resource;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateLaunchStatusDTO {

    private String status;
}
