package com.shopping.electronic.store.service;

import com.shopping.electronic.store.dto.UserDto;
import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    List<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    UserDto getUserById(String userId);

    UserDto getUserByEmail(String email);

    List<UserDto> searchUser(String keyword);

    UserDto updateUser(UserDto userDto, String userId);

    String deleteUser(String userId);

}
