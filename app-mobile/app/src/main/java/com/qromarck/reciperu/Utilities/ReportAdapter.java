package com.qromarck.reciperu.Utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.qromarck.reciperu.DTO.ReportDTO;
import com.qromarck.reciperu.R;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<ReportDTO> reportList;
    private Context context;

    // Constructor
    public ReportAdapter(Context context, List<ReportDTO> reportList) {
        this.context = context;
        this.reportList = reportList;
    }

    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflar el layout del item de reporte
        View view = LayoutInflater.from(context).inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportViewHolder holder, int position) {
        ReportDTO report = reportList.get(position);

        // Asignar los valores de los reportes a las vistas correspondientes
        holder.reportTitle.setText("Title");  // Asegúrate de que ReportDTO tenga un método getTitle()
        holder.reportDescription.setText(report.getDescription());  // Lo mismo para Description
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<ReportDTO> newReportList) {
        this.reportList = newReportList;
        notifyDataSetChanged();
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder {

        TextView reportTitle, reportDescription;

        public ReportViewHolder(View itemView) {
            super(itemView);
            reportTitle = itemView.findViewById(R.id.reportTitle);
            reportDescription = itemView.findViewById(R.id.reportDescription);
        }
    }
}
