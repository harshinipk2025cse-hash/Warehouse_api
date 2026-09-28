package com.warehouseslot.dto;

import jakarta.validation.constraints.*;

public record BinRequest(
        @NotBlank(message = "Bin code is required") String code,
        @NotNull(message = "zoneId is required") Long zoneId,
        @NotNull(message = "capacity is required") @Positive(message = "capacity must be positive") Integer capacity) {
}
