package com.example.hotelservice.service;

import com.example.hotelservice.mapper.HotelMapper;
import com.example.hotelservice.model.dto.*;
import com.example.hotelservice.model.entity.*;
import com.example.hotelservice.repository.HotelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {
    private HotelRepository repository;
    private HotelMapper mapper;

    public HotelService(HotelRepository repository) {
        this.repository = repository;
    }

    public List<HotelSummaryDto> getAllHotels(){
        List<HotelEntity> allEntity = repository.findAll();

        return allEntity.stream()
                .map(mapper::toSummaryDto)
                .toList();
    }

    public HotelDto getHotelsById(Long id){
        HotelEntity hotelEntity = repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Hotel with id " + id + " not found"));

        return mapper.toDetailsDto(hotelEntity);
    }
}
