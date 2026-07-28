# 🗺️ Hoja de Ruta (Roadmap) - Kinetix

Este documento detalla el plan de evolución del proyecto, dividido en fases de desarrollo a corto, mediano y largo plazo. Las metas están orientadas a potenciar las automatizaciones, mejorar la integración con el sistema operativo y enriquecer la experiencia de usuario.

---

## 📌 Estado Actual (Fase 1: Base Sólida y UX Fluida) ✅
- [x] **Persistencia de Datos**: Implementación completa de Room para CRUD de atajos personalizados.
- [x] **UX No Intrusiva**: Sustitución de la consola emergente automática por una cápsula flotante estilo *Dynamic Island* (cápsula interactiva al ejecutar).
- [x] **Entrada y Salida en Cadena**: Integración del comodín `${input}` para pasar valores secuencialmente de una acción a otra.
- [x] **Motor TTS Integrado**: Lectura en voz alta utilizando el motor nativo TextToSpeech de Android, incluyendo selección interactiva de motores y voces locales, control de velocidad y previsualización de voz y sonidos de notificación.
- [x] **Acciones Matemáticas**: Capacidad para realizar operaciones matemáticas consecutivas con los resultados intermedios.
- [x] **Permisos Dinámicos**: Declaración y solicitud correcta de permisos de vibración y notificaciones.
- [x] **Estructura de Scripting**: Dependencias iniciales para QuickJS (JavaScript) y LuaJ (Lua) integradas en el build de Gradle (Preparando el terreno).
- [x] **Lógica Condicional (Si / Entonces)**: Bifurcación dinámica de flujos basada en valores textuales, numéricos o contenido del `${input}`.
- [x] **Abrir Aplicación con Retraso**: Permite seleccionar cualquier app del sistema y programar un delay antes de abrirla.
- [x] **Control de Sonido y Volumen**: Permite configurar el porcentaje de volumen de multimedia, timbre, alarma y notificaciones, así como alternar perfiles (Sonido, Vibración, Silencio).
- [x] **Disparadores por Batería**: Automatización de ejecución de atajos cuando la batería alcanza un porcentaje específico o varía.
- [x] **Ajuste de Brillo de Pantalla**: Control directo y seguro del brillo de pantalla del sistema.
- [x] **Servicios de Accesibilidad**: Integración nativa de un servicio de accesibilidad para realizar gestos y comandos globales del sistema (Atrás, Inicio, Notificaciones, Menú de Encendido, etc.).
- [x] **Acción: Reproducir sonido**: Reproducción de señales acústicas interactivas del sistema (pitidos, tonos de llamada, alarmas) con opción de personalización estilo iPhone.
- [x] **Gestión de Archivos**: Guardado persistente, lectura y adición de registros (logs, notas) en archivos de texto locales de forma secuencial.
- [x] **Personalización Estética**: Posibilidad de cambiar el fondo de las tarjetas de atajos con fotos de la galería del usuario.
- [x] **Acción: Ejecutar JavaScript (QuickJS)**: Integración completa de QuickJS embebido para ejecutar scripts JS de manera local, segura e instantánea.
- [x] **Acción: Ejecutar comandos en Termux**: Integración nativa con la terminal de Termux mediante Intents del sistema para la ejecución de scripts y herramientas avanzadas.
- [x] **Acción: Bloque de Código Personalizado**: Motor de scripts nativo con soporte para variables dinámicas, concatenación, logs y control de voz (TTS).
- [x] **Acción: Captura de Cámara en Segundo Plano**: Captura silenciosa de fotos desde cámara frontal o trasera sin levantar interfaz, para procesamiento local.
- [x] **Acción: Simulación de Gestos (Tap & Swipe)**: Toques y desplazamientos de pantalla simulados mediante píxeles exactos apoyándose en el servicio de accesibilidad de Kinetix.
- [x] **Acción: Portapapeles Silencioso**: Lectura de texto o copiado automático en segundo plano para encadenar salidas con otras aplicaciones.
- [x] **Flujo de Bienvenida (Onboarding)**: Introducción animada al ecosistema de Kinetix con un logo vectorial inspirado en los atajos de iOS, distintivo de versión Pre-Alpha y control total de permisos transparentes por categoría.
- [x] **Identidad Visual Premium**: Creación de un ícono de lanzador adaptivo en capas (Gradients overlayed diamonds) de gran impacto estético con una versión de insignia roja de "A" (Pre-Alpha) y respaldo del original limpio.
- [x] **Información Educativa de Logs**: Inclusión de tarjetas informativas dentro de la Consola explicando explícitamente a los probadores que estos logs ayudan al creador a optimizar el rendimiento y resolver fallas del modelo específico de su teléfono.
- [x] **Copiado y Exportación de Logs Independiente**: Botones de "Copiar Log" y "Compartir / Exportar" mediante un Chooser del sistema, permitiendo enviar o almacenar los reportes de error con facilidad.
- [x] **Atribución y Licencias de Terceros**: Sección de "Atribuciones y Licencias" en Ajustes para créditos formalizados de recursos de audio de terceros (CC BY 4.0, CC0) con enlaces originales de Freesound.

