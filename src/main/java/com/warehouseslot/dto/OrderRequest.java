package com.warehouseslot.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record OrderRequest(
        @NotEmpty(message = "Order must have at least one line") List<@Valid Line> lines) {

    public record Line(
            @NotBlank(message = "itemCode is required") String itemCode,
            @NotNull(message = "quantity is required") @Positive(message = "quantity must be positive") Integer quantity) {
    }
}
