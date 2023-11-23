package ru.practicum.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.practicum.enums.StateActionAdmin;

@SuperBuilder
@Getter @Setter
@NoArgsConstructor
public class UpdateEventAdminRequest extends UpdateEventRequest {
    private StateActionAdmin stateAction;
}