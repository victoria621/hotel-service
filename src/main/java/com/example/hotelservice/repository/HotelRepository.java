package com.example.hotelservice.repository;

import com.example.hotelservice.model.entity.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Long> {

    @Query("SELECT DISTINCT h FROM HotelEntity h " +
            "LEFT JOIN FETCH h.amenities " +
            "WHERE (:name IS NULL OR LOWER(h.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:brand IS NULL OR LOWER(h.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) " +
            "AND (:city IS NULL OR LOWER(h.address.city) LIKE LOWER(CONCAT('%', :city, '%'))) " +
            "AND (:country IS NULL OR LOWER(h.address.country) LIKE LOWER(CONCAT('%', :country, '%'))) " +
            "AND (:amenity IS NULL OR EXISTS (SELECT a FROM h.amenities a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :amenities, '%'))))")
    List<HotelEntity> searchHotels(
            @Param("name") String name,
            @Param("brand") String brand,
            @Param("city") String city,
            @Param("country") String country,
            @Param("amenities") List<String> amenities);

    @Query("SELECT h.address.city, COUNT(h) FROM HotelEntity h GROUP BY h.address.city")
    List<Object[]> countByCity();

    @Query("SELECT h.address.country, COUNT(h) FROM HotelEntity h GROUP BY h.address.country")
    List<Object[]> countByCountry();

    @Query("SELECT h.brand, COUNT(h) FROM HotelEntity h GROUP BY h.brand")
    List<Object[]> countByBrand();

    @Query("SELECT a.name, COUNT(h) FROM HotelEntity h JOIN h.amenities a GROUP BY a.name")
    List<Object[]> countByAmenities();
}