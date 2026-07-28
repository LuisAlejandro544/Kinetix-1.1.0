# 🤖 AI_CONTEXT.md - Manual para Agentes de IA

Este documento sirve como manual técnico e instructivo detallado de contexto para cualquier Inteligencia Artificial o Agente que trabaje en esta base de código de **Kinetix**. Por favor, lee y respeta minuciosamente estas directrices para asegurar la coherencia arquitectónica, de diseño y funcional del software.

---

## 🧭 Reglas de Oro del Proyecto (Obligatorias)

1. **Sin Cambios de Estructura de Braces o Brackets Descuidados**:
   - Al editar el archivo de pantallas (`ShortcutListScreen.kt` o `ShortcutEditorScreen.kt`), comprueba minuciosamente los emparejamientos de llaves `{}`.
   - La pantalla principal (`ShortcutListScreen.kt`) contiene componentes de diseño superpuestos usando un contenedor `Box` a nivel raíz para permitir la cápsula flotante. Nunca rompas esta jerarquía.

2. **No elimines la Consola de Logs original**:
   - El usuario prefiere una experiencia de cápsula flotante no intrusiva por defecto, pero **desea conservar la Consola de Logs completa** para depuración y consulta técnica avanzada.
   - Mantén la variable `showConsole` en el ViewModel y conserva el composable `ConsoleDrawer(viewModel)`. Se debe poder abrir tocando la cápsula de ejecución ("Ver detalles").

3. **Manejo de Entrada/Salida Secuencial (`${input}`)**:
   - Cada acción en un flujo debe procesar su entrada reemplazando la cadena literal`${input}` (declarada como constante en `ActionType.input`) con el resultado retornado por el paso anterior.
   - En el motor de ejecución (`ShortcutExecutor.kt`), realiza este reemplazo de forma segura en hilos asíncronos en segundo plano utilizando `withContext(Dispatchers.IO)`.

4. **Extensibilidad de Motores de Scripting (QuickJS y LuaJ)**:
   - **Gestión de Recursos**: Al inicializar QuickJS (`app.cash.quickjs.QuickJs`), asegúrate de encerrarlo en un bloque `use` o invocar `.close()` explícitamente en la sección `finally` para liberar la memoria nativa y evitar fugas.
   - **Aislamiento**: Ejecuta los fragmentos de código del usuario en un entorno aislado sin acceso directo a APIs del sistema de archivos o red de Android a menos que se expongan intencionalmente interfaces controladas.

---

## 🛠️ Especificación de Tipos de Acción (`ActionType`)

Si vas a agregar una nueva acción al proyecto, debes:
1. Declararla en `ActionType.kt`.
2. Implementar su lógica en el bloque `when` de `ShortcutExecutor.executeAction(...)`.
3. Ofrecer un formulario de configuración interactivo y amigable para sus parámetros dentro del archivo `ShortcutEditorScreen.kt`.

### Acciones Actuales Soportadas:

