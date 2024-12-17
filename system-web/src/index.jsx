import React from 'react';
import ReactDOM from 'react-dom/client';  // Asegúrate de importar desde 'react-dom/client'
import App from './App';

// Crear el root usando 'createRoot' (solo en React 18)
const root = ReactDOM.createRoot(document.getElementById('root'));  // Obtén el elemento 'root'

// Usar render() para montar el componente principal
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
