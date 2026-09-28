package com.warehouseslot.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class Bin {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @ManyToOne(optional = false)
    @JoinColumn(name = "zone_id")
    private Zone zone;

    private int capacity;
    private int occupied;
}
