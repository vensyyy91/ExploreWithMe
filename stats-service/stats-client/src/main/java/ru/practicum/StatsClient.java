package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class StatsClient {
    @Value("${stats-server.url}")
    private String serverUrl;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final RestTemplate rest = new RestTemplate();

    public ResponseEntity<EndpointHitDto> addHit(EndpointHitDto endpointHitDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        RequestEntity<EndpointHitDto> request = RequestEntity
                .method(HttpMethod.POST, serverUrl + "/hit")
                .headers(headers)
                .body(endpointHitDto);
        ResponseEntity<EndpointHitDto> response = rest.exchange(request, EndpointHitDto.class);

        return prepareGatewayResponse(response);
    }

    public ResponseEntity<List<ViewStats>> getStats(LocalDateTime start, LocalDateTime end, Set<String> uris, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start.format(FORMATTER),
                "end", end.format(FORMATTER),
                "unique", unique
        );
        StringBuilder path = new StringBuilder(serverUrl + "/stats?start={start}&end={end}&unique={unique}");
        if (uris != null && !uris.isEmpty()) {
            for (String uri : uris) {
                path.append("&uris=").append(uri);
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        RequestEntity<Void> request = RequestEntity
                .method(HttpMethod.GET, path.toString(), parameters)
                .headers(headers)
                .build();
        ResponseEntity<List<ViewStats>> response = rest.exchange(
                request,
                new ParameterizedTypeReference<List<ViewStats>>() {
                }
        );

        return prepareGatewayResponse(response);
    }

    private static <T> ResponseEntity<T> prepareGatewayResponse(ResponseEntity<T> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}