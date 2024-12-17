package com.quiromarck.eco_ruta.controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.quiromarck.eco_ruta.controllers.dto.UserDTO;
import com.quiromarck.eco_ruta.controllers.dto.mappers.UserMapper;
import com.quiromarck.eco_ruta.entity.User;
import com.quiromarck.eco_ruta.exception.UserNotFoundException;
import com.quiromarck.eco_ruta.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final FirebaseAuth firebaseAuth;

    @Autowired
    public UserController(UserService userService, FirebaseAuth firebaseAuth) {
        this.userService = userService;
        this.firebaseAuth = firebaseAuth;
    }

    @GetMapping("/listAllUsers")
    public ResponseEntity<List<User>> listAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/findById/{uid}")
    public ResponseEntity<ApiResponse> findUserById(@PathVariable String uid) {
        try {
            User user = userService.getUserById(uid).orElseThrow(
                    () -> new UserNotFoundException("Usuario no encontrado con uid: " + uid));
            UserDTO userDTO = UserMapper.toDto(user);
            ApiResponse response = new ApiResponse(HttpStatus.OK.value(), "User found.", userDTO);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            ApiResponse response = new ApiResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<ApiResponse> getUserByEmail(@RequestParam String email) {
        try {
            UserDTO user = userService.getUserByEmail(email);
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), user);
            return ResponseEntity.ok(apiResponse);
        } catch (UserNotFoundException e) {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        } catch (Exception e){
            ApiResponse apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

    @PutMapping("/update/{uid}")
    public ResponseEntity<?> updateUser(@PathVariable String uid, @RequestBody User user) {
        try {
            user.setUid(uid);
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with uid: " + uid);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{uid}")
    public ResponseEntity<?> deleteUser(@PathVariable String uid) {
        try {
            userService.deleteUser(uid);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with uid: " + uid);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user: " + e.getMessage());
        }
    }
}
