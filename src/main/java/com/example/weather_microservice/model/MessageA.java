package com.example.weather_microservice.model;

import lombok.Data;

@Data
public class MessageA {
    private String msg;
    private String lng;
    private Coordinates coordinates;
}