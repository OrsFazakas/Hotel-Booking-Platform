package edu.ntt.hotelbookingplatform.dto.in;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class RoomCreationDTO {

    @NotBlank(message = "Room number field must not be blank!")
    private String roomNumber;

    @NotBlank(message = "Type field must not be blank!")
    @Pattern(
            regexp = "(Single|Double|Suite|Deluxe)",
            message = "Room type must be one of the following: Single, Double, Suite, Deluxe!"
    )
    private String type;

    @Min(value = 1, message = "Capacity must be at least 1!")
    private int capacity;

    @Positive(message = "Price per night must be a positive value!")
    private BigDecimal pricePerNight;

    @NotBlank(message = "Features field must not be blank!")
    private String features;
}
