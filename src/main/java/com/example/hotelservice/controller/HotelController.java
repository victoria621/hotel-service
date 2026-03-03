package com.example.hotelservice.controller;

import com.example.hotelservice.model.dto.*;
import com.example.hotelservice.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Hotel Management", description = "Endpoints for managing hotels")
public class HotelController {

    private static final Logger log = LoggerFactory.getLogger(HotelController.class);
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/hotels")
    @Operation(
            summary = "Get all hotels",
            description = "Returns list of all hotels with brief information. " +
                    "Use this endpoint to get a quick overview of all hotels."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Hotels retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HotelSummaryDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error - something went wrong"
            )
    })
    public ResponseEntity<List<HotelSummaryDto>> getAllHotels(){
        log.info("Call method getAllHotels");

        return ResponseEntity.status(HttpStatus.OK)
                .body(hotelService.getAllHotels());
    }

    @GetMapping("/hotels/{id}")
    @Operation(
            summary = "Get hotels with certain id",
            description = "Returns detailed information about a specific hotel"
    )
    @ApiResponses({
                    @ApiResponse(
                            responseCode = "200", description = "Hotel found"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Hotel not found with provided ID"
                    ),
                    @ApiResponse(
                            responseCode = "500", description = "Internal server error"
                    )
    })
    public ResponseEntity<HotelDto> getHotelById(
            @PathVariable("id") Long id
    ){
        log.info("Call method getHotelById");

        return ResponseEntity.status(HttpStatus.OK)
                .body(hotelService.getHotelsById(id));
    }

    @GetMapping("/search")
    @Operation(
            summary = "Get hotels by search",
            description = "Return information about hotels by " +
                    "search(name,brand,city,country,amenities) "
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Hotel found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Hotel not found with this search"
            ),
            @ApiResponse(
                    responseCode = "500", description = "Internal server error"
            )
    })
    public ResponseEntity<List<HotelSummaryDto>> getHotelsBySearch(
            @Parameter(description = "Hotel name")
            @RequestParam(required = false) String name,
            @Parameter(description = "Hotel brand")
            @RequestParam(required = false) String brand,
            @Parameter(description = "City")
            @RequestParam(required = false) String city,
            @Parameter(description = "Country")
            @RequestParam(required = false) String country,
            @Parameter(description = "Amenities")
            @RequestParam(required = false) List<String> amenities
    ){
        log.info("Call method getHotelsBySearch");

        return ResponseEntity.status(HttpStatus.OK)
                .body(hotelService.SearchHotels(name,brand,city,country,amenities));
    }

    @PostMapping("/hotels")
    @Operation(
            summary = "Create new hotel",
            description = "Creates a new hotel"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Hotel created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public ResponseEntity<HotelSummaryDto> createHotel(
            @RequestBody HotelCreateDto hotelCreateDto
    ){
        log.info("Call method createHotel");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hotelService.createHotel(hotelCreateDto));
    }

    @PostMapping("/hotels/{id}/amenities")
    @Operation(
            summary = "Add some amenities",
            description = "Adds list of amenities to a specific hotel"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Amenities added successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Hotel not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public ResponseEntity<Void>  addAmenities(
            @Parameter(description = "Hotel ID", required = true)
            @PathVariable("id") Long id,
            @RequestBody List<String> amenities
    ){
        log.info("Call method addAmenities");
        hotelService.addAmenities(id,amenities);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/histogram/{param}")
    @Operation(
            summary = "Get histogram",
            description = "Returns count of hotels grouped by specified parameter"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved histogram"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameter"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public ResponseEntity<Map<String,Long>> getHistogram(
            @PathVariable String param
    ){
        log.info("Call method getHistogram");
        return ResponseEntity.status(HttpStatus.OK)
                .body(hotelService.getHistogram(param));
    }

}
