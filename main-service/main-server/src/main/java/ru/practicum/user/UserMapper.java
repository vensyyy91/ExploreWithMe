package ru.practicum.user;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {
    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public UserShortDto toShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }

    public User fromDto(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }

    public User fromDto(UserShortDto usershortDto) {
        return User.builder()
                .id(usershortDto.getId())
                .name(usershortDto.getName())
                .build();
    }

    public User fromDto(NewUserRequest userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }
}