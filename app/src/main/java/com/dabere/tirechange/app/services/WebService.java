package com.dabere.tirechange.app.services;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.dabere.tirechange.app.entities.Workshop;
import com.dabere.tirechange.app.exceptions.UnsupportedHttpRequestOperator;
import com.dabere.tirechange.app.models.BookingMessageResponse;

import reactor.core.publisher.Mono;

@Service
public class WebService {

    public String sendGetRequestForAppointments(Workshop workshop, String until, String from) {
        WebClient client = WebClient.builder()
                .baseUrl(workshop.getBaseUrl())
                .build();

        // TODO Make "from" and "until" parameter names be configurable from config
        // block. Check if they are even needed.
        String response = client.get()
                .uri(UriBuilder -> UriBuilder.path(workshop.getGetUrl()).queryParam("from", from)
                        .queryParam("until", until)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response;
    }

    public BookingMessageResponse bookFreeTireChangeTime(String id, String address, Workshop workshop) {
        WebClient client = WebClient.builder()
                .baseUrl(workshop.getBaseUrl())
                .build();

        String reservationHttpRequestUrl = workshop.getReservationHttpRequestUrl();
        String reservationHttpRequestType = workshop.getReservationHttpRequestType();
        String body = workshop.getReservationHttpRequestBody();
        String bodyFormat = workshop.getReservationHttpRequestBodyFormat();

        try {
            Mono<ResponseEntity<String>> response;
            if (reservationHttpRequestType.equals("POST")) {
                response = createRequest(client, reservationHttpRequestUrl, id, body, bodyFormat, HttpMethod.POST);
            } else if (reservationHttpRequestType.equals("PUT")) {
                response = createRequest(client, reservationHttpRequestUrl, id, body, bodyFormat, HttpMethod.PUT);
            } else {
                throw new UnsupportedHttpRequestOperator("Wrong HTTP request operator. Can only use PUT and POST.");
            }
            response.block();
            return new BookingMessageResponse("Broneering õnnestus.");
        } catch (WebClientResponseException ex) {
            // Handle WebClientResponseException and return error details
            switch (ex.getStatusCode().toString()) {
                case "422 UNPROCESSABLE_ENTITY":
                    
                    return new BookingMessageResponse("See aeg on võetud. Palun valige teine.");
                
                default:
                    return new BookingMessageResponse("Tekkis probleem töökojaga. Palun valige teine.");
            }
        }

    }

    private Mono<ResponseEntity<String>> createRequest(WebClient client, String url, String id, String body,
            String format, HttpMethod method) {
        return client.method(method)
                .uri(uriBuilder -> uriBuilder.path(url).build(id))
                .contentType(format.equals("JSON") ? MediaType.APPLICATION_JSON : MediaType.APPLICATION_XML)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleClientError)
                .onStatus(HttpStatusCode::is5xxServerError, this::handleServerError)
                .toEntity(String.class);
    }

    private Mono<? extends Throwable> handleClientError(ClientResponse response) {
        return response.bodyToMono(String.class).flatMap(body -> {
            return Mono.error(new WebClientResponseException(response.statusCode().value(), "Client error: " + body,
                    null, null, null));
        });
    }

    private Mono<? extends Throwable> handleServerError(ClientResponse response) {
        return response.bodyToMono(String.class).flatMap(body -> {
            return Mono.error(new WebClientResponseException(response.statusCode().value(), "Server error: " + body,
                    null, null, null));
        });
    }

}