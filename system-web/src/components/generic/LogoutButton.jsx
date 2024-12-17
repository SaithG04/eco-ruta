// LogoutButton.jsx
import React from 'react';
import { signOut } from 'firebase/auth';
import { auth } from '../../js/firebase'; // Asegúrate de importar tu instancia de auth de Firebase

function LogoutButton() {
  const handleLogout = async () => {
    try {
      await signOut(auth);  // Desloguea al usuario
      window.location.href = '/'; // Redirige a la página de inicio
      console.log("Usuario deslogueado");
    } catch (error) {
      console.error("Error al cerrar sesión:", error.message);
    }
  };

  return (
    <button
      onClick={handleLogout}
      className="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-500 focus:outline-none"
    >
      Cerrar sesión
    </button>
  );
}

export default LogoutButton;
