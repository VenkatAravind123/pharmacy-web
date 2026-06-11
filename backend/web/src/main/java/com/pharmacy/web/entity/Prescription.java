package com.pharmacy.web.entity;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prescriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String filePath;

    @Enumerated(EnumType.STRING)
    private PrescriptionStatus status;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}