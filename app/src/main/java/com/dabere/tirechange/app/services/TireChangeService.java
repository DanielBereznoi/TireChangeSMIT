package com.dabere.tirechange.app.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dabere.tirechange.app.entities.WorkshopConfig;
import com.dabere.tirechange.models.WorkshopModel;

@Service
public class TireChangeService {
    private List<WorkshopConfig> configs;

    public void getAllAvailableTimes() {
        if (configs == null) {
            mapWorkshops();
        }
        String currentDate = java.time.LocalDate.now().toString();
        String endDate = "2040-01-01";
        HashMap<String, WorkshopModel> modelMap = new HashMap<>();
        configs.forEach(w -> modelMap.put(w.getBaseUrl(),
                new WorkshopModel(w.getName(), w.getAddress(), w.getVehicleTypes(), new ArrayList<>())));
        modelMap.keySet().forEach(k -> findTimes(k, modelMap.get(k), currentDate, endDate));
    }

    public void findTimes(String url, WorkshopModel model, String from, String until) {
        WebClient client = WebClient.builder()
                .baseUrl(url)
                .build();
        System.out.println(client.get()
                .uri(UriBuilder -> UriBuilder.path("/available").queryParam("from", from).queryParam("until", until)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block());
        //client.get()
        //        .uri(UriBuilder -> UriBuilder.path("/available").queryParam("from", from).queryParam("until", until)
        //                .build())
        //        .retrieve()
        //        .bodyToMono(String.class)
        //        .block();

    }

    public void mapWorkshops() {
        try {
            this.configs = WorkshopsReader.main(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
