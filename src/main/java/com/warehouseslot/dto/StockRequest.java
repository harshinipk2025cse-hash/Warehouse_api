package com.warehouseslot.dto;

import com.warehouseslot.model.Velocity;
import jakarta.validation.constraints.*;

public record StockRequest(
        @NotBlank(message = "itemCode is required") String itemCode,
        @NotNull(message = "velocity is required (FAST or SLOW)") Velocity velocity,
        @NotNull(message = "quantity is required") @Positive(message = "quantity must be positive") Integer quantity) {
}
