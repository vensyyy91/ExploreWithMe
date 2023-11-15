package ru.practicum;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class EndpointHitMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EndpointHit fromDto(EndpointHitDto endpointHitDto) {
        Long id = endpointHitDto.getId();
        String app = endpointHitDto.getApp();
        String uri = endpointHitDto.getUri();
        String ip = endpointHitDto.getIp();
        LocalDateTime timestamp = LocalDateTime.parse(endpointHitDto.getTimestamp(), formatter);

        return new EndpointHit(id, app, uri, ip, timestamp);
    }

    public EndpointHitDto toDto(EndpointHit endpointHit) {
        Long id = endpointHit.getId();
        String app = endpointHit.getApp();
        String uri = endpointHit.getUri();
        String ip = endpointHit.getIp();
        String timestamp = endpointHit.getTimestamp().format(formatter);

        return new EndpointHitDto(id, app, uri, ip, timestamp);
    }
}