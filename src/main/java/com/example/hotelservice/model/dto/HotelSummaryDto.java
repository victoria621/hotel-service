package com.example.hotelservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelSummaryDto {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
}