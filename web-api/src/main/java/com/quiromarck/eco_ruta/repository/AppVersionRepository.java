package com.quiromarck.eco_ruta.repository;

import com.quiromarck.eco_ruta.entity.AppVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppVersionRepository extends JpaRepository<AppVersion, Integer> {
}
