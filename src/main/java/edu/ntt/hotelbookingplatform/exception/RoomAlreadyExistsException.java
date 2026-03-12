package edu.ntt.hotelbookingplatform.exception;

public class RoomAlreadyExistsException extends RuntimeException {

    public RoomAlreadyExistsException(String roomNumber) {
        super("Room with number " + roomNumber + " already exists");
    }
}