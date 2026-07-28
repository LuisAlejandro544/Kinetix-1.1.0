package com.example.executor

import android.content.Context
import android.os.Build
import android.os.Process
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileLogManager {

    private inline fun <T> runOnLittleCore(block: () -> T): T {
        val tid = Process.myTid()
        val oldPriority = try { Process.getThreadPriority(tid) } catch (e: Exception) { Process.THREAD_PRIORITY_DEFAULT }
        try {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
        } catch (e: Exception) {}
        try {
            return block()
        } finally {
            try {
                Process.setThreadPriority(oldPriority)
            } catch (e: Exception) {}
        }
    }

    fun logCrash(context: Context, throwable: Throwable) {
        runOnLittleCore {
            try {
                val dir = context.getExternalFilesDir("crashes") ?: File(context.filesDir, "crashes")
                if (!dir.exists()) dir.mkdirs()
                
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val file = File(dir, "crash_$timestamp.txt")
                
                FileWriter(file).use { writer ->
                    val pw = PrintWriter(writer)
                    pw.println("Timestamp: " + SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()))
                    pw.println("Device: " + Build.MANUFACTURER + " " + Build.MODEL)
                    pw.println("Android OS: " + Build.VERSION.RELEASE + " (SDK " + Build.VERSION.SDK_INT + ")")
                    pw.println("----------------------------------------")
                    throwable.printStackTrace(pw)
                    pw.flush()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun logWarning(context: Context, tag: String, message: String) {
        runOnLittleCore {
            try {
                val dir = context.getExternalFilesDir("warnings") ?: File(context.filesDir, "warnings")
                if (!dir.exists()) dir.mkdirs()
                
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val file = File(dir, "warning_$timestamp.txt")
                
                FileWriter(file).use { writer ->
                    writer.write("Timestamp: " + SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()) + "\n")
                    writer.write("Tag: $tag\n")
                    writer.write("Message: $message\n")
                    writer.write("Device: " + Build.MANUFACTURER + " " + Build.MODEL + " (Android " + Build.VERSION.RELEASE + ")\n")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getCrashLogs(context: Context): List<FileLog> {
        return runOnLittleCore {
            val dir = context.getExternalFilesDir("crashes") ?: File(context.filesDir, "crashes")
            dir.listFiles()?.map { file ->
                FileLog(
                    name = file.name,
                    content = file.readText(),
                    file = file,
                    isCrash = true
                )
            }?.sortedByDescending { it.file.lastModified() } ?: emptyList()
        }
    }

    fun getWarningLogs(context: Context): List<FileLog> {
        return runOnLittleCore {
            val dir = context.getExternalFilesDir("warnings") ?: File(context.filesDir, "warnings")
            dir.listFiles()?.map { file ->
                FileLog(
                    name = file.name,
                    content = file.readText(),
                    file = file,
                    isCrash = false
                )
            }?.sortedByDescending { it.file.lastModified() } ?: emptyList()
        }
    }

    fun clearAllLogs(context: Context) {
        runOnLittleCore {
            try {
                context.getExternalFilesDir("crashes")?.listFiles()?.forEach { it.delete() }
                context.getExternalFilesDir("warnings")?.listFiles()?.forEach { it.delete() }
                File(context.filesDir, "crashes").listFiles()?.forEach { it.delete() }
                File(context.filesDir, "warnings").listFiles()?.forEach { it.delete() }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class FileLog(
    val name: String,
    val content: String,
    val file: File,
    val isCrash: Boolean
)
