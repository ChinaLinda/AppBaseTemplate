package com.linda.base.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.linda.base.repository.model.Result

@Database(entities = [Result::class], version = 1, exportSchema = false)
abstract class MusicRankingDatabase : RoomDatabase() {
    abstract fun musicRankingDao(): MusicRankingDao

    companion object {
        @Volatile
        private var INSTANCE: MusicRankingDatabase? = null

        fun getInstance(context: Context): MusicRankingDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MusicRankingDatabase::class.java, "music.db"
            ).build()
    }
}
