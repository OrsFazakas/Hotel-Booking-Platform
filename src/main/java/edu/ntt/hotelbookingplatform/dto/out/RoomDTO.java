package edu.ntt.hotelbookingplatform.dto.out;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class RoomDTO {

    private String roomNumber;
    private String type;
    private int capacity;
    private BigDecimal pricePerNight;
    private String features;



}


