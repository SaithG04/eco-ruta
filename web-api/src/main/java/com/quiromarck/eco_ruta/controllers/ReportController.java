package com.quiromarck.eco_ruta.controllers;

import com.quiromarck.eco_ruta.controllers.dto.ReportDTO;
import com.quiromarck.eco_ruta.controllers.dto.mappers.ReportMapper;
import com.quiromarck.eco_ruta.entity.ReportStatus;
import com.quiromarck.eco_ruta.exception.InvalidInputException;
import com.quiromarck.eco_ruta.exception.ReportNotFoundException;
import com.quiromarck.eco_ruta.service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Endpoint para crear un reporte
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createReport(@RequestBody ReportDTO reportDTO) {
        try {
            // Usamos el servicio para guardar el reporte
            ReportDTO savedReport = ReportMapper.toDTO(reportService.createReport(reportDTO));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(HttpStatus.CREATED.value(), "Report created successfully", savedReport));
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
        }
    }

    // Endpoint para obtener todos los reportes
    @GetMapping("/listAll")
    public ResponseEntity<ApiResponse> getAllReports() {
        try {
            List<ReportDTO> reports = new ArrayList<>();
            reportService.getAllReports().forEach(reportDTO -> reports.add(ReportMapper.toDTO(reportDTO)));
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(HttpStatus.OK.value(), "Reports fetched successfully", reports));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
        }
    }

    // Endpoint para obtener un reporte por su ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getReportById(@PathVariable("id") Long id) {
        try {
            ReportDTO reportDTO = ReportMapper.toDTO(reportService.getReportById(id));
            return ResponseEntity.status(HttpStatus.OK)
                        .body(new ApiResponse(HttpStatus.OK.value(), "Report fetched successfully", reportDTO));
        }catch (ReportNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
        }
    }

    // Endpoint para actualizar el estado del reporte
    @PutMapping("/{id}/updateStatus")
    public ResponseEntity<ApiResponse> updateReportStatus(@PathVariable("id") Long id, @RequestParam ReportStatus status) {
        try {
            ReportDTO dto = ReportMapper.toDTO(reportService.updateReportStatus(id, status));
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(HttpStatus.OK.value(), "Report updated successfully", dto));
        } catch (ReportNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
        }
    }

    // Endpoint para eliminar un reporte por su ID
    /*@DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteReport(@PathVariable("id") Long id) {
        try {
            boolean isDeleted = reportService.deleteReport(id);
            if (isDeleted) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ApiResponse(HttpStatus.OK.value(), "Report deleted successfully", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(HttpStatus.NOT_FOUND.value(), "Report not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null));
        }
    }*/

    @GetMapping("/listAllByUserId/{uid}")
    public ResponseEntity<ApiResponse> getAllByUserId(@PathVariable("uid") String uid) {
        try {
            List<ReportDTO> reports = new ArrayList<>();
            reportService.getReportsByUserId(uid).forEach(reportDTO -> reports.add(ReportMapper.toDTO(reportDTO)));
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(HttpStatus.OK.value(), "Reports fetched successfully", reports));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
        }
    }

}