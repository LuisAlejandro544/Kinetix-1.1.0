package com.example.ui.components

import androidx.compose.runtime.Composable
import com.example.data.ActionType
import com.example.ui.screens.inputs.*

@Composable
fun DynamicActionInputs(
    type: ActionType,
    params: Map<String, String>,
    onParamsChanged: (Map<String, String>) -> Unit
) {
    when (type) {
        ActionType.SPEAK_TEXT -> SpeakTextInput(params, onParamsChanged)
        ActionType.SHOW_NOTIFICATION -> ShowNotificationInput(params, onParamsChanged)
        ActionType.VIBRATE -> VibrateInput(params, onParamsChanged)
        ActionType.OPEN_URL -> OpenUrlInput(params, onParamsChanged)
        ActionType.TEXT_INPUT -> TextInputFieldInput(params, onParamsChanged)
        ActionType.TEXT_TRANSFORM -> TextTransformInput(params, onParamsChanged)
        ActionType.ALERT_DIALOG -> AlertDialogInput(params, onParamsChanged)
        ActionType.SYSTEM_INFO -> SystemInfoInput(params, onParamsChanged)
        ActionType.MATH_OP -> MathOpInput(params, onParamsChanged)
        ActionType.SHARE_TEXT -> ShareTextInput(params, onParamsChanged)
        ActionType.CONDITIONAL -> ConditionalInput(params, onParamsChanged)
        ActionType.OPEN_APP -> OpenAppInput(params, onParamsChanged)
        ActionType.SET_VOLUME -> SetVolumeInput(params, onParamsChanged)
        ActionType.SET_RINGER_MODE -> SetRingerModeInput(params, onParamsChanged)
        ActionType.WRITE_FILE -> WriteFileInput(params, onParamsChanged)
        ActionType.READ_FILE -> ReadFileInput(params, onParamsChanged)
        ActionType.APPEND_FILE -> AppendFileInput(params, onParamsChanged)
        ActionType.SET_BRIGHTNESS -> SetBrightnessInput(params, onParamsChanged)
        ActionType.ACCESSIBILITY_ACTION -> AccessibilityActionInput(params, onParamsChanged)
        ActionType.PLAY_SOUND -> PlaySoundInput(params, onParamsChanged)
        ActionType.EXEC_JAVASCRIPT -> ExecJavascriptInput(params, onParamsChanged)
        ActionType.TERMUX_COMMAND -> TermuxCommandInput(params, onParamsChanged)
        ActionType.CUSTOM_CODE -> CustomCodeInput(params, onParamsChanged)
        ActionType.BACKGROUND_CAMERA_CAPTURE -> BackgroundCameraCaptureInput(params, onParamsChanged)
        ActionType.SIMULATE_GESTURES -> SimulateGesturesInput(params, onParamsChanged)
        ActionType.CLIPBOARD_SILENT -> ClipboardSilentInput(params, onParamsChanged)
        ActionType.HTTP_REQUEST -> HttpRequestInput(params, onParamsChanged)
    }
}
