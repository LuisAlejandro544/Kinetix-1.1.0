# Registro de Cambios (Changelog) - Kinetix

Todos los cambios notables en esta actualización de Kinetix se detallan a continuación.

---

## [v1.1.0] - Petición HTTP / Webhooks, Programación por Horario y Reordenamiento Drag-and-Drop

### 🌐 Acción de Peticiones HTTP y Webhooks (`HTTP_REQUEST`)
* **Integración Web & API:** Implementamos la nueva acción `HTTP_REQUEST` con la estrategia `HttpRequestStrategy`. Soporta métodos HTTP clave (`GET`, `POST`, `PUT`, `DELETE`, `PATCH`), cabeceras personalizadas en formato `Clave: Valor`, cuerpo de petición dinámico con soporte para `{resultado}` / `${input}`, y tiempo de espera configurable.
* **Integración de UI (`HttpRequestInput`):** Formulario especializado dentro del editor de acciones para configurar URLs, métodos, cabeceras, cuerpo JSON/texto y tiempos de respuesta.

### ⏰ Programación por Horario / Alarmas (Cron Local)
* **Disparador Programado (`ScheduleTriggerSection`):** Nueva sección de automatización en la tarjeta de identidad que permite programar la ejecución automática de atajos a una hora fija (formato 24h `HH:mm`) y días específicos de la semana (`L, M, X, J, V, S, D` o `DAILY`).
* **Motor de Alarmas (`ScheduleTriggerManager` & `ScheduleAlarmReceiver`):** Gestor nativo basado en `AlarmManager` (`setExactAndAllowWhileIdle`) que calcula el próximo momento de activación, maneja reintentos de ciclo semanal y ejecuta atajos desatendidos mediante `BackgroundExecutor`.
* **Sincronización en Inicio de Sistema:** `BootCompletedReceiver` resincroniza automáticamente las alarmas programadas al encender o reiniciar el dispositivo.
* **Insignia Visual en Tarjetas:** Las tarjetas principales exhiben la insignia morada de hora programada (`TriggerBadges`).

### 🖐️ Reordenamiento Drag-and-Drop en el Editor
* **Controlador de Arrastre (`DragHandle`):** Agregamos un tirador táctil de arrastre en la cabecera de cada tarjeta de acción (`ActionListItem`).
* **Gestos Táctiles Reactivos (`detectDragGestures`):** Permite reordenar visualmente las acciones deslizándolas hacia arriba o abajo en lugar de eliminarlas o usar únicamente los botones manuales.

---

## [v1.3.0] - Selector de Voces y Motores Locales con Previsualización Interactiva

### 🗣️ Selector de Motores y Voces Locales para TTS
* **Soporte Multi-Motor y Multi-Voz:** Modificamos el gestor de síntesis de voz (**`TtsManager`**) para que actúe como un singleton reactivo que detecta de forma dinámica todos los motores de texto a voz instalados en el sistema operativo Android del usuario (ej. Google TTS, Vocalizer, etc.) y lista todas las voces y variantes de idiomas disponibles localmente.
* **Integración en la Configuración de Atajos:** Agregamos selectores interactivos avanzados dentro del formulario de parámetros de la acción "Leer en voz alta" (**`SpeakTextInput`**). Ahora el usuario puede cambiar el motor de voz y seleccionar la variante de voz exacta para cada atajo.
* **Persistencia e Inferencia de Parámetros:** Las elecciones de motor y voz se guardan como metadatos en el JSON de la acción, permitiendo que la estrategia (**`SpeakTextStrategy`**) aplique la configuración exacta al ejecutarse en segundo plano.

### 🔊 Previsualización de Audio en Caliente (Audio Preview Engine)
* **Previsualización TTS:** Añadimos un botón interactivo "Escuchar preview 🗣️" en el editor de atajos para el paso de síntesis de voz. Al pulsarlo, se ejecuta una reproducción de prueba instantánea del texto configurado con la velocidad y voz seleccionadas, permitiendo al usuario escuchar el resultado antes de guardar el flujo.
* **Previsualización de Sonidos de Notificación:** Integramos un botón interactivo "Escuchar preview 🔊" en la configuración de la acción "Reproducir sonido" (**`PlaySoundInput`**). Este botón ejecuta la señal acústica elegida de inmediato (usando el motor de baja latencia `SoundPool` o `ToneGenerator`), emulando perfectamente el timbre antes de guardar.

