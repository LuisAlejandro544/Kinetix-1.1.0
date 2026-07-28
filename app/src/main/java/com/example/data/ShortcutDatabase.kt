package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Shortcut::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ShortcutDatabase : RoomDatabase() {
    abstract fun shortcutDao(): ShortcutDao

    companion object {
        @Volatile
        private var INSTANCE: ShortcutDatabase? = null

        fun getDatabase(context: Context): ShortcutDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShortcutDatabase::class.java,
                    "shortcuts_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
