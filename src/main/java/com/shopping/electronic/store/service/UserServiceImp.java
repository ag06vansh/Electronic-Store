package com.shopping.electronic.store.service;

import com.shopping.electronic.store.dto.UserDto;
import com.shopping.electronic.store.exception.ResourceNotFoundException;
import com.shopping.electronic.store.model.User;
import com.shopping.electronic.store.repository.UserRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImp implements UserService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Value("$user.profile.image.path")
    private String imagePath;

    @Override
    public UserDto createUser(UserDto userDto) {
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        User user = userRepository.save(modelMapper.map(userDto, User.class));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equals("ASC") ? Sort.by(sortBy) : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        List<User> userList = userRepository.findAll(pageable).toList();
        List<UserDto> userDtoList =
            userList
                .stream()
                .map(
                    user -> modelMapper.map(user, UserDto.class)
                )
                .collect(Collectors.toList());
        return userDtoList;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with given id."));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> userList = userRepository.findByNameContaining(keyword);
        List<UserDto> userDtoList =
            userList
                .stream()
                .map(
                    user -> modelMapper.map(user, UserDto.class)
                )
                .collect(Collectors.toList());
        return userDtoList;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with given id."));
        user.setAbout(userDto.getAbout());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public String deleteUser(String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with given id."));

        String fullImagePath = imagePath + user.getImageName();
        try {
            Path path = Paths.get(fullImagePath);
            Files.delete(path);
        } catch (IOException ex) {
            log.info("User image not found in folder.");
            ex.printStackTrace();
        }
        userRepository.delete(user);
        return "User deleted with given id";
    }
}
