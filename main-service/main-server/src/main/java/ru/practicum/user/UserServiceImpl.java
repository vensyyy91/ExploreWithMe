package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(Set<Long> ids, int from, int size) {
        List<User> users = ids != null && !ids.isEmpty()
                ? userRepository.findAllById(ids)
                : userRepository.findAll(PageRequest.of(from / size, size)).getContent();
        log.info("Возвращен список объектов: {}", users);

        return users.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto addUser(NewUserRequest newUserRequest) {
        User user = UserMapper.fromDto(newUserRequest);
        User newUser = userRepository.save(user);
        log.info("Добавлен новый пользователь: {}", newUser);

        return UserMapper.toDto(newUser);
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        userRepository.deleteById(userId);
        log.info("Удален пользователь с id={}", userId);
    }
}