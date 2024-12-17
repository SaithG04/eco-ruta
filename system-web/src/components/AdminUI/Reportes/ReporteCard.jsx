import React, { useState } from 'react';
import ReporteModal from './ReporteModal';  // Importamos el componente modal

function ReporteCard({ reporte }) {
  const [showModal, setShowModal] = useState(false); // Estado para controlar la visibilidad del modal

  // Mapeo de los estados en inglés a español
  const estadoEnEspanol = {
    'PENDING': 'Pendiente',
    'APPROVED': 'Aprobado',
    'REJECTED': 'Rechazado',
    'SOLVED': 'Solucionado',
  };

  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden">
      {/* Imagen del reporte */}
      <img 
        src={reporte.photo_url} 
        alt={`Reporte ${reporte.id}`} 
        className="w-full h-48 object-cover cursor-pointer" 
        onClick={() => setShowModal(true)} 
      />
      <div className="p-4">
        {/* Descripción corta */}
        <p className="font-semibold text-lg mb-2">{reporte.description}</p>

        {/* Fecha del reporte */}
        <p className="text-sm text-gray-500 mb-2">
          Fecha: {new Date(reporte.report_date).toLocaleString()}
        </p>

        {/* Estado del reporte */}
        <div className="mb-4">
          <span 
            className={`text-sm font-semibold ${reporte.status === 'PENDING' ? 'text-yellow-500' :reporte.status === 'SOLVED' ? 'text-green-500' : reporte.status === 'APPROVED' ? 'text-green-500' : 'text-red-500'}`}
          >
            Estado: {estadoEnEspanol[reporte.status] || 'Estado desconocido'}
          </span>
        </div>
      </div>

      {/* Mostrar modal si showModal es verdadero */}
      {showModal && <ReporteModal reporte={reporte} closeModal={() => setShowModal(false)} />}
    </div>
  );
}

export default ReporteCard;
