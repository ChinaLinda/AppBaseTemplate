package com.linda.base.repository.database

import androidx.room.*
import com.linda.base.repository.model.Result

@Dao
abstract class MusicRankingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(musicRankingResult: Result)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(musicRankingResults: List<Result>)

    @Query("SELECT * FROM musicRankingResult")
    abstract fun getAllMusicRankingResult(): List<Result>

    @Delete
    abstract fun delete(musicRankingResult: Result)

    @Query("DELETE FROM musicRankingResult")
    abstract fun deleteAllMusicRaking()

}
