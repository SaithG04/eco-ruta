# EcoRuta

EcoRuta es una plataforma completa que permite la **optimización del manejo de residuos sólidos** mediante una **aplicación móvil, un sistema web administrativo** y un **backend robusto**. Su propósito principal es **reducir la contaminación urbana** al facilitar a los usuarios información en tiempo real y sistemas de incentivos.

---

## **Características Principales**

### 🌱 **Aplicación Móvil (Java)**  
- Visualización de **rutas y horarios** del camión de basura en **tiempo real**.  
- Sistema de **reportes de contaminación** mediante fotos y localización.  
- Recompensas mediante el sistema **EcoPoints**:
  - Escaneo de **códigos QR** de un solo uso (cuando el camión ha pasado).  
  - Puntos acumulables para futuros premios.

### 🛠️ **Sistema Web Administrativo (React)**  
- Gestión de usuarios, reportes y rutas.  
- Visualización de información clave en **tableros de control**.  
- Gestión de **reportes de contaminación** enviados por los usuarios.

### ☁️ **Backend (Spring Boot + Firebase)**  
- API REST para la gestión de rutas, reportes y usuarios.  
- Integración con **Firebase** para autenticación y almacenamiento.  
- Configuración robusta de controladores, entidades y servicios.

---

## **Tecnologías Utilizadas**

### **Backend**  
- Java + Spring Boot  
- Firebase (Base de datos, almacenamiento y autenticación)  
- API RESTful

### **Aplicación Móvil**  
- Lenguaje: Java  
- Herramientas: Firebase SDK, Android Studio  

### **Frontend**  
- React.js + Tailwind CSS  
- Componentes modulares y reutilizables  

---

## **Configuración del Proyecto**

### **Requisitos previos**
- **Node.js** y **npm** para el frontend.  
- **Java 21** y **Maven** para el backend.  
- **Android Studio** para la aplicación móvil.

---

### **Instalación**

1. **Backend (Spring Boot)**  
   ```bash
   cd web-api
   mvn clean install
   mvn spring-boot:run
   ```

2. **Frontend (React)**  
   ```bash
   cd SYSTEM-WEB
   npm install
   npm start
   ```

3. **Aplicación Móvil (Java)**  
   - Abre el proyecto en **Android Studio**.  
   - Configura las claves de Firebase en `google-services.json`.  
   - Ejecuta el emulador o dispositivo físico.


---

## **Uso**

1. **Usuarios**:  
   - Visualizan horarios y rutas del camión de basura.  
   - Reportan contaminación con fotos.  
   - Escanean códigos QR para ganar puntos.  

2. **Administradores**:  
   - Gestionan reportes y usuarios.  
   - Visualizan reportes y rutas activas.  

---

## **Contribución**

¡Las contribuciones son bienvenidas!  
1. Haz un **fork** del proyecto.  
2. Crea una **branch**: `git checkout -b feature/YourFeature`.  
3. Envía un **pull request**.

---

## **Licencia**

Este proyecto está bajo la **MIT License**.

---

## **Contacto**

- **Desarrolladores**: SaithG04, ThonyMarckDEV  
- **Correo**: qromarck2024@gmail.com
```
