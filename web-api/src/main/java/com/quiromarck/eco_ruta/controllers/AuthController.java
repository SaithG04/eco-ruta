package com.quiromarck.eco_ruta.controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.quiromarck.eco_ruta.controllers.dto.DriverDTO;
import com.quiromarck.eco_ruta.controllers.dto.UserDTO;
import com.quiromarck.eco_ruta.controllers.dto.mappers.DriverMapper;
import com.quiromarck.eco_ruta.controllers.dto.mappers.UserMapper;
import com.quiromarck.eco_ruta.entity.Driver;
import com.quiromarck.eco_ruta.entity.User;
import com.quiromarck.eco_ruta.exception.DriverNotFoundException;
import com.quiromarck.eco_ruta.exception.InvalidInputException;
import com.quiromarck.eco_ruta.exception.UserNotFoundException;
import com.quiromarck.eco_ruta.outbounds.ReniecAPIClient;
import com.quiromarck.eco_ruta.service.DriverService;
import com.quiromarck.eco_ruta.service.FirebaseAuthService;
import com.quiromarck.eco_ruta.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final FirebaseAuth firebaseAuth;
    private final FirebaseAuthService firebaseAuthService;
    private final UserService userService;
    private final DriverService driverService;
    private final ReniecAPIClient reniecAPIClient;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(FirebaseAuth firebaseAuth, FirebaseAuthService firebaseAuthService, UserService userService, DriverService driverService , ReniecAPIClient reniecAPIClient) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseAuthService = firebaseAuthService;
        this.userService = userService;
        this.driverService = driverService;
        this.reniecAPIClient = reniecAPIClient;
    }

    @PostMapping("/validateDni")
    public ResponseEntity<ApiResponse> validateDni(@RequestParam String dni) {
        try {
            User userByDNI = userService.getUserByDNI(dni).orElse(null);
            if (userByDNI != null) {
                throw new InvalidInputException("User with DNI " + dni + " already exists");
            }

            Object client = reniecAPIClient.findByDNI(dni);
            ApiResponse response = new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), client);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (InvalidInputException e){
            // Crear el ApiResponse para error de validación
            ApiResponse response = new ApiResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);

            // Devolver la respuesta con estado HTTP 400 (Bad Request)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }catch (Exception e){
            // Crear el ApiResponse para error interno
            ApiResponse response = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);

            // Devolver la respuesta con estado HTTP 500 (Internal Server Error)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody UserDTO newUserDTO, @RequestParam String password) {
        try {
            // Validar el DTO antes de procesar
            userService.validateUserDTO(newUserDTO);

            String email = newUserDTO.getEmail();

            UserDTO userByEmail;
            try {
                userByEmail = userService.getUserByEmail(email);
            } catch (UserNotFoundException e) {
                userByEmail = null;
            }

            if (userByEmail != null) {
                throw new InvalidInputException("Email already in use");
            }

            // Crear el usuario en Firebase
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password)
                    .setEmailVerified(false);

            UserRecord userRecord = firebaseAuth.createUser(request);

            // Generar el enlace de verificación
            String verificationLink = firebaseAuth.generateEmailVerificationLink(email);

            // Enviar el enlace de verificación usando tu servicio de correo
            userService.sendVerificationEmail(email, verificationLink);
            System.out.println("Verification link sent to: " + email);

            newUserDTO.setUid(userRecord.getUid());

            // Usamos el servicio para persistir el usuario en la base de datos
            User userCreated = userService.createUser(newUserDTO);

            UserDTO userCreatedDTO = UserMapper.toDto(userCreated);

            // Crear el ApiResponse para éxito
            ApiResponse response = new ApiResponse(HttpStatus.OK.value(), "User registered successfully. Verification email sent.", userCreatedDTO);
            logger.debug("Link: {}", verificationLink);
            // Devolver la respuesta con estado HTTP 200 (OK)
            return ResponseEntity.ok(response);

        } catch (InvalidInputException e) {
            // Crear el ApiResponse para error de validación
            ApiResponse response = new ApiResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);

            // Devolver la respuesta con estado HTTP 400 (Bad Request)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            logger.error(e.getMessage());
            // Crear el ApiResponse para error interno
            ApiResponse response = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);

            // Devolver la respuesta con estado HTTP 500 (Internal Server Error)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody Map<String, String> request) {
        String idToken = request.get("idToken");
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String email = decodedToken.getEmail();
            String uid = decodedToken.getUid();
            boolean emailVerified = decodedToken.isEmailVerified();

            if (!emailVerified) {
                ApiResponse response = new ApiResponse(HttpStatus.UNAUTHORIZED.value(), "Please verify your email before logging in.", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(response);
            }

            // Diferenciar entre conductor y usuario normal
            if (email.endsWith("@municipalidad.pe")) {
                Driver driver = driverService.getDriverByUserId(uid)
                        .orElseThrow(() -> new DriverNotFoundException("Driver not found"));

                DriverDTO driverDTO = DriverMapper.toDto(driver);

                ApiResponse response = new ApiResponse(HttpStatus.OK.value(), "Driver authenticated", driverDTO);
                return ResponseEntity.ok(response);
            } else {
                UserDTO userByEmail = userService.getUserByEmail(email);
                ApiResponse response = new ApiResponse(HttpStatus.OK.value(), "User authenticated", userByEmail);
                return ResponseEntity.ok(response);
            }
        } catch (DriverNotFoundException | UserNotFoundException e) {
            ApiResponse response = new ApiResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (FirebaseAuthException e) {
            ApiResponse response = new ApiResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /*@PostMapping("/loginSystemUser")
    public ResponseEntity<ApiResponse> loginSystemUser(@RequestBody Map<String, String> request) {
        String idToken = request.get("idToken");
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String email = decodedToken.getEmail();
            String uid = decodedToken.getUid();
            boolean emailVerified = decodedToken.isEmailVerified();

            if (!emailVerified) {
                ApiResponse response = new ApiResponse(HttpStatus.UNAUTHORIZED.value(), "Please verify your email before logging in.", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(response);
            }

            // Diferenciar entre conductor y usuario normal
            if (email.endsWith("@municipalidad.pe")) {
                Driver driver = driverService.getDriverByUserId(uid)
                        .orElseThrow(() -> new DriverNotFoundException("Driver not found"));

                DriverDTO driverDTO = DriverMapper.toDto(driver);

                ApiResponse response = new ApiResponse(HttpStatus.OK.value(), "Driver authenticated", driverDTO);
                return ResponseEntity.ok(response);
            }else {
                UserDTO userByEmail = userService.getUserByEmail(email);
                ApiResponse response = new ApiResponse(HttpStatus.OK.value(), "User authenticated", userByEmail);
                return ResponseEntity.ok(response);
            }
        } catch (DriverNotFoundException | UserNotFoundException e){
            ApiResponse response = new ApiResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (FirebaseAuthException e) {
            ApiResponse response = new ApiResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e){
            ApiResponse response = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }*/

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logoutUser(@RequestParam String uid) {
        try {
            // Revoca los tokens de sesión del usuario
            FirebaseAuth.getInstance().revokeRefreshTokens(uid);

            ApiResponse response = new ApiResponse(HttpStatus.OK.value(), "User logged out successfully.", null);
            return ResponseEntity.ok(response);
        } catch (FirebaseAuthException e) {
            ApiResponse response = new ApiResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e){
            ApiResponse response = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}