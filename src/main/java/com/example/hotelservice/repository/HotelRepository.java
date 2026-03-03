package com.example.hotelservice.repository;

import com.example.hotelservice.model.entity.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<HotelEntity, Long> {
}
