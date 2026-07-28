package com.example.data

import kotlinx.coroutines.flow.Flow

class ShortcutRepository(private val shortcutDao: ShortcutDao) {
    val allShortcuts: Flow<List<Shortcut>> = shortcutDao.getAllShortcuts()

    fun getShortcutById(id: Int): Flow<Shortcut?> {
        return shortcutDao.getShortcutById(id)
    }

    suspend fun insertShortcut(shortcut: Shortcut): Long {
        return shortcutDao.insertShortcut(shortcut)
    }

    suspend fun updateShortcut(shortcut: Shortcut) {
        shortcutDao.updateShortcut(shortcut)
    }

    suspend fun deleteShortcut(shortcut: Shortcut) {
        shortcutDao.deleteShortcut(shortcut)
    }
}
