package com.dzhaparov.service.user;

import com.dzhaparov.dto.user.request.UserDtoRequest;
import com.dzhaparov.entity.user.User;
import com.dzhaparov.entity.user.UserMapper;
import com.dzhaparov.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper mapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(UserDtoRequest request) {
        logger.info("Trying to create user with email: {}", request.email());

        if (userRepository.findByEmail(request.email()).isPresent()) {
            logger.warn("User with email {} already exists", request.email());
            throw new IllegalArgumentException("User with this email already exists");
        }

        try {
            User user = mapper.toEntity(request);
            user.setHashed_password(passwordEncoder.encode(user.getHashed_password()));
            User saved = userRepository.save(user);

            logger.info("User successfully saved with ID: {}", saved.getId());
            return saved;

        } catch (Exception e) {
            logger.error("Error while saving user: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong while saving user");
        }
    }

    @Override
    public List<User> readAll() {
        return List.of();
    }

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public User updateById(Long id, UserDtoRequest request) {
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }
}
