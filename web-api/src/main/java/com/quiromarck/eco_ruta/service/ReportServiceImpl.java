package com.quiromarck.eco_ruta.service;

import com.quiromarck.eco_ruta.controllers.dto.ReportDTO;
import com.quiromarck.eco_ruta.controllers.dto.mappers.ReportMapper;
import com.quiromarck.eco_ruta.entity.Report;
import com.quiromarck.eco_ruta.entity.ReportStatus;
import com.quiromarck.eco_ruta.exception.ReportNotFoundException;
import com.quiromarck.eco_ruta.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }


    public Report createReport(ReportDTO report) {
        Report newReport = ReportMapper.toEntity(report);
        return reportRepository.save(newReport);
    }

    @Override
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    @Override
    public Report getReportById(Long id) {
        return reportRepository.findById(id).orElseThrow(() -> new ReportNotFoundException("Report not found"));
    }

    @Override
    public Report updateReportStatus(Long id, ReportStatus status) {
        Report report = reportRepository.findById(id).orElseThrow(() -> new ReportNotFoundException("Report not found"));
        report.setStatus(status);
        return reportRepository.save(report);
    }

    @Override
    public List<Report> getReportsByUserId(String userId) {
        return reportRepository.findAllByUserId(userId);
    }
}
