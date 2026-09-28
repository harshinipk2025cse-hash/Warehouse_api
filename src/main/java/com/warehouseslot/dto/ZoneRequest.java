package com.warehouseslot.dto;

import com.warehouseslot.model.ZoneType;
import jakarta.validation.constraints.*;

public record ZoneRequest(
        @NotBlank(message = "Zone name is required") String name,
        @NotNull(message = "Zone type is required (NEAR_DISPATCH or FAR)") ZoneType type,
        @NotNull(message = "routeOrder is required") @Positive(message = "routeOrder must be positive") Integer routeOrder) {
}
