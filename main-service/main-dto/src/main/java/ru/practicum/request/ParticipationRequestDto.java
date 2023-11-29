package ru.practicum.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.enums.Status;

@Data
@Builder
public class ParticipationRequestDto {
    private Long id;
    private String created;
    private Long event;
    private Long requester;
    private Status status;
}