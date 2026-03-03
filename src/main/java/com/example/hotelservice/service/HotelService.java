package com.example.hotelservice.service;

import com.example.hotelservice.mapper.HotelMapper;
import com.example.hotelservice.model.dto.*;
import com.example.hotelservice.model.entity.*;
import com.example.hotelservice.repository.AmenityRepository;
import com.example.hotelservice.repository.HotelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class HotelService {
    private final HotelRepository repository;
    private final AmenityRepository amenityRepository;
    private final HotelMapper mapper;

    public HotelService(HotelRepository repository, AmenityRepository amenityRepository, HotelMapper mapper) {
        this.repository = repository;
        this.amenityRepository = amenityRepository;
        this.mapper = mapper;
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

    public List<HotelSummaryDto> SearchHotels(
            String name,
            String brand,
            String city,
            String country,
            List<String> amenities
    ) {
        return repository.searchHotels(name,brand,city,country,amenities)
                .stream()
                .map(mapper::toSummaryDto)
                .toList();
    }

    @Transactional
    public HotelSummaryDto createHotel(HotelCreateDto hotelCreateDto) {
        HotelEntity hotelEntity = mapper.toEntity(hotelCreateDto);

        if (hotelEntity.getAddress() != null) {
            hotelEntity.getAddress().setId(null);
        }
        if (hotelEntity.getContacts() != null) {
            hotelEntity.getContacts().setId(null);
        }
        if (hotelEntity.getArrivalTime() != null) {
            hotelEntity.getArrivalTime().setId(null);
        }

        HotelEntity savedHotel = repository.save(hotelEntity);
        return mapper.toSummaryDto(savedHotel);

    }

    @Transactional
    public void addAmenities(Long id, List<String> amenities) {
        HotelEntity hotelEntity = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Hotel with id " + id + " not found"));
        List<AmenityEntity> addAmenities = amenities.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .distinct()
                .map(name -> amenityRepository.findByName(name)
                        .orElseGet(() -> amenityRepository.save(new AmenityEntity(name))))
                .toList();

        addAmenities.stream()
                .filter(amenity -> !hotelEntity.getAmenities().contains(amenity))
                .forEach(hotelEntity.getAmenities()::add);
    }

    public Map<String, Long> getHistogram(String param) {
        List<Object[]> result = switch (param.toLowerCase()) {
            case "brand" -> repository.countByBrand();
            case "city" -> repository.countByCity();
            case "country" -> repository.countByCountry();
            case "amenities" -> repository.countByAmenities();
            default -> throw new IllegalArgumentException("Invalid parameter");
        };

        return result.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1],
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));
    }
}
