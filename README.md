# 🧁 Pastelería 1000 Sabores - App Móvil Nativa

## Un E-Commerce moderno con Arquitectura de Microservicios

Esta es una aplicación móvil nativa de E-Commerce para la "Pastelería 1000 Sabores", desarrollada en **Kotlin** con **Jetpack Compose**. El proyecto ha evolucionado desde una arquitectura monolítica con base de datos local a un sistema distribuido y escalable que consume una suite de **microservicios desplegados en AWS**.

La app ofrece a los clientes una experiencia fluida para explorar productos, gestionar su perfil, realizar pedidos y visualizar la ubicación de la tienda, todo ello respaldado por una arquitectura robusta y moderna.

---

## ✨ Características Principales

*   ### Arquitectura de Microservicios
    *   **Autenticación (`ms-auth`):** Gestión completa de registro, inicio de sesión y sesión persistente a través de un servicio dedicado.
    *   **Catálogo (`ms-catalogo`):** El catálogo de productos y categorías se consume en tiempo real desde un microservicio, asegurando que los datos estén siempre actualizados.
    *   **Pedidos (`ms-pedidos`):** Permite a los usuarios consultar su historial de pedidos.

*   ### Integración con APIs Externas
    *   **Geolocalización con Google Maps:** Una sección dedicada muestra la ubicación física de la pastelería en un mapa interactivo, utilizando la **Google Maps Platform**. La API Key se gestiona de forma segura mediante `local.properties`.

*   ### Experiencia de Usuario Moderna
    *   **Catálogo Dinámico:** Navegación fluida por productos y categorías cargados desde la red.
    *   **Gestión de Perfil:** Los usuarios pueden registrarse, iniciar sesión y mantener su sesión activa.
    *   **Manejo de Errores de Red:** La interfaz reacciona a problemas de conexión, mostrando mensajes de error y ofreciendo opciones para reintentar la carga de datos.
    *   **UI Robusta:** Se han implementado `placeholders` para imágenes y se manejan elegantemente los datos nulos que puedan provenir del servidor.

*   ### Calidad y Testing
    *   **Tests Unitarios:** Pruebas para `ViewModels` y `Repositories` que aseguran la correcta lógica de negocio y el manejo de datos, ejecutándose en la JVM para máxima velocidad.
    *   **Tests de Instrumentación (UI):** Pruebas sobre componentes de Jetpack Compose (ej. `LoginScreen`) que verifican la interacción del usuario en un entorno de emulador o dispositivo real.

---

## 🛠️ Stack Tecnológico y Arquitectura

*   **Lenguaje:** **Kotlin** (100%)
*   **UI:** **Jetpack Compose** (declarativa, sin XML) con **Material Design 3**.
*   **Arquitectura:** **MVVM** (Model-View-ViewModel) con Patrón Repositorio por cada microservicio.
*   **Gestión de Estado:** `StateFlow` y `MutableStateFlow` para una UI reactiva y predecible.
*   **Navegación:** **Compose Navigation** para gestionar el flujo entre pantallas.
*   **Conectividad de Red:**
    *   **Retrofit:** Para realizar las llamadas a las APIs REST de los microservicios. Se han configurado **múltiples instancias** para consumir simultáneamente desde `ms-auth` y `ms-catalogo`.
    *   **OkHttp Logging Interceptor:** Para depurar las llamadas de red en tiempo de desarrollo.
*   **Asincronía:** **Coroutines de Kotlin** para gestionar las operaciones de red y de base de datos en segundo plano.
*   **Inyección de Dependencias:** Implementada de forma manual a nivel de la clase `Application` para proveer los repositorios.
*   **Carga de Imágenes:** **Coil** para cargar imágenes de productos desde URLs de manera eficiente.
*   **Pruebas:**
    *   `JUnit 4` para la estructura de los tests unitarios.
    *   `Compose Test Rule` para los tests de UI (instrumentación).
*   **Control de Versiones:** **Git** y **GitHub**.
