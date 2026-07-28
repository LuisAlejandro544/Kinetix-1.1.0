# 📱 Kinetix

Una aplicación moderna y potente para Android desarrollada en **Kotlin** y **Jetpack Compose** que permite a los usuarios automatizar tareas cotidianas mediante flujos de acciones secuenciales personalizados. Diseñada bajo la filosofía de "Siri Shortcuts" o "Tasker", pero con una interfaz intuitiva, limpia y amigable para el usuario común.

---

## ✨ Características Principales

- **⚙️ Flujo Secuencial de Acciones**: Las automatizaciones ejecutan una secuencia de pasos ordenados donde el resultado de una acción se puede pasar automáticamente como entrada del siguiente paso mediante el comodín `${input}`.
- **🔋 Acceso a Sensores y Datos**: Acciones integradas para consultar el nivel de batería, modelo del dispositivo, entrada manual del usuario, operaciones matemáticas avanzadas, voz sintética (TTS) con selector de motores y voces locales, alertas en pantalla, notificaciones del sistema y la opción de compartir texto de forma nativa.
- **🎧 Selector de Voces y Previsualización de Audio**: Permite elegir motores locales (TTS) y voces instaladas en el dispositivo de forma interactiva desde el editor, además de escuchar una previsualización de la voz de síntesis (TTS) y del sonido de notificación antes de guardar la automatización.
- **⚡ Automatizaciones por Eventos del Sistema**: Configuración de disparadores automáticos avanzados que se activan según el nivel de batería actual (igual, caída por debajo o subida por encima del porcentaje que elija el usuario), conexión/desconexión del cargador (corriente eléctrica), o conexión/desconexión de auriculares con cable, usando broadcast listeners nativos.
- **🔄 Servicio en Primer Plano y Arranque Automático**: Un servicio Foreground persistente (`KinetixForegroundService`) y receptor de inicio de sistema (`BootCompletedReceiver`) que garantizan la ejecución continua 24/7 de todos los disparadores en segundo plano, incluso si la app se elimina de la lista de aplicaciones recientes o se reinicia el teléfono.
- **📁 Almacenamiento Estructurado de Logs**: Guarda registros detallados ante fallos imprevistos o advertencias en la memoria física del dispositivo bajo la ruta estándar de la app (`Android/data/com.kinetix/files/crashes` y `Android/data/com.kinetix/files/warnings`). Esto permite al usuario acceder a una consola dedicada de diagnóstico en la sección de configuraciones para copiar o exportar los reportes de manera local.
- **🛠️ Identidad Pre-Alpha Pública**: El ícono del lanzador (`ic_launcher_foreground.xml`) y la pantalla de bienvenida incorporan un distintivo indicador "Pre-Alpha". Esto prepara a la aplicación para una distribución inicial transparente, informando explícitamente a los probadores que los registros del sistema sirven para que el creador resuelva fallos en su dispositivo móvil específico.
- **⚙️ Panel de Configuraciones Avanzado**: Sección dedicada con toggle para reducir la calidad gráfica (desactiva degradados pesados y cargas de imágenes de fondo para máxima fluidez en dispositivos más antiguos) y acceso en vivo a la consola de logs, con herramientas de copiado rápido al portapapeles y compartición genérica.
- **📁 Gestión de Archivos local**: Acciones integradas para escribir, leer y añadir información al final de archivos de texto en el almacenamiento privado de la aplicación para crear registros, bitácoras o notas automatizadas.
- **☀️ Control del Sistema**: Ajuste directo del volumen independiente por canales y brillo de la pantalla con solicitud de permisos del sistema interactiva.
- **♿ Servicios de Accesibilidad**: Integración de un servicio de accesibilidad propio para realizar gestiones del sistema avanzadas de manera 100% nativa (Atrás, Inicio, Mostrar Notificaciones, Ajustes Rápidos, Menú de Apagado y Bloqueo de Pantalla).
- **🔊 Reproducción de Sonidos (Estilo iPhone)**: Nueva acción que permite emitir de forma inmediata y sin retrasos sonidos de notificación usando el motor de audio nativo **SoundPool**, incluyendo el timbre premium exclusivo **Kinetix Chime ✨** y el sonido retro arcade **Level Up 🎮** para simular la experiencia premium de atajos de iOS.
- **💻 Motor JavaScript (QuickJS) Embebido**: Ejecución interactiva de scripts JavaScript directamente desde tus automatizaciones de manera nativa, rápida y ultra segura.
- **⌨️ Integración Avanzada con Termux**: Permite a los usuarios avanzados ejecutar scripts y comandos avanzados directamente en la terminal de Termux mediante Intents en segundo plano.
- **⚙️ Creador de Acciones con Código Personalizado**: Un bloque lógico que permite usar comandos nativos (SET, PRINT, RETURN, TTS, UPPERCASE) para crear lógica compleja e interactiva de forma nativa.
- **📸 Captura de Cámara en Segundo Plano**: Captura fotos de manera silenciosa usando la cámara frontal o trasera sin levantar la interfaz de usuario, guardándolas localmente en caché y devolviendo su ruta física para su uso posterior.
- **🎮 Simulación de Gestos Automáticos (Tap & Swipe)**: Simulación precisa de toques y desplazamientos (swipe) en coordenadas específicas de la pantalla aprovechando el servicio de accesibilidad nativo.
- **📋 Portapapeles Silencioso**: Lee datos o escribe información directamente en el portapapeles del sistema de manera silenciosa para pasar texto dinámico a otras aplicaciones.
- **🌐 Peticiones HTTP y Webhooks**: Acción `HTTP_REQUEST` que permite realizar solicitudes REST (`GET`, `POST`, `PUT`, `DELETE`, `PATCH`) con cabeceras personalizadas y cuerpo dinámico con `{resultado}` / `${input}` para integraciones web y domótica.
- **⏰ Programación por Horario (Cron Local)**: Automatización que permite ejecutar un atajo de manera automática a una hora programada exacta y días seleccionados de la semana (`ScheduleTriggerManager` & `AlarmManager`).
- **🖐️ Reordenamiento Drag-and-Drop**: Tirador táctil `DragHandle` en cada tarjeta de acción del editor que permite reordenar el flujo deslizándolo verticalmente con gestos intuitivos.
- **✨ Pantalla de Bienvenida (Onboarding)**: Un flujo interactivo de bienvenida que narra el propósito de la app, con un logo adaptivo en capas similar al diseño icónico de atajos de iOS y un panel de permisos modular donde el usuario otorga control por separado (Notificaciones, Cámara y Accesibilidad) sin imposiciones.
- **🖼️ Automatizaciones Personalizadas**: Opción de seleccionar cualquier imagen de la galería de fotos del usuario para establecerla como fondo visual de la tarjeta de la automatización con un filtro oscuro translúcido protector de alto contraste.
- **⚡ Ejecución no intrusiva**: El progreso se visualiza a través de una elegante cápsula de estado flotante al estilo *Dynamic Island / Capsule* en la parte inferior de la pantalla, que permite abrir la consola detallada de ejecución si se desea, o continuar navegando sin interrupciones.
- **💾 Persistencia de Datos**: Las automatizaciones y sus flujos de acciones se guardan localmente en una base de datos segura gestionada por **Room** con soporte para operaciones CRUD asíncronas mediante Kotlin Coroutines y Flow.
- **🎨 Interfaz Material 3 Premium**: Diseñada con una paleta de colores vibrantes, esquinas redondeadas modernas, animaciones fluidas y controles de accesibilidad.

