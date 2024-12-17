import React, { useEffect, useState } from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import { Icon } from 'leaflet';
import 'leaflet/dist/leaflet.css';
import LoadingScreen from '../../home/LoadingScreen';  // Importa el componente de LoadingScreen
import API_BASE_URL from '../../../js/urlHelper';

function ReporteModal({ reporte, closeModal }) {
  const [position, setPosition] = useState(null);
  const [selectedStatus, setSelectedStatus] = useState(reporte.status);
  const [loading, setLoading] = useState(false);  // Estado para manejar el loading
  const [disabledStatuses, setDisabledStatuses] = useState([]); // Estado para manejar los checkboxes deshabilitados

  // Establecer la posición del mapa a las coordenadas del reporte
  useEffect(() => {
    if (reporte.latitude && reporte.longitude) {
      setPosition([reporte.latitude, reporte.longitude]);
    }
  }, [reporte]);

  // Crear un nuevo Icono personalizado
  const customIcon = new Icon({
    iconUrl: 'img/marker.png',  // Ruta de la imagen del marcador
    iconSize: [32, 32],  // Tamaño del ícono
    iconAnchor: [16, 32],  // Punto de anclaje del ícono
    popupAnchor: [0, -32],  // Punto donde se abrirá el popup
  });

  // Mapeo de estados en inglés a español
  const estadoEnEspanol = {
    'PENDING': 'Pendiente',
    'APPROVED': 'Aprobado',
    'REJECTED': 'Rechazado',
    'SOLVED': 'Solucionado',
  };

  // Función para manejar el cambio de estado
  const handleStatusChange = async (status) => {
    setSelectedStatus(status);
    setLoading(true);  // Iniciar el loading

    const allStatuses = ['PENDING', 'APPROVED', 'REJECTED', 'SOLVED'];
    const updatedDisabledStatuses = allStatuses.slice(0, allStatuses.indexOf(status) + 1);  // Deshabilitar el estado seleccionado y todos los anteriores

    setDisabledStatuses(updatedDisabledStatuses);  // Deshabilitar los estados anteriores

    // Realizar la solicitud API para cambiar el estado
    try {
      const response = await fetch(`${API_BASE_URL}api/v1/reports/${reporte.id}/updateStatus?status=${status}`, {
        method: 'PUT', // o 'POST' dependiendo de la API
        headers: {
          "ngrok-skip-browser-warning": "69420",
          "bypass-tunnel-reminder": "true" // Encabezado personalizado
        }
      });

      if (!response.ok) {
        throw new Error('Error al actualizar el estado');
      }

      console.log('Estado actualizado correctamente');
      // Recargar la página después de un cambio exitoso
      window.location.reload();  // Recarga la página para reflejar el cambio

    } catch (error) {
      console.error('Error en la solicitud:', error);
    } finally {
      setLoading(false);  // Terminar el loading
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-gray-500 bg-opacity-75">
      <div className="bg-white p-8 rounded-lg w-11/12 md:w-3/4 lg:w-2/3 xl:w-1/2 relative">
        {/* Botón de cierre dentro del modal */}
        <button
          onClick={closeModal}
          className="absolute top-4 right-4 text-xl font-bold text-gray-600"
        >
          X
        </button>
        
        <h3 className="text-xl font-semibold mb-6">Detalles del Reporte</h3>
        
        {/* Información del reporte */}
        <div className="mb-6">
          <p><strong>Descripción:</strong> {reporte.description}</p>
          <p><strong>Estado:</strong> <span>{estadoEnEspanol[reporte.status]}</span></p>
          <p><strong>Fecha de reporte:</strong> {new Date(reporte.report_date).toLocaleString()}</p>
        </div>

        {/* Mapa centrado en la latitud y longitud */}
        {position ? (
          <div className="w-full h-80 relative z-10"> {/* Asegúrate de que el z-index sea bajo en el mapa */}
            <MapContainer center={position} zoom={13} style={{ height: '100%', width: '100%' }}>
              <TileLayer
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
              />
              <Marker position={position} icon={customIcon}>
                <Popup>
                  {reporte.description}<br />{reporte.status}
                </Popup>
              </Marker>
            </MapContainer>
          </div>
        ) : (
          <p>No se puede mostrar el mapa, falta información de ubicación.</p>
        )}

        {/* Checkboxes para cambiar el estado */}
        <div className="mt-6">
          <h4 className="font-semibold text-lg">Cambiar estado:</h4>
          <div className="space-y-2">
            {['PENDING', 'APPROVED', 'REJECTED', 'SOLVED'].map((status) => (
              <label key={status} className="block">
                <input
                  type="checkbox"
                  checked={selectedStatus === status}
                  onChange={() => handleStatusChange(status)}
                  disabled={disabledStatuses.includes(status)}  // Deshabilitar el checkbox si el estado está bloqueado
                  className="mr-2"
                />
                {estadoEnEspanol[status]}
              </label>
            ))}
          </div>
        </div>

        {/* Mostrar loading cuando esté en progreso, asegurándonos de que esté encima del mapa */}
        {loading && (
          <div className="absolute top-0 left-0 right-0 bottom-0 z-50 bg-gray-500 bg-opacity-75 flex items-center justify-center">
            <LoadingScreen />
          </div>
        )}
      </div>
    </div>
  );
}

export default ReporteModal;
