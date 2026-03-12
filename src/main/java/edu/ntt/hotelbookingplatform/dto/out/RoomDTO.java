package edu.ntt.hotelbookingplatform.dto.out;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoomDTO {

    private String roomNumber;
    private String type;
    private int capacity;
    private double pricePerNight;
    private String features;



}


