package com.dwei.core.mvc.pojo.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IdRequest {

    @NotNull
    @Min(0)
    private Long id;

}