| Tipo (`ActionType`) | Parámetros Requeridos / Claves | Salida Generada | Propósito |
|:---|:---|:---|:---|
| `SYSTEM_INFO` | `infoType` ("Battery Level", "Device Model") | Un String con el dato solicitado. | Consulta información del hardware/SO. |
| `SPEAK_TEXT` | `text` (permite `${input}`), `speechRate`, `engine`, `voice` | El mismo String reproducido. | Lee en voz alta mediante el motor TTS nativo con selección de motor/voces locales y previsualización interactiva. |
| `TEXT_INPUT` | `prompt`, `defaultValue` | El texto ingresado por el usuario. | Detiene el flujo y solicita entrada manual. |
| `TEXT_TRANSFORM`| `transformType` ("UPPERCASE", "lowercase", "Reverse", "Word Count") | El texto modificado o la cuenta. | Formatea o analiza una cadena de texto. |
| `SHOW_NOTIFICATION`| `title`, `message` (permite `${input}`) | El texto del mensaje. | Envía una notificación push estándar. |
| `ALERT_DIALOG` | `title`, `message` (permite `${input}`) | El mensaje de la alerta. | Detiene el flujo y muestra un cuadro de diálogo interactivo. |
| `SHARE_TEXT` | `text` (permite `${input}`) | El texto compartido. | Desencadena el Intent nativo de compartir texto de Android. |
| `MATH_OP` | `operation` ("Add", "Subtract", "Multiply", "Divide"), `operand` | El número resultante como String. | Realiza cálculo aritmético sobre el `${input}` anterior. |
| `CONDITIONAL` | `value` (permite `${input}`), `operator` ("Equals", "NotEquals", "Contains", "GreaterThan", "LessThan"), `compareValue`, `thenValue` (permite `${input}`), `elseValue` (permite `${input}`) | El valor asignado a thenValue o elseValue. | Evalúa una condición lógica y retorna el valor correspondiente. |
| `OPEN_APP` | `packageName`, `appName`, `delay` | El valor de entrada previo. | Espera N segundos y abre una aplicación instalada seleccionada del sistema. |
| `SET_VOLUME` | `streamType` ("Music", "Ring", "Notification", "Alarm"), `volumePercent` (0-100) | El valor de entrada previo. | Ajusta de forma independiente el volumen de un canal específico mediante AudioManager. |
| `SET_RINGER_MODE` | `ringerMode` ("Normal", "Vibrate", "Silent") | El valor de entrada previo. | Cambia el perfil de sonido del sistema con fallback seguro de silenciado alternativo. |
| `WRITE_FILE` | `fileName`, `content` (permite `${input}`) | El valor de entrada previo. | Escribe o sobrescribe un archivo local en el almacenamiento privado de la app. |
| `READ_FILE` | `fileName` | El contenido de texto leído del archivo. | Lee el contenido de un archivo local de la app y lo retorna para el siguiente paso. |
| `APPEND_FILE` | `fileName`, `content` (permite `${input}`) | El valor de entrada previo. | Añade una línea con el contenido al final de un archivo local de la app. |
| `SET_BRIGHTNESS` | `brightnessPercent` (0-100) | El valor de entrada previo. | Ajusta el brillo de pantalla del sistema con redirección para permiso WRITE_SETTINGS si falta. |
| `ACCESSIBILITY_ACTION` | `actionType` ("Back", "Home", "Notifications", "Quick Settings", "Lock Screen", "Power Dialog") | El valor de entrada previo. | Ejecuta una acción de accesibilidad global mediante el servicio de accesibilidad. |
| `PLAY_SOUND` | `soundType` ("Kinetix", "LevelUp", "Success", "Beep", "Alert", "Notification", "Ringtone", "Alarm") | El valor de entrada previo. | Reproduce un sonido o tono acústico interactivo al estilo iOS usando SoundPool (baja latencia), incluyendo timbres personalizados de bajo peso. |
| `EXEC_JAVASCRIPT`  | `code` (permite `${input}`) | El valor retornado por el script JS. | Ejecuta un fragmento de JavaScript seguro en el motor QuickJS embebido nativo. |
| `TERMUX_COMMAND`   | `command`, `args`, `runInBackground` | El valor de entrada previo. | Envía y ejecuta un comando o script avanzado en la terminal de Termux utilizando Intents del sistema. |
| `CUSTOM_CODE`      | `script` (permite `${input}`) | El valor de retorno del script personalizado. | Permite crear acciones complejas usando sentencias nativas como PRINT, SET, RETURN, y TTS. |
| `BACKGROUND_CAMERA_CAPTURE` | `cameraType` ("BACK", "FRONT") | La ruta del archivo JPG resultante de la foto. | Captura fotos silenciosamente en segundo plano sin mostrar ninguna interfaz visual. |
| `SIMULATE_GESTURES` | `gestureType` ("TAP", "SWIPE"), `x1`, `y1`, `x2`, `y2`, `duration` | El valor de entrada previo. | Simula toques o deslizamientos en coordenadas exactas de la pantalla usando accesibilidad local. |
| `CLIPBOARD_SILENT` | `operation` ("WRITE", "READ"), `text` (permite `${input}`) | El texto copiado o extraído. | Realiza lecturas o escrituras silenciosas al portapapeles del sistema para integraciones de texto. |

