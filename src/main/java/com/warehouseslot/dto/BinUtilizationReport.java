package com.warehouseslot.dto;

public record BinUtilizationReport(String binCode, String zone, int capacity, int occupied, double utilizationPercent) {
}
