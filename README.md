🧁 Pastelería 1000 Sabores - App Móvil (Kotlin/Compose)

Esta es una aplicación móvil de E-Commerce para la "Pastelería 1000 Sabores", desarrollada como proyecto para el curso de Desarrollo de Aplicaciones Móviles (DSY1105).

La app permite a los clientes navegar por el catálogo de productos, filtrar por categorías y precio, gestionar un carrito de compras, y crear una cuenta de usuario con una foto de perfil personalizada.

✨ Características Principales

Catálogo de Productos: Muestra los productos en una grilla adaptable que se carga desde una base de datos local.

Menú de Filtros: Un panel lateral deslizable (NavigationDrawer) permite filtrar productos por:

Categoría (Tortas, Postres, Sin Azúcar, etc.)

Rango de Precio (usando un Slider).

Sistema de Autenticación: Los usuarios pueden Registrarse (con validación de campos) e Iniciar Sesión. La sesión del usuario se mantiene en la app.

Carrito de Compras Persistente: Los usuarios pueden añadir, ver y modificar la cantidad de productos en su carrito. El carrito se guarda en la base de datos, por lo que no se pierde al cerrar la app.

Perfil de Usuario: Una vez conectado, el usuario puede ver su perfil y subir una foto seleccionándola desde la galería del dispositivo.

Flujo de Pago Protegido: El botón "Ir a Pagar" en el carrito comprueba si el usuario ha iniciado sesión. Si no lo ha hecho, lo redirige a la pantalla de Login.

🛠️ Tecnologías Utilizadas

Lenguaje: Kotlin

UI: Jetpack Compose (100% nativo, sin XML)

Diseño: Material Design 3

Arquitectura: MVVM (Model-View-ViewModel) + Patrón Repositorio.

Navegación: Compose Navigation

Base de Datos Local: Room (para persistencia de productos, usuarios y carrito)

Asincronía: Corutinas de Kotlin y StateFlow para la gestión de estado reactiva.

Carga de Imágenes: Coil (para cargar las fotos de perfil desde la galería).

Control de Versiones: Git y GitHub.