---

## ⚡ Automatizaciones y Disparadores (Triggers)

El sistema soporta disparadores en segundo plano gestionados de forma unificada por `SystemTriggerManager` y mantenidos activos 24/7 mediante `KinetixForegroundService`:
- **Servicio en Primer Plano (`KinetixForegroundService`)**: Mantiene una notificación persistente de prioridad baja para evitar que el sistema operativo mate el proceso cuando la aplicación es cerrada desde la lista de aplicaciones recientes. Registra dinámicamente los monitores de batería, cargador y auriculares.
- **Receptor de Inicio de Sistema (`BootCompletedReceiver`)**: Escucha `ACTION_BOOT_COMPLETED` y `ACTION_LOCKED_BOOT_COMPLETED` para iniciar `KinetixForegroundService` de forma automática tan pronto como se enciende el teléfono.
- **Disparador por Nivel de Batería**: Registra un `BroadcastReceiver` para el intent `ACTION_BATTERY_CHANGED`. Evalúa condiciones basadas en porcentaje de batería (`isBatteryTriggerEnabled`):
  - `EQUALS` (Igual): Se activa exactamente cuando la batería llega al porcentaje.
  - `FALLS_BELOW` (Menor): Se activa cuando la batería cae por debajo del nivel establecido.
  - `RISES_ABOVE` (Mayor): Se activa cuando la batería sube por encima del nivel establecido.
- **Disparador por Cargador**: Registra eventos al conectar/desconectar el cable de corriente (`ACTION_POWER_CONNECTED` / `ACTION_POWER_DISCONNECTED`). Activa atajos de manera automatizada.
- **Disparador por Auriculares**: Detecta la conexión o desconexión de auriculares físicos con cable (`ACTION_HEADSET_PLUG`).

## 📁 Registro de Eventos e Interrupción de Hilos (`FileLogManager`)

- **Crashes Críticos**: Un controlador global intercepta todas las excepciones no capturadas de la app y las registra en `Android/data/com.kinetix/files/crashes/` junto con información detallada del hardware y sistema.
- **Warnings de Flujos**: Cuando un atajo falla de forma controlada o sufre una interrupción en su secuencia de ejecución, se registra una advertencia en `Android/data/com.kinetix/files/warnings/` indicando el paso y la causa del fallo.
- **Consola de Diagnóstico (Pre-Alpha)**: Un visualizador dedicado en la sección de Ajustes lee estos archivos de registro de forma asíncrona. Los registros cuentan con explicaciones explícitas de que sirven para depurar errores en el modelo del dispositivo móvil del usuario. Los usuarios pueden copiar el texto completo al portapapeles ("Copiar Log") o compartirlo mediante un Intent estándar del sistema ("Compartir / Exportar") sin necesidad de configurar una cuenta de correo directa obligatoria.

## 🎨 Identidad Visual y Logotipo (Edición Pre-Alpha Pública)

- **Logo Pre-Alpha**: El ícono del lanzador (`ic_launcher_foreground.xml`) y el logotipo de bienvenida en `OnboardingScreen` han sido actualizados con una marca visual e insignia de color rojo vibrante que indica el estado de "PRE-ALPHA".
- **Respaldo de Logo Original**: Se mantiene una copia de respaldo del logotipo limpio y original sin distintivos en `/app/src/main/res/drawable/ic_launcher_foreground_original.xml` para futuras publicaciones estables.

## 🖼️ Personalización de Automatizaciones y Optimización Gráfica

