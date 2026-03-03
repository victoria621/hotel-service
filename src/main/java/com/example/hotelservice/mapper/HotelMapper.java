package com.example.hotelservice.mapper;

import com.example.hotelservice.model.dto.*;
import com.example.hotelservice.model.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HotelMapper {

    public HotelSummaryDto toSummaryDto(HotelEntity entity) {
        if (entity == null) return null;

        String addressString = String.format("%d %s, %s, %s, %s",
                entity.getAddress().getHouseNumber(),
                entity.getAddress().getStreet(),
                entity.getAddress().getCity(),
                entity.getAddress().getCountry(),
                entity.getAddress().getPostCode()
        );

        return new HotelSummaryDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                addressString,
                entity.getContacts().getPhone()
        );
    }

    public HotelDto toDetailsDto(HotelEntity entity) {
        if (entity == null) return null;

        return new HotelDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getBrand(),
                toAddressDto(entity.getAddress()),
                toContactsDto(entity.getContacts()),
                toArrivalTimeDto(entity.getArrivalTime()),
                mapAmenities(entity.getAmenities())
        );
    }

    public HotelEntity toEntity(HotelCreateDto dto) {
        if (dto == null) return null;

        HotelEntity entity = new HotelEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setBrand(dto.getBrand());

        if (dto.getAddress() != null) {
            entity.setAddress(toAddressEntity(dto.getAddress()));
        }
        if (dto.getContacts() != null) {
            entity.setContacts(toContactsEntity(dto.getContacts()));
        }
        if (dto.getArrivalTime() != null) {
            entity.setArrivalTime(toArrivalTimeEntity(dto.getArrivalTime()));
        }

        return entity;
    }

    public AddressDto toAddressDto(AddressEntity entity) {
        if (entity == null) return null;
        return new AddressDto(
                entity.getHouseNumber(),
                entity.getStreet(),
                entity.getCity(),
                entity.getCountry(),
                entity.getPostCode()
        );
    }

    public AddressEntity toAddressEntity(AddressDto dto) {
        if (dto == null) return null;
        AddressEntity entity = new AddressEntity();
        entity.setHouseNumber(dto.getHouseNumber());
        entity.setStreet(dto.getStreet());
        entity.setCity(dto.getCity());
        entity.setCountry(dto.getCountry());
        entity.setPostCode(dto.getPostCode());
        return entity;
    }

    public ContactsDto toContactsDto(ContactsEntity entity) {
        if (entity == null) return null;
        return new ContactsDto(
                entity.getPhone(),
                entity.getEmail()
        );
    }

    public ContactsEntity toContactsEntity(ContactsDto dto) {
        if (dto == null) return null;
        ContactsEntity entity = new ContactsEntity();
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        return entity;
    }

    public ArrivalTimeDto toArrivalTimeDto(ArrivalTimeEntity entity) {
        if (entity == null) return null;
        return new ArrivalTimeDto(
                entity.getCheckIn(),
                entity.getCheckOut()
        );
    }

    public ArrivalTimeEntity toArrivalTimeEntity(ArrivalTimeDto dto) {
        if (dto == null) return null;
        ArrivalTimeEntity entity = new ArrivalTimeEntity();
        entity.setCheckIn(dto.getCheckIn());
        entity.setCheckOut(dto.getCheckOut());
        return entity;
    }

    private List<String> mapAmenities(List<AmenityEntity> amenities) {
        if (amenities == null) return null;
        return amenities.stream()
                .map(AmenityEntity::getName)
                .collect(Collectors.toList());
    }

    public void updateEntity(HotelEntity entity, HotelCreateDto dto) {
        if (dto == null) return;

        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getBrand() != null) entity.setBrand(dto.getBrand());

        if (dto.getAddress() != null && entity.getAddress() != null) {
            updateAddressEntity(entity.getAddress(), dto.getAddress());
        }

        if (dto.getContacts() != null && entity.getContacts() != null) {
            updateContactsEntity(entity.getContacts(), dto.getContacts());
        }

        if (dto.getArrivalTime() != null && entity.getArrivalTime() != null) {
            updateArrivalTimeEntity(entity.getArrivalTime(), dto.getArrivalTime());
        }
    }

    private void updateAddressEntity(AddressEntity entity, AddressDto dto) {
        if (dto.getHouseNumber() != null) entity.setHouseNumber(dto.getHouseNumber());
        if (dto.getStreet() != null) entity.setStreet(dto.getStreet());
        if (dto.getCity() != null) entity.setCity(dto.getCity());
        if (dto.getCountry() != null) entity.setCountry(dto.getCountry());
        if (dto.getPostCode() != null) entity.setPostCode(dto.getPostCode());
    }

    private void updateContactsEntity(ContactsEntity entity, ContactsDto dto) {
        if (dto.getPhone() != null) entity.setPhone(dto.getPhone());
        if (dto.getEmail() != null) entity.setEmail(dto.getEmail());
    }

    private void updateArrivalTimeEntity(ArrivalTimeEntity entity, ArrivalTimeDto dto) {
        if (dto.getCheckIn() != null) entity.setCheckIn(dto.getCheckIn());
        if (dto.getCheckOut() != null) entity.setCheckOut(dto.getCheckOut());
    }
}