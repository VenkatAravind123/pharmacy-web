package com.pharmacy.web.entity;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "medicine_id",
            unique = true,
            nullable = false
    )
    private Medicine medicine;

    private Integer quantity;
    
    private Integer reorderLevel;

    private LocalDateTime lastUpdated;
}
