package com.warehouseslot.dto;

/** Total quantity picked per item (higher = faster moving). */
public record ItemVelocityReport(String itemCode, Long totalPicked) {
}
