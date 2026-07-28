# 📂 Estructura y Arquitectura del Proyecto - Kinetix

Este documento explica de forma clara la organización del código fuente, las decisiones arquitectónicas tomadas y el rol que juega cada directorio y archivo en el sistema de automatizaciones.

---

## 🏛️ Decisiones de Arquitectura

El proyecto adopta un patrón **MVVM (Model-View-ViewModel)** limpio y adaptado para Jetpack Compose, con las siguientes responsabilidades diferenciadas:

1. **Capa de Datos (Data Layer)**:
   - Contiene la definición de los modelos que se guardan en la base de datos y se serializan en JSON (`Shortcut`, `ActionData`).
   - Usa **Room** para la persistencia local robusta con SQLite.
   - Proporciona un repositorio unificado (`ShortcutRepository`) que actúa como la única fuente de verdad para el ViewModel.

2. **Capa de Ejecución (Execution Layer)**:
   - Totalmente independiente de la UI. El motor `ShortcutExecutor` gestiona el procesamiento asíncrono y secuencial de la lista de acciones de un atajo, controlando la inyección de la variable `{entrada_atajo}`.
   - Desacoplado de implementaciones específicas mediante el patrón **Strategy**, ahora segmentado en archivos individuales por acción.

3. **Capa de Interfaz de Usuario (UI Layer)**:
   - El `ShortcutViewModel` mantiene y expone el estado de la aplicación mediante `MutableStateFlow` y variables reactivas.
   - Las pantallas en Jetpack Compose observan este estado y lo renderizan de forma reactiva.
   - **Navegación Modular**: Se ha extraído toda la lógica de transiciones, enrutamiento y gestión de pantallas de `MainActivity.kt` a un componente dedicado de navegación (`AppNavigation.kt`).

---

## 🗂️ Árbol de Carpetas del Proyecto

A continuación, se detalla la distribución de los componentes clave dentro del módulo `/app`:

