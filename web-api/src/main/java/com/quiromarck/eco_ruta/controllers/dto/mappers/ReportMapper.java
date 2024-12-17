package com.quiromarck.eco_ruta.controllers.dto.mappers;

import com.quiromarck.eco_ruta.controllers.dto.ReportDTO;
import com.quiromarck.eco_ruta.entity.Report;
import com.quiromarck.eco_ruta.entity.ReportStatus;

public class ReportMapper {

    // Convertir de Report (Entidad) a ReportDTO
    public static ReportDTO toDTO(Report report) {
        if (report == null) {
            return null;
        }

        return new ReportDTO(
                report.getId(),
                report.getLatitude(),
                report.getLongitude(),
                report.getDescription(),
                report.getReportDate(),
                String.valueOf(report.getStatus()),
                report.getUserId(),
                report.getPhotoUrl()
        );
    }

    // Convertir de ReportDTO a Report (Entidad)
    public static Report toEntity(ReportDTO reportDTO) {
        if (reportDTO == null) {
            return null;
        }

        Report report = new Report();
        report.setId(reportDTO.getId());
        report.setLatitude(reportDTO.getLatitude());
        report.setLongitude(reportDTO.getLongitude());
        report.setDescription(reportDTO.getDescription());
        report.setReportDate(reportDTO.getReportDate());
        report.setStatus(ReportStatus.valueOf(reportDTO.getStatus()));
        report.setUserId(reportDTO.getUserId());
        report.setPhotoUrl(reportDTO.getPhotoUrl());

        return report;
    }
}