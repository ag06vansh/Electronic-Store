package com.shopping.electronic.store.controller;

import com.shopping.electronic.store.dto.UserDto;
import com.shopping.electronic.store.service.FileService;
import com.shopping.electronic.store.service.UserService;
import com.shopping.electronic.store.util.ApiResponse;
import com.shopping.electronic.store.util.ImageResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@Tag(
        name = "User",
        description = "operations related to user profile management"
)
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    /**
     * Method to add new user profile
     *
     * @param userDto
     * @return
     */
    @Operation(summary = "add new user")
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody final UserDto userDto) {
        UserDto userDto1 = userService.createUser(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.OK);
    }

    /**
     * Method to update user profile details
     *
     * @param userId
     * @param userDto
     * @return
     */
    @Operation(summary = "update user")
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(value = "userId") final String userId,
                                              @Valid @RequestBody UserDto userDto) {
        UserDto userDto1 = userService.updateUser(userDto, userId);
        return new ResponseEntity<>(userDto1, HttpStatus.OK);
    }

    /**
     * Method to remove user profile
     *
     * @param userId
     * @return
     */
    @Operation(summary = "remove user")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable(value = "userId") final String userId) {
        String response = userService.deleteUser(userId);
        ApiResponse apiResponse = ApiResponse
                .builder()
                .message(response)
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * Method to fetch all users profile
     *
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */
    @Operation(summary = "get all users")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUser(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) final int pageNumber,
                                                    @RequestParam(value = "pageSize", defaultValue = "10", required = false) final int pageSize,
                                                    @RequestParam(value = "sortBy", defaultValue = "name", required = false) final String sortBy,
                                                    @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) final String sortDir
    ) {
        List<UserDto> userDtoList = userService.getAllUser(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    /**
     * Method to search user profile using userID
     *
     * @param userId
     * @return
     */
    @Operation(summary = "search user by userId")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(value = "userId") final String userId) {
        UserDto userDto = userService.getUserById(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * Method to search user profile using email
     *
     * @param email
     * @return
     */
    @Operation(summary = "search user by email")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable(value = "email") final String email) {
        UserDto userDto = userService.getUserByEmail(email);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * Method to search user profile
     *
     * @param keyword
     * @return
     */
    @Operation(summary = "search user by any keyword")
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable(value = "keyword") final String keyword) {
        List<UserDto> userDtoList = userService.searchUser(keyword);
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    /**
     * Method to upload user profile image
     *
     * @param userId
     * @param image
     * @return
     * @throws IOException
     */
    @Operation(summary = "upload user image")
    @PostMapping(value = "/image/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageResponse> uploadUserImage(@PathVariable("userId") final String userId,
                                                         @RequestParam("userImage") final MultipartFile image) throws IOException {
        String imageName = fileService.uploadFile(image, imageUploadPath);
        // saving image name with user data
        UserDto userDto = userService.getUserById(userId);
        userDto.setImageName(imageName);
        userService.updateUser(userDto, userId);

        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(imageName)
                .success(true)
                .message("User Image Uploaded Successfully !")
                .status(HttpStatus.CREATED)
                .build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    /**
     * Method to fetch user profile image
     *
     * @param userId
     * @param httpServletResponse
     * @throws IOException
     */
    @Operation(summary = "get user image")
    @GetMapping(value = "/image/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void getUserImage(@PathVariable("userId") final String userId,
                             HttpServletResponse httpServletResponse) throws IOException {
        UserDto userDto = userService.getUserById(userId);
        InputStream image = fileService.getResource(imageUploadPath, userDto.getImageName());
        httpServletResponse.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(image, httpServletResponse.getOutputStream());
    }
}