```
/app
├── src/main
│   ├── AndroidManifest.xml                  # Permisos (Vibrate, Notifications, Camera, Write Settings), actividades y entrada de la app.
│   ├── java/com/example
│   │   ├── MainActivity.kt                  # Actividad principal simplificada; inicia la aplicación y delega a AppNavigation.
│   │   │
│   │   ├── data/                            # --- CAPA DE DATOS ---
│   │   │   ├── ActionType.kt                # Enumeración con los tipos de acción soportados, etiquetas, íconos y valores predeterminados.
│   │   │   ├── ActionCategory.kt            # Categorización jerárquica de acciones (Audio/Visual, Sistema, Archivos, Lógica, Power Android, Developer).
│   │   │   ├── Shortcut.kt                  # Entidad Room que representa un atajo (incluye imagen de fondo y disparador por batería).
│   │   │   ├── ShortcutConverters.kt        # Convertidores de Room para serializar/deserializar listas de acciones en JSON.
│   │   │   ├── ShortcutDao.kt               # Interfaz DAO con las consultas SQL a la base de datos de Room.
│   │   │   ├── ShortcutDatabase.kt          # Inicializador Singleton de la base de datos Room de SQLite con fallback destructivo.
│   │   │   └── ShortcutRepository.kt        # Repositorio que unifica el acceso a datos.
│   │   │
│   │   ├── executor/                        # --- MOTOR DE AUTOMATIZACIONES (NÚCLEO) ---
│   │   │   ├── ShortcutExecutor.kt          # Clase core que orquesta la ejecución secuencial de las acciones resolviendo variables de entrada.
│   │   │   ├── VariableResolver.kt          # Helper modular para resolver y validar la interpolación de variables en parámetros.
│   │   │   ├── ActionStrategy.kt            # Interfaz base del Patrón Strategy para la ejecución de acciones individuales.
│   │   │   ├── ActionStrategyRegistry.kt    # Registrador central unificado que expone todas las estrategias disponibles a nivel global.
│   │   │   ├── KinetixForegroundService.kt  # Servicio Foreground persistente con notificación en segundo plano 24/7.
│   │   │   ├── BootCompletedReceiver.kt     # Receptor estático para arranque de sistema (ACTION_BOOT_COMPLETED).
│   │   │   ├── ShortcutAccessibilityService.kt # Servicio de accesibilidad para la inyección de comandos del sistema y gestos táctiles.
│   │   │   ├── ShortcutExecutionCallbacks.kt# Interfaz desacoplada para redirigir eventos de progreso, diálogos asíncronos y consola flotante.
│   │   │   ├── SystemTriggerManager.kt      # Receptor de batería, cargador y auriculares adaptado para APIs de Android modernas.
│   │   │   ├── ScheduleTriggerManager.kt    # Programador de alarmas y cron local basado en AlarmManager para ejecución desatendida.
│   │   │   ├── ScheduleAlarmReceiver.kt     # BroadcastReceiver nativo que captura las alarmas activadas por el sistema.
│   │   │   ├── BackgroundTriggerReceiver.kt # Receptor estático ligero para eventos de cargador en segundo plano.
│   │   │   ├── ChargerTriggerHandler.kt     # Procesador modular de eventos de cargador desatendidos.
│   │   │   ├── BackgroundExecutor.kt        # Ejecutor de atajos desatendidos en segundo plano.
│   │   │   ├── BackgroundCallbacks.kt       # Implementación silenciosa de callbacks para ejecuciones desatendidas.
│   │   │   ├── FileLogManager.kt            # Gestor persistente de fallos críticos y advertencias en el almacenamiento de Kinetix.
│   │   │   ├── TtsManager.kt                # Singleton y gestor unificado de motores y voces locales para Text-To-Speech (TTS).
│   │   │   │
│   │   │   └── strategies/                  # --- SUBMÓDULO DE ESTRATEGIAS INDIVIDUALES ---
│   │   │       ├── SystemStrategies.kt      # Registro específico de estrategias del sistema.
│   │   │       ├── DeveloperStrategies.kt   # Registro específico de estrategias avanzadas de programación.
│   │   │       ├── FileStrategies.kt        # Registro específico de estrategias de manipulación de archivos locales.
│   │   │       ├── PowerAndroidStrategies.kt # Registro específico de estrategias avanzadas de hardware y accesibilidad.
│   │   │       ├── TextAndMathStrategies.kt # Registro específico de estrategias de manipulación de textos y variables.
│   │   │       ├── AudioVisualStrategies.kt # Registro específico de estrategias visuales y de audio.
│   │   │       │
│   │   │       ├── VibrateStrategy.kt       # Ejecución de la vibración del hardware.
│   │   │       ├── SystemInfoStrategy.kt    # Consulta dinámica de batería, modelo de dispositivo y versión de Android.
│   │   │       ├── SetBrightnessStrategy.kt # Manipulación del brillo de la pantalla.
│   │   │       ├── SetRingerModeStrategy.kt # Modificación del perfil sonoro (Normal, Vibrar, Silencio).
│   │   │       ├── AccessibilityActionStrategy.kt # Acciones de accesibilidad del sistema.
│   │   │       ├── OpenAppStrategy.kt       # Apertura programada de aplicaciones externas mediante Intents.
│   │   │       ├── OpenUrlStrategy.kt       # Apertura segura de URLs del navegador.
│   │   │       ├── SpeakTextStrategy.kt     # Reproducción de voz de texto (TTS) con control de velocidad inteligente.
│   │   │       ├── ShowNotificationStrategy.kt # Despachador de notificaciones locales de sistema.
│   │   │       ├── PlaySoundStrategy.kt     # Reproducción de sonidos del sistema y efectos personalizados.
│   │   │       ├── SetVolumeStrategy.kt     # Control preciso de volumen por stream específico.
│   │   │       ├── ExecJavaScriptStrategy.kt # Motor rápido aislado para evaluación de JS mediante QuickJS.
│   │   │       ├── TermuxCommandStrategy.kt # Despachador de Intents hacia comandos en background de Termux.
│   │   │       ├── CustomCodeStrategy.kt    # Estrategia de ejecución de script propietario.
│   │   │       ├── CustomCodeInterpreter.kt # Intérprete y evaluador de expresiones de script propietario.
│   │   │       ├── ClipboardStrategy.kt     # Copiado o lectura silenciosa en el portapapeles.
│   │   │       ├── SimulateGesturesStrategy.kt # Emulación de toques o swipes rápidos en pantalla.
│   │   │       ├── BackgroundCameraCaptureStrategy.kt # Estrategia de disparo de captura fotográfica en segundo plano.
│   │   │       ├── CameraCaptureHelper.kt   # Helper de bajo nivel para inicialización y guardado de captura Camera2.
│   │   │       ├── WriteFileStrategy.kt     # Almacenamiento seguro en disco local.
│   │   │       ├── ReadFileStrategy.kt      # Lectura estructurada de archivos.
│   │   │       └── AppendFileStrategy.kt    # Adición incremental de logs a archivos locales.
│   │   │
│   │   └── ui/                              # --- CAPA DE PRESENTACIÓN / INTERFAZ ---
│   │       ├── ShortcutViewModel.kt         # ViewModel central que expone flujos reactivos desacoplados y maneja eventos globales.
│   │       ├── ShortcutAutomationHandler.kt # Controlador optimizado con filtros antirrebote (debounce) para triggers del sistema.
│   │       ├── ShortcutExecutorEngine.kt    # Gestor de la pila de ejecución interactiva del motor de atajos.
│   │       ├── InteractiveExecutionCallbacks.kt # Callbacks modulares para puente de UI interactivo con el ejecutor.
│   │       ├── DialogStates.kt              # Modelos de estado reactivo de diálogos de ejecución.
│   │       │
│   │       ├── components/                  # --- COMPONENTES COMUNES Y REUTILIZABLES ---
│   │       │   ├── ActionListItem.kt        # Item visual de acción de un atajo dentro del editor con controlador DragHandle.
│   │       │   ├── ActionPickerDial.kt      # Selector circular animado de categorías de automatizaciones.
│   │       │   ├── AppPickerDialog.kt       # Cuadro de diálogo para la selección dinámica de aplicaciones instaladas.
│   │       │   ├── ConsoleDrawer.kt         # Consola de logs superpuesta sobre la pantalla de ejecución.
│   │       │   ├── DynamicActionInputs.kt   # Formularios reactivos dinámicos para los parámetros de cada acción.
│   │       │   ├── IconMapper.kt            # Utilidad estática que traduce cadenas a iconos vectoriales de Material Design.
│   │       │   ├── ShortcutGridItem.kt      # Item visual de la rejilla principal de atajos.
│   │       │   └── ShortcutIdentityCard.kt  # Tarjeta de metadatos del atajo con soporte para disparador por horario.
│   │       │
│   │       ├── navigation/                  # --- MÓDULO DE NAVEGACIÓN ---
│   │       │   └── AppNavigation.kt         # Gestor centralizado de enrutamiento y transiciones de pantalla con Jetpack Compose.
│   │       │
│   │       ├── preferences/                 # --- CONFIGURACIÓN Y PERSISTENCIA ---
│   │       │   └── KinetixPreferencesManager.kt # Encapsulado modular de preferencias de usuario persistentes.
│   │       │
│   │       ├── screens/                     # --- VISTAS COMPOSE ---
│   │       │   ├── ShortcutListScreen.kt    # Vista principal de la grilla de atajos.
│   │       │   ├── ShortcutEditorScreen.kt  # Editor secuencial de flujos.
│   │       │   ├── OnboardingScreen.kt      # Pantalla animada de bienvenida.
│   │       │   ├── VariablesTutorialScreen.kt # Pantalla de guía interactiva de variables.
│   │       │   ├── SettingsScreen.kt        # Pantalla de ajustes, diagnósticos y logs.
│   │       │   │
│   │       │   ├── list/                    # --- SUBMÓDULO DE LA LISTA DE ATAJOS ---
│   │       │   │   ├── ShortcutListTopBar.kt # Barra superior modular para ShortcutListScreen.
│   │       │   │   └── ShortcutListFooter.kt # Pie de página informativo modular.
│   │       │   │
│   │       │   ├── settings/                # --- SUBMÓDULO DE AJUSTES Y LOGS ---
│   │       │   │   ├── SettingsTopBar.kt     # Barra superior modular para SettingsScreen.
│   │       │   │   └── EmptyLogCard.kt      # Tarjeta modular de estado vacío de logs de diagnóstico.
│   │       │   │
│   │       │   └── inputs/                  # --- FORMULARIOS DE PARÁMETROS DE ACCIÓN (MODULARIZADO) ---
│   │       │       ├── CommonInputs.kt      # Inputs de texto, alerta y notificaciones.
│   │       │       ├── FileInputs.kt        # Parámetros para operaciones de archivos locales.
│   │       │       ├── DeveloperInputs.kt   # Entradas para JS y comandos de consola Termux.
│   │       │       ├── LogicInputs.kt       # Formulario para condicionales.
│   │       │       ├── PowerAndroidInputs.kt # Parámetros para vibración y gestos.
│   │       │       ├── SharedInputs.kt      # Inputs compartidos.
│   │       │       ├── SystemInfoInput.kt   # Selección de tipo de dato del sistema.
│   │       │       ├── OpenAppInput.kt      # Selección de app con delay de apertura.
│   │       │       ├── SetVolumeInput.kt    # Control de volumen por canal de audio.
│   │       │       ├── SetRingerModeInput.kt # Selector de perfiles de sonido de timbre.
│   │       │       ├── SetBrightnessInput.kt # Slider de brillo de pantalla.
│   │       │       └── AccessibilityActionInput.kt # Selector de navegación por accesibilidad.
│   │       │   ...
```

