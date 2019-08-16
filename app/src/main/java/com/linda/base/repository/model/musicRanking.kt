package com.linda.base.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


data class musicRanking(
    var code: Int,
    var message: String,
    var result: List<Result>
)

@Entity(tableName = "musicRankingResult")
@TypeConverters(ContentTypeConverter::class)
data class Result(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var pic_s210: String,
    var bg_pic: String,
    var color: String,
    var pic_s444: String,
    var count: Int,
    var type: Int,
    var bg_color: String,
    var web_url: String,
    var name: String,
    var comment: String,
    var pic_s192: String,
    var pic_s260: String,
    var content: List<Content>
)

data class Content(
    var all_rate: String,
    var song_id: String,
    var rank_change: String,
    var biaoshi: String,
    var author: String,
    var album_id: String,
    var pic_small: String,
    var title: String,
    var pic_big: String,
    var album_title: String
)

class ContentTypeConverter {
    @TypeConverter
    fun fromContents(value: String): List<Content> {
        val listType = object : TypeToken<List<Content>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromContentValue(contents: List<Content>): String {
        val gson = Gson()
        return gson.toJson(contents)
    }
}
