# 🎵 Playlist

Una aplicación Android moderna desarrollada en Kotlin que muestra artistas, álbumes y canciones utilizando la API de Spotify.

## 🚀 Características

- **Arquitectura Moderna**: MVVM + Clean Architecture
- **UI Declarativa**: Jetpack Compose con Material Design 3
- **Asincronía**: Corrutinas y Flow
- **Navegación**: Navegación entre artistas, álbumes y canciones
- **Estados**: Manejo de estados (Loading, Success, Error)
- **Spotify API**: Integración completa con la API de Spotify

## 📋 Prerrequisitos

- Android Studio Hedgehog o superior
- Emulador Android con API 28+
- Cuenta de desarrollador en [Spotify Developer](https://developer.spotify.com/)

## ⚙️ Configuración

### 1. Obtener Credenciales de Spotify

1. Ve a [Spotify Developer Dashboard](https://developer.spotify.com/dashboard)
2. Crea una nueva aplicación
3. Copia tu **Client ID** y **Client Secret**

### 2. Configurar Variables de Entorno

1. Crea o edita el archivo `local.properties` en la raíz del proyecto
2. Agrega las siguientes líneas con tus credenciales:

```properties
SPOTIFY_CLIENT_ID=tu_client_id_aqui
SPOTIFY_CLIENT_SECRET=tu_client_secret_aqui
```
⚠️ Importante: Nunca commits el archivo local.properties ya que contiene información sensible.

### 3. Ejecutar la Aplicación

- Abre el proyecto en Android Studio
- Sincroniza el proyecto con Gradle (File → Sync Project with Gradle Files)
- Ejecuta en un emulador o dispositivo físico (Run → Run 'app')

## 🏗️ Arquitectura

La aplicación sigue **Clean Architecture** con **MVVM**:

```text
app/
├── data/                           # Capa de Datos
│   ├── remote/                     # Servicios y DTOs de API
│   ├── repository/                 # Implementaciones de Repository
│   └── mapper/                     # Mappers entre capas
├── domain/                         # Capa de Dominio
│   ├── model/                      # Entidades de dominio
│   ├── repository/                 # Interfaces de Repository
│   └── usercase/                   # Casos de uso
├── ui/                             # Capa de Presentación
│   ├── screens/                    # Pantallas Composable
│   │   ├── artists/                # Pantalla de artistas
│   │   ├── albums/                 # Pantalla de álbumes
│   │   └── tracks/                 # Pantalla de canciones
│   ├── navigation/                 # Navegación de la app
│   └── theme/                      # Temas y estilos
└── di/                             # Inyección de Dependencias
```

## 📱 Pantallas

- **🎤 Artistas**: Listado de artistas con búsqueda integrada
- **💿 Álbumes**: Álbumes del artista seleccionado
- **🎵 Canciones**: Canciones del álbum seleccionado
- **⏳ Loading**: Pantalla de carga inicial elegante

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Kotlin
- **UI**: Jetpack Compose, Material Design 3
- **Arquitectura**: MVVM, Clean Architecture
- **Asincronía**: Corrutinas, Flow
- **Networking**: Retrofit + Gson
- **Imágenes**: Coil Compose
- **Navegación**: Navigation Compose
- **HTTP Client**: OkHttp con Logging Interceptor
- **Build System**: Gradle con Version Catalog

## 📊 Especificaciones Técnicas

```gradle
compileSdk = 36
minSdk = 28
targetSdk = 36

Kotlin = 1.9.0+
Jetpack Compose = BOM
Material3 = 1.2.0+
```

## 🔧 Dependencias Principales

- `androidx.compose.material3` - Material Design 3
- `androidx.navigation.compose` - Navegación
- `coil-compose` - Carga de imágenes
- `retrofit` - Cliente HTTP
- `kotlinx-coroutines-android` - Corrutinas
- `okhttp-logging` - Logging de HTTP

## 🎯 Flujo de la Aplicación

1. **Pantalla de Loading** → Muestra durante la inicialización
2. **Lista de Artistas** → Búsqueda y selección de artistas
3. **Álbumes del Artista** → Lista de álbumes al seleccionar artista
4. **Canciones del Álbum** → Lista de canciones al seleccionar álbum

## 🧪 Testing

La aplicación incluye configuración para testing:

- `testImplementation` - Pruebas unitarias
- `androidTestImplementation` - Pruebas de instrumentación
- Pruebas de UI para componentes Compose

## 👨‍💻 Desarrollo

### Estructura de Módulos

```kotlin
// Ejemplo de estructura de paquetes
romero.alvaro.playlist.ui.screens.artists
romero.alvaro.playlist.ui.screens.artists.viewmodel
romero.alvaro.playlist.ui.screens.artists.components
romero.alvaro.playlist.domain.repository
romero.alvaro.playlist.data.remote.service
```

## 👨‍💻 Patrones Utilizados

- **Repository Pattern** - Abstracción de fuentes de datos
- **Use Cases** - Lógica de negocio reutilizable
- **State Management** - ViewModel con StateFlow
- **UI State** - Sealed classes para estados de UI
- **Mapper Pattern** - Transformación entre DTOs y Entities

## 🚀 Compilación y Distribución

```bash
# Compilar en modo debug
./gradlew assembleDebug

# Ejecutar pruebas
./gradlew test
./gradlew connectedAndroidTest
```

## ❓ Solución de Problemas

### Error: "Could not find SPOTIFY_CLIENT_ID"
- Verifica que el archivo `local.properties` existe
- Confirma que las variables están escritas correctamente
- Sincroniza el proyecto después de modificar `local.properties`

### Error de conexión con Spotify API
- Verifica que las credenciales sean correctas
- Confirma que la app esté activa en Spotify Developer Dashboard
- Revisa los logs de OkHttp para detalles del error

## 📄 Licencia

Este proyecto fue desarrollado como parte de una prueba técnica para Android Developer.

## 🤝 Contribución

Para cualquier mejora o sugerencia, por favor abre un issue o pull request.

---

**Desarrollado con ❤️ usando Kotlin y Jetpack Compose**