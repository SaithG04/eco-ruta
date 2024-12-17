package com.quiromarck.eco_ruta.controllers;

import com.quiromarck.eco_ruta.entity.AppVersion;
import com.quiromarck.eco_ruta.exception.InvalidInputException;
import com.quiromarck.eco_ruta.exception.ResourceNotFoundException;
import com.quiromarck.eco_ruta.service.AppVersionService;
import com.quiromarck.eco_ruta.service.AppVersionServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/version")
public class AppVersionController {

    private final AppVersionService service;
    private final Logger logger = LogManager.getLogger(AppVersionController.class);

    @Autowired
    public AppVersionController(AppVersionServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/listVersions")
    public ResponseEntity<List<AppVersion>> listVersions() {
        logger.info("ListVersions endpoint was hit");
        List<AppVersion> versions = service.getAllVersion();
        return ResponseEntity.ok(versions);
    }

    /*@PostMapping("/saveNewVersion")
    public ResponseEntity<?> saveNewVersion(@RequestBody AppVersion appVersion) {
        try {
            service.saveNewVersion(appVersion);
            return ResponseEntity.ok(appVersion);  // 200 OK
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Error saving version: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.getMessage());  // 500 Internal Server Error
        }
    }*/

    @PutMapping("/updateVersion/{id}")
    public ResponseEntity<?> updateVersion(@PathVariable Integer id, @RequestBody AppVersion updatedVersion) {
        try {
            AppVersion existingVersion = service.getVersionById(id);

            if (updatedVersion.getVersion() != null) existingVersion.setVersion(updatedVersion.getVersion());
            if (updatedVersion.getApk_url() != null) existingVersion.setApk_url(updatedVersion.getApk_url());

            service.saveNewVersion(existingVersion);
            return ResponseEntity.ok(existingVersion);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Version not found with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating version: " + e.getMessage());
        }
    }

    /*@GetMapping("/findVersiontById/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        try {
            AppVersion appVersion = service.getVersionById(id);
            return ResponseEntity.ok(appVersion);
        } catch (InvalidInputException e) {
            // 400
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse.getMessage());
        } catch (ResourceNotFoundException e) {
            // 404
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse.getMessage());
        } catch (Exception e) {
            // 500
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.getMessage());
        }
    }*/

    @DeleteMapping("/deleteVersion/{id}")
    public ResponseEntity<?> deleteVersion(@PathVariable Integer id) {
        try {
            service.getVersionById(id);
            service.deleteVersionById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 204 No Content
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Version not found with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting version: " + e.getMessage());
        }
    }


}
