package ru.practicum.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.practicum.enums.StateActionUser;

@SuperBuilder
@Getter @Setter
@NoArgsConstructor
public class UpdateEventUserRequest extends UpdateEventRequest {
    private StateActionUser stateAction;
}