package ru.practicum;

import ru.practicum.user.NewUserRequest;
import ru.practicum.user.UserDto;

import java.util.Set;

public interface UserService {
    UserDto getUsers(Set<Integer> ids, int from, int size);

    UserDto addUser(NewUserRequest user);

    void deleteUser(long userId);
}