### 📄 Documentación y Sincronización del Sistema
* **Actualización del Repositorio:** Sincronizamos las guías principales del sistema (`README.md`, `ROADMAP.md`, `STRUCTURE.md` y `AI_CONTEXT.md`) para documentar detalladamente las nuevas capacidades acústicas e interactivas de Kinetix.

---

## [v1.2.0] - Modulación Avanzada y Planificación CPU big.LITTLE

### ⚡ Programación Adaptativa de CPUs (Arquitectura big.LITTLE)
* **Gestor de Planificación (`CpuDispatcherProvider`):** Creamos un programador de hilos nativos para optimizar de manera inteligente la asignación de tareas según el tipo de carga de trabajo, mapeando llamadas a los grupos de hilos del kernel de Android (cgroups).
* **Tareas de Eficiencia (Cores "LITTLE"):**
  * Migramos **`WriteFileStrategy`** y **`ReadFileStrategy`** para ejecutarse en el pool de eficiencia de bajo consumo, garantizando que el almacenamiento masivo no consuma batería de forma innecesaria.
  * Optimizamos **`FileLogManager`** mediante el patrón inline de preservación de prioridad `runOnLittleCore`, asegurando que todos los volcados de caídas (crashes) y advertencias en disco corran en hilos de bajo consumo.
* **Tareas de Potencia (Cores "big"):**
  * Migramos el motor de JavaScript de QuickJS (**`ExecJavaScriptStrategy`**) al pool de alto rendimiento para garantizar latencia mínima en cálculos analíticos complejos.
  * Asignamos la interpretación de scripts dinámicos (**`CustomCodeInterpreter`** / **`CustomCodeStrategy`**) al pool de alto rendimiento.
  * Elevamos la prioridad de la simulación de gestos táctiles por accesibilidad (**`SimulateGesturesStrategy`**) para evitar retardos o tartamudeos (stuttering) al emular interacciones físicas en pantalla.

### 📦 Desarrollo Modular y Limpieza de Código
* **Estrategias Audiovisuales Desacopladas:** Fragmentamos el antiguo bloque de `AudioVisualStrategies.kt` en archivos independientes y testeables:
  * `SpeakTextStrategy.kt` (Gestor de Síntesis TTS).
  * `ShowNotificationStrategy.kt` (Sistema de Canales de Notificaciones).
  * `PlaySoundStrategy.kt` (Efectos de Audio y Tonos).
  * `SetVolumeStrategy.kt` (Control de Streams de Audio).
* **Formularios de Parámetros Modularizados:** Separamos los componentes de entrada unificados de `SystemInputs.kt` en pantallas reactivas independientes dentro del nuevo paquete `ui/screens/inputs/`:
  * `SystemInfoInput.kt`, `OpenAppInput.kt`, `SetVolumeInput.kt`, `SetRingerModeInput.kt`, `SetBrightnessInput.kt`, `AccessibilityActionInput.kt`.
* **Desacoplamiento de Motores Core:**
  * Separamos la captura de fotos en segundo plano de la estrategia creando el helper reutilizable **`CameraCaptureHelper.kt`** (utilizando la API Camera2 nativa).
  * Desacoplamos la interpretación de variables y operaciones lógicas del flujo general aislando la lógica en **`CustomCodeInterpreter.kt`**.

### 📁 Sincronización Arquitectónica
* **Estructura del Proyecto (`STRUCTURE.md`):** Sincronizamos las descripciones y el árbol jerárquico de archivos del proyecto con el nuevo diseño de subcarpetas independientes y modulares.

---

## [v1.1.0] - Nueva Actualización de Automatización, Adaptabilidad de Texto y Audio Premium

