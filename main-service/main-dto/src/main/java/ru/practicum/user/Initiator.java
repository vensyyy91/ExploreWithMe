package ru.practicum.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Initiator {
    private Long id;
    private String name;
    private String email;
    private double rating;
}