package ru.practicum.mark;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateMarkRequest {
    @Min(0)
    @Max(10)
    private Integer mark;
    @Size(min = 3, max = 1000, message = "Message length must be between 3 and 1000 characters")
    private String message;
}