- **Fondo con Imagen de Galería**: Los atajos soportan una imagen de fondo personalizada cargada desde la galería del dispositivo (`customPhotoUri`). Se renderiza usando Coil con un filtro oscuro translúcido para mantener una legibilidad de contraste de texto de nivel de producción (> 4.5:1).
- **Opción de Calidad Gráfica Baja**: Si `viewModel.isLowGraphicsQuality` está activo, la interfaz de usuario de Kinetix desactiva de inmediato todas las cargas en memoria de imágenes pesadas (`customPhotoUri`) en la lista de atajos y reemplaza degradados con pinceles de degradado de color dinámico por colores de fondo planos y de alto rendimiento. Esto garantiza la máxima estabilidad táctil en terminales con recursos de hardware muy limitados.

## ⚖️ Gestión de Licencias, Propiedad y Contribuciones

- **Licencia del Repositorio**: El proyecto está bajo la **PolyForm Noncommercial License 1.0.0** (`LICENSE`). Prohibido cualquier uso o distribución comercial.
- **Sin Contribuciones de Terceros**: No se aceptan Pull Requests ni contribuciones de terceros. El proyecto es gestionado únicamente de forma privada por el autor original.
- **Registro de Licencias de Recursos de Audio o Externos**:
  - Si un sonido u otro recurso integrado en el proyecto tiene una licencia que exige dar créditos obligatorios de atribución (por ejemplo, **Creative Commons Attribution 4.0 - CC BY 4.0**), es **estrictamente obligatorio** añadir su respectiva atribución y enlace original en el apartado especial de la pantalla de Ajustes (`SettingsScreen.kt` a través de `ThirdPartyLicensesCard.kt`).
  - Esto garantiza el cumplimiento de la propiedad intelectual de terceros de forma pública y visible dentro de la app sin importar que el repositorio del proyecto sea privado o la app esté compilada para uso cerrado.
  - Asegúrate de actualizar también `AUDIO_LICENSE.md` en la raíz del proyecto para documentar los parámetros técnicos y licencias del audio a nivel de código fuente.

---

## 🎨 Convenciones de Diseño y UI (Jetpack Compose)

- **Material Design 3 (M3)**: Todas las tarjetas y componentes deben heredar colores y tipografías del esquema `MaterialTheme.colorScheme` centralizado en `Theme.kt`.
- **Cápsula de Ejecución**: Muestra de forma flotante el progreso de la tarea mediante `AnimatedVisibility` con efectos de deslizamiento `slideInVertically` y desvanecimiento `fadeIn`. Su diseño debe ser redondeado, de alto contraste (`primaryContainer`), y no interferir con los toques principales en la rejilla de atajos.
- **Minimizar Padding Redundante**: Mantén un espacio equilibrado de 12.dp a 16.dp para los bordes de pantallas. Usa `TopAppBar` estándar en lugar de `LargeTopAppBar` para evitar espacios vacíos innecesarios en la parte superior.
- **Tags de Prueba (testTag)**: Los componentes interactivos (tarjetas, botones de acción, FAB, campos de texto en diálogos) **deben incluir obligatoriamente** la propiedad `Modifier.testTag("nombre_unico_en_snake_case")` para permitir su identificación automatizada y pruebas asíncronas con Robolectric o frameworks similares.

---

## 💾 Persistencia de Datos con Room y SQLite

- **Migraciones**: Si realizas cambios a las propiedades de la entidad `Shortcut` (en `Shortcut.kt`), recuerda incrementar la versión de la base de datos en `ShortcutDatabase.kt` e implementar un esquema de migración correspondiente o configurar la destrucción segura en desarrollo (`fallbackToDestructiveMigration()`).
- **Convertidores de Tipo**: El campo `actions` de la clase `Shortcut` se guarda en SQLite como una cadena JSON gracias a `ShortcutConverters.kt` que serializa y deserializa de forma transparente usando `Gson`. Evita mapeos manuales propensos a errores de conversión.
- **Uso de Hilos**: Toda consulta, inserción, actualización o eliminación en la base de datos **debe ejecutarse en hilos secundarios** usando `Dispatchers.IO` dentro de un bloque `viewModelScope.launch` para no bloquear o ralentizar el renderizado visual de la interfaz de usuario.