---

## 🔬 Descripción de Componentes Clave

### 1. Descomposición del Patrón Strategy (`ActionStrategyRegistry.kt`)
Para maximizar la separación de responsabilidades y asegurar que añadir nuevas automatizaciones no altere las clases existentes, el sistema de ejecución ha sido completamente modularizado:
- **Separación Física**: Cada acción se encapsula en su propia clase dedicada (ej. `BackgroundCameraCaptureStrategy.kt`, `WriteFileStrategy.kt`).
- **`ActionStrategyRegistry`**: Unifica los mapas de estrategias locales de cada categoría en un diccionario global.
- **Desacoplamiento**: `ShortcutExecutor.kt` no posee dependencias directas con las clases individuales de estrategia, logrando que el núcleo del motor sea genérico y fácil de extender.

### 2. Triggers Robustos con Control Antirrebote (`ShortcutAutomationHandler.kt`)
Los sensores físicos y de carga de los dispositivos Android suelen reportar múltiples cambios consecutivos debido a micro-interrupciones del hardware (ruido por rebote de puerto).
- **Debounce de Hardware**: Se ha integrado una constante `COOLDOWN_MS = 4000L` y mapeo temporal en `ShortcutAutomationHandler`. Si el sistema reporta que un cargador o auricular se desconectó y conectó rápidamente, el software ignora las ejecuciones redundantes dentro de la ventana de enfriamiento.
- **Conectores Seguros**: El registrador en `SystemTriggerManager.kt` utiliza el contexto de la aplicación para prevenir fugas de memoria y declara explícitamente los parámetros de seguridad requeridos por Android 14+ (`Context.RECEIVER_NOT_EXPORTED`), garantizando una ejecución inmune a caídas imprevistas por políticas de Google Play.

