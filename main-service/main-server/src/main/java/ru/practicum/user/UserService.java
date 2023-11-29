package ru.practicum.user;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<UserDto> getUsers(Set<Long> ids, int from, int size);

    UserDto addUser(NewUserRequest newUserRequest);

    void deleteUser(long userId);
}