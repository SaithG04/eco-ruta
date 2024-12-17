package com.quiromarck.eco_ruta.service;

import com.quiromarck.eco_ruta.entity.AppVersion;
import com.quiromarck.eco_ruta.exception.InvalidInputException;
import com.quiromarck.eco_ruta.exception.ResourceNotFoundException;
import com.quiromarck.eco_ruta.repository.AppVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppVersionServiceImpl implements AppVersionService {

    @Autowired
    private AppVersionRepository repo;

    @Override
    public List<AppVersion> getAllVersion() {
        return repo.findAll();
    }

    @Override
    public void saveNewVersion(AppVersion newVersion) {
        repo.save(newVersion);
    }

    @Override
    public AppVersion getVersionById(int id) {
        if (id <= 0) {
            throw new InvalidInputException("ID must be greater than zero.");
        }
        return repo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("No version found with id: " + id)
        );
    }
    @Override
    public void deleteVersionById(int id) {
        AppVersion versionToDelete = repo.findById(id).orElseThrow(
                () -> new RuntimeException("No version found with id: " + id)
        );
        repo.delete(versionToDelete);  // Eliminar la versión
    }
}
