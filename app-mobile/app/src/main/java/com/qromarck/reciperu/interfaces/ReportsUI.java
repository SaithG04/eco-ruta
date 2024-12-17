package com.qromarck.reciperu.interfaces;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qromarck.reciperu.DTO.ReportDTO;
import com.qromarck.reciperu.R;
import com.qromarck.reciperu.Utilities.ErrorUtilities;
import com.qromarck.reciperu.Utilities.InterfacesUtilities;
import com.qromarck.reciperu.Utilities.ReportAdapter;
import com.qromarck.reciperu.services.ReportServiceImpl;

import java.util.List;

public class ReportsUI extends AppCompatActivity {

    private RecyclerView recyclerViewReports;
    private ReportAdapter reportAdapter;
    private ReportServiceImpl reportService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reports_ui);

        // Inicia el ReportServiceImpl
        reportService = new ReportServiceImpl(this);

        // Configurar el RecyclerView
        recyclerViewReports = findViewById(R.id.recyclerViewReports);
        recyclerViewReports.setLayoutManager(new LinearLayoutManager(this));

        // Obtener los reportes del usuario
        String userId = InterfacesUtilities.getLocalUser(this).getUid();
        getReportsByUserId(userId);
    }

    // Método para obtener los reportes por userId
    private void getReportsByUserId(String userId) {
        reportService.getReportsByUserId(userId, apiResponse -> {

            // Cast de la respuesta a una lista de ReportDTO
            List<ReportDTO> reportList = apiResponse.getData();
            // Si la respuesta es exitosa, obtener los reportes y actualizar el RecyclerView
            //ReportDTO reportDTO = ReportMapper.mapToDTO(apiResponse.getData(), ReportDTO.class);
            updateRecyclerView(reportList);
        }, e -> ErrorUtilities.validateExceptionType(ReportsUI.this, e));
    }

    // Actualizar el RecyclerView con los reportes obtenidos
    private void updateRecyclerView(List<ReportDTO> reportList) {
        // Si ya tienes un adaptador configurado, solo actualiza la lista
        if (reportAdapter == null) {
            // Crear un nuevo adaptador si aún no lo hemos inicializado
            reportAdapter = new ReportAdapter(this, reportList);
            recyclerViewReports.setAdapter(reportAdapter);
        } else {
            // Si el adaptador ya existe, solo actualiza la lista de datos
            reportAdapter.updateData(reportList);
        }
    }
}
