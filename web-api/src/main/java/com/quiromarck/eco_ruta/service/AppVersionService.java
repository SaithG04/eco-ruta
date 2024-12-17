package com.quiromarck.eco_ruta.service;

import com.quiromarck.eco_ruta.entity.AppVersion;

import java.util.List;

public interface AppVersionService {
    List<AppVersion> getAllVersion();
    void saveNewVersion(AppVersion newVersion);
    AppVersion getVersionById(int id);
    void deleteVersionById(int id);
}
