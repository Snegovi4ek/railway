package com.example.railway.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AddBookingDto {

    @NotEmpty(message = "ID рейса обязательно!")
    private String tripId;

    @NotEmpty(message = "ID зала обязательно!")
    private String hallId;

    @NotNull(message = "Количество мест обязательно!")
    @Min(value = 1, message = "Минимум 1 место")
    private Integer seats;

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getHallId() {
        return hallId;
    }

    public void setHallId(String hallId) {
        this.hallId = hallId;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }
}