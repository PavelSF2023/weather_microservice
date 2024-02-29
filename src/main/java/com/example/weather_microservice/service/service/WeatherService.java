package com.example.weather_microservice.service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getTemperature(String latitude, String longitude) {
        final String apiKey = "fe6fe6a63b80cd1f3dc6c3f2fed7fb1c"; // API ключ от OpenWeatherMap
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" +
                longitude + "&appid=" + apiKey;

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode root = objectMapper.readTree(response.getBody());
                return root.at("/main/temp").asText();
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Error: Invalid API key. Please check your connection settings.");
            return null;
        } catch (HttpClientErrorException e) {
            System.out.println("Error: An error occurred while executing a request to the server. Error Status: ");
            return null;
        } catch (Exception e) {
            System.out.println("Error: An error occurred while executing a request to the server.");
            return null;
        }
        return null; // Возврат значения по умолчанию, если не удалось получить температуру
    }
}