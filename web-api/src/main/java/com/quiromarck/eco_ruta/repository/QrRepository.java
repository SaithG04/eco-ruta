package com.quiromarck.eco_ruta.repository;

import com.quiromarck.eco_ruta.entity.Qr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QrRepository extends JpaRepository<Qr, Long> {
    Optional<Qr> findByLocationId(Long locationId);
}
