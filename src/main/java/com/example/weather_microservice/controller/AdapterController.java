package com.example.weather_microservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.example.weather_microservice.model.MessageA;
import com.example.weather_microservice.model.MessageB;
import com.example.weather_microservice.service.service.WeatherService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Api(value = "AdapterController", tags = {"Adapter API"})
@RestController
public class AdapterController {

    private final WeatherService weatherService;

    public AdapterController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @ApiOperation(value = "Processes a message", notes = "Processes a MessageA object and sends a message to Service B")
    @PostMapping("/processMessage")
    public void processMessage(@RequestBody MessageA messageA) {

        String message = messageA.getMsg();
        String latitude = messageA.getCoordinates().getLatitude();
        String longitude = messageA.getCoordinates().getLongitude();

        if (message == null || message.isEmpty()) {
            System.out.println("Error: Message text is missing");
            return;
        }

        if (!"ru".equals(messageA.getLng())) {
            System.out.println("Error: Messages with only the \"lng\" attribute: \"ru\"");
            return;
        }
        if (latitude == null || longitude == null || latitude.isEmpty() || longitude.isEmpty()) {
            System.out.println("Error: Latitude or longitude is empty!");
            return;
        }

        // Получаем данные о погоде на основе координат из сообщения
        String temperature = weatherService.getTemperature(latitude, longitude);

        // Создаем новое сообщение для передачи в Service B
        MessageB messageB = new MessageB();

        // Добавляем проверку на наличие температуры и обработку ошибок
        if (temperature != null && !temperature.isEmpty()) {
            double currentKelvin = Double.parseDouble(temperature);
            int currentCelsius = (int) (currentKelvin - 273.15);
            System.out.println("Temperature in Kelvin: " + currentKelvin);
            messageB.setCurrentTmp(currentCelsius);
        } else {
            System.out.println("Error: Temperature not available");
            return;
        }

        messageB.setTxt(messageA.getMsg());

        // Устанавливаем текущую дату и время
        messageB.setCreatedDt(formatDateToRFC3339());

        // Дополнительный код для отправки сообщения в Service B
        System.out.println("Sending a message to Service B: " + messageB);
    }

    //Метод для форматирования даты в формат RFC3339
    private String formatDateToRFC3339() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return currentDateTime.format(formatter);
    }
}

