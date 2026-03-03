package com.example.hotelservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "arrival_times")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArrivalTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "check_in", nullable = false)
    private String checkIn;

    @Column(name = "check_out")
    private String checkOut;
}