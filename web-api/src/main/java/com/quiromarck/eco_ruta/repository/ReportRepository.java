package com.quiromarck.eco_ruta.repository;

import com.quiromarck.eco_ruta.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByUserId(String userId);
}