---

## 🛠️ Stack Tecnológico

- **Lenguaje**: [Kotlin](https://kotlinlang.org/) (100% nativo)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/compose) con Material Design 3 (M3)
- **Base de Datos**: [Room](https://developer.android.com/training/data-storage/room) con SQLite para persistencia local
- **Arquitectura**: MVVM (Model-View-ViewModel) con Clean Architecture simplificada
- **Asincronía**: Kotlin Coroutines & Flow (programación reactiva y asíncrona)
- **Speech Engine**: Android TextToSpeech (TTS) nativo
- **Motores de Scripting**: [QuickJS](https://github.com/cashapp/quickjs-android) (JavaScript) embebido totalmente integrado y funcional para ejecución nativa de JS, integración con Termux por comandos, y motor sintáctico personalizado para el bloque de código del usuario.
- **Automatización de Hardware & Sistema**: Gestión inteligente de volumen de canales independientes, control del perfil de sonido (Silencio, Vibración, Timbre), control de flujos condicionales y disparadores de apertura programada de apps.

---

## 📂 Estructura del Proyecto

Para una descripción detallada de la arquitectura y la distribución de carpetas del proyecto, consulta el archivo [STRUCTURE.md](./STRUCTURE.md).

Breve resumen de la estructura de código fuente:
- `com.example.data`: Entidades del modelo (`Shortcut`, `ActionData`), DAO, Base de datos y Repositorio de Room.
- `com.example.executor`: Motor de ejecución (`ShortcutExecutor`) y mapeadores de lógica de acciones secuenciales.
- `com.example.ui`: ViewModel central para la gestión del estado de la aplicación.
- `com.example.ui.screens`: Pantallas principales (`ShortcutListScreen`, `ShortcutEditorScreen`).
- `com.example.ui.theme`: Configuración del tema visual y Material Design 3.

---

## 🚀 Instalación y Guía de Uso Rápido

### Requisitos Previos
- **Android Studio** (Koala o superior recomendado)
- **JDK 17** o superior configurado
- Dispositivo Android físico o emulador con **Android 8.0 (API 26)** o superior

### Clonación y Configuración
1. Clona el repositorio:
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd <NOMBRE_DEL_REPOSITORIO>
   ```
2. Abre el proyecto en Android Studio como un proyecto de Gradle existente.
3. Deja que Android Studio sincronice las dependencias del archivo `build.gradle.kts`.

### Ejecución de Pruebas
Para ejecutar el conjunto de pruebas unitarias y de integración locales:
```bash
gradle :app:testDebugUnitTest
```

---

## ⚙️ Variables de Entorno y Configuración

El proyecto utiliza variables de entorno declaradas a través de archivos `.env`. Si se integran APIs adicionales (como Gemini API para automatización con IA), configura las credenciales en el archivo `.env` en la raíz del proyecto.

| Variable | Descripción | Requerido | Ejemplo |
|:---|:---|:---:|:---|
| `GEMINI_API_KEY` | Llave para el motor de Inteligencia Artificial (opcional) | No | `AIzaSyD-xxx...` |

---

## 📜 Licencia y Contribuciones

- **Licencia**: Este proyecto está protegido bajo la **PolyForm Noncommercial License 1.0.0** (ver archivo [`LICENSE`](./LICENSE)). Queda estrictamente prohibido cualquier uso, distribución o derivación con fines comerciales o de lucro financiero.
- **Contribuciones de Terceros**: **No se aceptan contribuciones ni Pull Requests de terceros**. El desarrollo, evolución y mantenimiento de este software se realizan de forma totalmente privada e individual por el autor original. Para conocer la planificación interna del proyecto, consulta el documento [ROADMAP.md](./ROADMAP.md).