### 3. Navegación Estricta y Desacoplada (`AppNavigation.kt`)
Anteriormente, la actividad principal contenía de forma monolítica la navegación en su función `setContent`. Se ha modularizado extrayendo el enrutamiento:
- **`AppNavigation`**: Expone las rutas de la app (`Screen.ONBOARDING`, `Screen.LIST`, `Screen.EDITOR`, `Screen.SETTINGS`) mediante animaciones compuestas suaves de desvanecimiento de entrada y salida (`fadeIn`/`fadeOut`).
- **`MainActivity` Simplificada**: Actúa como un mero contenedor del ciclo de vida de la ventana, permitiendo su fácil mantenimiento técnico.

### 4. Gestión de Configuraciones Separada (`KinetixPreferencesManager.kt`)
Toda la interacción de persistencia simple de ajustes del usuario (como activar el modo de baja resolución para acelerar renderizados en terminales antiguos) se ha centralizado en una clase modular separada de las responsabilidades funcionales del ViewModel.

### 5. Inyección de Dependencias Limpia (`ShortcutViewModel.kt`)
Se ha migrado la inicialización de recursos clave de `ShortcutViewModel` hacia **Constructor Dependency Injection (DI)** con parámetros opcionales por defecto:
- **Acoplamiento Débil**: El repositorio, el sintetizador de voz (`TtsManager`) y las preferencias se inyectan en el constructor principal en lugar de instanciarse rígidamente dentro de la clase.
- **Mantenibilidad y Testabilidad**: Esta aproximación permite sustituir fácilmente cualquier dependencia por dobles de pruebas (mocks/fakes) en entornos de testing sin romper la integración predeterminada de Jetpack Compose (`viewModel()`).

### 6. Validación e Interpolación Segura de Variables (`ShortcutExecutor.kt`)
Para prevenir desbordamientos de memoria (Out-of-Memory) y ataques de denegación de servicio internos por la inyección recursiva de cadenas masivas, el ejecutor de atajos implementa:
- **Truncado de Seguridad**: Las variables de entrada son validadas y recortadas de forma inteligente (`currentInput.take(10000)`) para asegurar un consumo estable de RAM durante bucles de automatizaciones.
- **Soporte Sintáctico Ampliado**: Soporta tanto la nomenclatura estándar de llaves (`{input}`, `{entrada_atajo}`) como la de inyección nativa con signo de dólar (`${input}`, `${entrada_atajo}`).

---
