package com.warehouseslot.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class PickList {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    private PickListStatus status = PickListStatus.OPEN;

    /** Stored in walking order (near-dispatch first). */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "pick_list_id")
    @OrderColumn
    private List<PickItem> items = new ArrayList<>();
}
