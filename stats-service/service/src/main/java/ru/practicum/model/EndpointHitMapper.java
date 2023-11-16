package ru.practicum.model;

import lombok.experimental.UtilityClass;
import ru.practicum.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@UtilityClass
public class EndpointHitMapper {
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EndpointHit fromDto(EndpointHitDto endpointHitDto) {
        try {
            return EndpointHit.builder()
                    .id(endpointHitDto.getId())
                    .app(endpointHitDto.getApp())
                    .uri(endpointHitDto.getUri())
                    .ip(endpointHitDto.getIp())
                    .timestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(), FORMATTER))
                    .build();
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Некорректный формат даты, укажите дату в формате yyyy-MM-dd HH:mm:ss");
        }
    }

    public EndpointHitDto toDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp().format(FORMATTER))
                .build();
    }
}