---

## ⚡ Corto Plazo (Fase 2: Conectividad y Nuevas Acciones) 🚀
*Enfoque: Incrementar la utilidad práctica en el uso diario.*

- [x] **Acción: Ejecutar Fragmento JavaScript (QuickJS)**: Ejecución local súper rápida con paso de datos de variables.
- [ ] **Acción: Ejecutar Script Lua (LuaJ)**: Ofrecer ejecución segura y liviana de scripts Lua para usuarios avanzados.
- [ ] **Acción: Esperar Tiempo (Retraso)**: Agregar la acción "Delay / Sleep" para pausar el flujo de ejecución durante N segundos entre pasos.
- [ ] **Acción: Ejecutar URL / Llamada HTTP**: Permitir realizar peticiones GET/POST a URLs externas (útil para integraciones con Webhooks de domótica, IFTTT, o Make).
- [ ] **Acción: Estado de Conexión**: Consultar si el dispositivo está conectado a una red Wi-Fi específica o datos móviles para condicionar pasos.
- [ ] **Mejora del Editor**: Añadir la capacidad de reordenar acciones existentes en el editor arrastrándolas (Drag-and-Drop) en lugar de eliminarlas y recrearlas.
- [ ] **Validación de Tipos de Entrada**: Verificar si el resultado anterior es un número antes de intentar una operación matemática, arrojando advertencias visuales amigables si hay incompatibilidad de tipos.

---

## 📲 Mediano Plazo (Fase 3: Integración Profunda con Android) ⚙️
*Enfoque: Integrar la aplicación de manera nativa con el launcher y eventos del sistema.*

- [x] **Automatizaciones por Eventos del Sistema Avanzados**:
  - [x] **Batería Baja**: Permite al usuario configurar dinámicamente un porcentaje exacto de batería y condición de disparo (Igual, Menor, Mayor).
  - [x] **Conexión de Cargador**: Disparo nativo al conectar o desconectar la corriente.
  - [x] **Conexión de Auriculares**: Disparo al conectar o desconectar auriculares con cable.
  - [x] **Servicio en Primer Plano (Foreground Service)**: Ejecución persistente 24/7 mediante `KinetixForegroundService` para mantener activos los disparadores con la app cerrada.
  - [x] **Receptor de Arranque del Sistema**: Inicio automático en encendido del teléfono vía `ACTION_BOOT_COMPLETED`.
- [x] **Panel de Configuraciones**:
  - [x] **Bajar calidad gráfica**: Reducción dinámica de degradados y transparencias pesadas para optimizar el rendimiento.
  - [x] **Consola de Logs de Diagnóstico**: Acceso nativo y visual a los archivos de logs de warning y crashes persistidos localmente en `Android/data/`.
- [ ] **Widgets del Launcher**: Permitir a los usuarios crear accesos directos o botones rápidos en la pantalla de inicio de Android para ejecutar un atajo específico con un solo toque.
- [ ] **Automatizaciones por Eventos adicionales (Wi-Fi, Bluetooth, Alarma)**:
  - Ejecutar un atajo cuando el dispositivo se conecte a una red Wi-Fi o Bluetooth específica.
  - Ejecutar un atajo a una hora exacta todos los días (Alarm/Cron local).
- [ ] **Llamada a APIs de IA**: Integración segura de la Gemini API mediante variables de entorno para procesar, categorizar o resumir textos usando Inteligencia Artificial dentro de las acciones de un atajo.

---

## 🔮 Largo Plazo (Fase 4: Ecosistema y Compartición Avanzada) 🌌
*Enfoque: Creación de una comunidad y escalabilidad técnica.*

- [ ] **Exportación e Importación de Automatizaciones (Archivos JSON/YAML)**: Permitir a los usuarios exportar sus flujos automatizados favoritos en un archivo local o compartirlo mediante un código QR.
- [ ] **Galería Pública de Automatizaciones (Kinetix)**: Un repositorio en la nube donde los usuarios puedan subir y descargar recetas creadas por la comunidad (clasificadas por categorías: Productividad, Utilidades, Domótica).
- [ ] **NFC Tags**: Capacidad de vincular un atajo a una etiqueta física NFC para que se ejecute de inmediato al acercar el teléfono a la etiqueta.
- [ ] **Historial Detallado de Ejecuciones**: Guardar un log persistente e histórico de las ejecuciones anteriores para que el usuario audite cuándo, cómo y con qué resultado se ejecutaron sus automatizaciones de fondo.
