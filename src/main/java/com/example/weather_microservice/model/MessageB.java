package com.example.weather_microservice.model;

import lombok.Data;

@Data
public class MessageB {
    private String txt;
    private String createdDt;
    private int currentTmp;
}

