package com.warehouseslot.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class OrderLine {
    private String itemCode;
    private int quantity;
}