### 🔄 Servicio en Primer Plano (Foreground Service) y Receptor de Inicio del Sistema
* **Servicio Persistente de Monitoreo (`KinetixForegroundService`):** Implementamos un servicio Foreground continuo con notificación en segundo plano que mantiene activos 24/7 los receptores de eventos y disparadores (conexión/desconexión de cargador, cambios de nivel de batería y conexión de auriculares) incluso cuando la app es eliminada de las aplicaciones recientes o descartada de la memoria RAM.
* **Receptor de Arranque de Sistema (`BootCompletedReceiver`):** Integramos un receptor estático para los eventos `ACTION_BOOT_COMPLETED`, `ACTION_LOCKED_BOOT_COMPLETED` y `ACTION_MY_PACKAGE_REPLACED`, asegurando que el servicio de monitoreo en segundo plano se inicie automáticamente tan pronto como el dispositivo móvil se enciende o reinicia.
* **Resiliencia de Disparadores:** Garantizamos la ejecución transparente de atajos automatizados (como el detector de conexión del cargador) en cualquier estado del sistema operativo.

### 📐 Control de Accesibilidad y Adaptabilidad de Texto (Font Scaling Clamp)
* **Limitación de Factor de Escala de Texto:** Se implementó una restricción global en `MyApplicationTheme` utilizando `CompositionLocalProvider(LocalDensity)` para fijar el factor de escala de fuente (`fontScale`) dentro de un rango seguro (`0.85f..1.25f`). Esto previene la rotura de diseño, desbordamientos de botones y encavalgamiento de elementos cuando el usuario tiene activado un tamaño de texto gigante en los ajustes del sistema Android.

### 🔊 Motor de Audio Nativo Consolidado
* **Tono Nativos Hardware (`ToneGenerator`):** Se simplificó la acción de reproducción de sonido para utilizar exclusivamente las señales acústicas integradas de Android (`ToneGenerator.TONE_PROP_BEEP` y `ToneGenerator.TONE_PROP_ACK`), garantizando el 100% de compatibilidad, cero consumo de memoria adicional y eliminación de fallas de reproducibilidad o bloqueos de hilo por SoundPool/MediaPlayer en cualquier versión de Android o emulador.
* **Remoción de Sonidos Incompatibles:** Se removieron las opciones pesadas e inestables que fallaban en ciertos dispositivos y emuladores (`Kinetix Chime`, `Level Up`, `Éxito (ACK)` y tonos de `RingtoneManager`), manteniendo únicamente las alertas ligeras y confiables (`Pitido simple` y `Alerta`).
* **Optimización de Almacenamiento:** Eliminación de los archivos de audio en disco para mantener el APK en su tamaño mínimo y con rendimiento óptimo.

### 📦 Refactorización y Desarrollo Modular
* **Modularización de UI y Pantallas:** Desacoplamiento de barras superiores y secciones secundarias (`ShortcutListTopBar`, `ShortcutListFooter`, `SettingsTopBar`, `EmptyLogCard`).
* **Modularización del Motor de Ejecución:** Aislamiento del resolutor de variables (`VariableResolver`), controladores de callbacks interactivos (`InteractiveExecutionCallbacks`), callbacks desatendidos (`BackgroundCallbacks`) y manejadores de eventos de hardware (`ChargerTriggerHandler`).

### 🛠️ Corrección de Errores Críticos (Bug Fixes)
* **Corrección en la sustitución de variables de salida:** Se corrigió un error crítico que ocurrió en la última fase de desarrollo modular, en el cual las automatizaciones que usaban variables dinámicas leían o mostraban los marcadores de posición literal (como `{resultado}` o `${resultado}`) en lugar de sustituirlos con el valor real de la acción anterior. Ahora la sustitución se realiza de forma segura y transparente.

### 📄 Documentación del Sistema
* **Licencia de Audio (`AUDIO_LICENSE.md`):** Creamos y actualizamos un archivo de especificaciones de licencia para documentar de manera profesional la autoría, los enlaces originales de descarga de Freesound, el diseño acústico, el espectro de decibelios (-8.3 LUFS) y las atribuciones obligatorias de Creative Commons de ambos sonidos, sin inflar la aplicación final.

---

*Desarrollado con precisión técnica y enfoque en la optimización de recursos.*
