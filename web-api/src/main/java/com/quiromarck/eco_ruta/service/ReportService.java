package com.quiromarck.eco_ruta.service;

import com.quiromarck.eco_ruta.controllers.dto.ReportDTO;
import com.quiromarck.eco_ruta.entity.Report;
import com.quiromarck.eco_ruta.entity.ReportStatus;

import java.util.List;

public interface ReportService {
    Report createReport(ReportDTO reportDTO);
    List<Report> getAllReports();
    Report getReportById(Long id);
    Report updateReportStatus(Long id, ReportStatus status);
    List<Report> getReportsByUserId(String userId);
}
