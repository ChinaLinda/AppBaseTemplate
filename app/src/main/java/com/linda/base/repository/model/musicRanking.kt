package com.linda.base.repository.model

/**
 * Created by Dell on 2019-08-16
 */
data class musicRanking(
    var code: Int,
    var message: String,
    var result: List<Result>
) {
    data class Result(
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
    ) {
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
    }
}