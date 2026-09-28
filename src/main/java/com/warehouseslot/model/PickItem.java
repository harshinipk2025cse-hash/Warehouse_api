package com.warehouseslot.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class PickItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemCode;
    private int quantity;

    @ManyToOne(optional = false)
    private Bin bin;

    @ManyToOne(optional = false)
    private StockAssignment stockAssignment;

    private boolean picked = false;
}
