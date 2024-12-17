import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import './index.css';
import Login from './ui/Login';
import AdminUI from './ui/ADMINUI/AdminUI';
import Reportes from './ui/ADMINUI/Reportes';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/adminui" element={<AdminUI />} />
        <Route path="/reportes" element={<Reportes />} />

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </Router>
  );
}

export default App;
