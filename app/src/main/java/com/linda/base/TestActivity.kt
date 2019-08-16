package com.linda.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.linda.base.repository.database.MusicRankingDatabase
import com.linda.base.repository.network.RetrofitFactory
import com.linda.base.repository.network.TestApi
import com.linda.base.utils.AppConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TestActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runBlocking {
            val results = RetrofitFactory.getRetrofitService(AppConfig.TEST_URL).create(TestApi::class.java)
                .musicRanking()
            launch(Dispatchers.IO) {
                MusicRankingDatabase.getInstance(this@TestActivity).musicRankingDao().deleteAllMusicRaking()
                MusicRankingDatabase.getInstance(this@TestActivity).musicRankingDao().insertAll(results.result)
                Log.i("ldh",MusicRankingDatabase.getInstance(this@TestActivity).musicRankingDao().getAllMusicRankingResult().size.toString())
            }
        }
    }

}