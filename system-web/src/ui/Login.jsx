import React, { useState } from 'react';
import { signInWithEmailAndPassword } from 'firebase/auth';
import { auth } from '../js/firebase'; // Asegúrate de tener la configuración de Firebase
import API_BASE_URL from '../js/urlHelper'; // Importa la URL de tu API
import { AiFillEye, AiFillEyeInvisible } from 'react-icons/ai';
import LoadingScreen from '../components/home/LoadingScreen'; // Componente LoadingScreen
import ecorutalogo from '../img/ECORUTALOGO.jpeg'; // Logo de la empresa
import { motion } from 'framer-motion'; // Importa Framer Motion

function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false); // Estado para controlar la carga
  const [showPassword, setShowPassword] = useState(false);

  // Función para alternar la visibilidad de la contraseña
  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  const verifyEmailExistence = async (email) => {
    try {
      // Crear los parámetros de la URL con URLSearchParams
      const url = new URL(`${API_BASE_URL}api/v1/systemUsers/findByEmail`);
      url.search = new URLSearchParams({ email }).toString(); // Agregar el parámetro email a la URL
  
      // Realizar la solicitud GET con la URL que contiene los parámetros
      const response = await fetch(url, {
        method: 'GET',
        headers: {
          "ngrok-skip-browser-warning": "69420",
          "bypass-tunnel-reminder": "true" // O usa un encabezado personalizado
        }
      });
  
      // Verificar si la respuesta fue exitosa (status 200)
      if (!response.ok) {
        throw new Error('Correo no Encontrado.');
      }
  
      const data = await response.json(); // Parsear la respuesta JSON
  
      // Verificar si la respuesta contiene los datos esperados
      if (data.code === 200 && data.message === "System user found") {
        // Si el usuario fue encontrado, retornar true
        return true; // Usuario encontrado
      }
  
      // Si la respuesta tiene un código diferente o no se encuentra el usuario
      if (data.code === 404) {
        throw new Error('El correo no está registrado.');
      }
  
      throw new Error(data.message || 'Error inesperado.');
  
    } catch (error) {
      console.error('Error al verificar el correo:', error);
      setError(error.message); // Mostrar el mensaje de error en la UI
      return false; // Devolver false si hay algún error
    }
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    // Verificar primero si el correo existe en el backend
    const emailExists = await verifyEmailExistence(email);

    if (emailExists) {
      try {
        // Si el correo existe, intentar loguearse con Firebase
        const userCredential = await signInWithEmailAndPassword(auth, email, password);

        // Si el login es exitoso, redirigir a la página principal
        console.log(userCredential.user); // Imprimir el usuario autenticado
        window.location.href = '/adminUI'; // Redirigir a la página principal
      } catch (error) {
        setError('Credenciales incorrectas.'); // Manejar error de login en Firebase
        console.error('Error al intentar iniciar sesión:', error.message);
      }
    }

    setLoading(false); // Dejar de mostrar carga
  };

  return (
    <div className="flex justify-center items-center min-h-screen bg-gray-100 shadow-xl">
      <div className="bg-white p-8 sm:p-10 rounded-lg w-full max-w-4xl shadow-lg flex sm:flex-row flex-col items-center">
        
        <div className="w-full sm:w-1/2 h-full">
          <img
            src={ecorutalogo} 
            alt="Login Image"
          />
        </div>
    
        <div className="w-full sm:w-1/2 h-full flex flex-col justify-center">
          {loading && <LoadingScreen />} {/* Pantalla de carga */}
    
          <h2 className="text-3xl font-bold mb-6 text-center text-gray-800">Iniciar sesión</h2>
    
          <form onSubmit={handleLogin}>
            {/* Input de correo */}
            <div className="mb-6">
              <label htmlFor="email" className="block text-sm font-medium text-gray-600">Correo electrónico</label>
              <motion.input
                id="email"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="Escribe tu correo"
                required
                className="w-full p-4 mt-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 transition-all duration-300"
                whileFocus={{ borderColor: "#34d399" }} // Animación de borde verde al hacer foco
              />
            </div>
    
            {/* Input de contraseña */}
            <div className="mb-6 relative">
              <label htmlFor="password" className="block text-sm font-medium text-gray-600">Contraseña</label>
              <div className="relative">
                <motion.input
                  id="password"
                  type={showPassword ? 'text' : 'password'}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="Escribe tu contraseña"
                  required
                  className="w-full p-4 mt-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 pr-16 appearance-none transition-all duration-300"
                  whileFocus={{ borderColor: "#34d399" }} // Animación de borde verde al hacer foco
                />
                <button
                  type="button"
                  onClick={togglePasswordVisibility}
                  className="absolute top-[60%] right-4 transform -translate-y-1/2 text-gray-600 hover:text-gray-800 focus:outline-none"
                >
                  {showPassword ? <AiFillEyeInvisible size={28} /> : <AiFillEye size={28} />}
                </button>
              </div>
            </div>
    
            {/* Botón de iniciar sesión */}
            <motion.button
              type="submit"
              className="w-full py-3 bg-green-600 text-white rounded-md hover:bg-green-500 focus:outline-none focus:ring-2 focus:ring-green-500 transition-all"
              whileHover={{ scale: 1.05 }} // Efecto de escala al pasar el mouse
            >
              Iniciar sesión
            </motion.button>
          </form>
    
          {/* Mostrar error */}
          {error && <p className="mt-4 text-center text-red-500">{error}</p>}
        </div>
      </div>
    </div>
  );
}

export default Login;
