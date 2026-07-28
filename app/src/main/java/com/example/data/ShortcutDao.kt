package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShortcutDao {
    @Query("SELECT * FROM shortcuts ORDER BY id DESC")
    fun getAllShortcuts(): Flow<List<Shortcut>>

    @Query("SELECT * FROM shortcuts ORDER BY id DESC")
    suspend fun getAllShortcutsList(): List<Shortcut>

    @Query("SELECT * FROM shortcuts WHERE id = :id")
    fun getShortcutById(id: Int): Flow<Shortcut?>

    @Query("SELECT * FROM shortcuts WHERE id = :id")
    suspend fun getShortcutByIdSync(id: Int): Shortcut?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShortcut(shortcut: Shortcut): Long

    @Update
    suspend fun updateShortcut(shortcut: Shortcut)

    @Delete
    suspend fun deleteShortcut(shortcut: Shortcut)
}
